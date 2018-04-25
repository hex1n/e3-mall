package x.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.dao.TbUserMapper;
import x.e3mall.pojo.TbUser;
import x.e3mall.pojo.TbUserExample;
import x.e3mall.sso.service.RegisterService;

import java.util.Date;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 10:05
 */
@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkDate(String param, int type) {
        /**
         * String param :要校验的数据
         * int type"校验的数据类型:1:用户名 2:手机号 3:邮箱
         * 相应数据json,e3Result封装的数据校验的结果true:成功,false:失败
         */

        /**
         * 业务逻辑:
         *  1.从tb_user表中查询数据
         *  2.查询条件根据参数动态生成
         *  3.判断查询条件,如果查询到数据返回false
         *  4.如果没有就返回true
         *  使用e3Result包装并返回
         */

        //校验
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneNotEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return E3Result.build(400, "非法参数");
        }

        //查询用户信息

        List<TbUser> users = tbUserMapper.selectByExample(example);
        if (users.size() == 0 && users == null) {
            //如果没有查到返回true
            return E3Result.ok(true);
        }
        return E3Result.ok(false);
    }


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    public E3Result register(TbUser user) {

        //检验数据
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
            return E3Result.build(400, "注册失败,用户信息不完整");
        }
        //检查数据
        E3Result e3Result = checkDate(user.getUsername(), 1);
        if (!(Boolean) e3Result.getData()) {
            return e3Result.build(400, "此用户名已被占用");
        }
        e3Result = checkDate(user.getPhone(), 2);
        if (!(Boolean) e3Result.getData()) {
            return e3Result.build(400, "此手机号已被使用");
        }
        //补全数据
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //使用MD5对密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //插入数据库
        tbUserMapper.insert(user);

        //返回添加成功
        return E3Result.ok();
    }
}
