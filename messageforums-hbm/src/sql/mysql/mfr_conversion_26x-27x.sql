
--////////////////////////////////////////////////////
--// SAK-11740
--// Email notification of new posts to forum
--////////////////////////////////////////////////////

CREATE TABLE `MFR_EMAIL_NOTIFICATION_T` (
  `ID` bigint(20) NOT NULL auto_increment,
  `VERSION` int(11) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `CONTEXT_ID` varchar(255) NOT NULL,
  `NOTIFICATION_LEVEL` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`)
);

 
CREATE INDEX MFR_EMAIL_USER_ID_I ON  MFR_EMAIL_NOTIFICATION_T(USER_ID);
CREATE INDEX  MFR_EMAIL_CONTEXT_ID_I ON  MFR_EMAIL_NOTIFICATION_T(CONTEXT_ID);


--////////////////////////////////////////////////////
--// SAK-15052
--// update cafe versions to 2.7.0-SNAPSHOT
--////////////////////////////////////////////////////

alter table MFR_MESSAGE_T add column THREADID bigint(20);
alter table MFR_MESSAGE_T add column LASTTHREADATE datetime;
alter table MFR_MESSAGE_T add column LASTTHREAPOST bigint(20);

update MFR_MESSAGE_T set THREADID=IN_REPLY_TO,LASTTHREADATE=CREATED;


--////////////////////////////////////////////////////
--// SAK-10869
--// Displaying all messages should mark them as read
--////////////////////////////////////////////////////

-- Add AutoMarkThreadsRead functionality to Message Center (SAK-10869)

-- add column to allow AutoMarkThreadsRead as template setting
alter table MFR_AREA_T add column (AUTO_MARK_THREADS_READ bit);
update MFR_AREA_T set AUTO_MARK_THREADS_READ=0 where AUTO_MARK_THREADS_READ is NULL;
alter table MFR_AREA_T modify column AUTO_MARK_THREADS_READ bit not null;

-- add column to allow AutoMarkThreadsRead to be set at the forum level
alter table MFR_OPEN_FORUM_T add column (AUTO_MARK_THREADS_READ bit);
update MFR_OPEN_FORUM_T set AUTO_MARK_THREADS_READ=0 where AUTO_MARK_THREADS_READ is NULL;
alter table MFR_OPEN_FORUM_T modify column AUTO_MARK_THREADS_READ bit not null;

-- add column to allow AutoMarkThreadsRead to be set at the topic level
alter table MFR_TOPIC_T add column (AUTO_MARK_THREADS_READ bit);
update MFR_TOPIC_T set AUTO_MARK_THREADS_READ=0 where AUTO_MARK_THREADS_READ is NULL;
alter table MFR_TOPIC_T modify column AUTO_MARK_THREADS_READ bit not null;


--////////////////////////////////////////////////////
--// SAK-10559
--// View who has read a message
--////////////////////////////////////////////////////

--Pending...


--////////////////////////////////////////////////////
--// SAK-15655
--// Rework MyWorkspace Synoptic view of Messages & Forums
--////////////////////////////////////////////////////


CREATE TABLE MFR_SYNOPTIC_ITEM ( 
    SYNOPTIC_ITEM_ID      	bigint(20) AUTO_INCREMENT NOT NULL,
    VERSION               	int(11) NOT NULL,
    USER_ID               	varchar(36) NOT NULL,
    SITE_ID               	varchar(99) NOT NULL,
    SITE_TITLE            	varchar(255) NULL,
    NEW_MESSAGES_COUNT    	int(11) NULL,
    MESSAGES_LAST_VISIT_DT	datetime NULL,
    NEW_FORUM_COUNT       	int(11) NULL,
    FORUM_LAST_VISIT_DT   	datetime NULL,
    HIDE_ITEM             	bit(1) NULL,
    PRIMARY KEY(SYNOPTIC_ITEM_ID)
);
ALTER TABLE MFR_SYNOPTIC_ITEM
    ADD CONSTRAINT USER_ID
	UNIQUE (USER_ID, SITE_ID);
CREATE UNIQUE INDEX USER_ID
    ON MFR_SYNOPTIC_ITEM(USER_ID, SITE_ID);



