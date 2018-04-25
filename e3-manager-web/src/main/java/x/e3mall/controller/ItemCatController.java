package x.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.TreeNode;
import x.e3mall.service.ItemCatService;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/13 10:40
 */

@Controller
public class ItemCatController {


    @Autowired
    private ItemCatService itemCatService;

    /**
     * 展示
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<TreeNode> getParentId(@RequestParam(value = "id", defaultValue = "0") long parentId) {
        List<TreeNode> nodes = itemCatService.getCatList(parentId);
        return nodes;
    }
}
