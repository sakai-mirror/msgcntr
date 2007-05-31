<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>

<f:view>
  <sakai:view title="#{msgs.cdfm_container_title}">
  <!--jsp/dfCompose.jsp-->
    <h:form id="dfCompose">
      <h3><h:outputText value="#{msgs.cdfm_tool_bar_message}" /></h3>
      <h4><h:outputText value="#{ForumTool.selectedForum.forum.title} - #{ForumTool.selectedTopic.topic.title}" /></h4> 
	  <div class="textPanel">
	  <h:outputText value="#{ForumTool.selectedTopic.topic.shortDescription}" />
	  </div>
	  <div class="textPanelFooter">
        <h:commandLink immediate="true" 
		                  action="#{ForumTool.processDfComposeToggle}" 
					  			     onmousedown="document.forms[0].onsubmit();"
						  		     rendered="#{ForumTool.selectedTopic.hasExtendedDesciption}" 
							   	   value="#{msgs.cdfm_read_full_description}"
							   	   title="#{msgs.cdfm_read_full_description}">
		      <f:param value="dfCompose" name="redirectToProcessAction"/>
		      <f:param value="true" name="composeExpand"/>
		    </h:commandLink>
		    <h:commandLink immediate="true" 
		                action="#{ForumTool.processDfComposeToggle}" 
								   onmousedown="document.forms[0].onsubmit();"
			  					   value="#{msgs.cdfm_hide_full_description}" 
			  					   rendered="#{ForumTool.selectedTopic.readFullDesciption}"
			  					   title=" #{msgs.cdfm_hide_full_description}">
		       <f:param value="dfCompose" name="redirectToProcessAction"/>
		     </h:commandLink>					
			</div>		 			 
			 			 
			<mf:htmlShowArea value="#{ForumTool.selectedTopic.topic.extendedDescription}" 
		                   rendered="#{ForumTool.selectedTopic.readFullDesciption}"
		                   id="topic_extended_description" 
		                   hideBorder="false" />
		                   
		  <br />
		                   
			<div class="instruction">			 
				 <h:outputText value="#{msgs.cdfm_required}"/>
				 <h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" />
			  </div>
			
          <h:panelGrid styleClass="jsfFormTable" columns="2" width="100%">
            <h:panelGroup styleClass="required">
				     <h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStar"/>
					   <h:outputLabel for="df_compose_title"><h:outputText value="#{msgs.cdfm_title}" /></h:outputLabel>
				   </h:panelGroup>
            <h:panelGroup styleClass="shorttext">
					   <h:inputText value="#{ForumTool.composeTitle}" style="width:30em;" required="true" id="df_compose_title">
					     <f:validator validatorId="MessageTitle" />
					   </h:inputText>
				   </h:panelGroup>
          </h:panelGrid>

          <h:message for="df_compose_title" warnStyle="WARN" styleClass="alertMessage"/>
		  <h4>
	            <h:outputText value="#{msgs.cdfm_message}" />
			</h4>	
	            <sakai:rich_text_area value="#{ForumTool.composeBody}" rows="17" columns="70"/>
<%--********************* Attachment *********************--%>	
	      <h4>
	        <h:outputText value="#{msgs.cdfm_att}" />
	      </h4>
		  <p class="instruction">	        
		        <h:outputText value="#{msgs.cdfm_no_attachments}" rendered="#{empty ForumTool.attachments}"/>
			</p>	
	        
		  <sakai:button_bar>
	          	<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_redirect}" immediate="true" accesskey="a" />
	          </sakai:button_bar>

	        
	        
					<h:dataTable styleClass="listHier lines nolines" id="attmsg" width="100%" value="#{ForumTool.attachments}" var="eachAttach"  columnClasses=",itemAction specialLink,," cellpadding="0" cellspacing="0">
					  <h:column rendered="#{!empty ForumTool.attachments}">
							<f:facet name="header">
								<h:outputText value="#{msgs.cdfm_title}"/>
							</f:facet>
								<h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-excel'}" alt="" />
								<h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/html'}" alt="" />
								<h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/pdf'}"/>
								<h:graphicImage url="/images/ppt.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
								<h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/plain'}" alt="" />
								<h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/msword'}" alt="" />
							
								<h:outputText value="#{eachAttach.attachment.attachmentName}"/>
							</h:column>
							<h:column>

								<h:commandLink action="#{ForumTool.processDeleteAttach}" 
									             immediate="true"
									             onfocus="document.forms[0].onsubmit();"
									             title="#{msgs.cdfm_remove}">
									<h:outputText value="#{msgs.cdfm_remove}"/>
<%--									<f:param value="#{eachAttach.attachmentId}" name="dfmsg_current_attach"/>--%>
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
        		
<%--********************* Label *********************
				<sakai:panel_titled>
          <table width="80%" align="left">
            <tr>
              <td align="left" width="20%">
                <h:outputText value="Label"/>
              </td>
              <td align="left">
 							  <h:selectOneListbox size="1" id="viewlist">
            		  <f:selectItem itemLabel="Normal" itemValue="none"/>
          			</h:selectOneListbox>  
              </td>                           
            </tr>                                
          </table>
        </sakai:panel_titled>
--%>		        
      <sakai:button_bar>
        <sakai:button_bar_item action="#{ForumTool.processDfMsgPost}" value="#{msgs.cdfm_button_bar_post_message}" accesskey="s" styleClass="active"/>
      <%--  <sakai:button_bar_item action="#{ForumTool.processDfMsgSaveDraft}" value="#{msgs.cdfm_button_bar_save_draft}" /> --%>
        <sakai:button_bar_item action="#{ForumTool.processDfMsgCancel}" value="#{msgs.cdfm_button_bar_cancel}" immediate="true" accesskey="x" />
      </sakai:button_bar>
    </h:form>
     

  </sakai:view>
</f:view> 

