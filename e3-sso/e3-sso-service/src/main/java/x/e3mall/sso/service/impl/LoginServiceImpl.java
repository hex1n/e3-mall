package x.e3mall.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import x.e3mall.common.jedis.JedisClient;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.utils.JsonUtils;
import x.e3mall.dao.TbUserMapper;
import x.e3mall.pojo.TbUser;
import x.e3mall.pojo.TbUserExample;
import x.e3mall.sso.service.LoginService;

import java.util.List;
import java.util.UUID;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 11:39
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public E3Result login(String username, String password) {

        //根据用户名查询用户信息
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if (tbUsers == null && tbUsers.size() == 0) {
            return E3Result.build(400, "用户名或密码错误");
        }
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUsers.get(0).getPassword())) {
            return E3Result.build(400, "用户名或密码错误");
        }
        //生成token
        String token = UUID.randomUUID().toString();
        //将密码设置为空,返回token到controller
        tbUsers.get(0).setPassword(null);
        //将用户信息保存到redis
        jedisClient.set("USER_INFO" + ":" + token, JsonUtils.objectToJson(tbUsers.get(0)));
        //key的过期时间 设置为30分钟
        jedisClient.expire("USER_INFO" + ":" + token, 1800);

        return E3Result.ok(token);
    }
}
