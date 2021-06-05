CREATE TABLE EXPOSED_VOTE (
	id  SERIAL PRIMARY KEY,
	drivers text[],
    ip_address varchar(40),
    insert_timestamp timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE EXPOSED (
     id  SERIAL PRIMARY KEY,
     race_id numeric,
     driver_id numeric,
     counter numeric
);
CREATE TABLE DRIVERS (
      id  SERIAL PRIMARY KEY,
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
   id numeric,
   position numeric,
   name text,
   code text,
   nationality text,
   car text,
   points numeric
);

CREATE TABLE CURRENT_CONSTRUCTOR_STANDINGS (
  id numeric,
  position numeric,
  name text,
  points numeric
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
