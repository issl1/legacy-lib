--td_contract
ALTER TABLE td_contract ALTER COLUMN fpd_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_id DROP NOT NULL;
--tu_org_bank_account
ALTER TABLE tu_org_bank_account ADD COLUMN ban_external_id bigint;
ALTER TABLE tu_org_bank_account ADD COLUMN ban_acc_holder_id bigint;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_id DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_holder DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_number DROP NOT NULL;
--tu_dealer
ALTER TABLE tu_dealer ADD COLUMN dea_cust bigint;
--td_cashflow
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_id DROP NOT NULL;
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
ALTER TABLE td_cashflow ADD COLUMN pay_cha_id bigint;
ALTER TABLE td_cashflow ADD COLUMN loc_spl_typ_id bigint;