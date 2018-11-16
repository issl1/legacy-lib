package org.seuksa.frmk.tools.exception;

/**
 * 
 * @author prasnar
 * @version $version$
 */
public class DaoException extends RuntimeException implements ErrorHandlerExceptionAware {

    /**
     * 
     */
    private static final long serialVersionUID = -7688275039731696274L;

    private ErrorHandler errorHandler;
	
	/**
	 * 
	 * @param errorHandler
	 */
	public DaoException(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
	
    /**
     * 
     * @param e
     */
    public DaoException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public DaoException(String message, Exception innerException) {
        super(message, innerException);
    }
    
    @Override
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
