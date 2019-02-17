package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @program: mmall
 * @description:处理分类的接口
 * @author: snb
 * @create: 2019-01-30 21:41
 */
public interface ICategoryService {
    //添加品类
    ServerResponse addCategory(String categoryName, Integer parentId);

    //更新categoryName
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    //查询子节点的category信息，并且不递归，保持平级
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    //获取当前节点的id，并且递归查询它的子节点的id
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
