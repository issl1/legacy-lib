package com.nokor.frmk.vaadin.ui.widget.table;

import java.io.Serializable;

import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Column definition of table widget
 * @author ly.youhort
 */
public class ColumnDefinition implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String propertyId;
	private String propertyCaption;
	private Align propertyAlignment;
	private int propertyWidth;
	private Class<?> propertyType;
	private Object defaultValue;
	private boolean readOnly;
	private boolean sortable;
	private boolean visible;
	private ColumnRenderer columnRenderer;
	private ColumnGenerator columnGenerator;
	
	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Align propertyAlignment, int propertyWidth) {
		this(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth);
	}

	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
     * @param visible visible
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Align propertyAlignment, int propertyWidth, boolean visible) {
		this(propertyId, propertyCaption, propertyType, propertyAlignment, propertyWidth);
		setVisible(visible);
	}
	
	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
     * @param columnGenerator Column generator
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Align propertyAlignment, 
			int propertyWidth, ColumnGenerator columnGenerator) {
		this(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth, null, columnGenerator);
	}
	
	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
     * @param columnRenderer Column renderer
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Align propertyAlignment, 
			int propertyWidth, ColumnRenderer columnRenderer) {
		this(propertyId, propertyCaption, propertyType, null, true, true, propertyAlignment, propertyWidth, columnRenderer, null);
	}
	
	/**
	 * 
	 * @param propertyId
	 * @param propertyType
	 */
	public ColumnDefinition(String propertyId, Class<?> propertyType) {
		this.propertyId = propertyId;
		this.propertyCaption = propertyId;
		this.propertyType = propertyType;
		this.visible = true;
	}
	
	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param defaultValue Default value of the property.
     * @param readOnly True if property is read only.
     * @param sortable True if property is sortable.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Object defaultValue,
			boolean readOnly, boolean sortable, Align propertyAlignment, int propertyWidth) {
		this(propertyId, propertyCaption, propertyType, defaultValue, readOnly, sortable, propertyAlignment, propertyWidth, null, null);
	}

	/**
	 * @param propertyId ID of the property.
	 * @param caption caption of the property.
     * @param type Type of the property.
     * @param defaultValue Default value of the property.
     * @param readOnly True if property is read only.
     * @param sortable True if property is sortable.
     * @param propertyAlignment align of the property
     * @param propertyWidth width of the property.
     * @param columnRenderer Column renderer
	 */
	public ColumnDefinition(String propertyId, String propertyCaption, Class<?> propertyType, Object defaultValue,
			boolean readOnly, boolean sortable, Align propertyAlignment, int propertyWidth, ColumnRenderer columnRenderer, ColumnGenerator columnGenerator) {
		this.propertyId = propertyId;
		this.propertyCaption = propertyCaption;
		this.propertyType = propertyType;
		this.defaultValue = defaultValue;
		this.readOnly = readOnly;
		this.sortable = sortable;
		this.propertyAlignment = propertyAlignment;
		this.propertyWidth = propertyWidth;
		this.columnRenderer = columnRenderer;
		this.columnGenerator = columnGenerator;
		this.visible = true;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}


	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}


	/**
	 * @return the propertyCaption
	 */
	public String getPropertyCaption() {
		return propertyCaption;
	}


	/**
	 * @param propertyCaption the propertyCaption to set
	 */
	public void setPropertyCaption(String propertyCaption) {
		this.propertyCaption = propertyCaption;
	}


	/**
	 * @return the propertyAlignment
	 */
	public Align getPropertyAlignment() {
		return propertyAlignment;
	}


	/**
	 * @param propertyAlignment the propertyAlignment to set
	 */
	public void setPropertyAlignment(Align propertyAlignment) {
		this.propertyAlignment = propertyAlignment;
	}


	/**
	 * @return the propertyWidth
	 */
	public int getPropertyWidth() {
		return propertyWidth;
	}


	/**
	 * @param propertyWidth the propertyWidth to set
	 */
	public void setPropertyWidth(int propertyWidth) {
		this.propertyWidth = propertyWidth;
	}


	/**
	 * @return the propertyType
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}


	/**
	 * @param propertyType the propertyType to set
	 */
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}


	/**
	 * @return the defaultValue
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}


	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}


	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}


	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


	/**
	 * @return the sortable
	 */
	public boolean isSortable() {
		return sortable;
	}


	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the columnRenderer
	 */
	public ColumnRenderer getColumnRenderer() {
		return columnRenderer;
	}

	/**
	 * @param columnRenderer the columnRenderer to set
	 */
	public void setColumnRenderer(ColumnRenderer columnRenderer) {
		this.columnRenderer = columnRenderer;
	}

	/**
	 * @return the columnGenerator
	 */
	public ColumnGenerator getColumnGenerator() {
		return columnGenerator;
	}

	/**
	 * @param columnGenerator the columnGenerator to set
	 */
	public void setColumnGenerator(ColumnGenerator columnGenerator) {
		this.columnGenerator = columnGenerator;
	}
}
