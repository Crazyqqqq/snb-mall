package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @program: mmall
 * @description: 产品接口
 * @author: snb
 * @create: 2019-02-01 20:47
 */
public interface IProductService {

    //新增或更新产品
    ServerResponse saveOrUpdateProduct(Product product);

    //修改产品销售状态
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    //产品详情
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    //后台查询产品list
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    //后台产品搜索
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    //前台产品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    //前台产品搜索
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
