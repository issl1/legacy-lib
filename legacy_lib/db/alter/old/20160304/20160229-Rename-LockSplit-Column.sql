ALTER TABLE td_lock_split RENAME COLUMN loc_spl_dt_payment TO loc_spl_dt_from;
ALTER TABLE td_lock_split RENAME COLUMN loc_spl_dt_expiry TO loc_spl_dt_to;