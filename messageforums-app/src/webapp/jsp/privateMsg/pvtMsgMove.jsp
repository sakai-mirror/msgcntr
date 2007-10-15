<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>
<f:view>
	<sakai:view title="#{msgs.pvt_move} #{msgs.pvt_rcvd_msgs}">
<!--jsp/privateMsg/pvtMsgMove.jsp-->	
		<h:form id="pvtMsgMove">
		<script type="text/javascript" src="/library/js/jquery.js"></script>
                <sakai:script contextBase="/sakai-messageforums-tool" path="/js/sak-10625.js"/>
		  <sakai:tool_bar_message value="#{msgs.pvt_msgs_label} #{msgs.pvt_move_msg_to}" /> 
			<h:messages styleClass="alertMessage" id="errorMessages" /> 

			<h:dataTable value="#{PrivateMessagesTool.decoratedForum}" var="forum">
		    <h:column>
					<h:dataTable id="privateForums" value="#{forum.topics}" var="topic"  >
						<h:column>
					    <f:facet name="header">
						</f:facet>
					    <h:selectOneRadio value="#{PrivateMessagesTool.moveToTopic}" onclick="this.form.submit();"
					    			valueChangeListener="#{PrivateMessagesTool.processPvtMsgParentFolderMove}">
					      	<f:selectItem itemValue="#{topic.topic.uuid}"  
							      	itemDisabled="#{PrivateMessagesTool.selectedTopic.topic == topic.topic}"
					      			itemLabel="#{topic.topic.title}" />	
					      	<%--<f:param value="#{topic.topic.uuid}" name="pvtMsgMoveTopicId"/>--%>
		  			    </h:selectOneRadio>
					  </h:column>
					  <h:column>
					  	<h:outputText value="#{msgs.pvt_move_current_folder}" styleClass="unreadMsg"
					      			rendered="#{PrivateMessagesTool.selectedTopic.topic == topic.topic}"/>
					  </h:column>
					</h:dataTable>
				</h:column>
		  </h:dataTable> 		  
        
 				
			<sakai:button_bar>
		    <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgMoveMessage}" value="#{msgs.pvt_move_msg}" accesskey="s" styleClass="active"/>
		    <sakai:button_bar_item action="#{PrivateMessagesTool.processPvtMsgCancelToDetailView}" value="#{msgs.pvt_cancel}" accesskey="x" />
		  </sakai:button_bar>   
	          
		</h:form>

	</sakai:view>
</f:view>

