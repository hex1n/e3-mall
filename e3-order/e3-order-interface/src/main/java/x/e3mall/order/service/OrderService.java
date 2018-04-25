package x.e3mall.order.service;

import x.e3mall.common.pojo.E3Result;
import x.e3mall.order.service.pojo.OrderInfo;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 20:05
 */
public interface OrderService {

    E3Result createOrder(OrderInfo orderInfo);
}
