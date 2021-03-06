﻿INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- EJournalEventGroup
	(322, 3, '01',          'ทั่วไป',           'ทั่วไป',        null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 4, '02',          'งานทะเบียน',      'งานทะเบียน',    null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 5, '03',          'ค่าส่งเสริมการขาย',  'ค่าส่งเสริมการขาย', null, null,    1, 1, now(), 'admin', now(), 'admin'),
	(322, 6, '04',          'เงินคืน',         'เงินคืน',         null, null,    1, 1, now(), 'admin', now(), 'admin');

-- JournalEvent
ALTER TABLE tu_journal_event DISABLE TRIGGER all;
delete from tu_journal_event; 
INSERT INTO tu_journal_event(jou_eve_id, jou_id, jou_eve_code, jou_eve_desc, jou_eve_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd, jou_eve_grp_id)
    VALUES
    -- Receipts Events
	(1, 5, '01', 'รับชำระตามสัญญา',                                      null, 1, 1, now(), 'admin', now(), 'admin', null),
	(2, 5, '05', 'ปิดบัญชีก่อนกำหนด',                                      null, 1, 1, now(), 'admin', now(), 'admin', null),
	(3, 5, '02', 'ค่าปรับล่าช้า',                                             null, 1, 1, now(), 'admin', now(), 'admin', null),
	(4, 5, '73', 'เงินดาวน์',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(5, 5, '09', 'ค่างวด,ส่วนลด',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(6, 5, '82', 'ค่าส่งเสริมการขาย',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(7, 5, '79', 'ค่าธรรมเนียมการทำสัญญา',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(8, 5, '71', 'ค่าติดตาม',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(9, 5, '63', 'ค่ายกเลิกสัญญา',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(10, 5, '65', 'ค่าตรวจสอบ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(11, 5, '72', 'รับชำระค่าเสียหายทรัพย์สินยึดคืน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(12, 5, '68', 'ค่าบริการเก็บรักษาเล่มทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(13, 5, '20', 'เงินประกันการโอนทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(14, 5, '21', 'ค่ามัดจำทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(15, 5, '23', 'ค่ามัดจำทะเบียนป้ายเหลือง',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(16, 5, '29', 'ค่าจัดส่งงานทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(17, 5, '31', 'ค่าต่อภาษี',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(18, 5, '32', 'ค่าต่อภาษีและค่าปรับ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(19, 5, '33', 'ค่าคัดวงกลม',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(20, 5, '34', 'ค่าคัดป้ายเหล็ก',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(21, 5, '35', 'ค่าเปลี่ยนสี',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(22, 5, '37', 'ค่าแจ้งย้าย',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(23, 5, '38', 'ค่าโอนทะเบียน 1 ต่อ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(24, 5, '39', 'ค่าโอนทะเบียน 2 ต่อ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(25, 5, '40', 'ค่าปรับโอนล่าช้า',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(26, 5, '41', 'ค่าแจ้งความ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(27, 5, '42', 'ค่าขอใช้ฯ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(28, 5, '44', 'ค่าต่อภาษี 2 ปี และค่าปรับ ค่าตรวจสภาพรถ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(29, 5, '45', 'ค่าปรับใบสั่ง',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(30, 5, '46', 'ค่าสมุดคู่มือจดทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(31, 5, '47', 'ค่าโอนทะเบียนเข้าบริษัท',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(32, 5, '48', 'ค่าแจ้งเปลี่ยนผู้ครอบครอง',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(33, 5, '49', 'แก้ไขทะเบียนที่จดผิด',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(34, 5, '53', 'ค่าเปลี่ยนชื่อ-สกุล',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(35, 5, '55', 'ค่าเปลี่ยนแปลงที่อยู่',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(36, 5, '56', 'ค่าระงับการแจ้งย้าย',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(37, 5, '57', 'ค่าเปลี่ยนประเภทรถ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(38, 5, '60', 'ค่าตรวจสภาพรถ (รถใหม่)',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(39, 5, '28', 'ค่าปรับปรุงใบคู่มือจดทะเบียน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(40, 5, '04', 'พักรับไว้เพื่อนำส่งบริษัทประกันฯ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(41, 5, '06', 'รับค่าเบี้ยประกันและภาษีอากรเพื่อนำส่งบริษัทประกัน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(42, 5, '75', 'ค่าบริการในการดำเนินการ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(43, 5, '70', 'ขายทรัพย์สินรอการขาย',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(44, 5, '03', 'ค่าปรับ เช็คคืน/หักบัญชีไม่ผ่าน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(45, 5, '87', 'โปรดคีย์รายละเอียดการรับค่าบริการ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(46, 5, '84', 'เงินชดเชยกรณีรถยึด',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(47, 5, '89', 'รับคืนค่าเบี้ยประกันรถจยย',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(48, 5, '91', 'รับคืนภ.พ.ค้างชำระ',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(49, 5, '07', 'โปรดคีย์ รายละเอียดการรับ ที่รวมภาษีมูลค่าเพิ่ม',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(50, 5, '69', 'โปรดคีย์ รายละเอียดการรับ ที่ไม่มีภาษีมูลค่าเพิ่ม',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(51, 5, '98', 'รับไว้เพื่อตัดบัญชี',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(52, 5, '99', 'รับไว้เพื่อรอจ่ายคืน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(53, 5, '80', 'รับเงินค่าธรรมเนียมศาลคืน',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
	(54, 5, '81', 'รับชำระหนี้ตามคำพิพากษา',                   null, 1, 1, now(), 'admin', now(), 'admin', null),
    
    -- Payments Events
    (155, 4, '0301', 'ค่าเบี้ยประกันภัยภาคบังคับ พรบ.ประจำงวด',                     null, 1, 1, now(), 'admin', now(), 'admin', 3		),
	(156, 4, '0705', 'ค่าส่งเสริมการขายค่าเบี้ยประกันภัยรถหาย',                    null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(157, 4, '0729', 'ค่าใช้จ่ายในการบังคับคดี เลขสัญญา',                    null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(158, 4, '0731', 'ชำระ ค่าใช้จ่ายเกี่ยวกับงานทะเบียน (งานทะเบียนที่บริษัทรับภาระค่าใช้จ่าย)',                    null, 1, 1, now(), 'admin', now(), 'admin', 4		),
	(159, 4, '0732', 'ชำระ ค่าใช้จ่ายค่าติดตาม เลขสัญญา',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(160, 4, '0733', 'ค่าบริการสื่อสารรับส่งข้อมูล',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(161, 4, '0736', 'ค่าใช้จ่ายในการติดตาม เลขสัญญา',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(162, 4, '0743', 'ชำระค่าอากรแสตมป์ อส.4ข ประจำงวด',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(163, 4, '3401', 'ค่าส่งเสริมการขายพิเศษ-ร้านค้า ประจำงวด',                     null, 1, 1, now(), 'admin', now(), 'admin', 5		),
	(164, 4, '3499', 'ค่าส่งเสริมการาขายพิเศษ-อื่นๆ ประจำงวด',                     null, 1, 1, now(), 'admin', now(), 'admin', 5		),
	(165, 4, '3701', 'ชำระค่าเบี้ยประกันภัยรถจักรยานยนต์ ประจำงวดเดือน',                    null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(166, 4, '3702', 'คืนค่าเบี้ยประกันภัย',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(167, 4, '3801', 'ชำระค่าเบี้ยประกันภัยภาคบังคับ พ.ร.บ. รถจักรยานยนต์ ประจำงวดเดือน ',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(168, 4, '3901', 'คืนเงินประกันความเสียหายเนื่องจากลูกค้าปิดบัญชีแล้ว เลขสัญญา',                     null, 1, 1, now(), 'admin', now(), 'admin', 6		),
	(169, 4, '4301', 'ชำระค่าต่อภาษีรถจักรยานยนต์ ตามสัญญาขนส่ง งวดที่ ',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(170, 4, '4402', 'ชำระค่าใช้จ่ายในการฟ้อง (ค่าจ้างทนาย,ค่านำหมาย,ฯลฯ)',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(171, 4, '4501', 'ชำระค่าบริการรับจ้างยึดรถ จำนวน...คัน',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(172, 4, '4502', 'ชำระค่าบริการพิเศษเป้ายึดรถประจำงวดเดือน',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(173, 4, '4601', 'โอนเงินประกันรับจ้างยึดงวด ฝากธนาคารชื่อบัญชีผู้รับจ้างยึด',                     null, 1, 1, now(), 'admin', now(), 'admin', 3),
	(174, 4, '9602', 'คืนเงินร้านเนื่องจาก...............',                     null, 1, 1, now(), 'admin', now(), 'admin', 6		),
	(175, 4, '9603', 'คืนเงินมัดจำทะเบียนสัญญา...........',                    null, 1, 1, now(), 'admin', now(), 'admin', 6		),
	(176, 4, '9604', 'คืนเงินค่าบริการงานทะเบียน',                    null, 1, 1, now(), 'admin', now(), 'admin', 6		),
	(177, 4, '9605', 'คืนเงินส่วนลดปิดบัญชีก่อนกำหนด',                    null, 1, 1, now(), 'admin', now(), 'admin', 3);
select setval('tu_journal_event_jou_eve_id_seq', 200);
ALTER TABLE tu_journal_event ENABLE TRIGGER all;
update tu_journal_event set jou_eve_desc_en = jou_eve_desc where jou_id = 5;
update tu_journal_event set jou_eve_desc_en = jou_eve_desc where jou_id = 4;