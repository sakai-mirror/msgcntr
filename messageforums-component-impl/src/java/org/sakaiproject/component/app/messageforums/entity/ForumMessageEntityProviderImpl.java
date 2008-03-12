package org.sakaiproject.component.app.messageforums.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.entity.ForumMessageEntityProvider;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.PropertyProvideable;

public class ForumMessageEntityProviderImpl implements ForumMessageEntityProvider,
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
    
    String userId = null;
    String siteId = null;
    String topicId = null;

    if (ENTITY_PREFIX.equals(prefixes[0])) {

      for (int i = 0; i < name.length; i++) {
        if ("context".equalsIgnoreCase(name[i]) || "site".equalsIgnoreCase(name[i]))
          siteId = searchValue[i];
        else if ("user".equalsIgnoreCase(name[i]) || "userId".equalsIgnoreCase(name[i]))
          userId = searchValue[i];
        else if ("topic".equalsIgnoreCase(name[i]) || "topicId".equalsIgnoreCase(name[i]))
          topicId = searchValue[i];
        else if ("parentReference".equalsIgnoreCase(name[i])) {
          String[] parts = searchValue[i].split("/");
          topicId = parts[parts.length - 1];
        }
      }

      //TODO: support search by something other then topic id...
      if (topicId != null) {
        List <Message> messages = forumManager.getTopicByIdWithMessagesAndAttachments(new Long(topicId)).getMessages();
        for (int i = 0; i < messages.size(); i++) {
          if (messages.get(i).getDraft().booleanValue() == false
                  && messages.get(i).getDeleted().booleanValue() == false) {
            rv.add("/" + ENTITY_PREFIX + "/" + messages.get(i).getId().toString());
          }
        }
      }
    }

    return rv;
  }

  public Map<String, String> getProperties(String reference) {
    Map<String, String> props = new HashMap<String, String>();
    System.out.println(reference + " ==> " + reference.substring(reference.lastIndexOf("/") + 1));
    Message message = forumManager.getMessageById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));
    
    props.put("author", message.getCreatedBy());
    props.put("title", message.getTitle());
    props.put("modifiedBy", message.getModifiedBy());
    if (message.getCreated() != null)
      props.put("date", message.getCreated().toString());
    props.put("label", message.getLabel());
    if (message.getDraft() != null)
      props.put("draft", message.getDraft().toString());
    
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
