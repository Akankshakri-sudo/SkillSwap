package com.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity – represents a student account in MySQL.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false, length = 255)
    private String password;  // BCrypt hash, never plain text

    @Size(max = 150, message = "College name must be under 150 characters")
    @Column(length = 150)
    private String college;

    @Size(max = 100, message = "Department must be under 100 characters")
    @Column(length = 100)
    private String department;

    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 6, message = "Year of study must be at most 6")
    private Integer yearOfStudy;

    @Size(max = 500, message = "Bio must be under 500 characters")
    @Column(length = 500)
    private String bio;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean offersFreeMentoring = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredMode preferredMode;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserSkill> userSkills = new ArrayList<>();

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LearningRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LearningRequest> receivedRequests = new ArrayList<>();

    public enum PreferredMode {
        ONLINE, IN_PERSON, BOTH
    }

    public User() {}

    public User(Long id, String name, String email, String password, String college,
                String department, Integer yearOfStudy, String bio, String profileImageUrl,
                Boolean offersFreeMentoring, PreferredMode preferredMode, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.college = college;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.offersFreeMentoring = offersFreeMentoring != null ? offersFreeMentoring : false;
        this.preferredMode = preferredMode;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Boolean getOffersFreeMentoring() { return offersFreeMentoring; }
    public void setOffersFreeMentoring(Boolean offersFreeMentoring) {
        this.offersFreeMentoring = offersFreeMentoring != null ? offersFreeMentoring : false;
    }

    public PreferredMode getPreferredMode() { return preferredMode; }
    public void setPreferredMode(PreferredMode preferredMode) { this.preferredMode = preferredMode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<UserSkill> getUserSkills() { return userSkills; }
    public void setUserSkills(List<UserSkill> userSkills) { this.userSkills = userSkills; }

    public List<LearningRequest> getSentRequests() { return sentRequests; }
    public void setSentRequests(List<LearningRequest> sentRequests) { this.sentRequests = sentRequests; }

    public List<LearningRequest> getReceivedRequests() { return receivedRequests; }
    public void setReceivedRequests(List<LearningRequest> receivedRequests) { this.receivedRequests = receivedRequests; }

    // Fluent Builder pattern
    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private String college;
        private String department;
        private Integer yearOfStudy;
        private String bio;
        private String profileImageUrl;
        private Boolean offersFreeMentoring = false;
        private PreferredMode preferredMode;
        private LocalDateTime createdAt;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder college(String college) { this.college = college; return this; }
        public UserBuilder department(String department) { this.department = department; return this; }
        public UserBuilder yearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; return this; }
        public UserBuilder bio(String bio) { this.bio = bio; return this; }
        public UserBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
        public UserBuilder offersFreeMentoring(Boolean offersFreeMentoring) { this.offersFreeMentoring = offersFreeMentoring; return this; }
        public UserBuilder preferredMode(PreferredMode preferredMode) { this.preferredMode = preferredMode; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public User build() {
            return new User(id, name, email, password, college, department, yearOfStudy, bio, profileImageUrl, offersFreeMentoring, preferredMode, createdAt);
        }
    }
}
