    
-- EMainEntity  
ALTER TABLE ts_main_entity DISABLE TRIGGER all;
delete from ts_main_entity;
insert into ts_main_entity (mai_ent_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (1, 'com.nokor.efinance.core.quotation.model.Quotation', 'Quotation', 'Quotation',                   1, 1, now(), 'admin', now(), 'admin'),
            (2, 'com.nokor.efinance.core.contract.model.Contract', 'Contract', 'Contract',                       1, 1, now(), 'admin', now(), 'admin'),
            (3, 'com.nokor.efinance.core.payment.model.Payment', 'Payment', 'Payment',                           1, 1, now(), 'admin', now(), 'admin'),
            (4, 'com.nokor.efinance.core.collection.model.Collection', 'Collection', 'Collection',               1, 1, now(), 'admin', now(), 'admin'),
            (5, 'com.nokor.efinance.core.auction.model.Auction', 'Auction', 'Auction',                           1, 1, now(), 'admin', now(), 'admin'),
            (6, 'com.nokor.efinance.core.applicant.model.Applicant', 'Applicant', 'Applicant',                   1, 1, now(), 'admin', now(), 'admin'),
            (7, 'com.nokor.efinance.core.dealer.model.Dealer', 'Dealer', 'Dealer',                               1, 1, now(), 'admin', now(), 'admin'),
            (8, 'com.nokor.ersys.core.hr.model.organization.Organization', 'Organization', 'Organization',       1, 1, now(), 'admin', now(), 'admin'),
            (9, 'com.nokor.ersys.collab.project.model.Task', 'Task', 'Task',                                     1, 1, now(), 'admin', now(), 'admin'),
            (10,'com.nokor.ersys.collab.project.model.Project', 'Project', 'Project',                            1, 1, now(), 'admin', now(), 'admin'),
            (11,'com.nokor.efinance.core.contract.model.LockSplitItem', 'LockSplitItem', 'LockSplitItem',        1, 1, now(), 'admin', now(), 'admin'),
            (12,'com.nokor.efinance.core.payment.model.PaymentFile', 'PaymentFile', 'PaymentFile',               1, 1, now(), 'admin', now(), 'admin'),
            (13,'com.nokor.efinance.core.payment.model.PaymentFileItem', 'PaymentFileItem', 'PaymentFileItem',   1, 1, now(), 'admin', now(), 'admin'),
            (14,'com.nokor.ersys.finance.accounting.model.JournalEntry', 'JournalEntry', 'JournalEntry',         1, 1, now(), 'admin', now(), 'admin'),
            (15,'com.nokor.efinance.core.contract.model.LockSplit', 'LockSplit', 'LockSplit',                    1, 1, now(), 'admin', now(), 'admin'),
            (16,'com.nokor.efinance.core.applicant.model.Individual', 'Individual', 'Individual',                1, 1, now(), 'admin', now(), 'admin'),
            (17,'com.nokor.efinance.core.applicant.model.IndividualArc', 'IndividualArc', 'IndividualArc',       1, 1, now(), 'admin', now(), 'admin'),
            (18,'com.nokor.ersys.core.finance.model.Bank', 'Bank', 'Bank',                                       1, 1, now(), 'admin', now(), 'admin'),
            (19,'com.nokor.efinance.core.dealer.model.DealerEmployee', 'DealerEmployee', 'DealerEmployee',       1, 1, now(), 'admin', now(), 'admin'),
            (20,'com.nokor.efinance.core.applicant.model.Company', 'Company', 'Company',                         1, 1, now(), 'admin', now(), 'admin'),
            (21,'com.nokor.efinance.core.common.reference.model.BlackListItem', 'BlackListItem', 'BlackListItem',                         1, 1, now(), 'admin', now(), 'admin'),
            (22,'com.nokor.efinance.core.applicant.model.CompanyEmployee', 'CompanyEmployee', 'CompanyEmployee',                          1, 1, now(), 'admin', now(), 'admin'),
            (23,'com.nokor.efinance.core.collection.model.ContractFlag', 'ContractFlag', 'ContractFlag',                          1, 1, now(), 'admin', now(), 'admin'),
            (24,'com.nokor.efinance.core.contract.model.ContractSimulation', 'ContractSimulation', 'ContractSimulation',           1, 1, now(), 'admin', now(), 'admin'),
            (25,'com.nokor.ersys.core.hr.model.organization.Employee', 	'Employee', 'Employee',         1, 1, now(), 'admin', now(), 'admin'),
            (26,'com.nokor.efinance.core.contract.model.Letter', 		'Letter', 	'Letter',           1, 1, now(), 'admin', now(), 'admin'),
			(27,'com.nokor.ersys.finance.accounting.model.TransactionEntry', 	'TransactionEntry', 	'TransactionEntry', 1, 1, now(), 'admin', now(), 'admin'),
			(29,'com.nokor.efinance.core.actor.model.ThirdParty', 'ThirdParty', 'ThirdParty',                1, 1, now(), 'admin', now(), 'admin');

select setval('ts_main_entity_mai_ent_id_seq', 50); 
ALTER TABLE ts_main_entity ENABLE TRIGGER all;  
