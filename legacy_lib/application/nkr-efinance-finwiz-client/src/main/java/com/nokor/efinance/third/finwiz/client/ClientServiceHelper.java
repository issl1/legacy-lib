package com.nokor.efinance.third.finwiz.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.third.ThirdAppConfigFileHelper;

/**
 * 
 * @author prasnar
 *
 */
public class ClientServiceHelper {
    private final static Logger logger = LoggerFactory.getLogger(ClientServiceHelper.class);
    
	private static final String SERVER_URL = ThirdAppConfigFileHelper.getFinwizServerURL();

	/**
	 * 
	 * @param inXml
	 * @param className
	 * @param endPoint
	 * @return
	 */
	/*public static FinWizResultMessage postMessage(FinWizXML inXml,String endPoint) {
		FinWizResultMessage resultMessage = null;
		if (AppConfigFileHelper.isThirdMockingMode()) {
			if (inXml instanceof UserLoginM) {
				List resultListObj = Arrays.asList(inXml);
				resultMessage = new FinWizResultMessage();
				resultMessage.setResultListObj(resultListObj);
			} else {
				throw new IllegalStateException("Mock [" + inXml.getClass().getCanonicalName() + "] not managed yet.");
			}
		} else {
			resultMessage = postMessage(inXml, endPoint, true);
		}
		return resultMessage;
	}
	*/
	/**
	 * 
	 * @param inputXml
	 * @param endPoint
	 * @param isReturn
	 * @return
	 */
	/*public static FinWizResultMessage postMessage(FinWizXML inputXml, String endPoint, boolean isReturn) {
		logger.info("Call service - endPoint[" + endPoint + "]");
		return HttpRequestUtils.postRequestJson(SERVER_URL, inputXml, FinWizResultMessage.class, endPoint, isReturn);
	}*/
}
