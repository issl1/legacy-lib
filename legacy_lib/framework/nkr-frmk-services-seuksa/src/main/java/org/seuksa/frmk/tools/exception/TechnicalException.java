package org.seuksa.frmk.tools.exception;

/**
 * For technical errors catching purpose 
 * @author prasnar
 */
public class TechnicalException extends RuntimeException implements ErrorHandlerExceptionAware {
	/** */
    private static final long serialVersionUID = -897030700509427958L;

    private ErrorHandler errorHandler;

    /**
	 * 
	 * @param message
	 * @param e
	 */
	public TechnicalException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param message
	 */
	public TechnicalException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public TechnicalException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public TechnicalException(String errorKey, Object[] params) {
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
