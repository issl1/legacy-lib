DROP TABLE if exists td_company_contact_info;
--td_company_contact_info
CREATE TABLE td_company_contact_info
(
  ind_cnt_inf_id bigserial NOT NULL,
  dt_cre timestamp without time zone NOT NULL,
  usr_cre character varying(30) NOT NULL,
  sta_rec_id bigint,
  dt_upd timestamp without time zone NOT NULL,
  usr_upd character varying(30) NOT NULL,
  com_id bigint NOT NULL,
  cnt_inf_id bigint NOT NULL,
  CONSTRAINT td_company_contact_info_pkey PRIMARY KEY (ind_cnt_inf_id),
  CONSTRAINT fkdt5prfr98v640poubx3bupaya FOREIGN KEY (com_id)
      REFERENCES td_company (com_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkspg5wgpnecpcswtyalp8xjm1g FOREIGN KEY (cnt_inf_id)
      REFERENCES td_contact_info (cnt_inf_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE td_company_contact_info
  OWNER TO efinance;
CREATE TABLE tu_incident_location
(
  inc_loc_id bigserial NOT NULL,
  dt_cre timestamp without time zone NOT NULL,
  usr_cre character varying(30) NOT NULL,
  sta_rec_id bigint,
  dt_upd timestamp without time zone NOT NULL,
  usr_upd character varying(30) NOT NULL,
  sort_index integer,
  inc_loc_code character varying(10),
  inc_loc_desc character varying(50),
  inc_loc_desc_en character varying(50),
  pro_id bigint,
  CONSTRAINT tu_incident_location_pkey PRIMARY KEY (inc_loc_id),
  CONSTRAINT fkr7gpqmptbucm2w07fmt68kifq FOREIGN KEY (pro_id)
      REFERENCES tu_province (pro_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tu_incident_location
  OWNER TO efinance;
  
  ALTER TABLE tu_lock_split_type DISABLE TRIGGER all;
delete from tu_lock_split_type;
INSERT INTO tu_lock_split_type(loc_spl_typ_id,loc_spl_typ_code,loc_spl_typ_desc,loc_spl_typ_desc_en,sort_index,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES
	(1, '01', 'รับชำระตามสัญญา', 'Payment Contract',                                    1, 1, now(), 'admin', now(), 'admin'),
	(2, '05', 'ปิดบัญชีก่อนกำหนด', 'Closing Early',                                    1, 1, now(), 'admin', now(), 'admin'),
	(3, '02', 'ค่าปรับล่าช้า', 'Delay Fines',                                    1, 1, now(), 'admin', now(), 'admin'),
	(4, '73', 'เงินดาวน์', 'down payment',                                    1, 1, now(), 'admin', now(), 'admin'),
	(5, '09', 'ค่างวด,ส่วนลด', 'Annuities, discounts',                                    1, 1, now(), 'admin', now(), 'admin'),
	(6, '82', 'ค่าส่งเสริมการขาย', 'Promotional value',                                    1, 1, now(), 'admin', now(), 'admin'),
	(7, '79', 'ค่าธรรมเนียมการทำสัญญา', 'Fee contract',                                    1, 1, now(), 'admin', now(), 'admin'),
	(8, '71', 'ค่าติดตาม', 'Follow up',                                    1, 1, now(), 'admin', now(), 'admin'),
	(9, '63', 'ค่ายกเลิกสัญญา', 'The contract cancellation',                                    1, 1, now(), 'admin', now(), 'admin'),
	(10, '65', 'ค่าตรวจสอบ', 'Check the',                                    1, 1, now(), 'admin', now(), 'admin'),
	(11, '72', 'รับชำระค่าเสียหายทรัพย์สินยึดคืน', 'Payment of compensation for property expropriation',                                    1, 1, now(), 'admin', now(), 'admin'),
	(12, '68', 'ค่าบริการเก็บรักษาเล่มทะเบียน', 'Fee shelf registration book',                                    1, 1, now(), 'admin', now(), 'admin'),
	(13, '20', 'เงินประกันการโอนทะเบียน', 'Insurance money transfer register',                                    1, 1, now(), 'admin', now(), 'admin'),
	(14, '21', 'ค่ามัดจำทะเบียน', 'Registration fee',                                    1, 1, now(), 'admin', now(), 'admin'),
	(15, '23', 'ค่ามัดจำทะเบียนป้ายเหลือง', 'Deposits up Yellow Label',                                    1, 1, now(), 'admin', now(), 'admin'),
	(16, '29', 'ค่าจัดส่งงานทะเบียน', 'Registration Fee',                                    1, 1, now(), 'admin', now(), 'admin'),
	(17, '31', 'ค่าต่อภาษี', 'The tax value',                                    1, 1, now(), 'admin', now(), 'admin'),
	(18, '32', 'ค่าต่อภาษีและค่าปรับ', 'The taxes and fines',                                    1, 1, now(), 'admin', now(), 'admin'),
	(19, '33', 'ค่าคัดวงกลม', 'Copy the circle',                                    1, 1, now(), 'admin', now(), 'admin'),
	(20, '34', 'ค่าคัดป้ายเหล็ก', 'Copy the steel plate',                                    1, 1, now(), 'admin', now(), 'admin'),
	(21, '35', 'ค่าเปลี่ยนสี', 'The color change',                                    1, 1, now(), 'admin', now(), 'admin'),
	(22, '37', 'ค่าแจ้งย้าย', 'The Relocation',                                    1, 1, now(), 'admin', now(), 'admin'),
	(23, '38', 'ค่าโอนทะเบียน 1 ต่อ', '1 registration fee per transfer',                                    1, 1, now(), 'admin', now(), 'admin'),
	(24, '39', 'ค่าโอนทะเบียน 2 ต่อ', 'The transfer of registration per second',                                    1, 1, now(), 'admin', now(), 'admin'),
	(25, '40', 'ค่าปรับโอนล่าช้า', 'Fines transfer delay',                                    1, 1, now(), 'admin', now(), 'admin'),
	(26, '41', 'ค่าแจ้งความ', 'The notification',                                    1, 1, now(), 'admin', now(), 'admin'),
	(27, '42', 'ค่าขอใช้ฯ', 'The request states',                                    1, 1, now(), 'admin', now(), 'admin'),
	(28, '44', 'ค่าต่อภาษี 2 ปี และค่าปรับ ค่าตรวจสภาพรถ', 'On the tax two years and fines, vehicle inspectors.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(29, '45', 'ค่าปรับใบสั่ง', 'Orders Fines',                                    1, 1, now(), 'admin', now(), 'admin'),
	(30, '46', 'ค่าสมุดคู่มือจดทะเบียน', 'The booklets listed',                                    1, 1, now(), 'admin', now(), 'admin'),
	(31, '47', 'ค่าโอนทะเบียนเข้าบริษัท', 'Transfer to company registration',                                    1, 1, now(), 'admin', now(), 'admin'),
	(32, '48', 'ค่าแจ้งเปลี่ยนผู้ครอบครอง', 'Changing the occupant',                                    1, 1, now(), 'admin', now(), 'admin'),
	(33, '49', 'แก้ไขทะเบียนที่จดผิด', 'Fix up the wrong note',                                    1, 1, now(), 'admin', now(), 'admin'),
	(34, '53', 'ค่าเปลี่ยนชื่อ-สกุล', 'Change the Name -',                                    1, 1, now(), 'admin', now(), 'admin'),
	(35, '55', 'ค่าเปลี่ยนแปลงที่อยู่', 'The address change',                                    1, 1, now(), 'admin', now(), 'admin'),
	(36, '56', 'ค่าระงับการแจ้งย้าย', 'The suspension of moves',                                    1, 1, now(), 'admin', now(), 'admin'),
	(37, '57', 'ค่าเปลี่ยนประเภทรถ', 'Change the type of car',                                    1, 1, now(), 'admin', now(), 'admin'),
	(38, '60', 'ค่าตรวจสภาพรถ (รถใหม่)', 'Check the condition of the car (new cars).',                                    1, 1, now(), 'admin', now(), 'admin'),
	(39, '28', 'ค่าปรับปรุงใบคู่มือจดทะเบียน', 'The updated Leaf registered guide',                                    1, 1, now(), 'admin', now(), 'admin'),
	(40, '04', 'พักรับไว้เพื่อนำส่งบริษัทประกันฯ', 'Rooms are to be submitted to the insurance company.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(41, '06', 'รับค่าเบี้ยประกันและภาษีอากรเพื่อนำส่งบริษัทประกัน', 'Get insurance premiums and taxes to be remitted insurers.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(42, '75', 'ค่าบริการในการดำเนินการ', 'Service in Action',                                    1, 1, now(), 'admin', now(), 'admin'),
	(43, '70', 'ขายทรัพย์สินรอการขาย', 'Sales of foreclosed properties',                                    1, 1, now(), 'admin', now(), 'admin'),
	(44, '03', 'ค่าปรับ เช็คคืน/หักบัญชีไม่ผ่าน', 'Fines returned check / debit verification.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(45, '87', 'โปรดคีย์รายละเอียดการรับค่าบริการ', 'Please key details be charged.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(46, '84', 'เงินชดเชยกรณีรถยึด', 'Car based compensation case',                                    1, 1, now(), 'admin', now(), 'admin'),
	(47, '89', 'รับคืนค่าเบี้ยประกันรถจยย', 'Return the car insurance premium motorcycle',                                    1, 1, now(), 'admin', now(), 'admin'),
	(48, '91', 'รับคืนภ.พ.ค้างชำระ', 'Returns Tr. May. Arrears.',                                    1, 1, now(), 'admin', now(), 'admin'),
	(49, '07', 'โปรดคีย์ รายละเอียดการรับ ที่รวมภาษีมูลค่าเพิ่ม', 'Please get key details VAT',                                    1, 1, now(), 'admin', now(), 'admin'),
	(50, '69', 'โปรดคีย์ รายละเอียดการรับ ที่ไม่มีภาษีมูลค่าเพิ่ม', 'Please get key details Without VAT',                                    1, 1, now(), 'admin', now(), 'admin'),
	(51, '98', 'รับไว้เพื่อตัดบัญชี', 'Welcome to deferred',                                    1, 1, now(), 'admin', now(), 'admin'),
	(52, '99', 'รับไว้เพื่อรอจ่ายคืน', 'Get paid to wait',                                    1, 1, now(), 'admin', now(), 'admin'),
	(53, '80', 'รับเงินค่าธรรมเนียมศาลคืน', 'Fees paid nights',                                    1, 1, now(), 'admin', now(), 'admin'),
	(54, '81', 'รับชำระหนี้ตามคำพิพากษา', 'The judgment debt',                                    1, 1, now(), 'admin', now(), 'admin');
select setval('tu_lock_split_type_loc_spl_typ_id_seq', 200);
ALTER TABLE tu_lock_split_type ENABLE TRIGGER all;

ALTER TABLE td_lock_split RENAME COLUMN loc_spl_dt_payment TO loc_spl_dt_from;
ALTER TABLE td_lock_split RENAME COLUMN loc_spl_dt_expiry TO loc_spl_dt_to;

INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(634, 'com.nokor.efinance.core.collection.model.EDealerCustomer',          'EDealerCustomer',             'EDealerCustomer',                                    'dealercustomer',       1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');

DELETE FROM ts_ref_data WHERE ref_dat_id = 1417;
DELETE FROM ts_ref_data WHERE ref_dat_id = 1418;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

    -- EDealerCustomer
    (634, 1,  'I',             'Individual',               'Individual',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (634, 2,  'C',             'Company',                  'Company',                 null, null, 1, 1, now(), 'admin', now(), 'admin');

delete from ts_ref_data where ref_dat_ide = 0;
delete from ts_ref_data where ref_tab_id = 193 and ref_dat_ide = 5;
delete from ts_ref_data where ref_tab_id = 632 and ref_dat_ide in (1,2,3,4);
delete from ts_ref_data where ref_tab_id = 604;
delete from ts_ref_data where ref_tab_id = 605;
  
  

  
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

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
-- EPaymentChannel
    (632, 1, '1', 'พนักงาน bill ไปเรียกเก้บเงินจากลูกค้าโดยตรง', 'Biller get money from customer',  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 2, '2', 'เค้าเตอร์ตัวแทนจำหน่าย', 'Dealer Counter',                                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 3, '3', 'เค้าเตอร์บริษัทกรุ๊ปลีส', 'GL Counter',                                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 4, '4', 'ธนาณัติ', 'Postal Money Order',                                      null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 5, '5', 'เก็บเงินจากทีมฝ่ายกฏหมาย', 'Money from the legal department',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 6, '6', '', '',                                                    null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 7, '7', 'เงินโอน', 'Transfer Money',                                          null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 8, '8', 'นำเข้าข้อมูลแก้ไขจากระบบ', 'Import to System with editor',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 9, '9', 'TR ยาว', 'Key in to system by Bill',                        null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 10, 'B', 'ธนาคาร', 'Bank',                                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 11, 'C', 'เค้าเตอร์เซอร์วิส', 'Counter Service',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 12, 'D', '***ยกเลิกไปแล้ว', '***Canceled',                                null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 13, 'L', 'เทสโก้โลตัส', 'Tesco Lotus',                                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 14, 'M', 'ทรูมันนี่', 'True Money',                                     null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 15, 'P', 'ไปรษณีย์ไทย', 'Thailand Post',                               null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 16, 'T', 'ชำระเงินจากการโทรติดตาม', 'Paid by call center',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	-- EBlackListSource
	(604, 1, '*T', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 2, '0', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 3, '00', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 4, '01', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 5, '02', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 6, '03', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 7, '04', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 8, '05', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 9, '06', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 10, '07', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 11, '08', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 12, '09', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 13, '0B', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 14, '0H', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 15, '1', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 16, '1-', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 17, '10', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 18, '11', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 19, '12', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 20, '13', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 21, '14', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 22, '15', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 23, '16', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 24, '17', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 25, '19', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 26, '1O', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 27, '1บ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 28, '2-', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 29, '20', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 30, '21', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 31, '22', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 32, '23', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 33, '24', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 34, '25', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 35, '26', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 36, '28', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 37, '29', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 38, '2H', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 39, '2ง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 40, '2บ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 41, '30', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 42, '31', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 43, '32', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 44, '33', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 45, '34', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 46, '35', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 47, '36', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 48, '37', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 49, '38', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 50, '39', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 51, '40', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 52, '41', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 53, '42', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 54, '43', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 55, '44', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 56, '45', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 57, '46', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 58, '47', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 59, '48', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 60, '49', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 61, '4ง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 62, '50', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 63, '51', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 64, '52', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 65, '53', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 66, '54', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 67, '55', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 68, '56', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 69, '57', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 70, '58', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 71, '59', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 72, '5ง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 73, '60', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 74, '61', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 75, '62', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 76, '63', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 77, '64', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 78, '65', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 79, '66', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 80, '67', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 81, '68', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 82, '69', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 83, '7-', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 84, '70', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 85, '71', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 86, '72', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 87, '73', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 88, '74', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 89, '75', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 90, '76', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 91, '77', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 92, '8', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 93, '8-', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 94, '80', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 95, '81', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 96, '83', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 97, '84', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 98, '85', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 99, '90', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 100, '91', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 101, '92', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 102, '93', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 103, '94', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 104, '97', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 105, '98', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 106, '99', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 107, 'AL', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 108, 'BC', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 109, 'BT', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 110, 'CB', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 111, 'CC', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 112, 'CK', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 113, 'CL', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 114, 'CM', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 115, 'CP', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 116, 'CT', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 117, 'CV', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 118, 'FS', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 119, 'FU', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 120, 'GE', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 121, 'GL', 'Group Lease of Blacklist', 'Group Lease of Blacklist', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 122, 'GN', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 123, 'HD', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 124, 'HW', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 125, 'IN', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 126, 'IT', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 127, 'KC', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 128, 'KP', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 129, 'LA', 'เกตุแรงเพชร', 'เกตุแรงเพชร', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 130, 'LW', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 131, 'MG', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 132, 'NB', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 133, 'NP', 'News paper', 'News paper', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 134, 'PC', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 135, 'PL', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 136, 'PN', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 137, 'PP', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 138, 'PS', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 139, 'R3', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 140, 'RS', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 141, 'SC', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 142, 'SF', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 143, 'SG', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 144, 'SR', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 145, 'ST', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 146, 'SU', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 147, 'SW', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 148, 'TB', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 149, 'TK', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 150, 'TL', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 151, 'TN', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 152, 'TV', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 153, 'Tk', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 154, 'WH', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 155, 'XX', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 156, 'cv', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 157, 'hw', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 158, 'pc', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 159, 'pp', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 160, 'q', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 161, 'sf', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 162, 'sw', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 163, 'tT', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 164, 'tb', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 165, 'tk', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 166, 'xx', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 167, 'กค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 168, 'กจ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 169, 'กท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 170, 'กธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 171, 'กน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 172, 'กบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 173, 'กพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 174, 'กม', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 175, 'กล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 176, 'กส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 177, 'กฬ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 178, 'ข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 179, 'ข*', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 180, 'ขก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 181, 'ขข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 182, 'ขง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 183, 'ขช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 184, 'ขฐ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 185, 'ขฒ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 186, 'ขด', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 187, 'ขต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 188, 'ขธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 189, 'ขน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 190, 'ขพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 191, 'ขภ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 192, 'ขม', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 193, 'ขย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 194, 'ขว', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 195, 'ขศ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 196, 'ขส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 197, 'ขห', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 198, 'ขฮ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 199, 'ฃ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 200, 'ฃง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 201, 'ฃธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 202, 'ฃล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 203, 'ค2', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 204, 'คก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 205, 'คค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 206, 'คน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 207, 'งก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 208, 'จ1', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 209, 'จ2', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 210, 'จ3', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 211, 'จ4', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 212, 'จ5', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 213, 'จ6', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 214, 'จ7', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 215, 'จข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 216, 'จท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 217, 'จบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 218, 'ฉช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 219, 'ฉท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 220, 'ชน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 221, 'ชบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 222, 'ชพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 223, 'ชภ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 224, 'ชม', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 225, 'ชร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 226, 'ชล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 227, 'ญธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 228, 'ดก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 229, 'ดน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 230, 'ดล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 231, 'ดส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 232, 'ตก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 233, 'ตง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 234, 'ตช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 235, 'ตด', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 236, 'ตต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 237, 'ตร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 238, 'ตล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 239, 'ท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 240, 'ทก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 241, 'ทพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 242, 'ทย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 243, 'ทร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 244, 'ทส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 245, 'ทห', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 246, 'ทอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 247, 'ธญ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 248, 'ธบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 249, 'น', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 250, 'นก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 251, 'นค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 252, 'นฐ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 253, 'นต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 254, 'นท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 255, 'นธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 256, 'นบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 257, 'นป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 258, 'นพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 259, 'นม', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 260, 'นย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 261, 'นร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 262, 'นล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 263, 'นว', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 264, 'นศ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 265, 'นส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 266, 'บง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 267, 'บญ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 268, 'บป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 269, 'บร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 270, 'ปข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 271, 'ปค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 272, 'ปจ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 273, 'ปช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 274, 'ปต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 275, 'ปท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 276, 'ปธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 277, 'ปน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 278, 'ปพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 279, 'ผค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 280, 'ผจ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 281, 'ผต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 282, 'ผท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 283, 'ผป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 284, 'ผร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 285, 'ผว', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 286, 'ผส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 287, 'ผอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 288, 'พ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 289, 'พ*', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 290, 'พ.', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 291, 'พก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 292, 'พข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 293, 'พฃ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 294, 'พค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 295, 'พง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 296, 'พจ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 297, 'พช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 298, 'พต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 299, 'พธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 300, 'พน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 301, 'พบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 302, 'พย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 303, 'พร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 304, 'พล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 305, 'พศ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 306, 'พอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 307, 'พี', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 308, 'ฟ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 309, 'ภก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 310, 'ภต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 311, 'ภอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 312, 'มข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 313, 'มฃ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 314, 'มช', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 315, 'มน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 316, 'มบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 317, 'มร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 318, 'มส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 319, 'ยก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 320, 'ยค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 321, 'ยธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 322, 'ยล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 323, 'ยส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 324, 'ยแ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 325, 'รก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 326, 'รง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 327, 'รน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 328, 'รบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 329, 'รย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 330, 'รร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 331, 'รอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 332, 'ล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 333, 'ล1', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 334, 'ล2', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 335, 'ล3', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 336, 'ล4', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 337, 'ล5', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 338, 'ล6', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 339, 'ล7', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 340, 'ล8', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 341, 'ล9', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 342, 'ลง', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 343, 'ลฎ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 344, 'ลน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 345, 'ลบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 346, 'ลป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 347, 'ลพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 348, 'ลฟ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 349, 'ลย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 350, 'วฏ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 351, 'วท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 352, 'วป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 353, 'ศ.', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 354, 'ศก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 355, 'ศญ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 356, 'ศย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 357, 'ศส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 358, 'ศอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 359, 'ส.', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 360, 'สข', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 361, 'สค', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 362, 'สธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 363, 'สน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 364, 'สบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 365, 'สป', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 366, 'สพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 367, 'สม', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 368, 'สร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 369, 'สล', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 370, 'สส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 371, 'สห', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 372, 'หก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 373, 'หส', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 374, 'อก', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 375, 'อญ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 376, 'อฐ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 377, 'อด', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 378, 'อต', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 379, 'อท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 380, 'อธ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 381, 'อน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 382, 'อบ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 383, 'อย', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 384, 'อร', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 385, 'ะา', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 386, 'เด', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 387, 'แน', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 388, 'แพ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 389, 'แอ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 390, 'ไท', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(604, 391, 'ๆ', '', '', null, null, 1, 1, now(), 'admin', now(), 'admin'),
	
	-- EBlackListReason
    (605, 1, '001', 'Reason 001',             'Reason 001',                129, null, 1, 1, now(), 'admin', now(), 'admin'),
    (605, 2, '002', 'Reason 002',             'Reason 002',                133, null, 1, 1, now(), 'admin', now(), 'admin');
    
 ALTER TABLE tu_payment_condition DISABLE TRIGGER all;
delete from tu_payment_condition;
INSERT INTO tu_payment_condition(
            pay_con_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, 
            pay_con_code, pay_con_nu_delay, pay_con_desc, pay_con_desc_en, 
            pay_con_bl_end_of_month, pay_met_id)
VALUES 
    (1, now(), 'admin', 1, now(), 'admin', 1, 'CASH', 0, 'Cash', 'Cash', false, 1),
    (2, now(), 'admin', 1, now(), 'admin', 1, 'LOSS', 0, 'Loss', 'Loss', false, 2),
    (3, now(), 'admin', 1, now(), 'admin', 1, 'CHEQUE', 0, 'Cheque', 'Cheque', false, 3),
    (4, now(), 'admin', 1, now(), 'admin', 1, 'TRANSFER', 0, 'Transfer', 'Transfer', false, 4),
	(5, now(), 'admin', 1, now(), 'admin', 1, 'UNKNOWN', 0, 'Unknown', 'Unknown', false, 5);
            
select setval('tu_payment_condition_pay_con_id_seq', 30);
ALTER TABLE tu_payment_condition ENABLE TRIGGER all;


update ts_ref_table 
set ref_tab_code = 'com.nokor.efinance.core.collection.model.EPaymentChannel',
ref_tab_desc = 'EPaymentChannel',
ref_tab_desc_en = 'EPaymentChannel',
ref_tab_shortname = 'paymentchannels'
where ref_tab_id = 632;

--td_contract
ALTER TABLE td_contract ALTER COLUMN fpd_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_id DROP NOT NULL;
--tu_org_bank_account
ALTER TABLE tu_org_bank_account ADD COLUMN ban_external_id bigint;
ALTER TABLE tu_org_bank_account ADD COLUMN ban_acc_holder_id bigint;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_id DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_holder DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_number DROP NOT NULL;
--tu_dealer
ALTER TABLE tu_dealer ADD COLUMN dea_cust bigint;
--td_cashflow
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_id DROP NOT NULL;
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
ALTER TABLE td_cashflow ADD COLUMN pay_cha_id bigint;
ALTER TABLE td_cashflow ADD COLUMN loc_spl_typ_id bigint;

ALTER TABLE tu_payment_method DISABLE TRIGGER all;
delete from tu_payment_method;  
INSERT INTO tu_payment_method (pay_met_id, pay_met_code, pay_met_desc, pay_met_desc_en, cat_pay_met_id, pay_met_auto_confirm, fin_srv_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EPaymentMethod
    (1, 'CASH', 'Cash', 'Cash', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),  
    (2, 'LOSS', 'Loss', 'Loss', 3, true, null,  1, 1, now(), 'admin', now(), 'admin'),
    (3, 'CHEQUE', 'Cheque', 'Cheque', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),
    (4, 'TRANSFER', 'Transfer', 'Transfer', 2, true, null,  1, 1, now(), 'admin', now(), 'admin'),
	(5, 'UNKNOWN', 'Unknown', 'Unknown', 2, true, null,  1, 1, now(), 'admin', now(), 'admin');
select setval('tu_payment_method_pay_met_id_seq', 50);
ALTER TABLE tu_payment_method ENABLE TRIGGER all;

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

delete from tu_org_address;
INSERT INTO tu_org_address(org_add_id,add_id,org_id,org_str_id,sta_rec_id,dt_cre,usr_cre,dt_upd,usr_upd)
VALUES(1, 9900, 3, null, 1, now(), 'admin', now(), 'admin'),
	(11, 10836, 11, null, 1, now(), 'admin', now(), 'admin'),
	(12, 10837, 11, null, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_address_org_add_id_seq', 13);

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
