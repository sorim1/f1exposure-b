ALTER TABLE aws ADD image_url text NULL;
ALTER TABLE aws ADD icon_url text NULL;

CREATE TABLE replays (
     id serial NOT NULL,
     title text,
     url text,
     status numeric,
     CONSTRAINT replays_pkey PRIMARY KEY (id)
);
