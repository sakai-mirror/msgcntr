/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2005 The Regents of the University of Michigan, Trustees of Indiana University,
 *                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
 * 
 * Licensed under the Educational Community License Version 1.0 (the "License");
 * By obtaining, using and/or copying this Original Work, you agree that you have read,
 * understand, and will comply with the terms and conditions of the Educational Community License.
 * You may obtain a copy of the License at:
 * 
 *      http://cvs.sakaiproject.org/licenses/license_1_0.html
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 **********************************************************************************/

package org.sakaiproject.component.app.messageforums.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.BaseForum;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.OpenForum;
import org.sakaiproject.api.app.messageforums.PrivateForum;
import org.sakaiproject.api.app.messageforums.Topic;

public class AttachmentImpl extends MutableEntityImpl implements Attachment {

    private static final Log LOG = LogFactory.getLog(AttachmentImpl.class);

    private String attachmentId;

    private String attachmentUrl;

    private String attachmentName;

    private String attachmentSize;

    private String attachmentType;

    private Long pvtMsgAttachId;
    
    // foreign keys for hibernate
    private Message message;
    private BaseForum forum;
    private Topic topic;    
    private OpenForum openForum;
    private PrivateForum privateForum;
    
    // indecies for hibernate
//    private int mesindex;    
//    private int ofindex;
//    private int pfindex;
//    private int tindex;
   
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(String attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    
//    public boolean equals(Object other) {
//        if (!(other instanceof AttachmentImpl)) {
//            return false;
//        }
//
//        Attachment attachment = (Attachment) other;
//        return new EqualsBuilder().append(attachmentId, attachment.getAttachmentId()).isEquals();
//    }
//
//    public int hashCode() {
//        return new HashCodeBuilder().append(attachmentId).toHashCode();
//    }
//
//    public String toString() {
//        return new ToStringBuilder(this).append("attachmentId", attachmentId).append("attachmentUrl", attachmentUrl).append("attachmentName", attachmentName).append("attachmentSize", attachmentSize).append("attachmentType", attachmentType).toString();
//    }

    public BaseForum getForum() {
        return forum;
    }

    public void setForum(BaseForum forum) {
        this.forum = forum;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

//    public int getMesindex() {
//        try {
//            return getMessage().getAttachments().indexOf(this);
//        } catch (Exception e) {
//            return mesindex;
//        }
//    }
//
//    public void setMesindex(int mesindex) {
//        this.mesindex = mesindex;
//    }
//
//    public int getOfindex() {
//        try {
//            return getForum().getAttachments().indexOf(this);
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
//            return getForum().getAttachments().indexOf(this);
//        } catch (Exception e) {
//            return pfindex;
//        }
//    }
//
//    public void setPfindex(int pfindex) {
//        this.pfindex = pfindex;
//    }
//
//    public int getTindex() {
//        try {
//            return getTopic().getAttachments().indexOf(this);
//        } catch (Exception e) {
//            return tindex;
//        }
//    }
//
//    public void setTindex(int tindex) {
//        this.tindex = tindex;
//    }

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

    public Long getPvtMsgAttachId()
    {
      return pvtMsgAttachId;
    }

    public void setPvtMsgAttachId(Long pvtMsgAttachId)
    {
      this.pvtMsgAttachId=pvtMsgAttachId;
    }

    public void setLastModifiedBy(String lastMOdifiedBy)
    {
      // TODO Auto-generated method stub
      
    }
}
