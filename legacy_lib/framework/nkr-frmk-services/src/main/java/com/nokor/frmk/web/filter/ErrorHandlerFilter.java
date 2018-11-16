package com.nokor.frmk.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seuksa.frmk.tools.exception.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author prasnar
 * 
 */
public class ErrorHandlerFilter implements Filter {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			ErrorHandler errorHandler = null;
			if (request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY) == null) {
				String errMsg = "**ERROR**: request.getAttribute[" + ErrorHandler.ERROR_HANDLER_KEY + "] returns null."; 
				logger.error(errMsg);

				logger.error("[" + this.getClass().getName() + "] generates the [" + ErrorHandler.class.getSimpleName() + "]");
				errMsg = "**ERROR**: A system error has occured. Please contact your administrator.";
				errorHandler = new ErrorHandler();
				errorHandler.setKey(this.getClass().getName());
				errorHandler.setTechnicalMessage(errMsg);
				request.setAttribute(ErrorHandler.ERROR_HANDLER_KEY, errorHandler);
			}
			if (!(request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY) instanceof ErrorHandler)) {
				String errMsg = "**ERROR**: request.getAttribute[" + ErrorHandler.ERROR_HANDLER_KEY + "] returns an object of the class [" 
						+ request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY).getClass().getName() 
						+ "] with the value [" + request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY).toString() + "]"
						+ "\r\n"
						+ "**ERROR**: It should return an object of the class [" + ErrorHandler.class.getName() + "]";
				logger.error(errMsg);
				
				logger.error("[" + this.getClass().getName() + "] generates the [" + ErrorHandler.class.getSimpleName() + "]");
				errMsg = "**ERROR**: A system error has occured. Please contact your administrator. "
						+ request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY).toString();
				errorHandler = new ErrorHandler();
				errorHandler.setKey(this.getClass().getName());
				errorHandler.setTechnicalMessage(errMsg);
				request.setAttribute(ErrorHandler.ERROR_HANDLER_KEY, errorHandler);
			}
			errorHandler = (ErrorHandler) request.getAttribute(ErrorHandler.ERROR_HANDLER_KEY);
			if (errorHandler.getExceptionSource() == null) {
				errorHandler.setExceptionSource(ex);
			}
			logger.error("ErrorHanlder: " + (errorHandler != null ? errorHandler.toString() : "N/A"));
			logger.error(ex.getMessage());
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		errorHandler = (ErrorHandler) WebApplicationContextUtils
//				.getRequiredWebApplicationContext(
//						filterConfig.getServletContext()).getBean(
//						"defaultErrorHandler");
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

}