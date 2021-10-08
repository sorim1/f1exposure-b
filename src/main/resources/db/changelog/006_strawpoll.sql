ALTER TABLE exposure_championship ADD "name" text NULL;
ALTER TABLE exposure_championship ADD status numeric NULL;
ALTER TABLE exposure_championship ADD votes numeric NULL;
ALTER TABLE exposed_vote_totals ADD strawpoll text NULL;
UPDATE exposure_championship SET status=3;
