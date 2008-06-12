<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>
<f:view>
  <sakai:view title="#{msgs.pvt_reply}">
    <h:form id="pvtMsgReply">
		<script type="text/javascript" src="/library/js/jquery.js"></script>
                <sakai:script contextBase="/sakai-messageforums-tool" path="/js/sak-10625.js"/>

		<h:panelGroup>
          	<f:verbatim><div class="breadCrumb"><h3></f:verbatim>
				  <h:panelGroup rendered="#{PrivateMessagesTool.messagesandForums}" >
				  	<h:commandLink action="#{PrivateMessagesTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title="#{msgs.cdfm_message_forums}"/>
				  	<f:verbatim><h:outputText value=" / " /></f:verbatim>
				  </h:panelGroup>
	  		      <h:commandLink action="#{PrivateMessagesTool.processActionPrivateMessages}" value="#{msgs.pvt_message_nav}" title=" #{msgs.cdfm_message_forums}"/>
	              <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
				<h:commandLink action="#{PrivateMessagesTool.processDisplayForum}" value="#{PrivateMessagesTool.msgNavMode}" title=" #{PrivateMessagesTool.msgNavMode}"/><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " />
				<h:commandLink action="#{PrivateMessagesTool.processDisplayMessages}" value="#{PrivateMessagesTool.detailMsg.msg.title}" title=" #{PrivateMessagesTool.detailMsg.msg.title}"/><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " />
				<h:outputText value="#{msgs.pvt_reply}" />
			<f:verbatim></h3></div></f:verbatim>
	</h:panelGroup>	
		
		
	<%--//designNote: gsilver:commenting this header out as redundant--%>		
      <%-- <sakai:tool_bar_message value="#{msgs.pvt_reply}" />--%> 

			<div class="instruction">
 			  <h:outputText value="#{msgs.cdfm_required}"/> <h:outputText value="#{msgs.pvt_star}" styleClass="reqStarInline"/>
		  </div>
		  
		  <h:outputLink rendered="#{PrivateMessagesTool.renderPrivacyAlert}" value="#{PrivateMessagesTool.privacyAlertUrl}" target="_blank" >
		  	 <sakai:instruction_message value="#{PrivateMessagesTool.privacyAlert}"/>
		  </h:outputLink>
		  
		  <h:messages styleClass="alertMessage" id="errorMessages" /> 
		  
		  <h:panelGrid styleClass="jsfFormTable" columns="2" summary="layout">
			  <h:panelGroup styleClass="shorttext">
					<h:outputLabel for="send_to" ><h:outputText value="#{msgs.pvt_to}"/></h:outputLabel>
				</h:panelGroup>
				<h:panelGroup styleClass="shorttext">
					<h:outputText id="send_to" value="#{PrivateMessagesTool.detailMsg.msg.author}" />
				</h:panelGroup>
				
				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="list1" ><h:outputText value="#{msgs.pvt_select_addtl_recipients}"/></h:outputLabel>
				</h:panelGroup>
				<h:panelGroup styleClass="shorttext">
					<h:selectManyListbox id="list1" value="#{PrivateMessagesTool.selectedComposeToList}" size="5" style="width: 200px;">
          		<f:selectItems value="#{PrivateMessagesTool.totalComposeToList}"/>
         	</h:selectManyListbox>   
				</h:panelGroup>
				
			 <h:panelGroup styleClass="shorttext" rendered= "#{PrivateMessagesTool.dispSendEmailOut}">
					<h:outputLabel><h:outputText value="#{msgs.pvt_send_cc}"/></h:outputLabel>
			  </h:panelGroup>
			   
			  
			  <h:panelGroup styleClass="checkbox" style="white-space: nowrap;" rendered= "#{PrivateMessagesTool.dispSendEmailOut}">
			  <h:selectBooleanCheckbox value="#{PrivateMessagesTool.booleanEmailOut}" id="send_email_out" >
			  </h:selectBooleanCheckbox>
		   <h:outputLabel for="send_email_out"><h:outputText value="#{msgs.pvt_send_as_email}"/></h:outputLabel>
			</h:panelGroup>			 
				
				<h:panelGroup  styleClass="shorttext">
					<h:outputLabel for="viewlist" ><h:outputText value="#{msgs.pvt_label}"/></h:outputLabel>
			  </h:panelGroup>
			  <h:panelGroup>
					<h:selectOneListbox size="1" id="viewlist" value="#{PrivateMessagesTool.selectedLabel}">
            	  <f:selectItem itemValue="Normal" itemLabel="#{msgs.pvt_priority_normal}"/>
            	  <f:selectItem itemValue="Low" itemLabel="#{msgs.pvt_priority_low}"/>
            	  <f:selectItem itemValue="High" itemLabel="#{msgs.pvt_priority_high}"/>
          	  </h:selectOneListbox> 
				</h:panelGroup>
				
				<h:panelGroup styleClass="shorttext required">
  					<h:outputText value="#{msgs.pvt_star}" styleClass="reqStar"/>
  					<h:outputLabel for="subject" ><h:outputText value="#{msgs.pvt_subject}"  /></h:outputLabel>
  			</h:panelGroup>
  			<h:panelGroup styleClass="shorttext">
					<h:inputText value="#{PrivateMessagesTool.replyToSubject}" id="subject" size="45" />
				</h:panelGroup>
				
				
				
			</h:panelGrid>
			<h4><h:outputText value="#{msgs.pvt_message}" /></h4>
	        <sakai:panel_edit>
	          <sakai:doc_section>
			  <sakai:rich_text_area rows="17" columns="70"  value="#{PrivateMessagesTool.replyToBody}" />	 
	         </sakai:doc_section>    
	        </sakai:panel_edit>

<%--********************* Attachment *********************--%>	

	         <h4> <h:outputText value="#{msgs.pvt_att}"/></h4>

	          <%-- Existing Attachments 
              <h:dataTable value="#{PrivateMessagesTool.detailMsg.msg.attachments}" var="existAttach" >
					  		<h:column rendered="#{!empty PrivateMessagesTool.detailMsg.message.attachments}">
								<h:graphicImage url="/images/excel.gif" rendered="#{existAttach.attachmentType == 'application/vnd.ms-excel'}"/>
								<h:graphicImage url="/images/html.gif" rendered="#{existAttach.attachmentType == 'text/html'}"/>
								<h:graphicImage url="/images/pdf.gif" rendered="#{existAttach.attachmentType == 'application/pdf'}"/>
								<h:graphicImage url="/sakai-messageforums-tool/images/ppt.gif" rendered="#{existAttach.attachmentType == 'application/vnd.ms-powerpoint'}"/>
								<h:graphicImage url="/images/text.gif" rendered="#{existAttach.attachmentType == 'text/plain'}"/>
								<h:graphicImage url="/images/word.gif" rendered="#{existAttach.attachmentType == 'application/msword'}"/>
							
								<h:outputText value="#{existAttach.attachmentName}"/>
						
								</h:column>
							</h:dataTable>  
					--%>	
	        	<p class="instruction"><h:outputText value="#{msgs.pvt_noatt}" rendered="#{empty PrivateMessagesTool.allAttachments}"/></p>	        
	          <sakai:button_bar>
	          	<sakai:button_bar_item action="#{PrivateMessagesTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_redirect}" accesskey="a" />
	          </sakai:button_bar>
	        	        
					<h:dataTable styleClass="listHier lines nolines" id="attmsgrep" width="100%" cellpadding="0" cellspacing="0" columnClasses="bogus,itemAction specialLink,bogus,bogus"
					             rendered="#{!empty PrivateMessagesTool.allAttachments}" value="#{PrivateMessagesTool.allAttachments}" var="eachAttach" >
					  <h:column >
							<f:facet name="header">
								<h:outputText value="#{msgs.pvt_title}"/>
							</f:facet>
							<sakai:doc_section>
								<h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-excel'}" alt="" />
								<h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/html'}"  alt="" />
								<h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/pdf'}" alt="" />
								<h:graphicImage url="/images/ppt.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
								<h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachment.attachmentType == 'text/plain'}" alt="" />
								<h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachment.attachmentType == 'application/msword'}" alt="" />
<%--													  <h:outputLink value="#{eachAttach.attachmentUrl}" target="_blank">
									<h:outputText value="#{eachAttach.attachmentName}"/>
								</h:outputLink>--%>
							  <h:outputLink value="#{eachAttach.url}" target="_blank">
									<h:outputText value="#{eachAttach.attachment.attachmentName}"/>
								</h:outputLink>

							</sakai:doc_section>
						</h:column>
						<h:column >
							<sakai:doc_section>
								<h:commandLink action="#{PrivateMessagesTool.processDeleteReplyAttach}" 
									immediate="true"
									onfocus="document.forms[0].onsubmit();">
									<h:outputText value="#{msgs.pvt_attrem}"/>
									<f:param value="#{eachAttach.attachment.attachmentId}" name="remsg_current_attach"/>
								</h:commandLink>
							</sakai:doc_section>
							
						</h:column>
					  <h:column>
							<f:facet name="header">
								<h:outputText value="#{msgs.pvt_attsize}" />
							</f:facet>
							<h:outputText value="#{eachAttach.attachment.attachmentSize}"/>
						</h:column>
					  <h:column >
							<f:facet name="header">
		  			    <h:outputText value="#{msgs.pvt_atttype}" />
							</f:facet>
							<h:outputText value="#{eachAttach.attachment.attachmentType}"/>
						</h:column>
						</h:dataTable>   
					 
 
      <sakai:button_bar>
        <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgReplySend}" value="#{msgs.pvt_send}" accesskey="s" styleClass="active" />
        <%--<sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgReplySaveDraft}" value="Save Draft" />--%>
        <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgCancelToDetailView}" value="#{msgs.pvt_cancel}" accesskey="x" />
      </sakai:button_bar>
    </h:form>

  </sakai:view>
</f:view> 

