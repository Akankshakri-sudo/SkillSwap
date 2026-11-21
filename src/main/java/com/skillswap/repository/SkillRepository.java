package com.skillswap.repository;

import com.skillswap.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * SkillRepository – data access for the Skill (master list) entity.
 *
 * findByNameIgnoreCase: looks up a skill by name case-insensitively.
 * This prevents "java", "Java", "JAVA" from creating three separate skills.
 *
 * existsByNameIgnoreCase: quick existence check before creating new skills.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
