-- apply changes
create table cp_account (
  id                            bigint generated by default as identity not null,
  create_time                   timestamptz,
  password_salt                 bytea,
  password_hash_iterations      integer not null,
  email_validated               boolean default false not null,
  version                       integer not null,
  display_name                  varchar(128),
  email                         varchar(128),
  password                      varchar(256),
  profile                       json,
  constraint pk_cp_account primary key (id)
);

create table cp_biz_services (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  name                          varchar(64),
  constraint pk_cp_biz_services primary key (id)
);

create table cp_escalation_policies (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  handoff_notify_enabled        boolean default false not null,
  tags                          hstore,
  created_time                  timestamptz,
  no_one_ack_repeat             integer not null,
  name                          varchar(64),
  descr                         varchar(256),
  rule_set                      json,
  constraint pk_cp_escalation_policies primary key (id)
);

create table cp_incidents (
  id                            bigint generated by default as identity not null,
  incident_status               integer,
  event_id                      bigint,
  opened_time                   timestamptz,
  ack_time                      timestamptz,
  reopened_time                 timestamptz,
  resolved_time                 timestamptz,
  duration_ms                   bigint not null,
  version                       integer not null,
  constraint ck_cp_incidents_incident_status check ( incident_status in (0,1,2)),
  constraint pk_cp_incidents primary key (id)
);

create table cp_incident_events (
  id                            bigint generated by default as identity not null,
  create_time                   timestamptz,
  event_type                    varchar(11),
  constraint ck_cp_incident_events_event_type check ( event_type in ('INTEGRATION','WEB_APP','MOBILE_APP','API','EMAIL','SLACK')),
  constraint pk_cp_incident_events primary key (id)
);

create table cp_incident_priorities (
  id                            bigint generated by default as identity not null,
  p_order                       integer not null,
  name                          varchar(16),
  color                         varchar(16),
  constraint pk_cp_incident_priorities primary key (id)
);

create table cp_incident_timelines (
  id                            bigint generated by default as identity not null,
  constraint pk_cp_incident_timelines primary key (id)
);

create table cp_schedules (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  name                          varchar(64),
  descr                         varchar(1024),
  zone_id                       varchar(16),
  layers                        json,
  final_schedule                json,
  constraint pk_cp_schedules primary key (id)
);

create table cp_services (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  escalation_policy_id          bigint,
  descr                         text,
  auto_resolve_timeout_sec      integer not null,
  acknowledgement_timeout       integer not null,
  last_incident_timestamp       timestamptz,
  alert_creation                integer,
  oncall_s_rule_id              bigint,
  enabled                       boolean default false not null,
  name                          varchar(64),
  summary                       varchar(128),
  svc_status                    varchar(11),
  support_hour                  json,
  urgency_strategy              varchar(22),
  maintenance_windows           json,
  constraint ck_cp_services_alert_creation check ( alert_creation in (0,1)),
  constraint ck_cp_services_svc_status check ( svc_status in ('ACTIVE','WARNING','CRITICAL','MAINTENANCE','DISABLED')),
  constraint ck_cp_services_urgency_strategy check ( urgency_strategy in ('HIGH','LOW','DYNAMIC','BASED_ON_SUPPORT_HOURS')),
  constraint pk_cp_services primary key (id)
);

create table cp_tenants (
  id                            uuid not null,
  name                          varchar(32),
  descr                         varchar(255),
  constraint pk_cp_tenants primary key (id)
);

create table cp_tickets (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  service_id                    bigint not null,
  opened_time                   timestamptz,
  ack_time                      timestamptz,
  reopened_time                 timestamptz,
  resolved_time                 timestamptz,
  duration_ms                   bigint not null,
  ticket_status                 integer,
  name                          varchar(128),
  constraint ck_cp_tickets_ticket_status check ( ticket_status in (0,1,2)),
  constraint pk_cp_tickets primary key (id)
);

create table cp_ticket_priorities (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  p_order                       integer not null,
  name                          varchar(16),
  color                         varchar(16),
  constraint pk_cp_ticket_priorities primary key (id)
);

create table cp_tier (
  id                            bigint generated by default as identity not null,
  tenant_id                     uuid not null,
  constraint pk_cp_tier primary key (id)
);

