package com.nokor.efinance.third.creditbureau.cbc;

import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import com.nokor.efinance.third.creditbureau.cbc.model.request.Request;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Response;

/**
 * @author ly.youhort
 */
public class XmlBinder {
	
	private static XmlBinder INSTANCE = new XmlBinder();
	    
	private XmlBinder() {
	}
	
	/**
	 * @param request
	 * @return
	 * @throws JiBXException
	 */
	public static String marshal(Request request) throws JiBXException {
	    StringWriter sw = new StringWriter();
	    INSTANCE.getMarshallingContext().marshalDocument(request, "UTF-8", null, sw);
	    return sw.toString();
	}
	
	/**
	 * @param xml
	 * @return
	 * @throws JiBXException
	 */
	public static Response unmarshal(String xml) throws JiBXException {
	    return (Response) INSTANCE.getUnmarshallingContext().unmarshalDocument(new StringReader(xml));
	}
	
	/**
	 * @return
	 * @throws JiBXException
	 */
	private synchronized IMarshallingContext getMarshallingContext() throws JiBXException {
	    IBindingFactory bindingFactory = BindingDirectory.getFactory(Request.class);
	    IMarshallingContext marshallingContext = bindingFactory.createMarshallingContext();
	    marshallingContext.setIndent(2);
	    return marshallingContext;
	}
	
	/**
	 * @return
	 * @throws JiBXException
	 */
	private synchronized IUnmarshallingContext getUnmarshallingContext() throws JiBXException {
	    IBindingFactory bindingFactory = BindingDirectory.getFactory(Response.class);
	    IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
	    return unmarshallingContext;
	}
}
