--td_employment
alter table td_employment alter column emp_va_work_phone type character varying(100);
--td_contract
ALTER TABLE td_contract ALTER COLUMN fpd_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
ALTER TABLE td_contract ALTER COLUMN pro_lin_id DROP NOT NULL;
--tu_org_bank_account
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_id DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_holder DROP NOT NULL;
ALTER TABLE tu_org_bank_account ALTER COLUMN ban_acc_number DROP NOT NULL;
--td_cashflow
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_id DROP NOT NULL;
ALTER TABLE td_cashflow ALTER COLUMN pro_lin_typ_id DROP NOT NULL;
--tu_black_list_item
alter table tu_black_list_item alter column per_id_number type character varying(50);
--td_asset
alter table td_asset alter column ass_va_rider_name type character varying(200);
--td_company
alter table td_company alter column com_mobile type character varying(50);
alter table td_company alter column com_tel type character varying(50);