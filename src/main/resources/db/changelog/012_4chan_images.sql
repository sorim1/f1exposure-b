ALTER TABLE fourchan_posts RENAME COLUMN thread TO tags;
ALTER TABLE fourchan_posts ALTER COLUMN tags TYPE text USING tags::text;
ALTER TABLE fourchan_posts ADD status numeric NULL;
