package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private Integer id;
    private String permissionName;
    private String permissionKey;
    private String permissionType; // MENU-菜单 BUTTON-按钮
    private String path; // 菜单路径
    private String component; // 组件路径
    private String icon; // 图标
    private Integer parentId; // 父级ID
    private Integer orderNum; // 排序
    private Integer status; // 0-禁用 1-启用
    private Date createdTime;
    private Date updatedTime;
}
