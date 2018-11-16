delete from ts_ref_data where ref_tab_id = 632;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES

    -- EPaymentChannel
    (632, 1, '1', 'พนักงาน bill ไปเก็บเงินจากลูกค้าโดยตรง', 'Biller get money from customer', null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 2, '2', 'เค้าเตอร์ตัวแทนจำหน่าย', 'Dealer Counter',                             null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 3, '3', 'เค้าเตอร์บริษัทกรุ๊ปลีส', 'GL Counter',                                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 4, '4', 'ธนาณัติ', 'Postal Money Order',                                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 5, '5', 'รับเงินผ่านทางทีมเร่งรัดหนี้สิน', 'Money from the Credit Collection',       null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 6, '6', 'รับเงินผ่านทางกฏหมายบังคับคดี', 'Money from the Legal Execution',        null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 7, '7', 'เงินโอน', 'Transfer Money',                                      null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 8, '8', 'นำเข้าข้อมูลแก้ไขจากระบบ', 'Import to System with editor',             null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 9, '9', 'TR ยาว', 'Key in to system by Bill',                    null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 10, 'B', 'ธนาคารกรุงศรีอยุธยา', 'Ayutthaya Bank',                       null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 11, 'B', 'ธนาคารกสิกรไทย', 'Kasikorn Bank',                          null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 12, 'B', 'ธนาคารไทยพาณิชย์', 'Siam commercial Bank',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 13, 'B', 'ธนาคารธนชาต', 'Thanachart Bank',                          null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 14, 'B', 'ธนาคารออมสิน', 'Government Savings Bank',                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 15, 'B', 'ธนาคารเพื่อการเกษตรและสหกรณ์การเกษตร', 'Bank for Agriculture and Agricultural Cooperatives',   null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 16, 'C', 'บริษัท เคาน์เตอร์เซอร์วิส จำกัด', 'Counter Service',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 17, 'D', '***ยกเลิกไปแล้ว', '***Canceled',                             null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 18, 'L', 'เทสโก้โลตัส', 'Tesco Lotus',                               null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 19, 'M', 'ทรูมันนี่', 'True Money',                                  null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 20, 'P', 'ไปรษณีย์ไทย', 'Thailand Post',                            null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(632, 21, 'T', 'ชำระเงินจากการโทรติดตาม', 'Paid by call center',              null, null, 1, 2, now(), 'admin', now(), 'admin'),
	(632, 22, 'BAY', 'BAY', 'BAY',              						  null, null, 1, 2, now(), 'admin', now(), 'admin');
	