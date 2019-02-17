package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @program: mmall
 * @description: 购物车service
 * @author: snb
 * @create: 2019-02-06 21:24
 */
public interface ICartService {

    //购物车添加商品
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    //更新购物车某个产品数量
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    //移除购物车某个产品
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    //购物车List列表
    ServerResponse<CartVo> list(Integer userId);

    //全选或者全不选
    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId);

    //查询在购物车里的产品数量
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
