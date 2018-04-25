package x.e3mall.order.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import x.e3mall.cart.service.CartService;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.CookieUtils;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbUser;
import x.e3mall.sso.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 15:23
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //判断用户是否存在
        if (StringUtils.isBlank(token)) {
            //如果token不存在,未登录状态,跳转sso系统登录,用户登录后跳转到当前请求的url
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }

        //如果token存在,需要调用sso系统服务,根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //如果取不到,用户登录已过期,需要登录
        if (e3Result.getStatus() != 200) {
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            //拦截
            return false;
        }
        //如果取到用户信息,是登录 状态,需要把用户信息写入request
        TbUser user = (TbUser) e3Result.getData();
        request.setAttribute("user", user);
        //判断cookie中是否有购物车数据,如果有就合并到客户端
        String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(jsonCartList)) {
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
        }
        //放行
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
