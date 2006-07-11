<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<f:loadBundle basename="org.sakaiproject.tool.messageforums.bundle.Messages" var="msgs"/>
<link href='/sakai-messageforums-tool/css/msgForums.css' rel='stylesheet' type='text/css' />

<f:view>
  <sakai:view title="#{msgs.pvt_reply}">

    <h:form id="pvtMsgReply">
  			<div class="breadCrumb">
  			  <h:commandLink action="#{PrivateMessagesTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title=" #{msgs.cdfm_message_forums}"/> /
  			  <h:commandLink action="#{PrivateMessagesTool.processActionPrivateMessages}" value="#{msgs.cdfm_message_pvtarea}" title=" #{msgs.cdfm_message_pvtarea}"/> /
				<h:outputText value="#{msgs.pvt_reply}" />
			</div>
			
      <sakai:tool_bar_message value="#{msgs.pvt_reply}" /> 
 			
			<div class="instruction">
 			  <h:outputText value="#{msgs.cdfm_required}"/> <h:outputText value="#{msgs.pvt_star}" styleClass="reqStarInline"/>
		  </div>
		  
		  <h:outputLink rendered="#{PrivateMessagesTool.renderPrivacyAlert}" value="#{PrivateMessagesTool.privacyAlertUrl}" target="_blank" >
		  	 <sakai:instruction_message value="#{PrivateMessagesTool.privacyAlert}"/>
		  </h:outputLink>
		  
		  <h:messages styleClass="alertMessage" id="errorMessages" /> 
		  
		  <sakai:panel_titled title="">
		  <h:panelGrid styleClass="jsfFormTable" columns="2" summary="">
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
				
				<h:panelGroup styleClass="shorttext">
  					<h:outputLabel for="sent_as" ><h:outputText value="#{msgs.pvt_send}" /></h:outputLabel>
  				</h:panelGroup>
  				<h:panelGroup styleClass="checkbox inlineForm">
					<h:selectOneRadio id="sent_as" value="#{PrivateMessagesTool.composeSendAsPvtMsg}" layout="pageDirection">
		    			  <f:selectItem itemValue="yes" itemLabel="#{msgs.pvt_send_as_private}"/>
		    			  <f:selectItem itemValue="no" itemLabel="#{msgs.pvt_send_as_email}"/>
    			    </h:selectOneRadio>
				</h:panelGroup>
				
				<h:panelGroup styleClass="shorttext required">
  					<h:outputText value="#{msgs.pvt_star}" styleClass="reqStar"/>
  					<h:outputLabel for="subject" ><h:outputText value="#{msgs.pvt_subject}"  /></h:outputLabel>
  			  </h:panelGroup>
  			  <h:panelGroup styleClass="shorttext">
					<h:inputText value="#{PrivateMessagesTool.replyToSubject}" id="subject" size="70" />
				</h:panelGroup>
				
			</h:panelGrid>

	        <sakai:panel_edit>
	          <sakai:doc_section>
	            <h:outputLabel for="" ><h:outputText value="#{msgs.pvt_message}" /></h:outputLabel>  
	            <sakai:rich_text_area rows="17" columns="70"  value="#{PrivateMessagesTool.replyToBody}" />	 
	         </sakai:doc_section>    
	        </sakai:panel_edit>
	    </sakai:panel_titled>

<%--********************* Attachment *********************--%>	
	      <sakai:panel_titled title="">
					<div class="msgHeadings">
	          <h:outputText value="#{msgs.pvt_att}"/>
	        </div>
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
	        <sakai:doc_section>	        
	        	<h:outputText value="#{msgs.pvt_noatt}" rendered="#{empty PrivateMessagesTool.allAttachments}"/>
	        </sakai:doc_section>
	        
	        <sakai:doc_section>
	          <sakai:button_bar>
	          	<sakai:button_bar_item action="#{PrivateMessagesTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_redirect}" accesskey="a" />
	          </sakai:button_bar>
	        </sakai:doc_section>
	        	        
					<h:dataTable styleClass="listHier" id="attmsgrep" width="100%" 
					             rendered="#{!empty PrivateMessagesTool.allAttachments}" value="#{PrivateMessagesTool.allAttachments}" var="eachAttach" >
					  <h:column >
							<f:facet name="header">
								<h:outputText value="#{msgs.pvt_title}"/>
							</f:facet>
							<sakai:doc_section>
								<h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-excel'}" alt="" />
								<h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachmentType == 'text/html'}"  alt="" />
								<h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachmentType == 'application/pdf'}" alt="" />
								<h:graphicImage url="/images/ppt.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
								<h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachmentType == 'text/plain'}" alt="" />
								<h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachmentType == 'application/msword'}" alt="" />
							 
							  <h:outputLink value="#{eachAttach.attachmentUrl}" target="_new_window">
									<h:outputText value="#{eachAttach.attachmentName}"/>
								</h:outputLink>

							</sakai:doc_section>
	
							<sakai:doc_section>
								<h:commandLink action="#{PrivateMessagesTool.processDeleteReplyAttach}" 
									immediate="true"
									onfocus="document.forms[0].onsubmit();">
									<h:outputText value="#{msgs.pvt_attrem}"/>
									<f:param value="#{eachAttach.attachmentId}" name="remsg_current_attach"/>
								</h:commandLink>
							</sakai:doc_section>
							
						</h:column>
					  <h:column>
							<f:facet name="header">
								<h:outputText value="#{msgs.pvt_attsize}" />
							</f:facet>
							<h:outputText value="#{eachAttach.attachmentSize}"/>
						</h:column>
					  <h:column >
							<f:facet name="header">
		  			    <h:outputText value="#{msgs.pvt_atttype}" />
							</f:facet>
							<h:outputText value="#{eachAttach.attachmentType}"/>
						</h:column>
						</h:dataTable>   
					</sakai:panel_titled>  
					 
 <%--********************* Reply *********************--%>	
	    <sakai:panel_titled title="">
	      	<div class="msgHeadings">
	          <h:outputText value="#{msgs.pvt_replyto}"/>
	      </div> 
   
        <table style="width: 100%">
		      <tr>
				    <td class="msgDetailsCol"> 
          			<h:outputText value="#{msgs.pvt_from} "/>		
             </td>
             <td>   
          			<h:outputText value="#{PrivateMessagesTool.userId}" />  
              	<h:outputText value=" #{msgs.pvt_openb}" />  
              	<h:outputText value="#{PrivateMessagesTool.time}">
  	            	  <f:convertDateTime pattern="#{msgs.pvt_time_format}"/>
  	          	  </h:outputText>
              	<h:outputText value="#{msgs.pvt_closeb}" />   
             </td>                           
           </tr>
           <tr>
             <td class="msgDetailsCol"> 
          			<h:outputText value="#{msgs.pvt_subject}"/>		
             </td>
             <td>   
          			<h:outputText value="#{PrivateMessagesTool.detailMsg.msg.title}" />  
             </td>                           
           </tr>
            <%--
            <tr>
              <td align="left">
                <h:outputText value="#{msgs.pvt_label}" />
              </td>
              <td align="left">
              	<h:outputText value="#{PrivateMessagesTool.detailMsg.msg.label}" />  
              </td>
            </tr>
            --%>
           <tr>
             <td class="msgDetailsCol">
              	<h:outputText value="#{msgs.pvt_message}" />
             </td>
             <td>
              	<mf:htmlShowArea value="#{PrivateMessagesTool.detailMsg.msg.body}"/>					
             </td>
           </tr>                                   
         </table>
	     </sakai:panel_titled>
	             		
       <hr/>
       
       <h:panelGrid styleClass="jsfFormTable" columns="3" summary="">
			  <h:panelGroup>
					<h:outputLabel for="viewlist" ><h:outputText value="#{msgs.pvt_label}"/></h:outputLabel>
			  </h:panelGroup>
			  <h:outputText value=" " />
			  <h:panelGroup>
					<h:selectOneListbox size="1" id="viewlist" value="#{PrivateMessagesTool.selectedLabel}">
            	  <f:selectItem itemValue="Normal" itemLabel="#{msgs.pvt_priority_normal}"/>
            	  <f:selectItem itemValue="Low" itemLabel="#{msgs.pvt_priority_low}"/>
            	  <f:selectItem itemValue="High Priority" itemLabel="#{msgs.pvt_priority_high}"/>
          	  </h:selectOneListbox> 
				</h:panelGroup>
			</h:panelGrid>
		   
      <sakai:button_bar>
        <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgReplySend}" value="#{msgs.pvt_send}" accesskey="s" />
        <%--<sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgReplySaveDraft}" value="Save Draft" />--%>
        <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgComposeCancel}" value="#{msgs.pvt_cancel}" accesskey="c" />
      </sakai:button_bar>
    </h:form>

  </sakai:view>
</f:view> 

