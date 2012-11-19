/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-hbm/src/java/org/sakaiproject/component/app/messageforums/dao/hibernate/UnreadStatusImpl.java $
 * $Id: UnreadStatusImpl.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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
package org.sakaiproject.component.app.messageforums.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.MessageMoveHistory;

public class MessageMoveHistoryImpl extends MutableEntityImpl implements MessageMoveHistory {

    private static final Log LOG = LogFactory.getLog(MessageMoveHistoryImpl.class);
    
    private Long fromTopicId;
	private Long toTopicId;
    private Long messageId;
    private Boolean reminder;

    
    public MessageMoveHistoryImpl() {
		// default constructor;
	}


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
 
	public Long getFromTopicId() {	
		return fromTopicId;
	}

	public void setFromTopicId(Long topicId) {
		this.fromTopicId = topicId;
	}
	
	public Boolean getReminder() {
		return reminder;
	}

	public void setReminder(Boolean remind) {
		this.reminder = remind;
	}

	public Long getToTopicId() {
		return toTopicId;
	}
	
	public void setToTopicId(Long topicId) {
		this.toTopicId = topicId;
		
	}


}
