package com.example.role.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
private Integer uid;
private List<Integer> rids;
}
