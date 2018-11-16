package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.model.eref.BaseERefData;
import org.seuksa.frmk.model.eref.SimpleERefData;

/**
 * 
 * @author prasnar
 *
 */
public class ERefDataOptionGroup<T extends SimpleERefData> extends SimpleOptionGroup {
	/** */
	private static final long serialVersionUID = 4697257054453494202L;

	/**
	 * 
	 */
	public ERefDataOptionGroup(Class<T> clazz) {
		this(clazz, (List<T>) null);
	}

	/**
	 * 
	 * @param clazz
	 * @param defaultValues
	 */
	public ERefDataOptionGroup(Class<T> clazz, T defaultSelectedValue) {
		this(clazz, null, Arrays.asList(defaultSelectedValue));
	}

	/**
	 * 
	 * @param clazz
	 * @param defaultSelectedValues
	 */
	public ERefDataOptionGroup(Class<T> clazz, List<T> defaultSelectedValues) {
		this(clazz, null, defaultSelectedValues);
	}

	/**
	 * 
	 * @param clazz
	 * @param values
	 * @param defaultSelectedValues
	 */
	public ERefDataOptionGroup(Class<T> clazz, List<T> values, List<T> defaultSelectedValues) {
		if (values == null || values.size() == 0) {
			this.values = BaseERefData.getValues(clazz);
		} else {
			this.values = values; 
		}
		init();
		if (defaultSelectedValues != null) { 
			setValue(defaultSelectedValues);
		}
	}
	
	/**
	 * 
	 * @param defaultValues
	 */
	public ERefDataOptionGroup(List<Boolean> defaultValues) {
		super(Arrays.asList(Boolean.TRUE , Boolean.FALSE), Arrays.asList("Yes" , "No"));
		setValue(defaultValues);
	}

}

