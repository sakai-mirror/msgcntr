-- MSGCNTR-241 

DROP SEQUENCE MFR_MOVE_HISTORY_S;
DROP INDEX MFR_MOVE_HISTORY_MESSAGE_I;
DROP TABLE MFR_MOVE_HISTORY_T;

create table MFR_MOVE_HISTORY_T (
   ID number(19,0) not null,
   UUID varchar2(36) not null,
   VERSION number(10,0) not null,
   TO_TOPIC_ID number(19,0) not null,
   FROM_TOPIC_ID number(19,0) not null,
   MESSAGE_ID number(19,0) not null,
   REMINDER number(1,0) null,
   CREATED_BY varchar2(36) not null,
   CREATED date not null,
   MODIFIED_BY varchar2(36) not null,
   MODIFIED date not null,
   primary key (ID)
);
create sequence MFR_MOVE_HISTORY_S;
create index MFR_MOVE_HISTORY_MESSAGE_I on MFR_MOVE_HISTORY_T (MESSAGE_ID);
