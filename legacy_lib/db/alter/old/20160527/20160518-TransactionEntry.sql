INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES 
 -- Accounting - TransactionEntry Statuses
    (1010, 'DRAFT', 'Draft', 'Dratf',                                                   1, 1, now(), 'admin', now(), 'admin'),
    (1011, 'REJECTED', 'Rejected', 'Rejected',                                       	1, 1, now(), 'admin', now(), 'admin'),
    (1012, 'POSTED', 'Posted', 'Posted',                                                1, 1, now(), 'admin', now(), 'admin');

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values
	(27,'com.nokor.ersys.finance.accounting.model.TransactionEntry', 	'TransactionEntry', 	'TransactionEntry', 1, 1, now(), 'admin', now(), 'admin');

insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values
	(27,   'TransactionEntry', 'TransactionEntry',  'TransactionEntry',       27,    1,       1, 1, now(), 'admin', now(), 'admin');