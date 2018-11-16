﻿ALTER TABLE tu_lock_split_type DISABLE TRIGGER all;
delete from tu_lock_split_type;
INSERT INTO tu_lock_split_type(loc_spl_typ_id,loc_spl_typ_code,loc_spl_typ_desc,loc_spl_typ_desc_en,sort_index,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	(1, '01', 'รับชำระตามสัญญา', 'Installment (Receive payments according to contract)', 1, 1, now(), 'admin', now(), 'admin'),
	(2, '05', 'ปิดบัญชีก่อนกำหนด', 'Early account closure', 1, 1, now(), 'admin', now(), 'admin'),
	(3, '02', 'ค่าปรับล่าช้า', 'Delay penalty', 1, 1, now(), 'admin', now(), 'admin'),
	(4, '73', 'เงินดาวน์', 'Down payment ', 1, 1, now(), 'admin', now(), 'admin'),
	(5, '09', 'ค่างวด,ส่วนลด', 'Installment, discount ', 1, 1, now(), 'admin', now(), 'admin'),
	(6, '82', 'ค่าส่งเสริมการขาย', 'Sales promotion fee', 1, 1, now(), 'admin', now(), 'admin'),
	(7, '79', 'ค่าธรรมเนียมการทำสัญญา', 'Contract fee', 1, 1, now(), 'admin', now(), 'admin'),
	(8, '71', 'ค่าติดตาม', 'Follow-up fee', 1, 1, now(), 'admin', now(), 'admin'),
	(9, '63', 'ค่ายกเลิกสัญญา', 'Contract termination fee', 1, 1, now(), 'admin', now(), 'admin'),
	(10, '65', 'ค่าตรวจสอบ', 'Inspection fee', 1, 1, now(), 'admin', now(), 'admin'),
	(11, '72', 'รับชำระค่าเสียหายทรัพย์สินยึดคืน', 'Receive payment from damages of confiscated assets', 1, 1, now(), 'admin', now(), 'admin'),
	(12, '68', 'ค่าบริการเก็บรักษาเล่มทะเบียน', 'Fee of keeping car registration book ', 1, 1, now(), 'admin', now(), 'admin'),
	(13, '20', 'เงินประกันการโอนทะเบียน', 'Deposit of register transfer', 1, 1, now(), 'admin', now(), 'admin'),
	(14, '21', 'ค่ามัดจำทะเบียน', 'Car registration deposit', 1, 1, now(), 'admin', now(), 'admin'),
	(15, '23', 'ค่ามัดจำทะเบียนป้ายเหลือง', 'Deposit of yellow license plate registration', 1, 1, now(), 'admin', now(), 'admin'),
	(16, '29', 'ค่าจัดส่งงานทะเบียน', 'Delivery charge to registration', 1, 1, now(), 'admin', now(), 'admin'),
	(17, '31', 'ค่าต่อภาษี', 'Tax extension fee', 1, 1, now(), 'admin', now(), 'admin'),
	(18, '32', 'ค่าต่อภาษีและค่าปรับ', 'Tax extension fee and penalty', 1, 1, now(), 'admin', now(), 'admin'),
	(19, '33', 'ค่าคัดวงกลม', 'Fee of request for new taxation sign', 1, 1, now(), 'admin', now(), 'admin'),
	(20, '34', 'ค่าคัดป้ายเหล็ก', 'Fee of request for new license plate', 1, 1, now(), 'admin', now(), 'admin'),
	(21, '35', 'ค่าเปลี่ยนสี', 'Color change expense', 1, 1, now(), 'admin', now(), 'admin'),
	(22, '37', 'ค่าแจ้งย้าย', 'Relocation fee', 1, 1, now(), 'admin', now(), 'admin'),
	(23, '38', 'ค่าโอนทะเบียน 1 ต่อ', 'Fee of car registration transfer( 1 time)', 1, 1, now(), 'admin', now(), 'admin'),
	(24, '39', 'ค่าโอนทะเบียน 2 ต่อ', 'Fee of car registration transfer( 2 times)', 1, 1, now(), 'admin', now(), 'admin'),
	(25, '40', 'ค่าปรับโอนล่าช้า', 'Delay penalty of transfer', 1, 1, now(), 'admin', now(), 'admin'),
	(26, '41', 'ค่าแจ้งความ', 'Police notification fee', 1, 1, now(), 'admin', now(), 'admin'),
	(27, '42', 'ค่าขอใช้ฯ', 'Fee of use request ', 1, 1, now(), 'admin', now(), 'admin'),
	(28, '44', 'ค่าต่อภาษี 2 ปี และค่าปรับ ค่าตรวจสภาพรถ', '2 years tax extension fee, penalty, car check fee', 1, 1, now(), 'admin', now(), 'admin'),
	(29, '45', 'ค่าปรับใบสั่ง', 'Ticket penalty', 1, 1, now(), 'admin', now(), 'admin'),
	(30, '46', 'ค่าสมุดคู่มือจดทะเบียน', 'Car registration book fee', 1, 1, now(), 'admin', now(), 'admin'),
	(31, '47', 'ค่าโอนทะเบียนเข้าบริษัท', 'Fee of transferring register to company', 1, 1, now(), 'admin', now(), 'admin'),
	(32, '48', 'ค่าแจ้งเปลี่ยนผู้ครอบครอง', 'Fee of changing owner ', 1, 1, now(), 'admin', now(), 'admin'),
	(33, '49', 'แก้ไขทะเบียนที่จดผิด', 'Edit wrong registration', 1, 1, now(), 'admin', now(), 'admin'),
	(34, '53', 'ค่าเปลี่ยนชื่อ-สกุล', 'Name-lastname change fee', 1, 1, now(), 'admin', now(), 'admin'),
	(35, '55', 'ค่าเปลี่ยนแปลงที่อยู่', 'Address change fee', 1, 1, now(), 'admin', now(), 'admin'),
	(36, '56', 'ค่าระงับการแจ้งย้าย', 'Fee of relocation suspension', 1, 1, now(), 'admin', now(), 'admin'),
	(37, '57', 'ค่าเปลี่ยนประเภทรถ', 'Fee of changing car type', 1, 1, now(), 'admin', now(), 'admin'),
	(38, '60', 'ค่าตรวจสภาพรถ (รถใหม่)', 'Car check fee ( new car)', 1, 1, now(), 'admin', now(), 'admin'),
	(39, '28', 'ค่าปรับปรุงใบคู่มือจดทะเบียน', 'Fee of updating car registration book ', 1, 1, now(), 'admin', now(), 'admin'),
	(40, '04', 'พักรับไว้เพื่อนำส่งบริษัทประกันฯ', 'Suspend for sending to insurance company', 1, 1, now(), 'admin', now(), 'admin'),
	(41, '06', 'รับค่าเบี้ยประกันและภาษีอากรเพื่อนำส่งบริษัทประกัน', 'Receive insurance premium and tax to send to insurance company', 1, 1, now(), 'admin', now(), 'admin'),
	(42, '75', 'ค่าบริการในการดำเนินการ', 'Operation fee', 1, 1, now(), 'admin', now(), 'admin'),
	(43, '70', 'ขายทรัพย์สินรอการขาย', 'Sell assets pending for sale', 1, 1, now(), 'admin', now(), 'admin'),
	(44, '03', 'ค่าปรับ เช็คคืน/หักบัญชีไม่ผ่าน', 'Penalty of returned cheque/ unsuccessful account debiting', 1, 1, now(), 'admin', now(), 'admin'),
	(45, '87', 'โปรดคีย์รายละเอียดการรับค่าบริการ', 'Please key detail of receiving service charge', 1, 1, now(), 'admin', now(), 'admin'),
	(46, '84', 'เงินชดเชยกรณีรถยึด', 'Compensation in case of car confiscation', 1, 1, now(), 'admin', now(), 'admin'),
	(47, '89', 'รับคืนค่าเบี้ยประกันรถจยย', 'Receive bike insurance premium ', 1, 1, now(), 'admin', now(), 'admin'),
	(48, '91', 'รับคืนภ.พ.ค้างชำระ', 'Receive unpaid VAT ', 1, 1, now(), 'admin', now(), 'admin'),
	(49, '07', 'โปรดคีย์ รายละเอียดการรับ ที่รวมภาษีมูลค่าเพิ่ม', 'Please key receipt detail which includes value added tax', 1, 1, now(), 'admin', now(), 'admin'),
	(50, '69', 'โปรดคีย์ รายละเอียดการรับ ที่ไม่มีภาษีมูลค่าเพิ่ม', 'Please key receipt detail which doesn’t include value added tax', 1, 1, now(), 'admin', now(), 'admin'),
	(51, '98', 'รับไว้เพื่อตัดบัญชี', 'Receive to  clear account', 1, 1, now(), 'admin', now(), 'admin'),
	(52, '99', 'รับไว้เพื่อรอจ่ายคืน', 'Receive for pending to pay ', 1, 1, now(), 'admin', now(), 'admin'),
	(53, '80', 'รับเงินค่าธรรมเนียมศาลคืน', 'Receive court fee ', 1, 1, now(), 'admin', now(), 'admin'),
	(54, '81', 'รับชำระหนี้ตามคำพิพากษา', 'Receive payments according to court judgment', 1, 1, now(), 'admin', now(), 'admin');
select setval('tu_lock_split_type_loc_spl_typ_id_seq', 200);
ALTER TABLE tu_lock_split_type ENABLE TRIGGER all;