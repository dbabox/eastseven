drop table twap_app_log_webcrawler;
create table twap_app_log_webcrawler (
log_id number not null,
log_message varchar(4000),
log_channel_id number,
log_url varchar(4000),
log_catch_count number,
create_time date default sysdate,
primary key (log_id)
);
create sequence seq_twap_app_log_webcrawler ;