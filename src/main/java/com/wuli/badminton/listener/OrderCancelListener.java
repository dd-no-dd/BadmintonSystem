package com.wuli.badminton.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuli.badminton.config.RabbitMQConfig;
import com.wuli.badminton.enums.ReservationStatusEnum;
import com.wuli.badminton.pojo.MallOrder;
import com.wuli.badminton.service.MallOrderService;
import com.wuli.badminton.service.ReservationOrderService;
import com.wuli.badminton.vo.ReservationOrderVo;
import com.wuli.badminton.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 订单取消监听器
 * 处理延迟队列中的超时订单，支持商城订单和预约订单
 */
@Component
public class OrderCancelListener {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelListener.class);
    
    @Autowired
    private MallOrderService mallOrderService;
    
    @Autowired
    private ReservationOrderService reservationOrderService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 处理订单超时取消
     * 监听订单取消队列，处理从延迟队列转发过来的消息
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CANCEL)
    public void processOrderCancel(String message) {
        logger.info("【订单超时关单】收到取消消息: message={}", message);
        
        try {
            // 尝试解析为包含业务类型的JSON消息
            Map<String, Object> messageData = parseMessage(message);
            if (messageData != null) {
                String businessType = (String) messageData.get("businessType");
                String orderNo = String.valueOf(messageData.get("orderNo"));
                
                logger.info("【订单超时关单】解析消息成功: businessType={}, orderNo={}", businessType, orderNo);
                
                if ("MALL".equals(businessType)) {
                    processMallOrderCancel(Long.parseLong(orderNo));
                } else if ("RESERVATION".equals(businessType)) {
                    processReservationOrderCancel(orderNo);
                } else {
                    logger.warn("【订单超时关单】未知的业务类型: businessType={}, orderNo={}", businessType, orderNo);
                }
            } else {
                // 兼容旧格式：纯订单号（默认为商城订单）
                Long orderNo = parseOrderNo(message);
                if (orderNo != null) {
                    logger.info("【订单超时关单】使用兼容模式处理商城订单: orderNo={}", orderNo);
                    processMallOrderCancel(orderNo);
                }
            }
            
        } catch (Exception e) {
            logger.error("【订单超时关单】处理异常: message={}, error={}", message, e.getMessage(), e);
        }
    }
    
    /**
     * 处理商城订单超时取消
     */
    private void processMallOrderCancel(Long orderNo) {
        try {
            // 查询订单状态
            MallOrder order = mallOrderService.selectByOrderNo(orderNo);
            if (order == null) {
                logger.warn("【商城订单超时关单】订单不存在: orderNo={}", orderNo);
                return;
            }
            
            // 检查订单状态，只有未支付的订单才需要取消
            if (order.getStatus().equals(MallOrder.STATUS_UNPAID)) {
                logger.info("【商城订单超时关单】开始取消订单: orderNo={}, 当前状态={}", orderNo, order.getStatus());
                
                // 取消订单（cancelOrder方法会自动检测是否为系统调用）
                boolean success = mallOrderService.cancelOrder(orderNo);
                if (success) {
                    logger.info("【商城订单超时关单】订单取消成功: orderNo={}", orderNo);
                } else {
                    logger.error("【商城订单超时关单】订单取消失败: orderNo={}", orderNo);
                }
            } else {
                logger.info("【商城订单超时关单】订单无需取消: orderNo={}, 当前状态={}", orderNo, order.getStatus());
            }
        } catch (Exception e) {
            logger.error("【商城订单超时关单】处理异常: orderNo={}, error={}", orderNo, e.getMessage(), e);
        }
    }
    
    /**
     * 处理预约订单超时取消
     */
    private void processReservationOrderCancel(String orderNo) {
        try {
            // 查询预约订单
            ResponseVo<ReservationOrderVo> orderResponse = reservationOrderService.getOrderByNo(orderNo);
            if (orderResponse.getCode() != 0 || orderResponse.getData() == null) {
                logger.warn("【预约订单超时关单】订单不存在: orderNo={}", orderNo);
                return;
            }
            
            ReservationOrderVo orderVo = orderResponse.getData();
            Integer currentStatus = orderVo.getStatus();
            
            // 检查订单状态，只有待支付的订单才需要取消
            if (ReservationStatusEnum.PENDING_PAYMENT.getCode().equals(currentStatus)) {
                logger.info("【预约订单超时关单】开始取消订单: orderNo={}, 当前状态={}", orderNo, currentStatus);
                
                // 取消预约订单（这里模拟系统调用，使用订单中的用户ID）
                ResponseVo<String> cancelResponse = reservationOrderService.cancelOrder(orderVo.getUserId(), orderVo.getId(), "系统超时自动取消");
                if (cancelResponse.getCode() == 0) {
                    logger.info("【预约订单超时关单】订单取消成功: orderNo={}", orderNo);
                } else {
                    logger.error("【预约订单超时关单】订单取消失败: orderNo={}, reason={}", orderNo, cancelResponse.getMsg());
                }
            } else {
                logger.info("【预约订单超时关单】订单无需取消: orderNo={}, 当前状态={}", orderNo, currentStatus);
            }
        } catch (Exception e) {
            logger.error("【预约订单超时关单】处理异常: orderNo={}, error={}", orderNo, e.getMessage(), e);
        }
    }
    
    /**
     * 解析消息为Map格式
     */
    private Map<String, Object> parseMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }
        
        message = message.trim();
        
        // 尝试JSON格式解析
        if (message.startsWith("{") && message.endsWith("}")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
                // 验证必要字段
                if (messageMap.containsKey("businessType") && messageMap.containsKey("orderNo")) {
                    return messageMap;
                }
            } catch (Exception e) {
                logger.debug("【订单超时关单】JSON解析失败: message={}, error={}", message, e.getMessage());
            }
        }
        
        return null;
    }
    
    /**
     * 解析订单号，支持多种格式
     * @param message 消息内容
     * @return 订单号
     */
    private Long parseOrderNo(String message) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }
        
        message = message.trim();
        logger.debug("【订单超时关单】开始解析消息: message={}", message);
        
        // 优先尝试直接解析为数字（最常见的情况）
        try {
            Long orderNo = Long.parseLong(message);
            logger.info("【订单超时关单】数字解析成功: orderNo={}", orderNo);
            return orderNo;
        } catch (NumberFormatException e) {
            logger.debug("【订单超时关单】数字格式解析失败，尝试其他格式: message={}", message);
        }
        
        // 如果消息看起来像ASCII码序列，先尝试转换
        if (message.contains(",") && message.matches("[0-9,]+")) {
            try {
                String[] asciiCodes = message.split(",");
                StringBuilder sb = new StringBuilder();
                for (String code : asciiCodes) {
                    int ascii = Integer.parseInt(code.trim());
                    if (ascii >= 32 && ascii <= 126) { // 可打印ASCII字符范围
                        sb.append((char) ascii);
                    }
                }
                String decodedMessage = sb.toString();
                logger.info("【订单超时关单】ASCII解码结果: original={}, decoded={}", message, decodedMessage);
                
                // 递归调用解析解码后的消息
                return parseOrderNo(decodedMessage);
            } catch (Exception e) {
                logger.debug("【订单超时关单】ASCII解码失败: message={}", message);
            }
        }
        
        // 处理转义的JSON字符串（去掉外层引号和转义字符）
        if (message.startsWith("\"") && message.endsWith("\"")) {
            try {
                // 去掉外层引号
                String unquoted = message.substring(1, message.length() - 1);
                // 处理转义字符
                String unescaped = unquoted.replace("\\\"", "\"").replace("\\\\", "\\");
                logger.info("【订单超时关单】转义字符串处理: original={}, unescaped={}", message, unescaped);
                
                // 递归调用解析处理后的消息
                return parseOrderNo(unescaped);
            } catch (Exception e) {
                logger.debug("【订单超时关单】转义字符串处理失败: message={}", message);
            }
        }
        
        // 尝试JSON格式解析
        if (message.startsWith("{") && message.endsWith("}")) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
                Object orderNoObj = messageMap.get("orderNo");
                if (orderNoObj != null) {
                    if (orderNoObj instanceof Number) {
                        Long orderNo = ((Number) orderNoObj).longValue();
                        logger.info("【订单超时关单】JSON解析成功: orderNo={}", orderNo);
                        return orderNo;
                    } else {
                        Long orderNo = Long.parseLong(orderNoObj.toString());
                        logger.info("【订单超时关单】JSON解析成功: orderNo={}", orderNo);
                        return orderNo;
                    }
                }
            } catch (Exception e) {
                logger.warn("【订单超时关单】JSON格式解析失败: message={}, error={}", message, e.getMessage());
            }
        }
        
        logger.error("【订单超时关单】所有解析方法都失败: message={}", message);
        return null;
    }
} 