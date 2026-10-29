CREATE TABLE activation_requests
(
    id UUID PRIMARY KEY,

    external_id VARCHAR(100) NOT NULL,

    customer_id VARCHAR(100) NOT NULL,

    product_code VARCHAR(100) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    version BIGINT NOT NULL
);

ALTER TABLE activation_requests
    ADD CONSTRAINT uk_external_id
        UNIQUE (external_id);