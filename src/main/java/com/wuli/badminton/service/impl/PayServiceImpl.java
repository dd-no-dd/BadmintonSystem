package com.wuli.badminton.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.wuli.badminton.config.RabbitMQConfig;
import com.wuli.badminton.dao.PayInfoMapper;
import com.wuli.badminton.dto.PayNotifyMessage;
import com.wuli.badminton.enums.PayPlatFormEnum;
import com.wuli.badminton.pojo.PayInfo;
import com.wuli.badminton.service.MallOrderService;
import com.wuli.badminton.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付服务实现类
 */
@Service
public class PayServiceImpl implements PayService {
    
    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    
    @Autowired
    private BestPayService bestPayService;
    
    @Autowired
    private PayInfoMapper payInfoMapper;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MallOrderService mallOrderService;
    
    @Value("${pay.return-url}")
    private String returnUrl;
    
    // 业务类型常量
    public static final String BUSINESS_TYPE_MALL = "MALL";
    public static final String BUSINESS_TYPE_RESERVATION = "RESERVATION";
    
    /**
     * 查询支付状态
     */
    @Override
    public boolean checkPayStatus(String orderNo) {
        PayInfo payInfo = payInfoMapper.selectByOrderNo(orderNo);
        return payInfo != null && payInfo.getStatus() == 1; // 1表示已支付
    }
    
    /**
     * 创建支付（底层方法）
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum, String businessType) {
        // 使用 synchronized 防止同一订单的并发创建
        synchronized (("PAY_CREATE_" + orderId).intern()) {
            // 再次检查是否已经存在支付记录（双重检查锁定）
            PayInfo existingPayInfo = payInfoMapper.selectByOrderNo(orderId);
            if (existingPayInfo != null) {
                logger.info("订单{}已存在支付记录，直接返回支付信息", orderId);
                
                // 如果已经支付成功，抛出异常
                if (existingPayInfo.getStatus() == 1) {
                    throw new RuntimeException("订单已支付，无法重复创建支付");
                }
                
                // 如果未支付，重新创建支付请求
                PayRequest request = new PayRequest();
                request.setOrderName("BadmintonSystem订单");
                request.setOrderId(orderId);
                request.setOrderAmount(amount.doubleValue());
                request.setPayTypeEnum(bestPayTypeEnum);
                
                PayResponse response = bestPayService.pay(request);
                logger.info("重新生成支付响应: {}", response);
                return response;
            }
            
            // 写入数据库
            PayInfo payInfo = new PayInfo();
            payInfo.setOrderNo(orderId); // 直接使用字符串订单号
            payInfo.setPayPlatform(PayPlatFormEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode());
            payInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name());
            payInfo.setBusinessType(businessType);
            payInfo.setPayAmount(amount);
            payInfo.setStatus(0); // 未支付
            payInfo.setCreateTime(new Date());
            payInfo.setUpdateTime(new Date());
            
            try {
                payInfoMapper.insert(payInfo);
                logger.info("成功创建支付记录: orderNo={}", orderId);
            } catch (Exception e) {
                logger.error("创建支付记录失败: orderNo={}, error={}", orderId, e.getMessage());
                // 处理并发情况下的唯一约束冲突
                if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("uqe_order_no")) {
                    logger.warn("并发创建支付记录，订单号{}已存在，重新查询", orderId);
                    PayInfo concurrentPayInfo = payInfoMapper.selectByOrderNo(orderId);
                    if (concurrentPayInfo != null) {
                        if (concurrentPayInfo.getStatus() == 1) {
                            throw new RuntimeException("订单已支付，无法重复创建支付");
                        }
                        // 如果是未支付状态，重新生成支付请求
                        PayRequest request = new PayRequest();
                        request.setOrderName("BadmintonSystem订单");
                        request.setOrderId(orderId);
                        request.setOrderAmount(amount.doubleValue());
                        request.setPayTypeEnum(bestPayTypeEnum);
                        
                        PayResponse response = bestPayService.pay(request);
                        logger.info("并发处理-重新生成支付响应: {}", response);
                        return response;
                    }
                }
                // 其他异常直接抛出
                throw e;
            }
            
            // 创建支付请求
            PayRequest request = new PayRequest();
            request.setOrderName("BadmintonSystem订单");
            request.setOrderId(orderId);
            request.setOrderAmount(amount.doubleValue());
            request.setPayTypeEnum(bestPayTypeEnum);
            
            PayResponse response = bestPayService.pay(request);
            logger.info("支付响应: {}", response);
            
            return response;
        }
    }
    
    /**
     * 异步通知处理
     */
    @Override
    public String asyncNotify(String notifyData) {
        // 1.签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        logger.info("支付异步通知: {}", payResponse);
        
        // 2.金额校验(从数据库查订单)
        PayInfo payInfo = payInfoMapper.selectByOrderNo(payResponse.getOrderId());
        if (payInfo == null) {
            // 严重错误情况，发出警报
            throw new RuntimeException("通过orderNo查询到的结果为null");
        }
        
        // 如果订单支付状态不是成功
        if (payInfo.getStatus() != 1) {
            // 校验金额
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知中的金额和数据库的不一致,orderNo=" + payResponse.getOrderId());
            }
            
            // 3.修改订单状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfo.setStatus(1); // 已支付
            payInfo.setUpdateTime(new Date());
            payInfoMapper.updateByOrderNo(payInfo);
            
            // 4.根据业务类型更新订单状态
            String businessType = payInfo.getBusinessType();
            if (PayService.BUSINESS_TYPE_MALL.equals(businessType)) {
                // 商城订单：转换为Long类型
                Long mallOrderNo = Long.parseLong(payInfo.getOrderNo());
                mallOrderService.paySuccess(mallOrderNo);
            } else if (PayService.BUSINESS_TYPE_RESERVATION.equals(businessType)) {
                // 预约订单：保持字符串格式，这里不再调用具体业务，交给MQ处理
                logger.info("【支付结果通知】预约订单支付成功，等待MQ处理: orderNo={}", payInfo.getOrderNo());
            } else {
                logger.error("【支付结果通知】未知业务类型：{}", businessType);
            }
            
            // 5.发送MQ消息
            PayNotifyMessage message = new PayNotifyMessage();
            // 注意：PayNotifyMessage中的orderNo字段是Long类型，需要处理
            if (PayService.BUSINESS_TYPE_MALL.equals(businessType)) {
                message.setOrderNo(Long.parseLong(payInfo.getOrderNo()));
            } else if (PayService.BUSINESS_TYPE_RESERVATION.equals(businessType)) {
                // 预约订单：去掉RO前缀转换为Long
                String orderNoStr = payInfo.getOrderNo();
                if (orderNoStr.startsWith("RO")) {
                    message.setOrderNo(Long.parseLong(orderNoStr.substring(2)));
                } else {
                    message.setOrderNo(Long.parseLong(orderNoStr));
                }
            }
            message.setBusinessType(payInfo.getBusinessType());
            message.setPayPlatform(payInfo.getPayPlatform());
            message.setPlatformNumber(payInfo.getPlatformNumber());
            message.setPayAmount(payInfo.getPayAmount());
            
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PAY_NOTIFY, objectMapper.writeValueAsString(message));
                logger.info("支付成功消息已发送: {}", message);
            } catch (JsonProcessingException e) {
                logger.error("发送支付成功消息失败: {}", e.getMessage(), e);
            }
        }
        
        // 5.通知微信/支付宝成功接收，停止通知
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }
        
        throw new RuntimeException("异步通知中错误的支付平台");
    }
    
    /**
     * 创建支付（新接口）
     */
    @Override
    public PayResponse createPay(String orderNo, BigDecimal amount, String businessType) {
        //TODO: 加入支付宝
        return create(orderNo, amount, BestPayTypeEnum.WXPAY_NATIVE, businessType);
    }
    
    /**
     * 根据订单号查询支付信息（新接口）
     */
    @Override
    public PayInfo queryByOrderId(String orderNo) {
        return payInfoMapper.selectByOrderNo(orderNo);
    }
    
    /**
     * 获取前端支付成功跳转地址（新接口）
     */
    @Override
    public String getReturnUrl(String orderNo) {
        return returnUrl + "?orderNo=" + orderNo;
    }
}