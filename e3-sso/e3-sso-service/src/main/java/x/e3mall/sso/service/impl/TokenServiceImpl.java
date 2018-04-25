package x.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.pojo.TbUser;
import x.e3mall.sso.service.TokenService;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 12:35
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result getUserByToken(String token) {
        //在redis中查询token
        String json = jedisClient.get("USER_INFO" + ":" + token);
        if (StringUtils.isBlank(json)) {
            //如果查不到数据,返回用户已过期
            return E3Result.build(222, "用户信息已过期,请重新登录");
        }
        //如果查到数据,说明用户已登录需要重新设置过期时间
        jedisClient.expire("USER_INFO" + ":" + token, 1800);
        //把json数据转化为TbUser对象返回
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);

        return E3Result.ok(tbUser);
    }
}
