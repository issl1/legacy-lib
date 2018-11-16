package com.nokor.frmk.web.struts.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.web.struts.action.AbstractActionSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author prasnar
 * 
 */
public class LoggingInterceptor implements Interceptor {
	/** */
	private static final long serialVersionUID = -6656803213884283724L;
    private static final Logger logger = LoggerFactory.getLogger(AbstractActionSupport.class);

	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String className = invocation.getAction().getClass().getName();
        long startTime = System.currentTimeMillis();
		logger.info("LoggingInterceptor.intercept - Before calling action [" + className + "]");
        
		String result = "success";
		try {
			result = invocation.invoke();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		
		Object objException = ActionContext.getContext().getValueStack().findValue("exception");
		if (objException instanceof RuntimeException) {
			RuntimeException ex = (RuntimeException) objException;
			logger.error("LoggingInterceptor.intercept - RuntimeException", ex);
			throw ex;
		} else if (objException instanceof Exception) {
			Exception ex = (Exception) objException;
			logger.error("LoggingInterceptor.intercept - Exception", ex);
			throw ex;
		} else if (objException != null) {
			logger.error("LoggingInterceptor.intercept - Exception [" + objException.getClass().getName() + "]");
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("After calling action: " + className + " Time taken: " + (endTime - startTime) + " ms");
		logger.info("LoggingInterceptor.intercept - rethrower");
		
		return result;
	}
}
