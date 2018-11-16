package org.seuksa.frmk.tools.exception;

/**
 * For Entity Update errors catching purpose
 * 
 * @author prasnar
 */
public class EntityUpdateException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = 4617154154808000030L;

	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityUpdateException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntityUpdateException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityUpdateException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public EntityUpdateException(String errorKey, Object[] params) {
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
