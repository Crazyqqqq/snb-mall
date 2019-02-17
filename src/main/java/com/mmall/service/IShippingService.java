package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @program: mmall
 * @description: 收货地址接口
 * @author: snb
 * @create: 2019-02-09 22:13
 */
public interface IShippingService {

    //添加地址
    ServerResponse add(Integer userId, Shipping shipping);

    //删除地址
    ServerResponse<String> del(Integer userId,Integer shippingId);

    //更新地址
    ServerResponse update(Integer userId,Shipping shipping);

    //查询地址
    ServerResponse<Shipping> select(Integer userId,Integer ShippingId);

    //地址列表
    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);

}
