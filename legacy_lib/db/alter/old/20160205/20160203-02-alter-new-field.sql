--td_employment
alter table td_employment add column emp_remarks character varying(1000);
--td_address
alter table td_address add column old_address character varying(800);
alter table td_address add column year_of_data integer;