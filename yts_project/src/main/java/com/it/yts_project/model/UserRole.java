package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Date createdTime;
}
