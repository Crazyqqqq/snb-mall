package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: mmall
 * @description: 购物车Service实现类
 * @author: snb
 * @create: 2019-02-06 21:25
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        //根据productId和userId判断产品是否在购物车的数据表里
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //产品不在购物车里，需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(cartItem);
        } else {
            //产品已经在购物车里了
            //数量相加
            count = count + cart.getQuantity();
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
        List<String> productList=Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return this.list(userId);
    }

    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo=this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 选择或者不选择
     * @param userId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer checked,Integer productId){
        cartMapper.checkedOrUncheckedProduct(userId,checked,productId);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId==null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }


    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //在mmall_cart里看看，userId用户买了哪些东西，存放在cartList列表里
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        //购物车的总价，初始值为0
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //如果库存>购物车中的数量
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        //库存<购物车中的数量
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //需要更新购物车中该商品的数量
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);

                    //计算总价 该产品单价*购物车中该产品的数量
                    BigDecimal productTotalPrice = BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity());
                    cartProductVo.setProductTotalPrice(productTotalPrice);
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    //如果已经勾选，则增加到购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }

    /**
     * 判断购物车中的东西是否全选
     *
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }


}
