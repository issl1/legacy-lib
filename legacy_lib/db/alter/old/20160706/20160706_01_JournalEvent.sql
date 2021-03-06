﻿-- JournalEvent
ALTER TABLE tu_journal_event DISABLE TRIGGER all;
delete from tu_journal_event; 
INSERT INTO tu_journal_event(jou_eve_id, jou_id, jou_eve_code, jou_eve_desc, jou_eve_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd, jou_eve_grp_id)
    VALUES
    -- Receipts Events
	(1, 5, '01', 'รับชำระตามสัญญา', 'receive payments according to contract', 1, 1, now(), 'admin', now(), 'admin', null),
	(2, 5, '02', 'ค่าปรับล่าช้า', 'delay penalty', 1, 1, now(), 'admin', now(), 'admin', null),
	(3, 5, '03', 'ค่าปรับ เช็คคืน/หักบัญชีไม่ผ่าน', 'penalty of returned cheque/ unsuccessful account debiting', 1, 1, now(), 'admin', now(), 'admin', null),
	(4, 5, '04', 'พักรับไว้เพื่อนำส่งบริษัทประกันฯ', 'suspend for sending to insurance company', 1, 1, now(), 'admin', now(), 'admin', null),
	(5, 5, '05', 'ปิดบัญชีก่อนกำหนด', 'early account closure', 1, 1, now(), 'admin', now(), 'admin', null),
	(6, 5, '06', 'รับค่าเบี้ยประกันและภาษีอากรเพื่อนำส่งบริษัทประกัน', 'receive insurance premium and tax to send to insurance company', 1, 1, now(), 'admin', now(), 'admin', null),
	(7, 5, '07', 'โปรดคีย์ รายละเอียดการรับ ที่รวมภาษีมูลค่าเพิ่ม', 'please key receipt detail which includes value added tax', 1, 1, now(), 'admin', now(), 'admin', null),
	(8, 5, '09', 'ค่างวด,ส่วนลด', 'installment, discount ', 1, 1, now(), 'admin', now(), 'admin', null),
	(9, 5, '20', 'เงินประกันการโอนทะเบียน', 'deposit of register transfer', 1, 1, now(), 'admin', now(), 'admin', null),
	(10, 5, '21', 'ค่ามัดจำทะเบียน', 'car registration deposit', 1, 1, now(), 'admin', now(), 'admin', null),
	(11, 5, '23', 'ค่ามัดจำทะเบียนป้ายเหลือง', 'deposit of yellow license plate registration', 1, 1, now(), 'admin', now(), 'admin', null),
	(12, 5, '28', 'ค่าปรับปรุงใบคู่มือจดทะเบียน', 'fee of updating car registration book ', 1, 1, now(), 'admin', now(), 'admin', null),
	(13, 5, '29', 'ค่าจัดส่งงานทะเบียน', 'delivery charge to registration', 1, 1, now(), 'admin', now(), 'admin', null),
	(14, 5, '31', 'ค่าต่อภาษี', 'tax extension fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(15, 5, '32', 'ค่าต่อภาษีและค่าปรับ', 'tax extension fee and penalty', 1, 1, now(), 'admin', now(), 'admin', null),
	(16, 5, '33', 'ค่าคัดวงกลม', 'fee of request for new taxation sign', 1, 1, now(), 'admin', now(), 'admin', null),
	(17, 5, '34', 'ค่าคัดป้ายเหล็ก', 'fee of request for new license plate', 1, 1, now(), 'admin', now(), 'admin', null),
	(18, 5, '35', 'ค่าเปลี่ยนสี', 'color change expense', 1, 1, now(), 'admin', now(), 'admin', null),
	(19, 5, '37', 'ค่าแจ้งย้าย', 'relocation fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(20, 5, '38', 'ค่าโอนทะเบียน 1 ต่อ', 'fee of car registration transfer( 1 time)', 1, 1, now(), 'admin', now(), 'admin', null),
	(21, 5, '39', 'ค่าโอนทะเบียน 2 ต่อ', 'fee of car registration transfer( 2 times)', 1, 1, now(), 'admin', now(), 'admin', null),
	(22, 5, '40', 'ค่าปรับโอนล่าช้า', 'delay penalty of transfer', 1, 1, now(), 'admin', now(), 'admin', null),
	(23, 5, '41', 'ค่าแจ้งความ', 'police notification fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(24, 5, '42', 'ค่าขอใช้ฯ', 'fee of use request ', 1, 1, now(), 'admin', now(), 'admin', null),
	(25, 5, '44', 'ค่าต่อภาษี 2 ปี และค่าปรับ ค่าตรวจสภาพรถ', '2 years tax extension fee, penalty, car check fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(26, 5, '45', 'ค่าปรับใบสั่ง', 'ticket penalty', 1, 1, now(), 'admin', now(), 'admin', null),
	(27, 5, '46', 'ค่าสมุดคู่มือจดทะเบียน', 'car registration book fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(28, 5, '47', 'ค่าโอนทะเบียนเข้าบริษัท', 'fee of transferring register to company', 1, 1, now(), 'admin', now(), 'admin', null),
	(29, 5, '48', 'ค่าแจ้งเปลี่ยนผู้ครอบครอง', 'fee of changing owner ', 1, 1, now(), 'admin', now(), 'admin', null),
	(30, 5, '49', 'แก้ไขทะเบียนที่จดผิด', 'edit wrong registration', 1, 1, now(), 'admin', now(), 'admin', null),
	(31, 5, '53', 'ค่าเปลี่ยนชื่อ-สกุล', 'name-lastname change fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(32, 5, '55', 'ค่าเปลี่ยนแปลงที่อยู่', 'address change fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(33, 5, '56', 'ค่าระงับการแจ้งย้าย', 'fee of relocation suspension', 1, 1, now(), 'admin', now(), 'admin', null),
	(34, 5, '57', 'ค่าเปลี่ยนประเภทรถ', 'fee of changing car type', 1, 1, now(), 'admin', now(), 'admin', null),
	(35, 5, '60', 'ค่าตรวจสภาพรถ (รถใหม่)', 'car check fee ( new car)', 1, 1, now(), 'admin', now(), 'admin', null),
	(36, 5, '63', 'ค่ายกเลิกสัญญา', 'contract termination fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(37, 5, '65', 'ค่าตรวจสอบ', 'inspection fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(38, 5, '68', 'ค่าบริการเก็บรักษาเล่มทะเบียน', 'fee of keeping car registration book ', 1, 1, now(), 'admin', now(), 'admin', null),
	(39, 5, '69', 'โปรดคีย์ รายละเอียดการรับ ที่ไม่มีภาษีมูลค่าเพิ่ม', 'please key receipt detail which doesn’t include value added tax', 1, 1, now(), 'admin', now(), 'admin', null),
	(40, 5, '70', 'ขายทรัพย์สินรอการขาย', 'sell assets pending for sale', 1, 1, now(), 'admin', now(), 'admin', null),
	(41, 5, '71', 'ค่าติดตาม', 'follow-up fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(42, 5, '72', 'รับชำระค่าเสียหายทรัพย์สินยึดคืน', 'receive payment from damages of confiscated assets', 1, 1, now(), 'admin', now(), 'admin', null),
	(43, 5, '73', 'เงินดาวน์', 'down payment ', 1, 1, now(), 'admin', now(), 'admin', null),
	(44, 5, '75', 'ค่าบริการในการดำเนินการ', 'operation fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(45, 5, '79', 'ค่าธรรมเนียมการทำสัญญา', 'contract fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(46, 5, '80', 'รับเงินค่าธรรมเนียมศาลคืน', 'receive court fee ', 1, 1, now(), 'admin', now(), 'admin', null),
	(47, 5, '81', 'รับชำระหนี้ตามคำพิพากษา', 'receive payments according to court judgment', 1, 1, now(), 'admin', now(), 'admin', null),
	(48, 5, '82', 'ค่าส่งเสริมการขาย', 'sales promotion fee', 1, 1, now(), 'admin', now(), 'admin', null),
	(49, 5, '84', 'เงินชดเชยกรณีรถยึด', 'compensation in case of car confiscation', 1, 1, now(), 'admin', now(), 'admin', null),
	(50, 5, '87', 'โปรดคีย์รายละเอียดการรับค่าบริการ', 'please key detail of receiving service charge', 1, 1, now(), 'admin', now(), 'admin', null),
	(51, 5, '89', 'รับคืนค่าเบี้ยประกันรถจยย', 'receive bike insurance premium ', 1, 1, now(), 'admin', now(), 'admin', null),
	(52, 5, '91', 'รับคืนภ.พ.ค้างชำระ', 'receive unpaid VAT ', 1, 1, now(), 'admin', now(), 'admin', null),
	(53, 5, '98', 'รับไว้เพื่อตัดบัญชี', 'receive to  clear account', 1, 1, now(), 'admin', now(), 'admin', null),
	(54, 5, '99', 'รับไว้เพื่อรอจ่ายคืน', 'receive for pending to pay ', 1, 1, now(), 'admin', now(), 'admin', null),
	(55, 5, '00', 'โปรดคีย์ รายละเอียดการรับ', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(56, 5, '08', 'ค่าตอบแทนการให้เช่า-', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(57, 5, '10', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(58, 5, '11', 'ค่าบริการตามสิทธิเรียกร้อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(59, 5, '12', 'เงินประกันสัญญาโอนสิทธิเรียกร้อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(60, 5, '13', 'ค่าปรับล่าช้า', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(61, 5, '14', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(62, 5, '15', 'รับชำระตามการโอนสิทธิเรียกร้องเพื่อปิดบัญชี', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(63, 5, '16', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(64, 5, '17', 'ค่าธรรมเนียมการขอเบิกใช้วงเงิน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(65, 5, '19', 'รับชำระตามสัญญา', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(66, 5, '22', 'ค่าปรับหักบัญชีไม่ผ่าน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(67, 5, '27', 'ค่าตรวจสอบการอายัดทะเบียน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(68, 5, '30', 'ค่าอากร', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(69, 5, '36', 'ค่ารับป้ายแผ่นเหล็ก', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(70, 5, '43', 'ค่าเปลี่ยนสี พร้อมโอนทะเบียน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(71, 5, '50', 'ค่าเปลี่ยนเครื่อง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(72, 5, '51', 'ค่าเปลี่ยนเครื่องและปรับ', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(73, 5, '52', 'ค่าปรับใบเสร็จเปลี่ยนเครื่องล่าช้า', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(74, 5, '54', 'ค่าใบเสร็จเปลี่ยนสีล่าช้า', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(75, 5, '58', 'ค่าเปลี่ยนชื่อ กรุ๊ปลีส จาก  บจก. เป็น บมจ.', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(76, 5, '59', 'ค่าบริการ-งานทะเบียน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(77, 5, '62', 'ค่าธรรมเนียมจัดการ', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(78, 5, '66', 'รายได้-ส่วนลดเบี้ยประกัน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(79, 5, '67', 'ค่าธรรมเนียมจัดการสินเชื่อเงินสด', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(80, 5, '74', 'เงินประกันความเสียหาย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(81, 5, '76', 'ขายทรัพย์สินตามสัญญาเช่าแบบลีสซิ่ง', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(82, 5, '77', 'ค่าธรรมเนียมธนาคาร', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(83, 5, '78', 'ค่าบริการตัวแทนเก็บเงิน', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(84, 5, '83', 'เงินดาวน์', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(85, 5, '85', 'ค่าบริการประกันภัยไตรมาสที่ N/ปีพ.ศ.', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(86, 5, '86', 'รายได้-ส่วนลดเบี้ยประกันภัย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(87, 5, '88', 'โปรดคีย์รายละเอียดค่าเช่าที่มีภาษีมูลค่าเพิ่ม', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(88, 5, '94', 'เงินประกันความเสียหาย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(89, 5, '95', 'เงินประกันทรัพย์สินฝากขาย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(90, 5, '96', 'รับคืนเงินทดรองจ่าย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(91, 5, '97', 'รับไว้เพื่อหาเลขสัญญา', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(92, 5, 'AS', 'ทรัพย์สินรอการขาย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(93, 5, 'BD', 'ตัดหนี้สูญ', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(94, 5, 'CA', 'สินเชื่อเงินสด', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(95, 5, 'CC', 'ปิดบัญชีงวดสุดท้าย', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(96, 5, 'FL', 'ปิดบัญชี', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(97, 5, 'XX', 'ปิดบัญชี', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(98, 5, 'RR', '', '', 1, 2, now(), 'admin', now(), 'admin', null),
	(99, 5, 'บบ', '', '', 1, 2, now(), 'admin', now(), 'admin', null),

    -- Payments Events
    (155, 4, '0301', 'ค่าเบี้ยประกันภัยภาคบังคับพรบ.  ประจำงวด', 'Insurance premium of compulsory insurance  period…', 1, 1, now(), 'admin', now(), 'admin', 3),
	(156, 4, '0705', 'ค่าส่งเสริมการขายค่าเบี้ยประกันภัยรถหาย', 'Fee of sales promotion of lost case car insurance premium', 1, 1, now(), 'admin', now(), 'admin', 3),
	(157, 4, '0729', 'ค่าใช้จ่ายในการบังคับคดี   เลขสัญญา', 'Cost of compulsory execution  contract number..', 1, 1, now(), 'admin', now(), 'admin', 3),
	(158, 4, '0731', 'ชำระ ค่าใช้จ่ายเกี่ยวกับงานทะเบียน (งานทะเบียนที่บริษัทรับภาระค่าใช้จ่าย)', 'Pay  registration-involved cost registration that is under control of the company)', 1, 1, now(), 'admin', now(), 'admin', 4),
	(159, 4, '0732', 'ชำระ ค่าใช้จ่ายค่าติดตาม  เลขสัญญา', 'Pay  expense of follow-up            contract number…', 1, 1, now(), 'admin', now(), 'admin', 3),
	(160, 4, '0733', 'ค่าบริการสื่อสารรับส่งข้อมูล', 'Fee of communication', 1, 1, now(), 'admin', now(), 'admin', 3),
	(161, 4, '0736', 'ค่าใช้จ่ายในการติดตาม  เลขสัญญา', 'Expense of follow-up  contract number…', 1, 1, now(), 'admin', now(), 'admin', 3),
	(162, 4, '0743', 'ชำระค่าอากรแสตมป์ อส.4ข ประจำงวด', 'Pay อส.4ข  stamp fee อส.4ข = form of stamp duty payment in cash) period...', 1, 1, now(), 'admin', now(), 'admin', 3),
	(163, 4, '3401', 'ค่าส่งเสริมการขายพิเศษ-ร้านค้า ประจำงวด', 'Special sales promotion fee - shops  period..', 1, 1, now(), 'admin', now(), 'admin', 5),
	(164, 4, '3499', 'ค่าส่งเสริมการาขายพิเศษ-อื่นๆ ประจำงวด', 'Special sales promotion fee - etc.  period..', 1, 1, now(), 'admin', now(), 'admin', 5),
	(165, 4, '3701', 'ชำระค่าเบี้ยประกันภัยรถจักรยานยนต์ ประจำงวดเดือน', 'Pay bike inusurance premium  period..', 1, 1, now(), 'admin', now(), 'admin', 3),
	(166, 4, '3702', 'คืนค่าเบี้ยประกันภัย', 'Return insurance premium', 1, 1, now(), 'admin', now(), 'admin', 3),
	(167, 4, '3801', 'ชำระค่าเบี้ยประกันภัยภาคบังคับ พ.ร.บ. รถจักรยานยนต์ ประจำงวดเดือน ', 'Pay  compulsory insurance premium  period..', 1, 1, now(), 'admin', now(), 'admin', 3),
	(168, 4, '3901', 'คืนเงินประกันความเสียหายเนื่องจากลูกค้าปิดบัญชีแล้ว เลขสัญญา', 'Return security deposit due to account closing  contract number…', 1, 1, now(), 'admin', now(), 'admin', 6),
	(169, 4, '4301', 'ชำระค่าต่อภาษีรถจักรยานยนต์ ตามสัญญาขนส่ง งวดที่ ', 'Pay bike tax extension according to contract of Department of Transportation  installment…..', 1, 1, now(), 'admin', now(), 'admin', 3),
	(170, 4, '4402', 'ชำระค่าใช้จ่ายในการฟ้อง (ค่าจ้างทนาย,ฯลฯ)', 'Pay  prosecution cost  ( lawyer-hire, etc.)', 1, 1, now(), 'admin', now(), 'admin', 3),
	(171, 4, '4501', 'ชำระค่าบริการรับจ้างยึดรถ จำนวน...คัน', 'Pay  car confiscation service charge  numbers of bikes…..', 1, 1, now(), 'admin', now(), 'admin', 3),
	(172, 4, '4502', 'ชำระค่าบริการพิเศษยึดรถ    ประจำงวดเดือน', 'Pay  Special  service charge of car confiscation  period…', 1, 1, now(), 'admin', now(), 'admin', 3),
	(173, 4, '4601', 'โอนเงินประกันรับจ้างยึดงวด ฝากธนาคารชื่อบัญชีผู้รับจ้างยึด', 'Transfer deposit money of car confiscation service ,  deposit money to the bank as an account of confiscator', 1, 1, now(), 'admin', now(), 'admin', 3),
	(174, 4, '9602', 'คืนเงินร้านเนื่องจาก...............', 'Return money to the shop due to……..', 1, 1, now(), 'admin', now(), 'admin', 6),
	(175, 4, '9603', 'คืนเงินมัดจำทะเบียนสัญญา...........', 'Return deposit of contract register', 1, 1, now(), 'admin', now(), 'admin', 6),
	(176, 4, '9604', 'คืนเงินค่าบริการงานทะเบียน', 'Return registration fee', 1, 1, now(), 'admin', now(), 'admin', 6),
	(177, 4, '9605', 'คืนเงินส่วนลดปิดบัญชีก่อนกำหนด', 'Return discount of early account closing', 1, 1, now(), 'admin', now(), 'admin', 3),
	(178, 4, '0101', 'Pay for financement', 'Pay for financement', 1, 1, now(), 'admin', now(), 'admin', 3),
	(179, 4, '0201', 'Pay for registration', 'Pay for registration', 1, 1, now(), 'admin', now(), 'admin', 3);
select setval('tu_journal_event_jou_eve_id_seq', 300);
ALTER TABLE tu_journal_event ENABLE TRIGGER all;