<%@ page import="java.util.*, javax.faces.context.*, javax.faces.application.*,
                 javax.faces.el.*, org.sakaiproject.tool.messageforums.*,
                 org.sakaiproject.tool.messageforums.ui.*"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>

<%
// FOR WHEN COMING FROM SYNOPTIC TOOL 
    FacesContext context = FacesContext.getCurrentInstance();
    ExternalContext exContext = context.getExternalContext();
    Map paramMap = exContext.getRequestParameterMap();
    
     if ("Received".equals((String) paramMap.get("selectedTopic"))) {
	  Application app = context.getApplication();
	  ValueBinding binding = app.createValueBinding("#{PrivateMessagesTool}");
	  PrivateMessagesTool pmt = (PrivateMessagesTool) binding.getValue(context);
	  pmt.initializePrivateMessageArea();
	  pmt.processPvtMsgTopic();
	  PrivateTopicDecoratedBean selectedTopic = pmt.getSelectedTopic();
	  selectedTopic.setHasNextTopic(true);
	  selectedTopic.setHasPreviousTopic(false);
    }
%>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>

<f:view>
	<sakai:view title="#{msgs.pvtarea_name}">
<!--jsp/privateMsg/pvtMsg.jsp-->
		<h:form id="prefs_pvt_form">
			<sakai:script contextBase="/sakai-messageforums-tool" path="/js/forum.js"/>		
			
			<sakai:tool_bar>
       			<sakai:tool_bar_item value="#{msgs.pvt_compose}" action="#{PrivateMessagesTool.processPvtMsgCompose}" />
 			</sakai:tool_bar>

			<%--<sakai:tool_bar_message value="#{msgs.pvt_pvtmsg}- #{PrivateMessagesTool.msgNavMode}" /> --%>
			<h:panelGrid columns="2" summary="layout" width="100%" styleClass="navPanel">
        <h:panelGroup>
          	<f:verbatim><div class="breadCrumb specialLink"><h3></f:verbatim>
				  <h:panelGroup rendered="#{PrivateMessagesTool.messagesandForums}" >
				  	<h:commandLink action="#{PrivateMessagesTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title="#{msgs.cdfm_message_forums}"/>
				  	<f:verbatim><h:outputText value=" / " /></f:verbatim>
				  </h:panelGroup>
	  		      <h:commandLink action="#{PrivateMessagesTool.processActionPrivateMessages}" value="#{msgs.pvt_message_nav}" title=" #{msgs.cdfm_message_forums}"/>
	              <f:verbatim><h:outputText value=" " /><h:outputText value=" / " /><h:outputText value=" " /></f:verbatim>
	 		      <h:outputText value="#{PrivateMessagesTool.msgNavMode}"/>
			    <f:verbatim></h3></div></f:verbatim>
        </h:panelGroup>
        <h:panelGroup styleClass="itemNav specialLink">
		<%-- gsilver:huh? renders anyway - because it is looking at topics instead of at folders?--%>
				 <h:commandLink action="#{PrivateMessagesTool.processDisplayPreviousTopic}" value="#{msgs.pvt_prev_folder}"  
				                rendered="#{PrivateMessagesTool.selectedTopic.hasPreviousTopic}" title=" #{msgs.pvt_prev_folder}">
	  			   <f:param value="#{PrivateMessagesTool.selectedTopic.previousTopicTitle}" name="previousTopicTitle"/>
	  		   </h:commandLink>
				<h:outputText 	value="#{msgs.pvt_prev_folder}"  rendered="#{!PrivateMessagesTool.selectedTopic.hasPreviousTopic}" />
				  <f:verbatim><h:outputText value=" | " /></f:verbatim>
	  		   <h:commandLink action="#{PrivateMessagesTool.processDisplayNextTopic}" value="#{msgs.pvt_next_folder}" 
	  		                  rendered="#{PrivateMessagesTool.selectedTopic.hasNextTopic}" title=" #{msgs.pvt_next_folder}">
	  			  <f:param value="#{PrivateMessagesTool.selectedTopic.nextTopicTitle}" name="nextTopicTitle"/>
	  		   </h:commandLink>
			   				<h:outputText 	 value="#{msgs.pvt_next_folder}"  rendered="#{!PrivateMessagesTool.selectedTopic.hasNextTopic}" />
        </h:panelGroup>
      </h:panelGrid>
 
 			<h:messages styleClass="alertMessage" id="errorMessages" /> 
 			<!-- Display successfully moving checked messsages to Deleted folder -->
  			<h:outputText value="#{PrivateMessagesTool.multiDeleteSuccessMsg}" styleClass="success" rendered="#{PrivateMessagesTool.multiDeleteSuccess}" />
  			
  		<%@include file="msgHeader.jsp"%>
		<%-- gsilver:this table needs a render atrtibute that will make it not display if there are no messages - and a companion text block classed as "instruction" that will render instead--%>	
	  <h:dataTable styleClass="listHier lines nolines"cellpadding="0" cellspacing="0"  id="pvtmsgs" width="100%" value="#{PrivateMessagesTool.decoratedPvtMsgs}" var="rcvdItems" 
	  	             rendered="#{PrivateMessagesTool.selectView != 'threaded'}"
	  	             summary="#{msgs.pvtMsgListSummary}"
					 columnClasses="attach,attach,specialLink,bogus,bogus,bogus">
	  	                
		  <h:column>
		    <f:facet name="header">
 					<h:commandLink action="#{PrivateMessagesTool.processCheckAll}" value="#{msgs.cdfm_checkall}" 
 					               title="#{msgs.cdfm_checkall}" />
		     <%--<h:commandButton alt="SelectAll" image="/sakai-messageforums-tool/images/checkbox.gif" action="#{PrivateMessagesTool.processSelectAllJobs}"/>--%>
		    </f:facet>
				<h:selectBooleanCheckbox value="#{rcvdItems.isSelected}" onclick="updateCount(this.checked); toggleBulkOperations(anyChecked(), 'prefs_pvt_form');" />
		  </h:column>
		  <h:column>
		    <f:facet name="header">					
			  <h:commandLink>
		        <h:graphicImage value="/images/attachment.gif"
		                        title="#{msgs.sort_attachment}" 
		                        alt="#{msgs.sort_attachment}" />
		        <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                        title="#{msgs.sort_attachment_asc}" alt="#{msgs.sort_attachment_asc}"
    	                        rendered="#{PrivateMessagesTool.sortType == 'attachment_asc'}"/>
    	        <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                        title="#{msgs.sort_attachment_desc}" alt=" #{msgs.sort_attachment_desc}"
    	                        rendered="#{PrivateMessagesTool.sortType == 'attachment_desc'}"/>    	                       
    	        <f:param name="sortColumn" value="attachment"/>
    	      </h:commandLink>
			</f:facet>
			<h:graphicImage value="/images/attachment.gif" rendered="#{rcvdItems.msg.hasAttachments}" alt="#{msgs.msg_has_attach}" />			 
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:commandLink value="#{msgs.pvt_subject}"
		                      title="#{msgs.sort_subject}">
		         <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                         title="#{msgs.sort_subject_asc}" alt="#{msgs.sort_subject_asc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'subject_asc'}"/>
    	         <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                         title="#{msgs.sort_subject_desc}" alt="#{msgs.sort_subject_desc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'subject_desc'}"/>
    	         <f:param name="sortColumn" value="subject"/>
    	       </h:commandLink>
		    </f:facet>
			  <f:verbatim><h4></f:verbatim>
			<h:commandLink action="#{PrivateMessagesTool.processPvtMsgDetail}" title="#{rcvdItems.msg.title}" immediate="true">

            <h:outputText value=" #{rcvdItems.msg.title}" rendered="#{rcvdItems.hasRead}"/>
            <h:outputText styleClass="unreadMsg" value=" #{rcvdItems.msg.title}" rendered="#{!rcvdItems.hasRead}"/>
			<h:outputText styleClass="skip" value="#{msgs.pvt_openb}#{msgs.pvt_unread}#{msgs.pvt_closeb}" rendered="#{!rcvdItems.hasRead}"/>
            <f:param value="#{rcvdItems.msg.id}" name="current_msg_detail"/>
          </h:commandLink>
		  			<f:verbatim></h4></f:verbatim>
		  </h:column>			
		  <h:column rendered="#{PrivateMessagesTool.msgNavMode != 'Sent'}">
		    <f:facet name="header">
		       <h:commandLink value="#{msgs.pvt_authby}"
		                      title="#{msgs.sort_author}">
		         <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                         title="#{msgs.sort_author_asc}" alt="#{msgs.sort_author_asc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'author_asc'}"/>
    	         <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                         title="#{msgs.sort_author_desc}" alt="#{msgs.sort_author_desc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'author_desc'}"/>
    	         <f:param name="sortColumn" value="author"/>
    	       </h:commandLink>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.msg.author}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.msg.author}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.msgNavMode == 'Sent'}">
		    <f:facet name="header">
   		     <h:commandLink value="#{msgs.pvt_to}"
		                      title="#{msgs.sort_to}">
		         <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                       title="#{msgs.sort_author_asc}" alt="#{msgs.sort_to_asc}"
    	                       rendered="#{PrivateMessagesTool.sortType == 'to_asc'}"/>
    	       <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                       title="#{msgs.sort_to_desc}" alt="#{msgs.sort_to_desc}"
    	                       rendered="#{PrivateMessagesTool.sortType == 'to_desc'}"/>
    	       <f:param name="sortColumn" value="to"/>
    	     </h:commandLink>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.sendToStringDecorated}" rendered="#{rcvdItems.hasRead}" />
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.sendToStringDecorated}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>	
		  	  
		  <h:column>
		    <f:facet name="header">
		       <h:commandLink value="#{msgs.pvt_date}"
		                      title="#{msgs.sort_date}">
		         <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                         title="#{msgs.sort_date_asc}" alt="#{msgs.sort_date_asc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'date_asc'}"/>
    	         <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                         title="#{msgs.sort_date_desc}" alt="#{msgs.sort_date_desc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'date_desc'}"/>    	                       
    	         <f:param name="sortColumn" value="date"/>
    	       </h:commandLink>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.created}" rendered="#{rcvdItems.hasRead}">
			     <f:convertDateTime pattern="#{msgs.date_format}" />
			 </h:outputText>
		   <h:outputText styleClass="unreadMsg" value="#{rcvdItems.msg.created}" rendered="#{!rcvdItems.hasRead}">
			   <f:convertDateTime pattern="#{msgs.date_format}" />
			 </h:outputText>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:commandLink value="#{msgs.pvt_label}"
		                      title="#{msgs.sort_label}">
		         <h:graphicImage value="/images/sortascending.gif" style="border:0" 
    	                         title="#{msgs.sort_label_asc}" alt="#{msgs.sort_label_asc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'label_asc'}"/>
    	         <h:graphicImage value="/images/sortdescending.gif" style="border:0" 
    	                         title="#{msgs.sort_label_desc}" alt="#{msgs.sort_label_desc}"
    	                         rendered="#{PrivateMessagesTool.sortType == 'label_desc'}"/>    	                       
    	         <f:param name="sortColumn" value="label"/>
    	       </h:commandLink>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.label}"/>
		  </h:column>
		</h:dataTable>
		
	  <mf:hierPvtMsgDataTable styleClass="listHier lines nolines" id="threaded_pvtmsgs" width="100%" 
	                          value="#{PrivateMessagesTool.decoratedPvtMsgs}" 
	  	                        var="rcvdItems" 
	  	                        rendered="#{PrivateMessagesTool.selectView == 'threaded'}"
	                        	 expanded="true"
								 columnClasses="attach,attach,specialLink,bogus,bogus,bogus">
		 	<h:column>
		    <f:facet name="header">
 					<h:commandLink action="#{PrivateMessagesTool.processCheckAll}" value="#{msgs.cdfm_checkall}" 
 					               title="#{msgs.cdfm_checkall}"/>
		     <%--<h:commandButton alt="SelectAll" image="/sakai-messageforums-tool/images/checkbox.gif" action="#{PrivateMessagesTool.processSelectAllJobs}"/>--%>
		    </f:facet>
				<h:selectBooleanCheckbox value="#{rcvdItems.isSelected}" onclick="updateCount(this.checked); toggleBulkOperations(anyChecked(), 'prefs_pvt_form');" />
		  </h:column>
		  <h:column>
				<f:facet name="header">
					<h:graphicImage value="/images/attachment.gif" alt="#{msgs.msg_has_attach}" />								
				</f:facet>
				<h:graphicImage value="/images/attachment.gif" rendered="#{rcvdItems.msg.hasAttachments}" alt="#{msgs.msg_has_attach}" />			 
			</h:column>
			<h:column id="_msg_subject">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_subject}"/>
		    </f:facet>
		      <h:commandLink action="#{PrivateMessagesTool.processPvtMsgDetail}" immediate="true" title=" #{rcvdItems.msg.title}">
            <h:outputText value=" #{rcvdItems.msg.title}" rendered="#{rcvdItems.hasRead}"/>
            <h:outputText styleClass="unreadMsg" value=" #{rcvdItems.msg.title}" rendered="#{!rcvdItems.hasRead}"/>
            <f:param value="#{rcvdItems.msg.id}" name="current_msg_detail"/>
          </h:commandLink>
		  </h:column>			
		  <h:column rendered="#{PrivateMessagesTool.msgNavMode != 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_authby}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.msg.author}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.msg.author}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.msgNavMode == 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_to}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.sendToStringDecorated}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.sendToStringDecorated}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>		  
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_date}"/>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.created}" rendered="#{rcvdItems.hasRead}">
			     <f:convertDateTime pattern="#{msgs.date_format}" />
			 </h:outputText>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.msg.created}" rendered="#{!rcvdItems.hasRead}">
			     <f:convertDateTime pattern="#{msgs.date_format}" />
			 </h:outputText>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_label}"/>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.label}"/>
		  </h:column>
		</mf:hierPvtMsgDataTable>
		
<%-- Added if user clicks Check All --%>
    <script language="Javascript" type="text/javascript">
     // setting number checked just in case Check All being processed
     // needed to 'enable' bulk operations
     numberChecked = <h:outputText value="#{PrivateMessagesTool.numberChecked}" />;

     toggleBulkOperations(numberChecked > 0, 'prefs_pvt_form');
     </script>

		 </h:form>
	</sakai:view>
</f:view>
