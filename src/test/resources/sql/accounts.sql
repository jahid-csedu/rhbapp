delete from accounts;
delete from customers;

INSERT INTO customers (id, name, email, create_date, last_modify_date) VALUES (1, 'Customer 1', 'customer1@gmail.com', '2024-01-01 00:00:00', '2024-01-02 00:00:00');
INSERT INTO customers (id, name, email, create_date, last_modify_date) VALUES (2, 'Customer 2', 'customer2@gmail.com', '2024-01-01 00:00:00', '2024-01-02 00:00:00');

INSERT INTO accounts (id, account_number, balance, customer_id, create_date, last_modify_date) VALUES (3, '1234567890', 1000.0, 1, '2024-01-01 00:00:00', '2024-01-02 00:00:00');
INSERT INTO accounts (id, account_number, balance, customer_id, create_date, last_modify_date) VALUES (4, '2987654321', 2000.0, 1, '2024-01-01 00:00:00', '2024-01-02 00:00:00');
