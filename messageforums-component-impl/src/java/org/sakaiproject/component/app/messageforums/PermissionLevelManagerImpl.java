/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-component-impl/src/java/org/sakaiproject/component/app/messageforums/PermissionLevelManagerImpl.java $
 * $Id: PermissionLevelManagerImpl.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
package org.sakaiproject.component.app.messageforums;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.AreaManager;
import org.sakaiproject.api.app.messageforums.DBMembershipItem;
import org.sakaiproject.api.app.messageforums.MessageForumsTypeManager;
import org.sakaiproject.api.app.messageforums.PermissionLevel;
import org.sakaiproject.api.app.messageforums.PermissionLevelManager;
import org.sakaiproject.api.app.messageforums.PermissionsMask;
import org.sakaiproject.id.api.IdManager;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.component.app.messageforums.dao.hibernate.DBMembershipItemImpl;
import org.sakaiproject.component.app.messageforums.dao.hibernate.PermissionLevelImpl;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.event.api.EventTrackingService;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PermissionLevelManagerImpl extends HibernateDaoSupport implements PermissionLevelManager {

	private static final Log LOG = LogFactory.getLog(PermissionLevelManagerImpl.class);
	private SqlService sqlService;
	private EventTrackingService eventTrackingService;
	private SessionManager sessionManager;
	private IdManager idManager;
	private MessageForumsTypeManager typeManager;
	private AreaManager areaManager;
	private Boolean autoDdl;
	
	private Map defaultPermissionsMap = new HashMap();
	
	private static final String QUERY_BY_TYPE_UUID = "findPermissionLevelByTypeUuid";
	private static final String QUERY_ORDERED_LEVEL_NAMES = "findOrderedPermissionLevelNames";
	
	
	
			
	public void init(){
						
    // run ddl            
    if (autoDdl.booleanValue()){
      try
      {                        
         sqlService.ddl(this.getClass().getClassLoader(), "mfr");
      }       
      catch (Throwable t)
      {
        LOG.warn(this + ".init(): ", t);
      }
    }      
    
    /** test creation of permission mask and author level
    PermissionsMask mask = new PermissionsMask();
    mask.put(PermissionLevel.NEW_FORUM, Boolean.TRUE); 
    mask.put(PermissionLevel.NEW_TOPIC, Boolean.TRUE);
    mask.put(PermissionLevel.NEW_RESPONSE, Boolean.TRUE);
    mask.put(PermissionLevel.RESPONSE_TO_RESPONSE, Boolean.TRUE);
    mask.put(PermissionLevel.MOVE_POSTING, Boolean.TRUE);
    mask.put(PermissionLevel.CHANGE_SETTINGS, Boolean.TRUE);
    mask.put(PermissionLevel.POST_GRADES, Boolean.TRUE);
    mask.put(PermissionLevel.READ, Boolean.TRUE);
    mask.put(PermissionLevel.MARK_AS_READ, Boolean.TRUE);
    mask.put(PermissionLevel.MODERATE_POSTINGS, Boolean.TRUE);
    mask.put(PermissionLevel.DELETE_OWN, Boolean.TRUE);
    mask.put(PermissionLevel.DELETE_ANY, Boolean.TRUE);
    mask.put(PermissionLevel.REVISE_OWN, Boolean.TRUE);
    mask.put(PermissionLevel.REVISE_ANY, Boolean.TRUE);
    DBMembershipItem membershipItem = createDBMembershipItem("jlannan", DBMembershipItemImpl.TYPE_USER);
    PermissionLevel level = createPermissionLevel("Author", typeManager.getAuthorLevelType(), mask);    
    Area area = areaManager.createArea(typeManager.getPrivateMessageAreaType());
    
    membershipItem.setPermissionLevel(level);
    
    // save DBMembershiptItem here to get an id so we can add to the set
    saveDBMembershipItem(membershipItem);
    area.addMembershipItem(membershipItem);
           
    area.setName("test");
    area.setHidden(Boolean.FALSE);
    area.setEnabled(Boolean.TRUE);
    area.setLocked(Boolean.FALSE);
    //area.addPermissionLevel(level);
    areaManager.saveArea(area); 
    
    List l = getOrderedPermissionLevelNames();
    **/
    
	}
	
	public PermissionLevel getPermissionLevelByName(String name){
		if (LOG.isDebugEnabled()){
			LOG.debug("getPermissionLevelByName executing(" + name + ")");
		}
		
		if (PERMISSION_LEVEL_NAME_OWNER.equals(name)){
			return getDefaultOwnerPermissionLevel();
		}
		else if (PERMISSION_LEVEL_NAME_AUTHOR.equals(name)){
			return getDefaultAuthorPermissionLevel();
		}
		else if (PERMISSION_LEVEL_NAME_NONEDITING_AUTHOR.equals(name)){
			return getDefaultNoneditingAuthorPermissionLevel();
		}
		else if (PERMISSION_LEVEL_NAME_CONTRIBUTOR.equals(name)){
			return getDefaultContributorPermissionLevel();
		}
		else if (PERMISSION_LEVEL_NAME_REVIEWER.equals(name)){
			return getDefaultReviewerPermissionLevel();
		}
		else if (PERMISSION_LEVEL_NAME_NONE.equals(name)){
			return getDefaultNonePermissionLevel();
		}		
		else{
			return null;
		}
	}
	
  public  List getOrderedPermissionLevelNames(){
						
		if (LOG.isDebugEnabled()){
			LOG.debug("getOrderedPermissionLevelNames executing");
		}
		
		HibernateCallback hcb = new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
          Query q = session.getNamedQuery(QUERY_ORDERED_LEVEL_NAMES);                      
          return q.list();
      }
    };
					
    return (List) getHibernateTemplate().execute(hcb);
  }	
	
	public String getPermissionLevelType(PermissionLevel level){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getPermissionLevelType executing(" + level + ")");
		}
		
		if (level == null) {      
      throw new IllegalArgumentException("Null Argument");
		}
		
		PermissionLevel ownerLevel = getDefaultOwnerPermissionLevel();		
		if (level.equals(ownerLevel)){
			return ownerLevel.getTypeUuid();
		}
				
		PermissionLevel authorLevel = getDefaultAuthorPermissionLevel();		
		if (level.equals(authorLevel)){
			return authorLevel.getTypeUuid();
		}
		
		PermissionLevel noneditingAuthorLevel = getDefaultNoneditingAuthorPermissionLevel();		
		if (level.equals(noneditingAuthorLevel)){
			return noneditingAuthorLevel.getTypeUuid();
		}
				
	  PermissionLevel reviewerLevel = getDefaultReviewerPermissionLevel();
	  if (level.equals(reviewerLevel)){
			return reviewerLevel.getTypeUuid();
		}
	  	  
		PermissionLevel contributorLevel = getDefaultContributorPermissionLevel();
		if (level.equals(contributorLevel)){
			return contributorLevel.getTypeUuid();
		}
				
		PermissionLevel noneLevel = getDefaultNonePermissionLevel();
		if (level.equals(noneLevel)){
			return noneLevel.getTypeUuid();
		}
		
		return null;
	}
	
	public PermissionLevel createPermissionLevel(String name, String typeUuid, PermissionsMask mask){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("createPermissionLevel executing(" + name + "," + typeUuid + "," + mask + ")");
		}
		
		if (name == null || typeUuid == null || mask == null) {      
      throw new IllegalArgumentException("Null Argument");
		}
								
		PermissionLevel newPermissionLevel = new PermissionLevelImpl();
		Date now = new Date();
		String currentUser = getCurrentUser();
		newPermissionLevel.setName(name);
		newPermissionLevel.setUuid(idManager.createUuid());
		newPermissionLevel.setCreated(now);
		newPermissionLevel.setCreatedBy(currentUser);
		newPermissionLevel.setModified(now);
		newPermissionLevel.setModifiedBy(currentUser);
		newPermissionLevel.setTypeUuid(typeUuid);
			
		// set permission properties using reflection
		for (Iterator i = mask.keySet().iterator(); i.hasNext();){
			String key = (String) i.next();
			Boolean value = (Boolean) mask.get(key);
			try{
			  PropertyUtils.setSimpleProperty(newPermissionLevel, key, value);
			}
			catch (Exception e){
				throw new Error(e);
			}
		}										
				
		return newPermissionLevel;		
	}
	
  public DBMembershipItem createDBMembershipItem(String name, String permissionLevelName, Integer type){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("createDBMembershipItem executing(" + name + "," + type + ")");
		}
		
		if (name == null || type == null) {      
      throw new IllegalArgumentException("Null Argument");
		}
								
		DBMembershipItem newDBMembershipItem = new DBMembershipItemImpl();
		Date now = new Date();
		String currentUser = getCurrentUser();
		newDBMembershipItem.setName(name);
		newDBMembershipItem.setPermissionLevelName(permissionLevelName);
		newDBMembershipItem.setUuid(idManager.createUuid());
		newDBMembershipItem.setCreated(now);
		newDBMembershipItem.setCreatedBy(currentUser);
		newDBMembershipItem.setModified(now);
		newDBMembershipItem.setModifiedBy(currentUser);
		newDBMembershipItem.setType(type);
															
		return newDBMembershipItem;		
	}
  
  public void saveDBMembershipItem(DBMembershipItem item){
  	getHibernateTemplate().saveOrUpdate(item);
  }
	
  public PermissionLevel getDefaultOwnerPermissionLevel(){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultOwnerPermissionLevel executing");
		}
						
		String typeUuid = typeManager.getOwnerLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
  
	public PermissionLevel getDefaultAuthorPermissionLevel(){
						
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultAuthorPermissionLevel executing");
		}
						
		String typeUuid = typeManager.getAuthorLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
	
	public PermissionLevel getDefaultNoneditingAuthorPermissionLevel(){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultNoneditingAuthorPermissionLevel executing");
		}
						
		String typeUuid = typeManager.getNoneditingAuthorLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
	
	public PermissionLevel getDefaultReviewerPermissionLevel(){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultReviewerPermissionLevel executing");
		}
						
		String typeUuid = typeManager.getReviewerLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
	
  public PermissionLevel getDefaultContributorPermissionLevel(){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultContributorPermissionLevel executing");
		}
						
		String typeUuid = typeManager.getContributorLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
  
  public PermissionLevel getDefaultNonePermissionLevel(){
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultNonePermissionLevel executing");
		}
						
		String typeUuid = typeManager.getNoneLevelType();
		
		if (typeUuid == null) {      
      throw new IllegalStateException("type cannot be null");
		}		
		return getDefaultPermissionLevel(typeUuid);				
	}
  	
	private PermissionLevel getDefaultPermissionLevel(final String typeUuid){
		
		if (typeUuid == null) {      
      throw new IllegalArgumentException("Null Argument");
		}
		
		if (LOG.isDebugEnabled()){
			LOG.debug("getDefaultPermissionLevel executing with typeUuid: " + typeUuid);
		}
		
		
		//PermissionLevel temp = (PermissionLevel) defaultPermissionsMap.get(typeUuid);		
		//if (temp != null){
		//	return temp;
		//}
		
				
		HibernateCallback hcb = new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException, SQLException {
          Query q = session.getNamedQuery(QUERY_BY_TYPE_UUID);
          q.setParameter("typeUuid", typeUuid);            
          return q.uniqueResult();
      }
    };
					
    PermissionLevel returnedLevel = (PermissionLevel) getHibernateTemplate().execute(hcb);
    
    //if (returnedLevel != null){
    //	defaultPermissionsMap.put(typeUuid, returnedLevel);
    //}
    
    return returnedLevel;
  }	
	
	private String getCurrentUser() {    
		String user = sessionManager.getCurrentSessionUserId();
		return (user == null) ? "test-user" : user;    
  }
	
	public void setEventTrackingService(EventTrackingService eventTrackingService) {
		this.eventTrackingService = eventTrackingService;
	}

	public void setTypeManager(MessageForumsTypeManager typeManager) {
		this.typeManager = typeManager;
	}

	public void setAutoDdl(Boolean autoDdl) {
		this.autoDdl = autoDdl;
	}

	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setAreaManager(AreaManager areaManager) {
		this.areaManager = areaManager;
	}

}
