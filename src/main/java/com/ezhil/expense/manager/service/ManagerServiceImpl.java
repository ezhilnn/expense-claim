package com.ezhil.expense.manager.service;

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
public class ManagerServiceImpl
        implements ManagerService {

    private final ExpenseClaimRepository expenseClaimRepository;
    private final ClaimAuditService claimAuditService;

    @Override
    public List<ExpenseClaimResponse>
    getSubmittedClaims() {

        return expenseClaimRepository
                .findByStatus(
                        ClaimStatus.SUBMITTED
                )
                .stream()
                .map(claim ->
                        new ExpenseClaimResponse(
                                claim.getId(),
                                claim.getTitle(),
                                claim.getDescription(),
                                claim.getAmount(),
                                claim.getStatus(),
                                claim.getCreatedAt()
                        )
                )
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
                != ClaimStatus.SUBMITTED) {

            throw new InvalidClaimStateException(
                    "Only submitted claims can be approved"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();
        claim.setStatus(
                ClaimStatus.MANAGER_APPROVED
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.MANAGER_APPROVED,
                "Manager approved",
                "MANAGER"
        );

        claim.setManagerReviewedAt(
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
                != ClaimStatus.SUBMITTED) {

            throw new InvalidClaimStateException(
                    "Only submitted claims can be rejected"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();
        claim.setStatus(
                ClaimStatus.MANAGER_REJECTED
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.MANAGER_REJECTED,
                comments,
                "MANAGER"
        );
        claim.setManagerComments(
                comments
        );

        claim.setManagerReviewedAt(
                LocalDateTime.now()
        );

        expenseClaimRepository.save(claim);
    }
}