﻿INSERT INTO ts_ref_table (ref_tab_id, ref_tab_code, ref_tab_desc, ref_tab_desc_en, 
                                                                                                                                                                    ref_tab_shortname, ref_typ_id, ref_tab_fetch_values_from_db, 
                                                                                                                                                                                                            ref_tab_visible, ref_tab_readonly, ref_tab_use_sort_index, 
                                                                                                                                                                                                                                        ref_tab_fetch_i18n_from_db, ref_tab_cached, ref_tab_generate_code, 
                                                                                                                                                                                                                                                                sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(633, 'com.nokor.efinance.core.collection.model.EDealerCustomer',          'EDealerCustomer',             'EDealerCustomer',                                    'dealercustomer',       1, true,        true,  true,  false,        true, true, false,      1, now(), 'admin', now(), 'admin');



INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- ESubTypeOrganization	
	(191, 6, 'DEALER_AGENT', 'Dealer agents', 'Dealer agents',		        3, null, 1, 1, now(), 'admin', now(), 'admin'),

	--ETypeIdNumber
	(230, 50, '50', 'ใบทะเบียนพาณิชย์', 'ใบทะเบียนพาณิชย์', 								null, null, 1, 1, now(), 'admin', now(), 'admin'),

	-- ETypeOrganization
	(190, 5, 'DATAMI', 'Data Migration', 'Data Migration',						null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EDealerCustomer
    (633, 1,  'I',             'Individual',               'Individual',              null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (633, 2,  'C',             'Company',                  'Company',                 null, null, 1, 1, now(), 'admin', now(), 'admin'),
	
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