package org.sakaiproject.component.app.messageforums.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.sakaiproject.api.app.messageforums.PrivateMessage;


public class PrivateMessageEventListener implements PreUpdateEventListener {
	
	private static final Log LOG = LogFactory.getLog(PrivateMessageEventListener.class);
	
	public void init() {
		LOG.info("init()");
	}


	public boolean onPreUpdate(PreUpdateEvent event) {
		debug("onPreUpdate() - " + event.getEntity().getClass().getName());
		Object entity = event.getEntity();
		if (entity instanceof PrivateMessage) {
			PrivateMessage currentMsg = (PrivateMessage)entity;

			PrivateMessageDataChecker.checkMessage(currentMsg);

		}
		return false;
	}

	/**
	 * Convenience method to spit out debug methods
	 * @param msg
	 */
	private void debug(String msg) {
		if (LOG.isDebugEnabled())
	    {
	      LOG.debug(msg);
	    }
	}
}
