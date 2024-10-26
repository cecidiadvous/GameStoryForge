package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.UserData;
import com.group10.gamestoryforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserData user) {

        if (user.getRole() == null) {
            user.setRole("USER"); // 默认角色
        }

        UserData registeredUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getRole());
        Map<String, String> response = new HashMap<>();

        if (registeredUser != null) {
            response.put("message", "User registered with username: " + registeredUser.getUsername());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserData user) {
        UserData authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {

            // 创建一个 Map 来保存返回的数据
            Map<String, String> response = new HashMap<>();
            response.put("username", authenticatedUser.getUsername());
            response.put("message", "Login successful for user: " + authenticatedUser.getUsername());

            return ResponseEntity.ok(response); // 返回 JSON 对象
        } else {
            // 返回错误消息的 JSON 对象
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");

            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}


