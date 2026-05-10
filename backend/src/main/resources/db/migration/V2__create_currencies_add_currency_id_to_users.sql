-- Create currency table
CREATE TABLE currency (
	id BIGSERIAL PRIMARY KEY,
	code VARCHAR(3) NOT NULL UNIQUE, -- ISO4217
	symbol VARCHAR(5) NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Alter Users Table to add updated_at and is_active
ALTER TABLE users 
	ADD COLUMN updated_at TIMESTAMP NOT NULL() DEFAULT NOW(),
	ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- Add Foreign Key and constraint
ALTER TABLE users
	ADD COLUMN currency_id BIGINT,
	ADD CONSTRAINT fk_users_currency
		FOREIGN KEY (currency_id)
		REFERENCES currency (id)
		ON DELETE SET NULL
		ON UPDATE CASCADE;

-- Create Index because we use pgsql, it prevents full table scan
CREATE INDEX idx_users_currency_id ON users (currency_id);
