package org.seuksa.frmk.tools;

import com.thoughtworks.xstream.XStream;

public class XMLUtils {

	/**
	 * Deserialize from xml to object.
	 * @param xml
	 * @return
	 */
//	public static Object fromXML(String xml, Class<?> clazz) {
//		return null;
//	}
	
	/**
	 * Serialize object to xml string.
	 * @param alias
	 * @return
	 */
	public static String toXML(Object object, Class<?> clazz) {
		XStream xstream = new XStream();
		xstream.alias(clazz.getSimpleName().toLowerCase() , clazz);
		return xstream.toXML(object);
	}
	
}
