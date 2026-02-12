//package com.company.digisavingscore.repository;
//
//import com.company.digisavingscore.entity.User;
//import com.company.digisavingscore.entity.enums.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
////public interface UserRepository extends JpaRepository<User, Long> {
////
////    Optional<User> findByEmail(String email);
////
////    boolean existsByEmail(String email);
////    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
////
////    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
////    List<User> findAllByOrderByCreatedAtDesc();
////}
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    Optional<User> findByEmail(String email);
//
//    boolean existsByEmail(String email);
//
//    Long countByRole(Role role);
//
//    // Add these methods
//    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
//
//    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
//    List<User> findAllByOrderByCreatedAtDesc();
//}

//package com.company.digisavingscore.repository;
//
//import com.company.digisavingscore.entity.User;
//import com.company.digisavingscore.entity.enums.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    Optional<User> findByEmail(String email);
//
//    boolean existsByEmail(String email);
//
//    Long countByRole(Role role);
//
//    // FIXED: Correct way to count users registered today
//    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND CAST(u.createdAt AS date) = CURRENT_DATE")
//    Long countNewUsersTodayByRole(@Param("role") Role role);
//
//    // Alternative if CAST doesn't work:
//    /*
//    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND DATE(u.createdAt) = CURRENT_DATE")
//    Long countNewUsersTodayByRole(@Param("role") Role role);
//    */
//
//    // NEW: Get count of users by role
//    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
//    List<Object[]> countUsersByRole();
//
//    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
//
//    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
//    List<User> findAllByOrderByCreatedAtDesc();
//}


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

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // This takes Role enum
    Long countByRole(Role role);

    // This takes String for native query
    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.role = :role AND DATE(u.created_at) = CURRENT_DATE",
            nativeQuery = true)
    Long countNewUsersTodayByRole(@Param("role") String role);

    // This returns Role enum objects
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();

    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findAllByOrderByCreatedAtDesc();
}