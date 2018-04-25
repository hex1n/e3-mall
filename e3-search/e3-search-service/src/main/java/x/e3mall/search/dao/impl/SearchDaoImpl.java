package x.e3mall.search.dao.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import x.e3mall.common.pojo.SearchItem;
import x.e3mall.common.pojo.SearchResult;
import x.e3mall.search.dao.SearchDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 10:02
 */


/**
 * 查询索引库
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        // 1）执行查询
        QueryResponse queryResponse = solrServer.query(query);
        // 2）取查询结果的总记录数
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        long numFound = solrDocumentList.getNumFound();
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<SearchItem> itemList = new ArrayList<>();
        // 3）取商品列表封装成SearchItem列表
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(title);
            //把商品对象添加到列表
            itemList.add(searchItem);
        }
        // 4）把结果封装到SearchResult对象中
        SearchResult result = new SearchResult();
        result.setRecourdCount(numFound);
        result.setItemList(itemList);
        // 5）返回SearchResult
        return result;
    }
}
