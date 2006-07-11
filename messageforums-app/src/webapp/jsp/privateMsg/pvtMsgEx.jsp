<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<f:loadBundle basename="org.sakaiproject.tool.messageforums.bundle.Messages" var="msgs"/>
<link href='/sakai-messageforums-tool/css/msgForums.css' rel='stylesheet' type='text/css' />

<f:view>
	<sakai:view title="#{msgs.pvt_rcvd_msgs}">

		<h:form id="prefs_form_search">
			<sakai:script contextBase="/sakai-messageforums-tool" path="/js/forum.js"/>		
			
			<div class="breadCrumb">
				<h:commandLink action="#{PrivateMessagesTool.processActionHome}" value="#{msgs.cdfm_message_forums}" title=" #{msgs.cdfm_message_forums}"/> /
	  		  <h:commandLink action="#{PrivateMessagesTool.processActionPrivateMessages}" value="#{msgs.cdfm_message_pvtarea}" title=" #{msgs.cdfm_message_forums}"/> /
				<h:commandLink action="#{PrivateMessagesTool.processDisplayForum}" value="#{PrivateMessagesTool.msgNavMode}" title=" #{msgs.cdfm_message_forums}"/> /
				<h:outputText value="#{msgs.pvt_search}" />	
		  </div>

			<h:messages styleClass="alertMessage" id="errorMessages" />
			<%@include file="msgHeader.jsp"%>
<%--
	  <h:dataTable styleClass="listHier" id="pvtmsgs" width="100%" value="#{PrivateMessagesTool.searchPvtMsgs}" var="rcvdItems" >   
		  <h:column>
		    <f:facet name="header">		     
		    </f:facet>
		    <h:selectBooleanCheckbox value="#{rcvdItems.isSelected}"/>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_subject}"/>
		    </f:facet>
		      <h:commandLink action="#{PrivateMessagesTool.processPvtMsgDetail}" immediate="true">
            <h:outputText value=" #{rcvdItems.msg.title}" rendered="#{rcvdItems.hasRead}"/>
            <h:outputText style="font-weight:bold" value=" #{rcvdItems.msg.title}" rendered="#{!rcvdItems.hasRead}"/>
            <f:param value="#{rcvdItems.msg.id}" name="current_msg_detail"/>
          </h:commandLink>              
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_authby}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.msg.athor}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText style="font-weight:bold" value="#{rcvdItems.msg.author}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_date}"/>
		    </f:facet>
		    <h:outputText value="#{rcvdItems.msg.created}" rendered="#{rcvdItems.hasRead}">
		        <f:convertDateTime pattern="#{msgs.date_format}" />
		    </h:outputText>
		    <h:outputText style="font-weight:bold" value="#{rcvdItems.msg.created}" rendered="#{!rcvdItems.hasRead}">
		        <f:convertDateTime pattern="#{msgs.date_format}" />
		    </h:outputText>
		  </h:column>
		  <h:column>
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_label}"/>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.label}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText style="font-weight:bold" value="#{rcvdItems.msg.label}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		</h:dataTable>

--%>


	  <h:dataTable styleClass="listHier" id="pvtmsgs" width="100%" value="#{PrivateMessagesTool.searchPvtMsgs}" var="rcvdItems" 
	  	rendered="#{PrivateMessagesTool.selectView != 'threaded'}">   
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded'}">
		    <f:facet name="header">
 					<h:commandLink action="#{PrivateMessagesTool.processCheckAll}" value="#{msgs.cdfm_checkall}" 
 					               rendered="#{PrivateMessagesTool.msgNavMode == 'Deleted'}" title="#{msgs.cdfm_checkall}" />
		     <%--<h:commandButton alt="SelectAll" image="/sakai-messageforums-tool/images/checkbox.gif" action="#{PrivateMessagesTool.processSelectAllJobs}"/>--%>
		    </f:facet>
<%--		    <h:selectBooleanCheckbox value="#{rcvdItems.isSelected}"/>--%>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded'}">
				<f:facet name="header">
					<h:graphicImage value="/images/attachment.gif" alt="#{msgs.msg_has_attach}"/>								
				</f:facet>
				<h:graphicImage value="/images/attachment.gif" rendered="#{rcvdItems.msg.hasAttachments}" alt="#{msgs.msg_has_attach}" />			 
			</h:column>
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_subject}"/>
		    </f:facet>
		      <h:commandLink action="#{PrivateMessagesTool.processPvtMsgDetail}" immediate="true" title=" #{rcvdItems.msg.title}">
            <h:outputText value=" #{rcvdItems.msg.title}" rendered="#{rcvdItems.hasRead}"/>
            <h:outputText style="font-weight:bold" value=" #{rcvdItems.msg.title}" rendered="#{!rcvdItems.hasRead}"/>
            <f:param value="#{rcvdItems.msg.id}" name="current_msg_detail"/>
          </h:commandLink>
		  </h:column>			
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded' && PrivateMessagesTool.msgNavMode != 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_authby}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.msg.author}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText style="font-weight:bold" value="#{rcvdItems.msg.author}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded' && PrivateMessagesTool.msgNavMode == 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_to}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.sendToStringDecorated}" rendered="#{rcvdItems.hasRead}" />
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.sendToStringDecorated}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>	
		  	  
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded'}">
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
		  <h:column rendered="#{PrivateMessagesTool.selectView != 'threaded'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_label}"/>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.label}"/>
		  </h:column>
		</h:dataTable>
		
	  <mf:hierPvtMsgDataTable styleClass="listHier" id="threaded_pvtmsgs" width="100%" 
	  	value="#{PrivateMessagesTool.searchPvtMsgs}" 
	  	var="rcvdItems" 
	  	rendered="#{PrivateMessagesTool.selectView == 'threaded'}"
	  	expanded="true">
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded'}">
		    <f:facet name="header">
		    </f:facet>
		    <h:selectBooleanCheckbox value="#{rcvdItems.isSelected}"/>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded'}">
				<f:facet name="header">
					<h:graphicImage value="/images/attachment.gif" alt="#{msgs.msg_has_attach}" />								
				</f:facet>
				<h:graphicImage value="/images/attachment.gif" rendered="#{rcvdItems.msg.hasAttachments}" alt="#{msgs.msg_has_attach}" />			 
			</h:column>
			<h:column id="_msg_subject"
				rendered="#{PrivateMessagesTool.selectView == 'threaded'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_subject}"/>
		    </f:facet>
		      <h:commandLink action="#{PrivateMessagesTool.processPvtMsgDetail}" immediate="true" title=" #{rcvdItems.msg.title}">
            <h:outputText value=" #{rcvdItems.msg.title}" rendered="#{rcvdItems.hasRead}"/>
            <h:outputText styleClass="unreadMsg" value=" #{rcvdItems.msg.title}" rendered="#{!rcvdItems.hasRead}"/>
            <f:param value="#{rcvdItems.msg.id}" name="current_msg_detail"/>
          </h:commandLink>
		  </h:column>			
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded' && PrivateMessagesTool.msgNavMode != 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_authby}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.msg.author}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.msg.author}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded' && PrivateMessagesTool.msgNavMode == 'Sent'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_to}"/>
		    </f:facet>		     		    
		     <h:outputText value="#{rcvdItems.sendToStringDecorated}" rendered="#{rcvdItems.hasRead}"/>
		     <h:outputText styleClass="unreadMsg" value="#{rcvdItems.sendToStringDecorated}" rendered="#{!rcvdItems.hasRead}"/>
		  </h:column>		  
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded'}">
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
		  <h:column rendered="#{PrivateMessagesTool.selectView == 'threaded'}">
		    <f:facet name="header">
		       <h:outputText value="#{msgs.pvt_label}"/>
		    </f:facet>
		     <h:outputText value="#{rcvdItems.msg.label}"/>
		  </h:column>
		</mf:hierPvtMsgDataTable>
		
		
		</h:form>

	</sakai:view>
</f:view>
