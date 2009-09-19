/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/ui/DiscussionForumManager.java $
 * $Id: DiscussionForumManager.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.api.app.messageforums.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.DBMembershipItem;
import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.DiscussionTopic;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.Topic;

/**
 * @author <a href="mailto:rshastri@iupui.edu">Rashmi Shastri</a>
 */
public interface DiscussionForumManager
{
  public List searchTopicMessages(Long topicId, String searchText);
    
  public Topic getTopicByIdWithAttachments(Long topicId);
 
  public DiscussionForum getForumByIdWithTopics(Long forumId);
  
  public List getTopicsByIdWithMessages(final Long forumId);
  
  public List getTopicsByIdWithMessagesAndAttachments(final Long forumId);
  
  public List getTopicsByIdWithMessagesMembershipAndAttachments(final Long forumId);
  
  public Topic getTopicByIdWithMessages(final Long topicId);
  
  public Topic getTopicWithAttachmentsById(final Long topicId);
  
  public Topic getTopicByIdWithMessagesAndAttachments(final Long topicId);
  
  /**
   * Returns all moderated topics in site
   * @param areaId
   * @return
   */
  public List getModeratedTopicsInSite();
   

  
  /**
   * Retrieve discussion forum area
   * 
   * @return
   */
  Area getDiscussionForumArea();

  /**
   * @param message
   */
  void saveMessage(Message message);
  void saveMessage(Message message, boolean logEvent);

  /**
   * @param message
   */
  void deleteMessage(Message message);

  /**
   * @param id
   * @return
   */
  Message getMessageById(Long id);

  /**
   * @param topic
   * @return
   */
  int getTotalNoMessages(Topic topic);

  /**
   * When topic is moderated and the user does not have the moderate
   * perm, only count approved messages and messages authored by user
   * @param topic
   * @return
   */
  int getTotalViewableMessagesWhenMod(Topic topic);
  
  /**
   * @param topic
   * @return
   */
  int getUnreadNoMessages(Topic topic);
  
  /**
   * When topic is moderated and the user does not have the moderate
   * perm, only count approved messages and messages authored by user
   * @param topic
   * @return
   */
  int getNumUnreadViewableMessagesWhenMod(Topic topic);
  
  /**
   * Mark all pending messages in a give topic as "Approved"
   * Used when a moderated topic is changed to not moderated
   * @param topicId
   */
  public void approveAllPendingMessages(Long topicId);
  
  /**
   * Returns pending msgs in site according to user's memberships
   * @return
   */
  List getPendingMsgsInSiteByMembership(List membershipList);
  
  /**
   * 
   * @return
   */
  public List getDiscussionForums();
  
  /**
   * @return
   */
  public List getDiscussionForumsWithTopics();

  /**
   * @return
   */
  public List getDiscussionForumsByContextId(String contextId);
  
  /**
   * @param topicId
   * @return
   */
  public DiscussionForum getForumById(Long forumId);

  /**
   * @param forumId
   * @return
   */
  public DiscussionForum getForumByUuid(String forumId);

  /**
   * @param topicId
   * @return
   */
  public List getMessagesByTopicId(Long topicId);

  /**
   * @param topicId
   * @return
   */
  public DiscussionTopic getTopicById(Long topicId);
  public DiscussionTopic getTopicByUuid(String uuid);

  /**
   * @return
   */
  public boolean hasNextTopic(DiscussionTopic topic);

  /**
   * @return
   */
  public boolean hasPreviousTopic(DiscussionTopic topic);

  /**
   * @param topic
   * @return
   */
  public DiscussionTopic getNextTopic(DiscussionTopic topic);

  /**
   * @param topic
   * @return
   */
  public DiscussionTopic getPreviousTopic(DiscussionTopic topic);

  /**
   * @return
   */
  public boolean isInstructor();
  
  /**
   * Tests if the user has instructor privileges to the site 
   * @param userId
   * @param siteId
   * @return true, only if user has site.upd
   */
  public boolean isInstructor(String userId, String siteId);

  /**
   * @return
   */
  public boolean isSectionTA();

  /**
   * @return
   */
  public DiscussionForum createForum();

  /**
   * @param forum
   */
  public void deleteForum(DiscussionForum forum);

  /**
   * @param forum
   *          TODO
   * @return
   */
  public DiscussionTopic createTopic(DiscussionForum forum);

  /**
   * Save a forum. If this is a new forum, assumes current context is available.
   * @param forum
   */
  public void saveForum(DiscussionForum forum);
  
  /**
   * Saves the given forum object. If forum is new, will be saved in the given contextId
   * @param contextId
   * @param forum
   */
  public void saveForum(String contextId, DiscussionForum forum);
  
  /**
   * @param forum
   * @param object 
   */
  public void saveForumAsDraft(DiscussionForum forum);


  /**
   * @param topic
   */
  public void saveTopic(DiscussionTopic topic);

  /**
   * @param topic
   */
  public void deleteTopic(DiscussionTopic topic);

  /**
   * @return
   */
  public List getDefaultControlPermissions();

  /**
   * @return
   */
  public List getDefaultMessagePermissions();

  /**
   * @return
   */
  public List getAreaControlPermissions();

  /**
   * @return
   */
  public List getAreaMessagePermissions();

  
  /**
   * @param forum
   * @return
   */
  public List getForumControlPermissions(DiscussionForum forum);

  /**
   * @param forum
   * @return
   */
  public List getForumMessagePermissions(DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public List getTopicControlPermissions(DiscussionTopic topic);

  /**
   * @param topic
   * @return
   */
  public List getTopicMessagePermissions(DiscussionTopic topic);

  /**
   * @param controlPermission
   */
  public void saveAreaControlPermissions(List controlpermissions);

  /**
   * @param messagePermissions
   */
  public void saveAreaMessagePermissions(List messagePermissions);

  /**
   * @param forum
   * @param controlPermissions
   */
  public void saveForumControlPermissions(DiscussionForum forum,
      List controlPermissions);

  /**
   * @param forum
   * @param messagePermissions
   */
  public void saveForumMessagePermissions(DiscussionForum forum,
      List messagePermissions);

  /**
   * @param topic
   * @param controlPermissions
   */
  public void saveTopicControlPermissions(DiscussionTopic topic,
      List controlPermissions);

  /**
   * @param topic
   * @param messagePermissions
   */
  public void saveTopicMessagePermissions(DiscussionTopic topic,
      List messagePermissions);

  /**
   * @param topic
   */
  public void saveTopicAsDraft(DiscussionTopic topic);

  /**
   * @param message
   * @param readStatus TODO
   */
  public void markMessageAs(Message message, boolean readStatus);
  
  /**
   * Mark the read status for a given message for a given user
   * @param message
   * @param readStatus
   * @param userId
   */
  public void markMessageReadStatusForUser(Message message, boolean readStatus, String userId);

   
  /**
   * @param accessorList
   * @return
   */
  public List decodeContributorsList(ArrayList contributorList);

  /**
   * @param accessorList
   * @return
   */
  public List decodeAccessorsList(ArrayList accessorList);

  /**
   * @param forum
   * @return
   */
  public List getContributorsList(DiscussionForum forum);
  
  
  /**
   * @param forum
   * @return
   */
  public List getAccessorsList(DiscussionForum forum);

  /**
   * @return
   */
  public Map getAllCourseMembers();

  /**
   * @param topic
   * @param forum 
   * @return
   */
  public List getAccessorsList(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @param forum 
   * @return
   */
  public List getContributorsList(DiscussionTopic topic, DiscussionForum forum); 
  
  /**
   * 
   */
  public void setCourseMemberMapToNull();

  public DBMembershipItem getAreaDBMember(Set originalSet, String name, Integer type);

  public DBMembershipItem getDBMember(Set originalSet, String name, Integer type);
  
  /**
   * 
   * @param attachId
   * @param name
   * @return
   */
  public Attachment createDFAttachment(String attachId, String name);
  
  /**
   * Get the read status of a list of messages for a given user	  
   * @param msgIds the msg ids to check
   * @param userId the user - can be null
   * @return a map of messages indicating their read status
   */
  public Map<Long, Boolean> getReadStatusForMessagesWithId(List<Long> msgIds, String userId);
  
  public List getDiscussionForumsWithTopicsMembershipNoAttachments(String contextId);
  
  /**
   * Returns all pending msgs in the given topic
   * @param topicId
   * @return
   */
  public List getPendingMsgsInTopic(Long topicId);
  
  /**
   * Returns num moderated topics in the current site that the current user
   * has moderate permission for, given the user's memberships
   * @param membershipList
   * @param contextId
   * @return
   */
  public int getNumModTopicsWithModPermission(List membershipList);
  
  /**
   * Returns forum with topics, topic attachments, and topic messages
   * @param forumId
   * @return
   */
  public DiscussionForum getForumByIdWithTopicsAttachmentsAndMessages(Long forumId);

  /**
   * Returns the context (siteId) for a given topic
   * @param topicId
   * @return context (siteId)
   */
  public String getContextForTopicById(Long topicId);
  
  /**
   * Returns the context (siteId) for a given forum
   * @param forumId
   * @return context (siteId)
   */
  public String getContextForForumById(Long forumId);
  
  /**
   * Returns the context (siteId) for a given message
   * @param messageId
   * @return context (siteId)
   */
  public String getContextForMessageById(Long messageId);

  /**
   * Returns the id of the Forum containing a given Message
   * @param messageId
   * @return forumId
   */
  public String ForumIdForMessage(Long messageId);
  public boolean  getAnonRole();
  
  /**
   *
   * @param topic
   * @return true if current use created the topic;
   * in role swap view this will always be false
   */
  public boolean isTopicOwner(DiscussionTopic topic);
 	  	 
  /**
   *
   * @param forum
   * @return true if current use created the forum;
   * in role swap view this will always be false
   */
  public boolean isForumOwner(DiscussionForum forum);
  
  /**
   * 
   * @param contextId
   * @return all discussion forums in the given context with attachments, topics,
   * and messages populated
   */
  public List getDiscussionForumsWithTopics(String contextId);

  /**
  *
  * @param topicId
  * @param checkReadPermission - user must have read permission for topic
  * @param checkModeratePermission - user must have moderate permission for topic
  * @return a set of userIds for the site members who have "read" and/or "moderate" permission
  * for the given topic. Uses the role and group permission settings for the topic
  * to determine permission
  */
  public Set<String> getUsersAllowedForTopic(Long topicId, boolean checkReadPermission, boolean checkModeratePermission);
  

}
