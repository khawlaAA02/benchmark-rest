-- Schéma Category (1) — Item (N)
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS category CASCADE;

CREATE TABLE category (
  id          BIGSERIAL PRIMARY KEY,
  code        VARCHAR(32) UNIQUE NOT NULL,
  name        VARCHAR(128)      NOT NULL,
  updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE item (
  id          BIGSERIAL PRIMARY KEY,
  sku         VARCHAR(64) UNIQUE NOT NULL,
  name        VARCHAR(128)      NOT NULL,
  price       NUMERIC(10,2)     NOT NULL,
  stock       INT               NOT NULL,
  category_id BIGINT            NOT NULL REFERENCES category(id),
  updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Index utiles
CREATE INDEX idx_item_category   ON item(category_id);
CREATE INDEX idx_item_updated_at ON item(updated_at);

-- Remplissage : 2 000 catégories (CAT0001..CAT2000)
INSERT INTO category (code, name)
SELECT
  'CAT' || LPAD(gs::text, 4, '0') AS code,
  'Category ' || gs
FROM generate_series(1, 2000) AS gs;

-- Remplissage : ~100 000 items ≈ 50 / catégorie
WITH dist AS (
  SELECT id AS category_id, 50 AS items_for_cat
  FROM category
),
gen AS (
  SELECT d.category_id, gs AS n
  FROM dist d
  JOIN LATERAL generate_series(1, d.items_for_cat) gs ON TRUE
)
INSERT INTO item (sku, name, price, stock, category_id)
SELECT
  'SKU-' || category_id || '-' || n,
  'Item ' || category_id || '-' || n,
  ROUND((10 + random()*490)::numeric, 2) AS price,   -- 10.00..500.00
  (10 + (random()*190))::int             AS stock,   -- 10..200
  category_id
FROM gen
LIMIT 100000;

-- Vérifications
-- SELECT COUNT(*) AS cat_count FROM category;
-- SELECT COUNT(*) AS item_count FROM item;
