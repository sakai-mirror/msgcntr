package org.sakaiproject.api.app.messageforums.ui;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.MessageForumsMessageManager;
import org.sakaiproject.api.app.messageforums.PrivateForum;
import org.sakaiproject.api.app.messageforums.PrivateMessage;
import org.sakaiproject.api.app.messageforums.PrivateTopic;
import org.sakaiproject.api.app.messageforums.Topic;

public interface PrivateMessageManager {
    
    public static String SORT_COLUMN_SUBJECT = "title";
    public static String SORT_COLUMN_AUTHOR = "author";
    public static String SORT_COLUMN_DATE = "message.created";
    public static String SORT_COLUMN_LABEL = "label";
    public static String SORT_COLUMN_TO = "message.recipientsAsText";
    public static String SORT_COLUMN_ATTACHMENT = "message.hasAttachments";
    
    public static String SORT_ASC = "asc";
    public static String SORT_DESC = "desc";
    
    public String getContextId();
    
    public String getContextSiteId();
    
    public PrivateMessage initMessageWithAttachmentsAndRecipients(PrivateMessage msg);
    
    //public List getPvtMsgByIdWithAttachments(PrivateMessage msg);
    
    public void saveAreaAndForumSettings(Area area, PrivateForum forum);
    
    public void savePrivateMessageArea(Area area);
    
    public void saveForumSettings(PrivateForum forum);
    
    public void markMessageAsReadForUser(final PrivateMessage message);
    
    public List getMessagesByType(final String typeUuid, final String orderField,
        final String order);
    
    /**
     * check if private message area is enabled for the current user
     * @return boolean
     */
    boolean isPrivateAreaEnabled();

    /**
     * retrieve private message area if it is enabled for the current user
     * @return area
     */
    Area getPrivateMessageArea();
    
    public PrivateForum initializePrivateMessageArea(Area area);        
    
    public PrivateForum initializationHelper(PrivateForum forum);
    
    /** 
     * create private message with type
     * @param typeUuid
     * @return private message
     */
    public PrivateMessage createPrivateMessage(String typeUuid);
    
    /**
     * send private message to recipients
     * @param message
     * @param recipients
     * @param asEmail
     */
    public void sendPrivateMessage(PrivateMessage message, Set recipients, boolean asEmail);
    
    
    /**
     * mark message as deleted for user
     * @param message
     */
    public void deletePrivateMessage(PrivateMessage message, String typeUuid);

    /**
     * Save private message
     * @param message
     * @param recipients
     */
    void savePrivateMessage(Message message);
            
    /**
     * find message count for type
     * @param typeUuid
     * @return count
     */
    public int findMessageCount(String typeUuid);
    
    /**
     * find unread message count for type
     * @param typeUuid
     * @return count
     */
    public int findUnreadMessageCount(String typeUuid);
    
    public MessageForumsMessageManager getMessageManager();
    public Message getMessageById(Long messageId);
    
    public List getReceivedMessages(String orderField, String order);
    public List getSentMessages(String orderField, String order);
    public List getDeletedMessages(String orderField, String order);
    public List getDraftedMessages(String orderField, String order);    
    
    // will the below be helpful for displaying messages related to other mutable topics. 
    public List getMessagesByTopic(String userId, Long topicId);
    
    int getTotalNoMessages(Topic topic);
    int getUnreadNoMessages(Topic topic);    
    
    
    
    //Topic Folder Setting
    public boolean isMutableTopicFolder(String parentTopicId);
    
    /** create a folder in Private forum    */
    public void createTopicFolderInForum(PrivateForum pf, String foderName);
    /** rename folder title     */
    public void renameTopicFolder(PrivateForum pf,String topicId, String newName);
    /**  Delete a topic from private forum   */
    public void deleteTopicFolder(PrivateForum pf, String topicId);
    /** Move the message under new topic */
    public void movePvtMsgTopic(PrivateMessage message,Topic oldTopic, Topic newTopic);
    /**  Get the Topic from uuid   
     * TODO - this can be replaced with another method which doesn't return messages*/
    public Topic getTopicByUuid(final String topicUuid);
    /** create Folder within Folder*/
    public void createTopicFolderInTopic(PrivateForum pf, PrivateTopic parentTopic, String folderName);
    
    
    //Attachment
    public void addAttachToPvtMsg(final PrivateMessage pvtMsgData, final Attachment pvtMsgAttach);
    public Attachment createPvtMsgAttachment(String attachId, String name);
    public void removePvtMsgAttachment(Attachment o);
    public Attachment getPvtMsgAttachment(final Long pvtMsgAttachId);
    
    
    public boolean isInstructor();
    
    public PrivateMessage getNextMessage(PrivateMessage message);
    public PrivateMessage getPreviousMessage(PrivateMessage message);
    public boolean hasPreviousMessage(PrivateMessage message);
    public boolean hasNextMessage(PrivateMessage message);
    
    /** advanced search for private messages */ 
    public List searchPvtMsgs(String typeUuid, String searchText,Date searchFromDate, Date searchToDate, Long searchByText, Long searchByAuthor,Long searchByBody, Long searchByLabel,Long searchByDate);
    
}
