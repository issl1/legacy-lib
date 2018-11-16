package org.seuksa.frmk.tools.exception;

/**
 * For Entity Creation errors catching purpose
 * 
 * @author prasnar
 */
public class EntityCreationException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = 786121625666842185L;

	private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityCreationException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public EntityCreationException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public EntityCreationException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public EntityCreationException(String errorKey, Object[] params) {
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
