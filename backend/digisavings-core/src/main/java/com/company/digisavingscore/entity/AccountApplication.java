package com.company.digisavingscore.entity;

import com.company.digisavingscore.entity.enums.AccountType;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_applications", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "application_status")
})
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //@OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    
    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "income_source", nullable = false)
    private String incomeSource;

    @Column(name = "annual_income", nullable = false)
    private Double annualIncome;

    @Column(name = "parent_name", nullable = false)
    private String parentName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "reviewed_by")
    private String reviewedBy;
}