INSERT INTO tu_org_structure(
		dtype, org_str_id, org_id, org_str_code, org_str_name, org_str_name_en, org_lev_id, org_str_mobile, org_str_tel, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
	('OrgStructure',	1, 		1, 	'Prachacheun',	'Prachacheun', 	'Prachacheun',		3,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	2, 		1, 	'Ayuthaya',		'Ayuthaya',	  	'Ayuthaya',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	3, 		1, 	'Amata',		'Amata',	  	'Amata',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	4, 		1, 	'Bowin',		'Bowin',	  	'Bowin',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	5, 		1, 	'Sri-racha',	'Sri-racha',	'Sri-racha',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	6, 		1, 	'Rayong',		'Rayong',	  	'Rayong',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	7, 		1, 	'Chantaburi',	'Chantaburi',  	'Chantaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	8, 		1, 	'Ratchaburi',	'Ratchaburi',  	'Ratchaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	9, 		1, 	'Korach',		'Korach',	  	'Korach',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	10, 	1, 	'Pranangklao',	'Pranangklao', 	'Pranangklao',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	11, 	1, 	'Pranburi',		'Pranburi',	  	'Pranburi',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	12, 	1, 	'Supanburi',	'Supanburi',	'Supanburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	13, 	1, 	'Bureerum',		'Bureerum',	  	'Bureerum',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	14, 	1, 	'Bangpu',		'Bangpu',	  	'Bangpu',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	15, 	1, 	'Nongkam',		'Nongkam',	  	'Nongkam',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	('OrgStructure',	16, 	1, 	'Sapanmai',		'Sapanmai',	  	'Sapanmai',			4,	 null, null,		1,	now(), 'admin', now(), 'admin');
select setval('tu_org_structure_org_str_id_seq', 50);	