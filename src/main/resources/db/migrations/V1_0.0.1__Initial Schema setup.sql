/**
 * Create tquestodb database
 */
/*CREATE DATABASE tquestodb
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;*/


/**
 * Create tquesto schema if not exists
 */
CREATE SCHEMA IF NOT EXISTS tquesto;

    

/**
 * Create tq_user table
 */
CREATE TABLE tquesto.tq_user
(
    id bigint NOT NULL,
    created_by character varying(50) COLLATE pg_catalog."default" NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_by character varying(50) COLLATE pg_catalog."default",
    last_modified_date TIMESTAMP WITH TIME ZONE,
    activated boolean NOT NULL,
    activation_key character varying(20) COLLATE pg_catalog."default",
    email character varying(100) COLLATE pg_catalog."default",
    first_name character varying(50) COLLATE pg_catalog."default",
    lang_key character varying(5) COLLATE pg_catalog."default",
    last_name character varying(50) COLLATE pg_catalog."default",
    password_hash character varying(60) COLLATE pg_catalog."default" NOT NULL,
    phone character varying(20) COLLATE pg_catalog."default",
    reset_date TIMESTAMP WITH TIME ZONE,
    reset_key character varying(20) COLLATE pg_catalog."default",
    user_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tq_user_pkey PRIMARY KEY (id),
    CONSTRAINT uk_44s6nc94locowkfq30s1s306h UNIQUE (email),
    CONSTRAINT uk_co21xg5bx6n594mg88cbpdcnp UNIQUE (user_name),
    CONSTRAINT uk_t4xbdp8l0eoktcrloj6k2ledp UNIQUE (phone)
)
TABLESPACE pg_default;


/**
 * Create tq_authority table
 */
CREATE TABLE tquesto.tq_authority
(
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tq_authority_pkey PRIMARY KEY (name)
)
TABLESPACE pg_default;


/**
 * Create tq_user_authority table
 */
CREATE TABLE tquesto.tq_user_authority
(
    user_id bigint NOT NULL,
    authority_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tq_user_authority_pkey PRIMARY KEY (authority_name, user_id),
    CONSTRAINT fkobfr2m7p7d3jpupnaq4ly3rog FOREIGN KEY (user_id)
        REFERENCES tquesto.tq_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkqlb6valiirimumin7tv81f72m FOREIGN KEY (authority_name)
        REFERENCES tquesto.tq_authority (name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;


/**
 * Create tq_configuration table
 */
CREATE TABLE tquesto.tq_configuration
(
    id bigint NOT NULL,
    created_by character varying(50) COLLATE pg_catalog."default" NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_by character varying(50) COLLATE pg_catalog."default",
    last_modified_date TIMESTAMP WITH TIME ZONE,
    key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    value character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tq_configuration_pkey PRIMARY KEY (id),
    CONSTRAINT uk_ocr2fpbdr2vnl4w0bbu6lv823 UNIQUE (value),
    CONSTRAINT uk_oiptntrncsmv8hwnwtcpqqp7h UNIQUE (key)
)
TABLESPACE pg_default;



/* Insert Few Demo Users */
INSERT INTO tquesto.tq_user ("id","user_name","password_hash","first_name","last_name","email","phone","activated","lang_key","activation_key","reset_key","created_by","created_date","reset_date","last_modified_by","last_modified_date") VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','98753452622','1','en',NULL,NULL,'system','2016-10-30 14:18:17',NULL,'system',NULL);
INSERT INTO tquesto.tq_user ("id","user_name","password_hash","first_name","last_name","email","phone","activated","lang_key","activation_key","reset_key","created_by","created_date","reset_date","last_modified_by","last_modified_date") VALUES (2,'anonymoususer','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','9853426262','1','en',NULL,NULL,'system','2016-10-30 14:18:17',NULL,'system',NULL);
INSERT INTO tquesto.tq_user ("id","user_name","password_hash","first_name","last_name","email","phone","activated","lang_key","activation_key","reset_key","created_by","created_date","reset_date","last_modified_by","last_modified_date") VALUES (3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','96543411263','1','en',NULL,NULL,'system','2016-10-30 14:18:17',NULL,'system',NULL);
INSERT INTO tquesto.tq_user ("id","user_name","password_hash","first_name","last_name","email","phone","activated","lang_key","activation_key","reset_key","created_by","created_date","reset_date","last_modified_by","last_modified_date") VALUES (4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','987654321','1','en',NULL,NULL,'system','2016-10-30 14:18:17',NULL,'system',NULL);


/* Insert Authrities */
INSERT INTO tquesto.tq_authority ("name") VALUES ('ROLE_ADMIN');
INSERT INTO tquesto.tq_authority ("name") VALUES ('ROLE_USER');


/* Insert Users Authrities */
INSERT INTO tquesto.tq_user_authority ("user_id","authority_name") VALUES (1,'ROLE_ADMIN');
INSERT INTO tquesto.tq_user_authority ("user_id","authority_name") VALUES (3,'ROLE_ADMIN');
INSERT INTO tquesto.tq_user_authority ("user_id","authority_name") VALUES (1,'ROLE_USER');
INSERT INTO tquesto.tq_user_authority ("user_id","authority_name") VALUES (3,'ROLE_USER');
INSERT INTO tquesto.tq_user_authority ("user_id","authority_name") VALUES (4,'ROLE_USER');


