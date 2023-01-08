package com.example.role.entity.dto;


import lombok.Data;


import jakarta.validation.constraints.NotNull;
import java.util.List;
@Data
public class RoleDto {
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
    private List<Integer> permissionIds;
}
