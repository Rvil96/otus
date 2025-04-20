CREATE SEQUENCE address_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE address (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('address_seq'),
    street VARCHAR(255) NOT NULL
);

ALTER TABLE client ADD COLUMN address_id BIGINT;

ALTER TABLE client
ADD CONSTRAINT fk_client_address
FOREIGN KEY (address_id) REFERENCES address(id);

WITH inserted_address AS (
    INSERT INTO address (street)
    VALUES ('ул. Пушкина, д. 10')
    RETURNING id
)

UPDATE client
SET address_id = (SELECT id FROM inserted_address)
WHERE login = 'admin';