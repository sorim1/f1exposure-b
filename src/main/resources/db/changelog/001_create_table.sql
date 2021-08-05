CREATE TABLE EXPOSED_VOTE (
	id  SERIAL PRIMARY KEY,
	drivers text[],
    ip_address varchar(40),
    season numeric,
    round numeric,
    insert_timestamp timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE APP_PROPERTIES (
  name text,
  value text
);
CREATE TABLE EXPOSED (
     season numeric,
     round numeric,
     driver text,
     counter numeric
);

CREATE TABLE exposed_vote_totals (
   season numeric NOT NULL,
   round numeric NOT NULL,
   voters numeric,
   votes numeric NULL
);

CREATE TABLE exposure_championship (
     season numeric NOT NULL,
     round numeric NOT NULL,
     driver text,
     exposure numeric NULL,
     color text
);

CREATE TABLE exposure_championship_standings (
   season numeric NOT NULL,
   driver text NOT NULL,
   exposure numeric NULL,
   full_name text NULL,
);


CREATE TABLE DRIVERS (
      code text,
      ergast_code text,
      full_name text
);

CREATE TABLE CONSTRUCTORS (
     id  SERIAL PRIMARY KEY,
     name text
);

CREATE TABLE CALENDAR (
     race_id numeric,
     practice1 TIMESTAMP,
     practice2 TIMESTAMP,
     practice3 TIMESTAMP,
     qualifying TIMESTAMP,
     race TIMESTAMP,
     practice1original TIMESTAMP,
     practice2original TIMESTAMP,
     practice3original TIMESTAMP,
     qualifying_original TIMESTAMP,
     race_original TIMESTAMP,
     location text,
     summary text
);

CREATE TABLE CURRENT_DRIVER_STANDINGS (
   id text,
   position numeric,
   name text,
   code text,
   nationality text,
   car text,
   points numeric,
   wins numeric,
   permanent_number numeric
);

CREATE TABLE DRIVER_STANDINGS_BY_ROUND (
        season numeric,
        round numeric,
          id text,
          position numeric,
          name text,
          code text,
          nationality text,
          car text,
          points numeric,
        points_this_round numeric,
        result_this_round numeric,
          wins numeric,
          permanent_number numeric,
        color text
);


CREATE TABLE CURRENT_CONSTRUCTOR_STANDINGS (
  id text,
  position numeric,
  name text,
  points numeric,
  wins numeric
);

CREATE TABLE CONSTRUCTOR_STANDINGS_BY_ROUND (
      season numeric,
      round numeric,
       id text,
       position numeric,
       name text,
       points numeric,
      points_this_round numeric,
       wins numeric,
      color text
);


CREATE TABLE SS_EVENT (
   id numeric,
   name text,
   event_group numeric
);

CREATE TABLE SS_STREAM (
   id numeric,
   name text,
   event numeric,
   url text
);

CREATE TABLE COMMENTS (
  id  SERIAL PRIMARY KEY,
  page numeric,
  nickname text,
  comment text,
  status numeric,
  timestamp TIMESTAMP
);
