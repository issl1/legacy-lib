package org.seuksa.frmk.tools.http.output;

/**
 * 
 * @author peng.boren
 *
 */
public class DataJsonOutputMessage extends JsonOutputMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3489332162131002029L;
	
	private Object data;
	
	public DataJsonOutputMessage() {
		setType(DataJsonOutputMessage.DATA);
	}

	/**
	 * @return data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data 
	 * 				the data to set.
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
