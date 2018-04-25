package x.e3mall.service;


import x.e3mall.common.pojo.TreeNode;

import java.util.List;


/**
 * @Author: hex1n
 * @Date: 2018/4/13 10:30
 */
public interface ItemCatService {

     List<TreeNode> getCatList(long parentId);
}
