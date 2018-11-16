CREATE TABLE ts_db_version
(
  db_ver_id bigserial NOT NULL,
  db_ver_code character varying(10) NOT NULL,
  db_ver_date timestamp without time zone NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ts_db_version OWNER TO efinance;
