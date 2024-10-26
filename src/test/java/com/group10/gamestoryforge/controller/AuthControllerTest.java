package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.UserData;
import com.group10.gamestoryforge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success_WithDefaultRole() {
        UserData userData = new UserData();
        userData.setUsername("testUser");
        userData.setPassword("password");
        userData.setRole(null);

        when(userService.registerUser(eq("testUser"), eq("password"), eq("USER"))).thenReturn(userData);

        ResponseEntity<Map<String, String>> response = authController.register(userData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered with username: testUser", response.getBody().get("message"));
    }

    @Test
    void testRegisterUser_Success_WithCustomRole() {
        UserData userData = new UserData();
        userData.setUsername("adminUser");
        userData.setPassword("password");
        userData.setRole("ADMIN");

        when(userService.registerUser(eq("adminUser"), eq("password"), eq("ADMIN"))).thenReturn(userData);

        ResponseEntity<Map<String, String>> response = authController.register(userData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered with username: adminUser", response.getBody().get("message"));
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        UserData userData = new UserData();
        userData.setUsername("testUser");
        userData.setPassword("password");
        userData.setRole(null);

        when(userService.registerUser(eq("testUser"), eq("password"), eq("USER"))).thenReturn(null);

        ResponseEntity<Map<String, String>> response = authController.register(userData);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists", response.getBody().get("message"));
    }

    @Test
    void testLoginUser_Success() {
        UserData userData = new UserData();
        userData.setUsername("testUser");
        userData.setPassword("password");
        userData.setRole(null);
        when(userService.loginUser(eq("testUser"), eq("password"))).thenReturn(userData);

        ResponseEntity<Map<String, String>> response = authController.login(userData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful for user: testUser", response.getBody().get("message"));
        assertEquals("testUser", response.getBody().get("username"));
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        UserData userData = new UserData();
        userData.setUsername("testUser");
        userData.setPassword("wrongPassword");
        userData.setRole(null);
        when(userService.loginUser(eq("testUser"), eq("wrongPassword"))).thenReturn(null);

        ResponseEntity<Map<String, String>> response = authController.login(userData);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody().get("message"));
    }
}

