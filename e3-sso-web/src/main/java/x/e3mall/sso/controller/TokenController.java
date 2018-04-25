package x.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.sso.service.TokenService;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 12:42
 */

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /**
     * 方式一:
     * 通过token查询用户信息
     * 使用jsop解决ajax跨域问题
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {

        E3Result result = tokenService.getUserByToken(token);
        //是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            //把结果封装成一个js语句
            String strResult = callback + "(" + JsonUtils.objectToJson(result) + ");";
            return strResult;
        }
        return JsonUtils.objectToJson(result);
    }

    /**
     * 方式二:
     * 通过token查询用户信息
     * 使用jsop解决ajax跨域问题
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/token/{token}")
    @ResponseBody
    public Object getUserByToken1(@PathVariable String token, String callback) {

        E3Result result = tokenService.getUserByToken(token);
        //是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
