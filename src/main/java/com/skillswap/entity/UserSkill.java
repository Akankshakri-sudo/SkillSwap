package com.skillswap.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_skills", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"user_id", "skill_id", "skill_type"},
                name = "uk_user_skill_type"
        )
})
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false, length = 20)
    private SkillType skillType;

    public enum SkillType {
        CAN_TEACH,
        WANTS_TO_LEARN
    }

    public UserSkill() {}

    public UserSkill(Long id, User user, Skill skill, SkillType skillType) {
        this.id = id;
        this.user = user;
        this.skill = skill;
        this.skillType = skillType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }

    public static UserSkillBuilder builder() { return new UserSkillBuilder(); }

    public static class UserSkillBuilder {
        private Long id;
        private User user;
        private Skill skill;
        private SkillType skillType;

        public UserSkillBuilder id(Long id) { this.id = id; return this; }
        public UserSkillBuilder user(User user) { this.user = user; return this; }
        public UserSkillBuilder skill(Skill skill) { this.skill = skill; return this; }
        public UserSkillBuilder skillType(SkillType skillType) { this.skillType = skillType; return this; }

        public UserSkill build() {
            return new UserSkill(id, user, skill, skillType);
        }
    }
}
