package com.ezhil.expense.finance.service;

import com.ezhil.expense.audit.service.ClaimAuditService;
import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.claim.repository.ExpenseClaimRepository;
import com.ezhil.expense.common.enums.ClaimStatus;
import com.ezhil.expense.common.exception.ClaimNotFoundException;
import com.ezhil.expense.common.exception.InvalidClaimStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl
        implements FinanceService {

    private final ExpenseClaimRepository expenseClaimRepository;
    private final ClaimAuditService claimAuditService;

    @Override
    public List<ExpenseClaimResponse> getApprovedClaims() {

        return expenseClaimRepository
                .findByStatus(
                        ClaimStatus.MANAGER_APPROVED
                )
                .stream()
                .map(claim -> new ExpenseClaimResponse(
                        claim.getId(),
                        claim.getTitle(),
                        claim.getDescription(),
                        claim.getAmount(),
                        claim.getStatus(),
                        claim.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void approveClaim(
            UUID claimId
    ) {

        ExpenseClaim claim =
                expenseClaimRepository
                        .findById(claimId)
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus()
                != ClaimStatus.MANAGER_APPROVED) {

            throw new InvalidClaimStateException(
                    "Only manager approved claims can be finance approved"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();
        claim.setStatus(
                ClaimStatus.FINANCE_APPROVED
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.FINANCE_APPROVED,
                "Finance approved",
                "FINANCE"
        );
        claim.setFinanceReviewedAt(
                LocalDateTime.now()
        );

        expenseClaimRepository.save(claim);
    }

    @Override
    public void rejectClaim(
            UUID claimId,
            String comments
    ) {

        ExpenseClaim claim =
                expenseClaimRepository
                        .findById(claimId)
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus()
                != ClaimStatus.MANAGER_APPROVED) {

            throw new InvalidClaimStateException(
                    "Only manager approved claims can be finance rejected"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();

        claim.setStatus(
                ClaimStatus.FINANCE_REJECTED
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.FINANCE_REJECTED,
                comments,
                "FINANCE"
        );
        claim.setFinanceComments(
                comments
        );

        claim.setFinanceReviewedAt(
                LocalDateTime.now()
        );

        expenseClaimRepository.save(claim);
    }

    @Override
    public void markAsPaid(
            UUID claimId
    ) {

        ExpenseClaim claim =
                expenseClaimRepository
                        .findById(claimId)
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus()
                != ClaimStatus.FINANCE_APPROVED) {

            throw new InvalidClaimStateException(
                    "Only finance approved claims can be paid"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();
        claim.setStatus(
                ClaimStatus.PAID
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.PAID,
                "Payment completed",
                "FINANCE"
        );

        claim.setPaidAt(
                LocalDateTime.now()
        );

        expenseClaimRepository.save(claim);
    }
}