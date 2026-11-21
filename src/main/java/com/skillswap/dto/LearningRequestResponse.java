package com.skillswap.dto;

import com.skillswap.entity.LearningRequest.RequestStatus;
import com.skillswap.entity.LearningRequest.RequestType;

import java.time.LocalDateTime;

public class LearningRequestResponse {

    private Long id;
    private UserSummary requester;
    private UserSummary receiver;
    private RequestType requestType;
    private SkillInfo skillToLearn;
    private SkillInfo skillToTeach;
    private String message;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LearningRequestResponse() {}

    public LearningRequestResponse(Long id, UserSummary requester, UserSummary receiver,
                                   RequestType requestType, SkillInfo skillToLearn,
                                   SkillInfo skillToTeach, String message, RequestStatus status,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.requestType = requestType;
        this.skillToLearn = skillToLearn;
        this.skillToTeach = skillToTeach;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserSummary getRequester() { return requester; }
    public void setRequester(UserSummary requester) { this.requester = requester; }

    public UserSummary getReceiver() { return receiver; }
    public void setReceiver(UserSummary receiver) { this.receiver = receiver; }

    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }

    public SkillInfo getSkillToLearn() { return skillToLearn; }
    public void setSkillToLearn(SkillInfo skillToLearn) { this.skillToLearn = skillToLearn; }

    public SkillInfo getSkillToTeach() { return skillToTeach; }
    public void setSkillToTeach(SkillInfo skillToTeach) { this.skillToTeach = skillToTeach; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static LearningRequestResponseBuilder builder() { return new LearningRequestResponseBuilder(); }

    public static class LearningRequestResponseBuilder {
        private Long id;
        private UserSummary requester;
        private UserSummary receiver;
        private RequestType requestType;
        private SkillInfo skillToLearn;
        private SkillInfo skillToTeach;
        private String message;
        private RequestStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public LearningRequestResponseBuilder id(Long id) { this.id = id; return this; }
        public LearningRequestResponseBuilder requester(UserSummary requester) { this.requester = requester; return this; }
        public LearningRequestResponseBuilder receiver(UserSummary receiver) { this.receiver = receiver; return this; }
        public LearningRequestResponseBuilder requestType(RequestType requestType) { this.requestType = requestType; return this; }
        public LearningRequestResponseBuilder skillToLearn(SkillInfo skillToLearn) { this.skillToLearn = skillToLearn; return this; }
        public LearningRequestResponseBuilder skillToTeach(SkillInfo skillToTeach) { this.skillToTeach = skillToTeach; return this; }
        public LearningRequestResponseBuilder message(String message) { this.message = message; return this; }
        public LearningRequestResponseBuilder status(RequestStatus status) { this.status = status; return this; }
        public LearningRequestResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public LearningRequestResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public LearningRequestResponse build() {
            return new LearningRequestResponse(id, requester, receiver, requestType, skillToLearn, skillToTeach, message, status, createdAt, updatedAt);
        }
    }

    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
        private String college;
        private String profileImageUrl;

        public UserSummary() {}

        public UserSummary(Long id, String name, String email, String college, String profileImageUrl) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.college = college;
            this.profileImageUrl = profileImageUrl;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getCollege() { return college; }
        public void setCollege(String college) { this.college = college; }

        public String getProfileImageUrl() { return profileImageUrl; }
        public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

        public static UserSummaryBuilder builder() { return new UserSummaryBuilder(); }

        public static class UserSummaryBuilder {
            private Long id;
            private String name;
            private String email;
            private String college;
            private String profileImageUrl;

            public UserSummaryBuilder id(Long id) { this.id = id; return this; }
            public UserSummaryBuilder name(String name) { this.name = name; return this; }
            public UserSummaryBuilder email(String email) { this.email = email; return this; }
            public UserSummaryBuilder college(String college) { this.college = college; return this; }
            public UserSummaryBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }

            public UserSummary build() {
                return new UserSummary(id, name, email, college, profileImageUrl);
            }
        }
    }

    public static class SkillInfo {
        private Long id;
        private String name;
        private String category;

        public SkillInfo() {}

        public SkillInfo(Long id, String name, String category) {
            this.id = id;
            this.name = name;
            this.category = category;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public static SkillInfoBuilder builder() { return new SkillInfoBuilder(); }

        public static class SkillInfoBuilder {
            private Long id;
            private String name;
            private String category;

            public SkillInfoBuilder id(Long id) { this.id = id; return this; }
            public SkillInfoBuilder name(String name) { this.name = name; return this; }
            public SkillInfoBuilder category(String category) { this.category = category; return this; }

            public SkillInfo build() {
                return new SkillInfo(id, name, category);
            }
        }
    }
}
