/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.component.app.messageforums.exception;

// ONC customization!

/**
 * This exception is thrown when a user attempts to reply to or forward a "corrupted" message
 * @author michellewagner
 *
 */
public class CorruptedMessageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CorruptedMessageException() {
        super("Attempt to reply to or forward a corrupt private message");
    }

    public CorruptedMessageException(String message) {
        super(message);
    }

    public CorruptedMessageException(String message, Throwable t) {
        super(message, t);
    }

    public CorruptedMessageException(Throwable t) {
        super(t);
    }
    
}
