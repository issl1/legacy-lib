--tu_black_list_item
alter table tu_black_list_item alter column per_id_number type character varying(50);
--td_asset
alter table td_asset alter column ass_va_rider_name type character varying(200);
--td_company
alter table td_company alter column com_mobile type character varying(50);
alter table td_company alter column com_tel type character varying(50);