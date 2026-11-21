package com.skillswap.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_requests")
public class LearningRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false, length = 20)
    private RequestType requestType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_to_learn_id", nullable = false)
    private Skill skillToLearn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_to_teach_id", nullable = true)
    private Skill skillToTeach;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status = RequestStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum RequestType {
        EXCHANGE,
        FREE_MENTORING
    }

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        COMPLETED
    }

    public LearningRequest() {}

    public LearningRequest(Long id, User requester, User receiver, RequestType requestType,
                           Skill skillToLearn, Skill skillToTeach, String message,
                           RequestStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.requestType = requestType;
        this.skillToLearn = skillToLearn;
        this.skillToTeach = skillToTeach;
        this.message = message;
        this.status = status != null ? status : RequestStatus.PENDING;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }

    public Skill getSkillToLearn() { return skillToLearn; }
    public void setSkillToLearn(Skill skillToLearn) { this.skillToLearn = skillToLearn; }

    public Skill getSkillToTeach() { return skillToTeach; }
    public void setSkillToTeach(Skill skillToTeach) { this.skillToTeach = skillToTeach; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static LearningRequestBuilder builder() { return new LearningRequestBuilder(); }

    public static class LearningRequestBuilder {
        private Long id;
        private User requester;
        private User receiver;
        private RequestType requestType;
        private Skill skillToLearn;
        private Skill skillToTeach;
        private String message;
        private RequestStatus status = RequestStatus.PENDING;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public LearningRequestBuilder id(Long id) { this.id = id; return this; }
        public LearningRequestBuilder requester(User requester) { this.requester = requester; return this; }
        public LearningRequestBuilder receiver(User receiver) { this.receiver = receiver; return this; }
        public LearningRequestBuilder requestType(RequestType requestType) { this.requestType = requestType; return this; }
        public LearningRequestBuilder skillToLearn(Skill skillToLearn) { this.skillToLearn = skillToLearn; return this; }
        public LearningRequestBuilder skillToTeach(Skill skillToTeach) { this.skillToTeach = skillToTeach; return this; }
        public LearningRequestBuilder message(String message) { this.message = message; return this; }
        public LearningRequestBuilder status(RequestStatus status) { this.status = status; return this; }
        public LearningRequestBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public LearningRequestBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public LearningRequest build() {
            return new LearningRequest(id, requester, receiver, requestType, skillToLearn, skillToTeach, message, status, createdAt, updatedAt);
        }
    }
}
