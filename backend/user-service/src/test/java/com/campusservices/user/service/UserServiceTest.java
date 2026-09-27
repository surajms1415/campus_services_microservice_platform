package com.campusservices.user.service;

import com.campusservices.user.dto.AuthRequest;
import com.campusservices.user.dto.AuthResponse;
import com.campusservices.user.dto.RegisterRequest;
import com.campusservices.user.entity.User;
import com.campusservices.user.repository.UserRepository;
import com.campusservices.user.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private UserService userService;

    @Test
    void register_Success() {
        // Arrange
        RegisterRequest req = new RegisterRequest();
        req.setName("Test User");
        req.setEmail("test@test.com");
        req.setPassword("password");
        
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(jwtUtil.generateToken(any(), any(), any())).thenReturn("mock-token");
        
        // Act
        AuthResponse res = userService.register(req);
        
        // Assert
        assertNotNull(res);
        assertEquals("mock-token", res.getToken());
        assertEquals("test@test.com", res.getUser().getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        // Arrange
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@test.com");
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(new User()));
        
        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.register(req));
        assertEquals("Email already in use", ex.getMessage());
    }

    @Test
    void login_Success() {
        // Arrange
        AuthRequest req = new AuthRequest();
        req.setEmail("test@test.com");
        req.setPassword("password");
        
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setRole(User.Role.STUDENT);
        
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(), any(), any())).thenReturn("mock-token");
        
        // Act
        AuthResponse res = userService.login(req);
        
        // Assert
        assertNotNull(res);
        assertEquals("mock-token", res.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
