package com.skillswap.service;

import com.skillswap.dto.CreateRequestDto;
import com.skillswap.dto.DashboardStats;
import com.skillswap.dto.LearningRequestResponse;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.LearningRequest;
import com.skillswap.entity.LearningRequest.RequestStatus;
import com.skillswap.entity.LearningRequest.RequestType;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.exception.BadRequestException;
import com.skillswap.exception.UnauthorizedException;
import com.skillswap.repository.LearningRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LearningRequestService using JUnit 5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class LearningRequestServiceTest {

    @Mock
    private LearningRequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private LearningRequestService requestService;

    private User requester;
    private User receiver;
    private Skill javaSkill;
    private Skill sqlSkill;
    private LearningRequest mockExchangeRequest;

    @BeforeEach
    void setUp() {
        requester = User.builder().id(1L).name("Student A").email("a@college.edu").build();
        receiver = User.builder().id(2L).name("Student B").email("b@college.edu").build();

        javaSkill = Skill.builder().id(10L).name("Java").category("Programming").build();
        sqlSkill = Skill.builder().id(20L).name("SQL").category("Database").build();

        mockExchangeRequest = LearningRequest.builder()
                .id(100L)
                .requester(requester)
                .receiver(receiver)
                .requestType(RequestType.EXCHANGE)
                .skillToLearn(javaSkill)
                .skillToTeach(sqlSkill)
                .status(RequestStatus.PENDING)
                .message("Let's swap Java for SQL!")
                .build();
    }

    @Test
    @DisplayName("Should successfully create a skill exchange request")
    void testCreateExchangeRequest_Success() {
        CreateRequestDto dto = new CreateRequestDto();
        dto.setReceiverId(2L);
        dto.setRequestType(RequestType.EXCHANGE);
        dto.setSkillToLearnId(10L);
        dto.setSkillToTeachId(20L);
        dto.setMessage("Let's swap Java for SQL!");

        when(userService.findUserById(1L)).thenReturn(requester);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(skillService.findSkillById(10L)).thenReturn(javaSkill);
        when(skillService.findSkillById(20L)).thenReturn(sqlSkill);
        when(requestRepository.existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn(
                eq(1L), eq(2L), eq(10L), anyList())).thenReturn(false);
        when(requestRepository.save(any(LearningRequest.class))).thenReturn(mockExchangeRequest);

        LearningRequestResponse response = requestService.createRequest(1L, dto);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(RequestType.EXCHANGE, response.getRequestType());
        assertEquals(RequestStatus.PENDING, response.getStatus());
        verify(requestRepository, times(1)).save(any(LearningRequest.class));
    }

    @Test
    @DisplayName("Should successfully create a free mentoring request without a skillToTeach")
    void testCreateFreeMentoringRequest_Success() {
        CreateRequestDto dto = new CreateRequestDto();
        dto.setReceiverId(2L);
        dto.setRequestType(RequestType.FREE_MENTORING);
        dto.setSkillToLearnId(10L);
        dto.setSkillToTeachId(null); // No skill offered for mentoring
        dto.setMessage("Would love guidance on Java collections!");

        LearningRequest mentoringReq = LearningRequest.builder()
                .id(101L)
                .requester(requester)
                .receiver(receiver)
                .requestType(RequestType.FREE_MENTORING)
                .skillToLearn(javaSkill)
                .skillToTeach(null)
                .status(RequestStatus.PENDING)
                .build();

        when(userService.findUserById(1L)).thenReturn(requester);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(skillService.findSkillById(10L)).thenReturn(javaSkill);
        when(requestRepository.existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn(
                eq(1L), eq(2L), eq(10L), anyList())).thenReturn(false);
        when(requestRepository.save(any(LearningRequest.class))).thenReturn(mentoringReq);

        LearningRequestResponse response = requestService.createRequest(1L, dto);

        assertNotNull(response);
        assertEquals(RequestType.FREE_MENTORING, response.getRequestType());
        assertNull(response.getSkillToTeach());
    }

    @Test
    @DisplayName("Should prevent student from sending a request to themselves")
    void testCreateRequest_SelfRequest_ThrowsBadRequest() {
        CreateRequestDto dto = new CreateRequestDto();
        dto.setReceiverId(1L); // Same as requester
        dto.setRequestType(RequestType.EXCHANGE);
        dto.setSkillToLearnId(10L);

        when(userService.findUserById(1L)).thenReturn(requester);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            requestService.createRequest(1L, dto);
        });

        assertTrue(ex.getMessage().contains("cannot send a request to yourself"));
        verify(requestRepository, never()).save(any(LearningRequest.class));
    }

    @Test
    @DisplayName("Exchange request without skill to teach should throw BadRequestException")
    void testCreateExchangeRequest_MissingSkillToTeach_ThrowsBadRequest() {
        CreateRequestDto dto = new CreateRequestDto();
        dto.setReceiverId(2L);
        dto.setRequestType(RequestType.EXCHANGE);
        dto.setSkillToLearnId(10L);
        dto.setSkillToTeachId(null); // Missing!

        when(userService.findUserById(1L)).thenReturn(requester);
        when(userService.findUserById(2L)).thenReturn(receiver);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            requestService.createRequest(1L, dto);
        });

        assertTrue(ex.getMessage().contains("must specify a skill you can teach"));
    }

    @Test
    @DisplayName("Should prevent duplicate pending or accepted requests for the same skill")
    void testCreateRequest_Duplicate_ThrowsBadRequest() {
        CreateRequestDto dto = new CreateRequestDto();
        dto.setReceiverId(2L);
        dto.setRequestType(RequestType.EXCHANGE);
        dto.setSkillToLearnId(10L);
        dto.setSkillToTeachId(20L);

        when(userService.findUserById(1L)).thenReturn(requester);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(skillService.findSkillById(10L)).thenReturn(javaSkill);
        when(skillService.findSkillById(20L)).thenReturn(sqlSkill);
        when(requestRepository.existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn(
                eq(1L), eq(2L), eq(10L), anyList())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            requestService.createRequest(1L, dto);
        });

        verify(requestRepository, never()).save(any(LearningRequest.class));
    }

    @Test
    @DisplayName("Receiver can accept pending request, changing status to ACCEPTED")
    void testAcceptRequest_Success() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));
        when(requestRepository.save(any(LearningRequest.class))).thenAnswer(i -> i.getArgument(0));

        LearningRequestResponse response = requestService.acceptRequest(2L, 100L); // User 2 is receiver

        assertNotNull(response);
        assertEquals(RequestStatus.ACCEPTED, response.getStatus());
        verify(requestRepository).save(mockExchangeRequest);
    }

    @Test
    @DisplayName("Non-receiver cannot accept request and throws UnauthorizedException")
    void testAcceptRequest_Unauthorized() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));

        // User 3 is neither receiver nor participant
        assertThrows(UnauthorizedException.class, () -> {
            requestService.acceptRequest(3L, 100L);
        });

        // User 1 is requester, not receiver
        assertThrows(UnauthorizedException.class, () -> {
            requestService.acceptRequest(1L, 100L);
        });

        verify(requestRepository, never()).save(any(LearningRequest.class));
    }

    @Test
    @DisplayName("Rejecting a pending request should update status to REJECTED")
    void testRejectRequest_Success() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));
        when(requestRepository.save(any(LearningRequest.class))).thenAnswer(i -> i.getArgument(0));

        LearningRequestResponse response = requestService.rejectRequest(2L, 100L);

        assertEquals(RequestStatus.REJECTED, response.getStatus());
    }

    @Test
    @DisplayName("Participant can mark accepted session as COMPLETED")
    void testCompleteRequest_Success() {
        mockExchangeRequest.setStatus(RequestStatus.ACCEPTED);
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));
        when(requestRepository.save(any(LearningRequest.class))).thenAnswer(i -> i.getArgument(0));

        // Either participant (User 1 or User 2) can complete
        LearningRequestResponse response = requestService.completeRequest(1L, 100L);

        assertEquals(RequestStatus.COMPLETED, response.getStatus());
    }

    @Test
    @DisplayName("Completing a session that is still PENDING should throw BadRequestException")
    void testCompleteRequest_NotAccepted_ThrowsBadRequest() {
        mockExchangeRequest.setStatus(RequestStatus.PENDING);
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));

        assertThrows(BadRequestException.class, () -> {
            requestService.completeRequest(1L, 100L);
        });
    }

    @Test
    @DisplayName("Non-participant attempting to complete session throws UnauthorizedException")
    void testCompleteRequest_Unauthorized() {
        mockExchangeRequest.setStatus(RequestStatus.ACCEPTED);
        when(requestRepository.findById(100L)).thenReturn(Optional.of(mockExchangeRequest));

        assertThrows(UnauthorizedException.class, () -> {
            requestService.completeRequest(999L, 100L); // User 999 not involved
        });
    }

    @Test
    @DisplayName("Dashboard stats should aggregate metrics correctly")
    void testGetDashboardStats() {
        when(userService.findUserById(1L)).thenReturn(requester);
        UserResponse userResp = UserResponse.builder()
                .skillsCanTeach(List.of(UserResponse.SkillDto.builder().build()))
                .skillsWantToLearn(List.of(UserResponse.SkillDto.builder().build(), UserResponse.SkillDto.builder().build()))
                .build();
        when(userService.toUserResponseWithSkills(requester)).thenReturn(userResp);

        when(requestRepository.countByReceiverIdAndStatus(1L, RequestStatus.PENDING)).thenReturn(2L);
        when(requestRepository.countByRequesterIdAndStatus(1L, RequestStatus.PENDING)).thenReturn(1L);
        when(requestRepository.findByRequesterIdAndStatusOrReceiverIdAndStatus(
                1L, RequestStatus.ACCEPTED, 1L, RequestStatus.ACCEPTED))
                .thenReturn(List.of(mockExchangeRequest));
        when(requestRepository.findByRequesterIdAndStatusOrReceiverIdAndStatus(
                1L, RequestStatus.COMPLETED, 1L, RequestStatus.COMPLETED))
                .thenReturn(Collections.emptyList());

        DashboardStats stats = requestService.getDashboardStats(1L);

        assertNotNull(stats);
        assertEquals(1, stats.getTotalSkillsOffered());
        assertEquals(2, stats.getTotalSkillsWanted());
        assertEquals(2, stats.getPendingRequestsReceived());
        assertEquals(1, stats.getPendingRequestsSent());
        assertEquals(1, stats.getActiveExchanges());
        assertEquals(0, stats.getActiveMentoringSessions());
        assertEquals(0, stats.getCompletedSessions());
    }
}
