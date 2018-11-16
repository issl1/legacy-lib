ALTER TABLE tu_dealer DROP column ins_comp_id;

ALTER TABLE tu_asset_model ADD COLUMN eng_id bigint;
Update tu_asset_model set eng_id = 1;
ALTER TABLE tu_asset_model ALTER COLUMN eng_id SET NOT NULL;
