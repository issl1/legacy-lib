delete from td_journal_entry;

insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (14,'com.nokor.ersys.finance.accounting.model.JournalEntry', 'JournalEntry', 'JournalEntry',   		1, 1, now(), 'admin', now(), 'admin');

			
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- Accounting - JournalEntry Statuses
    (1001, 'NEW', 'New', 'New',                                                 		1, 1, now(), 'admin', now(), 'admin'),
    (1002, 'VALIDATED', 'Validated', 'Validated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1003, 'CANCELLED', 'Cancelled', 'Cancelled',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1004, 'POSTED', 'Posted', 'Posted',                                       			1, 1, now(), 'admin', now(), 'admin');
			