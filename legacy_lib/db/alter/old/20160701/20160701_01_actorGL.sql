delete from td_actor;
INSERT INTO td_actor (act_id, fin_com_id, sta_rec_id, usr_cre, dt_cre, usr_upd, dt_upd)
	VALUES
		(1, 1, 1, 'admin', now(), 'admin', now());
select setval('td_actor_act_id_seq', 2);