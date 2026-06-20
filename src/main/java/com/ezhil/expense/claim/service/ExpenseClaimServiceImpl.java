package com.ezhil.expense.claim.service;

import com.ezhil.expense.audit.dto.ClaimAuditResponse;
import com.ezhil.expense.audit.service.ClaimAuditService;
import com.ezhil.expense.claim.dto.CreateExpenseClaimRequest;
import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.claim.dto.UpdateExpenseClaimRequest;
import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.claim.repository.ExpenseClaimRepository;
import com.ezhil.expense.common.enums.ClaimStatus;
import com.ezhil.expense.common.exception.*;
import com.ezhil.expense.storage.FileStorageService;
import com.ezhil.expense.user.entity.User;
import com.ezhil.expense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseClaimServiceImpl
        implements ExpenseClaimService {

    private final ExpenseClaimRepository expenseClaimRepository;
    private final UserRepository userRepository;
    private final ClaimAuditService claimAuditService;
    private final FileStorageService fileStorageService;

    @Override
    public ExpenseClaimResponse createClaim(
            CreateExpenseClaimRequest request,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim = ExpenseClaim
                .builder()
                .title(request.title())
                .description(request.description())
                .amount(request.amount())
                .createdBy(user)
                .build();

        ExpenseClaim savedClaim =
                expenseClaimRepository.save(claim);

        return new ExpenseClaimResponse(
                savedClaim.getId(),
                savedClaim.getTitle(),
                savedClaim.getDescription(),
                savedClaim.getAmount(),
                savedClaim.getStatus(),
                savedClaim.getCreatedAt()
        );
    }
    @Override
    public List<ExpenseClaimResponse> getMyClaims(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        return expenseClaimRepository
                .findByCreatedBy(user)
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
    public void submitClaim(UUID claimId, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus()
                != ClaimStatus.DRAFT) {

            throw new InvalidClaimStateException(
                    "Only draft claims can be submitted"
            );
        }

        claim.setStatus(
                ClaimStatus.SUBMITTED
        );
        claimAuditService.createAudit(
                claim,
                ClaimStatus.DRAFT,
                ClaimStatus.SUBMITTED,
                "Claim submitted",
                email
        );

        claim.setSubmittedAt(
                LocalDateTime.now()
        );

        expenseClaimRepository.save(claim);
    }
    @Override
    public ExpenseClaimResponse getClaim(
            UUID claimId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        return new ExpenseClaimResponse(
                claim.getId(),
                claim.getTitle(),
                claim.getDescription(),
                claim.getAmount(),
                claim.getStatus(),
                claim.getCreatedAt()
        );
    }
    @Override
    public ExpenseClaimResponse updateClaim(
            UUID claimId,
            UpdateExpenseClaimRequest request,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus() != ClaimStatus.DRAFT) {

            throw new InvalidClaimStateException(
                    "Only draft claims can be updated"
            );
        }

        claim.setTitle(request.title());
        claim.setDescription(request.description());
        claim.setAmount(request.amount());


        ExpenseClaim updated =
                expenseClaimRepository.save(claim);

        return new ExpenseClaimResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.getAmount(),
                updated.getStatus(),
                updated.getCreatedAt()
        );
    }
    @Override
    public void deleteClaim(
            UUID claimId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus() != ClaimStatus.DRAFT) {

            throw new InvalidClaimStateException(
                    "Only draft claims can be deleted"
            );
        }

        expenseClaimRepository.delete(claim);
    }
    @Override
    public void cancelClaim(
            UUID claimId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus()
                != ClaimStatus.SUBMITTED) {

            throw new InvalidClaimStateException(
                    "Only submitted claims can be cancelled"
            );
        }
        ClaimStatus oldStatus =
                claim.getStatus();
        claim.setStatus(
                ClaimStatus.CANCELLED
        );
        claimAuditService.createAudit(
                claim,
                oldStatus,
                ClaimStatus.CANCELLED,
                "Claim cancelled by employee",
                email
        );

        expenseClaimRepository.save(claim);
    }
    @Override
    public List<ClaimAuditResponse>
    getClaimTimeline(
            UUID claimId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        return claimAuditService
                .getClaimTimeline(claim);
    }
    @Override
    public ExpenseClaimResponse uploadReceipt(
            UUID claimId,
            MultipartFile file,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        ExpenseClaim claim =
                expenseClaimRepository
                        .findByIdAndCreatedBy(
                                claimId,
                                user
                        )
                        .orElseThrow(
                                ClaimNotFoundException::new
                        );

        if (claim.getStatus() != ClaimStatus.DRAFT
                && claim.getStatus() != ClaimStatus.SUBMITTED) {

            throw new InvalidClaimStateException(
                    "Receipt can only be uploaded for draft or submitted claims"
            );
        }

        String receiptUrl =
                fileStorageService.uploadFile(file);

        claim.setReceiptPath(
                receiptUrl
        );

        ExpenseClaim updated =
                expenseClaimRepository.save(claim);

        return new ExpenseClaimResponse(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.getAmount(),
                updated.getStatus(),
                updated.getCreatedAt()
        );
    }
}