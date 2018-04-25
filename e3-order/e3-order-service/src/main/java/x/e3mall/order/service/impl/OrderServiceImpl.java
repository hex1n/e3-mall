package x.e3mall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.dao.TbItemMapper;
import x.e3mall.dao.TbOrderItemMapper;
import x.e3mall.dao.TbOrderMapper;
import x.e3mall.dao.TbOrderShippingMapper;
import x.e3mall.order.service.OrderService;
import x.e3mall.order.service.pojo.OrderInfo;
import x.e3mall.pojo.TbOrderItem;
import x.e3mall.pojo.TbOrderShipping;

import java.util.Date;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 20:06
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;


    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;


    @Override
    public E3Result createOrder(OrderInfo orderInfo) {

        // 1、接收表单的数据
        // 2、生成订单id
        if (!jedisClient.exists(ORDER_GEN_KEY)) {
            //设置初始值
            jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
        }
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        // 3、向订单表插入数据。
        tbOrderMapper.insert(orderInfo);
        // 4、向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            //生成明细
            Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
            orderItem.setId(orderItemId.toString());
            orderItem.setOrderId(orderId);
            //插入数据
            tbOrderItemMapper.insert(orderItem);
        }
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        tbOrderShippingMapper.insert(orderShipping);
        // 6、返回e3Result。

        return E3Result.ok(orderId);
    }
}
