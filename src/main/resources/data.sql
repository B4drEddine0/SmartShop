INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, role)
VALUES ('user', 'user123', 'CLIENT')
ON CONFLICT (username) DO NOTHING;
