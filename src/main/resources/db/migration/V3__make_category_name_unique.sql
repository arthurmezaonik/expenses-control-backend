CREATE UNIQUE INDEX uq_categories_name_normalized
    ON categories (LOWER(BTRIM(name)));