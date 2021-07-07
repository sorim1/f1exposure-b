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

CREATE TABLE INSTAGRAM_POSTS (
     code text,
     likes numeric,
     comments numeric,
     post_type numeric,
     url text,
     location text,
     username text,
     userpic text
);

CREATE TABLE log_table (
      id  SERIAL PRIMARY KEY,
      code text,
      message text,
      created timestamp(0) DEFAULT CURRENT_TIMESTAMP
);


