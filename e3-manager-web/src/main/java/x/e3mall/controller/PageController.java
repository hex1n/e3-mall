package x.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: hex1n
 * @Date: 2018/4/12 16:32
 */

@Controller
public class PageController {


    @RequestMapping("/")
    public String toIndex(){

        return "index";
    }

    /**
     * 页面跳转
     *
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }

}
