CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
--    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  VARCHAR(500),
    requester_id BIGINT,
    CONSTRAINT fk_request_to_user FOREIGN KEY (requester_id) REFERENCES users (id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(500) NOT NULL,
    is_available boolean,
    owner_id     BIGINT,
    request_id   BIGINT,
    CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES requests (id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id),
    UNIQUE (id)
);
CREATE TYPE booking_status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     boolean,
    CONSTRAINT fk_booking_to_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_booking_to_user FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      VARCHAR(500),
    item_id   BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES items (id)
);
