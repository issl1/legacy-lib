-- Table: tu_sys_task

DROP TABLE if exists tu_sys_task;

CREATE TABLE tu_sys_task
(
  sys_tas_id bigserial NOT NULL,
  sys_tas_code character varying(255) NOT NULL,
  sys_tas_desc character varying(255),
  sys_tas_error_msg text,
  sys_tas_executed_date timestamp without time zone,
  sys_tas_method character varying(255),
  sys_tas_typ_code character varying(255) NOT NULL,
  sys_tas_where_code character varying(255) NOT NULL,
  sort_index integer,
  dt_cre timestamp without time zone NOT NULL,
  usr_cre character varying(30) NOT NULL,
  dt_upd timestamp without time zone NOT NULL,
  usr_upd character varying(30) NOT NULL,
  CONSTRAINT tu_sys_task_pkey PRIMARY KEY (sys_tas_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tu_sys_task OWNER TO efinance;

  
INSERT INTO tu_sys_task(sys_tas_code, sys_tas_desc, sys_tas_method, sys_tas_typ_code, sys_tas_where_code, sort_index, dt_cre, usr_cre, dt_upd, usr_upd)
	VALUES 
	--	('com.nokor.common.app.menu.MenuHelper', null, 'synchronizeMenus', 						'JAVA', 'PLACE_10', 	1, now(), 'admin', now(), 'admin'),
		('com.nokor.efinance.core.security.FinSecurity', null, 'execBuildDefaultSecurity', 		'JAVA', 'PLACE_10', 	1, now(), 'admin', now(), 'admin'),
		('com.nokor.efinance.core.security.FinSecurity', null, 'execBuildAdditionnalSecurity', 	'JAVA', 'PLACE_10', 	1, now(), 'admin', now(), 'admin');
