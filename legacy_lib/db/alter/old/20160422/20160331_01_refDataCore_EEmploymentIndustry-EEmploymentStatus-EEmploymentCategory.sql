delete from ts_ref_data where ref_tab_id = 240;
delete from ts_ref_data where ref_tab_id = 241;
delete from ts_ref_data where ref_tab_id = 242;
    
INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EEmploymentIndustry
	(240, 1, '001', 'พนักงานเอกชน (กลุ่มพนักงานบริษัท)', 'พนักงานเอกชน (กลุ่มพนักงานบริษัท)',                               null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 2, '002', 'พนักงานเอกชน (กลุ่มบริษัทตัวแทน)', 'พนักงานเอกชน (กลุ่มบริษัทตัวแทน)',                                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 3, '003', 'กลุ่มด้านการเงิน การธนาคาร ประกันภัย', 'กลุ่มด้านการเงิน การธนาคาร ประกันภัย',                         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 4, '004', 'กลุ่มอาชีพด้านการศึกษา', 'กลุ่มอาชีพด้านการศึกษา',                                                     null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 5, '005', 'กลุ่มอาชีพด้านสาธารณสุข', 'กลุ่มอาชีพด้านสาธารณสุข',                                                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 6, '006', 'กลุ่มอาชีพพนักงานรัฐวิสาหกิจ', 'กลุ่มอาชีพพนักงานรัฐวิสาหกิจ',                                         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 7, '007', 'กลุ่มอาชีพข้าราชการพลเรือน', 'กลุ่มอาชีพข้าราชการพลเรือน',                                             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 8, '008', 'กลุ่มอาชีพข้าราชการท้องถิ่น', 'กลุ่มอาชีพข้าราชการท้องถิ่น',                                           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 9, '009', 'กลุ่มอาชีพข้าราชการทหาร/ ตำรวจ', 'กลุ่มอาชีพข้าราชการทหาร/ ตำรวจ',                                     null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 10, '010', 'กลุ่มอาชีพนักการเมือง และนักการฑูต', 'กลุ่มอาชีพนักการเมือง และนักการฑูต',                                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 11, '011', 'กลุ่มอาชีพผู้ประกอบวิชาชีพที่มีความชำนาญอิสระ เฉพาะด้าน (ที่ไม่ใช่ข้าราชการ หรือพนักงานบริษัท)', 'กลุ่มอาชีพผู้ประกอบวิชาชีพที่มีความชำนาญอิสระ เฉพาะด้าน (ที่ไม่ใช่ข้าราชการ หรือพนักงานบริษัท)',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 12, '012', 'กลุ่มอาชีพอาชีพอิสระ', 'กลุ่มอาชีพอาชีพอิสระ',                                                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 13, '013', 'กลุ่มอาชีพขับรถรับจ้าง', 'กลุ่มอาชีพขับรถรับจ้าง',                                                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 14, '014', 'กลุ่มอาชีพลูกจ้างร้านค้า และลูกจ้างบุคคล', 'กลุ่มอาชีพลูกจ้างร้านค้า และลูกจ้างบุคคล',                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 15, '015', 'กลุ่มอาชีพด้านการเกษตรและประมง', 'กลุ่มอาชีพด้านการเกษตรและประมง',                                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 16, '016', 'กลุ่มอาชีพผู้ประกอบกิจการ', 'กลุ่มอาชีพผู้ประกอบกิจการ',                                              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 17, '017', 'กลุ่มอาชีพธุรกิจจดทะเบียน หจก. บริษัท นิติบุคคล', 'กลุ่มอาชีพธุรกิจจดทะเบียน หจก. บริษัท นิติบุคคล',  null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (240, 18, '018', 'กลุ่มอาชีพไม่ประกอบอาชีพ', 'กลุ่มอาชีพไม่ประกอบอาชีพ',                                                    null, null, 1, 1, now(), 'admin', now(), 'admin'),

	
    -- EEmploymentStatus
    (241, 1, 'EMP', 'Employed', 'Employed',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (241, 2, 'SEL', 'Self-Employed', 'Self-Employed',     null, null, 1, 1, now(), 'admin', now(), 'admin'),

    --EEmploymentCategory
    (242, 1, 'FT', 'Full Time', 'Full Time',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 2, 'PT', 'Part Time', 'Part Time',                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 3, 'OUT', 'Outsource', 'Outsource',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 4, 'TMP', 'Temporary', 'Temporary',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 5, 'FRE', 'Freelancer', 'Freelancer',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (242, 6, 'ENT', 'Business Owner', 'Business Owner',                   null, null, 1, 1, now(), 'admin', now(), 'admin');