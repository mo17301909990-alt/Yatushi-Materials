package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
}
