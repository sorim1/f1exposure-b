CREATE TABLE RACE_DATA
(
    id              SERIAL,
    round           numeric,
    season          text,
    url             text,
    image_url       text,
    race_name       text,
    circuit         jsonb,
    date            text,
    time            text,
    race_analysis   jsonb,
    upcoming_race_analysis jsonb,
    live_timing_race     text,
    timing_app_data text,
    circuit_id      text,
    live_timing_quali text NULL,
    live_timing_fp3 text NULL,
    live_timing_fp2 text NULL,
    live_timing_fp1 text NULL,
    live_timing_sprint_quali text NULL,
    CONSTRAINT RACE_DATA_pkey PRIMARY KEY (season, round)
);

CREATE TABLE INSTAGRAM_POSTS
(
    code      text,
    taken_at  numeric,
    likes     numeric,
    comments  numeric,
    post_type numeric,
    url       text,
    location  text,
    caption   text,
    username  text,
    userpic   text,
    CONSTRAINT INSTAGRAM_POSTS_pkey PRIMARY KEY (code)
);

CREATE TABLE TWITTER_POSTS
(
    id             numeric,
    text           text,
    favorite_count numeric,
    retweet_count  numeric,
    url            text,
    media_url      text,
    username       text,
    user_picture   text,
    source         numeric,
    created_at     TIMESTAMP
);

CREATE TABLE IMAGES
(
    code  text,
    image bytea,
    CONSTRAINT IMAGES_pkey PRIMARY KEY (code)
);

CREATE TABLE log_table
(
    id      SERIAL PRIMARY KEY,
    code    text,
    message text,
    created timestamp(0) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE aws
(
    code               text NOT NULL,
    title              text NULL,
    url                text NULL,
    text_content       text NULL,
    comment_count      numeric NULL,
    timestamp_created  timestamp NULL DEFAULT NOW(),
    timestamp_activity timestamp NULL DEFAULT NOW(),
    username           text NULL,
    ip                text NULL,
    status             numeric,
    CONSTRAINT aws_pkey PRIMARY KEY (code)
);

CREATE TABLE AWS_COMMENT
(
    id                SERIAL PRIMARY KEY,
    text_content      text NULL,
    content_code      text NULL,
    timestamp_created timestamp NULL DEFAULT NOW(),
    username          text NULL,
    ip                text NULL,
    status            numeric
);

CREATE TABLE MARKETING
(
    id        SERIAL PRIMARY KEY,
    url      text,
    image_url  text,
    description   text
);

CREATE TABLE REDDIT_POSTS_TOP
(
    id       text,
    url      text,
    image_url  text,
    title   text,
    created numeric,
    type numeric,
    CONSTRAINT REDDIT_POSTS_TOP_pkey PRIMARY KEY (id)
);

CREATE TABLE REDDIT_POSTS_NEW
(
    id       text,
    url      text,
    image_url  text,
    title   text,
    created numeric,
    type numeric,
    CONSTRAINT REDDIT_POSTS_NEW_pkey PRIMARY KEY (id)
);

CREATE TABLE BANLIST
(
    id       text,
    ip      text,
    username      text,
    timestamp_created timestamp NULL DEFAULT NOW(),
    status numeric,
    CONSTRAINT BANLIST_pkey PRIMARY KEY (id)
);