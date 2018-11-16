package org.seuksa.frmk.tools.http.servlet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seuksa.frmk.tools.StreamUtils;
import org.slf4j.LoggerFactory;


/**
 * Provides an abstract class to be subclassed to create
 * an HTTP servlet suitable for a Web site. A subclass of
 * <code>HttpServlet</code> must override at least 
 * one methods:
 *
 * <ul>
 * <li> <code>getParameterClass</code>, ParameterClass used to serialize request (JSON or XML) string
 * <li> <code>getRequestParameterValue</code>, deserialize request (JSON or XML) string into ParameterClass object.
 * <li> <code>processRequest</code>, send request parameter value to specific process.
 *  
 * @author peng.boren
 *
 */
public abstract class AbstractBaseHttpServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3715711685265197245L;
			
	/**
	 * Loads WebApplicationContext object.
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}
	

	/**
	 * @see javax.servlet.htTp.HtTpServlet#service(javax.servlet.htTp.HtTpServletRequest,
	 *      javax.servlet.htTp.HtTpServletResponse) Main entry point of the
	 *      Servlet
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		LoggerFactory.getLogger(getClass()).debug("service - start");
		
		InputStream stream = req.getInputStream();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutput out = new DataOutputStream(byteOut);
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		onClientRequest(stream, out);
		
		flush(out);
		byte[] buf = byteOut.toByteArray();
		resp.setContentLength(buf.length);
		LoggerFactory.getLogger(getClass()).debug("response buffer=" + (new String(buf)));
		ServletOutputStream servletOut = resp.getOutputStream();
		servletOut.write(buf);
		servletOut.close();
		LoggerFactory.getLogger(getClass()).debug("service -end");
	}
	/**
	 * Read request parameter then send request parameter object to business process.
	 * @param requestStream
	 * @param responseStream
	 * @throws IOException 
	 * @throws Exception
	 */
	protected void onClientRequest(InputStream requestStream, DataOutput responseStream) throws IOException {
		String requestString = StreamUtils.streamToString(requestStream);
		String response = processRequest(requestString);
		responseStream.write(response.getBytes());
	}
	
	protected abstract String processRequest(String requestString);
	
	protected abstract String[] getParamaterNames();

	public void flush(DataOutput responseStream) throws IOException {
		((DataOutputStream) responseStream).flush();
	}

}
