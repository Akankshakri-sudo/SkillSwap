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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserService userService;

    public SkillService(SkillRepository skillRepository,
                        UserSkillRepository userSkillRepository,
                        UserService userService) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Transactional
    public Skill createSkill(String name, String category) {
        if (skillRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Skill '" + name + "' already exists.");
        }
        return skillRepository.save(
            Skill.builder()
                .name(name.trim())
                .category(category != null ? category.trim() : null)
                .build()
        );
    }

    @Transactional
    public UserResponse addUserSkill(Long userId, AddUserSkillRequest request) {
        User user = userService.findUserById(userId);

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", request.getSkillId()));

        if (userSkillRepository.existsByUserIdAndSkillIdAndSkillType(
                userId, skill.getId(), request.getSkillType())) {
            throw new BadRequestException(
                "You already have '" + skill.getName() + "' as " +
                request.getSkillType().name().replace("_", " ").toLowerCase() + ".");
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .skillType(request.getSkillType())
                .build();

        userSkillRepository.save(userSkill);
        log.info("Added skill '{}' ({}) for user ID: {}", skill.getName(), request.getSkillType(), userId);

        return userService.toUserResponseWithSkills(user);
    }

    @Transactional
    public void removeUserSkill(Long userId, Long userSkillId) {
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill", "id", userSkillId));

        if (!userSkill.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only remove your own skills.");
        }

        userSkillRepository.delete(userSkill);
        log.info("Removed UserSkill ID: {} for user ID: {}", userSkillId, userId);
    }

    @Transactional(readOnly = true)
    public List<UserSkill> getUserSkills(Long userId) {
        return userSkillRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<UserSkill> getUserSkillsByType(Long userId, SkillType type) {
        return userSkillRepository.findByUserIdAndSkillType(userId, type);
    }

    public Skill findSkillById(Long skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));
    }
}
