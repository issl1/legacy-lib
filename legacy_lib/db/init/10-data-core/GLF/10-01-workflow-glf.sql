
-- EWkfFlow  
delete from tu_wkf_flow;
insert into tu_wkf_flow (wkf_flo_id, ref_code, ref_desc, ref_desc_en, default_at_creation_wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
	values	(1, 'com.nokor.efinance.core.quotation.model.Quotation', 'Quotation', 'Quotation',	             1,		1, 1, now(), 'admin', now(), 'admin'),
			(2, 'com.nokor.efinance.core.contract.model.Contract', 'Contract', 'Contract',		             1,		1, 1, now(), 'admin', now(), 'admin'),
			(3, 'com.nokor.efinance.core.payment.model.Payment', 'Payment', 'Payment',		             	 1,		1, 1, now(), 'admin', now(), 'admin'),
			(4, 'com.nokor.efinance.collection.model.Collection', 'Collection', 'Collection',	             1,		1, 1, now(), 'admin', now(), 'admin'),
			(5, 'com.nokor.efinance.auction.model.Auction', 'Auction', 'Auction',				             1,		1, 1, now(), 'admin', now(), 'admin'),
			(6, 'com.nokor.efinance.core.contract.model.LockSplitItem', 'LockSplitItem', 'LockSplitItem',	 1,		1, 1, now(), 'admin', now(), 'admin');


-- EWkfStatus
delete from ts_wkf_status;
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- GLF - Quotation Statuses
	(1, 'QUO', 'Quotation', 'Quotation',																				1, 0, now(), 'admin', now(), 'admin'),
	(2, 'PRO', 'Proposal', 'Proposal',																					1, 0, now(), 'admin', now(), 'admin'),
	(3, 'DEC', 'Declined', 'Declined',																					1, 0, now(), 'admin', now(), 'admin'),
	(4, 'PRA', 'proposal.approved', 'proposal.approved',																1, 0, now(), 'admin', now(), 'admin'),
	(5, 'SUB', 'Submitted', 'Submitted',																				1, 0, now(), 'admin', now(), 'admin'),
	(6, 'RAD', 'Request additional info', 'Request additional info',													1, 0, now(), 'admin', now(), 'admin'),
	(7, 'RFC', 'Request field check', 'Request field check',															1, 0, now(), 'admin', now(), 'admin'),
	(8, 'REU', 'Rejected underwriter', 'Rejected underwriter',															1, 0, now(), 'admin', now(), 'admin'),
	(9, 'REJ', 'Rejected', 'Rejected',																					1, 0, now(), 'admin', now(), 'admin'),
	(10, 'APU', 'Approved underwriter', 'Approved underwriter',															1, 0, now(), 'admin', now(), 'admin'),
	(11, 'APS', 'Approved underwriter supervisor', 'Approved underwriter supervisor',									1, 0, now(), 'admin', now(), 'admin'),
	(12, 'AWU', 'Approved with condition underwriter', 'Approved with condition underwriter',							1, 0, now(), 'admin', now(), 'admin'),
	(13, 'AWS', 'Approved with condition underwriter supervisor', 'Approved with condition underwriter supervisor',		1, 0, now(), 'admin', now(), 'admin'),
	(14, 'AWT', 'Approved with condition', 'Approved with condition',													1, 0, now(), 'admin', now(), 'admin'),
	(15, 'APV', 'Approved', 'Approved',																					1, 0, now(), 'admin', now(), 'admin'),
	(16, 'ACT', 'Activated', 'Activated',																				1, 0, now(), 'admin', now(), 'admin'),
	(17, 'CAN', 'Cancelled', 'Cancelled',																				1, 0, now(), 'admin', now(), 'admin'),
	(18, 'RCG', 'Request change guarantor', 'Request change guarantor',													1, 0, now(), 'admin', now(), 'admin'),
	(19, 'LCG', 'Allowed change guarantor', 'Allowed change guarantor',													1, 0, now(), 'admin', now(), 'admin'),
	(20, 'ACG', 'Approved change guarantor', 'Approved change guarantor',												1, 0, now(), 'admin', now(), 'admin'),
	(21, 'RVG', 'Request validate change guarantor', 'Request validate change guarantor',								1, 0, now(), 'admin', now(), 'admin'),
	(22, 'PPO', 'Pending purchase order', 'Pending purchase order',														1, 0, now(), 'admin', now(), 'admin');
	
	
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- GLF - Payment Statuses
	(201, 'NAL', 'Not allocated', 'Not allocated',										1, 1, now(), 'admin', now(), 'admin'),
	(202, 'RVA', 'Request validation', 'Request validation',								1, 1, now(), 'admin', now(), 'admin'),
	(203, 'VAL', 'Validation', 'Validation',												1, 1, now(), 'admin', now(), 'admin'),
	(204, 'PAI', 'Paid', 'Paid',															1, 1, now(), 'admin', now(), 'admin'),
	(205, 'CAN', 'Cancelled', 'Cancelled',													1, 1, now(), 'admin', now(), 'admin'),
	(206, 'UNP', 'Unpaid', 'Unpaid',														1, 1, now(), 'admin', now(), 'admin'),
	(207, 'PAL', 'Partially allocated', 'Partially allocated',								1, 1, now(), 'admin', now(), 'admin'),
	(208, 'REC', 'Processed', 'Processed',													1, 1, now(), 'admin', now(), 'admin'),
	(209, 'SOL', 'Payment totally allocated', 'Payment totally allocated',					1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- Collection Statuses
	(301, 'EVA', 'Evaluation', 'Evaluation',												1, 1, now(), 'admin', now(), 'admin');
	
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- Auction Statuses
	(401, 'EVA', 'Evaluation', 'Evaluation',											1, 1, now(), 'admin', now(), 'admin'),
	(402, 'VAL', 'Manager validation', 'Manager validation',							1, 1, now(), 'admin', now(), 'admin'),
	(403, 'WRE', 'Waiting for result', 'Waiting for result',							1, 1, now(), 'admin', now(), 'admin'),
	(404, 'SOL', 'Sold', 'Sold',														1, 1, now(), 'admin', now(), 'admin'),
	(405, 'CNS', 'Cannot sell', 'Cannot sell',											1, 1, now(), 'admin', now(), 'admin');

INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- GLF - Contract Statuses
	(501, 'COM', 'Committement', 'Committement',										1, 1, now(), 'admin', now(), 'admin'),
	(502, 'FIN', 'ACTIVE', 'ACTIVE',												    1, 1, now(), 'admin', now(), 'admin'),
	(503, 'CAN', 'Cancel', 'Cancel',													1, 1, now(), 'admin', now(), 'admin'),
	(504, 'EAR', 'Early settlement', 'Early settlement',								1, 1, now(), 'admin', now(), 'admin'),
	(505, 'PEN', 'Pending', 'Pending',													1, 1, now(), 'admin', now(), 'admin'),
	(506, 'LOS', 'Loss', 'Loss',														1, 1, now(), 'admin', now(), 'admin'),
	(507, 'CLO', 'Closed', 'Closed',													1, 1, now(), 'admin', now(), 'admin'),
	(508, 'REP', 'Repossessed', 'Repossessed',											1, 1, now(), 'admin', now(), 'admin'),
	(509, 'THE', 'Theft', 'Theft',														1, 1, now(), 'admin', now(), 'admin'),
	(510, 'ACC', 'Accident', 'Accident',												1, 1, now(), 'admin', now(), 'admin'),
	(511, 'FRA', 'Fraud', 'Fraud',														1, 1, now(), 'admin', now(), 'admin'),
	(512, 'WRI', 'Terminate', 'Terminate',												1, 1, now(), 'admin', now(), 'admin');
     
INSERT INTO ts_wkf_status (wkf_sta_id, ref_code, ref_desc, ref_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd) 
VALUES
	-- GL Thai - LockSplit Statuses
	(700, 'LNEW', 'New',          'New',                                                 1, 1, now(), 'admin', now(), 'admin'),
	(701, 'LPEN', 'Pending',      'Pending',								             1, 1, now(), 'admin', now(), 'admin'),
	(702, 'LPAR', 'Partial Paid', 'Partial Paid',                                        1, 1, now(), 'admin', now(), 'admin'),
	(703, 'LPAI', 'Paid',         'Paid',										         1, 1, now(), 'admin', now(), 'admin'),
	(704, 'LEXP', 'Expired',      'Expired',								             1, 1, now(), 'admin', now(), 'admin');
	
DELETE FROM tu_wkf_flow_status;
INSERT INTO tu_wkf_flow_status (wkf_flo_id, wkf_sta_id, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES 
-- GLF - Quotation
	(1, 1,				1, 0, now(), 'admin', now(), 'admin'),
	(1, 2,				1, 0, now(), 'admin', now(), 'admin'),
	(1, 3,				1, 0, now(), 'admin', now(), 'admin'),
	(1, 6,				1, 0, now(), 'admin', now(), 'admin'),
	(1, 7,				1, 0, now(), 'admin', now(), 'admin'),
	(1, 8,				1, 0, now(), 'admin', now(), 'admin'),
-- GL Thai - Contract
	(2, 501,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 502,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 503,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 504,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 505,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 506,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 507,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 508,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 509,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 510,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 511,			1, 1, now(), 'admin', now(), 'admin'),
	(2, 512,			1, 1, now(), 'admin', now(), 'admin'),
--	GL Thai - Payment
	(3, 201,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 202,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 203,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 204,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 205,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 206,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 207,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 208,			1, 1, now(), 'admin', now(), 'admin'),
	(3, 209,			1, 1, now(), 'admin', now(), 'admin');

DELETE FROM tu_wkf_flow_item;
INSERT INTO tu_wkf_flow_item (wkf_flo_id, wkf_from_sta_id, wkf_to_sta_id, wkf_flo_ite_before_action, before_wkf_typ_act_id, wkf_flo_ite_after_action, after_wkf_typ_act_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES 
-- GLF - Quotation
	(1, null, 1, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),
	(1, 1,    2, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),	
	(1, 1,    8, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),	
	(1, 2,    3, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),			   
	(1, 2,    6, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),
	(1, 2,    7, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),
	(1, 3,    2, null, null, null, null, 			0, now(), 'admin', now(), 'admin'),			   		   
	(1, 3,    8, null, null, null, null, 			0, now(), 'admin', now(), 'admin');
		   
