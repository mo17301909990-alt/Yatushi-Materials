package com.it.yts_project.service;

import com.it.yts_project.dto.UserDTO;
import com.it.yts_project.model.User;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO loginUser(String username, String password);
    
    // 用户管理相关方法
    List<User> getAllUsers();
    User getUserById(Integer id);
    User updateUser(User user);
    boolean deleteUser(Integer id);
    User createUser(User user);
    List<User> getAdminUsers();  // 获取管理员用户列表
}
