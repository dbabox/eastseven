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
drop sequence seq_twap_app_log_webcrawler ;
create sequence seq_twap_app_log_webcrawler ;

drop table twap_app_log_user;
create table twap_app_log_user(
log_id number not null,
user_id number,
user_name varchar2(50),
user_operation varchar2(10),
res_id number,
channel_id number,
create_time date default sysdate,
primary key (log_id)
);
drop sequence twap_app_log_user ;
create sequence twap_app_log_user ;

drop table wap_filter;
create table wap_filter(
FILTER_ID NUMBER(38),
KEY_VALUE VARCHAR2(100),
primary key (FILTER_ID)
);
drop sequence seq_wap_filter;
create sequence seq_wap_filter;

delete wap_filter;
commit;
insert into wap_filter(filter_id,key_value) values(seq_wap_filter.nextval,'ад');
commit;
