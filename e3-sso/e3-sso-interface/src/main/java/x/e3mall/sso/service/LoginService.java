package x.e3mall.sso.service;

import x.e3mall.common.pojo.E3Result;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 11:38
 */
public interface LoginService {

    E3Result login(String username, String password);
}
