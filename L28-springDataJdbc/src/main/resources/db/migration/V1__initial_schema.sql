CREATE TABLE client (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE address (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    street VARCHAR(255) NOT NULL,
    client_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_address_client FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE phone (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    number VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT fk_phone_client FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

INSERT INTO client (name) VALUES ('John Doe');

INSERT INTO address (street, client_id) VALUES ('123 Main Street', 1);

INSERT INTO phone (number, client_id) VALUES ('+1234567890', 1);
INSERT INTO phone (number, client_id) VALUES ('+0987654321', 1);
