package x.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.search.service.SearchItemService;

/**
 * @Author: hex1n
 * @Date: 2018/4/21 8:44
 */


/**
 * 导入索引库
 */
@Controller
public class SearchController {


    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemList() {
        E3Result result = searchItemService.importItemList();

        return result;
    }

}
