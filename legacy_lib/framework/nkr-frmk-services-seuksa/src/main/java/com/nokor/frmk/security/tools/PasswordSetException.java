package com.nokor.frmk.security.tools;

/**
 * 
 * @author prasnar
 * @version $version$
 */
public class PasswordSetException extends RuntimeException {
    /** */
	private static final long serialVersionUID = -5665907761134584943L;

	/**
     * 
     * @param e
     */
    public PasswordSetException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public PasswordSetException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public PasswordSetException(String message, Exception innerException) {
        super(message, innerException);
    }
}
