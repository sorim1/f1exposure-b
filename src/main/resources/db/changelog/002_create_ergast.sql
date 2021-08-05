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
       timing_app_data text,
       circuit_id text,
       race_analysis jsonb,
);

CREATE TABLE INSTAGRAM_POSTS (
     code text,
     taken_at numeric,
     likes numeric,
     comments numeric,
     post_type numeric,
     url text,
     location text,
     caption text,
     username text,
     userpic text
);

CREATE TABLE TWITTER_POSTS (
     id numeric,
     text text,
     favorite_count numeric,
     retweet_count numeric,
     url text,
     media_url text,
     username text,
     user_picture text,
     source numeric,
     created_at TIMESTAMP
);

CREATE TABLE IMAGES (
     code text,
     image bytea
);

CREATE TABLE log_table (
      id  SERIAL PRIMARY KEY,
      code text,
      message text,
      created timestamp(0) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE aws (
      code text NOT NULL,
      title text NULL,
      url text NULL,
      text_content text NULL,
      comment_count numeric NULL,
      timestamp_created timestamp NULL DEFAULT NOW(),
      timestamp_activity timestamp NULL DEFAULT NOW(),
      username text NULL,
      status numeric,
      CONSTRAINT aws_pkey PRIMARY KEY (code)
);

CREATE TABLE AWS_COMMENT (
     id  SERIAL PRIMARY KEY,
     text_content text NULL,
     content_code text NULL,
     timestamp_created timestamp NULL DEFAULT NOW(),
     username text NULL,
     status numeric
);
