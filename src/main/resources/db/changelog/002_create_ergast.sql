CREATE TABLE ERGAST_CURRENT_SEASON_RACES (
       round numeric,
       season text,
       url text,
       race_name text,
       circuit text,
       date text,
       time text
);

CREATE TABLE ERGAST_CIRCUIT (
                 round numeric,
                 circuit_id text,
                 url text,
                 circuit_name text,
                 race numeric
);

CREATE TABLE ERGAST_LOCATION (
             locality text,
             lat text,
             longitude text,
             country text,
             circuit text
);

