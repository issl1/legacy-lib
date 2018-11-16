INSERT INTO tu_call_center_result(cal_ctr_res_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, cal_ctr_res_code, cal_ctr_res_desc, cal_ctr_res_desc_en)
VALUES 
	(1, now(), 'admin', 1, now(), 'admin', null, 'OK', 'OK', 'OK'),
	(2, now(), 'admin', 1, now(), 'admin', null, 'KO', 'KO', 'KO'),
	(3, now(), 'admin', 1, now(), 'admin', null, 'OTHER', 'Other', 'Other');
select setval('tu_call_center_result_cal_ctr_res_id_seq', 20);