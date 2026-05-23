CREATE TABLE portfolio (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	
	created_at TIMESTAMP NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
	user_id BIGINT,

	CONSTRAINT fk_portfolio_users
		FOREIGN KEY (user_id)
		REFERENCES users (id)
		ON DELETE CASCADE
);

CREATE TABLE asset_type (
	type VARCHAR(255) PRIMARY KEY -- stock, cryptocurrency, etf ...
);

INSERT INTO asset_type (type) VALUES
('stock'),
('cryptocurrency'),
('etf');

CREATE TABLE asset (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) UNIQUE NOT NULL,
	ticker_symbol VARCHAR(255) NOT NULL,

	asset_type VARCHAR(255),

	CONSTRAINT fk_asset_asset_type
		FOREIGN KEY (asset_type)
		REFERENCES asset_type (type)
);

CREATE TABLE portfolio_asset (
	id BIGSERIAL PRIMARY KEY,
	portfolio_id BIGINT,
	
	CONSTRAINT fk_portfolio_asset_portfolio
		FOREIGN KEY(portfolio_id)
		REFERENCES portfolio (id)
		ON DELETE CASCADE,

	asset_id BIGINT,

	CONSTRAINT fk_portfolio_asset_asset
		FOREIGN KEY (asset_id)
		REFERENCES asset (id)
		ON DELETE CASCADE,

	quantity NUMERIC(18, 8) NOT NULL
);
CREATE TABLE portfolio_asset_mean_price (
	id BIGSERIAL PRIMARY KEY,

	portfolio_asset_id BIGINT NOT NULL,

	CONSTRAINT fk_portfolio_asset_mean_price_portfolio_asset
		FOREIGN KEY(portfolio_asset_id)
		REFERENCES portfolio_asset (id)
		ON DELETE CASCADE,

	mean_price NUMERIC(18, 8) NOT NULL
);

CREATE TABLE transactions (
	id BIGSERIAL PRIMARY KEY,
	quantity NUMERIC(18, 8), -- + is purchase, - is sell
	transaction_date TIMESTAMP NOT NULL DEFAULT NOW(),
	unit_price NUMERIC(18, 8),

	user_id BIGINT NOT NULL,
	asset_id BIGINT NOT NULL,

	CONSTRAINT fk_transactions_user
		FOREIGN KEY (user_id)
		REFERENCES users (id)
		ON DELETE CASCADE,

	CONSTRAINT fk_transactions_asset 
		FOREIGN KEY (asset_id)
		REFERENCES asset (id)
		ON DELETE CASCADE
);
