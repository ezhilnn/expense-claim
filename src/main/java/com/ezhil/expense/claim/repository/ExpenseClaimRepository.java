package com.ezhil.expense.claim.repository;

import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.common.enums.ClaimStatus;
import com.ezhil.expense.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseClaimRepository
        extends JpaRepository<ExpenseClaim, UUID> {

    List<ExpenseClaim> findByCreatedBy(
            User user
    );
    Optional<ExpenseClaim> findByIdAndCreatedBy(
            UUID id,
            User user
    );
    List<ExpenseClaim> findByStatus(
            ClaimStatus status
    );
}