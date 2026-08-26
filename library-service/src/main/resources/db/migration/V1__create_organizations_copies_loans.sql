CREATE TABLE organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(50) UNIQUE NOT NULL,
    loan_period_days INT DEFAULT 14 NOT NULL,
    max_renewals INT DEFAULT 2 NOT NULL,
    fine_per_day DECIMAL(10, 2) DEFAULT 5.00 NOT NULL,
    gate_policy VARCHAR(20) DEFAULT 'WARNING' NOT NULL,
    seat_allocation_enabled BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE copies (
    barcode VARCHAR(50) PRIMARY KEY,
    product_code VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE' NOT NULL,
    location VARCHAR(100),
    condition_notes TEXT,
    organization_id BIGINT NOT NULL REFERENCES organizations (id),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_copies_product_code ON copies (product_code);
CREATE INDEX idx_copies_org_status ON copies (organization_id, status);

CREATE TABLE loans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    barcode VARCHAR(50) NOT NULL REFERENCES copies (barcode),
    borrowed_at TIMESTAMP DEFAULT NOW() NOT NULL,
    due_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    renewal_count INT DEFAULT 0 NOT NULL,
    fine_amount DECIMAL(10, 2) DEFAULT 0.00 NOT NULL,
    fine_paid BOOLEAN DEFAULT false NOT NULL,
    organization_id BIGINT NOT NULL REFERENCES organizations (id)
);

CREATE INDEX idx_loans_user_status ON loans (user_id, status);
CREATE INDEX idx_loans_due_status ON loans (due_at, status);
CREATE INDEX idx_loans_barcode ON loans (barcode);

INSERT INTO organizations (id, name, slug, loan_period_days, max_renewals, fine_per_day)
VALUES (1, 'Central Library', 'central-library', 14, 2, 5.00);

INSERT INTO copies (barcode, product_code, status, organization_id)
VALUES
    ('BC-P100', 'P100', 'AVAILABLE', 1),
    ('BC-P101', 'P101', 'AVAILABLE', 1),
    ('BC-P102', 'P102', 'AVAILABLE', 1),
    ('BC-P103', 'P103', 'AVAILABLE', 1),
    ('BC-P104', 'P104', 'AVAILABLE', 1);

SELECT setval('organizations_id_seq', (SELECT MAX(id) FROM organizations));
