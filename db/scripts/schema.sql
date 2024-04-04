DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS status CASCADE;
DROP TABLE IF EXISTS control CASCADE;
DROP TABLE IF EXISTS message_detail CASCADE;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS process_data;
DROP TABLE IF EXISTS source_data;
DROP TABLE IF EXISTS table_column_info;
DROP TABLE IF EXISTS table_columns_info;
DROP TABLE IF EXISTS columns_display_preference CASCADE;


CREATE TABLE IF NOT EXISTS table_column_info
(
    id bigint primary key generated by default as identity,	
	table_name VARCHAR(40) NOT NULL,
    column_name VARCHAR(40) NOT NULL,
	header_name VARCHAR(40), 
    variable_name VARCHAR(40) NULL,	
    UNIQUE(table_name, column_name)
);

CREATE TABLE IF NOT EXISTS status (
  id bigint primary key generated by default as identity,	
  code VARCHAR(30) UNIQUE NOT NULL,
  description VARCHAR(130),
  type VARCHAR(10),
  is_deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS control (
  id bigint primary key generated by default as identity,	
  file_name VARCHAR(80) UNIQUE NOT NULL,
  user_id VARCHAR(30) NOT NULL,
  file_extracted_date DATE,
  batch_label_name VARCHAR(30) NOT NULL,
  load_type_facility BOOLEAN DEFAULT FALSE,	
  load_type_hds BOOLEAN DEFAULT FALSE,
  load_type_bus_org BOOLEAN DEFAULT FALSE,
  load_type_o_f_relationship BOOLEAN DEFAULT FALSE,
  load_type_o_o_relationship BOOLEAN DEFAULT FALSE,
  load_type_i_o_relationship BOOLEAN DEFAULT FALSE,
  load_type_wl_org_xref BOOLEAN DEFAULT FALSE,
  load_type_wl_prac_ident_xref BOOLEAN DEFAULT FALSE,
  process_start_date TIMESTAMP(6) DEFAULT NOW(),
  process_end_date TIMESTAMP(6),
  status_code VARCHAR(30),
  created_by varchar(45) DEFAULT CURRENT_USER,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_by VARCHAR(45),	
  updated_at TIMESTAMPTZ 
);

CREATE TABLE IF NOT EXISTS source_data (
  id bigint primary key generated by default as identity,
  control_id bigint NOT NULL,
  do_not_load                           varchar(10),
  stakeholder                           varchar(20),
  hds_ipc_id                            varchar(19), 
  hds_cpn_id                            varchar(19), 
  hds_provider_identifier1              varchar(50), 
  hds_provider_identifier2              varchar(50), 
  hds_provider_identifier3              varchar(50),
  hds_provider_identifier_type1         varchar(50), 
  hds_provider_identifier_type2         varchar(50), 
  hds_provider_identifier_type3         varchar(50),
  hds_hibc_facility_id                  varchar(50), 
  hds_type                              varchar(50),
  hds_name                              varchar(100), 
  hds_name_alias                        varchar(100), 
  hds_preferred_name_flag               varchar(1), 
  hds_email                             varchar(50), 
  hds_website                           varchar(50),
  hds_bus_tel_area_code                 varchar(30),
  hds_bus_tel_number                    varchar(30),
  hds_tel_extension                     varchar(30),
  hds_cell_area_code                    varchar(30),
  hds_cell_number                       varchar(30),
  hds_fax_area_code                     varchar(30),
  hds_fax_number                        varchar(30),
  hds_service_delivery_type             varchar(255),
  pcn_clinic_type                       varchar(255),
  pcn_pci_flag                          varchar(1),
  hds_hours_of_operation                varchar(255),
  hds_contact_name                      varchar(255),
  hds_is_for_profit_flag                varchar(1),
  source_status                         varchar(50),
  hds_parent_ipc_id                     varchar(19),
  bus_ipc_id                            varchar(19),
  bus_cpn_id                            varchar(19),
  bus_name                              varchar(100),
  bus_legal_name                        varchar(255),
  bus_payee_number                      varchar(255),
  bus_owner_name                        varchar(255),
  bus_owner_type                        varchar(255),
  bus_owner_type_other                  varchar(255),
  fac_building_name                     varchar(100),
  facility_hds_details_additional_info  varchar(255),
  physical_addr1                        varchar(255),
  physical_addr2                        varchar(255),
  physical_addr3                        varchar(255),
  physical_addr4                        varchar(255),
  physical_city                         varchar(255),
  physical_province                     varchar(255),
  physical_pcode                        varchar(255),
  physical_country                      varchar(255),
  phys_addr_is_private                  varchar(1),
  mail_addr1                            varchar(255),
  mail_addr2                            varchar(255),
  mail_addr3                            varchar(255),
  mail_addr4                            varchar(255),
  mail_city                             varchar(255),
  mail_bc                               varchar(255),
  mail_pcode                            varchar(255),
  mail_country                          varchar(255),
  mail_addr_is_private                  varchar(1),
  created_by VARCHAR(30) DEFAULT CURRENT_USER,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_by VARCHAR(30),	
  updated_at TIMESTAMPTZ,
     CONSTRAINT source_data_control_id_fk
      FOREIGN KEY(control_id) 
	  REFERENCES control(id)
);	

CREATE TABLE IF NOT EXISTS process_data (
  id bigint primary key NOT NULL,
  control_id bigint NOT NULL,
  do_not_load                           varchar(10),
  stakeholder                           varchar(20),
  hds_ipc_id                            varchar(19), 
  hds_cpn_id                            varchar(19), 
  hds_provider_identifier1              varchar(50), 
  hds_provider_identifier2              varchar(50), 
  hds_provider_identifier3              varchar(50),
  hds_provider_identifier_type1         varchar(50), 
  hds_provider_identifier_type2         varchar(50), 
  hds_provider_identifier_type3         varchar(50),
  hds_hibc_facility_id                  varchar(50), 
  hds_type                              varchar(50),
  hds_name                              varchar(100), 
  hds_name_alias                        varchar(100), 
  hds_preferred_name_flag               varchar(1), 
  hds_email                             varchar(50), 
  hds_website                           varchar(50),
  hds_bus_tel_area_code                 varchar(30),
  hds_bus_tel_number                    varchar(30),
  hds_tel_extension                     varchar(30),
  hds_cell_area_code                    varchar(30),
  hds_cell_number                       varchar(30),
  hds_fax_area_code                     varchar(30),
  hds_fax_number                        varchar(30),
  hds_service_delivery_type             varchar(255),
  pcn_clinic_type                       varchar(255),
  pcn_pci_flag                          varchar(1),
  hds_hours_of_operation                varchar(255),
  hds_contact_name                      varchar(255),
  hds_is_for_profit_flag                varchar(1),
  source_status                         varchar(50),
  hds_parent_ipc_id                     varchar(19),
  bus_ipc_id                            varchar(19),
  bus_cpn_id                            varchar(19),
  bus_name                              varchar(100),
  bus_legal_name                        varchar(255),
  bus_payee_number                      varchar(255),
  bus_owner_name                        varchar(255),
  bus_owner_type                        varchar(255),
  bus_owner_type_other                  varchar(255),
  fac_building_name                     varchar(100),
  facility_hds_details_additional_info  varchar(255),
  physical_addr1                        varchar(255),
  physical_addr2                        varchar(255),
  physical_addr3                        varchar(255),
  physical_addr4                        varchar(255),
  physical_city                         varchar(255),
  physical_province                     varchar(255),
  physical_pcode                        varchar(255),
  physical_country                      varchar(255),
  phys_addr_is_private                  varchar(1),
  mail_addr1                            varchar(255),
  mail_addr2                            varchar(255),
  mail_addr3                            varchar(255),
  mail_addr4                            varchar(255),
  mail_city                             varchar(255),
  mail_bc                               varchar(255),
  mail_pcode                            varchar(255),
  mail_country                          varchar(255),
  mail_addr_is_private                  varchar(1),
  facility_id							bigint,
  rowstatus_code VARCHAR(30),
  error_msg VARCHAR(500),
  created_by VARCHAR(30),
  created_at TIMESTAMPTZ,
  updated_by VARCHAR(30),	
  updated_at TIMESTAMPTZ,
     CONSTRAINT process_data_control_id_fk
      FOREIGN KEY(control_id) 
	  REFERENCES control(id),
     CONSTRAINT process_data_rowstatus_code_fk
      FOREIGN KEY(rowstatus_code) 
	  REFERENCES status(code)
);


CREATE TABLE IF NOT EXISTS message_detail (
  id bigint primary key generated by default as identity,
  process_data_id bigint,
  rowstatus_code VARCHAR(30),
  error_code integer,
  error_type VARCHAR(40),
  error_msg  VARCHAR(130)
);


CREATE TABLE IF NOT EXISTS columns_display_preference (
  id bigint primary key generated by default as identity,
  user_id VARCHAR(30) NOT NULL,
  view_name VARCHAR(40) NOT NULL,
  display_columns TEXT[],
  created_at TIMESTAMPTZ,
  created_by VARCHAR(30),
  updated_by VARCHAR(30),
  updated_at TIMESTAMPTZ,
  CONSTRAINT user_pref_unique UNIQUE(user_id, view_name)
);


CREATE INDEX user_viewname_index ON columns_display_preference(user_id, view_name);
