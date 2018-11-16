ALTER TABLE tu_asset_make ALTER COLUMN ass_mak_code DROP not null;
ALTER TABLE tu_asset_range ALTER COLUMN ass_ran_code DROP not null;

ALTER TABLE "tu_asset_make" DROP CONSTRAINT "uk_e9ht1i0q4nfxmpoku8v7iu9nj";
ALTER TABLE "tu_asset_range" DROP CONSTRAINT "uk_78gsrr65c65i8tdw4oc2nwoop";