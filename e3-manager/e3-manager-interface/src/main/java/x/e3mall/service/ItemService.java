package x.e3mall.service;

import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbItemDesc;

/**
 * @Author: hex1n
 * @Date: 4/10/2018 3:17 PM
 */

public interface ItemService {

    TbItem getItemById(Long id);

    DataGridResult getItemList(Integer page, Integer rows);

    E3Result addItem(TbItem tbItem, String desc);

    E3Result deleteItems(Long id);

    E3Result itemShelves(long id);

    E3Result itemReshelf(long id);

    E3Result itemEdit(TbItem tbItem, String desc);

    E3Result findItemDescById(long id);

    TbItemDesc getItemDescById(long itemId);

}
