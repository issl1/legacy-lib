--tu_incident_location
DROP TABLE if exists tu_incident_location;
DROP TABLE if exists td_company_contact_info;
CREATE TABLE tu_incident_location
(
  inc_loc_id bigserial NOT NULL,
  dt_cre timestamp without time zone NOT NULL,
  usr_cre character varying(30) NOT NULL,
  sta_rec_id bigint,
  dt_upd timestamp without time zone NOT NULL,
  usr_upd character varying(30) NOT NULL,
  sort_index integer,
  inc_loc_code character varying(10),
  inc_loc_desc character varying(50),
  inc_loc_desc_en character varying(50),
  pro_id bigint,
  CONSTRAINT tu_incident_location_pkey PRIMARY KEY (inc_loc_id),
  CONSTRAINT fkr7gpqmptbucm2w07fmt68kifq FOREIGN KEY (pro_id)
      REFERENCES tu_province (pro_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tu_incident_location
  OWNER TO efinance;

  
  
--td_company_contact_info
CREATE TABLE td_company_contact_info
(
  ind_cnt_inf_id bigserial NOT NULL,
  dt_cre timestamp without time zone NOT NULL,
  usr_cre character varying(30) NOT NULL,
  sta_rec_id bigint,
  dt_upd timestamp without time zone NOT NULL,
  usr_upd character varying(30) NOT NULL,
  com_id bigint NOT NULL,
  cnt_inf_id bigint NOT NULL,
  CONSTRAINT td_company_contact_info_pkey PRIMARY KEY (ind_cnt_inf_id),
  CONSTRAINT fkdt5prfr98v640poubx3bupaya FOREIGN KEY (com_id)
      REFERENCES td_company (com_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkspg5wgpnecpcswtyalp8xjm1g FOREIGN KEY (cnt_inf_id)
      REFERENCES td_contact_info (cnt_inf_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE td_company_contact_info
  OWNER TO efinance;