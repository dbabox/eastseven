drop table app_log_webcrawler;
create table app_log_webcrawler (
log_id integer not null,
log_message varchar(4000),
log_channel_id integer,
log_url varchar(4000),
log_catch_count integer,
create_time date default current_date,
primary key (log_id)
);