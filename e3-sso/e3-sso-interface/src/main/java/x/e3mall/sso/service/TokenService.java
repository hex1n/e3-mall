package x.e3mall.sso.service;

import x.e3mall.common.pojo.E3Result;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 12:34
 */
public interface TokenService {

    E3Result getUserByToken(String token);
}
