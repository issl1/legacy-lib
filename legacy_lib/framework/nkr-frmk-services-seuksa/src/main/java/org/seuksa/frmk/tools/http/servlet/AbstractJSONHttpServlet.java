package org.seuksa.frmk.tools.http.servlet;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.seuksa.frmk.tools.exception.ErrorCode;
import org.seuksa.frmk.tools.exception.FunctionalException;
import org.seuksa.frmk.tools.exception.ParameterException;
import org.seuksa.frmk.tools.exception.TechnicalException;
import org.seuksa.frmk.tools.http.output.ErrorJsonOupoutMessage;
import org.slf4j.LoggerFactory;

/**
 * @see org.seuksa.frmk.tools.http.servlet.AbstractBaseHttpServlet
 * @author peng.boren
 */
public abstract class AbstractJSONHttpServlet extends AbstractBaseHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Receive the request string from client side.
	 * Checking for incorrect parameter name
	 * Invoke web method to execute.
	 */
	protected String processRequest(String requestString) {
		JSONObject json;
		try {
			json = JSONObject.fromObject(requestString);
			checkMandatoryParameter(json, getParamaterNames());
		} 
		catch (ParameterException incorrectParameterEx) {
			ErrorJsonOupoutMessage errorOutMessage = new ErrorJsonOupoutMessage();
			errorOutMessage.setMessage(incorrectParameterEx.getErrorMessage());
			errorOutMessage.setCode(incorrectParameterEx.getErrorCode());
			return errorOutMessage.toJSON();
		}
		catch (Exception exception) {
			ErrorJsonOupoutMessage errorOutMessage = new ErrorJsonOupoutMessage();
			errorOutMessage.setMessage("The parameter is mandatory.");
			errorOutMessage.setCode(ErrorCode.ERR_INCORRECT_PARAMETERS.getId().intValue());
			
			LoggerFactory.getLogger(getClass()).error(exception.getMessage(), exception);
			
			return errorOutMessage.toJSON();
		}
		
		try {
			return invokeWebMethod(json);
		} catch (TechnicalException e) {
			LoggerFactory.getLogger(getClass()).error(e.getMessage(),e);
			ErrorJsonOupoutMessage errorOutMessage = new ErrorJsonOupoutMessage();
			errorOutMessage.setMessage("A system error has occured. Please contact your administrator.");
			errorOutMessage.setCode((int) e.getCode());
			return errorOutMessage.toJSON();
		} catch (FunctionalException e) {
			ErrorJsonOupoutMessage errorOutMessage = new ErrorJsonOupoutMessage();
			errorOutMessage.setMessage(e.getMessage());
			errorOutMessage.setCode((int) e.getCode());
			return errorOutMessage.toJSON();
		}
	}
	
	/**
	 * Check the parameter names. 
	 * @param jsonObject
	 * @param paramNames
	 * @throws ParameterException
	 */
	private void checkMandatoryParameter(JSONObject jsonObject, String...paramNames) throws ParameterException {
		if (paramNames != null) {
			for (String paramName : paramNames) {
				try {
					jsonObject.getString(paramName);
				}
				catch(JSONException jsonEx) {
					String errorMsg = "[Error code "
						+ ErrorCode.ERR_INCORRECT_PARAMETERS
						+ "] - The parameter " + paramName + " is mandatory.";
					LoggerFactory.getLogger(getClass()).error(errorMsg, jsonEx);
							
					ParameterException ex = new ParameterException();
					ex.setErrorCode(ErrorCode.ERR_INCORRECT_PARAMETERS.getId().intValue());
					ex.setErrorMessage(errorMsg);
					throw ex;
				}
			}
		}
	}
	
	/**
	 * Invoke web method.
	 * @param json
	 * @return
	 * @throws TechnicalException
	 * @throws FunctionalException
	 */
	protected abstract String invokeWebMethod(JSONObject json) throws TechnicalException, FunctionalException; 
	
}
