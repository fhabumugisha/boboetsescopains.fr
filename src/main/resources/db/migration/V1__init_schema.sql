-- V1: Initial schema creation
-- Create roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100)
);

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create user_roles junction table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Create password_reset_tokens table
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_password_reset_tokens_token ON password_reset_tokens(token);
CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);

-- Insert default roles
INSERT INTO roles (name, description) VALUES
    ('ROLE_USER', 'Utilisateur standard'),
    ('ROLE_ADMIN', 'Administrateur avec tous les droits');

-- Insert default admin user (password: admin123 - bcrypt encoded)
-- Note: Change this password in production!
INSERT INTO users (username, email, password, first_name, last_name, enabled)
VALUES ('admin', 'admin@boboetsescopains.fr', '$2a$10$7ZwG3h.DLTM5jK8xHGHqxevBV7FH4YJTvqCQxRqZHWzQ.PqKbNyIy', 'Admin', 'User', true);

-- Assign admin role to default admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';
