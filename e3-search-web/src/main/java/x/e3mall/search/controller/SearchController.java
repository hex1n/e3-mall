package x.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import x.e3mall.common.pojo.SearchResult;
import x.e3mall.search.service.SearchService;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 11:39
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer PAGE_ROWS;

    @RequestMapping("/search")
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {

        //需要转码
        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        //调用service层查询商品信息
        SearchResult searchResult = searchService.search(keyword, page, PAGE_ROWS);
        //把结果传递给jsp页面
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("recourdCount", searchResult.getRecourdCount());
        model.addAttribute("page", page);
        model.addAttribute("itemList", searchResult.getItemList());
        //返回逻辑视图
        return "search";
    }
}
