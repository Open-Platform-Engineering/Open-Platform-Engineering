create table cp_incidents (
    id bigint not null,
    ack_time timestamp (6),
    duration_ms bigint,
    event_id bigint,
    opened_time timestamp (6),
    reopened_time timestamp (6),
    resolved_time timestamp (6),
    incident_status smallint check (incident_status between 0 and 2),
    primary key (id)
);

create sequence cp_incident_events_seq start with 1 increment by 50;

create sequence cp_incident_priorities_seq start with 1 increment by 50;

create sequence cp_incidents_seq start with 1 increment by 50;

create table cp_incident_events (
    id bigint not null,
    createtime timestamp (6),
    event_type varchar(255),
    primary key (id)
);

create table cp_incident_priorities (
    id bigint not null,
    color varchar(16),
    name varchar(16),
    p_order integer,
    primary key (id)
);


create sequence cp_schedules_seq start with 1 increment by 50;

create table cp_schedules (
    id bigint not null,
    layers jsonb,
    time_zone varchar(16),
    version integer not null,
    primary key (id)
);
