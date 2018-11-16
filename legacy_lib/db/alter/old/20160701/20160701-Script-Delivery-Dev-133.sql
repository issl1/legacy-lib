INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(1005, 'READYTOPOST', 'Ready To Post', 'Ready To Post',                             1, 1, now(), 'admin', now(), 'admin');


delete from td_actor;
INSERT INTO td_actor (act_id, fin_com_id, sta_rec_id, usr_cre, dt_cre, usr_upd, dt_upd)
	VALUES
		(1, 1, 1, 'admin', now(), 'admin', now());
select setval('td_actor_act_id_seq', 2);


INSERT INTO tu_journal_event(jou_eve_id, jou_id, jou_eve_code, jou_eve_desc, jou_eve_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd, jou_eve_grp_id)
    VALUES
	(178, 4, '0101', 'Pay for financement', 'Pay for financement', 1, 1, now(), 'admin', now(), 'admin', 3),
	(179, 4, '0201', 'Pay for registration', 'Pay for registration', 1, 1, now(), 'admin', now(), 'admin', 3);