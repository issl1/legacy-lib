package org.seuksa.frmk.tools.http.output;

import java.io.Serializable;

import net.sf.json.JSONObject;

import org.seuksa.frmk.tools.XMLUtils;


/**
 *  
 * @author peng.boren
 *
 */
public abstract class JsonOutputMessage implements Serializable {

	private static final long serialVersionUID = 8858807494135172773L;
	
	protected static String DATA = "DATA";
	protected static String ERROR = "ERROR";
	
	private String type;

	/**
	 * @return message type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type string to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Serialize to json string.
	 * @return
	 */
	public String toJSON() {
		JSONObject jsonObject = JSONObject.fromObject(this);
		return jsonObject.toString();
	}
	
	/**
	 * Serialize to xml string.
	 * @param alias
	 * @return
	 */
	public String toXML() {
		return XMLUtils.toXML(this, getClass());
	}
	
}
