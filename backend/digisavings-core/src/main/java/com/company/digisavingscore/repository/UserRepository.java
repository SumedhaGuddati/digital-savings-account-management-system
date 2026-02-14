package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * @author Bharadi Harshitha
     */
    Long countByRole(Role role);

    /**
     * @author Bharadi Harshitha
     */
    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.role = :role AND DATE(u.created_at) = CURRENT_DATE",
            nativeQuery = true)
    Long countNewUsersTodayByRole(@Param("role") String role);

    /**
     * @author Bharadi Harshitha
     */
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();

    /**
     * @author Bharadi Harshitha
     */
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * @author Bharadi Harshitha
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findAllByOrderByCreatedAtDesc();
}