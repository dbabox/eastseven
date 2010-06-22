--WAP_SITE_FOLDER_RESOURCE
drop table WAP_SITE_FOLDER_RESOURCE;
CREATE TABLE WAP_SITE_FOLDER_RESOURCE(
 FOLDER_RESOURCE_ID BIGINT NOT NULL,
 FOLDER_ID          BIGINT,
 WAPSITE_ID         BIGINT,
 PASSNAME           VARCHAR(128),
 SUBTITLE           VARCHAR(128),
 POST_DATE          TIMESTAMP,
 CHECK_DATE         TIMESTAMP,
 RES_AUTHOR         VARCHAR(64),
 AVAIL_DATE         TIMESTAMP,
 FILE_PATH          VARCHAR(512),
 RES_SIZE           BIGINT,
 KEYWORD            VARCHAR(512),
 RES_DESC           VARCHAR(256),
 BROWSE_COUNT       BIGINT,
 MODIFY_TIME        TIMESTAMP,
 STATUS             CHAR(1),
 TEMPLATE_ID        BIGINT,
 ISTURNTOPAGE       CHAR(1),
 FILE_NAME          VARCHAR(64),
 USER_ID            BIGINT,
 COMPONENT_XML_PATH VARCHAR(64),
 RES_ORDER          BIGINT,
 RESOURCE_LEVEL_ID  BIGINT,
 PARENT_RESOURCE    BIGINT,
 SYNC               VARCHAR(64),
 TOTALSCORES        BIGINT,
 TOTALNUM           BIGINT
);

--WAP_SITE_FILE_RESOURCE
drop table WAP_SITE_FILE_RESOURCE;
CREATE TABLE WAP_SITE_FILE_RESOURCE(
 FILE_RESOURCE_ID                                   BIGINT NOT NULL,
 FOLDER_ID                                          BIGINT,
 WAPSITE_ID                                         BIGINT,
 PASSNAME                                           VARCHAR(128),
 SUBTITLE                                           VARCHAR(128),
 POST_DATE                                          TIMESTAMP,
 CHECK_DATE                                         TIMESTAMP,
 RES_AUTHOR                                         VARCHAR(64),
 AVAIL_DATE                                         TIMESTAMP,
 FILE_PATH                                          VARCHAR(512),
 RES_SIZE                                           BIGINT,
 KEYWORD                                            VARCHAR(512),
 RES_DESC                                           VARCHAR(256),
 BROWSE_COUNT                                       BIGINT,
 MODIFY_TIME                                        TIMESTAMP,
 STATUS                                             CHAR(1),
 RES_TYPE                                           CHAR(3)
);