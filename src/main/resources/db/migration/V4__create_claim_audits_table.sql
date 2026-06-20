CREATE TABLE claim_audits (

    id UUID PRIMARY KEY,

    claim_id UUID NOT NULL,

    old_status VARCHAR(50),

    new_status VARCHAR(50) NOT NULL,

    comments VARCHAR(1000),

    changed_by VARCHAR(255) NOT NULL,

    changed_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_claim_audit_claim
        FOREIGN KEY (claim_id)
        REFERENCES expense_claims(id)
);