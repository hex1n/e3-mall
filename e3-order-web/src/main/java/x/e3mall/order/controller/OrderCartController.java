package x.e3mall.order.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import x.e3mall.cart.service.CartService;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.order.service.OrderService;
import x.e3mall.order.service.pojo.OrderInfo;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 15:14
 */

@Controller
public class OrderCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;


    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request) {

        //取用户id
        TbUser user = (TbUser) request.getAttribute("user");
        //根据用户id取收货地址列表
        //使用静态数据
        //根据用户id取购物车列表
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);
        //返回页面
        return "order-cart";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        //接收表单提交的数据OrderInfo
        //补全用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用service创建订单
        E3Result result = orderService.createOrder(orderInfo);
        //取单号
        String orderId = result.getData().toString();
        //需要service返回订单号
        request.setAttribute("orderId", orderId);
        request.setAttribute("payment", orderInfo.getPayment());
        // 当前日期加三天
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
        //返回逻辑视图展示成功页面
        return "success";
    }
}
