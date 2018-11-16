/**
 * 
 */
package org.seuksa.frmk.tools.http;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static MyHttpResponse httpConnect(String url) throws HttpException, IOException {
		return httpConnect(url, null, null, null);
	}
	/**
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static MyHttpResponse httpConnect(String url, String content) throws HttpException, IOException {
		return httpConnect(url, content, null, null);
	}

	/**
	 * 
	 * @param url
	 * @param requestData
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static MyHttpResponse httpConnect(
										String url, 
										String content, 
										String contentType, 
										String charset)
			throws HttpException, IOException {
		logger.debug("httpConnect - start"
				+ "\nURL : " + url
				+ "\ncontent : " + content
				+ "\ncontentType : " + contentType
				+ "\ncharset : " + charset
				);
		
		PostMethod post = null;
		
		try {
			post = new PostMethod(url);
	
			if (content == null) {
				content = "";
			}
			// ISO-8859-1 
			// UTF-8
//			RequestEntity entity = new StringRequestEntity(content, "text/xml", "ISO-8859-1");
			RequestEntity entity = new StringRequestEntity(content, contentType, charset);
			post.setRequestEntity(entity);

			HttpClient httpclient = new HttpClient();

			logger.debug("Execute method POST ..");
			httpclient.executeMethod(post);

			// Display status code
			logger.debug("Response status code: " + post.getStatusCode());
			logger.debug("Response charset: " + post.getResponseCharSet());
			
			
			MyHttpResponse response = new MyHttpResponse(post);

			return response;
		} finally {
			// Release current connection to the connection pool once done
			if (post != null) {
				post.releaseConnection();
			}
			logger.debug("httpConnect - end");
		}

	}
	
	/**
     * 
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
    	try {
    		String ip = request.getHeader("x-forwarded-for");
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
    		   ip = request.getHeader("Proxy-Client-IP");      
    		}
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
    		   ip = request.getHeader("Proxy-Client-IP");      
    		}      
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
    		   ip = request.getHeader("WL-Proxy-Client-IP");      
    		} 
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
    		   ip = request.getHeader("HTTP_X_FORWARDED_FOR");      
    		}
    		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
    		   ip = request.getRemoteAddr();      
    		}   
    		return ip;
    	}
    	catch (Exception e) {
    		logger.error("Error while getting IP Address.", e);
    		return null;
   		}
	}
}
