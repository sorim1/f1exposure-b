CREATE TABLE EXPOSED_VOTE (
	id  SERIAL PRIMARY KEY,
	drivers text[],
    ip_address varchar(40),
    race_id varchar(60),
    insert_timestamp timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE APP_PROPERTIES (
  name text,
  value text
);
CREATE TABLE EXPOSED (
     id  SERIAL PRIMARY KEY,
     race_id text,
     driver_code text,
     counter numeric
);
CREATE TABLE DRIVERS (
      code text,
      first_name text,
      last_name text,
      full_name text
);

CREATE TABLE CONSTRUCTORS (
     id  SERIAL PRIMARY KEY,
     name text
);

CREATE TABLE CALENDAR (
     id  SERIAL PRIMARY KEY,
     race_id numeric,
     practice1 TIMESTAMP,
     practice2 TIMESTAMP,
     practice3 TIMESTAMP,
     qualifying TIMESTAMP,
     race TIMESTAMP,
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

CREATE TABLE CURRENT_CONSTRUCTOR_STANDINGS (
  id text,
  position numeric,
  name text,
  points numeric,
  wins numeric
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
  timestamp TIMESTAMP
);
