package com.group10.gamestoryforge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        // 模拟用户注册逻辑，例如保存到数据库
        return ResponseEntity.ok("User registered with username: " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {
        // 模拟用户登录逻辑，例如验证用户名和密码
        if ("testuser".equals(user.getUsername()) && "password".equals(user.getPassword())) {
            return ResponseEntity.ok("Login successful for user: " + user.getUsername());
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}

class UserDTO {
    private String username;
    private String password;

    // Getters and setters...

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

