/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/Message.java $
 * $Id: Message.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
package org.sakaiproject.api.app.messageforums;

import java.util.List;
import java.util.Set;

 

//import org.sakaiproject.component.app.messageforums.dao.hibernate.Type;

// TODO: Needs to be able to get to the MutableEntity stuff
// TODO: Make Type an interface too

public interface Message extends MutableEntity {

	public Boolean getDeleted();
	public void setDeleted(Boolean deleted);
    public Boolean getDraft();
    public void setDraft(Boolean draft);
    public Boolean getApproved();
    public void setApproved(Boolean approved);
    public Boolean getHasAttachments();
    public void setHasAttachments(Boolean hasAttachments);    
    public List getAttachments();
    public void setAttachments(List attachments);
    public Set getAttachmentsSet();
    public String getAuthor();
    public void setAuthor(String author);
    public String getBody();
    public void setBody(String body);
    public int getWordCount();
    public void setWordCount(int wordCount);
    public String getGradebook();
    public void setGradebook(String gradebook);
    public String getGradebookAssignment();
    public void setGradebookAssignment(String gradebookAssignment);
    public Message getInReplyTo();
    public void setInReplyTo(Message inReplyTo);
    public String getLabel();
    public void setLabel(String label);
    public String getTitle();
    public void setTitle(String title);
    public String getTypeUuid();
    public void setTypeUuid(String typeUuid); 
    public void setTopic(Topic topic);
    public Topic getTopic();
    public void addAttachment(Attachment attachment);
    public void removeAttachment(Attachment attachment);
    public String getGradeComment();
    public void setGradeComment(String gradeComment);
    public String getGradeAssignmentName();
    public void setGradeAssignmentName(String gradeAssignmentName);
    
}