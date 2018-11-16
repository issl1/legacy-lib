package org.seuksa.frmk.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.LoggerFactory;

/**
 * @author Prasnar
 * @version $Revision$
 */
public class MyHttpResponse {
	/** */
	private int statusCode;
	/** */
	private String responseCharset;
	/** */
	private byte[] body;
	
	/**
	 * Constructor
	 * @throws IOException 
	 */
	public MyHttpResponse(PostMethod post) throws IOException {
		statusCode = post.getStatusCode();
		responseCharset = post.getResponseCharSet();
		//InputStream bodyAsStream = post.getResponseBodyAsStream();
//		body = readFully(new InputStreamReader(bodyAsStream, getResponseCharSet()));
		body = post.getResponseBody(); //AsString().trim().getBytes(responseCharset);
		//LoggerFactory.getLogger(this).debug("Body Response:" + body);
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFully(Reader input) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = input instanceof BufferedReader ? (BufferedReader) input
					: new BufferedReader(input);
			StringBuffer result = new StringBuffer();
			char[] buffer = new char[4 * 1024];
			int charsRead;
			while ((charsRead = bufferedReader.read(buffer)) != -1) {
				result.append(buffer, 0, charsRead);
			}
			return result.toString().getBytes();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public long getResponseContentLength() {
		return body != null? body.length : 0;
	}

	/**
	 * 
	 * @return
	 */
	public String getResponseCharSet() {
		return responseCharset;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] getResponseBody() throws IOException {
		return body;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getResponseBodyAsString(String charset) throws IOException {
		LoggerFactory.getLogger(this.getClass()).debug(new String(body, charset));
		return new String(body, charset);
	}
		
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getResponseBodyAsString() throws IOException {
		
		return new String(body, responseCharset);
//		return postResp.getResponseBodyAsString();
	}
	
}
