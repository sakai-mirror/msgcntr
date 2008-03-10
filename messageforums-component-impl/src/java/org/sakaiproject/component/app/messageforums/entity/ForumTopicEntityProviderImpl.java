package org.sakaiproject.component.app.messageforums.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.entity.ForumTopicEntityProvider;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.PropertyProvideable;

public class ForumTopicEntityProviderImpl implements ForumTopicEntityProvider,
AutoRegisterEntityProvider, PropertyProvideable{

  private DiscussionForumManager forumManager;
  
  public String getEntityPrefix() {
    return ENTITY_PREFIX;
  }

  public boolean entityExists(String id) {
    Topic topic = null;
    try {
      topic = forumManager.getTopicById(new Long(id));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return (topic != null);
  }

  public List<String> findEntityRefs(String[] prefixes, String[] name, String[] searchValue, boolean exactMatch) {
    List<String> rv = new ArrayList<String>();
    
    String forumId = null;
    String userId = null;
    String siteId = null;

    if (ENTITY_PREFIX.equals(prefixes[0])) {

      for (int i = 0; i < name.length; i++) {
        if ("context".equalsIgnoreCase(name[i]) || "site".equalsIgnoreCase(name[i]))
          siteId = searchValue[i];
        else if ("user".equalsIgnoreCase(name[i]) || "userId".equalsIgnoreCase(name[i]))
          userId = searchValue[i];
        else if ("forum".equalsIgnoreCase(name[i]) || "forumId".equalsIgnoreCase(name[i]))
          forumId = searchValue[i];
      }

      //TODO: need a way to generate the url with out having siteId in search
      if (forumId != null) {
        DiscussionForum forum = forumManager.getForumById(new Long(forumId));
        List<Topic> topics = forum.getTopics();
        for (int i = 0; i < topics.size(); i++) {
          rv.add("/" + ENTITY_PREFIX + "/" + topics.get(i).getId().toString());
        }
      }
      else if (siteId != null) {
        List <DiscussionForum> forums = forumManager.getDiscussionForumsByContextId(siteId);
        for (int i = 0; i < forums.size(); i++) {
          if (forums.get(i).getDraft().booleanValue() == false) {
            List<Topic> topics = forums.get(i).getTopics();
            for (int j = 0; j < topics.size(); j++) {
              rv.add("/" + ENTITY_PREFIX + "/" + topics.get(j).getId().toString());
            }
          }
        }
      }
    }

    return rv;
  }

  public Map<String, String> getProperties(String reference) {
    Map<String, String> props = new HashMap<String, String>();
    System.out.println(reference + " ==> " + reference.substring(reference.lastIndexOf("/") + 1));
    Topic topic = forumManager.getTopicById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));
    
    props.put("author", topic.getCreatedBy());
    props.put("title", topic.getTitle());
    props.put("modifiedBy", topic.getModifiedBy());
    if (topic.getCreated() != null)
      props.put("date", topic.getCreated().toString());
    props.put("description", topic.getShortDescription());
//    props.put("context", topic.getOpenForum().getArea().getContextId());
    
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
