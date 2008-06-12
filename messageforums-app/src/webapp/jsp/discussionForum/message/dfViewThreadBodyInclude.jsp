<%-- Display single message in threaded view. (included for each message). --%>
<%-- designNote: what does read/unread mean in this context since I am seeing the whole message?--%>
<h:outputText escape="false" value="<a id=\"#{message.message.id}\" name=\"#{message.message.id}\"></a>" />
	<f:verbatim><div class="hierItemBlock"></f:verbatim>
			<%-- a deleted message --%>
			<h:panelGroup styleClass="inactive" rendered="#{message.deleted}" >
				<f:verbatim><span></f:verbatim>
					<h:outputText value="#{msgs.cdfm_msg_deleted_label}" />
				<f:verbatim></span></f:verbatim>
			</h:panelGroup>
			<%-- non deleted messages --%>
			<h:panelGroup rendered="#{!message.deleted}">
				<h:outputText styleClass="messageNew" value=" #{msgs.cdfm_newflag}" rendered="#{!message.read}"/>
				<%--pending  message flag --%>
				<h:outputText value="#{msgs.cdfm_msg_pending_label}" rendered="#{message.msgPending}" styleClass="messagePending"/>
				<%--denied  message flag --%>
				<h:outputText value="#{msgs.cdfm_msg_denied_label}" rendered="#{message.msgDenied}" styleClass="messageDenied" />
				<%--message subject line --%>
				<h:commandLink action="#{ForumTool.processActionDisplayMessage}" immediate="true" title=" #{message.message.title}" styleClass="title">
					<h:outputText value="#{message.message.title}" rendered="#{message.read}" />
					<h:outputText styleClass="unreadMsg" value="#{message.message.title}" rendered="#{!message.read}" />
					<f:param value="#{message.message.id}" name="messageId" />
					<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
					<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId" />
				</h:commandLink>
				<h:outputText value="<br />" escape="false" />
				<%--author --%>
				<h:outputText value="  #{message.message.author}" rendered="#{message.read}" styleClass="textPanelFooter"/>
				<h:outputText  value="  #{message.message.author}" rendered="#{!message.read }" styleClass="unreadMsg textPanelFooter"/>
				<%--date --%>
				<h:outputText value="#{message.message.created}" rendered="#{message.read}" styleClass="textPanelFooter">
					<f:convertDateTime pattern="#{msgs.date_format_paren}" />
				</h:outputText>
				<h:outputText  value="#{message.message.created}" rendered="#{!message.read}" styleClass="unreadMsg textPanelFooter">
					<f:convertDateTime pattern="#{msgs.date_format_paren}" />
				</h:outputText>
				<%-- mark as read link --%>
				<h:graphicImage value="/images/12-em-check.png"
					alt="#{msgs.cdfm_mark_as_read}" 
					title="#{msgs.cdfm_mark_as_read}" 
					rendered="#{!message.read}"
					style="cursor:pointer;margin-left:1em"
					onclick="doAjax(#{message.message.id}, #{ForumTool.selectedTopic.topic.id}, this);"/>
			</h:panelGroup>
		<%-- reply and other actions panel --%>
			<%-- If message actually deleted, don't display links --%>
			<h:panelGroup rendered="#{!message.deleted}" styleClass="itemToolBar">
				<%-- Reply link --%>
				<h:panelGroup rendered="#{ForumTool.selectedTopic.isNewResponseToResponse && message.msgApproved && !ForumTool.selectedTopic.locked}">
					<h:commandLink action="#{ForumTool.processDfMsgReplyMsgFromEntire}" title="#{msgs.cdfm_reply}"> 
						<h:graphicImage value="/../../library/image/silk/email_go.png" alt="#{msgs.cdfm_button_bar_reply_to_msg}" />
						<h:outputText value="#{msgs.cdfm_reply}" />
						<f:param value="#{message.message.id}" name="messageId" />
						<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
						<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId" />
					</h:commandLink>
				</h:panelGroup>
				<%-- (Hide) Other Actions links --%>
				<h:panelGroup rendered="#{(ForumTool.selectedTopic.isPostToGradebook && ForumTool.gradebookExist) || ForumTool.selectedTopic.isModeratedAndHasPerm || message.revise 
						|| message.userCanDelete}">
						<h:outputText value=" #{msgs.cdfm_toolbar_separator} " rendered="#{ForumTool.selectedTopic.isNewResponseToResponse && message.msgApproved && !ForumTool.selectedTopic.locked}" />
						<h:outputLink value="#" onclick="toggleDisplayInline('#{message.message.id}_advanced_box'); return false;">
						<h:graphicImage value="/../../library/image/silk/cog.png" alt="#{msgs.cdfm_other_actions}" />
						<h:outputText value="#{msgs.cdfm_other_actions}" />
					</h:outputLink>
				</h:panelGroup>
				<%--//designNote: panel holds other actions, display toggled above (do some testing - do they show up when they should not? Do I get a 
						"moderate" link when it is not a moderated context, or when the message is mine?) --%>
				<h:outputText escape="false" value="<span id=\"#{message.message.id}_advanced_box\" class=\"otherActions\" style=\"display:none\">" />
					<%-- link to grade --%>
					<h:panelGroup rendered="#{ForumTool.selectedTopic.isPostToGradebook && ForumTool.gradebookExist}">
						<h:commandLink action="#{ForumTool.processDfMsgGrdFromThread}" value="#{msgs.cdfm_button_bar_grade}">
							<f:param value="#{message.message.id}" name="messageId" />
							<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
							<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId" />
						</h:commandLink>
						<h:outputText value=" #{msgs.cdfm_toolbar_separator} " />
					</h:panelGroup>
					<%-- Revise other action --%>
					<h:panelGroup rendered="#{message.revise}">
						<h:commandLink action="#{ForumTool.processDfMsgRvsFromThread}" value="#{msgs.cdfm_button_bar_revise}">
							<f:param value="#{message.message.id}" name="messageId" />
							<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
							<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId" />
						</h:commandLink>
						<h:outputText value=" #{msgs.cdfm_toolbar_separator} " />
					</h:panelGroup>
					<%-- Delete other action --%>
					<h:panelGroup rendered="#{message.userCanDelete}" >
						<h:commandLink action="#{ForumTool.processDfMsgDeleteConfirm}" value="#{msgs.cdfm_button_bar_delete}">
							<f:param value="#{message.message.id}" name="messageId" />
							<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
							<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId" />
							<f:param value="dfViewThread" name="fromPage" />
						</h:commandLink>
						<h:outputText value=" #{msgs.cdfm_toolbar_separator} " rendered="#{ForumTool.selectedTopic.isModeratedAndHasPerm}" />
					</h:panelGroup>
					<%-- Moderate other action --%>
					<h:panelGroup rendered="#{ForumTool.selectedTopic.isModeratedAndHasPerm}">
						<h:commandLink action="#{ForumTool.processActionDisplayMessage}" immediate="true" title=" #{msgs.cdfm_moderate}">
							<h:outputText value="#{msgs.cdfm_moderate}" />
							<f:param value="#{message.message.id}" name="messageId" />
							<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId" />
							<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId" />
						</h:commandLink>
					</h:panelGroup>
			</h:panelGroup>
			<!-- close the div with class of specialLink -->
			<f:verbatim></span></f:verbatim>
		<%-- a float clearer --%>
		<f:verbatim><div style="clear:both;height:1px;width:100%;" class="titleBarBorder"></div></f:verbatim>
	<%-- the message body--%>
	<mf:htmlShowArea value="#{message.message.body}" hideBorder="true" rendered="#{!message.deleted}" />
	<%-- attach list --%>	
	<h:panelGroup rendered="#{!empty message.attachList && !message.deleted}">
		<h:dataTable value="#{message.attachList}" var="eachAttach" styleClass="attachListJSF" rendered="#{!empty message.attachList}" style="font-size:.9em;width:auto;margin-left:1em">
			<h:column rendered="#{!empty message.message.attachments}">
				<h:graphicImage url="/images/attachment.gif"  />
				<h:outputLink value="#{eachAttach.url}" target="_blank">
					<h:outputText value=" " />
					<h:outputText value="#{eachAttach.attachment.attachmentName}" />
				</h:outputLink>
			</h:column>
		</h:dataTable>
	</h:panelGroup>
	<%-- close the div with class of hierItemBlock --%>
<f:verbatim></div></f:verbatim>