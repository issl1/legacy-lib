package com.nokor.frmk.vaadin.ui.widget.table;


/**
 * 
 * @author youhort.ly
 *
 */
public abstract class PropertyColumnRenderer implements ColumnRenderer {
	/** */
	private static final long serialVersionUID = -5582802922475360052L;
	private Object propertyValue;
	
	/**
	 * 
	 */
	public PropertyColumnRenderer() {
	}
	
	/**
	 * @return the propertyValue
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}
}
