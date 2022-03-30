package com.ggf.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单实体对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 支付窗台
     */
    private String message;
}
