CREATE TYPE sock_color AS ENUM ('black','white', 'green', 'yellow', 'blue', 'red', 'purple');

CREATE TABLE socks (
    id SERIAL primary key,
    color sock_color,
    cotton_part INTEGER CHECK (cotton_part >= 0 AND cotton_part <= 100),
    quantity INTEGER CHECK (quantity > 0)
)