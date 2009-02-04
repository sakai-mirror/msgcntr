package org.sakaiproject.component.app.messageforums.entity;

import java.text.DateFormat;
import java.util.*;

import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.MessageForumsMessageManager;
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

public class ForumMessageEntityProviderImpl extends AbstractEntityProvider implements ForumMessageEntityProvider, CoreEntityProvider,
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

            // TODO: support search by something other then topic id...
            if (topicId != null) {
                List<Message> messages =
                        forumManager.getTopicByIdWithMessagesAndAttachments(new Long(topicId)).getMessages();
                for (int i = 0; i < messages.size(); i++) {
                    // TODO: authz is way too basic, someone more hip to message center please improve...
                    //This should also allow people with read access to an item to link to it
                    if (forumManager.isInstructor(userId, siteId)
                            || userId.equals(messages.get(i).getCreatedBy())) {
                        rv.add("/" + ENTITY_PREFIX + "/" + messages.get(i).getId().toString());
                    }
                }
            }
        }

        return rv;
    }

    public Map<String, String> getProperties(String reference) {
        Map<String, String> props = new HashMap<String, String>();
        Message message =
                forumManager.getMessageById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));

        props.put("title", message.getTitle());
        props.put("author", message.getCreatedBy());
        if (message.getCreated() != null)
            props.put("date", DateFormat.getInstance().format(message.getCreated()));
        if (message.getModifiedBy() != null) {
            props.put("modified_by", message.getModifiedBy());
            props.put("modified_date", DateFormat.getInstance().format(message.getModified()));
        }
        props.put("label", message.getLabel());
        if (message.getDraft() != null)
            props.put("draft", message.getDraft().toString());
        if (message.getApproved() != null)
            props.put("approved", message.getApproved().toString());
        if (message.getGradeAssignmentName() != null)
            props.put("assignment_name", message.getGradeAssignmentName());
        if (message.getGradeComment() != null)
            props.put("grade_comment", message.getGradeComment());
    
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

        Message message = (Message)entity;
        message.setCreated(new Date());
        message.setCreatedBy(developerHelperService.getCurrentUserId());
        forumManager.saveMessage(message);
        return message.getId()+"";
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
            throw new SecurityException("anonymous user cannot update forum: " + entityReference);
        }
        Message  message = forumManager.getMessageById(Long.valueOf(id));
        if (id == null) {
            throw new IllegalArgumentException("No Message found to update for the given reference: " + entityReference);
        }

        Message updatedMessage = (Message) entity;
        String siteId = developerHelperService.getCurrentLocationId();
        String userId = developerHelperService.getCurrentUserId();

        boolean allowed = false;
        String location = "/site/" + siteId;

        if (forumManager.isInstructor(userId, siteId) || userId.equals(message.getCreatedBy())) {
            allowed = true;
        }
        if (!allowed) {
            throw new SecurityException("Current user ("+userReference+") cannot update message in location ("+location+")");
        }

        developerHelperService.copyBean(message, updatedMessage, 0, new String[] {"id"}, true);
        forumManager.saveMessage(message);

    }

    public Object getEntity(EntityReference entityReference) {

        String id = entityReference.getId();
        Message message  = forumManager.getMessageById(Long.valueOf(id));
        if (message == null) {
            throw new IllegalArgumentException("No Message found for the given reference: " + entityReference);
        }
        return message;
    }

    public void deleteEntity(EntityReference entityReference, Map<String, Object> stringObjectMap) {

        String id = entityReference.getId();
        if (id == null) {
            throw new IllegalArgumentException("The reference must include an id for deletes (id is currently null)");
        }

        Message message  = forumManager.getMessageById(Long.valueOf(id));
        if (message == null) {
            throw new IllegalArgumentException("No message found for the given reference: " + entityReference);
        }
        forumManager.deleteMessage(message);
    }

    public List<?> getEntities(EntityReference entityReference, Search search) {
        String topicId = entityReference.getId();
        return forumManager.getTopicByIdWithMessagesAndAttachments(new Long(topicId)).getMessages();
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
