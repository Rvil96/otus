CREATE SEQUENCE phone_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE phone (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('phone_seq'),
    number VARCHAR(50) NOT NULL,
    client_id BIGINT
);

ALTER TABLE phone
ADD CONSTRAINT fk_phone_client
FOREIGN KEY (client_id) REFERENCES client(id);

INSERT INTO phone (number, client_id)
VALUES ('+7 (999) 000 00 00', 1);
