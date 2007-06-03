/***************************************************************
* File: tpteam_ddl.sql
* Description:  DDL statements for creating TPTeam schema
* Author: Bob Brady, rpbrady@gmail.com
* Date: 05Jun2007
****************************************************************/


create table TPTEAM.TOKEN (USER_ID integer not null, TOKEN_ID text not null, CREATED_DATE datetime, HOST_IP_ADDR text, primary key (USER_ID, TOKEN_ID(500)));
create table TPTEAM.JUNIT_TEST (ID integer not null, ECLIPSE_HOME text, WORKSPACE text, PROJECT text, REPORT_DIR text, TPTP_CONNECTION text, TEST_SUITE text, primary key (ID));
create table TPTEAM.PRODUCT (id integer not null auto_increment, NAME text, DESCRIPTION text, primary key (id), unique index PROD_NAME (NAME(512)));
create table TPTEAM.PROJECT (id integer not null auto_increment, PROD_ID integer, NAME text, DESCRIPTION text, primary key (id));
create table TPTEAM.PROJ_USER (USER_ID integer not null, PROJ_ID integer not null, primary key (PROJ_ID, USER_ID));
create table TPTEAM.ROLE (ROLE_ID integer not null auto_increment, NAME text, DESCRIPTION text, primary key (ROLE_ID));
create table TPTEAM.TEST (id integer not null auto_increment, PROJ_ID integer, TEST_TYPE_ID integer, CREATED_BY integer, MODIFIED_BY integer, PARENT_ID integer, NAME text, DESCRIPTION text, IS_FOLDER char(1), PATH text, CREATED_DATE datetime, MODIFIED_DATE datetime, primary key (id), unique index TEST_PATH (PATH(512)));
create table TPTEAM.TEST_EXECUTION (id integer not null auto_increment, RUN_BY integer, TEST_ID integer, STATUS char(1), EXEC_DATE datetime, COMMENTS text, primary key (id));
create table TPTEAM.TEST_TYPE (ID integer not null auto_increment, NAME text, primary key (ID));
create table TPTEAM.TPTEAM_USER (id integer not null auto_increment, ROLE_ID integer, USER_NAME text, FIRST_NAME text, LAST_NAME text, PASSWORD text, ECF_ID text, PHONE varchar(15), EMAIL text, CREATED_BY integer, CREATED_DATE datetime, MODIFIED_BY integer, MODIFIED_DATE datetime, primary key (id));
alter table TPTEAM.JUNIT_TEST add index FK28EF29635D877391 (ID), add constraint FK28EF29635D877391 foreign key (ID) references TPTEAM.TEST (id);
alter table TPTEAM.PROJECT add index FK185BD6F93F922B8E (PROD_ID), add constraint FK185BD6F93F922B8E foreign key (PROD_ID) references TPTEAM.PRODUCT (id);
alter table TPTEAM.PROJ_USER add index FK7238936D4A401337 (USER_ID), add constraint FK7238936D4A401337 foreign key (USER_ID) references TPTEAM.TPTEAM_USER (id);
alter table TPTEAM.PROJ_USER add index FK7238936D3F9763F2 (PROJ_ID), add constraint FK7238936D3F9763F2 foreign key (PROJ_ID) references TPTEAM.PROJECT (id);
alter table TPTEAM.TEST add index FK273C923F9763F2 (PROJ_ID), add constraint FK273C923F9763F2 foreign key (PROJ_ID) references TPTEAM.PROJECT (id);
alter table TPTEAM.TEST add index FK273C92667B5863 (TEST_TYPE_ID), add constraint FK273C92667B5863 foreign key (TEST_TYPE_ID) references TPTEAM.TEST_TYPE (ID);
alter table TPTEAM.TEST add index FK273C92F6FAB2B5 (MODIFIED_BY), add constraint FK273C92F6FAB2B5 foreign key (MODIFIED_BY) references TPTEAM.TPTEAM_USER (id);
alter table TPTEAM.TEST add index FK273C92D8EE1746 (PARENT_ID), add constraint FK273C92D8EE1746 foreign key (PARENT_ID) references TPTEAM.TEST (id);
alter table TPTEAM.TEST add index FK273C92A4A8CE36 (CREATED_BY), add constraint FK273C92A4A8CE36 foreign key (CREATED_BY) references TPTEAM.TPTEAM_USER (id);
alter table TPTEAM.TEST_EXECUTION add index FKCC0C438B1C31CBB3 (RUN_BY), add constraint FKCC0C438B1C31CBB3 foreign key (RUN_BY) references TPTEAM.TPTEAM_USER (id);
alter table TPTEAM.TEST_EXECUTION add index FKCC0C438B8BE725E (TEST_ID), add constraint FKCC0C438B8BE725E foreign key (TEST_ID) references TPTEAM.TEST (id);
alter table TPTEAM.TOKEN add index FK4C4C1D94A401337 (USER_ID), add constraint FK4C4C1D94A401337 foreign key (USER_ID) references TPTEAM.TPTEAM_USER (id);
alter table TPTEAM.TPTEAM_USER add index FKCF605571AF9837DE (ROLE_ID), add constraint FKCF605571AF9837DE foreign key (ROLE_ID) references TPTEAM.ROLE (ROLE_ID);
