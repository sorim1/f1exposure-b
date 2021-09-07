delete from calendar;
ALTER TABLE calendar ADD practice1name text NULL;
ALTER TABLE calendar ADD practice2name text NULL;
ALTER TABLE calendar ADD practice3name text NULL;
ALTER TABLE calendar ADD qualifying_name text NULL;
ALTER TABLE calendar ADD race_name text NULL;
ALTER TABLE race_data ADD live_timing_sprint_quali text NULL;
