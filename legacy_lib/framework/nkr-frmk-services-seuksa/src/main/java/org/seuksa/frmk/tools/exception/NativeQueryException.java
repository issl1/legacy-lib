package org.seuksa.frmk.tools.exception;

/**
 * @author ly.youhort
 *
 */
public class NativeQueryException extends Exception {

	private static final long serialVersionUID = -2822841769439713156L;

	public NativeQueryException() {
	    super();
    }

	public NativeQueryException(final String message, final Throwable cause) {
	    super(message, cause);
    }

	public NativeQueryException(final String message) {
	    super(message);
    }

	public NativeQueryException(final Throwable cause) {
	    super(cause);
    }

}
