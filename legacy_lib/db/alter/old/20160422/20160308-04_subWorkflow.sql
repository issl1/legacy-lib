﻿INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(230, 'PEN_TRAN', 'Pending Transfer', 'Pending Transfer',                           1, 1, now(), 'admin', now(), 'admin'),
	(231, 'BLOCKED_TRAN', 'Blocked Transfer', 'Blocked Transfer',                       1, 1, now(), 'admin', now(), 'admin');
 