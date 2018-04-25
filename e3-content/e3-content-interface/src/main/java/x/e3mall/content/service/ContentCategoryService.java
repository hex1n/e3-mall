package x.e3mall.content.service;

import x.e3mall.common.pojo.E3Result;
import x.e3mall.common.pojo.TreeNode;

import java.util.List;

/**
 * @Author: hex1n
 * @Date: 2018/4/16 13:58
 */
public interface ContentCategoryService {


    List<TreeNode> getContentCatList(long parentId);


    E3Result updateCategory(String name, long id);

    E3Result deleteCategory(long id);

    E3Result addContentCategory(long parentId, String name);
}
