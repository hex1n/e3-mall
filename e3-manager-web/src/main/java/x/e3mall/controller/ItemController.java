package x.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import x.e3mall.common.pojo.DataGridResult;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.pojo.TbItem;
import x.e3mall.service.ItemService;

/**
 * @Author: hex1n
 * @Date: 4/10/2018 3:20 PM
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    private TbItem getTbItemById(@PathVariable long itemId) {
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    /**
     * 分页查询所有商品
     * http://localhost:8081/item/list
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public DataGridResult getTbItemList(Integer page, Integer rows) {

        DataGridResult result = itemService.getItemList(page, rows);

        return result;
    }

    /**
     * 添加商品
     *
     * @param tbItem
     * @param desc
     * @return
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem, String desc) {
        E3Result result = itemService.addItem(tbItem, desc);

        return result;
    }

    /**
     * 商品下架
     *
     * @return
     */
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public E3Result itemInstock(String ids) {
        String[] idss = ids.split(",");
        E3Result result = null;
        for (String id : idss) {
            result = itemService.itemShelves(Long.parseLong(id));
        }
        return result;
    }

    /**
     * 商品上架
     *
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public E3Result itemReshelf(String ids) {
        String[] idss = ids.split(",");
        E3Result result = null;
        for (String id : idss) {
            result = itemService.itemReshelf(Long.parseLong(id));
        }
        return result;
    }

    /**
     * 删除商品
     *
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result itemReshelf(long[] ids) {
        E3Result result = null;
        for (long id : ids) {
            result = itemService.deleteItems(id);
        }
        return result;
    }


    /**
     * 回显商品描述
     *
     * @param id
     * @return
     */
    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public E3Result findItemDesc(@PathVariable("id") long id) {

        E3Result result = itemService.findItemDescById(id);
        return result;
    }

    /**
     * 跳转到修改商品页面
     *
     * @return
     */
    @RequestMapping("/rest/page/item-edit")
    public String itemToEdit() {

        return "item-edit";
    }

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public E3Result itemEdit(TbItem tbItem, String desc) {

        E3Result result = itemService.itemEdit(tbItem, desc);

        return result;
    }


}
