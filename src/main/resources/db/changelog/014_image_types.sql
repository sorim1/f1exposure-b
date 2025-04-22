ALTER TABLE f1exposure.images ADD COLUMN "type" text DEFAULT 'image/jpeg';
UPDATE f1exposure.images SET type='image/jpeg';