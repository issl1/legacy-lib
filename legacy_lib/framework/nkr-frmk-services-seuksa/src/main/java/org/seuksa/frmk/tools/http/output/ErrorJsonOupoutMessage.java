package org.seuksa.frmk.tools.http.output;

/**
 * 
 * @author peng.boren
 *
 */
public class ErrorJsonOupoutMessage extends JsonOutputMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2147155093596535201L;
	
	public int code;
	public String message;
	
	public ErrorJsonOupoutMessage() {
		setType(ERROR);
	}
	
	/**
	 * @return code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @param code
	 * 				the error code to set.
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * @return message string
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message 
	 * 					the message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
