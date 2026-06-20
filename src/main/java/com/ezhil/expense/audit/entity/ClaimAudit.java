package com.ezhil.expense.audit.entity;

import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.common.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claim_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "claim_id",
            nullable = false
    )
    private ExpenseClaim claim;

    @Enumerated(EnumType.STRING)
    private ClaimStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus newStatus;

    @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private String changedBy;

    @Column(nullable = false)
    private LocalDateTime changedAt;
}