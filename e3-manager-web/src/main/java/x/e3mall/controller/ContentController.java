package x.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.content.service.ContentService;
import x.e3mall.pojo.TbContent;

/**
 * @Author: hex1n
 * @Date: 2018/4/18 15:32
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;


    /**
     * 添加分类内容
     *
     * @param content
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public E3Result save(TbContent content) {

        E3Result result = contentService.save(content);

        return result;
    }


    /**
     * 分页展示内容列表
     *
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/query/list")
    @ResponseBody
    public DataGridResult contentList(long categoryId, Integer page, Integer rows) {

        DataGridResult dataGridResult = contentService.ContentList(categoryId, page, rows);

        return dataGridResult;
    }
}
