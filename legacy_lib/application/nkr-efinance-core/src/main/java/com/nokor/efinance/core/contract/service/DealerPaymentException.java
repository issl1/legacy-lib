package com.nokor.efinance.core.contract.service;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.seuksa.frmk.tools.exception.ErrorHandlerExceptionAware;

/**
 * @author youhort.ly
 */
public class DealerPaymentException extends RuntimeException implements ErrorHandlerExceptionAware {
	
	/**
	 */
	private static final long serialVersionUID = 4270907867496433098L;
	
	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public DealerPaymentException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public DealerPaymentException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public DealerPaymentException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public DealerPaymentException(String errorKey, Object[] params) {
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
