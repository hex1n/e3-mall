package x.e3mall.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import x.e3mall.content.service.ContentService;
import x.e3mall.pojo.TbContent;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/16 10:36
 */

@Controller
public class PageController {


    @Autowired
    private ContentService contentService;

    /**
     * 轮播图
     * categoryId
     */
    @Value("${SLIDE_SHOW_CATEGORY_ID}")
    private Long SLIDE_SHOW;


    @RequestMapping("/index")
    public String index(Model model) {

        List<TbContent> adlist = contentService.getContentList(SLIDE_SHOW);
        model.addAttribute("ad1List",adlist);

        return "index";
    }

}
