ALTER TABLE td_lock_split DROP CONSTRAINT uk_60vlsgox0jnrmman6p7ojfwk6;

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(705, 'LCAN', 'Canceled',      'Canceled',                                         1, 1, now(), 'admin', now(), 'admin');