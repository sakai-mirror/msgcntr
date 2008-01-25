/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-hbm/src/java/org/sakaiproject/component/app/messageforums/dao/hibernate/TopicImpl.java $
 * $Id: TopicImpl.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
package org.sakaiproject.component.app.messageforums.dao.hibernate;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.BaseForum;
import org.sakaiproject.api.app.messageforums.DBMembershipItem;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.OpenForum;
import org.sakaiproject.api.app.messageforums.PermissionLevel;
import org.sakaiproject.api.app.messageforums.PrivateForum;
import org.sakaiproject.api.app.messageforums.Topic;

public abstract class TopicImpl extends MutableEntityImpl implements Topic {

    private static final Log LOG = LogFactory.getLog(TopicImpl.class);
    
    private String title;
    private String shortDescription;
    private String extendedDescription;
    private Set attachmentsSet;// = new HashSet();
    private Boolean mutable;
    private Integer sortIndex;
    private String typeUuid;
    private BaseForum baseForum;
    private Set messagesSet;// = new HashSet();
    private Set membershipItemSet;
    private String defaultAssignName;
    private Boolean moderated;
    
    // foreign keys for hibernate
    private PrivateForum privateForum;
    private OpenForum openForum;        
    
    // indecies for hibernate
    //private int ofindex;
    //private int pfindex;   

    public List getMessages() {
        return Util.setToList(messagesSet);
    }

    public void setMessages(List messages) {
        this.messagesSet = Util.listToSet(messages);
    }

    public Set getMessagesSet() {
        return messagesSet;
    }

    public void setMessagesSet(Set messagesSet) {
        this.messagesSet = messagesSet;
    }

    public Set getAttachmentsSet() {
        return attachmentsSet;
    }
  
    public void setAttachmentsSet(Set attachmentsSet) {
        this.attachmentsSet = attachmentsSet;
    }
    
    public List getAttachments()
    {
      return Util.setToList(attachmentsSet);
    }
  
    public void setAttachments(List attachments)
    {
      this.attachmentsSet = Util.listToSet(attachments);
    }
    
    public Set getMembershipItemSet() {
			return membershipItemSet;
		}

		public void setMembershipItemSet(Set membershipItemSet) {
			this.membershipItemSet = membershipItemSet;
		}

    public String getExtendedDescription() {
        return extendedDescription;
    }

    public void setExtendedDescription(String extendedDescription) {
        this.extendedDescription = extendedDescription;
    }

    public Boolean getMutable() {
        return mutable;
    }

    public void setMutable(Boolean mutable) {
        this.mutable = mutable;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public BaseForum getBaseForum() {
    	if (openForum != null)
    		return openForum;
    	else if (privateForum != null)
    		return privateForum;
    	
        return baseForum;
    }

    public void setBaseForum(BaseForum forum) {
        this.baseForum = forum;        
    }
    
    public OpenForum getOpenForum() {
        return openForum;
    }

    public void setOpenForum(OpenForum openForum) {
        this.openForum = openForum;
    }

    public PrivateForum getPrivateForum() {
        return privateForum;
    }

    public void setPrivateForum(PrivateForum privateForum) {
        this.privateForum = privateForum;
    }
    
    public Boolean getModerated() {
    	return moderated;
    }
    
    public void setModerated(Boolean moderated) {
    	this.moderated = moderated;
    }
        

//    public int getOfindex() {
//        try {
//            return getOpenForum().getTopics().indexOf(this);
//        } catch (Exception e) {
//            return ofindex;
//        }
//    }
//
//    public void setOfindex(int ofindex) {
//        this.ofindex = ofindex;
//    }
//
//    public int getPfindex() {
//        try {
//            return getPrivateForum().getTopics().indexOf(this);
//        } catch (Exception e) {
//            return pfindex;
//        }
//    }
//
//    public void setPfindex(int pfindex) {
//        this.pfindex = pfindex;
//    }
    
    public String toString() {
    	return "Topic/" + id;
        //return "Topic.id:" + id;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Topic) {
            return getId().equals(((Topic)obj).getId());
        }
        return false;
    }

    // needs a better impl
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }
        
    ////////////////////////////////////////////////////////////////////////
    // helper methods for collections
    ////////////////////////////////////////////////////////////////////////
    
    public void addMessage(Message message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addForum(message " + message + ")");
        }
        
        if (message == null) {
            throw new IllegalArgumentException("message == null");
        }
        
        if (messagesSet == null) {
            messagesSet = new HashSet();
        }
        message.setTopic(this);
        messagesSet.add(message);
    }

    public void removeMessage(Message message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeForum(message " + message + ")");
        }
        
        if (message == null) {
            throw new IllegalArgumentException("Illegal message argument passed!");
        }

        // TODO: instead of this, set deleted field to true
        message.setTopic(null);
        messagesSet.remove(message);
    }
   
    public void addAttachment(Attachment attachment) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addAttachment(Attachment " + attachment + ")");
        }
        
        if (attachment == null) {
            throw new IllegalArgumentException("attachment == null");
        }
        
        if (attachmentsSet == null) {
            attachmentsSet = new HashSet();
        }
        attachment.setTopic(this);
        attachmentsSet.add(attachment);
    }

    public void removeAttachment(Attachment attachment) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeAttachment(Attachment " + attachment + ")");
        }
        
        if (attachment == null) {
            throw new IllegalArgumentException("Illegal attachment argument passed!");
        }
        
        attachment.setTopic(null);
        attachmentsSet.remove(attachment);
    }
    
    public void addMembershipItem(DBMembershipItem item) {
      if (LOG.isDebugEnabled()) {
          LOG.debug("addMembershipItem(item " + item + ")");
      }
      
      if (item == null) {
          throw new IllegalArgumentException("item == null");
      }
      
      if (membershipItemSet == null) {
      	membershipItemSet = new HashSet();
      }          
      membershipItemSet.add(item);
  }

    public void removeMembershipItem(DBMembershipItem item) {
      if (LOG.isDebugEnabled()) {
          LOG.debug("removeMembershipItem(item " + item + ")");
      }
      
      if (item == null) {
          throw new IllegalArgumentException("Illegal level argument passed!");
      }
          
      membershipItemSet.remove(item);
    }

    public String getDefaultAssignName()
    {
      return defaultAssignName;
    }

    public void setDefaultAssignName(String defaultAssignName)
    {
      this.defaultAssignName = defaultAssignName;
    }		

}
