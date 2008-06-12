<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
	<jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>

<f:view>
	<sakai:view title="#{msgs.cdfm_reply_tool_bar_message}">
	<!--jsp/dfMessageReply.jsp-->    
		<h:form id="dfCompose">
			<style type="text/css">
				@import url("/sakai-messageforums-tool/css/msgcntr.css");
			</style>
			
			<script type="text/javascript" src="/library/js/jquery.js"></script>
			<sakai:script contextBase="/sakai-messageforums-tool" path="/js/sak-10625.js"/>
			<h:outputText styleClass="messageAlert" value="#{msgs.cdfm_reply_deleted}" rendered="#{ForumTool.errorSynch}" />
			<h3><h:outputText value="#{msgs.cdfm_reply_tool_bar_message}"  /></h3>
			<table class="topicBloc topicBlocLone" border="0">
				<tr>
					<td>
						<h4 style="margin:0 0 1em 0">
							<h:outputText value="#{ForumTool.selectedForum.forum.title}" />
							<h:outputText value="/ "/>
							<h:outputText value="#{ForumTool.selectedTopic.topic.title}"/>
						</h4>	
						<h:outputText value="#{ForumTool.selectedTopic.topic.shortDescription}"/>
					</td>
				</tr>	
			</table>
			<div class="singleMessage">
				<%-- //designNote: need to make this toggle look and behave exactly like the  rest --%> 
				<p><a href="javascript:$('#replytomessage').toggle();resizeFrame('grow')" class="show"><h:outputText value="#{msgs.cdfm_replytoshowhide}"/></a></p>
				<div  id="replytomessage">   
					<h:outputText value="#{ForumTool.selectedMessage.message.title}" styleClass="title"/>
					<h:outputText value="#{ForumTool.selectedMessage.message.author}" styleClass="textPanelFooter"  style="padding-left:.5em"/>
					<h:outputText value=" #{msgs.cdfm_openb}" styleClass="textPanelFooter"/>
					<h:outputText value="#{ForumTool.selectedMessage.message.created}" styleClass="textPanelFooter">
						<f:convertDateTime pattern="#{msgs.date_format}" />  
					</h:outputText>
					<h:outputText value=" #{msgs.cdfm_closeb}" styleClass="textPanelFooter"/>
					<mf:htmlShowArea value="#{ForumTool.selectedMessage.message.body}" hideBorder="true" />
					<h:dataTable value="#{ForumTool.selectedMessage.message.attachments}" var="eachAttach"  rendered="#{!empty ForumTool.selectedMessage.message.attachments}" columnClasses="attach,bogus" styleClass="attachList">
						<h:column rendered="#{!empty ForumTool.selectedMessage.message.attachments}">
							<h:graphicImage url="/images/attachment.gif"/>
						</h:column>
						<h:column>
							<%--							<h:outputLink value="#{eachAttach.attachmentUrl}" target="_blank">
							<h:outputText value="#{eachAttach.attachmentName}"/>
							</h:outputLink>--%>
							<%--							<h:outputLink value="#{ForumTool.attachmentUrl}" target="_blank">
							<f:param name="attachmentId" value="#{eachAttach.attachment.attachmentId}"/>
							<h:outputText value="#{eachAttach.attachment.attachmentName}"/>
							</h:outputLink>--%>
							<h:outputText value="#{eachAttach.attachmentName}"/>							
						</h:column>
					</h:dataTable>
				</div>
			</div>
		<p class="instruction">
			<h:outputText value="#{msgs.cdfm_required}"/>
			<h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" />
		</p>	 

			<h:panelGrid styleClass="jsfFormTable" columns="1" style="width: 100%;margin:0;">
				
			<h:panelGroup style="padding-top:.5em">
				<h:message for="df_compose_title" styleClass="messageAlert" id="errorMessages"/>	
				<h:outputLabel for="df_compose_title" styleClass="block" style="display:block;float:none;clear:both;padding-bottom:.3em;padding-top:.5em"><h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStar"/><h:outputText value="#{msgs.cdfm_reply_title}" /></h:outputLabel>
					<h:inputText value="#{ForumTool.composeTitle}" style="width: 30em;" required="true" id="df_compose_title" />
				</h:panelGroup>
			</h:panelGrid>
			<h:outputText value="#{msgs.cdfm_message}" /> 

			<h:inputHidden id="msgHidden" value="#{ForumTool.selectedMessage.message.body}" />
			<h:inputHidden id="titleHidden" value="#{ForumTool.selectedMessage.message.title}" />
			<h:outputText value="&nbsp;&nbsp;&nbsp; " escape="false" />
			<a  href="#"  onclick="InsertHTML();">
			<img src="/library/image/silk/paste_plain.png" />
			<h:outputText value="#{msgs.cdfm_message_insert}" /></a>
			<sakai:rich_text_area value="#{ForumTool.composeBody}" rows="17" columns="70"/>
			<script language="javascript" type="text/javascript">
				var textareas = document.getElementsByTagName("textarea");
				var rteId = textareas.item(0).id;
	
				//	        function FCKeditor_OnComplete( editorInstance )
				//	        {
				//	          // clears the FCK editor after initial loading
				//	          editorInstance.SetHTML( "" );
				//	        }
							
				// set the previous message variable
				var messagetext = document.forms['dfCompose'].elements['dfCompose:msgHidden'].value;
				var titletext = document.forms['dfCompose'].elements['dfCompose:titleHidden'].value;
			
				function InsertHTML() 
				{ 
				  // These lines will write to the original textarea and makes the quoting work when FCK is not present
				  var finalhtml = '<b><i><h:outputText value="#{msgs.cdfm_insert_original_text_comment}" /></i></b><br/><b><i><h:outputText value="#{msgs.cdfm_from}" /></i></b> <i><h:outputText value="#{ForumTool.selectedMessage.message.author}" /><h:outputText value=" #{msgs.cdfm_openb}" /><h:outputText value="#{ForumTool.selectedMessage.message.created}" ><f:convertDateTime pattern="#{msgs.date_format}" /></h:outputText><h:outputText value="#{msgs.cdfm_closeb}" /></i><br/><b><i><h:outputText value="#{msgs.cdfm_subject}" /></i></b> <i>' + titletext + '</i><br/><br/><i>' + messagetext + '</i><br/><br/>';
				  document.forms['dfCompose'].elements[rteId].value = finalhtml;
				  // Get the editor instance that we want to interact with.
				  var oEditor = FCKeditorAPI.GetInstance(rteId);
				  // Check the active editing mode.
				  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )
				  {
				  // Insert the desired HTML.
				  oEditor.InsertHtml( finalhtml );
				  }
				  else alert( 'You must be on WYSIWYG mode!' );
				}
            </script>
            
			<%--********************* Attachment *********************--%>	
			<h4>
				<h:outputText value="#{msgs.cdfm_att}"/>
			</h4>
			<p>
				<h:outputText value="#{msgs.cdfm_no_attachments}" rendered="#{empty ForumTool.attachments}" styleClass="instruction" />
			</p>	
	    <%--//designNote: moving rendered attr from column to table to avoid childless table if empty--%>
			<h:dataTable styleClass="attachPanel" id="attmsg"  value="#{ForumTool.attachments}" var="eachAttach"   rendered="#{!empty ForumTool.attachments}"
				columnClasses="attach,bogus,specialLink itemAction,bogus,bogus">
				<h:column rendered="#{!empty ForumTool.attachments}">
					<h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-excel'}" alt="" />
					<h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/html'}" alt="" />
					<h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/pdf'}"/>
					<h:graphicImage url="/images/ppt.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
					<h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/plain'}" alt="" />
					<h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/msword'}" alt="" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="#{msgs.cdfm_title}"/>
					</f:facet>
					<h:outputText value="#{eachAttach.attachment.attachmentName}"/>			
				</h:column>
				<h:column>
					<h:commandLink action="#{ForumTool.processDeleteAttach}" 
							immediate="true"
							onfocus="document.forms[0].onsubmit();"
							title="#{msgs.cdfm_remove}">
						<h:outputText value="#{msgs.cdfm_remove}"/>
						<%--<f:param value="#{eachAttach.attachment.attachmentId}" name="dfmsg_current_attach"/>--%>
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

			<p style="padding:0" class="act">
				<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_redirect}" immediate="true"
					rendered="#{empty ForumTool.attachments}" style="font-size:95%"/>
				<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_more_redirect}" immediate="true"
					rendered="#{!empty ForumTool.attachments}" style="font-size:95%"/>
			</p>

			<p style="padding:0" class="act">
				<sakai:button_bar_item action="#{ForumTool.processDfReplyMsgPost}" value="#{msgs.cdfm_button_bar_post_message}" accesskey="s" styleClass="active" />
				<%-- <sakai:button_bar_item action="#{ForumTool.processDfReplyMsgSaveDraft}" value="#{msgs.cdfm_button_bar_save_draft}" /> --%>
				<sakai:button_bar_item action="#{ForumTool.processDfReplyMsgCancel}" value="#{msgs.cdfm_button_bar_cancel}" accesskey="x" />
			</p>
			<%--
				<sakai:button_bar>
				</sakai:button_bar>
			--%>
			<script type="text/javascript">
				setTimeout(function(){ 
				  var _div = document.getElementsByTagName('div');
				  for(i=0;i<_div.length; i++)
				  {
					if(_div[i].className == 'htmlarea')
					{
					  var children = _div[i].childNodes;
						for (j=0; j<children.length; j++)
						{
						  if(children.item(j).tagName == 'IFRAME')
						  {
							children.item(j).contentWindow.focus();
						  }
					  }
					}
				  }
				}, 800);
			</script>
      
		</h:form>
	</sakai:view>
</f:view> 

