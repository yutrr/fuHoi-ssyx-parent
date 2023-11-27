package com.xie.ssyx.acl.helper;

/**
 * @title: PermissionHelper
 * @Author Xie
 * @Date: 2023/9/15 21:44
 * @Version 1.0
 */


import com.xie.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 */
public class PermissionHelper {

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    public static List<Permission> build(List<Permission> treeNodes){
        List<Permission> trees=new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if (treeNode.getPid()==0){
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }


    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode, List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());
        for (Permission it : treeNodes) {
            if (treeNode.getId().longValue()==it.getPid().longValue()){
                Integer level = treeNode.getLevel() +1;
                it.setLevel(level);
                if (treeNode.getChildren()==null){
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }

        return treeNode;
    }
}
