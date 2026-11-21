package com.skillswap.repository;

import com.skillswap.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository – the data access layer for the User entity.
 *
 * By extending JpaRepository<User, Long>, we get these methods for FREE:
 *   save(user)         – INSERT or UPDATE
 *   findById(id)       – SELECT by primary key
 *   findAll()          – SELECT all
 *   delete(user)       – DELETE
 *   count()            – COUNT(*)
 *   existsById(id)     – existence check
 *   ... and many more
 *
 * We only need to define CUSTOM queries beyond that.
 *
 * Spring Data JPA AUTOMATICALLY implements this interface at runtime —
 * you never write SQL or implementation code for simple queries!
 *
 * For complex queries, we use:
 *   - Method name derivation: findByEmail() → "SELECT * FROM users WHERE email = ?"
 *   - @Query with JPQL (Java Persistence Query Language) — queries the entity,
 *     not the SQL table, so we use class names and field names.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * Spring Data derives the SQL: SELECT * FROM users WHERE email = ?
     * Returns Optional<User> so callers handle the "not found" case cleanly.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the given email already exists.
     * Used during registration to prevent duplicate accounts.
     * Spring Data derives: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Discover students – search by skill name and optional mode filter.
     *
     * JPQL explanation:
     *   SELECT DISTINCT u         → distinct users (avoid duplicates from JOIN)
     *   FROM User u               → User entity
     *   JOIN u.userSkills us      → join their UserSkill entries
     *   JOIN us.skill s           → join the actual Skill
     *   WHERE u.id != :userId     → exclude the searching student themselves
     *   AND (:skill IS NULL       → if no skill filter, ignore this condition
     *        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%')))
     *                             → case-insensitive partial match on skill name
     *   AND (:mode IS NULL        → if no mode filter, ignore
     *        OR u.preferredMode = :mode)
     *
     * This becomes the Discover Students page query.
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            JOIN u.userSkills us
            JOIN us.skill s
            WHERE u.id != :userId
            AND (:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%')))
            AND (:mode IS NULL OR u.preferredMode = :mode)
            ORDER BY u.name ASC
            """)
    Page<User> discoverStudents(
            @Param("userId") Long userId,
            @Param("skill") String skill,
            @Param("mode") User.PreferredMode mode,
            Pageable pageable
    );

    /**
     * Find students who offer free mentoring, with optional skill filter.
     */
    @Query("""
            SELECT DISTINCT u FROM User u
            JOIN u.userSkills us
            JOIN us.skill s
            WHERE u.id != :userId
            AND u.offersFreeMentoring = true
            AND (:skill IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skill, '%')))
            ORDER BY u.name ASC
            """)
    Page<User> findMentors(
            @Param("userId") Long userId,
            @Param("skill") String skill,
            Pageable pageable
    );
}
