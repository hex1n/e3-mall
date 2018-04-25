package x.e3mall.cart.service;

import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbItem;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/25 12:17
 */
public interface CartService {

    E3Result addCart(Long userId, Long itemId, int num);

    E3Result mergeCart(Long userId, List<TbItem> itemList);

    List<TbItem> getCartList(Long userId);

    E3Result updateCartItemNum(Long userId, Long itemId, Integer num);

    E3Result deleteCartItem(Long userId, Long itemId);
}
