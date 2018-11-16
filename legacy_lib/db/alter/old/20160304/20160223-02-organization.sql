INSERT INTO tu_organization(org_id, com_code, com_desc, com_desc_en, com_name, com_name_en, 
								typ_org_id, cou_id, 	wkf_sta_id, version, 		sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd,com_tel)
VALUES 
    --Insurance Company
    (3, 'INS', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 'ล็อคตั้น วัฒนา อินชัวรันส์ โบรคเกอร์ส(ประเทศไทย)', 2, 101, 1, 1, 1, now(), 'admin', now(), 'admin','0-2635-5000');

--insurance bank
ALTER TABLE tu_org_bank_account DISABLE TRIGGER all;
delete from tu_org_bank_account;
INSERT INTO tu_org_bank_account(org_ban_id, org_id, ban_external_id, ban_acc_holder_id, ban_acc_holder, ban_acc_number, 
    ban_acc_comment, ban_id, typ_ban_acc_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 3, 569, 558, null, null, null, null, 1, 1,	now(), 'admin', now(), 'admin');
select setval('tu_org_bank_account_org_ban_id_seq', 20);
ALTER TABLE tu_org_bank_account ENABLE TRIGGER all;


--td_address insurance
INSERT INTO td_address(add_id, add_va_house_no, add_line1, add_line2, add_va_street, com_id, dis_id, pro_id, add_postal_code, dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, typ_add_id, cou_id, add_building_name, old_address)
VALUES (9900, '', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '', 'ถนนสีลม', 27, 4, 1, '10500', now(), 'admin', now(), 'admin', 1, 1, 101, '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35', '323 อาคารยูไนเต็ดเซ็นเตอร์ ชั้น4,21,30และ35  ถนนสีลม สีลม บางรัก กรุงเทพมหานคร 10500');


ALTER TABLE tu_org_address DISABLE TRIGGER all;
delete from tu_org_address;
INSERT INTO tu_org_address(org_add_id,add_id,org_id,org_str_id,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES(1, 9900, 3, null, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_address_org_add_id_seq', 20);
ALTER TABLE tu_org_address ENABLE TRIGGER all;
