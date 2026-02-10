CREATE TABLE account_applications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    application_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    review_date TIMESTAMP,
    reviewed_by VARCHAR(20),
    CONSTRAINT fk_account_application_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_account_apps_user_id ON account_applications(user_id);