update ts_ref_table set 
   ref_tab_desc = 'Occupation Group', 
   ref_tab_desc_en = 'Occupation Group', 
   ref_tab_shortname = 'occupationgroups', 
   ref_tab_field_name1 = 'companysector',
   ref_tab_field1_cus_typ_id = '8' 
where ref_tab_id = 240;

update ts_ref_table set 
   ref_tab_desc = 'Company Sector', 
   ref_tab_desc_en = 'Company Sector', 
   ref_tab_shortname = 'companysector' 
where ref_tab_id = 243;


delete from ts_ref_data where ref_tab_id in (240, 241, 243);

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EEmploymentIndustry
	(240, 1, '001', 'พนักงานบริษัท/ห้าง (สำนักงาน)', 'พนักงานบริษัท/ห้าง (สำนักงาน)',         1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 2, '002', 'พนักงานในขบวนการผลิต', 'พนักงานในขบวนการผลิต',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 3, '003', 'พนักงานเร่งรัดหนี้/ติดตามหนี้', 'พนักงานเร่งรัดหนี้/ติดตามหนี้',            1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 4, '004', 'พนักงานฝ่ายกฎหมาย ทนายความ', 'พนักงานฝ่ายกฎหมาย ทนายความ',       1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 5, '005', 'พนักงานรักษาความปลอดภัย', 'พนักงานรักษาความปลอดภัย',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 6, '006', 'พนักงานทำความสะอาด', 'พนักงานทำความสะอาด',                  1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 7, '007', 'พนักงานส่งเอกสาร', 'พนักงานส่งเอกสาร',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 8, '008', 'พนักงานขับรถผู้บริหาร', 'พนักงานขับรถผู้บริหาร',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),

	(240, 9, '009', 'พนักงานบริษัท/ห้าง (สำนักงาน)', 'พนักงานบริษัท/ห้าง (สำนักงาน)',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 10, '010', 'พนักงานในขบวนการผลิต', 'พนักงานในขบวนการผลิต',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 11, '011', 'พนักงานรักษาความปลอดภัย', 'พนักงานรักษาความปลอดภัย',             2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 12, '012', 'พนักงานทำความสะอาด', 'พนักงานทำความสะอาด',                   2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 13, '013', 'พนักงานส่งเอกสาร', 'พนักงานส่งเอกสาร',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 14, '014', 'พนักงานขับรถผู้บริหาร', 'พนักงานขับรถผู้บริหาร',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 15, '015', 'พนักงานธนาคารพาณิชย์', 'พนักงานธนาคารพาณิชย์',                             3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 16, '016', 'พนักงานบริษัทเงินทุน บริษัทหลักทรัพย์', 'พนักงานบริษัทเงินทุน บริษัทหลักทรัพย์',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 17, '017', 'พนักงานบริษัทประกันภัย', 'พนักงานบริษัทประกันภัย',                      3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 18, '018', 'พนักงานบริษัทสินเชื่อ และบริษัทบัตรเครดิต', 'พนักงานบริษัทสินเชื่อ และบริษัทบัตรเครดิต', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 19, '019', 'พนักงาน ลูกจ้างสถาบันการศึกษา(ราชการ)', 'พนักงาน ลูกจ้างสถาบันการศึกษา(ราชการ)',   4, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 20, '020', 'พนักงาน ลูกจ้างสถาบันการศึกษา(เอกชน)', 'พนักงาน ลูกจ้างสถาบันการศึกษา(เอกชน)',    4, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 21, '021', 'ครู-ครูสอนศาสนา-อาจารย์ (ราชการ)', 'ครู-ครูสอนศาสนา-อาจารย์ (ราชการ)',         4, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 22, '022', 'ครู-ครูสอนศาสนา-อาจารย์ (เอกชน)', 'ครู-ครูสอนศาสนา-อาจารย์ (เอกชน)',          4, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 23, '023', 'นักวิชาการ', 'นักวิชาการ',                                                                                      4, null, 1, 1, now(), 'admin', now(), 'admin'),

	(240, 24, '024', 'แพทย์-พยาบาล-เภสัชกร (ราชการ)', 'แพทย์-พยาบาล-เภสัชกร (ราชการ)',            5, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 25, '025', 'แพทย์-พยาบาล-เภสัชกร (เอกชน)', 'แพทย์-พยาบาล-เภสัชกร (เอกชน)',             5, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 26, '026', 'พนักงาน ลูกจ้างสถาพยาบาล(ราชการ)', 'พนักงาน ลูกจ้างสถาพยาบาล(ราชการ)',        5, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 27, '027', 'พนักงาน ลูกจ้างสถาพยาบาล(เอกชน)', 'พนักงาน ลูกจ้างสถาพยาบาล(เอกชน)',         5, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 28, '028', 'พนักงานรัฐวิสาหกิจ', 'พนักงานรัฐวิสาหกิจ',             6, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 29, '029', 'ลูกจ้างประจำ รัฐวิสาหกิจ', 'ลูกจ้างประจำ รัฐวิสาหกิจ',        6, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 30, '030', 'ลูกจ้างชั่วคราว รัฐวิสาหกิจ', 'ลูกจ้างชั่วคราว รัฐวิสาหกิจ',      6, null, 1, 1, now(), 'admin', now(), 'admin'),

	(240, 31, '031', 'ข้าราชการพลเรือน (ประจำการ)', 'ข้าราชการพลเรือน (ประจำการ)',      7, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 32, '032', 'ข้าราชการบำนาญ', 'ข้าราชการบำนาญ',                     7, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 33, '033', 'ลูกจ้างประจำ หน่วยงานราชการ ', 'ลูกจ้างประจำ หน่วยงานราชการ ',      7, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 34, '034', 'ลูกจ้างชั่วคราว หน่วยงานราชการ ', 'ลูกจ้างชั่วคราว หน่วยงานราชการ ',    7, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 35, '035', 'เจ้าหน้าที่ ลูกจ้างปกครองส่วนท้องถิ่น (อบต. สภาเขต)', 'เจ้าหน้าที่ ลูกจ้างปกครองส่วนท้องถิ่น (อบต. สภาเขต)',         8, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 36, '036', 'สมาชิกสภา อบต.  สภาเขต', 'สมาชิกสภา อบต.  สภาเขต',            8, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 37, '037', 'สมาชิกสภาจังหวัด/สภากรุงเทพมหานคร/พัทยา', 'สมาชิกสภาจังหวัด/สภากรุงเทพมหานคร/พัทยา',         8, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 38, '038', 'เจ้าหน้าที่ ลูกจ้างปกครองส่วนระดับจังหวัด (อบจ. กทม.)', 'เจ้าหน้าที่ ลูกจ้างปกครองส่วนระดับจังหวัด (อบจ. กทม.)',         8, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 39, '039', 'กำนัน ผู้ใหญ่บ้าน', 'กำนัน ผู้ใหญ่บ้าน',                                            8, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 40, '040', 'ผู้ช่วยผู้ใหญ่บ้าน สารวัตรกำนัน', 'ผู้ช่วยผู้ใหญ่บ้าน สารวัตรกำนัน',        8, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 41, '041', 'ทหารประทวน', 'ทหารประทวน',               9, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 42, '042', 'ทหารสัญญาบัตร', 'ทหารสัญญาบัตร',            9, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 43, '043', 'ตำรวจประทวน', 'ตำรวจประทวน',              9, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 44, '044', 'ตำรวจสัญญาบัตร', 'ตำรวจสัญญาบัตร',           9, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 45, '045', 'สมาชิกวุฒิสภา', 'สมาชิกวุฒิสภา',              10, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 46, '046', 'สมาชิกสภาผู้แทนราษฎร', 'สมาชิกสภาผู้แทนราษฎร',   10, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 47, '047', 'ทูต/เจ้าหน้าที่สถานฑูต', 'ทูต/เจ้าหน้าที่สถานฑูต',      10, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 48, '048', 'ผู้ดำรงตำแหน่งทางการเมือง(ข้าราชการการเมือง)', 'ผู้ดำรงตำแหน่งทางการเมือง(ข้าราชการการเมือง)',         10, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 49, '049', 'ผู้ประกอบการเกษตรกร (ชาวนา ชาวสวน ชาวไร่)', 'ผู้ประกอบการเกษตรกร (ชาวนา ชาวสวน ชาวไร่)', 11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 50, '050', 'ผู้ประกอบการชาวประมงออกเรือ', 'ผู้ประกอบการชาวประมงออกเรือ',                    11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 51, '051', 'ผู้ประกอบการเกษตรกร (เลี้ยงสัตว์)', 'ผู้ประกอบการเกษตรกร (เลี้ยงสัตว์)',                 11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 52, '052', 'ลูกจ้างเกษตรกร (ชาวนา ชาวสวน ชาวไร่)', 'ลูกจ้างเกษตรกร (ชาวนา ชาวสวน ชาวไร่)',         11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 53, '053', 'ลูกเรือประมงออกเรือ', 'ลูกเรือประมงออกเรือ',                                 11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 54, '054', 'ลูกจ้างเกษตรกร (เลี้ยงสัตว์)', 'ลูกจ้างเกษตรกร (เลี้ยงสัตว์)',                         11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 55, '055', 'สัตวแพทย์ (ราชการ-เอกชน)', 'สัตวแพทย์ (ราชการ-เอกชน)',                       11, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 56, '056', 'พนักงาน ลูกจ้าง คลีนิครักษาสัตว์', 'พนักงาน ลูกจ้าง คลีนิครักษาสัตว์',                   11, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 57, '057', 'นักออกแบบ/สถาปนิก/มัณฑนากร', 'นักออกแบบ/สถาปนิก/มัณฑนากร', 12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 58, '058', 'นักวิทยาศาสตร์', 'นักวิทยาศาสตร์',                     12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 59, '059', 'วิศวกร', 'วิศวกร',                              12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 60, '060', 'นักคอมพิวเตอร์', 'นักคอมพิวเตอร์',                    12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 61, '061', 'ช่างภาพ/นักข่าวอิสระ', 'ช่างภาพ/นักข่าวอิสระ',              12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 62, '062', 'ทนายความอิสระ', 'ทนายความอิสระ',                    12, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 63, '063', 'นักจัดรายการวิทยุ/โทรทัศน์อิสระ', 'นักจัดรายการวิทยุ/โทรทัศน์อิสระ',  12, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 64, '064', 'นักร้อง/นักแสดง/พิธีกร/นักดนตรี/หางเครื่อง', 'นักร้อง/นักแสดง/พิธีกร/นักดนตรี/หางเครื่อง', 13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 65, '065', 'นักกีฬา', 'นักกีฬา',               13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 66, '066', 'นายหน้า', 'นายหน้า',              13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 67, '067', 'มัคคุเทศก์', 'มัคคุเทศก์',             13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 68, '068', 'นักเขียน', 'นักเขียน',              13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 69, '069', 'รับจ้างซักผ้า, รับจ้างเลี้ยงเด็ก', 'รับจ้างซักผ้า, รับจ้างเลี้ยงเด็ก',    13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 70, '070', 'ค้าขายตลาดนัด/หาบเร่แผงลอย', 'ค้าขายตลาดนัด/หาบเร่แผงลอย', 13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 71, '071', 'ลูกจ้าง, ผู้รับจ้างช่วง, ผู้ประกอบการเหมาก่อสร้าง', 'ลูกจ้าง, ผู้รับจ้างช่วง, ผู้ประกอบการเหมาก่อสร้าง', 13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 72, '072', 'นายหน้าประกัน/ขายตรง', 'นายหน้าประกัน/ขายตรง',        13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 73, '073', 'รถรับซื้อของเก่า', 'รถรับซื้อของเก่า',                  13, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 74, '074', 'รับจ้างทั่วไป', 'รับจ้างทั่วไป',                      13, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 75, '075', 'ขับรถจักรยานยนต์รับจ้างมีใบขับขี่สาธารณะ', 'ขับรถจักรยานยนต์รับจ้างมีใบขับขี่สาธารณะ',            14, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 76, '076', 'ขับรถจักรยานยนต์รับจ้างไม่มีใบขับขี่สาธารณะ', 'ขับรถจักรยานยนต์รับจ้างไม่มีใบขับขี่สาธารณะ',         14, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 77, '077', 'ขับรถรับจ้าง/ขับแท๊กซี่/นำรถวิ่งรวม (มีใบสาธารณะ)', 'ขับรถรับจ้าง/ขับแท๊กซี่/นำรถวิ่งรวม (มีใบสาธารณะ)',    14, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 78, '078', 'ขับรถรับจ้าง/ขับแท๊กซี่/นำรถวิ่งรวม (ไม่มีใบสาธารณะ)', 'ขับรถรับจ้าง/ขับแท๊กซี่/นำรถวิ่งรวม (ไม่มีใบสาธารณะ)', 14, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 79, '079', 'ร้านขายของชำ', 'ร้านขายของชำ',                            15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 80, '080', 'ร้านขายอาหารตามสั่ง', 'ร้านขายอาหารตามสั่ง',                     15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 81, '081', 'ร้านขายอาหารสด-แห้ง (ผัก,ผลไม้,เนื้อสัตว์)', 'ร้านขายอาหารสด-แห้ง (ผัก,ผลไม้,เนื้อสัตว์)', 15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 82, '082', 'ร้านอาหาร กิจการ เฟรมไซน์', 'ร้านอาหาร กิจการ เฟรมไซน์',             15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 83, '083', 'สถานบันเทิง ผับ บาร์ นวดแผนโบราณ อาบอบนวด', 'สถานบันเทิง ผับ บาร์ นวดแผนโบราณ อาบอบนวด', 15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 84, '084', 'ร้านรับซื้อของเก่า/รีไซเคิล', 'ร้านรับซื้อของเก่า/รีไซเคิล',                15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 85, '085', 'ร้านขายอุปกรณ์มือถือ/อุปกรณ์คอมพิวเตอร์', 'ร้านขายอุปกรณ์มือถือ/อุปกรณ์คอมพิวเตอร์', 15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 86, '086', 'ร้านขายต้นไม้ ตกแต่งสวน', 'ร้านขายต้นไม้ ตกแต่งสวน',               15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 87, '087', 'ร้านซักอบรีด/สถานรับเลี้ยงเด็ก', 'ร้านซักอบรีด/สถานรับเลี้ยงเด็ก',           15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 88, '088', 'ร้านเสริมสวย/สปา', 'ร้านเสริมสวย/สปา',                         15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 89, '089', 'บ้านเช่า/อพาร์ทเม้นท์/ห้องให้เช่า', 'บ้านเช่า/อพาร์ทเม้นท์/ห้องให้เช่า',         15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 90, '090', 'กิจการรับจ้างส่งของ ขนส่ง', 'กิจการรับจ้างส่งของ ขนส่ง',                15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 91, '091', 'กิจการให้เช่าอุปกรณ์ต่างๆ', 'กิจการให้เช่าอุปกรณ์ต่างๆ',                15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 92, '092', 'ร้านวิชาชีพช่าง งานประดิษฐ์', 'ร้านวิชาชีพช่าง งานประดิษฐ์',             15, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 93, '093', 'ร้านอู่ซ่อมรถ/ตกแต่ง/ขายอะไหล่/ล้างรถ', 'ร้านอู่ซ่อมรถ/ตกแต่ง/ขายอะไหล่/ล้างรถ', 15, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 94, '094', 'ลูกจ้างงานค้าขาย', 'ลูกจ้างงานค้าขาย',                                  16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 95, '095', 'ลูกจ้างงานบริการ', 'ลูกจ้างงานบริการ',                                   16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 96, '096', 'ลูกจ้างงานช่าง', 'ลูกจ้างงานช่าง',                                      16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 97, '097', 'ลูกจ้างงานเกษตร', 'ลูกจ้างงานเกษตร',                                   16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 98, '098', 'ลูกจ้างสถานบันเทิง ผับ บาร์ คาราโอเกะ', 'ลูกจ้างสถานบันเทิง ผับ บาร์ คาราโอเกะ',           16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 99, '099', 'ลูกจ้างสถานบันเทิง นวดแผนโบราณ อาบอบนวด', 'ลูกจ้างสถานบันเทิง นวดแผนโบราณ อาบอบนวด', 16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 100, '100', 'ลูกจ้างประจำบ้านแม่บ้าน คนสวน', 'ลูกจ้างประจำบ้านแม่บ้าน คนสวน',                 16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 101, '101', 'แคดดี้ สตาร์เตอร์', 'แคดดี้ สตาร์เตอร์',                                  16, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 102, '102', 'คนสวนสนามกอล์ฟ', 'คนสวนสนามกอล์ฟ',                                16, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 103, '103', 'ธุรกิจขนาดเล็ก ทุนจดทะเบียนต่ำกว่า 500,000 บาท', 'ธุรกิจขนาดเล็ก ทุนจดทะเบียนต่ำกว่า 500,000 บาท', 17, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 104, '104', 'ธุรกิจขนาดกลาง 500,000 - /2,000,000 บาท', 'ธุรกิจขนาดกลาง 500,000 - /2,000,000 บาท',      17, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 105, '105', 'ธุรกิจขนาดใหญ่ เกิน 2,000,000 บาท', 'ธุรกิจขนาดใหญ่ เกิน 2,000,000 บาท',                17, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	(240, 106, '106', 'ไม่ประกอบอาชีพ', 'ไม่ประกอบอาชีพ',             18, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 107, '107', 'นักเรียน/นักศึกษา', 'นักเรียน/นักศึกษา',            18, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 108, '108', 'พระ/นักบวช', 'พระ/นักบวช',                  18, null, 1, 1, now(), 'admin', now(), 'admin'),
	(240, 109, '109', 'แม่/พ่อบ้าน ดูแลลูกและสามี', 'แม่/พ่อบ้าน ดูแลลูกและสามี', 18, null, 1, 1, now(), 'admin', now(), 'admin'),

	
    -- EEmploymentStatus
    (241, 1, 'EMP', 'Employed', 'Employed',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (241, 2, 'SEL', 'Self-Employed', 'Self-Employed',         null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EEmploymentIndustryCategory
	(243, 1, '001', 'พนักงานเอกชน (กลุ่มพนักงานบริษัท)', 'พนักงานเอกชน (กลุ่มพนักงานบริษัท)',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 2, '002', 'พนักงานเอกชน (กลุ่มบริษัทตัวแทน)', 'พนักงานเอกชน (กลุ่มบริษัทตัวแทน)',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 3, '003', 'กลุ่มด้านการเงิน การธนาคาร ประกันภัย', 'กลุ่มด้านการเงิน การธนาคาร ประกันภัย',     null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 4, '004', 'กลุ่มอาชีพด้านการศึกษา', 'กลุ่มอาชีพด้านการศึกษา',                                              null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 5, '005', 'กลุ่มอาชีพด้านสาธารณสุข', 'กลุ่มอาชีพด้านสาธารณสุข',                                         null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 6, '006', 'กลุ่มอาชีพพนักงานรัฐวิสาหกิจ', 'กลุ่มอาชีพพนักงานรัฐวิสาหกิจ',                               null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 7, '007', 'กลุ่มอาชีพข้าราชการพลเรือน', 'กลุ่มอาชีพข้าราชการพลเรือน',                                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 8, '008', 'กลุ่มอาชีพข้าราชการท้องถิ่น', 'กลุ่มอาชีพข้าราชการท้องถิ่น',                                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 9, '009', 'กลุ่มอาชีพข้าราชการทหาร/ ตำรวจ', 'กลุ่มอาชีพข้าราชการทหาร/ ตำรวจ',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (243, 10, '010', 'กลุ่มอาชีพนักการเมือง และนักการฑูต', 'กลุ่มอาชีพนักการเมือง และนักการฑูต',           null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (243, 11, '011', 'กลุ่มอาชีพผู้ประกอบวิชาชีพที่มีความชำนาญอิสระ เฉพาะด้าน (ที่ไม่ใช่ข้าราชการ หรือพนักงานบริษัท)', 'กลุ่มอาชีพผู้ประกอบวิชาชีพที่มีความชำนาญอิสระ เฉพาะด้าน (ที่ไม่ใช่ข้าราชการ หรือพนักงานบริษัท)',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 12, '012', 'กลุ่มอาชีพอาชีพอิสระ', 'กลุ่มอาชีพอาชีพอิสระ',                                                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 13, '013', 'กลุ่มอาชีพขับรถรับจ้าง', 'กลุ่มอาชีพขับรถรับจ้าง',                                                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 14, '014', 'กลุ่มอาชีพลูกจ้างร้านค้า และลูกจ้างบุคคล', 'กลุ่มอาชีพลูกจ้างร้านค้า และลูกจ้างบุคคล',    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 15, '015', 'กลุ่มอาชีพด้านการเกษตรและประมง', 'กลุ่มอาชีพด้านการเกษตรและประมง',                null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 16, '016', 'กลุ่มอาชีพผู้ประกอบกิจการ', 'กลุ่มอาชีพผู้ประกอบกิจการ',                                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 17, '017', 'กลุ่มอาชีพธุรกิจจดทะเบียน หจก. บริษัท นิติบุคคล', 'กลุ่มอาชีพธุรกิจจดทะเบียน หจก. บริษัท นิติบุคคล',  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(243, 18, '018', 'กลุ่มอาชีพไม่ประกอบอาชีพ', 'กลุ่มอาชีพไม่ประกอบอาชีพ',                                      null, null, 1, 1, now(), 'admin', now(), 'admin');