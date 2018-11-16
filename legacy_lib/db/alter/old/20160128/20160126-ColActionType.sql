ALTER TABLE tu_col_action_type DISABLE TRIGGER all;
delete from tu_col_action_type;

INSERT INTO tu_col_action_type(col_act_typ_id, dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, col_act_id, col_typ_id)
    VALUES 
    	(1, now(), 'admin', now(), 'admin', 1, 1, 1),
    	(2, now(), 'admin', now(), 'admin', 1, 2, 1),
    	(3, now(), 'admin', now(), 'admin', 1, 3, 1),
    	(4, now(), 'admin', now(), 'admin', 1, 4, 1),
    	(5, now(), 'admin', now(), 'admin', 1, 5, 1),
    	(6, now(), 'admin', now(), 'admin', 1, 6, 1),
    	(7, now(), 'admin', now(), 'admin', 1, 7, 1),
    	(8, now(), 'admin', now(), 'admin', 1, 8, 1),
    	(9, now(), 'admin', now(), 'admin', 1, 9, 1),
    	(10, now(), 'admin', now(), 'admin', 1, 10, 1),
    	(11, now(), 'admin', now(), 'admin', 1, 16, 1),
    	(12, now(), 'admin', now(), 'admin', 1, 20, 1),

    	(13, now(), 'admin', now(), 'admin', 1, 1, 2),
    	(14, now(), 'admin', now(), 'admin', 1, 2, 2),
    	(15, now(), 'admin', now(), 'admin', 1, 3, 2),
    	(16, now(), 'admin', now(), 'admin', 1, 4, 2),
    	(17, now(), 'admin', now(), 'admin', 1, 5, 2),
    	(18, now(), 'admin', now(), 'admin', 1, 6, 2),
    	(19, now(), 'admin', now(), 'admin', 1, 7, 2),
    	(20, now(), 'admin', now(), 'admin', 1, 8, 2),
    	(21, now(), 'admin', now(), 'admin', 1, 9, 2),
    	(22, now(), 'admin', now(), 'admin', 1, 11, 2),
    	(23, now(), 'admin', now(), 'admin', 1, 13, 2),
    	(24, now(), 'admin', now(), 'admin', 1, 14, 2),
    	(25, now(), 'admin', now(), 'admin', 1, 15, 2),
    	(26, now(), 'admin', now(), 'admin', 1, 16, 2),
    	(27, now(), 'admin', now(), 'admin', 1, 17, 2),
    	(28, now(), 'admin', now(), 'admin', 1, 18, 2),
    	(29, now(), 'admin', now(), 'admin', 1, 19, 2),
    	(30, now(), 'admin', now(), 'admin', 1, 20, 2),

    	(32, now(), 'admin', now(), 'admin', 1, 2, 3),
    	(33, now(), 'admin', now(), 'admin', 1, 3, 3),
    	(34, now(), 'admin', now(), 'admin', 1, 4, 3),
    	(35, now(), 'admin', now(), 'admin', 1, 5, 3),
    	(36, now(), 'admin', now(), 'admin', 1, 6, 3),
    	(37, now(), 'admin', now(), 'admin', 1, 12, 3),
    	(38, now(), 'admin', now(), 'admin', 1, 13, 3),
    	(39, now(), 'admin', now(), 'admin', 1, 14, 3),
    	(40, now(), 'admin', now(), 'admin', 1, 15, 3),
    	(41, now(), 'admin', now(), 'admin', 1, 16, 3),
    	(42, now(), 'admin', now(), 'admin', 1, 17, 3),
    	(43, now(), 'admin', now(), 'admin', 1, 18, 3),
    	(44, now(), 'admin', now(), 'admin', 1, 19, 3),
    	(45, now(), 'admin', now(), 'admin', 1, 20, 3);

    	-- (46, now(), 'admin', now(), 'admin', 1, 1, null),
    	-- (47, now(), 'admin', now(), 'admin', 1, 2, null),
    	-- (48, now(), 'admin', now(), 'admin', 1, 3, null),
    	-- (49, now(), 'admin', now(), 'admin', 1, 4, null),
    	-- (50, now(), 'admin', now(), 'admin', 1, 5, null),
    	-- (51, now(), 'admin', now(), 'admin', 1, 6, null),
    	-- (52, now(), 'admin', now(), 'admin', 1, 7, null),
    	-- (53, now(), 'admin', now(), 'admin', 1, 8, null),
    	-- (54, now(), 'admin', now(), 'admin', 1, 9, null),
    	-- (55, now(), 'admin', now(), 'admin', 1, 10, null),
    	-- (56, now(), 'admin', now(), 'admin', 1, 16, null),
    	-- (57, now(), 'admin', now(), 'admin', 1, 20, null),

select setval('tu_col_action_type_col_act_typ_id_seq', 10000);
ALTER TABLE tu_col_action_type ENABLE TRIGGER all;
    	
    	