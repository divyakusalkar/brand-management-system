-- Seed Chain (Company) data
INSERT IGNORE INTO chain (chain_id, chain_name, is_active, created_at, updated_at)
VALUES
  (1, 'Alpha Group',    true, NOW(), NOW()),
  (2, 'Beta Holdings',  true, NOW(), NOW()),
  (3, 'Gamma Corp',     true, NOW(), NOW()),
  (4, 'Delta Ventures', true, NOW(), NOW()),
  (5, 'Epsilon Ltd',    true, NOW(), NOW());

-- Seed Brand data
INSERT IGNORE INTO brand (brand_name, chain_id, is_active, created_at, updated_at)
VALUES
  ('Alpha Brand A',  1, true, NOW(), NOW()),
  ('Alpha Brand B',  1, true, NOW(), NOW()),
  ('Beta Brand X',   2, true, NOW(), NOW()),
  ('Gamma Brand One',3, true, NOW(), NOW()),
  ('Delta Prime',    4, true, NOW(), NOW());
