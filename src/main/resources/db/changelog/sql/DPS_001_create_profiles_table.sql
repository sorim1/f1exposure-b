CREATE TABLE profiles (
	id  SERIAL PRIMARY KEY NOT NULL,
	sub varchar(50) NOT NULL,
	"version" numeric NULL,
	profile jsonb NULL,
    metadata jsonb NULL,
    last_change_source varchar(100) NULL,
    last_change_timestamp timestamp NULL,
    oib varchar(11) NULL,
    manageableAssetIds jsonb NULL,
	UNIQUE (sub)
);

CREATE INDEX profiles_metadata_gin ON "profiles" USING gin ((metadata->'keys') jsonb_path_ops);
CREATE INDEX profiles_metadata_username ON "profiles" ((profile ->> 'username'));
CREATE INDEX "profiles_sub" ON "profiles" ("sub");
CREATE INDEX "profiles_oib" ON "profiles" ("oib");
CREATE INDEX profiles_manageableAssetIds ON "profiles" USING gin (manageableAssetIds jsonb_path_ops);
