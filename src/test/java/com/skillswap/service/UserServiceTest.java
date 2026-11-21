package com.skillswap.service;

import com.skillswap.dto.RegisterRequest;
import com.skillswap.dto.UpdateProfileRequest;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.User;
import com.skillswap.exception.BadRequestException;
import com.skillswap.exception.ResourceNotFoundException;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService using JUnit 5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSkillRepository userSkillRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Aarav Patel");
        registerRequest.setEmail("aarav@university.edu");
        registerRequest.setPassword("securePassword123");
        registerRequest.setCollege("IIT Bombay");
        registerRequest.setDepartment("Computer Science");
        registerRequest.setYearOfStudy(3);

        mockUser = User.builder()
                .id(1L)
                .name("Aarav Patel")
                .email("aarav@university.edu")
                .password("$2a$10$hashedPassword")
                .college("IIT Bombay")
                .department("Computer Science")
                .yearOfStudy(3)
                .offersFreeMentoring(false)
                .build();
    }

    @Test
    @DisplayName("User registration should succeed when email is not taken")
    void testRegister_Success() {
        when(userRepository.existsByEmail("aarav@university.edu")).thenReturn(false);
        when(passwordEncoder.encode("securePassword123")).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("aarav@university.edu", response.getEmail());
        assertEquals("Aarav Patel", response.getName());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("securePassword123");
    }

    @Test
    @DisplayName("User registration should throw BadRequestException when email already exists")
    void testRegister_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail("aarav@university.edu")).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            userService.register(registerRequest);
        });

        assertTrue(ex.getMessage().contains("already exists"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("getUserById should return user response with skills")
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Aarav Patel", response.getName());
        assertEquals(0, response.getSkillsCanTeach().size());
        assertEquals(0, response.getSkillsWantToLearn().size());
    }

    @Test
    @DisplayName("getUserById should throw ResourceNotFoundException when user is not found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }

    @Test
    @DisplayName("updateProfile should update user attributes and return updated response")
    void testUpdateProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        UpdateProfileRequest updateReq = new UpdateProfileRequest();
        updateReq.setBio("Passionate coder eager to learn SQL and teach Java.");
        updateReq.setOffersFreeMentoring(true);

        UserResponse response = userService.updateProfile(1L, updateReq);

        assertNotNull(response);
        verify(userRepository).save(mockUser);
        assertEquals("Passionate coder eager to learn SQL and teach Java.", mockUser.getBio());
        assertTrue(mockUser.getOffersFreeMentoring());
    }

    @Test
    @DisplayName("discoverStudents should return paginated list of students excluding self")
    void testDiscoverStudents() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> userList = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(userList, pageable, 1);

        when(userRepository.discoverStudents(2L, "Java", null, pageable)).thenReturn(mockPage);
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        Page<UserResponse> result = userService.discoverStudents(2L, "Java", null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Aarav Patel", result.getContent().get(0).getName());
    }
}
