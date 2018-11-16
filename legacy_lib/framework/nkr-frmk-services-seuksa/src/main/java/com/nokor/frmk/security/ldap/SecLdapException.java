package com.nokor.frmk.security.ldap;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 * 
 * @author prasnar
 * @version $version$
 */
public class SecLdapException extends RuntimeException implements ErrorHandlerExceptionAware {
 	/** */
	private static final long serialVersionUID = 2229123528675252891L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public SecLdapException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
    /**
     * 
     * @param e
     */
    public SecLdapException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public SecLdapException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public SecLdapException(String message, Exception innerException) {
        super(message, innerException);
    }
    
    @Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
