package org.sakaiproject.component.app.messageforums.exception;

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
