-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL
);

-- Events table
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATETIME NOT NULL,
    total_seats INTEGER NOT NULL,
    ticket_price DECIMAL(10,2) NOT NULL
);

-- Tickets table
CREATE TABLE tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    user_id BIGINT,
    status VARCHAR(20) NOT NULL,
    booking_date DATETIME NOT NULL,
    seat_number VARCHAR(50) NOT NULL,
    UNIQUE(event_id, seat_number),
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Indexes
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_event_date ON events(event_date);
CREATE INDEX idx_ticket_status ON tickets(status);
CREATE INDEX idx_ticket_user ON tickets(user_id);
CREATE INDEX idx_ticket_event ON tickets(event_id); 