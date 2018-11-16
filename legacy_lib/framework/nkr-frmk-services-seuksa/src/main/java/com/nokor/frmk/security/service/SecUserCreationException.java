package com.nokor.frmk.security.service;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 * 
 * @author prasnar
 * @version $version$
 */
public class SecUserCreationException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = -4504195450629096408L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public SecUserCreationException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
    /**
     * 
     * @param e
     */
    public SecUserCreationException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public SecUserCreationException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public SecUserCreationException(String message, Exception innerException) {
        super(message, innerException);
    }
    
    @Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
