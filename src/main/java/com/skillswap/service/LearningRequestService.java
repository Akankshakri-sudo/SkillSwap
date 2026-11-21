package com.skillswap.service;

import com.skillswap.dto.CreateRequestDto;
import com.skillswap.dto.DashboardStats;
import com.skillswap.dto.LearningRequestResponse;
import com.skillswap.entity.LearningRequest;
import com.skillswap.entity.LearningRequest.RequestStatus;
import com.skillswap.entity.LearningRequest.RequestType;
import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.exception.BadRequestException;
import com.skillswap.exception.ResourceNotFoundException;
import com.skillswap.exception.UnauthorizedException;
import com.skillswap.repository.LearningRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LearningRequestService {

    private static final Logger log = LoggerFactory.getLogger(LearningRequestService.class);

    private final LearningRequestRepository requestRepository;
    private final UserService userService;
    private final SkillService skillService;

    public LearningRequestService(LearningRequestRepository requestRepository,
                                  UserService userService,
                                  SkillService skillService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.skillService = skillService;
    }

    @Transactional
    public LearningRequestResponse createRequest(Long requesterId, CreateRequestDto dto) {
        User requester = userService.findUserById(requesterId);
        User receiver = userService.findUserById(dto.getReceiverId());

        if (requesterId.equals(dto.getReceiverId())) {
            throw new BadRequestException("You cannot send a request to yourself.");
        }

        if (dto.getRequestType() == RequestType.EXCHANGE && dto.getSkillToTeachId() == null) {
            throw new BadRequestException(
                "For skill exchange, you must specify a skill you can teach in return.");
        }

        Skill skillToLearn = skillService.findSkillById(dto.getSkillToLearnId());
        Skill skillToTeach = null;
        if (dto.getSkillToTeachId() != null) {
            skillToTeach = skillService.findSkillById(dto.getSkillToTeachId());
        }

        boolean duplicateExists = requestRepository
                .existsByRequesterIdAndReceiverIdAndSkillToLearnIdAndStatusIn(
                    requesterId,
                    dto.getReceiverId(),
                    dto.getSkillToLearnId(),
                    List.of(RequestStatus.PENDING, RequestStatus.ACCEPTED)
                );
        if (duplicateExists) {
            throw new BadRequestException(
                "You already have an active request to learn '" + skillToLearn.getName() +
                "' from this student.");
        }

        LearningRequest request = LearningRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .requestType(dto.getRequestType())
                .skillToLearn(skillToLearn)
                .skillToTeach(skillToTeach)
                .message(dto.getMessage())
                .status(RequestStatus.PENDING)
                .build();

        LearningRequest saved = requestRepository.save(request);
        log.info("Created {} request (ID:{}) from user {} to user {}",
                dto.getRequestType(), saved.getId(), requesterId, dto.getReceiverId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LearningRequestResponse> getReceivedRequests(Long userId) {
        return requestRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LearningRequestResponse> getSentRequests(Long userId) {
        return requestRepository.findByRequesterIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<LearningRequestResponse> getActiveRequests(Long userId) {
        return requestRepository
                .findByRequesterIdAndStatusOrReceiverIdAndStatus(
                    userId, RequestStatus.ACCEPTED,
                    userId, RequestStatus.ACCEPTED)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public LearningRequestResponse acceptRequest(Long receiverId, Long requestId) {
        LearningRequest request = findRequestById(requestId);

        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new UnauthorizedException("Only the request recipient can accept it.");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Only pending requests can be accepted. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.ACCEPTED);
        log.info("Request {} accepted by user {}", requestId, receiverId);
        return toResponse(requestRepository.save(request));
    }

    @Transactional
    public LearningRequestResponse rejectRequest(Long receiverId, Long requestId) {
        LearningRequest request = findRequestById(requestId);

        if (!request.getReceiver().getId().equals(receiverId)) {
            throw new UnauthorizedException("Only the request recipient can reject it.");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Only pending requests can be rejected. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.REJECTED);
        log.info("Request {} rejected by user {}", requestId, receiverId);
        return toResponse(requestRepository.save(request));
    }

    @Transactional
    public LearningRequestResponse completeRequest(Long userId, Long requestId) {
        LearningRequest request = findRequestById(requestId);

        boolean isParticipant = request.getRequester().getId().equals(userId)
                || request.getReceiver().getId().equals(userId);
        if (!isParticipant) {
            throw new UnauthorizedException("Only participants can mark a session as completed.");
        }

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new BadRequestException("Only accepted requests can be marked as completed. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.COMPLETED);
        log.info("Request {} marked completed by user {}", requestId, userId);
        return toResponse(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats(Long userId) {
        User user = userService.findUserById(userId);

        long skillsOffered = userService.toUserResponseWithSkills(user)
                .getSkillsCanTeach().size();
        long skillsWanted = userService.toUserResponseWithSkills(user)
                .getSkillsWantToLearn().size();

        long pendingReceived = requestRepository
                .countByReceiverIdAndStatus(userId, RequestStatus.PENDING);
        long pendingSent = requestRepository
                .countByRequesterIdAndStatus(userId, RequestStatus.PENDING);

        long activeExchanges = requestRepository
                .findByRequesterIdAndStatusOrReceiverIdAndStatus(
                    userId, RequestStatus.ACCEPTED, userId, RequestStatus.ACCEPTED)
                .stream()
                .filter(r -> r.getRequestType() == RequestType.EXCHANGE)
                .count();

        long activeMentoring = requestRepository
                .findByRequesterIdAndStatusOrReceiverIdAndStatus(
                    userId, RequestStatus.ACCEPTED, userId, RequestStatus.ACCEPTED)
                .stream()
                .filter(r -> r.getRequestType() == RequestType.FREE_MENTORING)
                .count();

        long completed = requestRepository
                .findByRequesterIdAndStatusOrReceiverIdAndStatus(
                    userId, RequestStatus.COMPLETED, userId, RequestStatus.COMPLETED)
                .size();

        return DashboardStats.builder()
                .totalSkillsOffered(skillsOffered)
                .totalSkillsWanted(skillsWanted)
                .pendingRequestsReceived(pendingReceived)
                .pendingRequestsSent(pendingSent)
                .activeExchanges(activeExchanges)
                .activeMentoringSessions(activeMentoring)
                .completedSessions(completed)
                .build();
    }

    private LearningRequest findRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request", "id", id));
    }

    public LearningRequestResponse toResponse(LearningRequest request) {
        return LearningRequestResponse.builder()
                .id(request.getId())
                .requester(toUserSummary(request.getRequester()))
                .receiver(toUserSummary(request.getReceiver()))
                .requestType(request.getRequestType())
                .skillToLearn(toSkillInfo(request.getSkillToLearn()))
                .skillToTeach(request.getSkillToTeach() != null
                        ? toSkillInfo(request.getSkillToTeach()) : null)
                .message(request.getMessage())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }

    private LearningRequestResponse.UserSummary toUserSummary(User user) {
        return LearningRequestResponse.UserSummary.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .college(user.getCollege())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    private LearningRequestResponse.SkillInfo toSkillInfo(Skill skill) {
        return LearningRequestResponse.SkillInfo.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }
}
