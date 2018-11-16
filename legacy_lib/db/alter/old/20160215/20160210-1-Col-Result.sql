ALTER TABLE tu_col_result DISABLE TRIGGER all;
delete from tu_col_result;  
insert into tu_col_result (col_res_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, col_res_code, col_res_desc, col_res_desc_en, col_typ_id) 
values 
(1, now(), 'admin', 1, now(), 'admin', 'P01', 'Made 1st promise', 'Made 1st promise', 1),
(2, now(), 'admin', 1, now(), 'admin', 'P02', 'Made 2nd promise', 'Made 2nd promise', 1),
(3, now(), 'admin', 1, now(), 'admin', 'P03', 'Made 3rd promise', 'Made 3rd promise', 1),
(4, now(), 'admin', 1, now(), 'admin', 'P04', 'Said already paid ', 'Said already paid ', 1),
(5, now(), 'admin', 1, now(), 'admin', 'P05', 'Want to return motorbike without paying', 'Want to return motorbike without paying', 1),
(6, now(), 'admin', 1, now(), 'admin', 'P06', 'Motorbike is at garage', 'Motorbike is at garage', 1),
(7, now(), 'admin', 1, now(), 'admin', 'P07', 'Want to return motorbike and pay', 'Want to return motorbike and pay', 1),
(8, now(), 'admin', 1, now(), 'admin', 'P08', 'Lost/damaged motorbike', 'Lost/damaged motorbike', 1),
(9, now(), 'admin', 1, now(), 'admin', 'P09', 'Motorbike is pawned', 'Motorbike is pawned', 1),
(10, now(), 'admin', 1, now(), 'admin', 'P10', 'Motorbike at police station', 'Motorbike at police station', 1),
(11, now(), 'admin', 1, now(), 'admin', 'P11', 'No answer', 'No answer', 1),
(12, now(), 'admin', 1, now(), 'admin', 'P12', 'Busy - call back later', 'Busy - call back later', 1),
(13, now(), 'admin', 1, now(), 'admin', 'P13', 'Doesn''t want to pay', 'Doesn''t want to pay', 1),
(14, now(), 'admin', 1, now(), 'admin', 'P14', 'Wrong/dead number', 'Wrong/dead number', 1),
(15, now(), 'admin', 1, now(), 'admin', 'P15', 'Dead / incapacity to pay or return', 'Dead / incapacity to pay or return', 1),
(16, now(), 'admin', 1, now(), 'admin', 'P16', 'Returned motorbike already', 'Returned motorbike already', 1),
(17, now(), 'admin', 1, now(), 'admin', 'P17', 'Lessee has disappeared', 'Lessee has disappeared', 1),
(18, now(), 'admin', 1, now(), 'admin', 'P18', 'Want to close account', 'Want to close account', 1),
(19, now(), 'admin', 1, now(), 'admin', 'P19', 'Other', 'Other', 1);

select setval('tu_col_result_col_res_id_seq', 200);
ALTER TABLE tu_col_result ENABLE TRIGGER all;
