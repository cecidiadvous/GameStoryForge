package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.UserData;
import com.group10.gamestoryforge.dao.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDataRepository userDataRepository;

    public boolean isUsernameTaken(String username) {
        return userDataRepository.findByUsername(username) != null;
    }

    public UserData registerUser(String username, String password, String role) {
        if (isUsernameTaken(username)) {
            return null; // 如果用户名已存在，返回 null 表示注册失败
        }

        UserData user = new UserData();
        user.setUsername(username);
        user.setPassword(password); // 不加密，直接存储
        user.setRole(role);
        return userDataRepository.save(user);
    }

    public UserData loginUser(String username, String password) {
        UserData user = userDataRepository.findByUsername(username);
        // 验证明文密码是否匹配
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }
}
