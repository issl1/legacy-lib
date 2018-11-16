package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 * @author youhort.ly
 */
public class JournalEntryException extends RuntimeException implements ErrorHandlerExceptionAware {
	
	/**
	 */
	private static final long serialVersionUID = 2803552589986705750L;
	
	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public JournalEntryException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public JournalEntryException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public JournalEntryException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public JournalEntryException(String errorKey, Object[] params) {
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
