ALTER TABLE tu_org_structure DISABLE TRIGGER all;
delete from tu_org_structure;
INSERT INTO tu_org_structure(
		org_str_id, org_id, org_str_code, org_str_name, org_str_name_en, org_lev_id, org_str_mobile, org_str_tel, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
	(1, 	1, 	'00000',	'ประชาชื่น', 	'Prachachuen',		3,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(2, 	1, 	'00001',	'เทพารักษ์', 	'Thepharak',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	(3, 	1, 	'00002',	'อ้อมใหญ่', 	'Omyai',		    4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	(4, 	1, 	'00003',	'อยุธยา', 	    'Ayutthaya',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(5, 	1, 	'00004',	'นครปฐม', 	'Nakhon Pathom',	4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	(6, 	1, 	'00005',	'ฉะเชิงเทรา', 	'Chachoengsao',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	(7, 	1, 	'00006',	'อมตะ', 	    'Amata',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(8, 	1, 	'00007',	'ระยอง', 	    'Rayong',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(9, 	1, 	'00008',	'โคราช', 	    'Korat',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(10, 	1, 	'00009',	'ขอนแก่น',   	'Khon Kaen',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	(11, 	1, 	'00010',	'บ่อวิน', 	    'Bowin',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(12, 	1, 	'00011',	'พระนั่งเกล้า', 	'Pranangklao',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	(13, 	1, 	'00012',	'ราชบุรี', 	    'Ratchaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin');
select setval('tu_org_structure_org_str_id_seq', 50);
ALTER TABLE tu_org_structure ENABLE TRIGGER all;