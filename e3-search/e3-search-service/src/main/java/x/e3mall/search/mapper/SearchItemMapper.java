package x.e3mall.search.mapper;

import x.e3mall.common.pojo.SearchItem;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/19 17:33
 */
public interface SearchItemMapper {

    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
