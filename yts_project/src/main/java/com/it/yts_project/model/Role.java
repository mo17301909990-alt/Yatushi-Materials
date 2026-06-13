package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer id;
    private String roleName;
    private String roleKey;
    private String description;
    private Integer status; // 0-禁用 1-启用
    private Date createdTime;
    private Date updatedTime;
}
