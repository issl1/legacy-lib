package com.nokor.common.app.workflow;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 *  
 * @author prasnar
 */
public class WorkflowException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = -4675149491803017407L;

	private ErrorHandler errorHandler;

	/**
	 * 
	 * @param message
	 * @param e
	 */
	public WorkflowException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public WorkflowException(String message) {
		super(message);
	}
	
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public WorkflowException(String message, Throwable e) {
		super(message, e);
	}
	
	/**
     * 
     * @param errorKey
     * @param params
     */
    public WorkflowException(String errorKey, Object[] params) {
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
