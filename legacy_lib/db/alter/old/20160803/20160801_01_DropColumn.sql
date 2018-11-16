ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS info;
ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS label;
ALTER TABLE td_account_ledger DROP COLUMN IF EXISTS otherinfo;

ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_cat_id;
ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_desc;
ALTER TABLE tu_account DROP COLUMN IF EXISTS acc_desc_en;

ALTER TABLE td_journal_entry DROP COLUMN IF EXISTS par_id;

ALTER TABLE tu_journal_event_account ADD COLUMN jou_eve_acc_sort_index INTEGER;

ALTER TABLE td_address DROP COLUMN IF EXISTS are_id;

ALTER TABLE td_address_arc DROP COLUMN IF EXISTS are_id;
ALTER TABLE td_address_arc DROP COLUMN IF EXISTS add_type;
ALTER TABLE td_address_arc DROP COLUMN IF EXISTS old_id;

DROP TABLE td_sale_lost_cut_lost cascade;
DROP TABLE td_promise;