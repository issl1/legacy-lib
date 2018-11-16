ALTER TABLE tu_vat DISABLE TRIGGER all;
delete from tu_vat;
INSERT INTO tu_vat(vat_id, vat_desc, vat_desc_en, vat_dt_start, vat_dt_end, vat_rt_value, 
	sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	--Vat
	(1, '7%', '7%', 	'1992-01-01 00:00:00', 	'1997-08-15 23:59:39', 	0.07, 			1, 2, now(), 'admin', now(), 'admin'),
	(2, '10%', '10%', 	'1997-08-16 00:00:00', 	'1999-03-31 23:59:39', 	0.10, 			1, 2, now(), 'admin', now(), 'admin'),
	(3, '7%', '7%', 	'1999-04-01 00:00:00', 	null, 					0.07, 			1, 1, now(), 'admin', now(), 'admin');


select setval('tu_vat_vat_id_seq', 10);
ALTER TABLE tu_vat ENABLE TRIGGER all;