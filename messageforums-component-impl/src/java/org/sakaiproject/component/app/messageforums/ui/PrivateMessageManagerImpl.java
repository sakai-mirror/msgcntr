/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-component-impl/src/java/org/sakaiproject/component/app/messageforums/ui/PrivateMessageManagerImpl.java $
 * $Id: PrivateMessageManagerImpl.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.component.app.messageforums.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.AreaManager;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.MessageForumsForumManager;
import org.sakaiproject.api.app.messageforums.MessageForumsMessageManager;
import org.sakaiproject.api.app.messageforums.MessageForumsTypeManager;
import org.sakaiproject.api.app.messageforums.PrivateForum;
import org.sakaiproject.api.app.messageforums.PrivateMessage;
import org.sakaiproject.api.app.messageforums.PrivateMessageRecipient;
import org.sakaiproject.api.app.messageforums.PrivateTopic;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.UniqueArrayList;
import org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.component.app.messageforums.TestUtil;
import org.sakaiproject.component.app.messageforums.dao.hibernate.PrivateMessageImpl;
import org.sakaiproject.component.app.messageforums.dao.hibernate.PrivateMessageRecipientImpl;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PrivateMessageManagerImpl extends HibernateDaoSupport implements
    PrivateMessageManager
{

  private static final Log LOG = LogFactory
      .getLog(PrivateMessageManagerImpl.class);

  private static final String QUERY_AGGREGATE_COUNT = "findAggregatePvtMsgCntForUserInContext";  
  private static final String QUERY_MESSAGES_BY_USER_TYPE_AND_CONTEXT = "findPrvtMsgsByUserTypeContext";
  private static final String QUERY_MESSAGES_BY_ID_WITH_RECIPIENTS = "findPrivateMessageByIdWithRecipients";
  
  private static List aggregateList;

  private AreaManager areaManager;
  private MessageForumsMessageManager messageManager;
  private MessageForumsForumManager forumManager;
  private MessageForumsTypeManager typeManager;
  private IdManager idManager;
  private SessionManager sessionManager;  
  private EmailService emailService;
  

  public void init()
  {
    ;
  }

  public boolean getPrivateAreaEnabled()
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("getPrivateAreaEnabled()");
    }
        
    return areaManager.isPrivateAreaEnabled();

  }

  public void setPrivateAreaEnabled(boolean value)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("setPrivateAreaEnabled(value: " + value + ")");
    }

  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#isPrivateAreaEnabled()
   */
  public boolean isPrivateAreaEnabled()
  {
    return areaManager.isPrivateAreaEnabled();
  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#getPrivateMessageArea()
   */
  public Area getPrivateMessageArea()
  {
    return areaManager.getPrivateArea();    
  }

  public void savePrivateMessageArea(Area area)
  {
    areaManager.saveArea(area);
  }

  
  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#initializePrivateMessageArea(org.sakaiproject.api.app.messageforums.Area)
   */
  public PrivateForum initializePrivateMessageArea(Area area)
  {
    String userId = getCurrentUser();
    
    initializeMessageCounts();
    
    getHibernateTemplate().lock(area, LockMode.NONE);
    
    PrivateForum pf;

    /** create default user forum/topics if none exist */
    if ((pf = forumManager.getPrivateForumByOwnerArea(getCurrentUser(), area)) == null)
    {      
      /** initialize collections */
      //getHibernateTemplate().initialize(area.getPrivateForumsSet());
            
      pf = forumManager.createPrivateForum("Private Messages");
      
      //area.addPrivateForum(pf);
      //pf.setArea(area);
      //areaManager.saveArea(area);
      
      PrivateTopic receivedTopic = forumManager.createPrivateForumTopic("Received", true,false,
          userId, pf.getId());     

      PrivateTopic sentTopic = forumManager.createPrivateForumTopic("Sent", true,false,
          userId, pf.getId());      

      PrivateTopic deletedTopic = forumManager.createPrivateForumTopic("Deleted", true,false,
          userId, pf.getId());      

      //PrivateTopic draftTopic = forumManager.createPrivateForumTopic("Drafts", true,false,
      //    userId, pf.getId());
    
      /** save individual topics - required to add to forum's topic set */
      forumManager.savePrivateForumTopic(receivedTopic);
      forumManager.savePrivateForumTopic(sentTopic);
      forumManager.savePrivateForumTopic(deletedTopic);
      //forumManager.savePrivateForumTopic(draftTopic);
      
      pf.addTopic(receivedTopic);
      pf.addTopic(sentTopic);
      pf.addTopic(deletedTopic);
      //pf.addTopic(draftTopic);
      pf.setArea(area);  
      
      PrivateForum oldForum;
      if ((oldForum = forumManager.getPrivateForumByOwnerAreaNull(getCurrentUser())) != null)
      {
    		oldForum = initializationHelper(oldForum);
//    		getHibernateTemplate().initialize(oldForum.getTopicsSet());
    		List pvtTopics = oldForum.getTopics();
    		
    		for(int i=0; i<pvtTopics.size(); i++)
    		{
    			PrivateTopic currentTopic = (PrivateTopic) pvtTopics.get(i);
    			if(currentTopic != null)
    			{
    				if(!currentTopic.getTitle().equals("Received") && !currentTopic.getTitle().equals("Sent") && !currentTopic.getTitle().equals("Deleted") 
    						&& !currentTopic.getTitle().equals("Drafts") && area.getContextId().equals(currentTopic.getContextId()))
    				{
    					currentTopic.setPrivateForum(pf);
    		      forumManager.savePrivateForumTopic(currentTopic);
    					pf.addTopic(currentTopic);
    				}
    			}
    		}
      }
      
      forumManager.savePrivateForum(pf);            
      
    }    
    else{      
       getHibernateTemplate().initialize(pf.getTopicsSet());              
    }
   
    return pf;
  }
  
  public PrivateForum initializationHelper(PrivateForum forum){
    
    /** reget to load topic foreign keys */
  	PrivateForum pf = forumManager.getPrivateForumByOwnerAreaNull(getCurrentUser());
    getHibernateTemplate().initialize(pf.getTopicsSet());    
    return pf;
  }

  public PrivateForum initializationHelper(PrivateForum forum, Area area){
    
    /** reget to load topic foreign keys */
  	PrivateForum pf = forumManager.getPrivateForumByOwnerArea(getCurrentUser(), area);
    getHibernateTemplate().initialize(pf.getTopicsSet());    
    return pf;
  }
  

  
  

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#savePrivateMessage(org.sakaiproject.api.app.messageforums.Message)
   */
  public void savePrivateMessage(Message message)
  {
    messageManager.saveMessage(message);
  }

  public Message getMessageById(Long id)
  {
    return messageManager.getMessageById(id);
  }

  //Attachment
  public Attachment createPvtMsgAttachment(String attachId, String name)
  {
    try
    {
      Attachment attach = messageManager.createAttachment();

      attach.setAttachmentId(attachId);

      attach.setAttachmentName(name);

      ContentResource cr = ContentHostingService.getResource(attachId);
      attach.setAttachmentSize((new Integer(cr.getContentLength())).toString());
      attach.setCreatedBy(cr.getProperties().getProperty(
          cr.getProperties().getNamePropCreator()));
      attach.setModifiedBy(cr.getProperties().getProperty(
          cr.getProperties().getNamePropModifiedBy()));
      attach.setAttachmentType(cr.getContentType());
      String tempString = cr.getUrl();
      String newString = new String();
      char[] oneChar = new char[1];
      for (int i = 0; i < tempString.length(); i++)
      {
        if (tempString.charAt(i) != ' ')
        {
          oneChar[0] = tempString.charAt(i);
          String concatString = new String(oneChar);
          newString = newString.concat(concatString);
        }
        else
        {
          newString = newString.concat("%20");
        }
      }
      //tempString.replaceAll(" ", "%20");
      //attach.setAttachmentUrl(newString);
      attach.setAttachmentUrl("");

      return attach;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  // Himansu: I am not quite sure this is what you want... let me know.
  // Before saving a message, we need to add all the attachmnets to a perticular message
  public void addAttachToPvtMsg(PrivateMessage pvtMsgData,
      Attachment pvtMsgAttach)
  {
    pvtMsgData.addAttachment(pvtMsgAttach);
  }

  // Required for editing multiple attachments to a message. 
  // When you reply to a message, you do have option to edit attachments to a message
  public void removePvtMsgAttachment(Attachment o)
  {
    o.getMessage().removeAttachment(o);
  }

  public Attachment getPvtMsgAttachment(Long pvtMsgAttachId)
  {
    return messageManager.getAttachmentById(pvtMsgAttachId);
  }

  public int getTotalNoMessages(Topic topic)
  {
    return messageManager.findMessageCountByTopicId(topic.getId());
  }

  public int getUnreadNoMessages(Topic topic)
  {
    return messageManager.findUnreadMessageCountByTopicId(topic.getId());
  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#saveAreaAndForumSettings(org.sakaiproject.api.app.messageforums.Area, org.sakaiproject.api.app.messageforums.PrivateForum)
   */
  public void saveAreaAndForumSettings(Area area, PrivateForum forum)
  {

    /** method calls placed in this function to participate in same transaction */
           
    saveForumSettings(forum);

    /** need to evict forum b/c area saves fk on forum (which places two objects w/same id in session */
    //getHibernateTemplate().evict(forum);
    
    if (isInstructor()){
      savePrivateMessageArea(area);
    }
  }

  public void saveForumSettings(PrivateForum forum)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("saveForumSettings(forum: " + forum + ")");
    }

    if (forum == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }

    forumManager.savePrivateForum(forum);
  }

  /**
   * Topic Folder Setting
   */
  public boolean isMutableTopicFolder(String parentTopicId)
  {
    return false;
  }

  
  public void createTopicFolderInForum(PrivateForum pf, String folderName)
  {
    String userId = getCurrentUser();
    PrivateTopic createdTopic = forumManager.createPrivateForumTopic(folderName, true,true,
        userId, pf.getId()); 
    
    /** set context and type to differentiate user topics within sites */
    createdTopic.setContextId(getContextId());
    createdTopic.setTypeUuid(typeManager.getUserDefinedPrivateTopicType());
    
    forumManager.savePrivateForumTopic(createdTopic);
    pf.addTopic(createdTopic);   
    forumManager.savePrivateForum(pf);  
  }

  public void createTopicFolderInTopic(PrivateForum pf, PrivateTopic parentTopic, String folderName)
  {
    String userId = getCurrentUser();
    PrivateTopic createdTopic = forumManager.createPrivateForumTopic(folderName, true,true,
        userId, pf.getId()); 
    createdTopic.setParentTopic(parentTopic);
    forumManager.savePrivateForumTopic(createdTopic);
    pf.addTopic(createdTopic);    
    forumManager.savePrivateForum(pf);  
  }

  public void renameTopicFolder(PrivateForum pf, String topicUuid, String newName)
  {
    String userId = getCurrentUser();
    List pvtTopics= pf.getTopics();
    for (Iterator iter = pvtTopics.iterator(); iter.hasNext();)
    {
      PrivateTopic element = (PrivateTopic) iter.next();
      if(element.getUuid().equals(topicUuid))
      {
        element.setTitle(newName);
        element.setModifiedBy(userId);
        element.setModified(new Date());
      }      
    }
    forumManager.savePrivateForum(pf);  
  }

  public void deleteTopicFolder(PrivateForum pf,String topicUuid)
  {
    List pvtTopics= pf.getTopics();
    for (Iterator iter = pvtTopics.iterator(); iter.hasNext();)
    {
      PrivateTopic element = (PrivateTopic) iter.next();
      if(element.getUuid().equals(topicUuid))
      {
        pf.removeTopic(element);
        break;
      }
    }
    forumManager.savePrivateForum(pf);  
  }

  /**
   * Return Topic based on uuid 
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#getTopicByIdWithMessages(java.lang.Long)
   */
  public Topic getTopicByUuid(final String topicUuid)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("getTopicByIdWithMessages(final Long" + topicUuid + ")");
    }
    return forumManager.getTopicByUuid(topicUuid);
  }
  
  
  public static final String PVTMSG_MODE_RECEIVED = "Received";
  public static final String PVTMSG_MODE_SENT = "Sent";
  public static final String PVTMSG_MODE_DELETE = "Deleted";
  public static final String PVTMSG_MODE_DRAFT = "Drafts";
  public void movePvtMsgTopic(PrivateMessage message, Topic oldTopic, Topic newTopic)
  {
    List recipients= message.getRecipients();
    //get new topic type uuid
    String newTopicTypeUuid=getTopicTypeUuid(newTopic.getTitle());
    //get pld topic type uuid
    String oldTopicTypeUuid=getTopicTypeUuid(oldTopic.getTitle());
    
    //now set the recipiant with new topic type uuid
    for (Iterator iter = recipients.iterator(); iter.hasNext();)
    {
      PrivateMessageRecipient element = (PrivateMessageRecipient) iter.next();
      if (element.getTypeUuid().equals(oldTopicTypeUuid) && (element.getUserId().equals(getCurrentUser())))
      {
        element.setTypeUuid(newTopicTypeUuid);
      }
    }
    savePrivateMessage(message);
    
  }
  
  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#createPrivateMessage(java.lang.String)
   */
  public PrivateMessage createPrivateMessage(String typeUuid)
  {
    PrivateMessage message = new PrivateMessageImpl();
    message.setUuid(idManager.createUuid());
    message.setTypeUuid(typeUuid);
    message.setCreated(new Date());
    message.setCreatedBy(getCurrentUser());

    LOG.info("message " + message.getUuid() + " created successfully");
    return message;
  }

  public boolean hasNextMessage(PrivateMessage message)
  {
    // TODO: Needs optimized
    boolean next = false;
    if (message != null && message.getTopic() != null
        && message.getTopic().getMessages() != null)
    {
      for (Iterator iter = message.getTopic().getMessages().iterator(); iter
          .hasNext();)
      {
        Message m = (Message) iter.next();
        if (next)
        {
          return true;
        }
        if (m.getId().equals(message.getId()))
        {
          next = true;
        }
      }
    }

    // if we get here, there is no next message
    return false;
  }

  public boolean hasPreviousMessage(PrivateMessage message)
  {
    // TODO: Needs optimized
    PrivateMessage prev = null;
    if (message != null && message.getTopic() != null
        && message.getTopic().getMessages() != null)
    {
      for (Iterator iter = message.getTopic().getMessages().iterator(); iter
          .hasNext();)
      {
        Message m = (Message) iter.next();
        if (m.getId().equals(message.getId()))
        {
          // need to check null because we might be on the first message
          // which means there is no previous one
          return prev != null;
        }
        prev = (PrivateMessage) m;
      }
    }

    // if we get here, there is no previous message
    return false;
  }

  public PrivateMessage getNextMessage(PrivateMessage message)
  {
    // TODO: Needs optimized
    boolean next = false;
    if (message != null && message.getTopic() != null
        && message.getTopic().getMessages() != null)
    {
      for (Iterator iter = message.getTopic().getMessages().iterator(); iter
          .hasNext();)
      {
        Message m = (Message) iter.next();
        if (next)
        {
          return (PrivateMessage) m;
        }
        if (m.getId().equals(message.getId()))
        {
          next = true;
        }
      }
    }

    // if we get here, there is no next message
    return null;
  }

  public PrivateMessage getPreviousMessage(PrivateMessage message)
  {
    // TODO: Needs optimized
    PrivateMessage prev = null;
    if (message != null && message.getTopic() != null
        && message.getTopic().getMessages() != null)
    {
      for (Iterator iter = message.getTopic().getMessages().iterator(); iter
          .hasNext();)
      {
        Message m = (Message) iter.next();
        if (m.getId().equals(message.getId()))
        {
          return prev;
        }
        prev = (PrivateMessage) m;
      }
    }

    // if we get here, there is no previous message
    return null;
  }

  public List getMessagesByTopic(String userId, Long topicId)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public List getReceivedMessages(String orderField, String order)
  {
    return getMessagesByType(typeManager.getReceivedPrivateMessageType(),
        orderField, order);
  }

  public List getSentMessages(String orderField, String order)
  {
    return getMessagesByType(typeManager.getSentPrivateMessageType(),
        orderField, order);    
  }

  public List getDeletedMessages(String orderField, String order)
  {
    return getMessagesByType(typeManager.getDeletedPrivateMessageType(),
        orderField, order);
  }

  public List getDraftedMessages(String orderField, String order)
  {
    return getMessagesByType(typeManager.getDraftPrivateMessageType(),
        orderField, order);
  }
    
  public PrivateMessage initMessageWithAttachmentsAndRecipients(PrivateMessage msg){
    
    PrivateMessage pmReturn = (PrivateMessage) messageManager.getMessageByIdWithAttachments(msg.getId());    
    getHibernateTemplate().initialize(pmReturn.getRecipients());
    return pmReturn;
  }
  
  /**
   * helper method to get messages by type
   * @param typeUuid
   * @return message list
   */
  public List getMessagesByType(final String typeUuid, final String orderField,
      final String order)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("getMessagesByType(typeUuid:" + typeUuid + ", orderField: "
          + orderField + ", order:" + order + ")");
    }

    //    HibernateCallback hcb = new HibernateCallback() {
    //      public Object doInHibernate(Session session) throws HibernateException, SQLException {
    //        Criteria messageCriteria = session.createCriteria(PrivateMessageImpl.class);
    //        Criteria recipientCriteria = messageCriteria.createCriteria("recipients");
    //        
    //        Conjunction conjunction = Expression.conjunction();
    //        conjunction.add(Expression.eq("userId", getCurrentUser()));
    //        conjunction.add(Expression.eq("typeUuid", typeUuid));        
    //        
    //        recipientCriteria.add(conjunction);
    //        
    //        if ("asc".equalsIgnoreCase(order)){
    //          messageCriteria.addOrder(Order.asc(orderField));
    //        }
    //        else if ("desc".equalsIgnoreCase(order)){
    //          messageCriteria.addOrder(Order.desc(orderField));
    //        }
    //        else{
    //          LOG.debug("getMessagesByType failed with (typeUuid:" + typeUuid + ", orderField: " + orderField +
    //              ", order:" + order + ")");
    //          throw new IllegalArgumentException("order must have value asc or desc");          
    //        }
    //        
    //        //todo: parameterize fetch mode
    //        messageCriteria.setFetchMode("recipients", FetchMode.EAGER);
    //        messageCriteria.setFetchMode("attachments", FetchMode.EAGER);
    //        
    //        return messageCriteria.list();        
    //      }
    //    };

    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_MESSAGES_BY_USER_TYPE_AND_CONTEXT);
        Query qOrdered = session.createQuery(q.getQueryString() + " order by "
            + orderField + " " + order);

        qOrdered.setParameter("userId", getCurrentUser(), Hibernate.STRING);
        qOrdered.setParameter("typeUuid", typeUuid, Hibernate.STRING);
        qOrdered.setParameter("contextId", getContextId(), Hibernate.STRING);
        return qOrdered.list();
      }
    };

    return (List) getHibernateTemplate().execute(hcb);        
  }
  
  /**
   * FOR SYNOPTIC TOOL:
   * 	helper method to get messages by type
   * 	needed to pass contextId since could be in MyWorkspace
   * 
   * @param typeUuid
   * 			The type of forum it is (Private or Topic)
   * @param contextId
   * 			The site id whose messages are needed
   * 
   * @return message list
   */
  public List getMessagesByTypeByContext(final String typeUuid, final String contextId)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("getMessagesByTypeForASite(typeUuid:" + typeUuid + ")");
    }

    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_MESSAGES_BY_USER_TYPE_AND_CONTEXT);

        q.setParameter("userId", getCurrentUser(), Hibernate.STRING);
        q.setParameter("typeUuid", typeUuid, Hibernate.STRING);
        q.setParameter("contextId", contextId, Hibernate.STRING);
        return q.list();
      }
    };

    return (List) getHibernateTemplate().execute(hcb);        
  }
  

    /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#findMessageCount(java.lang.String)
   */
  public int findMessageCount(String typeUuid)
  {    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("findMessageCount executing with typeUuid: " + typeUuid);
    }

    if (typeUuid == null)
    {
      LOG.error("findMessageCount failed with typeUuid: " + typeUuid);
      throw new IllegalArgumentException("Null Argument");
    }    
    
    if (aggregateList == null)
    {
      LOG.error("findMessageCount failed with aggregateList: " + aggregateList);
      throw new IllegalStateException("aggregateList is null");
    }
    
    int totalCount = 0;
    for (Iterator i = aggregateList.iterator(); i.hasNext();){
      Object[] element = (Object[]) i.next();
      /** filter on type */
      if (typeUuid.equals(element[1])){        
        /** add read/unread message types */        
        totalCount += ((Integer) element[2]).intValue(); 
      }      
    }
            
    return totalCount;
  }
  
  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#findUnreadMessageCount(java.lang.String)
   */
  public int findUnreadMessageCount(String typeUuid)
  {    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("findUnreadMessageCount executing with typeUuid: " + typeUuid);
    }

    if (typeUuid == null)
    {
      LOG.error("findUnreadMessageCount failed with typeUuid: " + typeUuid);
      throw new IllegalArgumentException("Null Argument");
    }    
    
    if (aggregateList == null)
    {
      LOG.error("findMessageCount failed with aggregateList: " + aggregateList);
      throw new IllegalStateException("aggregateList is null");
    }
    
    int unreadCount = 0;
    for (Iterator i = aggregateList.iterator(); i.hasNext();){
      Object[] element = (Object[]) i.next();
      /** filter on type and read status*/
      if (!typeUuid.equals(element[1]) || Boolean.TRUE.equals(element[0])){
        continue;
      }
      else{        
        unreadCount = ((Integer) element[2]).intValue();
        break;
      }      
    }
            
    return unreadCount;    
  }
  
  /**
   * initialize message counts
   * @param typeUuid
   */
  private void initializeMessageCounts()
  {    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("initializeMessageCounts executing");
    }

    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_AGGREGATE_COUNT);        
        q.setParameter("contextId", getContextId(), Hibernate.STRING);
        q.setParameter("userId", getCurrentUser(), Hibernate.STRING);
        return q.list();
      }
    };
        
    aggregateList = (List) getHibernateTemplate().execute(hcb);        
  }


  /**
   * FOR SYNOPTIC TOOL:
   * 	Returns a list of all sites this user is in along with a count of his/her
   * 	unread messages
   * 
   * @return
   * 	List of site id, count of unread message pairs
   */
  public List getPrivateMessageCountsForAllSites() {
	  HibernateCallback hcb = new HibernateCallback() {
		  public Object doInHibernate(Session session) throws HibernateException,
	  	 	SQLException
	  	 {
			  Query q = session.getNamedQuery("findUnreadPvtMsgCntByUserForAllSites");
			  q.setParameter("userId", getCurrentUser(), Hibernate.STRING);
			  return q.list();
	  	 }
	  };
  
	  return (List) getHibernateTemplate().execute(hcb);
	  
  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#deletePrivateMessage(org.sakaiproject.api.app.messageforums.PrivateMessage, java.lang.String)
   */
  public void deletePrivateMessage(PrivateMessage message, String typeUuid)
  {

    String userId = getCurrentUser();

    if (LOG.isDebugEnabled())
    {
      LOG.debug("deletePrivateMessage(message:" + message + ", typeUuid:"
          + typeUuid + ")");
    }

    /** fetch recipients for message */
    PrivateMessage pvtMessage = getPrivateMessageWithRecipients(message);

    /**
     *  create PrivateMessageRecipient to search
     */
    PrivateMessageRecipient pmrReadSearch = new PrivateMessageRecipientImpl(
        userId, typeUuid, getContextId(), Boolean.TRUE);

    PrivateMessageRecipient pmrNonReadSearch = new PrivateMessageRecipientImpl(
        userId, typeUuid, getContextId(), Boolean.FALSE);

    int indexDelete = -1;
    int indexRead = pvtMessage.getRecipients().indexOf(pmrReadSearch);
    if (indexRead != -1)
    {
      indexDelete = indexRead;
    }
    else
    {
      int indexNonRead = pvtMessage.getRecipients().indexOf(pmrNonReadSearch);
      if (indexNonRead != -1)
      {
        indexDelete = indexNonRead;
      }
      else
      {
        LOG
            .error("deletePrivateMessage -- cannot find private message for user: "
                + userId + ", typeUuid: " + typeUuid);
      }
    }

    if (indexDelete != -1)
    {
      PrivateMessageRecipient pmrReturned = (PrivateMessageRecipient) pvtMessage
          .getRecipients().get(indexDelete);

      if (pmrReturned != null)
      {

        /** check for existing deleted message from user */
        PrivateMessageRecipient pmrDeletedSearch = new PrivateMessageRecipientImpl(
            userId, typeManager.getDeletedPrivateMessageType(), getContextId(),
            Boolean.TRUE);

        int indexDeleted = pvtMessage.getRecipients().indexOf(pmrDeletedSearch);

        if (indexDeleted == -1)
        {
          pmrReturned.setRead(Boolean.TRUE);
          pmrReturned.setTypeUuid(typeManager.getDeletedPrivateMessageType());
        }
        else
        {
          pvtMessage.getRecipients().remove(indexDelete);
        }
      }
    }
  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#sendPrivateMessage(org.sakaiproject.api.app.messageforums.PrivateMessage, java.util.Set, boolean)
   */
  public void sendPrivateMessage(PrivateMessage message, Set recipients, boolean asEmail)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("sendPrivateMessage(message: " + message + ", recipients: "
          + recipients + ")");
    }

    if (message == null || recipients == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }

    if (recipients.size() == 0)
    {
      /** for no just return out
        throw new IllegalArgumentException("Empty recipient list");
      **/
      return;
    }

    String currentUserAsString = getCurrentUser();
    List recipientList = new UniqueArrayList();

    /** test for draft message */
    if (message.getDraft().booleanValue())
    {
      PrivateMessageRecipientImpl receiver = new PrivateMessageRecipientImpl(
      		currentUserAsString, typeManager.getDraftPrivateMessageType(),
          getContextId(), Boolean.TRUE);

      recipientList.add(receiver);
      message.setRecipients(recipientList);
      savePrivateMessage(message);
      return;
    }

    for (Iterator i = recipients.iterator(); i.hasNext();)
    {
      User u = (User) i.next();      
      String userId = u.getId();
      
      /** determine if recipient has forwarding enabled */
      Area currentArea = getAreaByContextIdAndTypeId(typeManager.getPrivateMessageAreaType());
      PrivateForum pf = forumManager.getPrivateForumByOwnerArea(userId, currentArea);
      
      boolean forwardingEnabled = false;
      
      if (pf != null && pf.getAutoForward().booleanValue()){
        forwardingEnabled = true;
      }
      
      List additionalHeaders = new ArrayList(1);
      additionalHeaders.add("Content-Type: text/html");
      

      User currentUser = UserDirectoryService.getCurrentUser();
      StringBuffer body = new StringBuffer(message.getBody());
      
      body.insert(0, "From: " + currentUser.getDisplayName() + "<p/>"); 
      
      // need to filter out hidden users if there are any and:
      //   a non-instructor (! site.upd)
      //   instructor but not the author
      String sendToString = message.getRecipientsAsText();
      if (sendToString.indexOf("(") > 0 && (! isInstructor() || (message.getAuthor() != getAuthorString())) ) {
    	  sendToString = sendToString.substring(0, sendToString.indexOf("("));
      }
      
      body.insert(0, "To: " + sendToString + "<p/>");
      
      if (message.getAttachments() != null && message.getAttachments().size() > 0) {
                           
          body.append("<br/><br/>");
          for (Iterator iter = message.getAttachments().iterator(); iter.hasNext();) {
            Attachment attachment = (Attachment) iter.next();
            //body.append("<a href=\"" + attachment.getAttachmentUrl() +
                        //"\">" + attachment.getAttachmentName() + "</a><br/>");            
            body.append("<a href=\"" + messageManager.getAttachmentUrl(attachment.getAttachmentId()) +
                "\">" + attachment.getAttachmentName() + "</a><br/>");            
          }
      }
      
      String siteTitle = null;
      try{
        siteTitle = SiteService.getSite(getContextId()).getTitle();
      }
      catch (IdUnusedException e){
        LOG.error(e.getMessage(), e);
      }
      
      String thisPageId = "";
      ToolSession ts = sessionManager.getCurrentToolSession();
  	  if (ts != null)
	  {
  	    ToolConfiguration tool = SiteService.findTool(ts.getPlacementId());
  	    if (tool != null)
  	    {
  	      thisPageId = tool.getPageId();
  	    }
	  }

      String footer = "<p>----------------------<br>" +
                      "This forwarded message was sent via " + ServerConfigurationService.getString("ui.service") +  
                      " Message Center from the \"" +
                      siteTitle + "\" site.\n" +
                      "To reply to this message click this link to access Message Center for this site:" +
                      " <a href=\"" +
                      ServerConfigurationService.getPortalUrl() + 
                      "/site/" + ToolManager.getCurrentPlacement().getContext() +
                      "/page/" + thisPageId+
                      "\">";
                                            
      footer += siteTitle + "</a>.</p>";                      
      body.append(footer);

      String bodyString = body.toString();
      
      /** determine if current user is equal to recipient */
      Boolean isRecipientCurrentUser = 
        (currentUserAsString.equals(userId) ? Boolean.TRUE : Boolean.FALSE);      
      
      if (!asEmail && forwardingEnabled){
          emailService.send(u.getEmail(), pf.getAutoForwardEmail(), message.getTitle(), 
              bodyString, u.getEmail(), null, additionalHeaders);
          
          // use forwarded address if set
          
          PrivateMessageRecipientImpl receiver = new PrivateMessageRecipientImpl(
              userId, typeManager.getReceivedPrivateMessageType(), getContextId(),
              isRecipientCurrentUser);
          recipientList.add(receiver);                    
      }      
      else if (asEmail){
        emailService.send(u.getEmail(), u.getEmail(), message.getTitle(), 
            bodyString, u.getEmail(), null, additionalHeaders);
      }      
      else{        
        PrivateMessageRecipientImpl receiver = new PrivateMessageRecipientImpl(
            userId, typeManager.getReceivedPrivateMessageType(), getContextId(),
            isRecipientCurrentUser);
        recipientList.add(receiver);
      }
    }

    /** add sender as a saved recipient */
    PrivateMessageRecipientImpl sender = new PrivateMessageRecipientImpl(
    		currentUserAsString, typeManager.getSentPrivateMessageType(),
        getContextId(), Boolean.TRUE);

    recipientList.add(sender);

    message.setRecipients(recipientList);

    savePrivateMessage(message);

    /** enable if users are stored in message forums user table
     Iterator i = recipients.iterator();
     while (i.hasNext()){
     String userId = (String) i.next();
     
     //getForumUser will create user if forums user does not exist
     message.addRecipient(userManager.getForumUser(userId.trim()));
     }
     **/

  }

  /**
   * @see org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager#markMessageAsReadForUser(org.sakaiproject.api.app.messageforums.PrivateMessage)
   */
  public void markMessageAsReadForUser(final PrivateMessage message)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("markMessageAsReadForUser(message: " + message + ")");
    }

    if (message == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }

    final String userId = getCurrentUser();

    /** fetch recipients for message */
    PrivateMessage pvtMessage = getPrivateMessageWithRecipients(message);

    /** create PrivateMessageRecipientImpl to search for recipient to update */
    PrivateMessageRecipientImpl searchRecipient = new PrivateMessageRecipientImpl(
        userId, typeManager.getReceivedPrivateMessageType(), getContextId(),
        Boolean.FALSE);

    List recipientList = pvtMessage.getRecipients();

    if (recipientList == null || recipientList.size() == 0)
    {
      LOG.error("markMessageAsReadForUser(message: " + message
          + ") has empty recipient list");
      throw new Error("markMessageAsReadForUser(message: " + message
          + ") has empty recipient list");
    }

    int recordIndex = pvtMessage.getRecipients().indexOf(searchRecipient);

    if (recordIndex != -1)
    {
      ((PrivateMessageRecipientImpl) recipientList.get(recordIndex))
          .setRead(Boolean.TRUE);
    }
  }

  /**
   * FOR SYNOPTIC TOOL:
   * 	Need to pass in contextId also
   */
  public void markMessageAsReadForUser(final PrivateMessage message, final String contextId)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("markMessageAsReadForUser(message: " + message + ")");
    }

    if (message == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }

    final String userId = getCurrentUser();

    /** fetch recipients for message */
    PrivateMessage pvtMessage = getPrivateMessageWithRecipients(message);

    /** create PrivateMessageRecipientImpl to search for recipient to update */
    PrivateMessageRecipientImpl searchRecipient = new PrivateMessageRecipientImpl(
        userId, typeManager.getReceivedPrivateMessageType(), contextId,
        Boolean.FALSE);

    List recipientList = pvtMessage.getRecipients();

    if (recipientList == null || recipientList.size() == 0)
    {
      LOG.error("markMessageAsReadForUser(message: " + message
          + ") has empty recipient list");
      throw new Error("markMessageAsReadForUser(message: " + message
          + ") has empty recipient list");
    }

    int recordIndex = pvtMessage.getRecipients().indexOf(searchRecipient);

    if (recordIndex != -1)
    {
      ((PrivateMessageRecipientImpl) recipientList.get(recordIndex))
          .setRead(Boolean.TRUE);
    }
  }

  private PrivateMessage getPrivateMessageWithRecipients(
      final PrivateMessage message)
  {

    if (LOG.isDebugEnabled())
    {
      LOG.debug("getPrivateMessageWithRecipients(message: " + message + ")");
    }

    if (message == null)
    {
      throw new IllegalArgumentException("Null Argument");
    }

    HibernateCallback hcb = new HibernateCallback()
    {
      public Object doInHibernate(Session session) throws HibernateException,
          SQLException
      {
        Query q = session.getNamedQuery(QUERY_MESSAGES_BY_ID_WITH_RECIPIENTS);
        q.setParameter("id", message.getId(), Hibernate.LONG);
        return q.uniqueResult();
      }
    };

    PrivateMessage pvtMessage = (PrivateMessage) getHibernateTemplate()
        .execute(hcb);

    if (pvtMessage == null)
    {
      LOG.error("getPrivateMessageWithRecipients(message: " + message
          + ") could not find message");
      throw new Error("getPrivateMessageWithRecipients(message: " + message
          + ") could not find message");
    }

    return pvtMessage;
  }
    
  
  public List searchPvtMsgs(String typeUuid, String searchText,Date searchFromDate, Date searchToDate, 
      boolean searchByText, boolean searchByAuthor, boolean searchByBody, boolean searchByLabel, boolean searchByDate)
  {    
    return messageManager.findPvtMsgsBySearchText(typeUuid, searchText,searchFromDate, searchToDate, 
        searchByText,searchByAuthor,searchByBody,searchByLabel,searchByDate);
  }

  public String getAuthorString() {
      String authorString = getCurrentUser();
      
      try
      {
        authorString = UserDirectoryService.getUser(authorString).getSortName();

      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      
      return authorString;
   }
   
  private String getCurrentUser()
  {
    if (TestUtil.isRunningTests())
    {
      return "test-user";
    }
    return sessionManager.getCurrentSessionUserId();
  }

  public AreaManager getAreaManager()
  {
    return areaManager;
  }

  public void setAreaManager(AreaManager areaManager)
  {
    this.areaManager = areaManager;
  }

  public MessageForumsMessageManager getMessageManager()
  {
    return messageManager;
  }

  public void setMessageManager(MessageForumsMessageManager messageManager)
  {
    this.messageManager = messageManager;
  }

  public void setTypeManager(MessageForumsTypeManager typeManager)
  {
    this.typeManager = typeManager;
  }

  public void setSessionManager(SessionManager sessionManager)
  {
    this.sessionManager = sessionManager;
  }

  public void setIdManager(IdManager idManager)
  {
    this.idManager = idManager;
  }
  
  public void setForumManager(MessageForumsForumManager forumManager)
  {
    this.forumManager = forumManager;
  }

  public void setEmailService(EmailService emailService)
  {
    this.emailService = emailService;
  }
    
  
  public boolean isInstructor()
  {
    LOG.debug("isInstructor()");
    return isInstructor(UserDirectoryService.getCurrentUser());
  }

  /**
   * Check if the given user has site.upd access
   * 
   * @param user
   * @return
   */
  private boolean isInstructor(User user)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("isInstructor(User " + user + ")");
    }
    if (user != null)
      return SecurityService.unlock(user, "site.upd", getContextSiteId());
    else
      return false;
  }

  /**
   * @return siteId
   */
  public String getContextSiteId()
  {
    LOG.debug("getContextSiteId()");

    return ("/site/" + ToolManager.getCurrentPlacement().getContext());
  }

  public String getContextId()
  {

    LOG.debug("getContextId()");

    if (TestUtil.isRunningTests())
    {
      return "01001010";
    }
    else
    {
      return ToolManager.getCurrentPlacement().getContext();
    }
  }  
     
  //Helper class
  public String getTopicTypeUuid(String topicTitle)
  {
    String topicTypeUuid;
    if((PVTMSG_MODE_RECEIVED).equals(topicTitle))
    {
      topicTypeUuid=typeManager.getReceivedPrivateMessageType();
    }
    else if((PVTMSG_MODE_SENT).equals(topicTitle))
    {
      topicTypeUuid=typeManager.getSentPrivateMessageType();
    }
    else if((PVTMSG_MODE_DELETE).equals(topicTitle))
    {
      topicTypeUuid=typeManager.getDeletedPrivateMessageType();
    }
    else if((PVTMSG_MODE_DRAFT).equals(topicTitle))
    {
      topicTypeUuid=typeManager.getDraftPrivateMessageType();
    }
    else
    {
      topicTypeUuid=typeManager.getCustomTopicType(topicTitle);
    }
    return topicTypeUuid;
  }
  
  public Area getAreaByContextIdAndTypeId(final String typeId) {
    LOG.debug("getAreaByContextIdAndTypeId executing for current user: " + getCurrentUser());
    HibernateCallback hcb = new HibernateCallback() {
        public Object doInHibernate(Session session) throws HibernateException, SQLException {
            Query q = session.getNamedQuery("findAreaByContextIdAndTypeId");
            q.setParameter("contextId", getContextId(), Hibernate.STRING);
            q.setParameter("typeId", typeId, Hibernate.STRING);
            return q.uniqueResult();
        }
    };

    return (Area) getHibernateTemplate().execute(hcb);
  }
}
