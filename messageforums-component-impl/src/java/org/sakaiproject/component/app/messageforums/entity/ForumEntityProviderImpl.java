package org.sakaiproject.component.app.messageforums.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.entity.ForumEntityProvider;
import org.sakaiproject.api.app.messageforums.entity.ForumTopicEntityProvider;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.PropertyProvideable;

public class ForumEntityProviderImpl implements ForumEntityProvider,
AutoRegisterEntityProvider, PropertyProvideable{

  private DiscussionForumManager forumManager;
  
  public String getEntityPrefix() {
    return ENTITY_PREFIX;
  }

  public boolean entityExists(String id) {
    DiscussionForum forum = null;
    try {
      forum = forumManager.getForumById(new Long(id));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return (forum != null);
  }

  public List<String> findEntityRefs(String[] prefixes, String[] name, String[] searchValue, boolean exactMatch) {
    List<String> rv = new ArrayList<String>();
    
    String userId = null;
    String siteId = null;

    if (ENTITY_PREFIX.equals(prefixes[0])) {

      for (int i = 0; i < name.length; i++) {
        if ("context".equalsIgnoreCase(name[i]) || "site".equalsIgnoreCase(name[i]))
          siteId = searchValue[i];
        else if ("user".equalsIgnoreCase(name[i]) || "userId".equalsIgnoreCase(name[i]))
          userId = searchValue[i];
      }

      if (siteId != null) {
        List <DiscussionForum> forums = forumManager.getDiscussionForumsByContextId(siteId);
        for (int i = 0; i < forums.size(); i++) {
          if (forums.get(i).getDraft().booleanValue() == false) {
             rv.add("/" + ENTITY_PREFIX + "/" + forums.get(i).getId().toString());
          }
        }
      }
    }

    return rv;
  }

  public Map<String, String> getProperties(String reference) {
    Map<String, String> props = new HashMap<String, String>();
    System.out.println(reference + " ==> " + reference.substring(reference.lastIndexOf("/") + 1));
    DiscussionForum forum = forumManager.getForumById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));
    
    props.put("author", forum.getCreatedBy());
    props.put("title", forum.getTitle());
    props.put("modifiedBy", forum.getModifiedBy());
    if (forum.getCreated() != null)
      props.put("date", forum.getCreated().toString());
    props.put("description", forum.getShortDescription());
    if (forum.getDraft() != null)
      props.put("draft", forum.getDraft().toString());
    props.put("child_provider", ForumTopicEntityProvider.ENTITY_PREFIX);
    
    return props;
  }

  public String getPropertyValue(String reference, String name) {
    //TODO: don't be so lazy, just get what we need...
    Map<String, String> props = getProperties(reference);
    return props.get(name);
  }

  public void setPropertyValue(String reference, String name, String value) {
    //This does nothing for now... we could all the setting of many published assessment properties here though... if you're feeling jumpy feel free.
  }

  public void setForumManager(DiscussionForumManager forumManager) {
    this.forumManager = forumManager;
  }
}
