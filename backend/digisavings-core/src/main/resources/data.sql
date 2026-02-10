-- Use this for manual testing

INSERT INTO users (id, name, email, password, role, created_at)
VALUES
(2, 'Alice Johnson', 'alice@example.com', '$2a$10$abcdefghijklmnopqrstuv', 'USER', NOW()),
(3, 'Bob Smith', 'bob@example.com', '$2a$10$abcdefghijklmnopqrstuv', 'USER', NOW());

INSERT INTO account_applications
(id, user_id, account_type, application_status, application_date, review_date, reviewed_by)
VALUES
(1, 2, 'SAVINGS', 'PENDING', NOW(), NULL, NULL),
(2, 3, 'SAVINGS', 'APPROVED', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 'superadmin@example.com');


INSERT INTO savings_accounts
(id, account_number, user_id, balance, status, interest_rate, opened_date)
VALUES
(1, 'SA1000001', 3, 1000.00, 'ACTIVE', 3.50, NOW() - INTERVAL '1 day');
