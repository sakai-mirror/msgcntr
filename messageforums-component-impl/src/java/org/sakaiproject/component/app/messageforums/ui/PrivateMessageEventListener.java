package org.sakaiproject.component.app.messageforums.ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.sakaiproject.api.app.messageforums.PrivateMessage;

import org.sakaiproject.component.app.messageforums.dao.hibernate.PrivateMessageImpl;
import org.sakaiproject.component.app.messageforums.exception.MessageCenterDataIntegrityException;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.cover.SqlService;

public class PrivateMessageEventListener implements PreUpdateEventListener {
	
	private static final Log LOG = LogFactory.getLog(PrivateMessageEventListener.class);
	
	public void init() {
		LOG.info("init()");
	}


	public boolean onPreUpdate(PreUpdateEvent event) {
		debug("onPreUpdate()");
		Object entity = event.getEntity();
		if (entity instanceof PrivateMessage) {
			PrivateMessage currentMsg = (PrivateMessage)entity;

			if (currentMsg.getId() != null) {
				//this had better be a normal update...

				try {
					final Connection connection = SqlService.borrowConnection();

					boolean wasCommit = connection.getAutoCommit();

					connection.setAutoCommit(false);

					String sql = "select ID, TITLE, BODY, AUTHOR from MFR_MESSAGE_T where ID = ?";
					Object[] fields = new Object[1];
					fields[0] = currentMsg.getId();
					List<PrivateMessage> msgs = SqlService.dbRead(connection, sql, fields, new SqlReader() {



						public PrivateMessage readSqlResultRecord(ResultSet result)
						{
							try
							{
								PrivateMessage tmpMsg = new PrivateMessageImpl();
								String id = result.getString(1);
								String title = result.getString(2);
								String body = result.getString(3);
								String author = result.getString(4);

								tmpMsg.setId(Long.parseLong(id));
								tmpMsg.setTitle(title);
								tmpMsg.setBody(body);
								tmpMsg.setAuthor(author);
								return tmpMsg;

							} catch (SQLException ignore) {
								LOG.error("Had some sql problems", ignore);
								return null;
							}
						}
					});

					connection.commit();
					connection.setAutoCommit(wasCommit);
					SqlService.returnConnection(connection);
					
					if (msgs.size() == 1) {
						PrivateMessage loopMsg = msgs.get(0);
						if (loopMsg.getId().equals(currentMsg.getId())) {
							//just making sure the id is the same
							if (!loopMsg.getTitle().equals(currentMsg.getTitle()) || 
								!loopMsg.getBody().equals(currentMsg.getBody()) ||
								!loopMsg.getAuthor().equals(currentMsg.getAuthor())) {
								//throw an exception
								String msg ="Message with id: " + String.valueOf(currentMsg.getId()) + " was about to be overwritten - failing transaction to prevent data loss";
								
								LOG.error(msg);
								throw new MessageCenterDataIntegrityException(msg);								
							}
						}
					}
					else {
						//not sure what else to do
					}
					
					
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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
