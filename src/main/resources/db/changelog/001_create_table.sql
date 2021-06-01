CREATE TABLE EXPOSED_VOTE (
	id  SERIAL PRIMARY KEY,
	drivers text[],
    ip_address varchar(40),
    insert_timestamp timestamp DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE EXPOSED (
     id  SERIAL PRIMARY KEY,
     match_id numeric,
     driver_id numeric,
     counter numeric
);
CREATE TABLE DRIVERS (
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
