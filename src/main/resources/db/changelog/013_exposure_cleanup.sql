DROP TABLE f1exposure.exposed;
DROP TABLE f1exposure.exposed_vote;
CREATE TABLE json_repository_2023 (
     id text NOT NULL,
     "json" jsonb NULL,
     status numeric NULL,
     CONSTRAINT json_repository_2023_pkey PRIMARY KEY (id)
);
