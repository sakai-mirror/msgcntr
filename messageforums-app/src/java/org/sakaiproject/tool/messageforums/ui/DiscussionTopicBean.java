package org.sakaiproject.tool.messageforums.ui;
 
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.api.app.messageforums.Topic;
public class DiscussionTopicBean
{
  private Topic topic;
  private int totalNoMessages;
  private int unreadNoMessages;
  private boolean hasNextTopic=false;
  private boolean hasPreviousTopic=false;
  private String nextTopicId;
  private String previousTopicId;
 
  private List messages=new ArrayList();
  
  public DiscussionTopicBean(Topic topic)
  {
   this.topic= topic;    
  }
    
  /**
   * @return
   */
  public Topic getTopic()
  {
    return topic;
  }
  
  /**
   * @return
   */
  public int getTotalNoMessages()
  {
    return totalNoMessages;
  }
  
  /**
   * @param totalMessages
   */
  public void setTotalNoMessages(int totalMessages)
  {
    this.totalNoMessages = totalMessages;
  }
  
  /**
   * @return
   */
  public int getUnreadNoMessages()
  {
    return unreadNoMessages;
  }
  
  /**
   * @param unreadMessages
   */
  public void setUnreadNoMessages(int unreadMessages)
  {
    this.unreadNoMessages = unreadMessages;
  }
  
  
   
  /**
   * @return Returns the hasNextTopic.
   */
  public boolean isHasNextTopic()
  {
    return hasNextTopic;
  }


  /**
   * @param hasNextTopic The hasNextTopic to set.
   */
  public void setHasNextTopic(boolean hasNextTopic)
  {
    this.hasNextTopic = hasNextTopic;
  }


  /**
   * @return Returns the hasPreviousTopic.
   */
  public boolean isHasPreviousTopic()
  {
    return hasPreviousTopic;
  }


  /**
   * @param hasPreviousTopic The hasPreviousTopic to set.
   */
  public void setHasPreviousTopic(boolean hasPreviousTopic)
  {
    this.hasPreviousTopic = hasPreviousTopic;
  }


  /**
   * @return Returns the nextTopicId.
   */
  public String getNextTopicId()
  {
    return nextTopicId;
  }


  /**
   * @param nextTopicId The nextTopicId to set.
   */
  public void setNextTopicId(String nextTopicId)
  {
    this.nextTopicId = nextTopicId;
  }


  /**
   * @return Returns the previousTopicId.
   */
  public String getPreviousTopicId()
  {
    return previousTopicId;
  }


  /**
   * @param previousTopicId The previousTopicId to set.
   */
  public void setPreviousTopicId(String previousTopicId)
  {
    this.previousTopicId = previousTopicId;
  }


  /**
   * @return Returns the decorated messages.
   */
  public List getMessages()
  {
     return messages ;
  }  
  
   

  public void addMessage(DiscussionMessageBean decoMessage)
  {
    if(!messages.contains(decoMessage))
    {
      messages.add(decoMessage);    
    }
  } 
}
