//package com.company.digisavingscore.repository;
//
//import com.company.digisavingscore.entity.AccountApplication;
//import com.company.digisavingscore.entity.enums.ApplicationStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
//    Optional<AccountApplication> findByUserId(Long userId);
//
//    List<AccountApplication> findByStatus(ApplicationStatus status);
//}


//package com.company.digisavingscore.repository;
//
//import com.company.digisavingscore.entity.AccountApplication;
//import com.company.digisavingscore.entity.enums.ApplicationStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;

//@Repository
//public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
//
//    Optional<AccountApplication> findByUserId(Long userId);
//
//    List<AccountApplication> findByStatus(ApplicationStatus status);
//
//    @Query("SELECT a FROM AccountApplication a JOIN FETCH a.user WHERE a.status = :status ORDER BY a.applicationDate DESC")
//    List<AccountApplication> findAllByStatusWithUser(@Param("status") ApplicationStatus status);
//    List<AccountApplication> findByReviewDateBetween(LocalDateTime start, LocalDateTime end);
//
//    @Query("SELECT a FROM AccountApplication a WHERE a.reviewDate IS NOT NULL ORDER BY a.reviewDate DESC")
//    List<AccountApplication> findAllByOrderByReviewDateDesc();
//}
//@Repository
//public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {
//
//    Optional<AccountApplication> findByUserId(Long userId);
//
//    List<AccountApplication> findByStatus(ApplicationStatus status);
//
//    @Query("SELECT a FROM AccountApplication a JOIN FETCH a.user WHERE a.status = :status ORDER BY a.applicationDate DESC")
//    List<AccountApplication> findAllByStatusWithUser(@Param("status") ApplicationStatus status);
//
//    // Add these methods
//    List<AccountApplication> findByReviewDateBetween(LocalDateTime start, LocalDateTime end);
//
//    @Query("SELECT a FROM AccountApplication a WHERE a.reviewDate IS NOT NULL ORDER BY a.reviewDate DESC")
//    List<AccountApplication> findAllByOrderByReviewDateDesc();
//}

package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountApplicationRepository extends JpaRepository<AccountApplication, Long> {

    Optional<AccountApplication> findByUserId(Long userId);

    List<AccountApplication> findByStatus(ApplicationStatus status);

    Long countByStatus(ApplicationStatus status);

    @Query("SELECT a FROM AccountApplication a JOIN FETCH a.user WHERE a.status = :status ORDER BY a.applicationDate DESC")
    List<AccountApplication> findAllByStatusWithUser(@Param("status") ApplicationStatus status);

    List<AccountApplication> findByReviewDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM AccountApplication a WHERE a.reviewDate IS NOT NULL ORDER BY a.reviewDate DESC")
    List<AccountApplication> findAllByOrderByReviewDateDesc();
}