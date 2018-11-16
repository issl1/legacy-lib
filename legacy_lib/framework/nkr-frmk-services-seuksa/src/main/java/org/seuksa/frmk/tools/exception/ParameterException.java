package org.seuksa.frmk.tools.exception;

/**
 * 
 * @author peng.boren
 *
 */
public class ParameterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	private String errorMessage;

	public ParameterException() {
        super();
    }
	
	/**
     * 
     * @param e
     */
    public ParameterException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public ParameterException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public ParameterException(String message, Exception innerException) {
        super(message, innerException);
    }
    
    /**
     * 
     * @return
     */
    public int getErrorCode() {
		return errorCode;
	}

    /**
     * 
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 
	 * @return
	 */
    public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * 
	 * @param errorMessage
	 */
    public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}
