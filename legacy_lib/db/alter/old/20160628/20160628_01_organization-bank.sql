ALTER TABLE tu_org_bank_account DISABLE TRIGGER all;
delete from tu_org_bank_account;
INSERT INTO tu_org_bank_account(org_ban_id, org_id, ban_acc_id, ban_acc_holder, ban_acc_number, 
    ban_acc_comment, ban_id, typ_ban_acc_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 569, null, null, null, null, 1, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_bank_account_org_ban_id_seq', 100);
ALTER TABLE tu_org_bank_account ENABLE TRIGGER all;




ALTER TABLE tu_org_account_holder DISABLE TRIGGER all;
delete from tu_org_account_holder;
INSERT INTO tu_org_account_holder(org_acc_hol_id, org_id, acc_hol_id, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)
VALUES
	(1, 86, 558, 1, now(), 'admin', now(), 'admin'),
	(2, 721, 558, 1, now(), 'admin', now(), 'admin'),
	(3, 722, 558, 1, now(), 'admin', now(), 'admin'),
	(4, 848, 558, 1, now(), 'admin', now(), 'admin'),
	(5, 2531, 558, 1, now(), 'admin', now(), 'admin'),
	(6, 2602, 558, 1, now(), 'admin', now(), 'admin'),
	(7, 3038, 558, 1, now(), 'admin', now(), 'admin'),
	(8, 4060, 558, 1, now(), 'admin', now(), 'admin'),
	(9, 5970, 558, 1, now(), 'admin', now(), 'admin'),
	(10, 5999, 558, 1, now(), 'admin', now(), 'admin'),
	(11, 6178, 558, 1, now(), 'admin', now(), 'admin'),
	(12, 7396, 558, 1, now(), 'admin', now(), 'admin');
select setval('tu_org_account_holder_org_acc_hol_id_seq', 100);
ALTER TABLE tu_org_account_holder ENABLE TRIGGER all;