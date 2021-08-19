CREATE TABLE EXPOSED_VOTE
(
    id               SERIAL,
    drivers          text[],
    ip_address       varchar(40),
    season           numeric,
    round            numeric,
    insert_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT EXPOSED_VOTE_pkey PRIMARY KEY (id)
);

CREATE TABLE APP_PROPERTIES
(
    name  text,
    value text,
    CONSTRAINT APP_PROPERTIES_pkey PRIMARY KEY (name)
);
CREATE TABLE EXPOSED
(
    season  numeric,
    round   numeric,
    driver  text,
    counter numeric,
    CONSTRAINT EXPOSED_pkey PRIMARY KEY (season, round, driver)
);

CREATE TABLE exposed_vote_totals
(
    season numeric NOT NULL,
    round  numeric NOT NULL,
    voters numeric,
    votes  numeric NULL,
    CONSTRAINT exposed_vote_totals_pkey PRIMARY KEY (season, round)
);

CREATE TABLE exposure_championship
(
    season   numeric NOT NULL,
    round    numeric NOT NULL,
    driver   text,
    exposure numeric NULL,
    color    text,
    CONSTRAINT exposure_championship_pkey PRIMARY KEY (season, round, driver)
);

CREATE TABLE exposure_championship_standings
(
    season    numeric NOT NULL,
    driver    text    NOT NULL,
    exposure  numeric NULL,
    full_name text NULL,
    CONSTRAINT exposure_championship_standings_pkey PRIMARY KEY (season, driver)
);


CREATE TABLE DRIVERS
(
    code        text,
    ergast_code text,
    full_name   text,
    CONSTRAINT DRIVERS_pkey PRIMARY KEY (code)
);

CREATE TABLE CONSTRUCTORS
(
    id   SERIAL,
    name text,
    CONSTRAINT CONSTRUCTORS_pkey PRIMARY KEY (name)
);

CREATE TABLE CALENDAR
(
    race_id             numeric,
    practice1           TIMESTAMP,
    practice2           TIMESTAMP,
    practice3           TIMESTAMP,
    qualifying          TIMESTAMP,
    race                TIMESTAMP,
    practice1original   TIMESTAMP,
    practice2original   TIMESTAMP,
    practice3original   TIMESTAMP,
    qualifying_original TIMESTAMP,
    race_original       TIMESTAMP,
    location            text,
    summary             text,
    CONSTRAINT CALENDAR_pkey PRIMARY KEY (race_id)
);

CREATE TABLE CURRENT_DRIVER_STANDINGS
(
    id               text,
    position         numeric,
    name             text,
    code             text,
    ergast_code      text,
    nationality      text,
    car              text,
    driver_url       text,
    constructor_url  text,
    points           numeric,
    wins             numeric,
    permanent_number numeric,
    CONSTRAINT CURRENT_DRIVER_STANDINGS_pkey PRIMARY KEY (code)
);

CREATE TABLE DRIVER_STANDINGS_BY_ROUND
(
    season            numeric,
    round             numeric,
    id                text,
    position          numeric,
    name              text,
    code              text,
    nationality       text,
    car               text,
    points            numeric,
    points_this_round numeric,
    result_this_round numeric,
    wins              numeric,
    permanent_number  numeric,
    color             text,
    CONSTRAINT DRIVER_STANDINGS_BY_ROUND_pkey PRIMARY KEY (season, round, code)
);


CREATE TABLE CURRENT_CONSTRUCTOR_STANDINGS
(
    id       text,
    position numeric,
    name     text,
    points   numeric,
    wins     numeric,
    url     text,
    CONSTRAINT CURRENT_CONSTRUCTOR_STANDINGS_pkey PRIMARY KEY (name)
);

CREATE TABLE CONSTRUCTOR_STANDINGS_BY_ROUND
(
    season            numeric,
    round             numeric,
    id                text,
    position          numeric,
    name              text,
    points            numeric,
    points_this_round numeric,
    wins              numeric,
    color             text,
    CONSTRAINT CONSTRUCTOR_STANDINGS_BY_ROUND_pkey PRIMARY KEY (season, round, name)
);


CREATE TABLE SS_EVENT
(
    id          numeric,
    name        text,
    event_group numeric
);

CREATE TABLE SS_STREAM
(
    id    numeric,
    name  text,
    event numeric,
    url   text
);

CREATE TABLE COMMENTS
(
    id        SERIAL PRIMARY KEY,
    page      numeric,
    nickname  text,
    comment   text,
    status    numeric,
    timestamp TIMESTAMP
);
