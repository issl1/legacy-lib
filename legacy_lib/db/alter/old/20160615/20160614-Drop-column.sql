ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS typ_ban_acc_id;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_id;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_branch;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_acc_holder;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_acc_number;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_acc_comment;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_external_id;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS externalid;
ALTER TABLE tu_dealer_bank_account DROP COLUMN IF EXISTS ban_acc_holder_id;

ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS typ_ban_acc_id;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_id;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_branch;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_acc_holder;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_acc_number;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_acc_comment;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS externalid;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_acc_holder_id;
ALTER TABLE tu_dealer_account_holder DROP COLUMN IF EXISTS ban_external_id;

UPDATE td_payment SET pay_met_id=3, pay_payee_acc_hol_id=6, pay_payee_bank_acc_id=1
 WHERE pay_typ_id=1 and pay_met_id=1 and dea_id=1;