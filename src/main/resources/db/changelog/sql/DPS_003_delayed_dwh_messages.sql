CREATE TABLE delayed_dwh_messages (
    id serial NOT NULL,
    timestamp_created timestamp NOT NULL DEFAULT NOW(),
    timestamp_sent timestamp NULL,
    message jsonb NOT NULL,
    headers jsonb NOT NULL,
    key text,
    CONSTRAINT delayed_dwh_messages_pkey PRIMARY KEY (id)
);
