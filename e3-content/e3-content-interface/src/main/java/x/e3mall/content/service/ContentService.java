package x.e3mall.content.service;

import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbContent;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/18 15:27
 */
public interface ContentService {

    E3Result save(TbContent tbContent);

    DataGridResult ContentList(long categoryId, Integer page , Integer rows);

    List<TbContent> getContentList(long categoryId);
}
