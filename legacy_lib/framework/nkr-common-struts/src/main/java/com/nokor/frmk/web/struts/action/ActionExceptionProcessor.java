package com.nokor.frmk.web.struts.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;


/**
 * 
 * @author prasnar
 *
 */
public class ActionExceptionProcessor extends AbstractActionSupport {
	/** */
	private static final long serialVersionUID = -1518255908088106601L;
	
	private Exception exception;
	
	/**
	 * 
	 */
	public ActionExceptionProcessor() {
		super();
		logger.info("ActionExceptionProcessor");
		String errMsg = "Error catched by ActionExceptionProcessor";
		getActionResult().setErrorMessage(errMsg);
	}
	
	@Override
	public void invokeMethod() throws Exception {
		String errMsg = "Error catched by ActionExceptionProcessor";
		getActionResult().setErrorMessage(errMsg);
		
		Object obj = findException();
		if (obj instanceof Exception) {
			Exception ex = (Exception) obj;
			logger.error("ActionExceptionProcessor.findException", ex);
		}
		
	}

	
	/**
	 * Finds exception object on the value stack
	 * 
	 * @return the exception object on the value stack
	 */
	public static Object findException() {        
	    ActionContext ac = ActionContext.getContext();
	    ValueStack vs = ac.getValueStack();
	    Object exception = vs.findValue("exception");       

	    return exception;
	}
	/**
	 * @return the exception
	 */
	public Exception getException() {
		if (exception == null) {
			exception = (Exception) findException();
		}
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	
	
}