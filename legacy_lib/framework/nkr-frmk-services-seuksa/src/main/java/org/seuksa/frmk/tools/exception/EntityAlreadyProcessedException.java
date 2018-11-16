package org.seuksa.frmk.tools.exception;

/**
 * For Entity Already Processed errors catching purpose
 * 
 * @author prasnar
 */
public class EntityAlreadyProcessedException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
	private static final long serialVersionUID = -3160712797730863241L;

	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityAlreadyProcessedException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntityAlreadyProcessedException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityAlreadyProcessedException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public EntityAlreadyProcessedException(String errorKey, Object[] params) {
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
