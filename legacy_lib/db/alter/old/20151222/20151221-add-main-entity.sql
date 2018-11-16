insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values  
    (16,'com.nokor.efinance.core.applicant.model.Individual',    'Individual',    'Individual',          1, 1, now(), 'admin', now(), 'admin'),
    (17,'com.nokor.efinance.core.applicant.model.IndividualArc', 'IndividualArc', 'IndividualArc',       1, 1, now(), 'admin', now(), 'admin');
         
UPDATE ts_main_entity SET ref_code='com.nokor.efinance.core.collection.model.Collection', ref_desc_en='Contract Active' WHERE mai_ent_id= 4;
UPDATE ts_main_entity SET ref_code='com.nokor.efinance.core.auction.model.Auction', ref_desc_en='Contract Active' WHERE mai_ent_id= 5;
UPDATE ts_histo_reason SET ref_desc='Contract Active', ref_desc_en='Contract Active' WHERE his_rea_id= 101;
UPDATE ts_histo_reason SET ref_desc='Contract Terminate', ref_desc_en='Contract Terminate' WHERE his_rea_id= 109;		

insert into ts_histo_reason (his_rea_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
values	
	(111,  'CONTRACT_HOLD', 'Contract Hold', 'Contract Hold',     							2,		1, 1, now(), 'admin', now(), 'admin'),
	(112,  'CONTRACT_CAN', 'Contract Cancel', 'Contract Cancel',    						2,		1, 1, now(), 'admin', now(), 'admin');
