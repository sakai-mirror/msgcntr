package org.sakaiproject.component.app.messageforums.entity;

import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.entity.ForumEntityProvider;
import org.sakaiproject.api.app.messageforums.entity.ForumTopicEntityProvider;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.*;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.extension.TemplateMap;
import org.sakaiproject.entitybroker.entityprovider.extension.RequestStorage;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForumEntityProviderImpl extends AbstractEntityProvider implements ForumEntityProvider, CoreEntityProvider,
        RequestStorable, RESTful, RedirectDefinable {

    private DiscussionForumManager forumManager;
    private static final Log log = LogFactory.getLog(ForumEntityProviderImpl.class);

    public String getEntityPrefix() {
        return ENTITY_PREFIX;
    }

    RequestStorage requestStorage = null;

    public void setRequestStorage(RequestStorage requestStorage) {
       this.requestStorage = requestStorage;
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

    public List<String> findEntityRefs(String[] prefixes, String[] name, String[] searchValue,
                                       boolean exactMatch) {
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

            if (siteId != null && userId != null) {
                List<DiscussionForum> forums = forumManager.getDiscussionForumsByContextId(siteId);
                for (int i = 0; i < forums.size(); i++) {
                    // TODO: authz is way too basic, someone more hip to message center please improve...
                    //This should also allow people with read access to an item to link to it
                    if (forumManager.isInstructor(userId, siteId)
                            || userId.equals(forums.get(i).getCreatedBy())) {
                        rv.add("/" + ENTITY_PREFIX + "/" + forums.get(i).getId().toString());
                    }
                }
            }
        }

        return rv;
    }

    public Map<String, String> getProperties(String reference) {
        Map<String, String> props = new HashMap<String, String>();
        DiscussionForum forum =
                forumManager.getForumById(new Long(reference.substring(reference.lastIndexOf("/") + 1)));

        props.put("title", forum.getTitle());
        props.put("author", forum.getCreatedBy());
        if (forum.getCreated() != null)
            props.put("date", DateFormat.getInstance().format(forum.getCreated()));
        if (forum.getModified() != null) {
            props.put("modified_date", DateFormat.getInstance().format(forum.getModified()));
            props.put("modified_by", forum.getModifiedBy());
        }
        props.put("short_description", forum.getShortDescription());
        props.put("description", forum.getExtendedDescription());
        if (forum.getDraft() != null)
            props.put("draft", forum.getDraft().toString());
        if (forum.getModerated() != null)
            props.put("moderated", forum.getModerated().toString());
        props.put("child_provider", ForumTopicEntityProvider.ENTITY_PREFIX);
        props.put("assignment_name", forum.getDefaultAssignName());
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
        return forumManager.createForum().getId()+"";
    }

    public Object getSampleEntity() {        
         return forumManager.createForum();
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
        DiscussionForum  discussionForum = forumManager.getForumById(new Long(id));
        if (id == null) {
            throw new IllegalArgumentException("No forum found to update for the given reference: " + entityReference);
        }

        DiscussionForum updatedDisscussionForum  = (DiscussionForum) entity;
        String siteId = developerHelperService.getCurrentLocationId();
        boolean allowed = false;
        String location = "/site/" + siteId;

        if(forumManager.isForumOwner(discussionForum) || forumManager.isInstructor(userReference,siteId)){
          allowed = true;
        }
        if (!allowed) {
            throw new SecurityException("Current user ("+userReference+") cannot update forums in location ("+location+")");
        }

        developerHelperService.copyBean(discussionForum, updatedDisscussionForum, 0, new String[] {"id","uuId"}, true);
        forumManager.saveForum(discussionForum);

    }

    public Object getEntity(EntityReference entityReference) {

        String id = entityReference.getId();
        DiscussionForum disccusionForum  = forumManager.getForumById(new Long(id));
        if (disccusionForum == null) {
            throw new IllegalArgumentException("No Forum found for the given reference: " + entityReference);
        }

        return disccusionForum;
    }

    public void deleteEntity(EntityReference entityReference, Map<String, Object> stringObjectMap) {

        String id = entityReference.getId();
        if (id == null) {
            throw new IllegalArgumentException("The reference must include an id for deletes (id is currently null)");
        }

        DiscussionForum disccusionForum  = forumManager.getForumById(new Long(id));
        if (disccusionForum == null) {
            throw new IllegalArgumentException("No forum found for the given reference: " + entityReference);
        }
        forumManager.deleteForum(disccusionForum);
    }

    public List<?> getEntities(EntityReference entityReference, Search search) {
        if(log.isDebugEnabled()) log.debug("getEntities()");
        List forums = forumManager.getDiscussionForumsWithTopics();        
        return forums;

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
