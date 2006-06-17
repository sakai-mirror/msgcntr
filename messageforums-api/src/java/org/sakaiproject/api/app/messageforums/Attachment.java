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

package org.sakaiproject.api.app.messageforums;

public interface Attachment extends MutableEntity {

    public String getAttachmentId();

    public void setAttachmentId(String attachmentId);

    public String getAttachmentName();

    public void setAttachmentName(String attachmentName);

    public String getAttachmentSize();

    public void setAttachmentSize(String attachmentSize);

    public String getAttachmentType();

    public void setAttachmentType(String attachmentType);

    public String getAttachmentUrl();

    public void setAttachmentUrl(String attachmentUrl);

    public Message getMessage();

    public void setMessage(Message parent);

    public BaseForum getForum();

    public void setForum(BaseForum forum);
    
    public Topic getTopic();
    
    public void setTopic(Topic topic);
    
    public OpenForum getOpenForum();
    
    public void setOpenForum(OpenForum openForum);
    
    public PrivateForum getPrivateForum();
    
    public void setPrivateForum(PrivateForum privateForum);    
    
    //Is it required for editing attachment in Pvt Msg????
    public Long getPvtMsgAttachId();
    public void setPvtMsgAttachId(Long pvtMsgAttachId);
    public void setCreatedBy(String createdBy);
    public void setLastModifiedBy(String lastMOdifiedBy);
}