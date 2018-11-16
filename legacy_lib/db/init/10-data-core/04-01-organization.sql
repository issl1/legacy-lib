update tu_organization 
	set
		com_code = 'GL', 
		com_desc = 'บริษัท กรุ๊ปลีส จำกัด(มหาชน)', com_desc_en = 'Group Lease Public Company Limited',
		com_name = 'บริษัท กรุ๊ปลีส จำกัด(มหาชน)', com_name_en = 'Group Lease Public Company Limited',
		com_licence_number = '0107537000327',
		com_start_date = to_date('1986-05-06', 'yyyy-mm-dd')
	where org_id = 1;
       
INSERT INTO tu_organization(org_id, com_code, com_desc, com_desc_en, com_name, com_name_en, 
							typ_org_id, cou_id, wkf_sta_id, version, com_licence_number, com_start_date, 
							sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd, com_tel, sub_typ_org_id)
VALUES 
      --Company
      (2, 'TNB', 'ธนบรรณ', 'Thanaban Bike', 'ธนบรรณ', 'Thanaban Bike', 1, 101, 1, 1, '0105521022758', to_date('1978-07-03', 'yyyy-mm-dd'), 1, now(), 'admin', now(), 'admin',null, null),
	  --warehouse Location
   (3, '0007', 'นครราชสีมา', 'Nakhon Ratchasima', 'นครราชสีมา', 'Nakhon Ratchasima', 4, null, 1, 1, null, null, 1, now(), 'admin', now(), 'admin',null, 5),
   (4, '0014', 'พระนั่งเกล้า', 'Pranangklao', 'พระนั่งเกล้า', 'Pranangklao', 4, null, 1, 1, null, null, 1, now(), 'admin', now(), 'admin',null, 5);
	  --Insurance
	  (86,		'INS', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '580-7555(Auto 50)', null),
	  (721,		'INS', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '237-8237-9', null),
	  (722,		'INS', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '2547850-9/6860-6', null),
	  (848,		'INS', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '2469635-54', null),
	  (2531,	'INS', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '643-0280', null),
	  (2602,	'INS', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '207-0266-85', null),
	  (3038,	'INS', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '0-2318-8318', null),
	  (4060,	'INS', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '0-2635-5000', null),
	  (5970,	'INS', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '', null),
	  (5999,	'INS', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '', null),
	  (6178,	'INS', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '', null),
	  (7396,	'INS', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 2, 101, 1, 1, null, null, 1, now(), 'admin', now(), 'admin', '0-2676-9888', null);
select setval('tu_organization_org_id_seq', 8000);

ALTER TABLE tu_org_structure DISABLE TRIGGER all;
delete from tu_org_structure;
INSERT INTO tu_org_structure(
		org_str_id, org_id, org_str_code, org_str_name, org_str_name_en, org_lev_id, org_str_mobile, org_str_tel, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
	--(1, 	1, 	'Prachacheun',	'Prachacheun', 	'Prachacheun',		3,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(2, 	1, 	'Ayuthaya',		'Ayuthaya',	  	'Ayuthaya',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(3, 	1, 	'Amata',		'Amata',	  	'Amata',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(4, 	1, 	'Bowin',		'Bowin',	  	'Bowin',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(5, 	1, 	'Sri-racha',	'Sri-racha',	'Sri-racha',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(6, 	1, 	'Rayong',		'Rayong',	  	'Rayong',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(7, 	1, 	'Chantaburi',	'Chantaburi',  	'Chantaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(8, 	1, 	'Ratchaburi',	'Ratchaburi',  	'Ratchaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(9, 	1, 	'Korach',		'Korach',	  	'Korach',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(10, 	1, 	'Pranangklao',	'Pranangklao', 	'Pranangklao',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(11, 	1, 	'Pranburi',		'Pranburi',	  	'Pranburi',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(12, 	1, 	'Supanburi',	'Supanburi',	'Supanburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(13, 	1, 	'Bureerum',		'Bureerum',	  	'Bureerum',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(14, 	1, 	'Bangpu',		'Bangpu',	  	'Bangpu',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(15, 	1, 	'Nongkam',		'Nongkam',	  	'Nongkam',			4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(16, 	1, 	'Sapanmai',		'Sapanmai',	  	'Sapanmai',			4,	 null, null,		1,	now(), 'admin', now(), 'admin');

	--(1, 	1, 	'00000',	'ประชาชื่น', 	'Prachachuen',		3,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(2, 	1, 	'00001',	'เทพารักษ์', 	'Thepharak',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	--(3, 	1, 	'00002',	'อ้อมใหญ่', 	'Omyai',		    4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	--(4, 	1, 	'00003',	'อยุธยา', 	    'Ayutthaya',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(5, 	1, 	'00004',	'นครปฐม', 	'Nakhon Pathom',	4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	--(6, 	1, 	'00005',	'ฉะเชิงเทรา', 	'Chachoengsao',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	--(7, 	1, 	'00006',	'อมตะ', 	    'Amata',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(8, 	1, 	'00007',	'ระยอง', 	    'Rayong',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(9, 	1, 	'00008',	'โคราช', 	    'Korat',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(10, 	1, 	'00009',	'ขอนแก่น',   	'Khon Kaen',		4,	 null, null,		2,	now(), 'admin', now(), 'admin'),
	--(11, 	1, 	'00010',	'บ่อวิน', 	    'Bowin',		    4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(12, 	1, 	'00011',	'พระนั่งเกล้า', 	'Pranangklao',		4,	 null, null,		1,	now(), 'admin', now(), 'admin'),
	--(13, 	1, 	'00012',	'ราชบุรี', 	    'Ratchaburi',		4,	 null, null,		1,	now(), 'admin', now(), 'admin');
	(1,		1,	'0001',	'ประชาชื่น',	'Prachachuen',				3,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(2,		1,	'0002',	'อมตะ(ชลบุรี)',	'Amata (Chon Buri)',	4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(3,		1,	'0003',	'บ่อวิน',	'Bowin',					4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(4,		1,	'0004',	'ศรีราชา',	'Si Racha',					4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(5,		1,	'0005',	'ระยอง',	'Rayong',					4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(6,		1,	'0006',	'จันทบุรี',	'Chanthaburi',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(7,		1,	'0007',	'นครราชสีมา',	'Nakhon Ratchasima',	4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(8,		1,	'0008',	'บุรีรัมย์',	'Buri Ram',					4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(9,		1,	'0009',	'ชัยภูมิ',	'Chaiyaphum',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(10,	1,	'0010',	'อยุธยา',	'Ayutthaya',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(11,	1,	'0011',	'สุพรรณบุรี',	'Suphan Buri',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(12,	1,	'0012',	'ราชบุรี',	'Ratchaburi',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(13,	1,	'0013',	'ปราณบุรี',	'Pran Buri',				4,	null, null,	1,	now(), 'admin', now(), 'admin'),
	(14,	1,	'0014',	'พระนั่งเกล้า',	'Pranangklao',				4,	null, null,	1,	now(), 'admin', now(), 'admin');
select setval('tu_org_structure_org_str_id_seq', 50);
ALTER TABLE tu_org_structure ENABLE TRIGGER all;


ALTER TABLE tu_org_bank_account DISABLE TRIGGER all;
delete from tu_org_bank_account;
INSERT INTO tu_org_bank_account(org_ban_id, org_id, ban_acc_id, ban_acc_holder, ban_acc_number, 
    ban_acc_comment, ban_id, typ_ban_acc_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_bank_account_org_ban_id_seq', 100);
ALTER TABLE tu_org_bank_account ENABLE TRIGGER all;


ALTER TABLE tu_org_account_holder DISABLE TRIGGER all;
delete from tu_org_account_holder;
INSERT INTO tu_org_account_holder(org_acc_hol_id, org_id, acc_hol_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 558, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 558, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 558, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 558, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 558, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 558, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 558, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 558, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 558, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 558, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 558, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 558, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_account_holder_org_acc_hol_id_seq', 100);
ALTER TABLE tu_org_account_holder ENABLE TRIGGER all;



--td_address insurance
ALTER TABLE td_address DISABLE TRIGGER all;
delete from td_address;
INSERT INTO td_address(add_id, add_va_house_no, add_line1, add_line2, add_va_street, com_id, dis_id, pro_id, add_postal_code, dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, typ_add_id, cou_id, add_building_name, old_address)
VALUES 
	--address GL company
	(1, '63', '', '1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 1, 101, '', ''),
	(2, '63', '', '1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 8, 101, '', ''),

	--address TNB company
	(3, '63', '', '1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 1, 101, '', ''),
	(4, '63', '', '1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 8, 101, '', ''),

	--address insurance company
	(5, '', '63', 'ซอย1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 1, 101, '63', '63 ซอย1 ถนนเทศบาลนิมิตรใต้ ลาดยาว จตุจักร กรุงเทพมหานคร 10900'),
	(6, '', '302 อาคารกรุงเทพประกันภัย', '', 'สีลม', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '302 อาคารกรุงเทพประกันภัย', '302 อาคารกรุงเทพประกันภัย  สีลม สีลม บางรัก กรุงเทพมหานคร 10500'),
	(7, '', '26/5-6 อาคารอรกานต์', '', 'ถนนชิดลม', 40, 7, 1, '10330', now(), 'admin', now(), 'admin', 1, 1, 101, '26/5-6 อาคารอรกานต์', '26/5-6 อาคารอรกานต์  ถนนชิดลม ลุมพินี ปทุมวัน กรุงเทพมหานคร 10330'),
	(8, '', '123 อาคารไทยประกันชีวิต', '', 'ถนนรัชดาภิเษก', 105, 26, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '123 อาคารไทยประกันชีวิต', '123 อาคารไทยประกันชีวิต  ถนนรัชดาภิเษก ดินแดง ดินแดง กรุงเทพมหานคร 10320'),
	(9, '', '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3', '', 'พระราม9', 70, 17, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3', '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3  พระราม9 ห้วยขวาง. ห้วยขวาง กรุงเทพมหานคร 10320'),
	(10, '', '1550 อาคารธนภูมิ ชั้น24', '', 'ถนนเพชรบุรีตัดใหม่', 141, 37, 1, '10400', now(), 'admin', now(), 'admin', 1, 1, 101, '1550 อาคารธนภูมิ ชั้น24', '1550 อาคารธนภูมิ ชั้น24  ถนนเพชรบุรีตัดใหม่ มักกะสัน ราชเทวี กรุงเทพมหานคร 10400'),
	(11, '', '1908 อาคารซี จี ยู', '', 'ถนนเพชรบุรีตัดใหม่', 70, 17, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '1908 อาคารซี จี ยู', '1908 อาคารซี จี ยู  ถนนเพชรบุรีตัดใหม่ ห้วยขวาง. ห้วยขวาง กรุงเทพมหานคร 10320'),
	(12, '', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '', 'ถนนสีลม', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35  ถนนสีลม สีลม บางรัก กรุงเทพมหานคร 10500'),
	(13, '', '86,88,90', '', 'จรัญสนิทวงศ์', 101, 25, 1, '10700', now(), 'admin', now(), 'admin', 1, 1, 101, '86,88,90', '86,88,90  จรัญสนิทวงศ์ บางพลัด บางพลัด กรุงเทพมหานคร 10700'),
	(14, '', '86,88,90', '', 'จรัญสนิทวงศ์', 101, 25, 1, '10700', now(), 'admin', now(), 'admin', 1, 1, 101, '86,88,90', '86,88,90  จรัญสนิทวงศ์ บางพลัด บางพลัด กรุงเทพมหานคร 10700'),
	(15, '', '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25', '', 'ถนนพระราม9', 70, 17, 1, '10310', now(), 'admin', now(), 'admin', 1, 1, 101, '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25', '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25  ถนนพระราม9 ห้วยขวาง ห้วยขวาง กรุงเทพมหานคร 10310'),
	(16, '', '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1', '', 'ถนนสาทรเหนือ', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1', '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1  ถนนสาทรเหนือ สีลม บางรัก กรุงเทพมหานคร 10500');
select setval('td_address_add_id_seq', 100);
ALTER TABLE td_address ENABLE TRIGGER all;


ALTER TABLE tu_org_address DISABLE TRIGGER all;
delete from tu_org_address;
INSERT INTO tu_org_address(org_add_id,add_id,org_id,org_str_id,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	--address GL company
	(1, 1, 1, null, 1, now(), 'admin', now(), 'admin'),
	(2, 2, 1, null, 1, now(), 'admin', now(), 'admin'),
	
	--address TNB company
	(3, 3, 2, null, 1, now(), 'admin', now(), 'admin'),
	(4, 4, 2, null, 1, now(), 'admin', now(), 'admin'),

	--address insurance company
	(5, 5, 86, null, 1, now(), 'admin', now(), 'admin'),
	(6, 6, 721, null, 1, now(), 'admin', now(), 'admin'),
	(7, 7, 722, null, 1, now(), 'admin', now(), 'admin'),
	(8, 8, 848, null, 1, now(), 'admin', now(), 'admin'),
	(9, 9, 2531, null, 1, now(), 'admin', now(), 'admin'),
	(10, 10, 2602, null, 1, now(), 'admin', now(), 'admin'),
	(11, 11, 3038, null, 1, now(), 'admin', now(), 'admin'),
	(12, 12, 4060, null, 1, now(), 'admin', now(), 'admin'),
	(13, 13, 5970, null, 1, now(), 'admin', now(), 'admin'),
	(14, 14, 5999, null, 1, now(), 'admin', now(), 'admin'),
	(15, 15, 6178, null, 1, now(), 'admin', now(), 'admin'),
	(16, 16, 7396, null, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_address_org_add_id_seq', 100);
ALTER TABLE tu_org_address ENABLE TRIGGER all;