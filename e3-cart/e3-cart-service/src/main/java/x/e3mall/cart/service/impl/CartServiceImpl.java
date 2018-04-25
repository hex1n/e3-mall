package x.e3mall.cart.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.e3mall.cart.service.CartService;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.dao.TbItemMapper;
import x.e3mall.pojo.TbItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 12:20
 */
@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Override
    public E3Result addCart(Long userId, Long itemId, int num) {
        // 向redis中添加购物车
        // 数据类型是hash  key:用户id  field:商品id value:商品信息
        Boolean hset = jedisClient.hexists("CART" + ":" + userId, itemId + "");
        // 判断商品是否存在
        if (hset) {
            // 如果存在,商品数量相加
            String json = jedisClient.hget("CART" + ":" + userId, itemId + "");
            //把json转换成java对象
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
            //写入redis
            jedisClient.hset("CART" + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
            //返回添加成功
            return E3Result.ok();

        }
        // 如果不存在,根据商品id取商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);

        //设置购物车数量
        tbItem.setNum(num);
        //取一张图片
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        // 添加到购物车列表
        jedisClient.hset("CART" + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        // 返回成功
        return E3Result.ok();

    }


    /**
     * 购物车合并
     *
     * @param userId
     * @param itemList
     * @return
     */
    @Override
    public E3Result mergeCart(Long userId, List<TbItem> itemList) {
        // 遍历商品列表
        // 把列表添加到购物车
        // 判断购物车中是否有此商品
        // 如果有,数量相加
        // 如果没有添加新的商品
        for (TbItem tbItem : itemList) {
            //调用addCart方法
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        // 返回成功
        return E3Result.ok();
    }

    /**
     * 取购物车列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<TbItem> getCartList(Long userId) {
        //根据用户id查询购物车列表
        List<String> jsonList = jedisClient.hvals("CART" + ":" + userId);
        ArrayList<TbItem> itemList = new ArrayList<>();
        for (String s : jsonList) {
            //创建一个TbItem对象
            TbItem item = JsonUtils.jsonToPojo(s, TbItem.class);
            //添加到列表
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public E3Result updateCartItemNum(Long userId, Long itemId, Integer num) {

        //从redis中取商品信息
        String json = jedisClient.hget("CART" + ":" + userId, itemId + "");
        //转换成java对象
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        //更新商品数量
        tbItem.setNum(num);
        //写入redis
        jedisClient.hset("CART" + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));

        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {

        //根据商品id删除redis中对应的商品数量
        jedisClient.hdel("CART" + ":" + userId, itemId + "");
        return E3Result.ok();
    }
}
