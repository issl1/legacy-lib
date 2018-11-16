package org.seuksa.frmk.tools.exception;


/**
 * 
 * @author prasnar
 * @version $version$
 */
public class EntityActivateException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = 4165050495058411669L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public EntityActivateException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
	/**
     * 
     * @param e
     */
    public EntityActivateException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public EntityActivateException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public EntityActivateException(String message, Exception innerException) {
        super(message, innerException);
    }

	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
