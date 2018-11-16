package org.seuksa.frmk.tools.exception;

/**
 * For functional errors catching purpose 
 * @author Prasnar
 * @version 1.0
 */
public class FunctionalException extends Exception implements ErrorHandlerExceptionAware {
	/**  */
    private static final long serialVersionUID = 4768329319450337779L;

    private ErrorHandler errorHandler;
   
    /**
	 * 
	 * @param message
	 * @param e
	 */
	public FunctionalException(Throwable e) {
		super(e);
	}
	
	/**
	 * 
	 * @param e
	 */
	public FunctionalException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param e
	 */
	public FunctionalException(String message, Throwable e) {
		super(message, e);
	}
	
    /**
     * 
     * @param errorKey
     * @param params
     */
    public FunctionalException(String errorKey, Object[] params) {
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
