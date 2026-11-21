package com.skillswap.dto;

import com.skillswap.entity.User.PreferredMode;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String college;
    private String department;
    private Integer yearOfStudy;
    private String bio;
    private String profileImageUrl;
    private Boolean offersFreeMentoring;
    private PreferredMode preferredMode;
    private LocalDateTime createdAt;
    private List<SkillDto> skillsCanTeach;
    private List<SkillDto> skillsWantToLearn;

    public UserResponse() {}

    public UserResponse(Long id, String name, String email, String college, String department,
                        Integer yearOfStudy, String bio, String profileImageUrl,
                        Boolean offersFreeMentoring, PreferredMode preferredMode,
                        LocalDateTime createdAt, List<SkillDto> skillsCanTeach,
                        List<SkillDto> skillsWantToLearn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.college = college;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.offersFreeMentoring = offersFreeMentoring;
        this.preferredMode = preferredMode;
        this.createdAt = createdAt;
        this.skillsCanTeach = skillsCanTeach;
        this.skillsWantToLearn = skillsWantToLearn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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
    public void setOffersFreeMentoring(Boolean offersFreeMentoring) { this.offersFreeMentoring = offersFreeMentoring; }

    public PreferredMode getPreferredMode() { return preferredMode; }
    public void setPreferredMode(PreferredMode preferredMode) { this.preferredMode = preferredMode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<SkillDto> getSkillsCanTeach() { return skillsCanTeach; }
    public void setSkillsCanTeach(List<SkillDto> skillsCanTeach) { this.skillsCanTeach = skillsCanTeach; }

    public List<SkillDto> getSkillsWantToLearn() { return skillsWantToLearn; }
    public void setSkillsWantToLearn(List<SkillDto> skillsWantToLearn) { this.skillsWantToLearn = skillsWantToLearn; }

    public static UserResponseBuilder builder() { return new UserResponseBuilder(); }

    public static class UserResponseBuilder {
        private Long id;
        private String name;
        private String email;
        private String college;
        private String department;
        private Integer yearOfStudy;
        private String bio;
        private String profileImageUrl;
        private Boolean offersFreeMentoring;
        private PreferredMode preferredMode;
        private LocalDateTime createdAt;
        private List<SkillDto> skillsCanTeach;
        private List<SkillDto> skillsWantToLearn;

        public UserResponseBuilder id(Long id) { this.id = id; return this; }
        public UserResponseBuilder name(String name) { this.name = name; return this; }
        public UserResponseBuilder email(String email) { this.email = email; return this; }
        public UserResponseBuilder college(String college) { this.college = college; return this; }
        public UserResponseBuilder department(String department) { this.department = department; return this; }
        public UserResponseBuilder yearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; return this; }
        public UserResponseBuilder bio(String bio) { this.bio = bio; return this; }
        public UserResponseBuilder profileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; return this; }
        public UserResponseBuilder offersFreeMentoring(Boolean offersFreeMentoring) { this.offersFreeMentoring = offersFreeMentoring; return this; }
        public UserResponseBuilder preferredMode(PreferredMode preferredMode) { this.preferredMode = preferredMode; return this; }
        public UserResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserResponseBuilder skillsCanTeach(List<SkillDto> skillsCanTeach) { this.skillsCanTeach = skillsCanTeach; return this; }
        public UserResponseBuilder skillsWantToLearn(List<SkillDto> skillsWantToLearn) { this.skillsWantToLearn = skillsWantToLearn; return this; }

        public UserResponse build() {
            return new UserResponse(id, name, email, college, department, yearOfStudy, bio, profileImageUrl, offersFreeMentoring, preferredMode, createdAt, skillsCanTeach, skillsWantToLearn);
        }
    }

    public static class SkillDto {
        private Long userSkillId;
        private Long skillId;
        private String name;
        private String category;

        public SkillDto() {}

        public SkillDto(Long userSkillId, Long skillId, String name, String category) {
            this.userSkillId = userSkillId;
            this.skillId = skillId;
            this.name = name;
            this.category = category;
        }

        public Long getUserSkillId() { return userSkillId; }
        public void setUserSkillId(Long userSkillId) { this.userSkillId = userSkillId; }

        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public static SkillDtoBuilder builder() { return new SkillDtoBuilder(); }

        public static class SkillDtoBuilder {
            private Long userSkillId;
            private Long skillId;
            private String name;
            private String category;

            public SkillDtoBuilder userSkillId(Long userSkillId) { this.userSkillId = userSkillId; return this; }
            public SkillDtoBuilder skillId(Long skillId) { this.skillId = skillId; return this; }
            public SkillDtoBuilder name(String name) { this.name = name; return this; }
            public SkillDtoBuilder category(String category) { this.category = category; return this; }

            public SkillDto build() {
                return new SkillDto(userSkillId, skillId, name, category);
            }
        }
    }
}
