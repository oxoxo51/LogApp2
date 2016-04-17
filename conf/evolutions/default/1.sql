# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table life_log (
  id                        bigint not null,
  log_date                  timestamp,
  sleep_hour                bigint,
  sleep_min                 bigint,
  wake_up_hour              bigint,
  wake_up_min               bigint,
  leave_hour                bigint,
  leave_min                 bigint,
  walk_count                bigint(10) default 0,
  run_distance              decimal(7,2) default 0.00,
  read_count                bigint(3) default 0,
  tech_read_count           bigint(3) default 0,
  biz_read_count            bigint(3) default 0,
  tech_study_time           bigint(5) default 0,
  english_study_time        bigint(5) default 0,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_life_log primary key (id))
;

create sequence life_log_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists life_log;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists life_log_seq;

