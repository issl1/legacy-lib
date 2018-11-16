INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- GL Thai - Contract Statuses
	(218, 'SALELOST', 'Sale lost and cut lost', 'Sale lost and cut lost',               1, 1, now(), 'admin', now(), 'admin'),
	(219, 'LEGAL', 'Legal', 'Legal',                                                    1, 1, now(), 'admin', now(), 'admin');