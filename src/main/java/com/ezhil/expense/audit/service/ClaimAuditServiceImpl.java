package com.ezhil.expense.audit.service;

import com.ezhil.expense.audit.dto.ClaimAuditResponse;
import com.ezhil.expense.audit.entity.ClaimAudit;
import com.ezhil.expense.audit.repository.ClaimAuditRepository;
import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.common.enums.ClaimStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimAuditServiceImpl
        implements ClaimAuditService {

    private final ClaimAuditRepository claimAuditRepository;

    @Override
    public void createAudit(
            ExpenseClaim claim,
            ClaimStatus oldStatus,
            ClaimStatus newStatus,
            String comments,
            String changedBy
    ) {

        ClaimAudit audit =
                ClaimAudit.builder()
                        .claim(claim)
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .comments(comments)
                        .changedBy(changedBy)
                        .changedAt(
                                LocalDateTime.now()
                        )
                        .build();

        claimAuditRepository.save(audit);
    }

    @Override
    public List<ClaimAuditResponse>
    getClaimTimeline(
            ExpenseClaim claim
    ) {

        return claimAuditRepository
                .findByClaimOrderByChangedAtAsc(
                        claim
                )
                .stream()
                .map(audit ->
                        new ClaimAuditResponse(
                                audit.getOldStatus(),
                                audit.getNewStatus(),
                                audit.getComments(),
                                audit.getChangedBy(),
                                audit.getChangedAt()
                        )
                )
                .toList();
    }

}