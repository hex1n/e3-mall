package x.e3mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.pojo.TreeNode;
import x.e3mall.content.service.ContentCategoryService;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/16 14:35
 */

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {


    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 内容分类展示
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<TreeNode> contentCatList(@RequestParam(value = "id", defaultValue = "0") long parentId) {

        List<TreeNode> treeNodes = contentCategoryService.getContentCatList(parentId);

        return treeNodes;
    }

    /**
     * 添加分类
     *
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public E3Result addContentCategory(long parentId, String name) {
        E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
        return e3Result;
    }

    /**
     * 修改分类信息
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public E3Result updateCategory(long id, String name) {

        E3Result result = contentCategoryService.updateCategory(name, id);

        return result;
    }

    /**
     * 删除分类信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public E3Result deleteCategory(long id) {

        E3Result result = contentCategoryService.deleteCategory(id);

        return result;
    }
}

