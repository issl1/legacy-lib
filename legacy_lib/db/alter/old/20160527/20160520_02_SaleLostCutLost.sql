ALTER TABLE td_sale_lost_cut_lost DISABLE TRIGGER all;
delete from td_sale_lost_cut_lost;
INSERT INTO td_sale_lost_cut_lost(sc_lost_id, sc_lost_code, sc_lost_desc, sc_lost_date, dt_cre,usr_cre,sta_rec_id,dt_upd,usr_upd)
VALUES 
	(1, 'S0001', 'SALE LOST JMT กลุ่มที่ 1 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(2, 'S0002', 'SALE LOST JMT กลุ่มที่ 2 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(3, 'S0003', 'SALE LOST JMT กลุ่มที่ 3 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(4, 'S0004', 'SALE LOST JMT กลุ่มที่ 4 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(5, 'S0005', 'SALE LOST JMT กลุ่มที่ 5 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(6, 'C0001', 'CUT LOST JMT กลุ่มที่ 1 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(7, 'C0002', 'CUT LOSS JMT กลุ่มที่ 2 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(8, 'C0003', 'CUT LOSS JMT กลุ่มที่ 3 (31/03/2015)', to_date('31.03.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(9, 'C0004', 'CUT LOSS JMT กลุ่มที่ 4 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(10, 'C0005', 'CUT LOSS JMT กลุ่มที่ 5 (01/04/2015)', to_date('01.04.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(11, 'CTCKB150629', 'CUT LOSS TCK กลุ่ม B (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(12, 'STCKB150629', 'SALE LOSS TCK กลุ่ม B (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(13, 'CLLDA150629', 'CUT LOSS LLD กลุ่ม A (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(14, 'CLLDC150629', 'CUT LOSS LLD กลุ่ม C (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(15, 'SLLDA150629', 'SALE LOSS LLD กลุ่ม A (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(16, 'SLLDC150629', 'SALE LOSS LLD กลุ่ม C (29/06/2015)', to_date('29.06.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(17, 'CLLDA150930', 'CUT LOSS LLD กลุ่ม A (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(18, 'CLLDB150930', 'CUT LOSS LLD กลุ่ม B (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(19, 'CLLDC150930', 'CUT LOSS LLD กลุ่ม C (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(20, 'CLLDD150930', 'CUT LOSS LLD กลุ่ม D (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(21, 'CLLDE150930', 'CUT LOSS LLD กลุ่ม E (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(22, 'CLLDF150930', 'CUT LOSS LLD กลุ่ม F (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(23, 'SLLDA150930', 'SALE LOSS LLD กลุ่ม A (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(24, 'SLLDB150930', 'SALE LOSS LLD กลุ่ม B (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(25, 'SLLDC150930', 'SALE LOSS LLD กลุ่ม C (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(26, 'SLLDD150930', 'SALE LOSS LLD กลุ่ม D (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(27, 'SLLDE150930', 'SALE LOSS LLD กลุ่ม E (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin'),
	(28, 'SLLDF150930', 'SALE LOSS LLD กลุ่ม F (30/09/2015)', to_date('30.09.2015', 'yyyy-mm-dd'),              now(), 'admin', 1, now(), 'admin');

select setval('td_sale_lost_cut_lost_sc_lost_id_seq', 50);
ALTER TABLE td_sale_lost_cut_lost ENABLE TRIGGER all;