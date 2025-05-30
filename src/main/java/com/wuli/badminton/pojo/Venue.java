package com.wuli.badminton.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 场地实体类
 */
@Data
public class Venue {
    
    /**
     * 场地ID
     */
    private Integer id;
    
    /**
     * 场地名称
     */
    private String name;
    
    /**
     * 场地描述
     */
    private String description;
    
    /**
     * 场地位置
     */
    private String location;
    
    /**
     * 每小时价格
     */
    private BigDecimal pricePerHour;
    
    /**
     * 场地类型：1-羽毛球场
     */
    private Integer type;
    
    /**
     * 场地基础状态：0-未启用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
} 