-- apply alter tables
alter table cp_account add column if not exists teams bigint[];
-- apply post alter
alter table cp_account add constraint uq_cp_account_email unique  (email);
alter table cp_teams add constraint uq_cp_teams_name unique  (name);
