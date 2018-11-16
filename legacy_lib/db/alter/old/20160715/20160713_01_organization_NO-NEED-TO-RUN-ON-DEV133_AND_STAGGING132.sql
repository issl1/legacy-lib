delete from tu_org_address where org_add_id < 100;
delete from td_address where add_id < 100;


update tu_organization 
	set
		com_code = 'GL', 
		com_desc = 'บริษัท กรุ๊ปลีส จำกัด(มหาชน)', com_desc_en = 'Group Lease Public Company Limited',
		com_name = 'บริษัท กรุ๊ปลีส จำกัด(มหาชน)', com_name_en = 'Group Lease Public Company Limited',
		com_licence_number = '0107537000327',
		com_start_date = to_date('1986-05-06', 'yyyy-mm-dd')
	where org_id = 1;

update tu_organization 
	set
		com_licence_number = '0105521022758',
		com_start_date = to_date('1978-07-03', 'yyyy-mm-dd')
	where org_id = 2;

--td_address insurance
ALTER TABLE td_address DISABLE TRIGGER all;
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
ALTER TABLE td_address ENABLE TRIGGER all;


ALTER TABLE tu_org_address DISABLE TRIGGER all;
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
ALTER TABLE tu_org_address ENABLE TRIGGER all;