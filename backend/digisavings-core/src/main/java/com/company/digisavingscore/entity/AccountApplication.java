package com.company.digisavingscore.entity;

import com.company.digisavingscore.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "account_applications")
@Data
public class AccountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reviewed_by")
    private UserRole reviewedBy;
}