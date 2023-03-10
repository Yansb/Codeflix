
CREATE TABLE categories (
    id CHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL
);