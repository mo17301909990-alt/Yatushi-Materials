package com.it.yts_project.mapper;

import com.it.yts_project.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 登录
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 注册
     * @param user
     */
    void registerUser(User user);
    
    // 用户管理相关方法
    List<User> getAllUsers();
    User getUserById(@Param("id") Integer id);
    int updateUser(User user);
    int deleteUser(@Param("id") Integer id);
    int insertUser(User user);
    
    /**
     * 获取管理员用户列表
     * @param roleKeys 管理员角色key列表
     * @return 管理员用户列表
     */
    List<User> getAdminUsers(@Param("roleKeys") List<String> roleKeys);
}
