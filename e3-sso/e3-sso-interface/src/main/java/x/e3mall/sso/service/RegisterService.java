package x.e3mall.sso.service;

import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbUser;

/**
 * @Author: hex1n
 * @Date: 2018/4/24 9:58
 */
public interface RegisterService {

    E3Result checkDate(String param,int type);
    E3Result register(TbUser user);
}
