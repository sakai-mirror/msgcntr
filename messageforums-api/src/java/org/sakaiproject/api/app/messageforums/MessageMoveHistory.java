/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/UnreadStatus.java $
 * $Id: UnreadStatus.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
package org.sakaiproject.api.app.messageforums;

public interface MessageMoveHistory extends MutableEntity{

    public Long getId();

    public void setId(Long id);

    public Integer getVersion();

    public void setVersion(Integer version);

    public Long getMessageId();

    public void setMessageId(Long messageId);

    public Long getFromTopicId();

    public void setFromTopicId(Long topicId);

    public Long getToTopicId();

    public void setToTopicId(Long topicId);

    public Boolean getReminder();

    public void setReminder(Boolean remind);
     

    /*
     *
select * from mfr_message_t  
where surrogatekey = :topicid 
union all
(select * from mfr_message_t, mfr_m
where id in (select message_id from mfr_move_history_t where from_topic_id = :topicid))



select msg1.*, 0 as reminder from mfr_message_t msg1
where msg1.surrogatekey = 9080 
union all
(select msg.*, hist.reminder  from mfr_message_t msg, mfr_move_history_t hist
where msg.id in (select message_id from mfr_move_history_t where from_topic_id = 9080))


     */
 
}