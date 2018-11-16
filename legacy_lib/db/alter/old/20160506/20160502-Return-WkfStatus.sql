INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	 -- Return Status
    (1400, 'REPEN',     'Pending',     'Pending',     1, 1, now(), 'admin', now(), 'admin'),
    (1401, 'RECLO',     'Closed',     'Closed',     1, 1, now(), 'admin', now(), 'admin');