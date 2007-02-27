/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/ui/DiscussionForumManager.java $
 * $Id: DiscussionForumManager.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
package org.sakaiproject.api.app.messageforums.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.AreaControlPermission;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.BaseForum;
import org.sakaiproject.api.app.messageforums.DBMembershipItem;
import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.DiscussionTopic;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.PermissionsMask;
import org.sakaiproject.authz.api.Role;

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
  
  public Topic getTopicByIdWithMessages(final Long topicId);
  
  public Topic getTopicByIdWithMessagesAndAttachments(final Long topicId);
   

  
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
   * @param topic
   * @return
   */
  int getUnreadNoMessages(Topic topic);

  /**
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
   * @param forum
   */
  public void saveForum(DiscussionForum forum);
  
  
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
  
  public Map getReadStatusForMessagesWithId(List msgIds, String userId);
}
