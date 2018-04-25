package x.e3mall.cart.interceptor;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 10:49
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.CookieUtils;
import x.e3mall.pojo.TbUser;
import x.e3mall.sso.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //前处理,执行handler之前执行此方法
        //返回true:放行,false:拦截

        //1.从cookie中取token
        String token = CookieUtils.getCookieValue(request, "token");
        //2.如果没有token,未登录,直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //3.取到token,需要调用sso系统服务,根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);

        //4.没有取到用户信息,登录过期,直接放行
        if (e3Result.getStatus() != 200) {
            return true;
        }
        //5.取到用户信息,登录状态
        TbUser tbUser = (TbUser) e3Result.getData();
        //6.把用户信息放到request中,只需要早Controller中判断request中是否包含user信息,放行
        request.setAttribute("user", tbUser);

        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //handler执行之后,返回ModelAndView之前
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        //完成处理,返回ModelAndView之后
        //可以在此处理异常
    }
}
