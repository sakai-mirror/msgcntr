<%@ page import="java.util.*, javax.faces.context.*, javax.faces.application.*,
                 javax.faces.el.*, org.sakaiproject.tool.messageforums.*"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>
<f:view>
	<sakai:view title="#{msgs.cdfm_discussion_topic_settings}" toolCssHref="/messageforums-tool/css/msgcntr.css">
	<script type="text/javascript" src="/library/js/jquery-ui-latest/js/jquery.min.js"></script>
	<script type="text/javascript" src="/library/js/jquery-ui-latest/js/jquery-ui.min.js"></script>
	<sakai:script contextBase="/messageforums-tool" path="/js/sak-10625.js"/>      
	<script type="text/javascript" src="/messageforums-tool/js/jquery.charcounter.js"> </script>
	<sakai:script contextBase="/messageforums-tool" path="/js/permissions_header.js"/>
	<sakai:script contextBase="/messageforums-tool" path="/js/forum.js"/>
	
	<sakai:script contextBase="/messageforums-tool" path="/js/datetimepicker.js"/>             		             		
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
	<script type="text/javascript">
	function setDatesEnabled(radioButton){
		$(".calWidget").fadeToggle('slow');
	}

	function openDateCal(){
		NewCal('revise:openDate','MMDDYYYY',true,12);
	}

	function closeDateCal(){
		NewCal('revise:closeDate','MMDDYYYY',true,12);	
	}
	</script>

<!--jsp/dfReviseTopicSettingsAttach.jsp-->
    <h:form id="revise">
			<h3 class="specialLink">
	      <h:commandLink action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title=" #{msgs.cdfm_message_forums}"
	      		rendered="#{ForumTool.messagesandForums}" />
	      <h:commandLink action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_discussion_forums}" title=" #{msgs.cdfm_discussion_forums}"
	      		rendered="#{ForumTool.forumsTool}" />
  			  <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
			  <h:commandLink action="#{ForumTool.processActionDisplayForum}" value="#{ForumTool.selectedForum.forum.title}" title=" #{ForumTool.selectedForum.forum.title}" rendered="#{ForumTool.showForumLinksInNav}">
				  <f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
			  </h:commandLink>
			  <h:outputText value="#{ForumTool.selectedForum.forum.title}" rendered="#{!ForumTool.showForumLinksInNav}"/>
			  <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
			  <h:outputText value="#{ForumTool.selectedTopic.topic.title}" />
				<h:outputText value="#{msgs.cdfm_discussion_topic_settings}" />

			</h3>

 			<div class="instruction">
  			<h:outputText id="instruction"  value="#{msgs.cdfm_settings_instruction}"/>
			 	<h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" />
			</div>
			<h:messages errorClass="messageAlert" infoClass="success" id="errorMessages" rendered="#{! empty facesContext.maximumSeverity}"/> 

			<h:panelGrid styleClass="jsfFormTable" columns="1"  columnClasses="shorttext">
			<h:panelGroup>
		
				<h:outputLabel id="outputLabel" for="topic_title"   style="padding-bottom:.3em;display:block;clear:both;float:none">
					<h:outputText id="req_star"  value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" style="padding-right:3px"/>
					<h:outputText value="#{msgs.cdfm_topic_title}" />
				</h:outputLabel>	 
				<h:inputText size="50" id="topic_title"  maxlength="250" value="#{ForumTool.selectedTopic.topic.title}">
					<f:validateLength minimum="1" maximum="255"/>
				</h:inputText>
			</h:panelGroup>	
			</h:panelGrid>
			<%-- //designNote: rendered attr below should resolve to false only if there is no prior short description
			 		and if there is server property (TBD) saying not to use it  - below just checking for pre-existing short description--%>
			<h:panelGrid columns="1"  columnClasses="longtext" rendered="#{ForumTool.showTopicShortDescription}">
				<h:panelGroup>
					<h:outputLabel id="outputLabel1" for="topic_shortDescription"  value="#{msgs.cdfm_shortDescription}" />

							<h:outputText value="#{msgs.cdfm_shortDescriptionCharsRem}"  styleClass="charRemFormat" style="display:none"/>
							<%--							
							<h:outputText value="%1 chars remain"  styleClass="charRemFormat" style="display:none"/>
							--%>
							<h:outputText value="" styleClass="charsRemaining" style="padding-left:3em;font-size:.85em;"/>
							<h:outputText value=""  style="display:block"/>
					

					<h:inputTextarea rows="3" cols="45" id="topic_shortDescription"  value="#{ForumTool.selectedTopic.topic.shortDescription}" styleClass="forum_shortDescriptionClass" style="float:none"/>
				</h:panelGroup>	
     	</h:panelGrid>

			<%--RTEditor area - if enabled--%>
		<h:panelGroup rendered="#{! ForumTool.disableLongDesc}">
				<h:outputText id="outputLabel2"   value="#{msgs.cdfm_fullDescription}" style="display:block;padding:.5em 0"/>
			<sakai:inputRichText textareaOnly="#{PrivateMessagesTool.mobileSession}" rows="#{ForumTool.editorRows}" cols="120" id="topic_description" value="#{ForumTool.selectedTopic.topic.extendedDescription}">
				<f:validateLength maximum="65000"/>
			</sakai:inputRichText>
		</h:panelGroup>
		
			<%--Attachment area  --%>
		<h4><h:outputText value="#{msgs.cdfm_att}"/></h4>
		
			<div style="padding-left:1em">
			<%--designNote: would be nice to make this an include, as well as a more comprehensive MIME type check  --%>
			<h:dataTable styleClass="attachPanel" id="attmsg" value="#{ForumTool.attachments}" var="eachAttach"  cellpadding="0" cellspacing="0" columnClasses="attach,bogus,specialLink,bogus,bogus" rendered="#{!empty ForumTool.attachments}">
				<h:column>
					<f:facet name="header">   <h:outputText escape="false"  value="&nbsp;"/>                                          
					</f:facet>
						<sakai:contentTypeMap fileType="#{eachAttach.attachment.attachmentType}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
						<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />							
				</h:column>
				<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.cdfm_title}"/>
						</f:facet>
							<h:outputText value="#{eachAttach.attachment.attachmentName}"/>
				</h:column>
				<h:column>
				<f:facet name="header"><h:outputText escape="false" value="&nbsp;"/>
				</f:facet>
							<h:commandLink action="#{ForumTool.processDeleteAttachSetting}" 
								immediate="true"
								onfocus="document.forms[0].onsubmit();"
								title="#{msgs.cdfm_remove}">
							<h:outputText value="#{msgs.cdfm_remove}"/>
								<f:param value="#{eachAttach.attachment.attachmentId}" name="dfmsg_current_attach"/>
							</h:commandLink>
				  </h:column>
					<h:column rendered="#{!empty ForumTool.attachments}">
						<f:facet name="header">
							<h:outputText value="#{msgs.cdfm_attsize}" />
						</f:facet>
						<h:outputText value="#{eachAttach.attachment.attachmentSize}"/>
					</h:column>
					<h:column rendered="#{!empty ForumTool.attachments}">
						<f:facet name="header">
		  			  <h:outputText value="#{msgs.cdfm_atttype}" />
						</f:facet>
						<h:outputText value="#{eachAttach.attachment.attachmentType}"/>
					</h:column>
					</h:dataTable>   
			
			<div class="instruction">	     
				<h:outputText value="#{msgs.cdfm_no_attachments}" rendered="#{empty ForumTool.attachments}"/>
			</div>

			<p class="act" style="padding:0 0 1em 0;">
				<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" 
					value="#{msgs.cdfm_button_bar_add_attachment_more_redirect}" 
					immediate="true"
					accesskey="a" 
					 rendered="#{!empty ForumTool.attachments}"
					 style="font-size:95%"/>
				<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" 
					value="#{msgs.cdfm_button_bar_add_attachment_redirect}" 
					immediate="true"
					accesskey="a" 
					 rendered="#{empty ForumTool.attachments}" 
					 style="font-size:95%"/>
			</p>
			</div>                                                                                  
			<%--general posting  topic settings --%>
			<h4><h:outputText  value="#{msgs.cdfm_topic_posting}"/></h4>

			<div class="indnt1">
				<p class="checkbox">
					<h:selectBooleanCheckbox
						title="topicLocked" value="#{ForumTool.selectedTopic.topicLocked}"
						id="topic_locked">
					</h:selectBooleanCheckbox> <h:outputLabel for="topic_locked" value="#{msgs.cdfm_lock_topic}" />
				</p>	
				<p class="checkbox">
					<h:selectBooleanCheckbox
						title="Moderated" value="#{ForumTool.selectedTopic.topicModerated}"
						id="topic_moderated">
					</h:selectBooleanCheckbox> <h:outputLabel for="topic_moderated" value="#{msgs.cdfm_moderate_topic}" />
				</p>
				<p class="checkbox">
					<h:selectBooleanCheckbox
						title="postFirst" value="#{ForumTool.selectedTopic.topicPostFirst}"
						id="topic_postFirst">
					</h:selectBooleanCheckbox> <h:outputLabel for="topic_postFirst" value="#{msgs.cdfm_postFirst}" />
				</p>	
			</div>	
			<h4><h:outputText  value="#{msgs.cdfm_forum_availability}" /></h4>
			<div class="indnt1">
			<h:panelGrid columns="1" columnClasses="longtext,checkbox" cellpadding="0" cellspacing="0">
              <h:panelGroup>
                 <h:selectOneRadio layout="pageDirection" onclick="this.blur()" onchange="setDatesEnabled(this);" disabled="#{not ForumTool.editMode}" id="availabilityRestricted"  value="#{ForumTool.selectedTopic.availabilityRestricted}">
                  <f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_forum_avail_show}"/>
                  <f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_forum_avail_date}"/>
               </h:selectOneRadio>
               </h:panelGroup>
               <h:panelGroup id="openDateSpan" styleClass="indnt2 openDateSpan  calWidget" style="display: #{ForumTool.selectedTopic.availabilityRestricted ? '' : 'none'}">
               	   <h:outputLabel value="#{msgs.openDate}: " for="openDate"/>
	               <h:inputText id="openDate" value="#{ForumTool.selectedTopic.openDate}"/>
	               <f:verbatim>
	               	<a id="openCal" href="javascript:openDateCal();">
	               </f:verbatim>
	               <h:graphicImage url="/images/calendar.png" title="#{msgs.pickDate}" alt="#{msgs.pickDate}"/>
	               <f:verbatim>
	               </a>
	               </f:verbatim>
              		<h:outputLabel value="#{msgs.closeDate}: " for="closeDate"/>
	               <h:inputText id="closeDate" value="#{ForumTool.selectedTopic.closeDate}"/>
	               <f:verbatim>
	               	<a id="closeCal" href="javascript:closeDateCal();">
	               </f:verbatim>
	               <h:graphicImage url="/images/calendar.png" title="#{msgs.pickDate}" alt="#{msgs.pickDate}"/>
	               <f:verbatim>
	               </a>
	               </f:verbatim>
              	</h:panelGroup>
           </h:panelGrid>

			</div>
		<%--
		   <h4><h:outputText  value="Confidential Responses"/></h4>
		   <h:selectBooleanCheckbox   title= "#{msgs.cdfm_topic_allow_anonymous_postings}"  value="false" />
		   <h:outputText   value="  #{msgs.cdfm_topic_allow_anonymous_postings}" /> 
		   <br/>
		   <h:selectBooleanCheckbox   title= "#{msgs.cdfm_topic_author_identity}"  value="false" />
		   <h:outputText   value="  #{msgs.cdfm_topic_author_identity}" />
     
       <h4><h:outputText  value="#{mags.cdfm_topic_post_before_reading}"/></h4>
    	   <p class="shorttext">
			<h:panelGrid columns="2">
				<h:panelGroup><h:outputLabel id="outputLabel4" for="topic_reading"  value="#{msgs.cdfm_topic_post_before_reading_desc}"/>	</h:panelGroup>
				<h:panelGroup>
					<h:selectOneRadio layout="lineDirection"  id="topic_reading" value="#{ForumTool.selectedTopic.mustRespondBeforeReading}">
    					<f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_yes}"/>
    					<f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_no}"/>
  					</h:selectOneRadio>
				</h:panelGroup>
			</h:panelGrid>
		</p>
		  --%>
      		  
      <h4><h:outputText value="#{msgs.cdfm_forum_mark_read}"/></h4>

			<table><tr><td>
				<p class="indnt1 checkbox"><h:selectBooleanCheckbox
				title="autoMarkThreadsRead"
				value="#{ForumTool.selectedTopic.topicAutoMarkThreadsRead}"
				id="autoMarkThreadsRead">
			</h:selectBooleanCheckbox> <h:outputLabel for="autoMarkThreadsRead"
				value="#{msgs.cdfm_auto_mark_threads_read}" /></p>
				</td></tr></table>
				

			<%@include file="/jsp/discussionForum/permissions/permissions_include.jsp"%>



      <%--designNote: gradebook assignment - need to finesse this - make aware that functionality exists, but flag that there are no gb assignmetns to select --%>
			<%--designNote:  How is this a "permission" item? --%>  
			<h:panelGrid columns="2" rendered="#{ForumTool.gradebookExist &&  ForumTool.permissionMode == 'topic' && !ForumTool.selectedForum.markForDeletion}" style="margin-top:.5em;clear:both"  styleClass="itemSummary">
		    <h:panelGroup  style="white-space:nowrap;">
					<h:outputLabel for="topic_assignments"  value="#{msgs.perm_choose_assignment}"  ></h:outputLabel>
		  	</h:panelGroup>		
				  <h:panelGroup  styleClass="gradeSelector   itemAction actionItem"> 
					<h:selectOneMenu value="#{ForumTool.selectedTopic.gradeAssign}" id="topic_assignments" disabled="#{not ForumTool.editMode}">
		     	    <f:selectItems value="#{ForumTool.assignments}" />
		  	    </h:selectOneMenu>
								<h:outputText value="#{msgs.perm_choose_assignment_none_t}" styleClass="instrWOGrades" style="display:none;margin-left:0"/>
							<h:outputText value=" #{msgs.perm_choose_instruction_topic} " styleClass="instrWithGrades" style="margin-left:0;"/>
							<h:outputLink value="#" style="text-decoration:none"  styleClass="instrWithGrades"><h:outputText styleClass="displayMore" value="#{msgs.perm_choose_instruction_more_link}"/></h:outputLink>
				    </h:panelGroup>
							<h:panelGroup>
				    </h:panelGroup>
							<h:panelGroup styleClass="itemAction actionItem">

							<h:outputText styleClass="displayMorePanel" style="display:none" value="#{msgs.perm_choose_instruction_topic_more}"/>
				    </h:panelGroup>
		  </h:panelGrid>

      <div class="act">
          <h:commandButton action="#{ForumTool.processActionSaveTopicSettings}" value="#{msgs.cdfm_button_bar_save_setting}" accesskey="s"
          								 rendered="#{!ForumTool.selectedTopic.markForDeletion}"> 
    	 	  	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>    
    	 	  	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>         
          </h:commandButton>
          <h:commandButton action="#{ForumTool.processActionSaveTopicAsDraft}" value="#{msgs.cdfm_button_bar_save_draft}" accesskey="v"
          								 rendered="#{!ForumTool.selectedTopic.markForDeletion}">
	        	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/> 
          </h:commandButton>
          <h:commandButton action="#{ForumTool.processActionSaveTopicAndAddTopic}" value="#{msgs.cdfm_button_bar_save_setting_add_topic}" accesskey="t"
          								 rendered="#{!ForumTool.selectedTopic.markForDeletion}">
	        	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/> 
          </h:commandButton>
          <h:commandButton action="#{ForumTool.processActionDeleteTopicConfirm}" id="delete_confirm" 
                           value="#{msgs.cdfm_button_bar_delete}" accesskey="d" rendered="#{!ForumTool.selectedTopic.markForDeletion && ForumTool.displayTopicDeleteOption}">
	        	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
          </h:commandButton>
          <h:commandButton action="#{ForumTool.processActionDeleteTopic}" id="delete" accesskey="d"
                           value="#{msgs.cdfm_button_bar_delete}" rendered="#{ForumTool.selectedTopic.markForDeletion}">
	        	<f:param value="#{ForumTool.selectedTopic.topic.id}" name="topicId"/>
          </h:commandButton>
          <h:commandButton  action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_button_bar_cancel}" accesskey="c" />
       </div>
       
	 </h:form>
			  <script type="text/javascript">
            $(document).ready(function(){
							$('.displayMore').click(function(e){
									e.preventDefault();
									$('.displayMorePanel').fadeIn('slow')
							})
							if ($('.gradeSelector').find('option').length ===1){
								$('.gradeSelector').find('select').hide();
								$('.gradeSelector').find('.instrWithGrades').hide();
								$('.gradeSelector').find('.instrWOGrades').show();
							}
							
		
				var charRemFormat = $('.charRemFormat').text();
				$(".forum_shortDescriptionClass").charCounter(255, {
					container: ".charsRemaining",
					format: charRemFormat
				 });
				
				var hideMessages = new Object();
				hideMessages ['topic_locked'] = '<h:outputText value= " for: #{msgs.cdfm_lock_topic}" />';
				hideMessages ['moderated'] = '<h:outputText value=" for: #{msgs.cdfm_moderate_topic}" />';  
				hideMessages ['postFirst'] = '<h:outputText value=" for: #{msgs.cdfm_postFirst}" />';  
				hideMessages ['autoMarkThreadsRead'] = '<h:outputText value=" for: #{msgs.cdfm_auto_mark_threads_read}" />';  
				hideMessages ['revisePostings'] = '<h:outputText value=" for: #{msgs.perm_revise_postings}" />';         
				hideMessages ['deletePostings'] = '<h:outputText value=" for: #{msgs.perm_delete_postings}" />'; 
				                              
				var input = $("input:radio").each(function (){
				  var idrand  = "id" + Math.random();
				  $(this).attr('id',idrand);
				  var name = $(this).attr('name');
				  var splitedName = name.split(":");
				  var lastParamName = splitedName[splitedName.length-1];
				                                                               
				  var inputParent = $(this).parent().parent();
				  $(inputParent).prepend(this);
				
				  var label = $(inputParent).find("label:first");
			      $(label).attr('for',idrand);
				  if (hideMessages[lastParamName]!= null){
				           $(label).append ("<span style='display:none'>"+hideMessages[lastParamName]+"</span>");
				   }                                  
			    });
				
			 });				 
        </script>
		
    </sakai:view>
</f:view>
