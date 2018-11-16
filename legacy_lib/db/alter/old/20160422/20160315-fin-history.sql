ALTER TABLE ts_fin_history_type DISABLE TRIGGER all;
delete from ts_fin_history_type;	
insert into ts_fin_history_type (fin_his_typ_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	values	
			-- Default - DO NOT DELETE
			(1,  'FIN_HIS_CNT', 	'Contacts', 	'Contacts',			1, 1, now(), 'admin', now(), 'admin'),
			(2,  'FIN_HIS_CMT', 	'Comments', 	'Comments',			1, 1, now(), 'admin', now(), 'admin'),
			(3,  'FIN_HIS_REM', 	'Reminders', 	'Reminders',		1, 1, now(), 'admin', now(), 'admin'),
			(4,  'FIN_HIS_SMS', 	'SMS', 			'SMS',				1, 1, now(), 'admin', now(), 'admin'),
			(5,  'FIN_HIS_PAY', 	'Payments', 	'Payments',			1, 1, now(), 'admin', now(), 'admin'),
			(6,  'FIN_HIS_LCK', 	'Lock Splits', 	'Lock Splits',		1, 1, now(), 'admin', now(), 'admin'),
			(7,  'FIN_HIS_REQ', 	'Requests', 	'Requests',		    1, 1, now(), 'admin', now(), 'admin'),
			(8,  'FIN_HIS_UPD', 	'Updates', 	    'Updates',		    1, 1, now(), 'admin', now(), 'admin'),
			(9,  'FIN_HIS_SYS', 	'Systems', 	    'Systems',		    1, 1, now(), 'admin', now(), 'admin');
select setval('ts_fin_history_type_fin_his_typ_id_seq', 100);	
ALTER TABLE ts_fin_history_type ENABLE TRIGGER all;
