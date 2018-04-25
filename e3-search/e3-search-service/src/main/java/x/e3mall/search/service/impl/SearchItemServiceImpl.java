package x.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.pojo.SearchItem;
import x.e3mall.search.mapper.SearchItemMapper;
import x.e3mall.search.service.SearchItemService;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/19 19:43
 */

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;


    @Override
    public E3Result importItemList() {

        //查询全部数据

        try {
            List<SearchItem> itemList = searchItemMapper.getItemList();
            //导入索引库
            for (SearchItem item : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", item.getId());
                document.addField("item_title", item.getTitle());
                document.addField("item_sell_point", item.getSell_point());
                document.addField("item_price", item.getPrice());
                document.addField("item_image", item.getImage());
                document.addField("item_category_name", item.getCategory_name());
                // 写入索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            //返回成功
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "商品导入失败");
        }
    }
}
