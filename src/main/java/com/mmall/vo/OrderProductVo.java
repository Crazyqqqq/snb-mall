package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: mmall
 * @description: 订单商品信息VO类
 * @author: snb
 * @create: 2019-02-14 22:59
 */
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private String imageHost;
    private BigDecimal productTotalPrice;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
