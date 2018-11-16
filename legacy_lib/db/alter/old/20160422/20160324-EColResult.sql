ALTER TABLE tu_col_result DISABLE TRIGGER all;
select setval('tu_col_result_col_res_id_seq', 0);
delete from tu_col_result;  
INSERT INTO tu_col_result(col_res_code, col_res_desc, col_res_desc_en, col_typ_id,
             dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, sort_index)
   VALUES 
   		('PF01', 'said paid already', 'said paid already', 																		2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF02', 'busy - call back later', 'busy - call back later', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF03', 'promise to pay', 'promise to pay', 																			2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF04', 'returned motorbike already', 'returned motorbike already', 													2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF05', 'want to return motorbike', 'want to return motorbike', 														2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF06', 'motorbike is used by a third party', 'motorbike is used by a third party', 									2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF07', 'motorbike is used by a third party who disappeared', 'motorbike is used by a third party who disappeared', 	2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF08', 'motorbike is pawned', 'motorbike is pawned', 																	2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF09', 'no answer', 'no answer', 																						2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF10', 'busy - call back later', 'busy - call back later', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF11', 'wrong/dead number', 'wrong/dead number', 																		2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF12', 'dead / incapacity to pay or return', 'dead / incapacity to pay or return', 									2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF13', 'wrong/dead address', 'wrong/dead address', 																	2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF14', 'lessee has disappeared', 'lessee has disappeared', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF15', 'lost/damaged motorbike', 'lost/damaged motorbike', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF16', 'motorbike at police station', 'motorbike at police station', 													2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF17', 'want to close account', 'want to close account', 																2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF18', 'does not want to pay', 'does not want to pay',	 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('PF19', 'other', 'other', 																				2, now(), 'admin', now(), 'admin', 1, 0),
   		
   		('F01', 'paid partial already', 'paid partial already', 																2, now(), 'admin', now(), 'admin', 1, 0),
   		('F02', 'paid full already', 'paid full already', 																		2, now(), 'admin', now(), 'admin', 1, 0),
   		('F03', 'absent', 'absent', 																							2, now(), 'admin', now(), 'admin', 1, 0),
   		('F04', 'asked to come back later', 'asked to come back later', 														2, now(), 'admin', now(), 'admin', 1, 0),
   		('F05', 'returned motorbike already', 'returned motorbike already', 													2, now(), 'admin', now(), 'admin', 1, 0),
   		('F06', 'lost/damaged motorbike', 'lost/damaged motorbike', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('F07', 'motorbike at police station', 'motorbike at police station', 													2, now(), 'admin', now(), 'admin', 1, 0),
   		('F08', 'motorbike is pawned', 'motorbike is pawned', 																	2, now(), 'admin', now(), 'admin', 1, 0),
   		('F09', 'repossessed', 'repossessed', 																					2, now(), 'admin', now(), 'admin', 1, 0),
   		('F10', 'does not want to pay or return', 'does not want to pay or return', 											2, now(), 'admin', now(), 'admin', 1, 0),
   		('F11', 'identity theft', 'identity theft', 																			2, now(), 'admin', now(), 'admin', 1, 0),
   		('F12', 'motorbike is used by a third party', 'motorbike is used by a third party', 									2, now(), 'admin', now(), 'admin', 1, 0),
   		('F13', 'motorbike is used by a third party who disappeared', 'motorbike is used by a third party who disappeared', 	2, now(), 'admin', now(), 'admin', 1, 0),
   		('F15', 'want to close account', 'want to close account', 																2, now(), 'admin', now(), 'admin', 1, 0),
   		('F16', 'wrong/dead address(es)', 'wrong/dead address(es)', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('F17', 'disappeared/unfindable', 'disappeared/unfindable', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('F18', 'motorbike taken at dealershop', 'motorbike taken at dealershop', 												2, now(), 'admin', now(), 'admin', 1, 0),
   		('F19', 'hold and wait for trap', 'hold and wait for trap', 															2, now(), 'admin', now(), 'admin', 1, 0),
   		('F20', 'dead / incapacity to pay or return','dead / incapacity to pay or return', 										2, now(), 'admin', now(), 'admin', 1, 0),
   		('F21', 'other', 'other', 																				2, now(), 'admin', now(), 'admin', 1, 0);

ALTER TABLE tu_col_result ENABLE TRIGGER all;
