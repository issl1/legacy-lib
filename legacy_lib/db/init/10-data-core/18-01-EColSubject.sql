﻿ALTER TABLE tu_col_subject DISABLE TRIGGER all;
delete from tu_col_subject;
INSERT INTO tu_col_subject(col_suj_id, dt_cre, usr_cre, sta_rec_id, dt_upd, usr_upd, sort_index, col_suj_code, col_suj_desc, col_suj_desc_en)
    VALUES 
    (1, now(), 'admin', 1, now(), 'admin', 1, 'DEBT', 'Debt Follow Up', 'Debt Follow Up'),
    (2, now(), 'admin', 1, now(), 'admin', 1, 'ENQ',  'Enquiry', 	    'Enquiry'),
    (3, now(), 'admin', 1, now(), 'admin', 1, 'OTH',  'Other', 	     	'Other');

select setval('tu_col_subject_col_suj_id_seq', 50);
ALTER TABLE tu_col_subject ENABLE TRIGGER all;