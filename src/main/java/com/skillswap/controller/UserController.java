package com.skillswap.controller;

import com.skillswap.dto.AddUserSkillRequest;
import com.skillswap.dto.DashboardStats;
import com.skillswap.dto.UpdateProfileRequest;
import com.skillswap.dto.UserResponse;
import com.skillswap.entity.User;
import com.skillswap.service.LearningRequestService;
import com.skillswap.service.SkillService;
import com.skillswap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SkillService skillService;
    private final LearningRequestService requestService;

    public UserController(UserService userService,
                          SkillService skillService,
                          LearningRequestService requestService) {
        this.userService = userService;
        this.skillService = skillService;
        this.requestService = requestService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userService.toUserResponseWithSkills(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        UserResponse updated = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/discover")
    public ResponseEntity<Page<UserResponse>> discoverStudents(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) User.PreferredMode mode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        User currentUser = userService.findUserByEmail(userDetails.getUsername());
        Pageable pageable = PageRequest.of(page, size);

        Page<UserResponse> results = userService.discoverStudents(
                currentUser.getId(), skill, mode, pageable);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/me/skills")
    public ResponseEntity<UserResponse> addSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddUserSkillRequest request) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        UserResponse response = skillService.addUserSkill(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/skills/{userSkillId}")
    public ResponseEntity<Void> removeSkill(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userSkillId) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        skillService.removeUserSkill(user.getId(), userSkillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/dashboard")
    public ResponseEntity<DashboardStats> getDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        DashboardStats stats = requestService.getDashboardStats(user.getId());
        return ResponseEntity.ok(stats);
    }
}
