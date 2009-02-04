package org.sakaiproject.component.app.messageforums.entity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.DiscussionTopic;
import org.sakaiproject.api.app.messageforums.entity.ForumTopicEntityProvider;
import org.sakaiproject.api.app.messageforums.entity.ForumMessageEntityProvider;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.extension.TemplateMap;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.PropertyProvideable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RedirectDefinable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.sakaiproject.entitybroker.EntityReference;

public class ForumTopicEntityProviderImpl extends AbstractEntityProvider implements ForumTopicEntityProvider, CoreEntityProvider,
        PropertyProvideable, RESTful, RedirectDefinable,AutoRegisterEntityProvider {

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

  public List<String> findEntityRefs(String[] prefixes, String[] name, String[] searchValue,
      boolean exactMatch) {
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
        else if ("parentReference".equalsIgnoreCase(name[i])) {
          String[] parts = searchValue[i].split("/");
          forumId = parts[parts.length - 1];
        }
      }

      // TODO: need a way to generate the url with out having siteId in search
      if (forumId != null && userId != null) {
        DiscussionForum forum = forumManager.getForumByIdWithTopics(new Long(forumId));
        List<Topic> topics = forum.getTopics();
        for (int i = 0; i < topics.size(); i++) {
          // TODO: authz is way too basic, someone more hip to message center please improve...
          //This should also allow people with read access to an item to link to it
          if (forumManager.isInstructor(userId, siteId)
              || userId.equals(topics.get(i).getCreatedBy()))
            rv.add("/" + ENTITY_PREFIX + "/" + topics.get(i).getId().toString());
        }
      }
      else if (siteId != null && userId != null) {
        List<DiscussionForum> forums = forumManager.getDiscussionForumsByContextId(siteId);
        for (int i = 0; i < forums.size(); i++) {
          List<Topic> topics = forums.get(i).getTopics();
          for (int j = 0; j < topics.size(); j++) {
            // TODO: authz is way too basic, someone more hip to message center please improve...
            //This should also allow people with read access to an item to link to it
            if (forumManager.isInstructor(userId, siteId)
                || userId.equals(topics.get(j).getCreatedBy()))
              rv.add("/" + ENTITY_PREFIX + "/" + topics.get(j).getId().toString());
          }
        }
      }
    }

    return rv;
  }

  public Map<String, String> getProperties(String reference) {
    Map<String, String> props = new HashMap<String, String>();
    Topic topic =
      forumManager.getTopicById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));

    props.put("title", topic.getTitle());
    props.put("author", topic.getCreatedBy());
    if (topic.getCreated() != null)
      props.put("date", DateFormat.getInstance().format(topic.getCreated()));
    if (topic.getModified() != null) {
      props.put("modified_by", topic.getModifiedBy());
      props.put("modified_date", DateFormat.getInstance().format(topic.getModified()));
    }
    props.put("short_description", topic.getShortDescription());
    props.put("description", topic.getExtendedDescription());
    if (topic.getModerated() != null)
      props.put("moderated", topic.getModerated().toString());
    props.put("child_provider", ForumMessageEntityProvider.ENTITY_PREFIX);

    return props;
  }

  public String getPropertyValue(String reference, String name) {
    // TODO: don't be so lazy, just get what we need...
    Map<String, String> props = getProperties(reference);
    return props.get(name);
  }

  public void setPropertyValue(String reference, String name, String value) {
    // This does nothing for now... we could all the setting of many published assessment properties
    // here though... if you're feeling jumpy feel free.
  }

  public void setForumManager(DiscussionForumManager forumManager) {
    this.forumManager = forumManager;
  }


    public String createEntity(EntityReference entityReference, Object entity, Map<String, Object> stringObjectMap) {
        String id = entityReference.getId();
        DiscussionForum discussionForum = forumManager.getForumById(Long.valueOf(id));
        return forumManager.createTopic(discussionForum).getId()+"";
    }

    public Object getSampleEntity() {
        return null;
    }

    public void updateEntity(EntityReference entityReference, Object entity, Map<String, Object> stringObjectMap) {

        String id = entityReference.getId();
        if (id == null) {
            throw new IllegalArgumentException("The reference must include an id for updates (id is currently null)");
        }
        String userReference = developerHelperService.getCurrentUserReference();
        if (userReference == null) {
            throw new SecurityException("anonymous user cannot update topic: " + entityReference);
        }
        DiscussionTopic topic = forumManager.getTopicById(Long.getLong(id));
        if (id == null) {
            throw new IllegalArgumentException("No topic found to update for the given reference: " + entityReference);
        }

        DiscussionTopic updatedTopic  = (DiscussionTopic) entity;
        String siteId = developerHelperService.getCurrentLocationId();
        String userId = developerHelperService.getCurrentUserId();
        boolean allowed = false;
        String location = "/site/" + siteId;

        if (forumManager.isInstructor(userId, siteId) || userId.equals(topic.getCreatedBy())) {
            allowed = true;
        }
        if (!allowed) {
            throw new SecurityException("Current user ("+userReference+") cannot update topic in location ("+location+")");
        }

        developerHelperService.copyBean(topic, updatedTopic, 0, new String[] {"id",}, true);
        forumManager.saveTopic(topic);

    }

    public Object getEntity(EntityReference entityReference) {
        String id = entityReference.getId();
        DiscussionTopic topic  = forumManager.getTopicById(Long.valueOf(id));
        if (topic == null) {
            throw new IllegalArgumentException("No topic found for the given reference: " + entityReference);
        }
        return topic;
    }

    public void deleteEntity(EntityReference entityReference, Map<String, Object> stringObjectMap) {
        String id = entityReference.getId();
        if (id == null) {
            throw new IllegalArgumentException("The reference must include an id for deletes (id is currently null)");
        }

        DiscussionTopic topic  = forumManager.getTopicById(Long.valueOf(id));
        if (topic == null) {
            throw new IllegalArgumentException("No topic found for the given reference: " + entityReference);
        }
        forumManager.deleteTopic(topic);
    }

    public List<?> getEntities(EntityReference entityReference, Search search) {
        String forumId = entityReference.getId();
        return forumManager.getTopicsByIdWithMessages(new Long(forumId));
    }

    public String[] getHandledOutputFormats() {
        return new String[] {Formats.XML, Formats.JSON};
    }

    public String[] getHandledInputFormats() {
        return new String[] {Formats.XML, Formats.JSON, Formats.HTML};
    }
    public TemplateMap[] defineURLMappings() {
        return new TemplateMap[0];
    }
}
