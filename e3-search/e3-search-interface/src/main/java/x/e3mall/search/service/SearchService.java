package x.e3mall.search.service;

import x.e3mall.common.pojo.SearchResult;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 12:52
 */
public interface SearchService {

    SearchResult search(String keyWords , int page , int rows) throws Exception;
}
