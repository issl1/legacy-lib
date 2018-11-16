ALTER TABLE tu_dealer_bank_account ALTER COLUMN ban_acc_holder DROP NOT NULL;
ALTER TABLE tu_dealer_bank_account ALTER COLUMN ban_acc_number DROP NOT NULL;
ALTER TABLE tu_dealer_bank_account ALTER COLUMN ban_id DROP NOT NULL;

UPDATE td_payment SET pay_met_id=3, pay_payee_acc_hol_id=6, pay_payee_bank_acc_id=1
 WHERE pay_typ_id=1 and pay_met_id=1 and dea_id=1;