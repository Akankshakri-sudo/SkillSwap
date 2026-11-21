package com.skillswap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillswap.dto.LoginRequest;
import com.skillswap.dto.RegisterRequest;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.User;
import com.skillswap.exception.BadRequestException;
import com.skillswap.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration / Controller tests for AuthController using MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private RegisterRequest validRegisterReq;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        validRegisterReq = new RegisterRequest();
        validRegisterReq.setName("Sneha Rao");
        validRegisterReq.setEmail("sneha@college.edu");
        validRegisterReq.setPassword("strongPass123");
        validRegisterReq.setCollege("NIT Karnataka");

        userResponse = UserResponse.builder()
                .id(1L)
                .name("Sneha Rao")
                .email("sneha@college.edu")
                .college("NIT Karnataka")
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/register should return 201 Created on valid input")
    void testRegister_Success() throws Exception {
        when(userService.register(any(RegisterRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sneha Rao"))
                .andExpect(jsonPath("$.email").value("sneha@college.edu"));
    }

    @Test
    @DisplayName("POST /api/auth/register with invalid email should return 400 Bad Request")
    void testRegister_InvalidEmail_Returns400() throws Exception {
        validRegisterReq.setEmail("invalid-email-format");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @DisplayName("POST /api/auth/register with short password should return 400 Bad Request")
    void testRegister_ShortPassword_Returns400() throws Exception {
        validRegisterReq.setPassword("123"); // Less than 8 chars

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("POST /api/auth/register with duplicate email returns 400 with custom message")
    void testRegister_DuplicateEmail_Returns400() throws Exception {
        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new BadRequestException("An account with email 'sneha@college.edu' already exists."));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("An account with email 'sneha@college.edu' already exists."));
    }

    @Test
    @DisplayName("POST /api/auth/login with valid credentials returns 200 OK")
    void testLogin_Success() throws Exception {
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail("sneha@college.edu");
        loginReq.setPassword("strongPass123");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        User mockUser = User.builder().id(1L).email("sneha@college.edu").name("Sneha Rao").build();
        when(userService.findUserByEmail("sneha@college.edu")).thenReturn(mockUser);
        when(userService.toUserResponseWithSkills(mockUser)).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sneha@college.edu"));
    }

    @Test
    @DisplayName("POST /api/auth/login with bad credentials returns 401 Unauthorized")
    void testLogin_BadCredentials_Returns401() throws Exception {
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail("sneha@college.edu");
        loginReq.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }
}
