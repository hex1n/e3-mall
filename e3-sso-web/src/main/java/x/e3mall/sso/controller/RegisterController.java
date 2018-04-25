package x.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbUser;
import x.e3mall.sso.service.RegisterService;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 9:40
 */

/**
 * 注册功能Controller
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 展示注册页面
     *
     * @return
     */
    @RequestMapping("/page/register")
    public String showRegister() {

        return "register";
    }

    /**
     * 检查数据信息
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkDate(@PathVariable String param, @PathVariable Integer type) {

        E3Result e3Result = registerService.checkDate(param, type);

        return e3Result;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user) {

        E3Result result = registerService.register(user);
        return result;
    }
}
