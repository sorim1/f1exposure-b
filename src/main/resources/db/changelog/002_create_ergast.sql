CREATE TABLE ERGAST_CURRENT_SEASON_RACES (
       id  SERIAL PRIMARY KEY,
       round numeric,
       season text,
       url text,
       race_name text,
       circuit jsonb,
       date text,
       time text,
       live_timing text,
       circuit_id text
);

