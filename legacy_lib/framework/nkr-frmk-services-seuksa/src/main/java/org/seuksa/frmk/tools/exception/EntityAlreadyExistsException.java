package org.seuksa.frmk.tools.exception;

/**
 * For Entity Already Exists errors catching purpose
 * 
 * @author prasnar
 */
public class EntityAlreadyExistsException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = -3564113627301856690L;

	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityAlreadyExistsException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntityAlreadyExistsException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityAlreadyExistsException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public EntityAlreadyExistsException(String errorKey, Object[] params) {
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
