package org.sakaiproject.component.app.messageforums.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.hibernate.type.Type;
import org.sakaiproject.api.app.messageforums.PrivateMessage;

import org.sakaiproject.component.app.messageforums.dao.hibernate.PrivateMessageImpl;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.cover.SqlService;

public class PrivateMessageEventListener implements PreUpdateEventListener {
	
	private static final Log LOG = LogFactory.getLog(PrivateMessageEventListener.class);
	
	public void init() {
		LOG.info("init()");
	}


	public boolean onPreUpdate(PreUpdateEvent event) {
		// TODO Auto-generated method stub
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


	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		debug("onSaveOrUpdate()");
		Object entity = event.getObject();
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
								String id = result.getString(0);
								String title = result.getString(1);
								String body = result.getString(2);
								String author = result.getString(3);

								tmpMsg.setId(Long.parseLong(id));
								tmpMsg.setTitle(title);
								tmpMsg.setBody(body);
								tmpMsg.setAuthor(author);
								return tmpMsg;

							} catch (SQLException ignore) {
								LOG.error(ignore);
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
	}

	/**
	 * @deprecated
	 * @param entity
	 * @param id
	 * @param state
	 * @param propertyNames
	 * @param types
	 * @return
	 * @throws CallbackException
	 */
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {

		debug("onSave()");
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
								String id = result.getString(0);
								String title = result.getString(1);
								String body = result.getString(2);
								String author = result.getString(3);

								tmpMsg.setId(Long.parseLong(id));
								tmpMsg.setTitle(title);
								tmpMsg.setBody(body);
								tmpMsg.setAuthor(author);
								return tmpMsg;

							} catch (SQLException ignore) {
								LOG.error(ignore);
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

	private void debug(String msg) {
		if (LOG.isDebugEnabled())
	    {
	      LOG.debug(msg);
	    }
	}
	
	/**
	 * New exception to indicate when a message was about to be overwritten
	 * @author chrismaurer
	 *
	 */
	public class MessageCenterDataIntegrityException extends RuntimeException {

		public MessageCenterDataIntegrityException() {
			super();
		}
		
		public MessageCenterDataIntegrityException(String message) {
			super(message);
		}
		
		public MessageCenterDataIntegrityException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public MessageCenterDataIntegrityException(Throwable cause) {
			super(cause);
		}
	}
}
