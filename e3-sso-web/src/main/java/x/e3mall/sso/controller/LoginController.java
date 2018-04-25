package x.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.CookieUtils;
import x.e3mall.sso.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 10:54
 */

/**
 * 用户登录处理
 */
@Controller
public class LoginController {


    @Autowired
    private LoginService loginService;

    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;

    /**
     * 展示登录页面
     *
     * @return
     */
    @RequestMapping("/page/login")
    public String showLogin(String redirectUrl, Model model) {

        model.addAttribute("redirect", redirectUrl);

        return "login";
    }

    //登录
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        //调用service获取token
        E3Result e3Result = loginService.login(username, password);
        //从返回结果中取token,写入cookie,cookie要跨域
        String token = e3Result.getData().toString();
        CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
        //响应数据json,e3result,其中包含Token
        return e3Result;
    }


}
