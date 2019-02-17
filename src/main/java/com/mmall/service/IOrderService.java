package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @program: mmall
 * @description:支付、订单的接口
 * @author: snb
 * @create: 2019-02-12 19:12
 */
public interface IOrderService {
    //支付
    ServerResponse pay(Long orderNo, Integer userId, String path);

    //支付宝回调
    ServerResponse alipayCallback(Map<String,String> params);

    //查询订单支付状态
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    //创建订单
    ServerResponse createOrder(Integer userId,Integer shippingId);

    //取消订单
    ServerResponse<String> cancel(Integer userId,Long orderNo);

    //获取购物车中已经选中的商品详情
    ServerResponse getOrderCartProduct(Integer userId);

    //订单详情detail
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    //订单List
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);



    //后台订单List
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    //后台订单详情detail
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    //后台按订单号查询
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    //后台订单发货
    ServerResponse<OrderVo> manageSendGoods(Long orderNo);
}
