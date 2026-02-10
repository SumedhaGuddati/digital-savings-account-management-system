CREATE TABLE account_transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),

    CONSTRAINT fk_account
        FOREIGN KEY (account_id) REFERENCES savings_accounts(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_account_transactions_account_id
ON account_transactions(account_id);