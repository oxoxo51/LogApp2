# --- !Ups

create table life_log (
  id                            serial not null primary key,
  log_date                      timestamp,
  sleep_hour                    integer,
  sleep_min                     integer,
  wake_up_hour                  integer,
  wake_up_min                   integer,
  leave_hour                    integer,
  leave_min                     integer,
  walk_count                    integer default 0,
  run_distance                  decimal(7,2) default 0.00,
  read_count                    integer default 0,
  tech_read_count               integer default 0,
  biz_read_count                integer default 0,
  tech_study_time               integer default 0,
  english_study_time            integer default 0,
  create_date                   timestamp not null,
  update_date                   timestamp not null
);


# --- !Downs

drop table life_log;

