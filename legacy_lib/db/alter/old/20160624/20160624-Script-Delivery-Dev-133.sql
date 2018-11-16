INSERT INTO tu_organization(org_id, com_code, com_desc, com_desc_en, com_name, com_name_en, 
								typ_org_id, cou_id, 	wkf_sta_id, version, 		sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd,com_tel)
VALUES 
	  --Insurance
	  (86,		'INS', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 'กรุ๊ปลีส', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '580-7555(Auto 50)'),
	  (721,		'INS', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 'กรุงเทพประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '237-8237-9'),
	  (722,		'INS', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 'ประกันคุ้มภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '2547850-9/6860-6'),
	  (848,		'INS', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 'ไทยไพบูลย์ประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '2469635-54'),
	  (2531,	'INS', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 'กลางคุ้มครองผู้ประสบภัยจากรถ', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '643-0280'),
	  (2602,	'INS', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 'โรยัลแอนด์ซันอัลลายแอนซ์ประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '207-0266-85'),
	  (3038,	'INS', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 'ซี จี ยู ประกันภัย(ไทย)', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '0-2318-8318'),
	  (4060,	'INS', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '0-2635-5000'),
	  (5970,	'INS', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', ''),
	  (5999,	'INS', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 'วิริยะประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', ''),
	  (6178,	'INS', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', ''),
	  (7396,	'INS', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 'ฟอลคอนประกันภัย', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin', '0-2676-9888');
select setval('tu_organization_org_id_seq', 8000);

ALTER TABLE tu_org_structure DISABLE TRIGGER all;
delete from tu_org_structure;
INSERT INTO tu_org_structure(
		org_str_id, org_id, org_str_code, org_str_name, org_str_name_en, org_lev_id, org_str_mobile, org_str_tel, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES
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
INSERT INTO tu_org_bank_account(org_ban_id, org_id, ban_external_id, ban_acc_holder_id, ban_acc_holder, ban_acc_number, 
    ban_acc_comment, ban_id, typ_ban_acc_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 569, 558, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_bank_account_org_ban_id_seq', 20);
ALTER TABLE tu_org_bank_account ENABLE TRIGGER all;


--td_address insurance
ALTER TABLE td_address DISABLE TRIGGER all;
delete from td_address;
INSERT INTO td_address(add_id, add_va_house_no, add_line1, add_line2, add_va_street, com_id, dis_id, pro_id, add_postal_code, dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, typ_add_id, cou_id, add_building_name, old_address)
VALUES 
	(1, '', '63', 'ซอย1', 'ถนนเทศบาลนิมิตรใต้', 115, 30, 1, '10900', now(), 'admin', now(), 'admin', 1, 1, 101, '63', '63 ซอย1 ถนนเทศบาลนิมิตรใต้ ลาดยาว จตุจักร กรุงเทพมหานคร 10900'),
	(2, '', '302 อาคารกรุงเทพประกันภัย', '', 'สีลม', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '302 อาคารกรุงเทพประกันภัย', '302 อาคารกรุงเทพประกันภัย  สีลม สีลม บางรัก กรุงเทพมหานคร 10500'),
	(3, '', '26/5-6 อาคารอรกานต์', '', 'ถนนชิดลม', 40, 7, 1, '10330', now(), 'admin', now(), 'admin', 1, 1, 101, '26/5-6 อาคารอรกานต์', '26/5-6 อาคารอรกานต์  ถนนชิดลม ลุมพินี ปทุมวัน กรุงเทพมหานคร 10330'),
	(4, '', '123 อาคารไทยประกันชีวิต', '', 'ถนนรัชดาภิเษก', 105, 26, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '123 อาคารไทยประกันชีวิต', '123 อาคารไทยประกันชีวิต  ถนนรัชดาภิเษก ดินแดง ดินแดง กรุงเทพมหานคร 10320'),
	(5, '', '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3', '', 'พระราม9', 70, 17, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3', '65/42เอ อาคารชำนาญเพ็ญชาติ บิสเนสเซ็นเตอร์ ชั้น3  พระราม9 ห้วยขวาง. ห้วยขวาง กรุงเทพมหานคร 10320'),
	(6, '', '1550 อาคารธนภูมิ ชั้น24', '', 'ถนนเพชรบุรีตัดใหม่', 141, 37, 1, '10400', now(), 'admin', now(), 'admin', 1, 1, 101, '1550 อาคารธนภูมิ ชั้น24', '1550 อาคารธนภูมิ ชั้น24  ถนนเพชรบุรีตัดใหม่ มักกะสัน ราชเทวี กรุงเทพมหานคร 10400'),
	(7, '', '1908 อาคารซี จี ยู', '', 'ถนนเพชรบุรีตัดใหม่', 70, 17, 1, '10320', now(), 'admin', now(), 'admin', 1, 1, 101, '1908 อาคารซี จี ยู', '1908 อาคารซี จี ยู  ถนนเพชรบุรีตัดใหม่ ห้วยขวาง. ห้วยขวาง กรุงเทพมหานคร 10320'),
	(8, '', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '', 'ถนนสีลม', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35  ถนนสีลม สีลม บางรัก กรุงเทพมหานคร 10500'),
	(9, '', '86,88,90', '', 'จรัญสนิทวงศ์', 101, 25, 1, '10700', now(), 'admin', now(), 'admin', 1, 1, 101, '86,88,90', '86,88,90  จรัญสนิทวงศ์ บางพลัด บางพลัด กรุงเทพมหานคร 10700'),
	(10, '', '86,88,90', '', 'จรัญสนิทวงศ์', 101, 25, 1, '10700', now(), 'admin', now(), 'admin', 1, 1, 101, '86,88,90', '86,88,90  จรัญสนิทวงศ์ บางพลัด บางพลัด กรุงเทพมหานคร 10700'),
	(11, '', '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25', '', 'ถนนพระราม9', 70, 17, 1, '10310', now(), 'admin', now(), 'admin', 1, 1, 101, '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25', '33/4 อาคารเอ เดอะไนน์ทาวเวอร์ ชั้น24-25  ถนนพระราม9 ห้วยขวาง ห้วยขวาง กรุงเทพมหานคร 10310'),
	(12, '', '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1', '', 'ถนนสาทรเหนือ', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1', '90/26-27 ชั้น11,90/50-51 ชั้น18 อาคารสาธรธานี 1  ถนนสาทรเหนือ สีลม บางรัก กรุงเทพมหานคร 10500');
select setval('td_address_add_id_seq', 100);
ALTER TABLE td_address ENABLE TRIGGER all;


ALTER TABLE tu_org_address DISABLE TRIGGER all;
delete from tu_org_address;
INSERT INTO tu_org_address(org_add_id,add_id,org_id,org_str_id,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	(1, 1, 86, null, 1, now(), 'admin', now(), 'admin'),
	(2, 2, 721, null, 1, now(), 'admin', now(), 'admin'),
	(3, 3, 722, null, 1, now(), 'admin', now(), 'admin'),
	(4, 4, 848, null, 1, now(), 'admin', now(), 'admin'),
	(5, 5, 2531, null, 1, now(), 'admin', now(), 'admin'),
	(6, 6, 2602, null, 1, now(), 'admin', now(), 'admin'),
	(7, 7, 3038, null, 1, now(), 'admin', now(), 'admin'),
	(8, 8, 4060, null, 1, now(), 'admin', now(), 'admin'),
	(9, 9, 5970, null, 1, now(), 'admin', now(), 'admin'),
	(10, 10, 5999, null, 1, now(), 'admin', now(), 'admin'),
	(11, 11, 6178, null, 1, now(), 'admin', now(), 'admin'),
	(12, 12, 7396, null, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_address_org_add_id_seq', 100);
ALTER TABLE tu_org_address ENABLE TRIGGER all;


delete from tu_organization where org_id = 3;

--======================================================================================
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
   (418, 4, 'MRR', 'Minimum Return Rate', 'Minimum Return Rate',                   null, null, 1, 1, now(), 'admin', now(), 'admin');
   
   
--======================================================================================

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(529, 13, 'PRESSFEE', 'Pressing Fee', 'Pressing Fee',            null, null, 1, 1, now(), 'admin', now(), 'admin');

update ts_ref_data set ref_dat_code = 'OPERFEE' where ref_dat_code = 'OPERATION' and ref_tab_id = 529 and ref_dat_ide = 11;
update ts_ref_data set ref_dat_code = 'TRANSFEE' where ref_dat_code = 'TRANSFERFEE' and ref_tab_id = 529 and ref_dat_ide = 12;


--=====================================================================================
INSERT INTO tu_service(fin_srv_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, fin_srv_bl_allow_change_price, fin_srv_bl_allow_include_in_installment, 
            cal_met_id, fin_srv_code, fin_srv_bl_contract_duration, fin_srv_desc, fin_srv_desc_en, fin_srv_bl_paid_begin_contract, fin_srv_bl_paid_one_shot,  
            ser_typ_id, fin_srv_bl_split_with_installment, fin_srv_rt_percentage, fin_srv_am_te_price, fin_srv_am_ti_price, tre_typ_id, fin_srv_am_vat_price)
VALUES 
    (1,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'FEE',      false, 'Fee', 'Fee', false, false, 1, false, 0, 0, 0, 3, 0),
    (2,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COMM',     false, 'Commission', 'Commission', false, false, 2, false, 0, 0, 0, 3, 0),
    (3,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INEX',     false, 'Insurance Expenses', 'Insurance Expenses', false, false, 3, false, 0, 0, 0, 3, 0),
    (4,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'DMIS',     false, 'Miscellaneous', 'Miscellaneous', false, false, 4, false, 0, 0, 0, 3, 0),
    (5,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSFEE',   false, 'Insurance Fee', 'Insurance Fee', false, false, 5, false, 0, 60, 60, 3, 0),
    (6,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'SRVFEE',   false, 'Servcing Fee', 'Servicing Fee', false, false, 6, false, 0, 20, 20, 3, 0),
    (7,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSLOS',   false, 'Insurance Lost', 'Insurance Lost', false, false, 7, false, 0, 0, 0, 3, 0),
    (8,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'INSAOM',   false, 'Insurance AOM', 'Insurance AOM', false, false, 8, false, 0, 0, 0, 3, 0),
    (9,  now(), 'admin', 1, now(), 'admin', false, false, 1, 'COLFEE',   false, 'Collection Fee', 'Collection Fee', false, false, 9, false, 0, 0, 0, 3, 0),
    (10, now(), 'admin', 1, now(), 'admin', false, false, 1, 'REPOSFEE', false, 'Repossession Fee', 'Repossession Fee', false, false, 10, false, 0, 0, 0, 3, 0),
    (11, now(), 'admin', 1, now(), 'admin', false, false, 1, 'OPERFEE',  false, 'Operation Fee', 'Operation Fee', false, false, 11, false, 0, 0, 0, 3, 0),
    (12, now(), 'admin', 1, now(), 'admin', false, false, 1, 'TRANSFEE', false, 'Transfer Fee', 'Transfer Fee', false, false, 12, false, 0, 0, 0, 3, 0),
    (13, now(), 'admin', 1, now(), 'admin', false, false, 1, 'PRESSFEE', false, 'Pressing Fee', 'Pressing Fee', false, false, 13, false, 0, 0, 0, 3, 0);


	--=======================================================================================
ALTER TABLE td_payment_file_item DROP COLUMN pay_fil_ite_bank_name, DROP COLUMN pay_fil_ite_bank_branch;


--=======================================================================================
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
 
    (236, 'NBC010', 'ปกติ', 'Normal', 																								1, 1, now(), 'admin', now(), 'admin'),
    (237, 'NBC011', 'ปิดบัญชี', 'Closed account', 																					1, 1, now(), 'admin', now(), 'admin'),
    (238, 'NBC012', 'พักชำระหนี้ ตามนโยบายของรัฐบาล', 'Debt Moratorium as per government policy', 									1, 1, now(), 'admin', now(), 'admin'),
    (239, 'NBC020', 'หนี้ค้างชำระเกิน 90 วัน', 'Past due over 90 days', 															1, 1, now(), 'admin', now(), 'admin'),
    (240, 'NBC031', 'อยู่หว่างชำระหนี้ตามคำพิพากษาตามยินยอม', 'Under the processs of payment as agreed upon in the courts of law.', 1, 1, now(), 'admin', now(), 'admin'),
    (241, 'NBC032', 'ศาสพิพากษาฟ้องเนื่องจากขาดอายุความ', 'Case dismissed due to laspse of period of prescription', 				1, 1, now(), 'admin', now(), 'admin'),
    (242, 'NBC033', 'ปิดบัญชีเนื่องจากตัดหนี้สูญ', 'Write off account', 															1, 1, now(), 'admin', now(), 'admin'),
    (243, 'NBC042', 'โอนหรือขายหนี้', 'Debt transferred or sold.', 																	1, 1, now(), 'admin', now(), 'admin'),
    (244, 'NBC043', 'ปิดบัญชีของการโอนขายหนี้', 'ปิดบัญชีของการโอนขายหนี้', 														1, 1, now(), 'admin', now(), 'admin');


--========================================================================================
INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
 (639, 'com.nokor.efinance.core.collection.model.EPromiseStatus',           'Promise Status',              'Promise Status',                                     'promisestatuses',      1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin'),
 (640, 'com.nokor.efinance.core.collection.model.EPromiseType',             'Promise Type',                'Promise Type',                                       'promisetypes',         1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- EPromiseStatus
    (639, 1,  'PENDING', 'Pending',        'Pending',       null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (639, 2,  'BROKEN',  'Broken',         'Broken',        null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (639, 3,  'KEPT',    'Kept',           'Kept',          null, null, 1, 1, now(), 'admin', now(), 'admin'),

     -- EPromiseType
    (640, 1,  '001',     'Promise to pay',        'Promise to pay',         null, null, 1, 1, now(), 'admin', now(), 'admin');

--========================================================================================	
   