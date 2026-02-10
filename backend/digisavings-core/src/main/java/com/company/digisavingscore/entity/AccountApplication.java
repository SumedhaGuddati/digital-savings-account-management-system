package com.company.digisavingscore.entity;

import com.company.digisavingscore.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "account_applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AccountApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String accountType = "SAVINGS";

    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String reviewedBy;
    private LocalDate reviewDate;
}