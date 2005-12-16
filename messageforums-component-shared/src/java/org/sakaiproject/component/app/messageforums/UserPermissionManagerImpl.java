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

package org.sakaiproject.component.app.messageforums;

import org.sakaiproject.api.app.messageforums.MessagePermissions;
import org.sakaiproject.api.app.messageforums.PermissionManager;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.UserPermissionManager;
import org.sakaiproject.api.kernel.id.IdManager;
import org.sakaiproject.api.kernel.session.SessionManager;
import org.sakaiproject.api.kernel.tool.Placement;
import org.sakaiproject.api.kernel.tool.cover.ToolManager;
import org.sakaiproject.service.legacy.authzGroup.cover.AuthzGroupService;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

public class UserPermissionManagerImpl extends HibernateDaoSupport implements UserPermissionManager {

    private IdManager idManager;

    private SessionManager sessionManager;

    private PermissionManager permissionManager;
    
    public void init() {
        ;
    }
 
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public IdManager getIdManager() {
        return idManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setIdManager(IdManager idManager) {
        this.idManager = idManager;
    }

    public boolean canRead(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getRead().booleanValue();
    }
    
    public boolean canReviseAny(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getReviseAny().booleanValue();
    }
    
    public boolean canReviseOwn(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getReviseOwn().booleanValue();
    }
    
    public boolean canDeleteAny(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getDeleteAny().booleanValue();
    }
    
    public boolean canDeleteOwn(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getDeleteOwn().booleanValue();
    }
    
    public boolean canMarkAsRead(Topic topic, String typeId) {
        MessagePermissions permission = permissionManager.getTopicMessagePermissionForRole(topic, getCurrentUserRole(), typeId);
        return permission == null ? false : permission.getMarkAsRead().booleanValue();
    }
        
    // helpers

    private String getCurrentUser() {
        if (TestUtil.isRunningTests()) {
            return "test-user";
        }
        return sessionManager.getCurrentSessionUserId();
    }
    
    private String getCurrentUserRole() {
        return AuthzGroupService.getUserRole(getCurrentUser(), "/site/" + getContextId());
    }

    private String getContextId() {
        if (TestUtil.isRunningTests()) {
            return "test-context";
        }
        Placement placement = ToolManager.getCurrentPlacement();
        String presentSiteId = placement.getContext();
        return presentSiteId;
    }
}
