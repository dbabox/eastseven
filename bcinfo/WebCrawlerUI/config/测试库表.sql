/*==============================================================*/
/* Table: TWAP_PUBLIC_CHANNEL  频道表                           */
/*==============================================================*/
create table TWAP_PUBLIC_CHANNEL  (
   CHANNEL_ID           NUMERIC(11)                     not null,
   CHANNEL_PID          NUMERIC(11),
   CHANNEL_NAME         VARCHAR2(64),
   CHANNEL_PATH         VARCHAR2(256),
   CHANNEL_INDEX        VARCHAR2(64),
   CREATE_TIME          DATE                           default SYSDATE,
   constraint PK_TWAP_PUBLIC_CHANNEL primary key (CHANNEL_ID)
)
/*tablespace NX_DATA4*/;
create sequence SEQ_TWAP_PUBLIC_CHANNEL;

/*==============================================================*/
/* Table: TWAP_PUBLIC_CRAWL_RESOURCE  抓取资源表                */
/*==============================================================*/
--drop table TWAP_PUBLIC_CRAWL_RESOURCE;
create table TWAP_PUBLIC_CRAWL_RESOURCE  (
   RES_ID            NUMERIC(11) not null,
   CHANNEL_ID        NUMERIC(11),
   RES_TITLE         VARCHAR2(512),
   RES_LINK          VARCHAR2(512),
   RES_CONTENT       VARCHAR2(4000),
   RES_IMG_PATH_SET  VARCHAR2(4000),
   RES_FILE_PATH_SET  VARCHAR2(4000),
   RES_TEXT          CLOB default empty_clob(),
   RES_STATUS        CHAR(1) default '0',/*0:未审核 1:已审核*/
   CREATE_TIME       DATE  default sysdate,
   constraint PK_TWAP_PUBLIC_CRAWL_RESOURCE primary key (RES_ID)
)
/*tablespace NX_DATA4*/;
create sequence SEQ_TWAP_PUBLIC_CRAWL_RESOURCE;

/*==============================================================*/
/* Table: TWAP_PUBLIC_CRAWL_RESOURCE_STA  抓取资源使用表        */
/*==============================================================*/
--drop table TWAP_PUBLIC_CRAWL_RESOURCE_STA;
create table TWAP_PUBLIC_CRAWL_RESOURCE_STA  (
   RES_ID            NUMERIC(11),
   CHANNEL_ID        NUMERIC(11),
   RES_TITLE         VARCHAR2(512),
   RES_LINK          VARCHAR2(512),
   RES_CONTENT       VARCHAR2(4000),
   RES_IMG_PATH_SET  VARCHAR2(4000),
   RES_FILE_PATH_SET  VARCHAR2(4000),
   RES_TEXT          CLOB default empty_clob(),
   RES_STATUS        CHAR(1) default '0',/*0:未审核 1:已审核*/
   CREATE_TIME       DATE  default sysdate,
   constraint PK_TWAP_PUBLIC_CRAWL_RES_STA primary key (RES_ID)
)
/*tablespace NX_DATA4*/;

/*==============================================================*/
/* Table: TWAP_PUBLIC_CRAWL_LIST  抓取地址表                    */
/*==============================================================*/
create table TWAP_PUBLIC_CRAWL_LIST  (
   CRAWL_ID              NUMERIC(11) not null,
   CHANNEL_ID             NUMERIC(11) not null,
   CRAWL_URL             VARCHAR2(512) not null,
   CRAWL_STATUS           CHAR(1) default '1',
   CREATE_TIME           DATE    default SYSDATE,
   constraint PK_TWAP_PUBLIC_CRAWL_LIST primary key (CRAWL_ID)
)
/*tablespace NX_DATA4*/;
create sequence SEQ_TWAP_PUBLIC_CRAWL_LIST ;

/*==============================================================*/
/* Table: TWAP_PUBLIC_CHANNEL_MAPPING  频道订阅表               */
/*==============================================================*/
--drop table TWAP_PUBLIC_CHANNEL_MAPPING;
create table TWAP_PUBLIC_CHANNEL_MAPPING  (
   MAPPING_ID              NUMERIC(11) not null,
   CHANNEL_ID               NUMERIC(11) not null,
   LOCAL_CODE              VARCHAR2(4) not null,/*按电话区号来区分*/
   LOCAL_CHANNEL_ID        VARCHAR2(512) not null,
   USER_ID                 NUMERIC(11) not null,
   CREATE_TIME             DATE    default SYSDATE,
   constraint PK_TWAP_PUBLIC_CHANNEL_MAPPING primary key (MAPPING_ID)
)
/*tablespace NX_DATA4*/;
create sequence SEQ_TWAP_PUBLIC_CHL_MAPPING ;

/*==============================================================*/
/* Table: TWAP_PUBLIC_CHANNEL_MAPPING_A  频道订阅 自动表          */
/*==============================================================*/
--drop table TWAP_PUBLIC_CHANNEL_MAPPING_A;
create table TWAP_PUBLIC_CHANNEL_MAPPING_A  (
   MAPPING_ID              NUMERIC(11) not null,
   CHANNEL_ID              NUMERIC(11) not null,
   LOCAL_CODE              VARCHAR2(4) not null,/*按电话区号来区分*/
   LOCAL_CHANNEL_ID        VARCHAR2(512) not null,
   USER_ID                 NUMERIC(11) not null,
   OPERATION               NUMERIC(11) default 1,/*1：写入，2：更新*/
   CREATE_TIME             DATE    default SYSDATE,
   constraint PK_CHANNEL_MAPPING_AUTO primary key (MAPPING_ID)
)
/*tablespace NX_DATA4*/;

/*==============================================================*/
/* Table: TWAP_PUBLIC_USER  用户表                              */
/*==============================================================*/
--drop table TWAP_PUBLIC_USER;
create table TWAP_PUBLIC_USER  (
   USER_ID                 NUMERIC(11) not null,
   USER_NAME               VARCHAR2(50) not null,
   USER_PASSWORD           VARCHAR2(50) not null,
   LOCAL_CODE              VARCHAR2(4) not null,/*按电话区号来区分*/
   USERT_STATUS            VARCHAR2(1) default '1' not null,
   CREATE_TIME             DATE    default SYSDATE,
   constraint PK_TWAP_PUBLIC_USER primary key (USER_ID)
)
/*tablespace NX_DATA4*/;
create sequence SEQ_TWAP_PUBLIC_USER ;

/*==============================================================*/
/* Table: TWAP_PUBLIC_FTP  FTP表                                */
/*==============================================================*/
create table TWAP_PUBLIC_FTP  (
   LOCAL_CODE              VARCHAR2(4) not null,/*按电话区号来区分*/
   FTP_HOST                 VARCHAR2(100) not null,
   FTP_USER              VARCHAR2(50) not null,/*按电话区号来区分*/
   FTP_PASSWORD        VARCHAR2(50) not null,
   FTP_DIR             VARCHAR2(512) not null,
   FTP_PORT            VARCHAR2(10)  default '21' not null,
   constraint PK_TWAP_PUBLIC_FTP primary key (LOCAL_CODE)
)
/*tablespace NX_DATA4*/;
