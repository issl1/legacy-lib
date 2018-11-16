package org.seuksa.frmk.tools.exception;

/**
 * For Entity can not find errors catching purpose
 * 
 * @author prasnar
 */
public class EntityNotFoundException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = -3564113627301856690L;

	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityNotFoundException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityNotFoundException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public EntityNotFoundException(String errorKey, Object[] params) {
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
