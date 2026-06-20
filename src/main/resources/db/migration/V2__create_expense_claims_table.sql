CREATE TABLE expense_claims (

    id UUID PRIMARY KEY,

    title VARCHAR(200) NOT NULL,

    description VARCHAR(1000),

    amount NUMERIC(12,2) NOT NULL,

    status VARCHAR(50) NOT NULL,

    submitted_at TIMESTAMP,

    manager_reviewed_at TIMESTAMP,

    finance_reviewed_at TIMESTAMP,

    paid_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    created_by UUID NOT NULL,

    CONSTRAINT fk_expense_claim_user
        FOREIGN KEY (created_by)
        REFERENCES users(id)
);