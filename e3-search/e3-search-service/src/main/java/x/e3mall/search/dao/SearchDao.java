package x.e3mall.search.dao;

import org.apache.solr.client.solrj.SolrQuery;
import x.e3mall.common.pojo.SearchResult;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 13:13
 */
public interface SearchDao {
    SearchResult search(SolrQuery query) throws Exception;

}
