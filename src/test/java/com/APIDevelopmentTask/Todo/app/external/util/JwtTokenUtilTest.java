package com.APIDevelopmentTask.Todo.app.external.util;

import com.APIDevelopmentTask.Todo.app.application.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    public void testGetUsernameFromInvalidToken() {
        String invalidToken = "invalid.token.string";

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> {
            jwtTokenUtil.getUsernameFromToken(invalidToken);
        });

        assertEquals("Invalid JWT token", exception.getMessage());
    }
}
