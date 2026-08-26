CREATE TABLE access_cards (
    card_id VARCHAR(50) PRIMARY KEY,
    user_id BIGINT,
    card_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    issued_at TIMESTAMP DEFAULT NOW() NOT NULL,
    valid_from DATE,
    valid_until DATE,
    qr_code_data VARCHAR(255),
    security_deposit DECIMAL(10, 2) DEFAULT 0.00 NOT NULL,
    organization_id BIGINT NOT NULL REFERENCES organizations (id),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_access_cards_user_id ON access_cards (user_id);
CREATE INDEX idx_access_cards_org_status ON access_cards (organization_id, status);

CREATE SEQUENCE access_card_seq START WITH 1 INCREMENT BY 1;
