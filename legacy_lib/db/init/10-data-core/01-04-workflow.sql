-- WkfHistoConfig  
ALTER TABLE tu_wkf_cfg_histo_config DISABLE TRIGGER all;
delete from tu_wkf_cfg_histo_config;
insert into tu_wkf_cfg_histo_config (wkf_cfg_his_id, mai_ent_id, wkf_cfg_his_class_name, wkf_cfg_his_item_class_name, wkf_cfg_his_temp_properties, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  (1000,  1000,   'com.nokor.common.app.workflow.model.WkfHistory',                       'com.nokor.common.app.workflow.model.WkfHistoryItem',                       null,       1, now(), 'admin', now(), 'admin'),
            (1,     1,      'com.nokor.efinance.core.quotation.model.QuotationWkfHistory',          'com.nokor.efinance.core.quotation.model.QuotationWkfHistoryItem',          null,       1, now(), 'admin', now(), 'admin'),
            (2,     2,      'com.nokor.efinance.core.contract.model.ContractWkfHistory',            'com.nokor.efinance.core.contract.model.ContractWkfHistoryItem',            null,       1, now(), 'admin', now(), 'admin'),
            (3,     3,      'com.nokor.efinance.core.payment.model.PaymentWkfHistory',              'com.nokor.efinance.core.payment.model.PaymentWkfHistoryItem',              null,       1, now(), 'admin', now(), 'admin'),
            (4,     4,      'com.nokor.efinance.core.collection.model.CollectionWkfHistory',        'com.nokor.efinance.core.collection.model.CollectionWkfHistoryItem',        null,       1, now(), 'admin', now(), 'admin'),
            (5,     5,      'com.nokor.efinance.core.auction.model.AuctionWkfHistory',              'com.nokor.efinance.core.auction.model.AuctionWkfHistoryItem',              null,       1, now(), 'admin', now(), 'admin'),
            (6,     6,      'com.nokor.efinance.core.applicant.model.ApplicantWkfHistory',          'com.nokor.efinance.core.applicant.model.ApplicantWkfHistoryItem',          null,       1, now(), 'admin', now(), 'admin'),
            (7,     7,      'com.nokor.efinance.core.dealer.model.DealerWkfHistory',                'com.nokor.efinance.core.dealer.model.DealerWkfHistoryItem',                null,       1, now(), 'admin', now(), 'admin'),
            (8,     8,      'com.nokor.ersys.core.hr.model.organization.OrganizationWkfHistory',    'com.nokor.ersys.core.hr.model.organization.OrganizationWkfHistoryItem',    null,       1, now(), 'admin', now(), 'admin'),
            (9,     9,      'com.nokor.ersys.collab.project.model.TaskWkfHistory',                  'com.nokor.ersys.collab.project.model.TaskWkfHistoryItem',                  null,       1, now(), 'admin', now(), 'admin'),
            (10,    15,     'com.nokor.efinance.core.contract.model.LockSplitWkfHistory',           'com.nokor.efinance.core.contract.model.LockSplitWkfHistoryItem',           null,       1, now(), 'admin', now(), 'admin'),
            (11,    11,     'com.nokor.efinance.core.contract.model.LockSplitItemWkfHistory',       'com.nokor.efinance.core.contract.model.LockSplitItemWkfHistoryItem',       null,       1, now(), 'admin', now(), 'admin'),
            (12,    27,     'com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfHistory', 'com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfHistoryItem', null,   1, now(), 'admin', now(), 'admin'),
            (13,    12,     'com.nokor.efinance.core.payment.model.PaymentFileWkfHistory',          'com.nokor.efinance.core.payment.model.PaymentFileWkfHistoryItem',          null,       1, now(), 'admin', now(), 'admin'),
            (14,    13,     'com.nokor.efinance.core.payment.model.PaymentFileItemWkfHistory',      'com.nokor.efinance.core.payment.model.PaymentFileItemWkfHistoryItem',      null,       1, now(), 'admin', now(), 'admin');
select setval('tu_wkf_cfg_histo_config_wkf_cfg_his_id_seq', 1000);
ALTER TABLE tu_wkf_cfg_histo_config ENABLE TRIGGER all;
update tu_wkf_cfg_histo_config 
    set wkf_cfg_his_temp_properties = 'code;title;titleEn;assignee;comment'
    where wkf_cfg_his_id = 9;

-- EWkfFlow  
ALTER TABLE tu_wkf_flow DISABLE TRIGGER all;
delete from tu_wkf_flow;
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, mai_ent_id, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
    values  
            (1000,  'Default',          'Default',          'Default',           null,  1,       1, 1, now(), 'admin', now(), 'admin'),
            (1,     'Quotation',       'Quotation',         'Quotation',         1,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (2,     'Contract',        'Contract',          'Contract',          2,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (3,     'Payment',         'Payment',           'Payment',           3,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (4,     'Collection',      'Collection',        'Collection',        4,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (5,     'Auction',         'Auction',           'Auction',           5,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (6,     'Applicant',       'Applicant',         'Applicant',         6,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (7,     'Dealer',          'Dealer',            'Dealer',            7,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (8,     'Organization',    'Organization',      'Organization',      8,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (9,     'Task',            'Task',              'Task',              9,     1,       1, 1, now(), 'admin', now(), 'admin'),
            (10,    'Project',         'Project',           'Project',           10,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (11,    'LockSplitItem',   'LockSplitItem',     'LockSplitItem',     11,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (12,    'PaymentFile',     'PaymentFile',       'PaymentFile',       12,    800,     1, 1, now(), 'admin', now(), 'admin'),
            (13,    'PaymentFileItem', 'PaymentFileItem',   'PaymentFileItem',   13,    800,     1, 1, now(), 'admin', now(), 'admin'),
            (14,    'JournalEntry',    'JournalEntry',      'JournalEntry',      14,    1001,    1, 1, now(), 'admin', now(), 'admin'),
            (15,    'LockSplit',       'LockSplit',         'LockSplit',         15,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (16,    'Individual',      'Individual',        'Individual',        16,    900,     1, 1, now(), 'admin', now(), 'admin'),
            (17,    'IndividualArc',   'IndividualArc',     'IndividualArc',     17,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (18,    'Bank',            'Bank',              'Bank',              18,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (19,    'DealerEmployee',  'DealerEmployee',    'DealerEmployee',    19,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (20,    'Company',         'Company',           'Company',           20,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (21,   'BlackListItem',    'BlackListItem',     'BlackListItem',     21,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (22,   'CompanyEmployee',    'CompanyEmployee',     'CompanyEmployee',     22,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (23,   'ContractFlag',    'ContractFlag',     'ContractFlag',     23,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (24,   'ContractSimulation', 'ContractSimulation', 'ContractSimulation',     24,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (25,   'Employee',           'Employee',           'Employee',     25,    1,       1, 1, now(), 'admin', now(), 'admin'),
            (26,   'Letter',             'Letter',      	   'Letter',       26,    1,       1, 1, now(), 'admin', now(), 'admin'),
			(27,   'TransactionEntry', 'TransactionEntry',  'TransactionEntry',       27,    1,       1, 1, now(), 'admin', now(), 'admin');
select setval('tu_wkf_flow_wkf_flo_id_seq', 1000);
ALTER TABLE tu_wkf_flow ENABLE TRIGGER all;

-- EWkfStatus
ALTER TABLE ts_wkf_status DISABLE TRIGGER all;
DELETE FROM ts_wkf_status;
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
    -- DO NOT REMOVE THE DEFAULT Status
    (  1, 'DEFAULT', 'Default', 'Default',                                              1, 1, now(), 'admin', now(), 'admin'),

    -- Remains the wkf_sta_id fo GLF
    -- GL Thai - Contract Statuses
    (200, 'PEN', 'Pending', 'Pending',                                                  1, 1, now(), 'admin', now(), 'admin'),
    (201, 'RECEIVED', 'Received', 'Received',                                           1, 1, now(), 'admin', now(), 'admin'),
    (202, 'FIN', 'ACTIVE', 'ACTIVE',                                                    1, 1, now(), 'admin', now(), 'admin'),
    (203, 'CAN', 'Cancel', 'Cancel',                                                    1, 1, now(), 'admin', now(), 'admin'),
    (204, 'EAR', 'Early Settlement', 'Early Settlement',                                1, 1, now(), 'admin', now(), 'admin'),
    (206, 'LOS', 'Loss', 'Loss',                                                        1, 1, now(), 'admin', now(), 'admin'),
    (207, 'CLO', 'Closed', 'Closed',                                                    1, 1, now(), 'admin', now(), 'admin'),
    (208, 'REP', 'Repossessed', 'Repossessed',                                          1, 1, now(), 'admin', now(), 'admin'),
    (209, 'THE', 'Theft', 'Theft',                                                      1, 1, now(), 'admin', now(), 'admin'),
    (210, 'ACC', 'Accident', 'Accident',                                                1, 1, now(), 'admin', now(), 'admin'),
    (211, 'FRA', 'Fraud', 'Fraud',                                                      1, 1, now(), 'admin', now(), 'admin'),
    (212, 'WRI', 'Terminate', 'Terminate',                                              1, 1, now(), 'admin', now(), 'admin'),
    (213, 'BLOCKED', 'Blocked', 'Blocked',                                              1, 1, now(), 'admin', now(), 'admin'),
    (216, 'HOLD_PAY', 'Hold Payment', 'Hold Payment',                                   1, 1, now(), 'admin', now(), 'admin'),
    (217, 'WITHDRAWN', 'Withdrawn', 'Withdrawn',                                        1, 1, now(), 'admin', now(), 'admin'),
	(218, 'SALELOST', 'Sale lost and cut lost', 'Sale lost and cut lost',               1, 1, now(), 'admin', now(), 'admin'),
	(219, 'LEGAL', 'Legal', 'Legal',                                                    1, 1, now(), 'admin', now(), 'admin'),
	(230, 'PEN_TRAN', 'Pending Transfer', 'Pending Transfer',                           1, 1, now(), 'admin', now(), 'admin'),
	(231, 'BLOCKED_TRAN', 'Blocked Transfer', 'Blocked Transfer',                       1, 1, now(), 'admin', now(), 'admin'),
    
    -- Legal Status
    (232, '001', 'Under Litigation', 'Under Litigation',                                                1, 1, now(), 'admin', now(), 'admin'),
    (233, '002', 'Under Process of Payment', 'Under Process of Payment',                                1, 1, now(), 'admin', now(), 'admin'),
    (234, '003', 'Account close with on going payment', 'Account close with on going payment',          1, 1, now(), 'admin', now(), 'admin'),
    (235, '004', 'Investigation requested by a/c owner', 'Investigation requested by a/c owner',      	1, 1, now(), 'admin', now(), 'admin'),

	-- NCB status
    (236, 'NBC010', 'ปกติ', 'Normal', 																				1, 1, now(), 'admin', now(), 'admin'),
    (237, 'NBC011', 'ปิดบัญชี', 'Closed account', 																	1, 1, now(), 'admin', now(), 'admin'),
    (238, 'NBC012', 'พักชำระหนี้ ตามนโยบายของรัฐบาล', 'Debt Moratorium as per government policy', 								1, 1, now(), 'admin', now(), 'admin'),
    (239, 'NBC020', 'หนี้ค้างชำระเกิน 90 วัน', 'Past due over 90 days', 														1, 1, now(), 'admin', now(), 'admin'),
    (240, 'NBC031', 'อยู่หว่างชำระหนี้ตามคำพิพากษาตามยินยอม', 'Under the processs of payment as agreed upon in the courts of law.', 1, 1, now(), 'admin', now(), 'admin'),
    (241, 'NBC032', 'ศาสพิพากษาฟ้องเนื่องจากขาดอายุความ', 'Case dismissed due to laspse of period of prescription', 				1, 1, now(), 'admin', now(), 'admin'),
    (242, 'NBC033', 'ปิดบัญชีเนื่องจากตัดหนี้สูญ', 'Write off account', 															1, 1, now(), 'admin', now(), 'admin'),
    (243, 'NBC042', 'โอนหรือขายหนี้', 'Debt transferred or sold.', 														1, 1, now(), 'admin', now(), 'admin'),
    (244, 'NBC043', 'ปิดบัญชีของการโอนขายหนี้', 'ปิดบัญชีของการโอนขายหนี้', 														        1, 1, now(), 'admin', now(), 'admin'),
	
    -- GL Thai - Payment Statuses
    -- (301, 'NEW', 'New', 'New',                                                           1, 1, now(), 'admin', now(), 'admin'),
    -- GL Thai - Collection Statuses
    -- (401, 'NEW', 'New', 'New',                                                           1, 1, now(), 'admin', now(), 'admin'),
    -- GL Thai - Auction Statuses
    -- (601, 'NEW', 'New', 'New',                                                           1, 1, now(), 'admin', now(), 'admin'),

    -- GL Thai - LockSplit Statuses
    (700, 'LNEW', 'New',          'New',                                               1, 1, now(), 'admin', now(), 'admin'),
    (701, 'LPEN', 'Pending',      'Pending',                                           1, 1, now(), 'admin', now(), 'admin'),
    (702, 'LPAR', 'Partial Paid', 'Partial Paid',                                      1, 1, now(), 'admin', now(), 'admin'),
    (703, 'LPAI', 'Paid',         'Paid',                                              1, 1, now(), 'admin', now(), 'admin'),
    (704, 'LEXP', 'Expired',      'Expired',                                           1, 1, now(), 'admin', now(), 'admin'),
	(705, 'LCAN', 'Canceled',      'Canceled',                                         1, 1, now(), 'admin', now(), 'admin'),
    
    -- GL Thai - PaymentFile and PaymentFileItem
    (800, 'NEW',            'New',              'New',                                 1, 1, now(), 'admin', now(), 'admin'),
    (801, 'ALLOCATED',      'Allocated',        'Allocated',                           1, 1, now(), 'admin', now(), 'admin'),
    (802, 'UNIDENTIFIED',   'Unidentified',     'Unidentified',                        1, 1, now(), 'admin', now(), 'admin'),
    (803, 'Over',              'Over',              'Over',                            1, 1, now(), 'admin', now(), 'admin'),
    (804, 'PARTIAL_ALLOCATED', 'Partial Allocated', 'Partial Allocated',               1, 1, now(), 'admin', now(), 'admin'),
    (805, 'UNMATCHED',         'Unmatched',         'Unmatched',                       1, 1, now(), 'admin', now(), 'admin'),
    (806, 'SUSPENDED',         'Suspended',         'Suspended',                       1, 1, now(), 'admin', now(), 'admin'),
    (807, 'MATCHED',           'Matched',           'Matched',                         1, 1, now(), 'admin', now(), 'admin'),
	(808, 'ERR', 'Error', 'Error',                        								1, 1, now(), 'admin', now(), 'admin'),
    
    -- GL Thai - Applicant Statuses
    (900, 'ACTIVE',         'Active',           'Active',                              1, 1, now(), 'admin', now(), 'admin'),
    (901, 'INACTIVE',       'Inactive',         'Inactive',                            1, 1, now(), 'admin', now(), 'admin'),
    (902, 'DEAD',           'Deactivate',       'Deactivate',                          1, 1, now(), 'admin', now(), 'admin'),
    
    -- Accounting - JournalEntry Statuses
    (1001, 'NEW', 'New', 'New',                                                         1, 1, now(), 'admin', now(), 'admin'),
    (1002, 'VALIDATED', 'Validated', 'Validated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1003, 'CANCELLED', 'Cancelled', 'Cancelled',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1004, 'POSTED', 'Posted', 'Posted',                                                1, 1, now(), 'admin', now(), 'admin'),
	(1005, 'READYTOPOST', 'Ready To Post', 'Ready To Post',                             1, 1, now(), 'admin', now(), 'admin'),
	
    -- Accounting - TransactionEntry Statuses
    (1010, 'DRAFT', 'Draft', 'Dratf',                                                   1, 1, now(), 'admin', now(), 'admin'),
    (1011, 'REJECTED', 'Rejected', 'Rejected',                                       	1, 1, now(), 'admin', now(), 'admin'),
    (1012, 'POSTED', 'Posted', 'Posted',                                                1, 1, now(), 'admin', now(), 'admin'),	
    (1013, 'APPROVED', 'Approved', 'Approved',                                          1, 1, now(), 'admin', now(), 'admin'),	

    (1100, 'AMENDED', 'AMENDED', 'AMENDED',                                                         1, 1, now(), 'admin', now(), 'admin'),
    (1101, 'AMENDED_SUBMI', 'AMENDED_SUBMI', 'AMENDED_SUBMI',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1102, 'AMENDED_REFUS', 'AMENDED_REFUS', 'AMENDED_REFUS',                                       1, 1, now(), 'admin', now(), 'admin'),
    (1103, 'AMENDED_VALID', 'AMENDED_VALID', 'AMENDED_VALID',                                                1, 1, now(), 'admin', now(), 'admin'),
	
		-- ISR Status
    (1200, 'ISRPEN',  'Pending', 'Pending',  1, 1, now(), 'admin', now(), 'admin'),
    (1201, 'ISRCONF', 'Confirm', 'Confirm',  1, 1, now(), 'admin', now(), 'admin'),
    

	-- Letter Status
	(1300, 'LENEW',     'New',     'New',     1, 1, now(), 'admin', now(), 'admin'),
    (1301, 'LESEND',    'Send',    'Send',    1, 1, now(), 'admin', now(), 'admin'),
    (1302, 'LEPENDING', 'Pending', 'Pending', 1, 1, now(), 'admin', now(), 'admin'),
    (1303, 'LESUCCESS', 'Success', 'Success', 1, 1, now(), 'admin', now(), 'admin'),

    -- Return Status
    (1400, 'REPEN',     'Pending',     'Pending',     1, 1, now(), 'admin', now(), 'admin'),
    (1401, 'RECLO',     'Closed',     'Closed',     1, 1, now(), 'admin', now(), 'admin'),

	-- Payment Statuses
    (2201, 'NAL', 'Not Allocated', 'Not Allocated',                                     1, 1, now(), 'admin', now(), 'admin'),
    (2202, 'RVA', 'Request Validation', 'Request Validation',                           1, 1, now(), 'admin', now(), 'admin'),
    (2203, 'VAL', 'Validation', 'Validation',                                       	1, 1, now(), 'admin', now(), 'admin'),
	(2204, 'PAI', 'Paid', 'Paid',                                      				 	1, 1, now(), 'admin', now(), 'admin'),
	(2205, 'CAN', 'Cancelled', 'Cancelled',                                       		1, 1, now(), 'admin', now(), 'admin'),
	(2206, 'UNP', 'Unpaid', 'Unpaid',                                       			1, 1, now(), 'admin', now(), 'admin'),
	(2207, 'PAL', 'Partially Allocated', 'Partially Allocated',                         1, 1, now(), 'admin', now(), 'admin'),
	
	 -- After sales simulation Statuses
    (5000, 'SIMULATED', 'Simulated', 'Simulated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (5001, 'VALIDATED', 'Validated', 'Validated',                                       1, 1, now(), 'admin', now(), 'admin'),
    (5002, 'CANCELLED', 'Cancelled', 'Cancelled',                                       1, 1, now(), 'admin', now(), 'admin');


select setval('ts_wkf_status_wkf_sta_id_seq', 10000);
ALTER TABLE ts_wkf_status ENABLE TRIGGER all;

ALTER TABLE tu_wkf_flow_status DISABLE TRIGGER all;
DELETE FROM tu_wkf_flow_status;
-- INSERT INTO tu_wkf_flow_status (wkf_flo_id, wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
-- VALUES 
-- GL Thai - Quotation
--  (1, 101,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 102,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 103,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 104,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 106,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 119,                1, 1, now(), 'admin', now(), 'admin'),
--  (1, 120,                1, 1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_wkf_flow_status ENABLE TRIGGER all;


ALTER TABLE tu_wkf_flow_item DISABLE TRIGGER all;
DELETE FROM tu_wkf_flow_item;
-- INSERT INTO tu_wkf_flow_item (wkf_flo_ite_id, wkf_flo_id, wkf_from_sta_id, wkf_to_sta_id, wkf_flo_ite_before_action, before_wkf_typ_act_id, wkf_flo_ite_after_action, after_wkf_typ_act_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
-- VALUES 
-- GL Thai - Quotation
--  (1, 1, null, 101, null, null, null, null,           1, now(), 'admin', now(), 'admin'),
--  (2, 1, 101,  102, null, null, null, null,           1, now(), 'admin', now(), 'admin'), 
--  (3, 1, 102,  120, null, null, null, null,           1, now(), 'admin', now(), 'admin'), 
--  (4, 1, 102,  104, null, null, null, null,           1, now(), 'admin', now(), 'admin'),            
--  (5, 1, 104,  102, null, null, null, null,           1, now(), 'admin', now(), 'admin'),
--  (6, 1, 104,  119, null, null, null, null,           1, now(), 'admin', now(), 'admin'),
--  (7, 1, 106,  120, null, null, null, null,           1, now(), 'admin', now(), 'admin'), 
--  (8, 1, 106,  104, null, null, null, null,           1, now(), 'admin', now(), 'admin');
ALTER TABLE tu_wkf_flow_item ENABLE TRIGGER all;
    
-- Objective: List of actions (controls) 
--            An action is linked to a WkfFlowItem (fromStatus/toStatus)
-- INSERT INTO tu_wkf_flow_control (sec_ctl_id, wkf_flo_ite_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
-- VALUES
--  -- GL Thai - Quotation action
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin'),
--  (1, 2,          1, 1, now(), 'admin', now(), 'admin');

-- Objective: List of profiles authorized to access/read a given status for a given WkfFlow
-- INSERT INTO tu_wkf_flow_profile
