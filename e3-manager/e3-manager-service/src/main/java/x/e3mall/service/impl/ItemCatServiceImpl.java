package x.e3mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import x.e3mall.common.pojo.TreeNode;
import x.e3mall.dao.TbItemCatMapper;
import x.e3mall.pojo.TbItemCat;
import x.e3mall.pojo.TbItemCatExample;
import x.e3mall.service.ItemCatService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/13 10:31
 */

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {


    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TreeNode> getCatList(long parentId) {

        //设置条件 根据parentId查询节点列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //调用dao
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);
        //创建List
        List<TreeNode> list = new ArrayList<>();

        //遍历tbItemCats
        for (TbItemCat tbItemCat : tbItemCats) {
            TreeNode treeNode = new TreeNode();
            //设置属性
            treeNode.setId(tbItemCat.getId());
            treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            treeNode.setText(tbItemCat.getName());
            //将treeNode添加到集合中
            list.add(treeNode);
        }

        return list;
    }
}
