<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<f:loadBundle basename="org.sakaiproject.tool.messageforums.bundle.Messages" var="msgs"/>
<f:view>
  <f:loadBundle basename="org.sakaiproject.tool.messageforums.bundle.Messages" var="msgs"/>
  <sakai:view title="#{msgs.cdfm_reply_tool_bar_message}">
<!--jsp/dfMessageReply.jsp-->    
      <h:form id="dfCompose">
        <h:outputText styleClass="alertMessage" value="#{msgs.cdfm_reply_deleted}" rendered="#{ForumTool.errorSynch}" />
	

		  <h3><h:outputText value="#{msgs.cdfm_reply_tool_bar_message}"  /></h3>
      <h4><h:outputText value="#{ForumTool.selectedForum.forum.title}" />
		    <h:outputText value=" - "/>
	      <h:outputText value="#{ForumTool.selectedTopic.topic.title}"/>
		  </h4>	

    <p class="textPanel">
		  <h:outputText value="#{ForumTool.selectedTopic.topic.shortDescription}"/>
    </p>


		<p class="instruction">
              <h:outputText value="#{msgs.cdfm_required}"/>
              <h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" />
		</p>	  
          <h:panelGrid styleClass="jsfFormTable" columns="2" style="width: 100%;">
            <h:panelGroup>
	   			     <h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStar"/>
			  		   <h:outputLabel for="df_compose_title"><h:outputText value="#{msgs.cdfm_reply_title}" /></h:outputLabel>
 					  </h:panelGroup>
				   <h:inputText value="#{ForumTool.composeTitle}" style="width: 30em;" required="true" id="df_compose_title" />

          </h:panelGrid>
          
          <h:message for="df_compose_title" styleClass="alertMessage" id="errorMessages" />
		  <h4>
		     <h:outputText value="#{msgs.cdfm_message}" /> 
		  </h4>
            <sakai:rich_text_area value="#{ForumTool.composeBody}" rows="17" columns="70"/>
<%--********************* Attachment *********************--%>	
	        <h4>
	          <h:outputText value="#{msgs.cdfm_att}"/>
	        </h4>
			<h:outputText value="#{msgs.cdfm_no_attachments}" rendered="#{empty ForumTool.attachments}" styleClass="instruction" />

	          <sakai:button_bar>
	          	<sakai:button_bar_item action="#{ForumTool.processAddAttachmentRedirect}" value="#{msgs.cdfm_button_bar_add_attachment_redirect}" immediate="true"/>
	          </sakai:button_bar>
	        <%-- gsilver:moving rendered attr from column to table to avoid childless table if empty--%>
		    <h:dataTable styleClass="listHier lines nolines" id="attmsg" width="100%" value="#{ForumTool.attachments}" var="eachAttach"   rendered="#{!empty ForumTool.attachments}"
			columnClasses="attach,bogus,specialLink itemAction,bogus,bogus">
			  <h:column rendered="#{!empty ForumTool.attachments}">
				  <h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-excel'}" alt="" />
				  <h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachmentType == 'text/html'}" alt="" />
				  <h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachmentType == 'application/pdf'}"/>
				  <h:graphicImage url="/images/ppt.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
				  <h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachmentType == 'text/plain'}" alt="" />
				  <h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachmentType == 'application/msword'}" alt="" />
				</h:column>
				  <h:column>
					  <f:facet name="header">
						<h:outputText value="#{msgs.cdfm_title}"/>
					</f:facet>
					  <h:outputText value="#{eachAttach.attachmentName}"/>			
				</h:column>
				<h:column>
				  <h:commandLink action="#{ForumTool.processDeleteAttach}" 
								immediate="true"
								onfocus="document.forms[0].onsubmit();"
								title="#{msgs.cdfm_remove}">
				    <h:outputText value="#{msgs.cdfm_remove}"/>
<%--									<f:param value="#{eachAttach.attachmentId}" name="dfmsg_current_attach"/>--%>
					<f:param value="#{eachAttach.attachmentId}" name="dfmsg_current_attach"/>
				  </h:commandLink>
			  </h:column>
			  <h:column rendered="#{!empty ForumTool.attachments}">
			    <f:facet name="header">
				  <h:outputText value="#{msgs.cdfm_attsize}" />
				</f:facet>
				<h:outputText value="#{eachAttach.attachmentSize}"/>
			  </h:column>
			  <h:column rendered="#{!empty ForumTool.attachments}">
			    <f:facet name="header">
		  		  <h:outputText value="#{msgs.cdfm_atttype}" />
				</f:facet>
				<h:outputText value="#{eachAttach.attachmentType}"/>
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

	        
            
            <%--
            <tr>
              <td>
                <h:outputText value="Label" />
              </td>
              <td>
              	<h:outputText value="#{ForumTool.selectedMessage.message.label}" />  
              </td>
            </tr>
            --%>
<%--********************* Reply *********************--%>	     	

		<h4 class="textPanelHeader">
	  	<h:outputText value="#{msgs.cdfm_replyto}"/>  
	  </h4> 
	  
	  <h:panelGrid columns="2" styleClass="itemSummary">
	    <h:outputText value="#{msgs.cdfm_from}" />
	    <h:panelGroup>
	      <h:outputText value="#{ForumTool.selectedMessage.message.author}" />
	      <h:outputText value=" #{msgs.cdfm_openb}" />
	      <h:outputText value="#{ForumTool.selectedMessage.message.created}" >
          <f:convertDateTime pattern="#{msgs.date_format}" />  
        </h:outputText>
        <h:outputText value=" #{msgs.cdfm_closeb}" />
	    </h:panelGroup>
	    
	    <h:outputText value="#{msgs.cdfm_subject}" />
	    <h:outputText value="#{ForumTool.selectedMessage.message.title}" />
	    
	    <h:outputText value="#{msgs.cdfm_att}" rendered="#{!empty ForumTool.selectedMessage.message.attachments}"/>
	    <h:panelGroup rendered="#{!empty ForumTool.selectedMessage.message.attachments}">
	      <h:dataTable value="#{ForumTool.selectedMessage.message.attachments}" var="eachAttach"  rendered="#{!empty ForumTool.selectedMessage.message.attachments}" columnClasses="attach,bogus" styleClass="attachList">
					  <h:column rendered="#{!empty ForumTool.selectedMessage.message.attachments}">
						  <h:graphicImage url="/images/excel.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-excel'}" alt="" />
							<h:graphicImage url="/images/html.gif" rendered="#{eachAttach.attachmentType == 'text/html'}" alt="" />
							<h:graphicImage url="/images/pdf.gif" rendered="#{eachAttach.attachmentType == 'application/pdf'}" alt="" />
							<h:graphicImage url="/sakai-messageforums-tool/images/ppt.gif" rendered="#{eachAttach.attachmentType == 'application/vnd.ms-powerpoint'}" alt="" />
							<h:graphicImage url="/images/text.gif" rendered="#{eachAttach.attachmentType == 'text/plain'}" alt="" />
							<h:graphicImage url="/images/word.gif" rendered="#{eachAttach.attachmentType == 'application/msword'}" alt="" />
							</h:column>
								<h:column>
<%--							<h:outputLink value="#{eachAttach.attachmentUrl}" target="_blank">
							  <h:outputText value="#{eachAttach.attachmentName}"/>
							  </h:outputLink>--%>
							<h:outputLink value="#{ForumTool.attachmentUrl}" target="_blank">
							  <f:param name="attachmentId" value="#{eachAttach.attachmentId}"/>
							  <h:outputText value="#{eachAttach.attachmentName}"/>
							</h:outputLink>
					  </h:column>
					</h:dataTable>
	    </h:panelGroup> 
	    
	    <h:outputText value="#{msgs.cdfm_message}" />
	    <mf:htmlShowArea value="#{ForumTool.selectedMessage.message.body}" hideBorder="true" />
	       
	  </h:panelGrid>		
			
      <sakai:button_bar>
        <sakai:button_bar_item action="#{ForumTool.processDfReplyMsgPost}" value="#{msgs.cdfm_button_bar_post_message}" accesskey="s" styleClass="active" />
    <%--    <sakai:button_bar_item action="#{ForumTool.processDfReplyMsgSaveDraft}" value="#{msgs.cdfm_button_bar_save_draft}" /> --%>
        <sakai:button_bar_item action="#{ForumTool.processDfReplyMsgCancel}" value="#{msgs.cdfm_button_bar_cancel}" immediate="true" accesskey="x" />
      </sakai:button_bar>

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

