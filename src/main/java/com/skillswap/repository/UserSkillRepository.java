package com.skillswap.repository;

import com.skillswap.entity.UserSkill;
import com.skillswap.entity.UserSkill.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserSkillRepository – data access for UserSkill join entities.
 *
 * Key queries:
 *
 * findByUserIdAndSkillType:
 *   Gets all skills a user can teach (or wants to learn).
 *   Spring Data derives: SELECT * FROM user_skills
 *                        WHERE user_id = ? AND skill_type = ?
 *
 * findByUserIdAndSkillIdAndSkillType:
 *   Checks for a duplicate before adding a skill.
 *   If a row already exists, we reject the request.
 *
 * existsByUserIdAndSkillIdAndSkillType:
 *   Faster existence check (no data returned).
 */
@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserId(Long userId);

    List<UserSkill> findByUserIdAndSkillType(Long userId, SkillType skillType);

    Optional<UserSkill> findByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, SkillType skillType);

    boolean existsByUserIdAndSkillIdAndSkillType(Long userId, Long skillId, SkillType skillType);
}
