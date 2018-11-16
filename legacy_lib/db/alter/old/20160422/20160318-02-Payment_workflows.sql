INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	 -- Payment Statuses
    (2201, 'NAL', 'Not Allocated', 'Not Allocated',                                     1, 1, now(), 'admin', now(), 'admin'),
    (2202, 'RVA', 'Request Validation', 'Request Validation',                           1, 1, now(), 'admin', now(), 'admin'),
    (2203, 'VAL', 'Validation', 'Validation',                                       	1, 1, now(), 'admin', now(), 'admin'),
	(2204, 'PAI', 'Paid', 'Paid',                                      				 	1, 1, now(), 'admin', now(), 'admin'),
	(2205, 'CAN', 'Cancelled', 'Cancelled',                                       		1, 1, now(), 'admin', now(), 'admin'),
	(2206, 'UNP', 'Unpaid', 'Unpaid',                                       			1, 1, now(), 'admin', now(), 'admin'),
	(2207, 'PAL', 'Partially Allocated', 'Partially Allocated',                         1, 1, now(), 'admin', now(), 'admin');