package com.it.yts_project.service.Impl;

import com.it.yts_project.constant.RoleConstants;
import com.it.yts_project.dto.UserDTO;
import com.it.yts_project.mapper.UserMapper;
import com.it.yts_project.model.User;
import com.it.yts_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        // 创建用户对象
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(userDTO.getEmail());

        // 保存用户
        userMapper.registerUser(user);

        // 返回包含用户ID的UserDTO
        UserDTO resultDTO = new UserDTO();
        resultDTO.setId(user.getId());
        resultDTO.setUsername(user.getUsername());
        resultDTO.setEmail(user.getEmail());
        // password 不返回，保持为 null
        return resultDTO;
    }

    @Override
    public UserDTO loginUser(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 返回包含用户ID的UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        // password 不返回，保持为 null
        return userDTO;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        userMapper.updateUser(user);
        return userMapper.getUserById(user.getId());
    }

    @Override
    public boolean deleteUser(Integer id) {
        return userMapper.deleteUser(id) > 0;
    }

    @Override
    public User createUser(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        
        // 设置默认值
        if (user.getIs_active() == null) {
            user.setIs_active(true);
        }
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public List<User> getAdminUsers() {
        // 使用全局配置的管理员角色列表
        return userMapper.getAdminUsers(RoleConstants.ADMIN_ROLE_KEYS);
    }
}
