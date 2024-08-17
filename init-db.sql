CREATE TABLE translations (
    id SERIAL PRIMARY KEY,
    original_text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    user_ip VARCHAR(45) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
