<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<f:loadBundle basename="org.sakaiproject.tool.messageforums.bundle.Messages" var="msgs"/>
<link href='/sakai-messageforums-tool/css/msgForums.css' rel='stylesheet' type='text/css' />
<f:view>
   <sakai:view>
      <h:form id="msgForum">
		<div class="forumsRow">
			<div class="left-header-section">
				<h3>
					<h:commandLink action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_message_forums}"/> /
					<h:outputText value="#{ForumTool.selectedForum.forum.title}" />
				</h3>
				 <sakai:instruction_message value="#{ForumTool.selectedForum.forum.shortDescription}" />
				  <h:commandLink immediate="true" action="#{ForumTool.processActionToggleDisplayForumExtendedDescription}" rendered="#{ForumTool.selectedForum.hasExtendedDesciption}"
				 	id="forum_extended_show" value="#{msgs.cdfm_read_full_description}">
					 <f:param value="#{forum.forum.id}" name="forumId"/>
					 <f:param value="processActionDisplayForum" name="redirectToProcessAction"/>
				 </h:commandLink>
				<%-- <h:inputTextarea rows="5" cols="100" id="forum_extended_description" disabled="true" value="#{ForumTool.selectedForum.forum.extendedDescription}" rendered="#{ForumTool.selectedForum.readFullDesciption}"/> --%>
					 <sakai:inputRichText rows="5" cols="110" buttonSet="none" readonly="true" showXPath="false" id="forum_extended_description" value="#{ForumTool.selectedForum.forum.extendedDescription}" rendered="#{ForumTool.selectedForum.readFullDesciption}"/>
					<f:verbatim><br/></f:verbatim>
				<h:commandLink immediate="true" action="#{ForumTool.processActionToggleDisplayForumExtendedDescription}" id="forum_extended_hide"
						 value="#{msgs.cdfm_hide_full_description}" rendered="#{ForumTool.selectedForum.readFullDesciption}">
					<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
					<f:param value="processActionDisplayForum" name="redirectToProcessAction"/>
				</h:commandLink>
			</div>
			<div class="right-header-section">
				<h:commandLink action="#{ForumTool.processActionNewTopic}"  value="#{msgs.cdfm_new_topic}" rendered="#{ForumTool.selectedForum.newTopic}" >
					<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandLink action="#{ForumTool.processActionForumSettings}" value="#{msgs.cdfm_forum_settings}" rendered="#{ForumTool.selectedForum.changeSettings}">
					<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
				</h:commandLink>
		 	</div>
		</div>
		<h:dataTable id="topics" styleClass="listHier" value="#{ForumTool.selectedForum.topics}" var="topic">
			<h:column>

				<h:commandLink action="#{ForumTool.processActionDisplayTopic}" id="topic_title" value="#{topic.topic.title}">
					<f:param value="#{topic.topic.id}" name="topicId"/>
					<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
				</h:commandLink>
				<h:outputText id="topic_msg_count" value=" (#{topic.totalNoMessages} messages - #{topic.unreadNoMessages} unread)"/>
				<h:commandLink action="#{ForumTool.processActionTopicSettings}" id="topic_setting" styleClass="rightAlign" value="#{msgs.cdfm_topic_settings}"
				rendered="#{topic.changeSettings}">
					<f:param value="#{topic.topic.id}" name="topicId"/>
					<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
				</h:commandLink>
				<f:verbatim><br/><br/></f:verbatim>
				<h:outputText id="topic_desc" value="#{topic.topic.shortDescription}" />
				<f:verbatim><hr/></f:verbatim>

				<h:dataTable id="messages" value="#{topics.messages}" var="message">
					<h:column>
						<h:outputText id="message_title" value="#{message.message.title}"/>
						<f:verbatim><br/></f:verbatim>
						<h:outputText id="message_desc" value="#{message.message.shortDescription}" />
					</h:column>
				</h:dataTable>
			</h:column>
		</h:dataTable>
	 </h:form>
    </sakai:view>
</f:view>

