insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (26,'com.nokor.efinance.core.contract.model.Letter', 		'Letter', 	'Letter',           1, 1, now(), 'admin', now(), 'admin');
 
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values 
      (26,   'Letter',             'Letter',      	   'Letter',       26,    1,       1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
    VALUES
    (1300, 'LENEW', 	'New',     'New',     1, 1, now(), 'admin', now(), 'admin'),
	(1301, 'LESEND', 	'Send',    'Send',    1, 1, now(), 'admin', now(), 'admin'),
	(1302, 'LEPENDING', 'Pending', 'Pending', 1, 1, now(), 'admin', now(), 'admin'),
	(1303, 'LESUCCESS', 'Success', 'Success', 1, 1, now(), 'admin', now(), 'admin');

alter table td_letter add column wkf_sta_id bigint; 
alter table td_letter add column wkf_sub_sta_id bigint;
update td_letter set wkf_sta_id = 1300;
alter table td_letter alter column wkf_sta_id set NOT NULL;
