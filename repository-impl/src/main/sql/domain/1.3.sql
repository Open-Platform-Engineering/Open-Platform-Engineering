-- apply alter tables
alter table cp_teams add column if not exists creator_id bigint not null;
