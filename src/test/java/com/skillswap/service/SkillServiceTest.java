package com.skillswap.service;

import com.skillswap.dto.AddUserSkillRequest;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkill;
import com.skillswap.entity.UserSkill.SkillType;
import com.skillswap.exception.BadRequestException;
import com.skillswap.exception.ResourceNotFoundException;
import com.skillswap.exception.UnauthorizedException;
import com.skillswap.repository.SkillRepository;
import com.skillswap.repository.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SkillService using JUnit 5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserSkillRepository userSkillRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SkillService skillService;

    private User mockUser;
    private Skill mockSkill;
    private UserSkill mockUserSkill;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .name("Priya Sharma")
                .email("priya@university.edu")
                .build();

        mockSkill = Skill.builder()
                .id(10L)
                .name("Java")
                .category("Programming")
                .build();

        mockUserSkill = UserSkill.builder()
                .id(100L)
                .user(mockUser)
                .skill(mockSkill)
                .skillType(SkillType.CAN_TEACH)
                .build();
    }

    @Test
    @DisplayName("addUserSkill should succeed when skill exists and is not duplicated")
    void testAddUserSkill_Success() {
        AddUserSkillRequest req = new AddUserSkillRequest();
        req.setSkillId(10L);
        req.setSkillType(SkillType.CAN_TEACH);

        when(userService.findUserById(1L)).thenReturn(mockUser);
        when(skillRepository.findById(10L)).thenReturn(Optional.of(mockSkill));
        when(userSkillRepository.existsByUserIdAndSkillIdAndSkillType(1L, 10L, SkillType.CAN_TEACH)).thenReturn(false);
        when(userSkillRepository.save(any(UserSkill.class))).thenReturn(mockUserSkill);

        UserResponse expectedResponse = UserResponse.builder().id(1L).name("Priya Sharma").build();
        when(userService.toUserResponseWithSkills(mockUser)).thenReturn(expectedResponse);

        UserResponse result = skillService.addUserSkill(1L, req);

        assertNotNull(result);
        verify(userSkillRepository, times(1)).save(any(UserSkill.class));
    }

    @Test
    @DisplayName("addUserSkill should throw BadRequestException when user already added this skill and type")
    void testAddUserSkill_Duplicate_ThrowsBadRequest() {
        AddUserSkillRequest req = new AddUserSkillRequest();
        req.setSkillId(10L);
        req.setSkillType(SkillType.CAN_TEACH);

        when(userService.findUserById(1L)).thenReturn(mockUser);
        when(skillRepository.findById(10L)).thenReturn(Optional.of(mockSkill));
        when(userSkillRepository.existsByUserIdAndSkillIdAndSkillType(1L, 10L, SkillType.CAN_TEACH)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            skillService.addUserSkill(1L, req);
        });

        verify(userSkillRepository, never()).save(any(UserSkill.class));
    }

    @Test
    @DisplayName("removeUserSkill should delete when the requesting user owns the skill")
    void testRemoveUserSkill_Success() {
        when(userSkillRepository.findById(100L)).thenReturn(Optional.of(mockUserSkill));

        assertDoesNotThrow(() -> skillService.removeUserSkill(1L, 100L));
        verify(userSkillRepository, times(1)).delete(mockUserSkill);
    }

    @Test
    @DisplayName("removeUserSkill should throw UnauthorizedException when another user attempts deletion")
    void testRemoveUserSkill_Unauthorized() {
        when(userSkillRepository.findById(100L)).thenReturn(Optional.of(mockUserSkill));

        assertThrows(UnauthorizedException.class, () -> {
            skillService.removeUserSkill(2L, 100L); // User 2 is not owner
        });

        verify(userSkillRepository, never()).delete(any(UserSkill.class));
    }

    @Test
    @DisplayName("createSkill should save new skill when name does not already exist")
    void testCreateSkill_Success() {
        when(skillRepository.existsByNameIgnoreCase("Kotlin")).thenReturn(false);
        when(skillRepository.save(any(Skill.class))).thenReturn(
                Skill.builder().id(11L).name("Kotlin").category("Mobile").build());

        Skill result = skillService.createSkill("Kotlin", "Mobile");

        assertNotNull(result);
        assertEquals("Kotlin", result.getName());
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    @DisplayName("createSkill should throw BadRequestException when skill name already exists")
    void testCreateSkill_DuplicateName() {
        when(skillRepository.existsByNameIgnoreCase("Java")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            skillService.createSkill("Java", "Programming");
        });
        verify(skillRepository, never()).save(any(Skill.class));
    }
}
