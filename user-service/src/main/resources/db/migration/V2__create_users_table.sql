CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users
(
    id            BIGINT    NOT NULL DEFAULT nextval('user_id_seq'),
    username      TEXT      NOT NULL UNIQUE,
    email         TEXT      NOT NULL UNIQUE,
    first_name    TEXT      NOT NULL,
    middle_name   TEXT,
    last_name     TEXT      NOT NULL,
    full_name     TEXT      NOT NULL,
    phone         TEXT,
    password_hash TEXT,
    auth_provider TEXT      NOT NULL DEFAULT 'LOCAL',
    provider_id   TEXT,
    role_id       BIGINT    NOT NULL DEFAULT 1 REFERENCES roles (id),
    enabled       BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT chk_local_has_password
        CHECK (auth_provider <> 'LOCAL' OR password_hash IS NOT NULL),
    CONSTRAINT chk_sso_has_provider_id
        CHECK (auth_provider = 'LOCAL' OR provider_id IS NOT NULL)
);

CREATE INDEX idx_users_auth_provider_id ON users (auth_provider, provider_id);

CREATE UNIQUE INDEX uq_users_sso_identity ON users (auth_provider, provider_id)
    WHERE provider_id IS NOT NULL;
