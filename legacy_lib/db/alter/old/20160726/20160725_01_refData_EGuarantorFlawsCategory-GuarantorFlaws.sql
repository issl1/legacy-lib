﻿delete from ts_ref_data where ref_tab_id = 611;
delete from ts_ref_data where ref_tab_id = 612;
delete from ts_ref_data where ref_tab_id = 613;
delete from ts_ref_data where ref_tab_id = 614;
delete from ts_ref_data where ref_tab_id = 615;
delete from ts_ref_data where ref_tab_id = 616;

INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- EGuarantorFlawsCategory
    (611, 2,  '01',         'BlackList',     'BlackList',             null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (611, 1,  '02',         'NCB',           'NCB',                   null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (611, 3,  '03',         'Policy',        'Policy',                null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorFlaws
	(612, 1, 'B01',         'ลูกค้าแก็งฑ์',                    'ลูกค้าแก็งฑ์',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 2, 'B02',         'พฤติกรรมน่าสงสัย',                'พฤติกรรมน่าสงสัย',            1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 3, 'B03',         'ปลอมเปลงเอกสาร',                'ปลอมเปลงเอกสาร',            1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 4, 'B04',         'ข้อมูลเท็จ',                    'ข้อมูลเท็จ',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 5, 'B05',         'บริษัทฯที่ลูกค้าทำงานมีพฤติกรรมน่าสงสัย',      'บริษัทฯที่ลูกค้าทำงานมีพฤติกรรมน่าสงสัย', 1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 6, 'B06',         'Fraud อื่นๆ',                'Fraud อื่นๆ',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 7, 'B99',         'อื่นๆ',                       'อื่นๆ',                 1, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 8, 'N01',         'บุคคลล้มละลาย',                  'บุคคลล้มละลาย',            2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 9, 'N02',         'รถยึด',                      'รถยึด',                 2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 10, 'N03',        'ลูกค้าเก่าประวัติไม่ดี',                'ลูกค้าเก่าประวัติไม่ดี',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 11, 'N04',        'NCB=>50,001 บาท ',          'NCB=>50,001 บาท ',    2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 12, 'N05',        'NCB=20,001-50,000 บาท',    'NCB=20,001-50,000 บาท', 2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 13, 'N06',        'NCB=5,001-20,000 บาท',     'NCB=5,001-20,000 บาท',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 14, 'N07',        'NCB=<5,000 บาท',           'NCB=<5,000 บาท',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 15, 'N08',        'NCB',                    'NCB',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 16, 'N09',        'สินเชื่อจักรยานยนต์คันที่2ไม่ตามเกณฑ์',       'สินเชื่อจักรยานยนต์คันที่2ไม่ตามเกณฑ์',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 17, 'N10',        'ภาระหนี้เกินเกณฑ์',                 'ภาระหนี้เกินเกณฑ์',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 18, 'N11',        'มีรายการยื่นเรื่องขอสินเชื่อเช่าซื้อก่อนหน้า',      'มีรายการยื่นเรื่องขอสินเชื่อเช่าซื้อก่อนหน้า', 2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 19, 'NH99',       'อื่นๆ',                       'อื่นๆ',                  2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 20, 'P01',        'อาชีพ-ไม่ชัดเจน',                  'อาชีพ-ไม่ชัดเจน',             3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 21, 'P02',        'ที่พักอาศัย-ไม่ชัดเจน',                'ที่พักอาศัย-ไม่ชัดเจน',           3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 22, 'P03',        'เอกสารสำคัญ-ไม่ครบ/สมบูรณ์',            'เอกสารสำคัญ-ไม่ครบ/สมบูรณ์',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 23, 'P04',        'ซื้อแทน',                      'ซื้อแทน',                 3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 24, 'P05',        'เบอร์ซ้ำ-ที่พัก/เบอร์โทรที่เดียวกันกับลูกค้าGLมีปัญหา', 'เบอร์ซ้ำ-ที่พัก/เบอร์โทรที่เดียวกันกับลูกค้าGLมีปัญหา', 3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 25, 'P06',        'ไม่เข้าหลักเกณฑ์-ลูกค้าพม่า',             'ไม่เข้าหลักเกณฑ์-ลูกค้าพม่า',        3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 26, 'P07',        'ไม่เข้าหลักเกณฑ์-ลูกค้าต่างชาติ',            'ไม่เข้าหลักเกณฑ์-ลูกค้าต่างชาติ',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 27, 'P08',        'กลุ่มอาชีพ-ไม่ตามเกณฑ์',               'กลุ่มอาชีพ-ไม่ตามเกณฑ์',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 28, 'P09',        'เอกสารการเงิน-ไม่ล่าสุด',               'เอกสารการเงิน-ไม่ล่าสุด',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 29, 'P10',        'ไม่มี-ผู้ค้ำประกัน',                  'ไม่มี-ผู้ค้ำประกัน',              3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 30, 'P11',        'ไม่เข้า-เงื่อนไขพิเศษ',                'ไม่เข้า-เงื่อนไขพิเศษ',            3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 31, 'P12',        'ไม่อยู่ในพื้นที่ให้บริการขอบริษัทฯ',          'ไม่อยู่ในพื้นที่ให้บริการขอบริษัทฯ',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 32, 'P13',        'มีรายการยื่นเรื่องขอสินเชื่อเช่าซื้อก่อนหน้า',      'มีรายการยื่นเรื่องขอสินเชื่อเช่าซื้อก่อนหน้า',   3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 33, 'P14',        'รายได้-ไม่ตามเกณฑ์',                'รายได้-ไม่ตามเกณฑ์',            3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 34, 'P15',        'อายุงาน-ไม่ตามเกณฑ์',               'อายุงาน-ไม่ตามเกณฑ์',            3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 35, 'P16',        'อายุตัว-ไม่ตามเกณฑ์',                'อายุตัว-ไม่ตามเกณฑ์',            3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 36, 'P17',        'โทรศัพท์-ไม่ตามเกณฑ์',               'โทรศัพท์-ไม่ตามเกณฑ์',           3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 37, 'P18',        'ลูกค้าเก่า-ไม่เข้าหลักเกณฑ์',             'ลูกค้าเก่า-ไม่เข้าหลักเกณฑ์',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 38, 'P19',        'ระยะที่พักอาศัย-ไม่ตามเกณฑ์',           'ระยะที่พักอาศัย-ไม่ตามเกณฑ์',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 39, 'P20',        'ผู้ค้ำประกัน-ไม่เข้าหลักเกณฑ์',            'ผู้ค้ำประกัน-ไม่เข้าหลักเกณฑ์',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 40, 'P21',        'บริษัทที่ลูกค้าทำงานขาดส่งงบดุลมากกว่า3ปี',      'บริษัทที่ลูกค้าทำงานขาดส่งงบดุลมากกว่า3ปี',  3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 41, 'P22',        'ยอดจัดเกินกว่าตารางยอดจัด',             'ยอดจัดเกินกว่าตารางยอดจัด',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 42, 'P23',        'รถจักรยานยนต์รุ่นนอกตาราง',            'รถจักรยานยนต์รุ่นนอกตาราง',         3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 43, 'P24',        'ดอกเบี้ยขั้นต่ำไม่ตามเกณฑ์',             'ดอกเบี้ยขั้นต่ำไม่ตามเกณฑ์',          3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 44, 'P25',        'จำนวนงวดไม่ตามเกณฑ์',              'จำนวนงวดไม่ตามเกณฑ์',            3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 45, 'P26',        'ไม่มี-ผู้อ้างอิง',                   'ไม่มี-ผู้อ้างอิง',                 3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(612, 46, 'P99',        'อื่นๆ',                       'อื่นๆ',                     3, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- ERequestsCategory
    (613, 1,  'MOREDOCUMENTS',  'ขอเอกสารเพิ่มเติม',                 'More Documents',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (613, 2,  'VERIFICATIONS',  'Phone Verifications',     'Phone Verifications',    null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (613, 3,  'CHANGECONTRACT', 'Field Verifications',     'Field Verifications',    null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- ELesseeRequests
    (614, 1,  'RDL001',         'ใบสมัคร',                             'Application',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 2,  'RDL002',         'หนังสือให้ความยินยอม',                      'NCB Consent',                       1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 3,  'RDL003',         'บัตรประชาชน',                          'ID card',                           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 4,  'RDL004',         'ทะเบียนบ้าน',                           'House registration',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 5,  'RDL005',         'สลิปเดือนล่าสุด',                          'Latest salary slip',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 6,  'RDL006',         'หนังสือรับรองรายได้',                       'Income certificate',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 7,  'RDL007',         'บัญชีเงินเดือน 3 เดือนล่าสุด',                    '3-month-salary statement',          1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 8,  'RDL008',         'ใบหักภาษี ณ. ที่จ่าย',                       'Withholding tax slip',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 9,  'RDL009',         'ใบขับขี่สาธารณะ',                         'Public riding license',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 10, 'RDL010',         'ทะเบียนรถที่ใช้วิ่งรับจ้าง',                      'Registration no',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 11, 'RDL011',         'แบบฟอร์มการตรวจสอบเอกสาร',                  'Inspectation form',                 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 12, 'RDL012',         'ขอมารดา/บิดามาร่วมค้ำกรณีอายุไม่ถึง20 ปี',             'Guadian-coguaranty form',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 13, 'RDL999',         'อื่นๆระบุ…………………',                     'Others: Key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 14, 'RVL001',         'รอติดต่อใหม่',                          'Recontact',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 15, 'RVL002',         'รอติดต่อที่ทำงานใหม่',                       'Recontact to working place',        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 16, 'RVL003',         'ขอเบอร์โทรมือถือใหม่',                       'Request new mobile number',         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 17, 'RVL004',         'ขอเบอร์โทรที่ทำงานใหม่',                      'Request new working place number',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 18, 'RVL005',         'ขอที่อยู่บ้านคนใช้รถในกรณีซื้อแทน',                 'Request rider''s address',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 19, 'RVL006',         'ขอรายละเอียดที่ส่งเอกสาร',                      'Request mail address',              2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 20, 'RVL007',         'ขอเบอร์หัวหน้างาน-หน่วยงาน',                    'Request boss/department info',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 21, 'RVL008',         'Field Check: ขอตรวจสอบบ้าน',            'Field Check: House',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 22, 'RVL009',         'Field Check: ขอตรวจสอบที่ทำงาน',           'Field Check: Work place',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 23, 'RVL999',         'อื่นๆระบุ…………………',                       'Others: Key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 24, 'RCL001',         'Reduce Finance Amount',            'Reduce Finance Amount',             3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 25, 'RCL002',         'Pay 1 Installment in advance',     'Pay 1 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 26, 'RCL003',         'Pay 2 Installment in advance',     'Pay 2 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 27, 'RCL004',         'Pay 3 Installment in advance',     'Pay 3 Installment in advance',      3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 28, 'RCL005',         'เปลี่ยนแปลงผู้ค้ำประกัน',                        'Replace Guarantor',                 3, null, 1, 1, now(), 'admin', now(), 'admin'),
    (614, 29, 'RCL006',         'เพิ่มผู้ค้ำ',                               'Add a Guarantor',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(614, 30, 'RCL007',         'ขอตรวจสอบอาชีพ',                           'ขอตรวจสอบอาชีพ',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(614, 31, 'RCL008',         'ขอใบแก้ไขรุ่นรถ ยอดจัด ค่างวดใหม่',                  'ขอใบแก้ไขรุ่นรถ ยอดจัด ค่างวดใหม่',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(614, 32, 'RCL009',         'ขอแผนที่บ้านคนใช้รถในกรณีซื้อแทน',                 'ขอแผนที่บ้านคนใช้รถในกรณีซื้อแทน',                   3, null, 1, 1, now(), 'admin', now(), 'admin'),
	(614, 33, 'RCL999',         'อื่นๆระบุ…………………',                      'Other: Key In',                     3, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorRequestsCategory
    (615, 1,  'MOREDOCUMENTS',   'ขอเอกสารเพิ่มเติม',                 'More Documents',         null, null, 1, 1, now(), 'admin', now(), 'admin'),
    (615, 2,  'VERIFICATIONS',   'Phone Verifications',     'Phone Verifications',          null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(615, 3,  'CHANGECONTRACT',  'Field Verifications',     'Field Verifications',          null, null, 1, 1, now(), 'admin', now(), 'admin'),

    -- EGuarantorRequests
    (616, 1,  'RDG001',         'ใบสมัคร',                        'Application',                      1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 2,  'RDG002',         'บัตรประชาชน',                     'ID card',                           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 3,  'RDG003',         'ทะเบียนบ้าน',                      'House registration',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 4,  'RDG004',         'สลิปเดือนล่าสุด',                     'Latest salary slip',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 5,  'RDG005',         'หนังสือรับรองรายได้',                   'Income certificate',                1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 6,  'RDG006',         'บัญชีเงินเดือน 3 เดือนล่าสุด',                '3-month-salary statement',          1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 7,  'RDG007',         'ใบหักภาษี ณ. ที่จ่าย',                   'Withholding tax slip',              1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 8,  'RDG008',         'ใบขับขี่สาธารณะ',                     'Public riding license',             1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 9,  'RDG009',         'ทะเบียนรถที่ใช้วิ่งรับจ้าง',                  'Registration no',                   1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 10, 'RDG010',         'แบบฟอร์มการตรวจสอบเอกสาร',              'Inspectation form',                 1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 11, 'RDG011',         'ขอมารดา/บิดามาร่วมค้ำกรณีอายุไม่ถึง20 ปี',         'Guadian-coguaranty form',           1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 12, 'RDG999',         'อื่นๆระบุ…………………',                 'Others: Key in',                    1, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 13, 'RVG001',         'รอติดต่อใหม่',                       'Recontact',                         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 14, 'RVG002',         'รอติดต่อที่ทำงานใหม่',                   'Recontact to working place',        2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 15, 'RVG003',         'ขอเบอร์โทรมือถือใหม่',                   'Request new mobile number',         2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 16, 'RVG004',         'ขอเบอร์โทรที่ทำงานใหม่',                  'Request new working place number',  2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 17, 'RVG005',         'ขอที่อยู่บ้านคนใช้รถในกรณีซื้อแทน',             'Request rider''s address',          2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 18, 'RVG006',         'ขอรายละเอียดที่ส่งเอกสาร',                 'Request mail address',              2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 19, 'RVG007',         'ขอเบอร์หัวหน้างาน-หน่วยงาน',                'Request boss/department info',      2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 20, 'RVG008',         'Field Check: ขอตรวจสอบที่อยู่',        'Field Check: House',                2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 21, 'RVG009',         'Field Check: ขอตรวจสอบที่ทำงาน',      'Field Check: Work place',           2, null, 1, 1, now(), 'admin', now(), 'admin'),
    (616, 22, 'RVG999',         'อื่นๆระบุ…………………',                  'Others: Key in',                    2, null, 1, 1, now(), 'admin', now(), 'admin'),
	(616, 23, 'RCG001',         'ขอตรวจสอบอาชีพ',                     'ขอตรวจสอบอาชีพ',                           3, null, 1, 1, now(), 'admin', now(), 'admin');