package com.example.role.server.impl;


import com.example.role.commons.util.R;
import com.example.role.entity.SysPermission;
import com.example.role.entity.vo.PermissionVo;
import com.example.role.mapper.SysPermissionMapper;
import com.example.role.server.SysPermissionServer;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysPermissionServerImpl implements SysPermissionServer {
    @Resource
    public SysPermissionMapper sysPermissionMapper;

    public int insert(SysPermission sysPermission) {
        return sysPermissionMapper.insert(sysPermission);
    }

    public SysPermission findById(Integer id) {
        return sysPermissionMapper.findById(id);
    }

    public R findAll() {
        List<SysPermission> all = sysPermissionMapper.findAll();
        return R.ok(toThree(all));
    }

    public int update(SysPermission sysPermission) {
        return sysPermissionMapper.update(sysPermission);
    }

    public int delete(Integer id) {
        return sysPermissionMapper.delete(id);
    }

    private List<PermissionVo> toThree(List<SysPermission> list) {
        List<PermissionVo> permissionVos = list.stream().map(sysPermission -> {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyProperties(sysPermission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());
        List<PermissionVo> rootTree = new ArrayList<>();
        for (PermissionVo tree : permissionVos) {
            // 第一步 筛选出最顶级的父节点
            if (0 == tree.getPid()) {
                rootTree.add(tree);
            }
            // 第二步 筛选出该父节点下的所有子节点列表
            for (PermissionVo node : permissionVos) {
                if (node.getPid().equals(tree.getId())) {
                    if (CollectionUtils.isEmpty(tree.getChildren())) {
                        tree.setChildren(new ArrayList<>());
                    }
                    tree.getChildren().add(node);
                }
            }
        }
        return rootTree;
    }
}