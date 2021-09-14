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