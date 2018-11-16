package org.seuksa.frmk.dao.criteria;


/**
 * 
 * @author prasnar
 *
 */
public class FieldFilter implements FilterContent {
	private String fieldName;
	private Object[] fieldValues;
	
	private FilterMode filterMode;

	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param filterMode
	 */
	public FieldFilter(String fieldName, FilterMode filterMode, Object...fieldValues) {
		this.fieldName = fieldName;
		this.filterMode = filterMode;
		this.fieldValues = fieldValues;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldValue
	 */
	@Override
	public Object[] getFieldValues() {
		return fieldValues;
	}
	
	/**
	 * 
	 */
	public Object getFieldValue() {
		return fieldValues[0];
	}
	
	/**
	 * 
	 */
	public Object getField2Value() {
		return fieldValues[1];
	}

	/**
	 * @param fieldValue the fieldValues to set
	 */
	public void setFieldValue(Object...fieldValues) {
		if (!isSupportedField(fieldValues)) {
			throw new IllegalArgumentException("Only the following types are supported: Date, String, Number.");
		}
		this.fieldValues = fieldValues;
	}
	
	

	/**
	 * @return the filterMode
	 */
	public FilterMode getFilterMode() {
		return filterMode;
	}

	/**
	 * @param filterMode the filterMode to set
	 */
	public void setFilterMode(FilterMode filterMode) {
		this.filterMode = filterMode;
	}
	
	/**
	 * 
	 * @param fieldValue
	 * @return
	 */
	private boolean isSupportedField(Object fieldValue) {
//		return fieldValue instanceof String
//				|| fieldValue instanceof Date
//				|| Number.class.isAssignableFrom(fieldValue.getClass());
		return true;
	}
}
