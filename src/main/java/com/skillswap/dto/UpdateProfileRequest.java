package com.skillswap.dto;

import com.skillswap.entity.User.PreferredMode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;

    @Size(max = 150, message = "College name must be under 150 characters")
    private String college;

    @Size(max = 100, message = "Department must be under 100 characters")
    private String department;

    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 6, message = "Year of study must be at most 6")
    private Integer yearOfStudy;

    @Size(max = 500, message = "Bio must be under 500 characters")
    private String bio;

    @Size(max = 500)
    private String profileImageUrl;

    private Boolean offersFreeMentoring;

    private PreferredMode preferredMode;

    public UpdateProfileRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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
}
