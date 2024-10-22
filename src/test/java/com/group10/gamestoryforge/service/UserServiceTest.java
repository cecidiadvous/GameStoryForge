package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.UserDataRepository;
import com.group10.gamestoryforge.model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsUsernameTaken_UsernameExists() {
        // 模拟存在的用户
        String username = "testUser";
        when(userDataRepository.findByUsername(username)).thenReturn(new UserData());

        boolean result = userService.isUsernameTaken(username);

        assertTrue(result);
        verify(userDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsUsernameTaken_UsernameNotExists() {
        // 模拟不存在的用户
        String username = "newUser";
        when(userDataRepository.findByUsername(username)).thenReturn(null);

        boolean result = userService.isUsernameTaken(username);

        assertFalse(result);
        verify(userDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testRegisterUser_Success() {
        String username = "newUser";
        String password = "password123";
        String role = "USER";

        // 模拟用户名未被占用
        when(userDataRepository.findByUsername(username)).thenReturn(null);

        UserData savedUser = new UserData();
        savedUser.setUsername(username);
        savedUser.setPassword(password);
        savedUser.setRole(role);

        when(userDataRepository.save(any(UserData.class))).thenReturn(savedUser);

        UserData result = userService.registerUser(username, password, role);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(role, result.getRole());
        verify(userDataRepository, times(1)).findByUsername(username);
        verify(userDataRepository, times(1)).save(any(UserData.class));
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        String username = "existingUser";
        String password = "password123";
        String role = "USER";

        // 模拟用户名已被占用
        when(userDataRepository.findByUsername(username)).thenReturn(new UserData());

        UserData result = userService.registerUser(username, password, role);

        assertNull(result);
        verify(userDataRepository, times(1)).findByUsername(username);
        verify(userDataRepository, times(0)).save(any(UserData.class));
    }

    @Test
    void testLoginUser_Success() {
        String username = "testUser";
        String password = "password123";

        // 模拟数据库中存在该用户
        UserData user = new UserData();
        user.setUsername(username);
        user.setPassword(password);

        when(userDataRepository.findByUsername(username)).thenReturn(user);

        UserData result = userService.loginUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        verify(userDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String username = "testUser";
        String password = "wrongPassword";

        // 模拟数据库中存在该用户
        UserData user = new UserData();
        user.setUsername(username);
        user.setPassword("password123");

        when(userDataRepository.findByUsername(username)).thenReturn(user);

        UserData result = userService.loginUser(username, password);

        assertNull(result);
        verify(userDataRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoginUser_UserNotFound() {
        String username = "unknownUser";
        String password = "password123";

        // 模拟数据库中不存在该用户
        when(userDataRepository.findByUsername(username)).thenReturn(null);

        UserData result = userService.loginUser(username, password);

        assertNull(result);
        verify(userDataRepository, times(1)).findByUsername(username);
    }
}
