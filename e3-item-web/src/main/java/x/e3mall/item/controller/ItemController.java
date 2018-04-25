package x.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import x.e3mall.item.pojo.Item;
import x.e3mall.pojo.TbItem;
import x.e3mall.pojo.TbItemDesc;
import x.e3mall.service.ItemService;

/**
 * @Author: hex1n
 * @Date: 2018/4/23 14:12
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {

        //调用服务取商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        //取商品描述信息
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //把信息传递给页面
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        //返回逻辑视图
        return "/WEB-INF/jsp/item.ftl";
    }
}
