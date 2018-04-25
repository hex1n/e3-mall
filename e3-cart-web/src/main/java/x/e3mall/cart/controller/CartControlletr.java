package x.e3mall.cart.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.cart.service.CartService;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.CookieUtils;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbUser;
import x.e3mall.service.ItemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 19:32
 */


/**
 * 购物车处理
 */
@Controller
public class CartControlletr {


    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    /**
     * 未登录情况下的购物车
     *
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //将购物车存到redis
        if (user != null) {
            //保存到服务端
            cartService.addCart(user.getId(), itemId, num);

            //返回成功页面
            return "cartSuccess";
        }

        //从cookie中取购物车列表
        boolean flag = false;
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品在商品列表中是否存在
        for (TbItem tbItem : cartList) {
            ////如果存在数量相加
            if (tbItem.getId().longValue() == itemId) {
                flag = true;
                //找到商品,数量相加
                tbItem.setNum(tbItem.getNum() + num);
                //跳出循环
                break;
            }
        }

        //如果不存在,根据商品id查询商品信息,得到一个TbItem对象
        if (!flag) {
            TbItem tbItem = itemService.getItemById(itemId);
            //设置商品购买数量
            tbItem.setNum(num);
            //取一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                tbItem.setImage(image.split(",")[0]);
            }
            //把商品添加到商品列表
            cartList.add(tbItem);

        }

        //把购物车商品列表写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回成功页面
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表的处理
     *
     * @param request
     * @return
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断json是否为空
        if (StringUtils.isBlank(json)) {
            //如果为空,返回一个空集合
            return new ArrayList<>();
        }
        //把json转换成一个商品列表
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }


    /**
     * 展示购物车列表
     *
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态
        if (user != null) {
            //从Cookie中取购物列表
            List<TbItem> cartList = getCartListFromCookie(request);
            //如果不为空,把Cookie中的购物车商品和服务端的购物车商品合并
            cartService.mergeCart(user.getId(), cartList);
            //把Cookie中的购物车删除
            CookieUtils.deleteCookie(request, response, "cart");
            //从服务端取购物车列表
            List<TbItem> list = cartService.getCartList(user.getId());
            //返回逻辑视图
            return "cart";

        }
        //未登录状态
        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        request.setAttribute("cartList", cartList);
        //返回逻辑视图

        return "cart";
    }


    /**
     * 更新购物车列表
     */
    @RequestMapping("cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletResponse response, HttpServletRequest request) {
        //判断是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //更新服务端的购物车
            cartService.updateCartItemNum(user.getId(), itemId, num);
            return E3Result.ok();
        }

        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历商品列表找到对应商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //更新数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        return E3Result.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //删除服务端的购物车商品
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }

        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历购物车列表,找到要删除商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId) {
                //删除商品
                cartList.remove(tbItem);
                //跳出循环
                break;
            }
        }
        //把购物车放回cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }


}
