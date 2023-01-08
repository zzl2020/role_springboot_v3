package com.example.role.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVo {
    private Integer id;
    private String name;
    private Integer pid;
    private List<PermissionVo> children;
}
