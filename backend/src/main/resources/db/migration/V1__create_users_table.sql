CREATE TABLE currencies (
	id BIGSERIAL PRIMARY KEY,
	code VARCHAR(3) NOT NULL UNIQUE, -- ISO 4217
	symbol VARCHAR(5) NOT NULL,
	name VARCHAR NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR(254) UNIQUE NOT NULL,
	password VARCHAR(60) NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	currency_id BIGINT,

	CONSTRAINT fk_users_currency
		FOREIGN KEY (currency_id)
		REFERENCES currencies (id)
		ON DELETE SET NULL
		ON UPDATE CASCADE
);

-- Create Index to prevent full table scan
CREATE INDEX idx_users_currency_id ON users(currency_id);
