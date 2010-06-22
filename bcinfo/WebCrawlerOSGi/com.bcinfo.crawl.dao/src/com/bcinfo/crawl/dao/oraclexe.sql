drop table WAP_SITE_FOLDER_RESOURCE;
create table WAP_SITE_FOLDER_RESOURCE(
 FOLDER_RESOURCE_ID                                 NUMBER(13) NOT NULL,
 FOLDER_ID                                          NUMBER(13),
 WAPSITE_ID                                         NUMBER(13),
 PASSNAME                                           VARCHAR2(128),
 SUBTITLE                                           VARCHAR2(128),
 POST_DATE                                          TIMESTAMP(6),
 CHECK_DATE                                         TIMESTAMP(6),
 RES_AUTHOR                                         VARCHAR2(64),
 AVAIL_DATE                                         TIMESTAMP(6),
 FILE_PATH                                          VARCHAR2(512),
 RES_SIZE                                           NUMBER(11),
 KEYWORD                                            VARCHAR2(512),
 RES_DESC                                           VARCHAR2(256),
 BROWSE_COUNT                                       NUMBER(11),
 MODIFY_TIME                                        TIMESTAMP(6),
 STATUS                                             CHAR(1),
 TEMPLATE_ID                                        NUMBER(8),
 ISTURNTOPAGE                                       CHAR(1),
 FILE_NAME                                          VARCHAR2(64),
 USER_ID                                            NUMBER(13),
 COMPONENT_XML_PATH                                 VARCHAR2(64),
 RES_ORDER                                          NUMBER(13),
 RESOURCE_LEVEL_ID                                  NUMBER(13),
 PARENT_RESOURCE                                    NUMBER(13),
 SYNC                                               VARCHAR2(64),
 TOTALSCORES                                        NUMBER(10),
 TOTALNUM                                           NUMBER(10)
);

drop table WAP_SITE_FILE_RESOURCE;
create table WAP_SITE_FILE_RESOURCE(
 FILE_RESOURCE_ID                                   NUMBER(13) NOT NULL,
 FOLDER_ID                                          NUMBER(13),
 WAPSITE_ID                                         NUMBER(13),
 PASSNAME                                           VARCHAR2(128),
 SUBTITLE                                           VARCHAR2(128),
 POST_DATE                                          TIMESTAMP(6),
 CHECK_DATE                                         TIMESTAMP(6),
 RES_AUTHOR                                         VARCHAR2(64),
 AVAIL_DATE                                         TIMESTAMP(6),
 FILE_PATH                                          VARCHAR2(512),
 RES_SIZE                                           NUMBER(11),
 KEYWORD                                            VARCHAR2(512),
 RES_DESC                                           VARCHAR2(256),
 BROWSE_COUNT                                       NUMBER(11),
 MODIFY_TIME                                        TIMESTAMP(6),
 STATUS                                             CHAR(1),
 RES_TYPE                                           CHAR(3)
 );
 
 drop table APF_USER;
 create table APF_USER(
  USER_ID NUMBER(38),
  USERNAME VARCHAR2(32)
 );
 
 insert into APF_USER values(1,'admin');
 
 drop table TWAP_PROVISION_QUEUE;
 create table TWAP_PROVISION_QUEUE(
 QUEUE_ID                                           NUMBER(11) NOT NULL,
 PROCESS_TYPE                                       VARCHAR2(2),
 FOLDER_ID                                          NUMBER(13),
 OTHER_ID                                           NUMBER(13),
 STATUS                                             CHAR(1),
 DEBUG                                              VARCHAR2(512),
 WAPPORTAL_ID                                       NUMBER(11)
 );
 
 drop sequence S_WAP_SITE_FOLDER_RESOURCE;
 drop sequence S_WAP_SITE_FILE_RESOURCE;
 create sequence S_WAP_SITE_FOLDER_RESOURCE;
 create sequence S_WAP_SITE_FILE_RESOURCE;
 drop sequence SEQ_TWAP_PROVISION_QUEUE;
 create sequence SEQ_TWAP_PROVISION_QUEUE;