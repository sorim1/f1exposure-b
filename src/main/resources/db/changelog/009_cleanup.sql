ALTER TABLE f1exposure.aws RENAME TO news;
ALTER TABLE f1exposure.aws_comment RENAME TO news_comment;
ALTER TABLE f1exposure.reddit_posts_new RENAME TO reddit_posts;
ALTER TABLE f1exposure.ss_event RENAME TO streamable;
ALTER TABLE f1exposure.streamable RENAME COLUMN "name" TO "text";
ALTER TABLE f1exposure.streamable RENAME COLUMN event_group TO thread;
ALTER TABLE f1exposure.ss_stream RENAME TO fourchan_posts;
ALTER TABLE f1exposure.fourchan_posts RENAME COLUMN "name" TO "text";
ALTER TABLE f1exposure.fourchan_posts RENAME COLUMN "event" TO thread;
UPDATE f1exposure.fourchan_posts SET "text"='';
DELETE FROM f1exposure.reddit_posts_top;
ALTER TABLE f1exposure.reddit_posts_top RENAME TO mercury_table;
ALTER TABLE f1exposure.mercury_table RENAME COLUMN url TO text2;
ALTER TABLE f1exposure.mercury_table RENAME COLUMN image_url TO text3;
ALTER TABLE f1exposure.mercury_table RENAME COLUMN title TO text1;
ALTER TABLE f1exposure.mercury_table RENAME COLUMN created TO number1;
ALTER TABLE f1exposure.mercury_table RENAME COLUMN "type" TO number2;



