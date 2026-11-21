package com.skillswap.controller;

import com.skillswap.dto.CreateRequestDto;
import com.skillswap.dto.LearningRequestResponse;
import com.skillswap.entity.User;
import com.skillswap.service.LearningRequestService;
import com.skillswap.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class LearningRequestController {

    private final LearningRequestService requestService;
    private final UserService userService;

    public LearningRequestController(LearningRequestService requestService,
                                     UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<LearningRequestResponse> createRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateRequestDto dto) {
        User requester = userService.findUserByEmail(userDetails.getUsername());
        LearningRequestResponse response = requestService.createRequest(requester.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/received")
    public ResponseEntity<List<LearningRequestResponse>> getReceivedRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.getReceivedRequests(user.getId()));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<LearningRequestResponse>> getSentRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.getSentRequests(user.getId()));
    }

    @GetMapping("/active")
    public ResponseEntity<List<LearningRequestResponse>> getActiveRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.getActiveRequests(user.getId()));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<LearningRequestResponse> acceptRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.acceptRequest(user.getId(), id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LearningRequestResponse> rejectRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.rejectRequest(user.getId(), id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<LearningRequestResponse> completeRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(requestService.completeRequest(user.getId(), id));
    }
}
