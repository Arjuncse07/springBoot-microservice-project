CREATE TABLE roles
(
    id          BIGINT NOT NULL PRIMARY KEY,
    name        TEXT   NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO roles (id, name, description)
VALUES (1, 'USER', 'Standard bookstore user');

INSERT INTO roles (id, name, description)
VALUES (2, 'ADMIN', 'Bookstore administrator');
