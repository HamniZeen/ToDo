package com.APIDevelopmentTask.Todo.app.application.controller;

import static org.junit.jupiter.api.Assertions.*;


import com.APIDevelopmentTask.Todo.app.domain.entity.User;
import com.APIDevelopmentTask.Todo.app.domain.service.impl.UserService;
import com.APIDevelopmentTask.Todo.app.external.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    void register_UserSuccessfullyRegistered_ReturnsSuccessMessage() {
        ResponseEntity<?> response = authController.register(user);

        assertEquals(ResponseEntity.ok("User registered successfully"), response);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void login_UserSuccessfullyLoggedIn_ReturnsToken() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), Collections.emptyList()
        );
        String token = "dummyToken";

        when(userService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        ResponseEntity<?> response = authController.login(user);

        assertEquals(ResponseEntity.ok(Collections.singletonMap("token", token)), response);
        verify(userService, times(1)).loadUserByUsername(user.getEmail());
        verify(jwtTokenUtil, times(1)).generateToken(userDetails);
    }

}
