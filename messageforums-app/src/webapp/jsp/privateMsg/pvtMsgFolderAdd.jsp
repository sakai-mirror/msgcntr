<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.messageforums.bundle.Messages"/>
</jsp:useBean>

<f:view>
  <sakai:view title="#{msgs.pvt_msgs_label} #{msgs.pvt_create_folder}">
<!--jsp/privateMsg/pvtMsgFolderAdd.jsp-->
		<h:form id="pvtMsgFolderAdd">
		
			  <sakai:tool_bar_message value="#{msgs.pvt_msgs_label} #{msgs.pvt_create_folder}" />
			 
				<div class="instruction">
				  <h:outputText value="#{msgs.cdfm_required}"/> <h:outputText value="#{msgs.pvt_star}" styleClass="reqStarInline"/>
				</div>
			   
			   <h:messages styleClass="alertMessage" id="errorMessages" />
			 
 <h:panelGrid styleClass="jsfFormTable" columns="2" summary="layout">
			  <h:panelGroup styleClass="shorttext required">
				  <h:outputText value="#{msgs.pvt_star}" styleClass="reqStar"/>
				  <h:outputLabel for="title" value="#{msgs.pvt_folder_title}"/>
			  </h:panelGroup>
				<h:panelGroup styleClass="shorttext ">
				<h:inputText id="title" value="#{PrivateMessagesTool.addFolder}" />
				</h:panelGroup>
			  </h:panelGrid>
			     
				<sakai:button_bar>
			  	  <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgFldCreate}" value="#{msgs.pvt_add}" accesskey="s"  styleClass="active"/>
			    <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgFldAddCancel}" value="#{msgs.pvt_cancel}" accesskey="x" />
			  </sakai:button_bar>   
           
		 </h:form>

	</sakai:view>
</f:view>

