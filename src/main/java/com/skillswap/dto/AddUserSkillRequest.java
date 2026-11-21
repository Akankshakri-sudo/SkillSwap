package com.skillswap.dto;

import com.skillswap.entity.UserSkill.SkillType;
import jakarta.validation.constraints.NotNull;

public class AddUserSkillRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Skill type is required (CAN_TEACH or WANTS_TO_LEARN)")
    private SkillType skillType;

    public AddUserSkillRequest() {}

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }
}
