ALTER TABLE driver_standings_by_round ADD grid numeric NULL;
ALTER TABLE driver_standings_by_round ADD status text NULL;
ALTER TABLE driver_standings_by_round ADD result_this_round_text text NULL;
ALTER TABLE driver_standings_by_round ADD result_this_round_dnf numeric NULL;

CREATE TABLE json_repository (
    id text NOT NULL,
    "json" jsonb NULL,
    status numeric NULL,
    CONSTRAINT json_repository_pkey PRIMARY KEY (id)
);
