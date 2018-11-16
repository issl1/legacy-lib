INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(805, 'UNMATCHED',         'Unmatched',         'Unmatched',                       1, 1, now(), 'admin', now(), 'admin'),
	(806, 'SUSPENDED',         'Suspended',         'Suspended',                       1, 1, now(), 'admin', now(), 'admin'),
	(807, 'MATCHED',           'Matched',           'Matched',                         1, 1, now(), 'admin', now(), 'admin');

UPDATE ts_wkf_status SET ref_code='OVER', ref_desc='Over', ref_desc_en='Over' WHERE wkf_sta_id = 803;


