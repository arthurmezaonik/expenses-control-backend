-- Category
CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at  TIMESTAMP WITHOUT TIME ZONE
);

-- Expense
CREATE TABLE expenses (
    id UUID PRIMARY KEY,
    type VARCHAR(32) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    date DATE NOT NULL,
    category_id UUID NOT NULL,
    note VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(32) NOT NULL,

    CONSTRAINT fk_category_id
        FOREIGN KEY (category_id)
            REFERENCES categories(id)
);