package com.nokor.frmk.security.tools;

/**
 * 
 * @author prasnar
 * @version $version$
 */
public class PasswordGetException extends RuntimeException {
    /** */
	private static final long serialVersionUID = -712632921675359198L;

	/**
     * 
     * @param e
     */
    public PasswordGetException(Exception e) {
        super(e);
    }

    /**
     * 
     * @param message
     */
    public PasswordGetException(String message) {
        super(message);
    }

    /**
     * 
     * @param message
     * @param innerException
     */
    public PasswordGetException(String message, Exception innerException) {
        super(message, innerException);
    }
}
