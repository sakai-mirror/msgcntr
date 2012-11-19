<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>

<f:view>
	<sakai:view title="Forums">
<link rel="stylesheet" href="/messageforums-tool/css/jquery-ui-1.7.2.custom.css" type="text/css" />
<link rel="stylesheet" href="/messageforums-tool/css/msgcntr.css" type="text/css" />
<link rel="stylesheet" href="/messageforums-tool/css/msgcntr_move_thread.css" type="text/css" />

<!-- messageforums-app/src/webapp/jsp/discussionForum/message-->

<script type="text/javascript" src="/library/js/jquery.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/json2.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/fluidframework-min.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/Scroller.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/ui.dialog.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/ui.draggable.js"></script>

<script type="text/javascript" src="/messageforums-tool/js/forum.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/frameAdjust.js"></script>
<script type="text/javascript" src="/messageforums-tool/js/forum_movethread.js"></script>

<sakai:script contextBase="/messageforums-tool" path="/js/sak-10625.js"/>

<!--jsp/discussionForum/message/dfAllMessages.jsp-->
		<link rel="stylesheet" type="text/css" href="../../css/TableSorter.css" />
  		<script type="text/javascript" src="/library/js/jquery-latest.min.js"></script>
 		<sakai:script contextBase="/messageforums-tool" path="/js/jquery.tablesorter.js"/>
 		<sakai:script contextBase="/messageforums-tool" path="/js/forumTopicThreadsSorter.js"/>
 		<script language="JavaScript">
 		jQuery(document).ready(function(){
 			//sort forum threads
 			$('#msgForum\\:messagesInHierDataTable').threadsSorter();
			//add handles to list for thread operat
			instrumentThreads('msgForum\\:messagesInHierDataTable');
 		});

                function disableMoveLink(){
                        var linkid = "msgForum:df_move_message_commandLink";
                        var movelink = document.getElementById(linkid);
                	movelink.style.color="grey";
                }

                function enableMoveLink(){
                        var linkid = "msgForum:df_move_message_commandLink";
                        var movelink = document.getElementById(linkid);
                	movelink.style.color="";
                }
 
// this is  called from messageforums-app/src/java/org/sakaiproject/tool/messageforums/jsf/HierDataTableRender.java. checkbox is encoded there.  

                function enableDisableMoveThreadLink(){
                        //function to check total number of CheckBoxes that are checked in a form
                //initialize total count to zero
                var totalChecked = 0;
                                //get total number of CheckBoxes in form
                        if (typeof document.forms['msgForum'].moveCheckbox.length === 'undefined') {
                   /*when there is just one checkbox moveCheckbox is not an array,  document.forms['msgForum'].moveCheckbox.length returns undefined */
                        if (document.forms['msgForum'].moveCheckbox.checked == true )
                        {
                                        totalChecked += 1;
                        }
                        }
                        else {
                // more than one checkbox is checked.
                var chkBoxCount = document.forms['msgForum'].moveCheckbox.length;
                //loop through each CheckBox
                        for (var i = 0; i < chkBoxCount; i++)
                        {
                                //check the state of each CheckBox
                                if (eval("document.forms['msgForum'].moveCheckbox[" + i + "].checked") == true)
                                {
                                        //it's checked so increment the counter
                                        totalChecked += 1;
                                }
                        }
                        }
                        if (totalChecked >0){
                                // enable the move link
                                enableMoveLink();
                        }
                        else {
                                disableMoveLink();
                        }

                }

 		</script>
		<h:outputText styleClass="showMoreText"  style="display:none" value="#{msgs.cdfm_show_more_full_description}"  />

	<%--//
		//plugin required below
		<sakai:script contextBase="/messageforums-tool" path="/js/pxToEm.js"/>
		
		/*
		gsilver: get a value representing max indents
	 	from the server configuraiton service or the language bundle, parse 
		all the indented items, and if the item indent goes over the value, flatten to the value 
		*/
		<script type="text/javascript">
		$(document).ready(function() {
			// pick value from element (that gets it from language bundle)
			maxThreadDepth =$('#maxthreaddepth').text()
			// double check that this is a number
			if (isNaN(maxThreadDepth)){
				maxThreadDepth=10
			}
			// for each message, if the message is indented more than the value above
			// void that and set the new indent to the value
			$("td.messageTitle").each(function (i) {
				paddingDepth= $(this).css('padding-left').split('px');
				if ( paddingDepth[0] > parseInt(maxThreadDepth.pxToEm ({scope:'body', reverse:true}))){
					$(this).css ('padding-left', maxThreadDepth + 'em');
				}
			});
		});

		</script>	
		// element into which the value gets insert and retrieved from
		<span class="highlight"  id="maxthreaddepth" class="skip"><h:outputText value="#{msgs.cdfm_maxthreaddepth}" /></span>
//--%>
	<h:form id="msgForum" rendered="#{!ForumTool.selectedTopic.topic.draft || ForumTool.selectedTopic.topic.createdBy == ForumTool.userId}">
<f:subview id="picker2">
        <%@ include file="moveThreadPicker.jsp" %>
</f:subview>

		<sakai:tool_bar separator="#{msgs.cdfm_toolbar_separator}">
				<%--
				<sakai:tool_bar_item value="#{msgs.cdfm_container_title_thread}" action="#{ForumTool.processAddMessage}" id="df_compose_message_dfAllMessages"
		  			rendered="#{ForumTool.selectedTopic.isNewResponse && !ForumTool.selectedTopic.locked}" />
						--%>
						
				<%--
						  <sakai:tool_bar_item value="#{msgs.cdfm_container_title_thread}" action="#{ForumTool.processAddMessage}" id="df_compose_message_dfAllMessages"
						  	rendered="#{ForumTool.selectedTopic.isNewResponse && !ForumTool.selectedTopic.locked && !ForumTool.selectedForum.locked == 'true'}" />
						--%>
						
      	<sakai:tool_bar_item value="#{msgs.cdfm_flat_view}" action="#{ForumTool.processActionDisplayFlatView}" />
			<%--	
				<h:commandLink action="#{ForumTool.processActionTopicSettings}" id="topic_setting" value="#{msgs.cdfm_topic_settings}" 
				rendered="#{ForumTool.selectedTopic.changeSettings}"> 
					<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
				</h:commandLink>
			--%>
				
				<h:commandLink action="#{ForumTool.processActionDeleteTopicConfirm}" id="delete_confirm" 
				value="#{msgs.cdfm_button_bar_delete_topic}" accesskey="d" rendered="#{!ForumTool.selectedTopic.markForDeletion && ForumTool.displayTopicDeleteOption}">
				<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
				</h:commandLink>
				
				<h:outputLink id="print" value="javascript:printFriendly('#{ForumTool.printFriendlyUrl}');">
					<h:graphicImage url="/../../library/image/silk/printer.png" alt="#{msgs.print_friendly}" title="#{msgs.print_friendly}" />
				</h:outputLink>
 		</sakai:tool_bar>
 		<h:messages styleClass="alertMessage" id="errorMessages" rendered="#{! empty facesContext.maximumSeverity}" />
			<h:panelGrid columns="2" width="100%" styleClass="specialLink">
			    <h:panelGroup>
					<f:verbatim><div class="specialLink"><h3></f:verbatim>
			      <h:commandLink action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title=" #{msgs.cdfm_message_forums}"
			      		rendered="#{ForumTool.messagesandForums}" />
			      <h:commandLink action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_discussion_forums}" title=" #{msgs.cdfm_discussion_forums}"
			      		rendered="#{ForumTool.forumsTool}" />
      			  <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
					  <h:commandLink action="#{ForumTool.processActionDisplayForum}" title="#{ForumTool.selectedForum.forum.title}" rendered="#{ForumTool.showForumLinksInNav}">
						<h:outputText value="#{ForumTool.selectedForum.forum.title}"/>
						  <f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
					  </h:commandLink>
					  <h:outputText value="#{ForumTool.selectedForum.forum.title}" rendered="#{!ForumTool.showForumLinksInNav}"/>
					  <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
					  <h:outputText value="#{ForumTool.selectedTopic.topic.title}" />
						<%--//designNote: up arrow should go here - get decent image and put title into link. --%>
						<h:commandLink action="#{ForumTool.processActionDisplayForum}"  title="#{msgs.cdfm_up_level_title}" rendered="#{ForumTool.showForumLinksInNav}" style="margin-left:.3em">
							<h:graphicImage url="/images/silk/arrow_turn_up.gif" style="vertical-align:top;padding:0;margin-top:-2px"/>	
							<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
						</h:commandLink>
					  <f:verbatim></h3></div></f:verbatim>
				 </h:panelGroup>
				 <h:panelGroup styleClass="itemNav">
				   <h:outputText   value="#{msgs.cdfm_previous_topic}"  rendered="#{!ForumTool.selectedTopic.hasPreviousTopic}" />
					 <h:commandLink action="#{ForumTool.processActionDisplayPreviousTopic}" value="#{msgs.cdfm_previous_topic}"  rendered="#{ForumTool.selectedTopic.hasPreviousTopic}" 
					                title=" #{msgs.cdfm_previous_topic}">
						 <f:param value="#{ForumTool.selectedTopic.previousTopicId}" name="previousTopicId"/>
						 <f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
					 </h:commandLink>
					 <f:verbatim><h:outputText  id="blankSpace1" value=" |  " /></f:verbatim>				
					 <h:outputText   value="#{msgs.cdfm_next_topic}" rendered="#{!ForumTool.selectedTopic.hasNextTopic}" />
					 <h:commandLink action="#{ForumTool.processActionDisplayNextTopic}" value="#{msgs.cdfm_next_topic}" rendered="#{ForumTool.selectedTopic.hasNextTopic}" 
					                title=" #{msgs.cdfm_next_topic}">
						<f:param value="#{ForumTool.selectedTopic.nextTopicId}" name="nextTopicId"/>
						<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
					 </h:commandLink>
				 </h:panelGroup>
			  </h:panelGrid>
		
			<h:panelGrid columns="1" width="100%"  styleClass="topicBloc topicBlocLone specialLink"  cellspacing="0" cellpadding="0">
				<h:panelGroup>
					<h:outputText styleClass="highlight title" id="draft" value="#{msgs.cdfm_draft}" rendered="#{ForumTool.selectedTopic.topic.draft == 'true'}"/>
					<h:outputText id="draft_space" value="  - " rendered="#{ForumTool.selectedTopic.topic.draft == 'true'}" styleClass="title"/>
					<h:graphicImage url="/images/silk/date_delete.png" title="#{msgs.topic_restricted_message}" alt="#{msgs.topic_restricted_message}" rendered="#{ForumTool.selectedTopic.topic.availability == 'false'}" style="margin-right:.5em"/>
					<h:graphicImage url="/images/silk/lock.png" alt="#{msgs.cdfm_forum_locked}" rendered="#{ForumTool.selectedForum.forum.locked == 'true' || ForumTool.selectedTopic.topic.locked == 'true'}" style="margin-right:.5em"/>
					  
					
					
					<h:outputText value="#{ForumTool.selectedTopic.topic.title}" styleClass="title"/>
					
					<%-- // display singular ('message') if one message --%>
					<h:outputText styleClass="textPanelFooter" id="topic_msg_count55" value=" #{msgs.cdfm_openb} #{ForumTool.selectedTopic.totalNoMessages} #{msgs.cdfm_lowercase_msg} - #{ForumTool.selectedTopic.unreadNoMessages} #{msgs.cdfm_unread} " 
								rendered="#{ForumTool.selectedTopic.totalNoMessages == 1}"/>
						<%-- // display plural ('messages') if 0 or more than 1 messages --%>
						<h:outputText id="topic_msg_count56" value=" #{msgs.cdfm_openb} #{ForumTool.selectedTopic.totalNoMessages} #{msgs.cdfm_lowercase_msgs} - #{ForumTool.selectedTopic.unreadNoMessages} #{msgs.cdfm_unread} " 
						  rendered="#{(ForumTool.selectedTopic.totalNoMessages > 1 || ForumTool.selectedTopic.totalNoMessages == 0) }" styleClass="textPanelFooter" />
						<h:outputText id="topic_moderated" value="#{msgs.cdfm_topic_moderated_flag} " styleClass="textPanelFooter" rendered="#{ForumTool.selectedTopic.moderated == 'true' }" />
						<h:outputText value="#{msgs.cdfm_closeb}" styleClass="textPanelFooter"/>
					
					  <%--//designNote: for paralellism to other views, need to add read/unread count here as well as Moderated attribute--%>  
					  <%-- 
					  <h:outputText value=" #{msgs.cdfm_openb}" styleClass="textPanelFooter"/> 
					  <h:outputText value="123 messages - 5 unread" styleClass="textPanelFooter todo" />
					  <h:outputText id="topic_moderated" value="  #{msgs.cdfm_topic_moderated_flag}"  styleClass="textPanelFooter" rendered="#{ForumTool.selectedTopic.topic.moderated == 'true'}" />
					  <h:outputText value="#{msgs.cdfm_closeb}" styleClass="textPanelFooter"/>
					  --%>
					  <h:outputText value=" "  styleClass="actionLinks"/>
					  <%--//designNote: for paralellism to other views, need to move the "Post new thread" link below, but it is a sakai:toolbar item above...same for Topic Settings --%>
					<sakai:tool_bar_item action="#{ForumTool.processActionTopicSettings}" id="topic_setting" value="#{msgs.cdfm_topic_settings}" 
					rendered="#{ForumTool.selectedTopic.changeSettings}" /> 
					
					<f:verbatim><br/></f:verbatim>
					<f:verbatim><div class="post_move_links"></f:verbatim>
					  <sakai:tool_bar_item value="#{msgs.cdfm_container_title_thread}" action="#{ForumTool.processAddMessage}" id="df_compose_message_dfAllMessages"
					  	rendered="#{ForumTool.selectedTopic.isNewResponse && !ForumTool.selectedTopic.locked}" />
					<h:outputText  value=" | " />


<%-- hidden link to call ForumTool.processMoveThread  --%>
<h:commandLink value="" action="#{ForumTool.processMoveThread}" id="hidden_move_message_commandLink" ></h:commandLink>

<%-- link for Move Thread(s)  --%>
					<f:verbatim>
<a style="color:grey" class="display-topic-picker" id="msgForum:df_move_message_commandLink" onclick="resizeFrameForDialog();" href="#" >
				</f:verbatim>
<h:outputText value="#{msgs.move_thread}" />
					<f:verbatim></a></f:verbatim>
					<f:verbatim></div></f:verbatim>

					<h:outputText   value="#{ForumTool.selectedTopic.topic.shortDescription}" rendered="#{ForumTool.selectedTopic.topic.shortDescription} != ''}"  styleClass="shortDescription" />
					
					
					
						<h:outputLink id="forum_extended_show" value="#" title="#{msgs.cdfm_view}" styleClass="show"
								rendered="#{!empty ForumTool.selectedTopic.attachList || ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"
							onclick="resize();$(this).next('.hide').toggle(); $('div.toggle').slideToggle(resize);$(this).toggle();">
								<h:graphicImage url="/images/collapse.gif"/><h:outputText value="#{msgs.cdfm_view}" />
								<h:outputText value=" #{msgs.cdfm_full_description}" rendered="#{ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"/>
								<h:outputText value=" #{msgs.cdfm_and}" rendered="#{!empty ForumTool.selectedTopic.attachList && ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"/>
								<h:outputText value=" #{msgs.cdfm_attach}" rendered="#{!empty ForumTool.selectedTopic.attachList}"/>
					  </h:outputLink>
				   
			
					<h:outputLink id="forum_extended_hide" value="#" title="#{msgs.cdfm_hide}" style="display:none " styleClass="hide" 
								rendered="#{!empty ForumTool.selectedTopic.attachList || ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"
							onclick="resize();$(this).prev('.show').toggle(); $('div.toggle').slideToggle(resize);$(this).toggle();">
								<h:graphicImage url="/images/expand.gif"/><h:outputText value="#{msgs.cdfm_hide}" />
								<h:outputText value=" #{msgs.cdfm_full_description}" rendered="#{ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"/>
								<h:outputText value=" #{msgs.cdfm_and}" rendered="#{!empty ForumTool.selectedTopic.attachList && ForumTool.selectedTopic.topic.extendedDescription != '' && ForumTool.selectedTopic.topic.extendedDescription != null && tForumTool.selectedTopic.topic.extendedDescription != '<br/>'}"/>
								<h:outputText value=" #{msgs.cdfm_attach}" rendered="#{!empty ForumTool.selectedTopic.attachList}"/>
					  </h:outputLink>
					
					<f:verbatim><div class="toggle" style="display:none"></f:verbatim>
						<mf:htmlShowArea  id="forum_fullDescription" hideBorder="true"	 value="#{ForumTool.selectedTopic.topic.extendedDescription}"/> 
						<h:dataTable styleClass="listHier" value="#{ForumTool.selectedTopic.attachList}" var="eachAttach" rendered="#{!empty ForumTool.selectedTopic.attachList}" cellpadding="3" cellspacing="0" columnClasses="attach,bogus" style="font-size:.9em;width:auto;margin-left:1em">
					  <h:column>
						<sakai:contentTypeMap fileType="#{eachAttach.attachment.attachmentType}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
						<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />						
						</h:column>
						<h:column>
						<h:outputLink value="#{eachAttach.url}" target="_blank">
							<h:outputText value="#{eachAttach.attachment.attachmentName}" />
						</h:outputLink>				  
					</h:column>
			  </h:dataTable>
					<f:verbatim></div></f:verbatim>
				</h:panelGroup>
			</h:panelGrid>	
				<%--<%@ include file="dfViewSearchBar.jsp"%> --%>
   		
			<%--//designNote: need a rendered attribute here that will toggle the display of the table (if messages) or a textblock (class="instruction") if there are no messages--%>
			<h:outputText styleClass="messageAlert" value="#{msgs.cdfm_postFirst_warning}" rendered="#{ForumTool.needToPostFirst}"/>				
			<h:outputText value="#{msgs.cdfm_no_messages}" rendered="#{empty ForumTool.selectedTopic.messages && !ForumTool.needToPostFirst}"  styleClass="instruction" style="display:block"/>
			<%--//designNote: need a rendered attribute here that will toggle the display of the table (if messages) or a textblock (class="instruction") if there are no messages--%> 				
			<h:outputText value="#{msgs.cdfm_no_messages}" rendered="#{empty ForumTool.messages}"  styleClass="instruction" style="display:block"/>
			<%--//gsilver: need a rendered attribute here that will toggle the display of the table (if messages) or a textblock (class="instruction") if there are no messages--%> 						
<div id="checkbox">
			<mf:hierDataTable styleClass=" listHier  specialLink allMessages" id="messagesInHierDataTable" rendered="#{!empty ForumTool.messages}"  value="#{ForumTool.messages}" var="message" expanded="#{ForumTool.expanded}"
					columnClasses="attach, attach,messageTitle,attach,bogus,bogus" cellspacing="0" cellpadding="0" style="border:none">		
			<h:column id="_checkbox">
				<f:facet name="header">
				</f:facet>
			</h:column>
			<h:column id="_toggle">
				<f:facet name="header">			
					<h:commandLink action="#{ForumTool.processActionToggleExpanded}" immediate="true" title="#{msgs.cdfm_collapse_expand_all}">
							<h:graphicImage value="/images/collapse-expand.gif" style="vertical-align:middle" rendered="#{ForumTool.expanded == 'true'}" />
							<h:graphicImage value="/images/expand-collapse.gif" style="vertical-align:middle" rendered="#{ForumTool.expanded != 'true'}" />
					</h:commandLink>
				</f:facet>				
			</h:column>
			<h:column id="_msg_subject">
				<f:facet name="header">
					<h:outputText value="#{msgs.cdfm_thread}" />
				</f:facet>

				<%-- moved--%>
				<h:panelGroup rendered="#{message.moved}">
					<h:outputText styleClass="textPanelFooter" escape="false" value="| #{message.message.title} - " />
					<h:commandLink action="#{ForumTool.processActionDisplayTopic}" id="topic_title" styleClass="title">
						<h:outputText value="#{msgs.moved}" />
                                                  <f:param value="#{message.message.topic.id}" name="topicId"/>
                                                  <f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
                                          </h:commandLink>

					<h:outputText escape="false" styleClass="textPanelFooter"  value=" to #{message.message.topic.title}" />
				</h:panelGroup>
				<%-- moved--%>

				<%-- NOT moved--%>
				<h:panelGroup rendered="#{!message.moved}">
				<h:outputText escape="false" value="<a id=\"#{message.message.id}\" name=\"#{message.message.id}\"></a>" />
					<%-- display deleted message linked if any child messages (not deleted)
						displays the message "this message has been deleted" if the message has been, um deleted, leaves reply children in place --%>
					<h:panelGroup styleClass="inactive firstChild" rendered="#{message.deleted && message.depth == 0 && message.childCount > 0}">
					<h:commandLink action="#{ForumTool.processActionDisplayThread}" immediate="true" title="#{msgs.cdfm_msg_deleted_label}" >
						<h:outputText value="#{msgs.cdfm_msg_deleted_label}" />
       	    			<f:param value="#{message.message.id}" name="messageId"/>
   	    		    	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
    			    	<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId"/>
					</h:commandLink>
				</h:panelGroup>
				
					<h:panelGroup rendered="#{!message.deleted}" styleClass="firstChild">
				
					<h:outputText styleClass="messageNew" value=" #{msgs.cdfm_newflag}" rendered="#{!message.read}"/>	

					<%-- message has been submitted and is pending approval by moderator --%>
						<h:outputText value="#{msgs.cdfm_msg_pending_label}" styleClass="messagePending" rendered="#{message.msgPending}" />
						<%-- message has been submitted and has bene denied  approval by moderator --%>
						<h:outputText value="#{msgs.cdfm_msg_denied_label}"  styleClass="messageDenied"  rendered="#{message.msgDenied}" />
					<%-- Rendered to view current thread only --%>
							<%--//designNote:  not sure what this controls - seems to affect all threads except the deleted, pending and denied--%>
					<h:commandLink styleClass="messagetitlelink" action="#{ForumTool.processActionDisplayThread}" immediate="true" title="#{message.message.title}"
						rendered="#{message.depth == 0}">
				   		<h:outputText value="#{message.message.title}" rendered="#{message.read && message.childUnread == 0 }" />
							
    	        		<h:outputText styleClass="unreadMsg" value="#{message.message.title}" rendered="#{!message.read || message.childUnread > 0}"/>
	       	    		<f:param value="#{message.message.id}" name="messageId"/>
    	    	    	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
   	    		    	<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId"/>
	    	      	</h:commandLink>
				</h:panelGroup>

				<%-- Rendered to view current message only --%>
					<%-- shows the message "This message has been deleted" if the message has been deleted --%>
					<h:panelGroup styleClass="inactive firstChild" rendered="#{message.deleted && (message.depth != 0 || message.childCount == 0)}" >
					<f:verbatim><span></f:verbatim>
						<h:outputText value="#{msgs.cdfm_msg_deleted_label}" />
					<f:verbatim></span></f:verbatim>
				</h:panelGroup>

					<%-- render the message that has not been deleted, what else? --%>
				<h:panelGroup rendered="#{!message.deleted}">	          	
					<h:commandLink styleClass="messagetitlelink" action="#{ForumTool.processActionDisplayMessage}" immediate="true" title=" #{message.message.title}"
						rendered="#{message.depth != 0}" >
					   	<h:outputText value="#{message.message.title}" rendered="#{message.read}" />
    	    	    	<h:outputText styleClass="unreadMsg" value="#{message.message.title}" rendered="#{!message.read}"/>
    	   	    		<f:param value="#{message.message.id}" name="messageId"/>
       		    		<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
        		    	<f:param value="#{ForumTool.selectedTopic.topic.baseForum.id}" name="forumId"/>
	          		</h:commandLink>
						<%-- //designNote: icon to mark as read, does it belong here? Is it the right icon? Is this functionality desired?--%>
						<%--
		          	<h:outputText value="  " />
				
						<h:graphicImage value="/images/trans.gif" rendered="#{!message.read}"
							alt="#{msgs.cdfm_mark_as_read}" title="#{msgs.cdfm_mark_as_read}"
							onclick="doAjax(#{message.message.id}, #{ForumTool.selectedTopic.topic.id}, this);" styleClass="markAsReadIcon"/>
						--%>	
				</h:panelGroup>
					<%--  thread metadata (count) --%>
					<%-- designNote: debug block --%>
					<%--
					<h:outputText value=" md: #{message.depth}" /> 
					<h:outputText value=" cc: #{message.childCount }" />
					<h:outputText value=" cu: #{message.childUnread }" />
					<h:outputText value= " mr: #{message.read}" />
					--%>
					<h:outputText escape="false" styleClass="textPanelFooter" rendered="#{message.depth == 0 && message.childCount ==0}"  value=" #{msgs.cdfm_openb} #{message.childCount + 1} #{msgs.cdfm_lowercase_msg} - <em>#{(message.childUnread) + (message.read ? 0 : 1)}</em> #{msgs.cdfm_unread} #{msgs.cdfm_closeb}" />
					<h:outputText escape="false" styleClass="textPanelFooter" rendered="#{message.depth == 0 && message.childCount ==1}"  value=" #{msgs.cdfm_openb} #{message.childCount + 1} #{msgs.cdfm_lowercase_msgs} - <em>#{(message.childUnread) + (message.read ? 0 : 1)}</em> #{msgs.cdfm_unread} #{msgs.cdfm_closeb}" />
					<h:outputText  escape="false" styleClass="textPanelFooter" rendered="#{message.depth == 0 && message.childCount > 1}" value=" #{msgs.cdfm_openb} #{message.childCount + 1} #{msgs.cdfm_lowercase_msgs} - <em>#{(message.childUnread) + (message.read ? 0 : 1)}</em> #{msgs.cdfm_unread} #{msgs.cdfm_closeb}" />
					<h:outputText  value="#{msgs.cdfm_newflagresponses}" styleClass="childrenNew childrenNewThread" rendered="#{message.depth == 0 && ((message.childUnread) + (message.read ? 0 : 1)) > 0}"/>							

				<%-- NOT moved--%>
				</h:panelGroup>
			</h:column>
				<%-- author column --%>
			<h:column>
				<f:facet name="header">
						<h:outputText value="&nbsp;" escape="false"/>
					</f:facet>
               	
					<h:graphicImage value="/images/trans.gif" rendered="#{message.read}" style="margin-left:.5em"/>
					<h:graphicImage value="/images/trans.gif" rendered="#{!message.read}"
						alt="#{msgs.cdfm_mark_as_read}" title="#{msgs.cdfm_mark_as_read}"
						onclick="doAjax(#{message.message.id}, #{ForumTool.selectedTopic.topic.id}, this);" styleClass="markAsReadIcon"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{msgs.cdfm_authoredby}" />
				</f:facet>
				<h:panelGroup rendered="#{!message.deleted}" >
						<h:outputText value="#{message.message.author}"/>
						<%--<h:outputText styleClass="unreadMsg" value="#{message.message.author}" rendered="#{!message.read || message.childUnread > 0}"/>--%>
				</h:panelGroup>
			</h:column>
				<%-- date column --%>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{msgs.cdfm_date}" />
				</f:facet>
				<h:panelGroup rendered="#{!message.deleted}" >
					<h:outputText value="#{message.message.created}" rendered="#{message.read}">
						<f:convertDateTime pattern="#{msgs.date_format}" timeZone="#{ForumTool.userTimeZone}" locale="#{ForumTool.userLocale}"/>
					</h:outputText>
					<h:outputText styleClass="unreadMsg" value="#{message.message.created}" rendered="#{!message.read}">
						<f:convertDateTime pattern="#{msgs.date_format}" timeZone="#{ForumTool.userTimeZone}" locale="#{ForumTool.userLocale}"/>
					</h:outputText>
				</h:panelGroup>
			</h:column> 
		</mf:hierDataTable>
</div>
		
<input type="hidden" id="selectedTopicid" name="selectedTopicid" class="selectedTopicid" value="0" />
<input type="hidden" id="moveReminder" name="moveReminder" class="moveReminder" value="false" />

		<h:inputHidden id="mainOrForumOrTopic" value="dfAllMessages" />
		<%
  String thisId = request.getParameter("panel");
  if (thisId == null) 
  {
    thisId = "Main" + org.sakaiproject.tool.cover.ToolManager.getCurrentPlacement().getId();
  }
%>
			<script type="text/javascript">
			function resize(){
  				mySetMainFrameHeight('<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>');
  			}
			</script> 
<h:outputText escape="false" value="<script type='text/javascript'>$(document).ready(function() {setupLongDesc()});</script>"  rendered="#{!ForumTool.showShortDescription}"/>
	</h:form>

	<h:outputText value="#{msgs.cdfm_insufficient_privileges_view_topic}" rendered="#{ForumTool.selectedTopic.topic.draft && ForumTool.selectedTopic.topic.createdBy != ForumTool.userId}" />
		
</sakai:view>
</f:view>
