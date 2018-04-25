package x.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.e3mall.common.pojo.SearchResult;
import x.e3mall.search.dao.SearchDao;
import x.e3mall.search.service.SearchService;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 12:52
 */

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private SolrServer solrServer;


    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        // 1）创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        // 2）设置查询条件
        //设置主查询条件
        query.setQuery(keyword);
        //设置分页条件
        query.setStart((page -1 ) * rows);
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", "item_keywords");
        //开启高亮
        query.setHighlight(true);
        //设置高亮显示的域
        query.addHighlightField("item_title");
        //高亮前缀
        query.setHighlightSimplePre("<em style=\"color:red\">");
        //高亮后缀
        query.setHighlightSimplePost("</em>");
        // 3）调用dao执行查询，得到一个SearchResult对象
        SearchResult searchResult = searchDao.search(query);
        // 4）根据总记录数计算总页数。需要每页显示的记录数。
        long recourdCount = searchResult.getRecourdCount();
        long pageCount  = recourdCount / rows;
        if (recourdCount % rows > 0) {
            pageCount++;
        }
        // 5）把总页数添加到SearchResult对象中。
        searchResult.setTotalPages(pageCount);
        // 6）返回SearchResult
        return searchResult;
    }

}
