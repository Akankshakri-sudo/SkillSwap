package com.skillswap.dto;

import com.skillswap.entity.LearningRequest.RequestType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateRequestDto {

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    @NotNull(message = "Request type is required (EXCHANGE or FREE_MENTORING)")
    private RequestType requestType;

    @NotNull(message = "Please specify which skill you want to learn")
    private Long skillToLearnId;

    private Long skillToTeachId;

    @Size(max = 1000, message = "Message must be under 1000 characters")
    private String message;

    public CreateRequestDto() {}

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }

    public Long getSkillToLearnId() { return skillToLearnId; }
    public void setSkillToLearnId(Long skillToLearnId) { this.skillToLearnId = skillToLearnId; }

    public Long getSkillToTeachId() { return skillToTeachId; }
    public void setSkillToTeachId(Long skillToTeachId) { this.skillToTeachId = skillToTeachId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
