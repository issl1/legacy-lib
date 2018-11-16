ALTER TABLE tu_incident_location DISABLE TRIGGER all;
delete from tu_incident_location;
INSERT INTO tu_incident_location(inc_loc_id,inc_loc_code,inc_loc_desc,inc_loc_desc_en, pro_id,sort_index,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	(1, '001', 'จอดข้างทางสาธารณะ', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(2, '002', 'จอดลานจอดรถ', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(3, '003', 'จอดหน้าบ้านตนเอง', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(4, '004', 'จอดหน้าบ้านคนอื่น', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(5, '005', 'จอดในบ้านตนเอง', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(6, '006', 'จอดในบ้านคนอื่น', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(7, '007', 'ยืมรถแล้วเชิดหายไป', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(8, '008', 'อุบัติเหตุรถหาย', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(9, '009', 'ถูกชิงทรัพย์', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin'),	
	(10, '010', 'ลูกจ้างเชิดรถหนีไป', 	null,                         null, 1, 1, now(), 'admin', now(), 'admin');
update tu_incident_location set inc_loc_desc_en = inc_loc_desc;
select setval('tu_incident_location_inc_loc_id_seq', 50);
ALTER TABLE tu_incident_location ENABLE TRIGGER all;