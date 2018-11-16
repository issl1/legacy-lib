package org.seuksa.frmk.tools.exception;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18NAware;

/**
 *  Translatable error message.
 *  
 * @author prasnar
 * @version $Revision$
 */
public class ErrorHandler implements Serializable, I18NAware {
 	/** */
	private static final long serialVersionUID = 1078157572934692013L;
	
	public final static String ERROR_HANDLER_KEY = "errorHandler";

	public static final ErrorHandler ERROR_NONE = new ErrorHandler(ErrorCode.ERR_NONE);
	public static final ErrorHandler ERR_DEFAULT = new ErrorHandler(ErrorCode.ERR_SYS);
	
	private ErrorCode code;
    private String key;
    private Object[] params;
    private String technicalMessage;
    private String functionalMessage;
    private Exception exceptionSource;

    /**
     * 
     * @param key
     */
    public ErrorHandler() {
    }
    
    /**
     * 
     * @param key
     */
    public ErrorHandler(ErrorCode code) {
    	this.code = code;
    }

    /**
     * 
     * @param key
     */
    public ErrorHandler(ErrorCode code, String functionalMessage) {
    	this.code = code;
    	this.functionalMessage = functionalMessage;
    }

    
    /**
     * 
     * @param key
     */
    public ErrorHandler(String key) {
        this.key = key;
    }

    /**
     * 
     * @param code
     * @param key
     */
    public ErrorHandler(String key, Object... params) {
        this(key);
        this.params = params;
    }

    @Override
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	


	/**
	 * @return the code
	 */
	public ErrorCode getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(ErrorCode code) {
		this.code = code;
	}

	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}

	/**
	 * @return the technicalMessage
	 */
	public String getTechnicalMessage() {
		return technicalMessage;
	}

	/**
	 * @param technicalMessage the technicalMessage to set
	 */
	public void setTechnicalMessage(String technicalMessage) {
		this.technicalMessage = technicalMessage;
	}


	/**
	 * @return the functionalMessage
	 */
	public String getFunctionalMessage() {
		return functionalMessage;
	}


	/**
	 * @param functionalMessage the functionalMessage to set
	 */
	public void setFunctionalMessage(String functionalMessage) {
		this.functionalMessage = functionalMessage;
	}
	
	/**
	 * 
	 * @return
	 */
	private String parametersToString() {
		String str = "";
		if (params != null) {
			for (Object param : params) {
				if (param != null) {
					str += param.toString() + " | ";
				}
			}
		}
		return str;
	}
	
	

	/**
	 * @return the exceptionSource
	 */
	public Exception getExceptionSource() {
		return exceptionSource;
	}


	/**
	 * @param exceptionSource the exceptionSource to set
	 */
	public void setExceptionSource(Exception exceptionSource) {
		this.exceptionSource = exceptionSource;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ErrorHandler)) {
			return false;
		}
		ErrorHandler eh = (ErrorHandler) o;
		if (StringUtils.isNotEmpty(key)) {
			return key.equals(eh.getKey()) && code.equals(eh.getCode());
		}
		return code.equals(eh.getCode());
	}

	@Override
	public String toString() {
		return "Code: " + (getCode() != null ? getCode() : "N/A")
			 + "Key: " + (getKey() != null ? getKey() : "N/A")
			 + "Params length: " + (getParams() != null ? getParams().length : "N/A")
			 + "Params: " + (parametersToString() != null ? parametersToString() : "N/A")
			 + "TechnicalMessage: " + (getTechnicalMessage() != null ? getTechnicalMessage() : "N/A")
			 + "FunctionalMessage: " + (getFunctionalMessage() != null ? getFunctionalMessage() : "N/A")
			 + "ExceptionSource: " + (getExceptionSource() != null ? getExceptionSource() : "N/A")
			 + "ExceptionSource message " + (getExceptionSource() != null ? getExceptionSource().getMessage() : "N/A");

	}
}
