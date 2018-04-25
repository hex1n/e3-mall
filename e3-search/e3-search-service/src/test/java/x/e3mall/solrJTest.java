package x.e3mall;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: hex1n
 * @Date: 2018/4/19 19:57
 */
public class solrJTest {


    /**
     * 添加文档
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void addDocument() throws IOException, SolrServerException {
        // 第一步:把solrJ的jar包添加到工程中
        // 第二步:创建一个SolrServer,使用HttpSolerServer创建对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        // 第三步:创建一个文档对象SolrInputDocument对象
        SolrInputDocument document = new SolrInputDocument();
        //第四步:向文档中添加域,必须有id域,域的名称必须在schema.xml中定义
        document.addField("id", "test01");
        document.addField("item_title", "测试商品");
        document.addField("item_price", "199");
        //第五步:把文档添加到索引库中
        solrServer.add(document);
        //第六步.提交
        solrServer.commit();
    }

    /**
     * 删除文档
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void deleteDocument() throws IOException, SolrServerException {
        // 第一步:创建一个SolrServer对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        // 第二步,调用SolrServer对象的根据id删除方法
        solrServer.deleteById("test01");
        // 第三步:提交
        solrServer.commit();
    }

    /**
     * 根据查询删除
     */

    @Test
    public void deleteDocumentByQuery() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        solrServer.deleteByQuery("title:change.me");
        solrServer.commit();

    }


    /**
     * 简单查询
     *
     * @throws SolrServerException
     */
    @Test
    public void queryDocument() throws SolrServerException {
        // 第一步:创建一个SolrServer对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        // 第二步:创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        // 第三步:向SolrQuery中添加查询条件
        query.setQuery("*:*");
        // 第四步:执行查询.得到一个Respone对象
        QueryResponse response = solrServer.query(query);
        // 第五步:取查询结果
        SolrDocumentList results = response.getResults();
        System.out.println("查询结果的总记录数:" + results.getNumFound());
        // 第六步:遍历并打印结果
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("item_title"));
            System.out.println(result.get("item_price"));
        }

    }

    /**
     * 带高亮显示查询
     *
     * @throws SolrServerException
     */
    @Test
    public void queyDocumentWithHighLighting() throws SolrServerException {
        // 第一步:创建一个SolrServer对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        // 第二步:创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        // 第三步:向SolrQuery中添加查询条件,过滤条件
        query.setQuery("测试");
        // 3.1:指定默认搜索域
        query.set("df", "item_keywords");
        // 3.2:开启高亮显示
        query.setHighlight(true);
        // 3.3:高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        // 第四步:执行查询,得到一个Response对象
        QueryResponse response = solrServer.query(query);
        // 第五步:取查询结果
        SolrDocumentList results = response.getResults();
        System.out.println("查询的总记录数为:" + results.getNumFound());
        // 遍历结果
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(result.get("id")).get("item_title");
            String itemTitle = null;
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) result.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(result.get("item_price"));
        }
    }


    //使用solrJ实现条件查询查询
    @Test
    public void queryDocumentTest() throws SolrServerException {

        //创建一个SolrServer对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8080/solr");
        //创建一个查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.set("q", "手机");
        //设置分页条件
        query.setRows(2);
        query.setStart(1);
        //开启高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //设置默认搜索域
        query.set("df", "item_title");
        //执行查询,得到一个response对象
        QueryResponse response = solrServer.query(query);
        //获取查询结果
        SolrDocumentList documentList = response.getResults();
        //获取总数
        System.out.println("查询的总记录数为:" + documentList.getNumFound());
        //获取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // 遍历所有结果
        for (SolrDocument document : documentList) {
            System.out.println(document.get("id"));
            //去高亮后的结果
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = (String) document.get(0);
            } else {
                title = (String) document.get("item_title");
            }
            System.out.println(title);
            System.out.println(document.get("item_sell_point"));
            System.out.println(document.get("item_price"));
            System.out.println(document.get("item_image"));
            System.out.println(document.get("item_category_name"));
        }
    }


    /**
     * 使用solrJ进行集群管理
     */
    @Test
    public void solrCloudAddDocument() throws IOException, SolrServerException {
        //第一步:把solrJ相关的jar包添加到工程中
        //第二步:创建一个SolrServer对象,需要使用CloudSolrServer子类,构造方法的参数是zookeeper的地址列表
        //参数是zookeeper的地址列表,使用逗号分隔
        CloudSolrServer solrServer = new CloudSolrServer("http://192.168.25.128:2181,192.168.25.128:2181,192.168.25.128:2183");
        //第三步:需要设置DefaultCollection属性
        solrServer.setDefaultCollection("collection2");
        //第四步:创建SolrInputDocument对象
        SolrInputDocument document = new SolrInputDocument();
        //第五步:向文档对象中添加域
        document.addField("item_title", "测试商品");
        document.addField("item_price", "100");
        document.addField("id", "test0001");
        //第六步:把文档对象写入索引库
        solrServer.add(document);
        //第七步:提交
        solrServer.commit();

    }
}
