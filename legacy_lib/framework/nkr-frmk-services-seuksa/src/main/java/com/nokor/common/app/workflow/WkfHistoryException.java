package com.nokor.common.app.workflow;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 *  
 * @author prasnar
 */
public class WkfHistoryException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = -608376592169409086L;

	private ErrorHandler errorHandler;

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public WkfHistoryException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public WkfHistoryException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public WkfHistoryException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public WkfHistoryException(String errorKey, Object[] params) {
    	errorHandler = new ErrorHandler(errorKey, params);
	}

    @Override
    public ErrorHandler getErrorHandler() {
       return errorHandler;
    }
   
   
    /**
     * 
     * @return
     */
    public long getCode() {
    	return errorHandler.getCode().getId();
    }
}
