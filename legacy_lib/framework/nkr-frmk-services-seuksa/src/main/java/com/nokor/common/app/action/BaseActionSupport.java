package com.nokor.common.app.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseActionSupport implements ActionSupport {
    /** */
	private static final long serialVersionUID = -2639593927405670520L;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String execute(Object[] params) throws Exception {
        logger.info("execute - start");

        try {
//            logger.info("Service called by : IP [" + request.getRemoteAddr() + "]");
//            logger.info("Service called by : Host [" + request.getRemoteHost() + "]");
//            logger.info("Service called by : Port [" + request.getRemotePort() + "]");
//            logger.info("Service called by : User [" + request.getRemoteUser() + "]");

            invokeMethod(params);

        } catch (Exception e) {
            // Any exceptions thrown by invoking the server
            // method should be sent back to the client. 
            // Make sure we are working with a 'pure' output stream
            // that does not contain any other data	
            String errMsg = "The action has failed. A technical error has occured.";
            logger.error(errMsg, e);
            throw new IllegalStateException(errMsg, e);
            
        } finally {
            logger.info("execute - result: ");
        }

        return "OK";
    }

    /**
     * 
     */
	public abstract void invokeMethod(Object[] params);
    

}
