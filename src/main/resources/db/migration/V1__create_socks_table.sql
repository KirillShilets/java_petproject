CREATE TABLE socks (
    id SERIAL NOT NULL PRIMARY KEY ,
    color VARCHAR(255) NOT NULL,
    cotton_part INTEGER NOT NULL CHECK (cotton_part BETWEEN 0 AND 100),
    quantity INTEGER NOT NULL CHECK (quantity > 0)
)