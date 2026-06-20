package com.ezhil.expense.audit.repository;

import com.ezhil.expense.audit.entity.ClaimAudit;
import com.ezhil.expense.claim.entity.ExpenseClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClaimAuditRepository
        extends JpaRepository<ClaimAudit, UUID> {

    List<ClaimAudit> findByClaimOrderByChangedAtAsc(
            ExpenseClaim claim
    );
}