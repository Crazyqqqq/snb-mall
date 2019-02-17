package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @program: mmall
 * @description: 收货地址实现类
 * @author: snb
 * @create: 2019-02-09 22:14
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount=shippingMapper.insert(shipping);
        if(rowCount>0){
            Map result=Maps.newHashMap();
            result.put("shippingId",shipping.getId());//新增地址的主键id通过shipping.getId()可以获得
            return ServerResponse.createBySuccess("新建地址成功",result);//需要给前端返回新建地址的id
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> del(Integer userId,Integer shippingId){
        //为了防止横向越权，这个方法不能用
        //int resultCount=shippingMapper.deleteByPrimaryKey(userId);
        int resultCount=shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);//为了防止横向越权，需要把登陆用户的id设置成shipping的id
        int rowCount=shippingMapper.updateByShipping(shipping);
        if(rowCount>0){
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId,Integer ShippingId){
        Shipping shipping=shippingMapper.selectByShippingIdUserId(userId,ShippingId);
        if(shipping==null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
        if(CollectionUtils.isEmpty(shippingList)){
            return ServerResponse.createByErrorMessage("未查询到地址");
        }
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
