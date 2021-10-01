CREATE TABLE ART_IMAGES
(
    code  text,
    image bytea,
    season numeric,
    round numeric,
    title text,
    status numeric,
    created timestamp(0) DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ART_IMAGES_pkey PRIMARY KEY (code)
);

