package org.seuksa.frmk.tools.exception;


/**
 * 
 * @author prasnar
 * @version $version$
 */
public class EntityRecycledBinException extends RuntimeException implements ErrorHandlerExceptionAware {
    /** */
	private static final long serialVersionUID = 4165050495058411669L;

	private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public EntityRecycledBinException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
	/**
     * 
     * @param e
     */
    public EntityRecycledBinException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public EntityRecycledBinException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public EntityRecycledBinException(String message, Exception innerException) {
        super(message, innerException);
    }

	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
