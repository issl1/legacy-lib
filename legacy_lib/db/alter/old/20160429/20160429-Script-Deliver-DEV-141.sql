INSERT INTO ts_ref_data (ref_tab_id, ref_dat_ide, ref_dat_code, ref_dat_desc, ref_dat_desc_en, ref_dat_value1, ref_dat_value2, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
(529, 11, 'OPERATION', 'Operation Fee', 'Operation Fee',            null, null, 1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	(1200, 'ISRPEN', 'Pending', 'Pending',        1, 1, now(), 'admin', now(), 'admin');

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

INSERT INTO tu_col_result(col_res_code, col_res_desc, col_res_desc_en, col_typ_id,
             dt_cre, usr_cre, dt_upd, usr_upd, sta_rec_id, sort_index)
   VALUES 
        ('P01', 'Made 1st promise', 'Made 1st promise',                                                 1, now(), 'admin', now(), 'admin', 1, 0),
        ('P02', 'Made 2nd promise', 'Made 2nd promise',                                                 1, now(), 'admin', now(), 'admin', 1, 0),
        ('P03', 'Made 3st promise', 'Made 3st promise',                                                 1, now(), 'admin', now(), 'admin', 1, 0),
        ('P04', 'Said already paid ', 'Said already paid ',                                             1, now(), 'admin', now(), 'admin', 1, 0),
        ('P05', 'Want to return motorbike without paying', 'Want to return motorbike without paying',   1, now(), 'admin', now(), 'admin', 1, 0),
        ('P06', 'Motorbike is at garage', 'Motorbike is at garage',                                     1, now(), 'admin', now(), 'admin', 1, 0),
        ('P07', 'Want to return motorbike and pay', 'Want to return motorbike and pay',                 1, now(), 'admin', now(), 'admin', 1, 0),
        ('P08', 'Lost/damaged motorbike', 'Lost/damaged motorbike',                                     1, now(), 'admin', now(), 'admin', 1, 0),
        ('P09', 'Motorbike is pawned', 'Motorbike is pawned',                                           1, now(), 'admin', now(), 'admin', 1, 0),
        ('P10', 'Motorbike at police station', 'Motorbike at police station',                           1, now(), 'admin', now(), 'admin', 1, 0),
        ('P11', 'No answer', 'No answer',                                                               1, now(), 'admin', now(), 'admin', 1, 0),
        ('P12', 'Busy - call back later', 'Busy - call back later',                                     1, now(), 'admin', now(), 'admin', 1, 0),
        ('P13', 'Doesnt want to pay', 'Doesnt want to pay',                                             1, now(), 'admin', now(), 'admin', 1, 0),
        ('P14', 'Wrong/dead number', 'Wrong/dead number',                                               1, now(), 'admin', now(), 'admin', 1, 0),
        ('P15', 'Dead / incapacity to pay or return', 'Dead / incapacity to pay or return',             1, now(), 'admin', now(), 'admin', 1, 0),
        ('P16', 'Returned motorbike already', 'Returned motorbike already',                             1, now(), 'admin', now(), 'admin', 1, 0),
        ('P17', 'Lessee has disappeared', 'Lessee has disappeared',                                     1, now(), 'admin', now(), 'admin', 1, 0),
        ('P18', 'Want to close account', 'Want to close account',                                       1, now(), 'admin', now(), 'admin', 1, 0),
        ('P19', 'Other', 'Other',                                                                       1, now(), 'admin', now(), 'admin', 1, 0);

INSERT INTO ts_db_version(db_ver_code, db_ver_date) VALUES ('XXX', now());
