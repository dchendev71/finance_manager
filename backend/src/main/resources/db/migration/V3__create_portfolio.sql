CREATE TABLE portfolios (
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

CREATE TABLE asset_types (
	type VARCHAR(255) PRIMARY KEY -- stock, cryptocurrency, etf ...
);

INSERT INTO asset_types (type) VALUES
('stock'),
('cryptocurrency'),
('etf');

CREATE TABLE assets (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) UNIQUE NOT NULL,
	ticker_symbol VARCHAR(255) NOT NULL,

	asset_types VARCHAR(255),

	CONSTRAINT fk_assets_asset_types
		FOREIGN KEY (asset_types)
		REFERENCES asset_types (type)
);

CREATE TABLE portfolio_assets (
	id BIGSERIAL PRIMARY KEY,
	portfolio_id BIGINT,
	
	CONSTRAINT fk_portfolios_assets_portfolios
		FOREIGN KEY(portfolio_id)
		REFERENCES portfolios (id)
		ON DELETE CASCADE,

	asset_id BIGINT,

	CONSTRAINT fk_portfolio_assets_assets
		FOREIGN KEY (asset_id)
		REFERENCES assets (id)
		ON DELETE CASCADE,

	quantity NUMERIC(18, 8) NOT NULL
);
CREATE TABLE portfolio_asset_mean_prices (
	id BIGSERIAL PRIMARY KEY,

	portfolio_asset_id BIGINT NOT NULL,

	CONSTRAINT fk_portfolio_assets_mean_prices_portfolio_assets
		FOREIGN KEY(portfolio_asset_id)
		REFERENCES portfolio_assets (id)
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
		REFERENCES assets (id)
		ON DELETE CASCADE
);
