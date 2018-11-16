﻿ALTER TABLE tu_lock_split_type DISABLE TRIGGER all;
delete from tu_lock_split_type;
INSERT INTO tu_lock_split_type(loc_spl_typ_id,loc_spl_typ_code,loc_spl_typ_desc,loc_spl_typ_desc_en,sta_rec_id,sort_index,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	(1, '01', 'รับชำระตามสัญญา', 'receive payments according to contract', 1,                       1, now(), 'admin', now(), 'admin'),
	(2, '02', 'ค่าปรับล่าช้า', 'delay penalty', 1,                                                    1, now(), 'admin', now(), 'admin'),
	(3, '03', 'ค่าปรับ เช็คคืน/หักบัญชีไม่ผ่าน', 'penalty of returned cheque/ unsuccessful account debiting', 1, 1, now(), 'admin', now(), 'admin'),
	(4, '04', 'พักรับไว้เพื่อนำส่งบริษัทประกันฯ', 'suspend for sending to insurance company', 1,              1, now(), 'admin', now(), 'admin'),
	(5, '05', 'ปิดบัญชีก่อนกำหนด', 'early account closure', 1,                                        1, now(), 'admin', now(), 'admin'),
	(6, '06', 'รับค่าเบี้ยประกันและภาษีอากรเพื่อนำส่งบริษัทประกัน', 'receive insurance premium and tax to send to insurance company', 1, 1, now(), 'admin', now(), 'admin'),
	(7, '07', 'โปรดคีย์ รายละเอียดการรับ ที่รวมภาษีมูลค่าเพิ่ม', 'please key receipt detail which includes value added tax', 1, 1, now(), 'admin', now(), 'admin'),
	(8, '09', 'ค่างวด,ส่วนลด', 'installment, discount ', 1,                                          1, now(), 'admin', now(), 'admin'),
	(9, '20', 'เงินประกันการโอนทะเบียน', 'deposit of register transfer', 1,                             1, now(), 'admin', now(), 'admin'),
	(10, '21', 'ค่ามัดจำทะเบียน', 'car registration deposit', 1,                                      1, now(), 'admin', now(), 'admin'),
	(11, '23', 'ค่ามัดจำทะเบียนป้ายเหลือง', 'deposit of yellow license plate registration', 1,            1, now(), 'admin', now(), 'admin'),
	(12, '28', 'ค่าปรับปรุงใบคู่มือจดทะเบียน', 'fee of updating car registration book ', 1,                 1, now(), 'admin', now(), 'admin'),
	(13, '29', 'ค่าจัดส่งงานทะเบียน', 'delivery charge to registration', 1,                             1, now(), 'admin', now(), 'admin'),
	(14, '31', 'ค่าต่อภาษี', 'tax extension fee', 1,                                                 1, now(), 'admin', now(), 'admin'),
	(15, '32', 'ค่าต่อภาษีและค่าปรับ', 'tax extension fee and penalty', 1,                               1, now(), 'admin', now(), 'admin'),
	(16, '33', 'ค่าคัดวงกลม', 'fee of request for new taxation sign', 1,                            1, now(), 'admin', now(), 'admin'),
	(17, '34', 'ค่าคัดป้ายเหล็ก', 'fee of request for new license plate', 1,                           1, now(), 'admin', now(), 'admin'),
	(18, '35', 'ค่าเปลี่ยนสี', 'color change expense', 1,                                              1, now(), 'admin', now(), 'admin'),
	(19, '37', 'ค่าแจ้งย้าย', 'relocation fee', 1,                                                    1, now(), 'admin', now(), 'admin'),
	(20, '38', 'ค่าโอนทะเบียน 1 ต่อ', 'fee of car registration transfer( 1 time)', 1,                   1, now(), 'admin', now(), 'admin'),
	(21, '39', 'ค่าโอนทะเบียน 2 ต่อ', 'fee of car registration transfer( 2 times)', 1,                  1, now(), 'admin', now(), 'admin'),
	(22, '40', 'ค่าปรับโอนล่าช้า', 'delay penalty of transfer', 1,                                     1, now(), 'admin', now(), 'admin'),
	(23, '41', 'ค่าแจ้งความ', 'police notification fee', 1,                                         1, now(), 'admin', now(), 'admin'),
	(24, '42', 'ค่าขอใช้ฯ', 'fee of use request ', 1,                                               1, now(), 'admin', now(), 'admin'),
	(25, '44', 'ค่าต่อภาษี 2 ปี และค่าปรับ ค่าตรวจสภาพรถ', '2 years tax extension fee, penalty, car check fee', 1, 1, now(), 'admin', now(), 'admin'),
	(26, '45', 'ค่าปรับใบสั่ง', 'ticket penalty', 1,                                                 1, now(), 'admin', now(), 'admin'),
	(27, '46', 'ค่าสมุดคู่มือจดทะเบียน', 'car registration book fee', 1,                                1, now(), 'admin', now(), 'admin'),
	(28, '47', 'ค่าโอนทะเบียนเข้าบริษัท', 'fee of transferring register to company', 1,                 1, now(), 'admin', now(), 'admin'),
	(29, '48', 'ค่าแจ้งเปลี่ยนผู้ครอบครอง', 'fee of changing owner ', 1,                                 1, now(), 'admin', now(), 'admin'),
	(30, '49', 'แก้ไขทะเบียนที่จดผิด', 'edit wrong registration', 1,                                   1, now(), 'admin', now(), 'admin'),
	(31, '53', 'ค่าเปลี่ยนชื่อ-สกุล', 'name-lastname change fee', 1,                                    1, now(), 'admin', now(), 'admin'),
	(32, '55', 'ค่าเปลี่ยนแปลงที่อยู่', 'address change fee', 1,                                         1, now(), 'admin', now(), 'admin'),
	(33, '56', 'ค่าระงับการแจ้งย้าย', 'fee of relocation suspension', 1,                              1, now(), 'admin', now(), 'admin'),
	(34, '57', 'ค่าเปลี่ยนประเภทรถ', 'fee of changing car type', 1,                                  1, now(), 'admin', now(), 'admin'),
	(35, '60', 'ค่าตรวจสภาพรถ (รถใหม่)', 'car check fee ( new car)', 1,                              1, now(), 'admin', now(), 'admin'),
	(36, '63', 'ค่ายกเลิกสัญญา', 'contract termination fee', 1,                                    1, now(), 'admin', now(), 'admin'),
	(37, '65', 'ค่าตรวจสอบ', 'inspection fee', 1,                                                 1, now(), 'admin', now(), 'admin'),
	(38, '68', 'ค่าบริการเก็บรักษาเล่มทะเบียน', 'fee of keeping car registration book ', 1,                1, now(), 'admin', now(), 'admin'),
	(39, '69', 'โปรดคีย์ รายละเอียดการรับ ที่ไม่มีภาษีมูลค่าเพิ่ม', 'please key receipt detail which doesn’t include value added tax', 1, 1, now(), 'admin', now(), 'admin'),
	(40, '70', 'ขายทรัพย์สินรอการขาย', 'sell assets pending for sale', 1,                             1, now(), 'admin', now(), 'admin'),
	(41, '71', 'ค่าติดตาม', 'follow-up fee', 1,                                                    1, now(), 'admin', now(), 'admin'),
	(42, '72', 'รับชำระค่าเสียหายทรัพย์สินยึดคืน', 'receive payment from damages of confiscated assets', 1, 1, now(), 'admin', now(), 'admin'),
	(43, '73', 'เงินดาวน์', 'down payment ', 1,                                                    1, now(), 'admin', now(), 'admin'),
	(44, '75', 'ค่าบริการในการดำเนินการ', 'operation fee', 1,                                           1, now(), 'admin', now(), 'admin'),
	(45, '79', 'ค่าธรรมเนียมการทำสัญญา', 'contract fee', 1,                                           1, now(), 'admin', now(), 'admin'),
	(46, '80', 'รับเงินค่าธรรมเนียมศาลคืน', 'receive court fee ', 1,                                     1, now(), 'admin', now(), 'admin'),
	(47, '81', 'รับชำระหนี้ตามคำพิพากษา', 'receive payments according to court judgment', 1,           1, now(), 'admin', now(), 'admin'),
	(48, '82', 'ค่าส่งเสริมการขาย', 'sales promotion fee', 1,                                         1, now(), 'admin', now(), 'admin'),
	(49, '84', 'เงินชดเชยกรณีรถยึด', 'compensation in case of car confiscation', 1,                   1, now(), 'admin', now(), 'admin'),
	(50, '87', 'โปรดคีย์รายละเอียดการรับค่าบริการ', 'please key detail of receiving service charge', 1,     1, now(), 'admin', now(), 'admin'),
	(51, '89', 'รับคืนค่าเบี้ยประกันรถจยย', 'receive bike insurance premium ', 1,                         1, now(), 'admin', now(), 'admin'),
	(52, '91', 'รับคืนภ.พ.ค้างชำระ', 'receive unpaid VAT ', 1,                                        1, now(), 'admin', now(), 'admin'),
	(53, '98', 'รับไว้เพื่อตัดบัญชี', 'receive to  clear account', 1,                                   1, now(), 'admin', now(), 'admin'),
	(54, '99', 'รับไว้เพื่อรอจ่ายคืน', 'receive for pending to pay ', 1,                                1, now(), 'admin', now(), 'admin'),
	(55, '00', 'โปรดคีย์ รายละเอียดการรับ', '', 2,                                                      1, now(), 'admin', now(), 'admin'),
	(56, '08', 'ค่าตอบแทนการให้เช่า-', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(57, '10', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 2,                                                  1, now(), 'admin', now(), 'admin'),
	(58, '11', 'ค่าบริการตามสิทธิเรียกร้อง', '', 2,                                                      1, now(), 'admin', now(), 'admin'),
	(59, '12', 'เงินประกันสัญญาโอนสิทธิเรียกร้อง', '', 2,                                                 1, now(), 'admin', now(), 'admin'),
	(60, '13', 'ค่าปรับล่าช้า', '', 2,                                                              1, now(), 'admin', now(), 'admin'),
	(61, '14', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 2,                                                  1, now(), 'admin', now(), 'admin'),
	(62, '15', 'รับชำระตามการโอนสิทธิเรียกร้องเพื่อปิดบัญชี', '', 2,                                           1, now(), 'admin', now(), 'admin'),
	(63, '16', 'รับชำระตามการโอนสิทธิเรียกร้อง', '', 2,                                                  1, now(), 'admin', now(), 'admin'),
	(64, '17', 'ค่าธรรมเนียมการขอเบิกใช้วงเงิน', '', 2,                                                   1, now(), 'admin', now(), 'admin'),
	(65, '19', 'รับชำระตามสัญญา', '', 2,                                                          1, now(), 'admin', now(), 'admin'),
	(66, '22', 'ค่าปรับหักบัญชีไม่ผ่าน', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(67, '27', 'ค่าตรวจสอบการอายัดทะเบียน', '', 2,                                                    1, now(), 'admin', now(), 'admin'),
	(68, '30', 'ค่าอากร', '', 2,                                                                 1, now(), 'admin', now(), 'admin'),
	(69, '36', 'ค่ารับป้ายแผ่นเหล็ก', '', 2,                                                          1, now(), 'admin', now(), 'admin'),
	(70, '43', 'ค่าเปลี่ยนสี พร้อมโอนทะเบียน', '', 2,                                                    1, now(), 'admin', now(), 'admin'),
	(71, '50', 'ค่าเปลี่ยนเครื่อง', '', 2,                                                            1, now(), 'admin', now(), 'admin'),
	(72, '51', 'ค่าเปลี่ยนเครื่องและปรับ', '', 2,                                                       1, now(), 'admin', now(), 'admin'),
	(73, '52', 'ค่าปรับใบเสร็จเปลี่ยนเครื่องล่าช้า', '', 2,                                                  1, now(), 'admin', now(), 'admin'),
	(74, '54', 'ค่าใบเสร็จเปลี่ยนสีล่าช้า', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(75, '58', 'ค่าเปลี่ยนชื่อ กรุ๊ปลีส จาก  บจก. เป็น บมจ.', '', 2,                                              1, now(), 'admin', now(), 'admin'),
	(76, '59', 'ค่าบริการ-งานทะเบียน', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(77, '62', 'ค่าธรรมเนียมจัดการ', '', 2,                                                         1, now(), 'admin', now(), 'admin'),
	(78, '66', 'รายได้-ส่วนลดเบี้ยประกัน', '', 2,                                                      1, now(), 'admin', now(), 'admin'),
	(79, '67', 'ค่าธรรมเนียมจัดการสินเชื่อเงินสด', '', 2,                                                  1, now(), 'admin', now(), 'admin'),
	(80, '74', 'เงินประกันความเสียหาย', '', 2,                                                       1, now(), 'admin', now(), 'admin'),
	(81, '76', 'ขายทรัพย์สินตามสัญญาเช่าแบบลีสซิ่ง', '', 2,                                               1, now(), 'admin', now(), 'admin'),
	(82, '77', 'ค่าธรรมเนียมธนาคาร', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(83, '78', 'ค่าบริการตัวแทนเก็บเงิน', '', 2,                                                       1, now(), 'admin', now(), 'admin'),
	(84, '83', 'เงินดาวน์', '', 2,                                                               1, now(), 'admin', now(), 'admin'),
	(85, '85', 'ค่าบริการประกันภัยไตรมาสที่ N/ปีพ.ศ.', '', 2,                                              1, now(), 'admin', now(), 'admin'),
	(86, '86', 'รายได้-ส่วนลดเบี้ยประกันภัย', '', 2,                                                     1, now(), 'admin', now(), 'admin'),
	(87, '88', 'โปรดคีย์รายละเอียดค่าเช่าที่มีภาษีมูลค่าเพิ่ม', '', 2,                                            1, now(), 'admin', now(), 'admin'),
	(88, '94', 'เงินประกันความเสียหาย', '', 2,                                                       1, now(), 'admin', now(), 'admin'),
	(89, '95', 'เงินประกันทรัพย์สินฝากขาย', '', 2,                                                     1, now(), 'admin', now(), 'admin'),
	(90, '96', 'รับคืนเงินทดรองจ่าย', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(91, '97', 'รับไว้เพื่อหาเลขสัญญา', '', 2,                                                       1, now(), 'admin', now(), 'admin'),
	(92, 'AS', 'ทรัพย์สินรอการขาย', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(93, 'BD', 'ตัดหนี้สูญ', '', 2,                                                              1, now(), 'admin', now(), 'admin'),
	(94, 'CA', 'สินเชื่อเงินสด', '', 2,                                                            1, now(), 'admin', now(), 'admin'),
	(95, 'CC', 'ปิดบัญชีงวดสุดท้าย', '', 2,                                                        1, now(), 'admin', now(), 'admin'),
	(96, 'FL', 'ปิดบัญชี', '', 2,                                                              1, now(), 'admin', now(), 'admin'),
	(97, 'XX', 'ปิดบัญชี', '', 2,                                                              1, now(), 'admin', now(), 'admin'),
	(98, 'RR', '', '', 2,                                                                 1, now(), 'admin', now(), 'admin'),
	(99, 'บบ', '', '', 2,                                                                   1, now(), 'admin', now(), 'admin');
select setval('tu_lock_split_type_loc_spl_typ_id_seq', 200);
ALTER TABLE tu_lock_split_type ENABLE TRIGGER all;