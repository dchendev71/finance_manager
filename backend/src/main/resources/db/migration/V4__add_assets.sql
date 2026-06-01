INSERT INTO assets (name, ticker_symbol, asset_types) VALUES
('NVIDIA', 'NVDA', 'stock'),
('APPLE', 'APPL', 'stock'),
('AMAZON', 'AMZN', 'stock'),
('MICROSOFT', 'MSFT', 'stock'),
('META', 'META', 'stock'),
('BITCOIN', 'BTC', 'cryptocurrency'),
('ETHEREUM', 'ETH', 'cryptocurrency'),
('RIPPLE', 'XRP', 'cryptocurrency'),
('CARDANO', 'ADA', 'cryptocurrency')
ON CONFLICT DO NOTHING;
