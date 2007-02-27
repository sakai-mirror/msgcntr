/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/ui/UIPermissionsManager.java $
 * $Id: UIPermissionsManager.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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

import java.util.Iterator;
import java.util.Set;

import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.DiscussionTopic;
import org.sakaiproject.api.app.messageforums.Area;

/**
 * @author <a href="mailto:rshastri@iupui.edu">Rashmi Shastri</a>
 */
public interface UIPermissionsManager
{

  /**
   * @return
   */
  public boolean isNewForum();
  
  /**
   * @return
   */
  public boolean isChangeSettings(DiscussionForum forum);
  
  /**     
   * @param forum
   * @return
   */
  public boolean isNewTopic(DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isNewResponse(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isNewResponseToResponse(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isMovePostings(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isChangeSettings(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isPostToGradebook(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isRead(DiscussionTopic topic, DiscussionForum forum );

  /**
   * @param topic
   * @return
   */
  public boolean isReviseAny(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isReviseOwn(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isDeleteAny(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isDeleteOwn(DiscussionTopic topic, DiscussionForum forum);

  /**
   * @param topic
   * @return
   */
  public boolean isMarkAsRead(DiscussionTopic topic, DiscussionForum forum);
  
  public Set getAreaItemsSet(Area area);

  public Set getForumItemsSet(DiscussionForum forum);
  
  public Set getTopicItemsSet(DiscussionTopic topic);
}
