package com.skillswap.service;

import com.skillswap.dto.RegisterRequest;
import com.skillswap.dto.UpdateProfileRequest;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkill;
import com.skillswap.exception.BadRequestException;
import com.skillswap.exception.ResourceNotFoundException;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.UserSkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserSkillRepository userSkillRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                "An account with email '" + request.getEmail() + "' already exists.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .college(request.getCollege())
                .department(request.getDepartment())
                .yearOfStudy(request.getYearOfStudy())
                .offersFreeMentoring(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return toUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = findUserById(userId);
        return toUserResponseWithSkills(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserById(userId);

        if (request.getName() != null) user.setName(request.getName());
        if (request.getCollege() != null) user.setCollege(request.getCollege());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getYearOfStudy() != null) user.setYearOfStudy(request.getYearOfStudy());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        if (request.getOffersFreeMentoring() != null) user.setOffersFreeMentoring(request.getOffersFreeMentoring());
        if (request.getPreferredMode() != null) user.setPreferredMode(request.getPreferredMode());

        User updated = userRepository.save(user);
        log.info("Profile updated for user ID: {}", userId);
        return toUserResponseWithSkills(updated);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> discoverStudents(Long currentUserId, String skill,
                                                User.PreferredMode mode, Pageable pageable) {
        return userRepository
                .discoverStudents(currentUserId, skill, mode, pageable)
                .map(this::toUserResponseWithSkills);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .college(user.getCollege())
                .department(user.getDepartment())
                .yearOfStudy(user.getYearOfStudy())
                .bio(user.getBio())
                .profileImageUrl(user.getProfileImageUrl())
                .offersFreeMentoring(user.getOffersFreeMentoring())
                .preferredMode(user.getPreferredMode())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserResponse toUserResponseWithSkills(User user) {
        List<UserSkill> allSkills = userSkillRepository.findByUserId(user.getId());

        List<UserResponse.SkillDto> canTeach = allSkills.stream()
                .filter(us -> us.getSkillType() == UserSkill.SkillType.CAN_TEACH)
                .map(us -> UserResponse.SkillDto.builder()
                        .userSkillId(us.getId())
                        .skillId(us.getSkill().getId())
                        .name(us.getSkill().getName())
                        .category(us.getSkill().getCategory())
                        .build())
                .toList();

        List<UserResponse.SkillDto> wantToLearn = allSkills.stream()
                .filter(us -> us.getSkillType() == UserSkill.SkillType.WANTS_TO_LEARN)
                .map(us -> UserResponse.SkillDto.builder()
                        .userSkillId(us.getId())
                        .skillId(us.getSkill().getId())
                        .name(us.getSkill().getName())
                        .category(us.getSkill().getCategory())
                        .build())
                .toList();

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .college(user.getCollege())
                .department(user.getDepartment())
                .yearOfStudy(user.getYearOfStudy())
                .bio(user.getBio())
                .profileImageUrl(user.getProfileImageUrl())
                .offersFreeMentoring(user.getOffersFreeMentoring())
                .preferredMode(user.getPreferredMode())
                .createdAt(user.getCreatedAt())
                .skillsCanTeach(canTeach)
                .skillsWantToLearn(wantToLearn)
                .build();
    }
}
