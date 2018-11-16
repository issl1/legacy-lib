package com.nokor.frmk.messaging.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

/**
 * 
 * @author 
 *
 */
public class HttpRequestUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);
	private static final String CR = "\r\n"; 

	private final static int XML_FORMAT = 1;
	private final static int JSON_FORMAT = 2;
	
	/**
	 * 
	 * @param serverUrl
	 * @param inJson
	 * @param outJsonClass
	 * @param endPoint
	 * @param isReturn
	 * @return
	 */
	public static <OutJson extends Object> OutJson postRequestJson(String serverUrl, Object inJson, Class<OutJson> outJsonClass, String endPoint, boolean isReturn) {
		return postRequestGeneric(serverUrl, JSON_FORMAT, inJson, outJsonClass, endPoint, isReturn);
	}
	/**
	 * 
	 * @param serverUrl
	 * @param inXml
	 * @param outXmlClass
	 * @param endPoint
	 * @param isReturn
	 * @return
	 */
	public static <OutXml extends Object> OutXml postRequestXml(String serverUrl, Object inXml, Class<OutXml> outXmlClass, String endPoint, boolean isReturn) {
		return postRequestGeneric(serverUrl, XML_FORMAT, inXml, outXmlClass, endPoint, isReturn);
	}
	
	/**
	 * 
	 * @param serverUrl
	 * @param format
	 * @param inObj
	 * @param outClass
	 * @param endPoint
	 * @param isReturn
	 * @return
	 */
	public static <OutFormat extends Object> OutFormat postRequestGeneric(String serverUrl, int format, Object inObj, Class<OutFormat> outClass, String endPoint, boolean isReturn) {
		String connectionUrl = serverUrl + endPoint;
		
		logger.debug("Post request - serverUrl/endPoint [" + connectionUrl + "]");

		// format the  input
		XStream xstream = null;
		if (format == JSON_FORMAT) {
			xstream = new XStream(new JettisonMappedXmlDriver());
		} 
		// default is XML
		else {
			xstream = new XStream(new Dom4JDriver());
		}
		xstream.processAnnotations(inObj.getClass());
		String xmlStr = xstream.toXML(inObj);
		
		logger.debug("Post request - xmlStr [" + CR + xmlStr + CR + "]");

		// Build the HttpPost
		HttpPost httpPost = new HttpPost(connectionUrl);
		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(xmlStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Encoding exception", e);
		}
		httpPost.setEntity(entity);
		
		// Execute (send) the HttpPost (request)
		// The result is stored in HttpResponse
		CloseableHttpClient  httpClient = HttpClientBuilder.create().build();
		HttpResponse response = null;
		HttpEntity resEntity = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			throw new IllegalStateException("Error while posting the request", e);
		}
		resEntity = response.getEntity();
		logger.debug("Response - Entity returned - [" + (resEntity != null ? resEntity.getClass().getCanonicalName() : "<null>") + "]");
		
		
		OutFormat ouputMsg = null;
		InputStream in = null;
		if (isReturn) {
			if (resEntity != null) {

				try {
					in = resEntity.getContent();
					xstream.processAnnotations(outClass);
					ouputMsg = (OutFormat) xstream.fromXML(in);
					logger.debug("Response - Outpout content - [" + CR + (ouputMsg != null ? ouputMsg.toString() : "<no ouput>") + CR + "]");
				} catch (Exception e) {
					String errMsg = "Error while getting the object from XML";
					logger.error(errMsg, e);
					throw new IllegalStateException(errMsg, e);
				} finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException e) {
						logger.warn("Error while closing HttpResponse content stream");
					}
				}
			}
		}
		try {
			httpClient.close();
		} catch (IOException e) {
			logger.warn("Error while closing httpClient");
		}
		return ouputMsg;
	}

	
}
