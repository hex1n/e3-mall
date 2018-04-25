package x.e3mall.content.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.pojo.TreeNode;
import x.e3mall.content.service.ContentCategoryService;
import x.e3mall.dao.TbContentCategoryMapper;
import x.e3mall.pojo.TbContentCategory;
import x.e3mall.pojo.TbContentCategoryExample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/16 14:26
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TreeNode> getContentCatList(long parentId) {

        //创建条件查询
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //根据条件查询
        List<TbContentCategory> categories = tbContentCategoryMapper.selectByExample(example);
        //创建TreeNode集合
        List<TreeNode> treeNodes = new ArrayList<>();
        //遍历集合
        for (TbContentCategory category : categories) {
            //创建TreeNodes封装tree中的属性
            TreeNode treeNode = new TreeNode();
            treeNode.setId(category.getId());
            treeNode.setText(category.getName());
            treeNode.setState(category.getIsParent() ? "closed" : "open");
            //将TreeNode添加到集合中
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {

        // 1、创建一个tb_content_category表对应的pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        // 2、设置对象的属性。
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //1(正常),2(删除)
        contentCategory.setStatus(1);
        //默认是1
        contentCategory.setSortOrder(1);
        //该类目是否为父类目，1为true，0为false',
        //新增节点isparent一定是false
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        // 3、把数据插入到数据库中。
        tbContentCategoryMapper.insert(contentCategory);
        // 4、判断父节点的isParent是否是true，如果不是应该改为true。
        //取父节点信息
        TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentNode.getIsParent()) {
            parentNode.setIsParent(true);
            //更新到数据库
            tbContentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        // 5、把pojo对象包装到E3Result中返回。
        return E3Result.ok(contentCategory);
    }

    @Override
    public E3Result updateCategory(String name, long id) {

        //创建内容分类
        TbContentCategory tbContentCategory = new TbContentCategory();
        //设置属性
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        //调用根据id修改分类方法
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);

        return E3Result.ok(tbContentCategory);
    }

    @Override
    public E3Result deleteCategory(long id) {

        //根据id查询内容分类
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        //如果该内容分类不是父节点
        if (!contentCategory.getIsParent()) {
            //删除该节点
            tbContentCategoryMapper.deleteByPrimaryKey(id);
        } else {
            //如果该节点是父节点
            //创建查询条件
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            //查询当前父节点的所有子节点
            List<TbContentCategory> categories = tbContentCategoryMapper.selectByExample(example);
            //如果该父节点下有子节点,则遍历删除所有子节点
            if (categories.size() > 0) {
                for (TbContentCategory category : categories) {
                    deleteCategory(category.getId());
                }
                //将父节点属性设置为false
                contentCategory.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
                deleteCategory(contentCategory.getId());
            } else {
                contentCategory.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
                deleteCategory(id);
            }

        }

        return E3Result.ok();
    }


}

