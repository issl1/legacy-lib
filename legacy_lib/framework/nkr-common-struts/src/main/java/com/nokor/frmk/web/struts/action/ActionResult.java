package com.nokor.frmk.web.struts.action;

import java.io.Serializable;

import org.seuksa.frmk.tools.exception.ErrorHandler;

import com.opensymphony.xwork2.Action;

/**
 * @author prasnar
 * @version $Revision$
 */
public class ActionResult implements Serializable {

    /** */
    private static final long serialVersionUID = 8356523129512137294L;

    public final static String BREAKLINE = "@BR@";
    public final static String RESULT_SUCCESS= Action.SUCCESS;
    public final static String RESULT_ERROR= Action.ERROR;
    public final static String RESULT_SYS_ERROR = "syserror";
    public final static String RESULT_REDIRECT = "redirect";
    public final static String RESULT_NOT_AUTHORIZED_PAGE = "errorNotAuthorizedPage";
    public final static String RESULT_REQUIRED_SYSADMIN= "requiredSysAdmin";
    public final static String RESULT_REQUIRED_SUPERADMIN= "requiredSuperAdmin";
    public final static String RESULT_NO_ACCESS_RIGHT= "noAccessRight";
    
    
    private ErrorHandler errorHandler;
    private String result;
    private String infoMessage;
    private String errorMessage;
    private String warningMessage;
    private String successMessage;
    
    private String urlRedirect;

    /**
     * 
     */
    public ActionResult() {
    	errorHandler = new ErrorHandler();
        result = RESULT_SUCCESS;
    }

    /**
     * @return the errorHandler
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * @param errorHandler the errorView to set
     */
    public void setErrorHanlder(final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
    
    /**
     * 
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }
    
    /**
     * 
     */
    public void setResultSysError() {
        this.result = RESULT_SYS_ERROR;
    }

    /**
     * 
     */
    public void setResultNotAuthorized() {
        this.result = RESULT_NOT_AUTHORIZED_PAGE;
    }
    
    /**
     * 
     */
    public void setResultRequiredSysAdmin() {
        this.result = RESULT_REQUIRED_SYSADMIN;
    }

    /**
     * 
     */
    public void setResultRequiredSuperAdmin() {
        this.result = RESULT_REQUIRED_SUPERADMIN;
    }

    /**
     * 
     */
    public void setResultNoAccessRight() {
        this.result = RESULT_NO_ACCESS_RIGHT;
    }

    /**
     * 
     */
    public void setResultError() {
        this.result = RESULT_ERROR;
    }

    /**
     * 
     */
    public void setResultSuccess() {
        this.result = RESULT_SYS_ERROR;
    }

    /**
     * 
     */
    public void setResultRedirect(String urlRedirect) {
        this.result = RESULT_REDIRECT;
        this.urlRedirect = urlRedirect;
    }

    /**
     * @return the infoMessage
     */
    public String getInfoMessage() {
        return infoMessage;
    }

    /**
     * @param infoMessage the infoMessage to set
     */
    public void setInfoMessage(final String infoMessage) {
        this.infoMessage = infoMessage;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        setResultError();
    }
    
    public void setErrorSysMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        setResultSysError();
    }

    /**
     * @return the warningMessage
     */
    public String getWarningMessage() {
        return warningMessage;
    }

    /**
     * @param warningMessage the warningMessage to set
     */
    public void setWarningMessage(final String warningMessage) {
        this.warningMessage = warningMessage;
    }

    /**
     * 
     * @return the successMessage
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * 
     * @param successMessage the successMessage to set
     */
    public void setSuccessMessage(final String successMessage) {
        this.successMessage = successMessage;
    }

	/**
	 * @return the urlRedirect
	 */
	public String getUrlRedirect() {
		return urlRedirect;
	}

	/**
	 * @param urlRedirect the urlRedirect to set
	 */
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

}
