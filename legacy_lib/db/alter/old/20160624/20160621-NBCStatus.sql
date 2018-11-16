INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
 
    (236, 'NBC010', 'ปกติ', 'Normal', 																								1, 1, now(), 'admin', now(), 'admin'),
    (237, 'NBC011', 'ปิดบัญชี', 'Closed account', 																					1, 1, now(), 'admin', now(), 'admin'),
    (238, 'NBC012', 'พักชำระหนี้ ตามนโยบายของรัฐบาล', 'Debt Moratorium as per government policy', 									1, 1, now(), 'admin', now(), 'admin'),
    (239, 'NBC020', 'หนี้ค้างชำระเกิน 90 วัน', 'Past due over 90 days', 															1, 1, now(), 'admin', now(), 'admin'),
    (240, 'NBC031', 'อยู่หว่างชำระหนี้ตามคำพิพากษาตามยินยอม', 'Under the processs of payment as agreed upon in the courts of law.', 1, 1, now(), 'admin', now(), 'admin'),
    (241, 'NBC032', 'ศาสพิพากษาฟ้องเนื่องจากขาดอายุความ', 'Case dismissed due to laspse of period of prescription', 				1, 1, now(), 'admin', now(), 'admin'),
    (242, 'NBC033', 'ปิดบัญชีเนื่องจากตัดหนี้สูญ', 'Write off account', 															1, 1, now(), 'admin', now(), 'admin'),
    (243, 'NBC042', 'โอนหรือขายหนี้', 'Debt transferred or sold.', 																	1, 1, now(), 'admin', now(), 'admin'),
    (244, 'NBC043', 'ปิดบัญชีของการโอนขายหนี้', 'ปิดบัญชีของการโอนขายหนี้', 														1, 1, now(), 'admin', now(), 'admin');