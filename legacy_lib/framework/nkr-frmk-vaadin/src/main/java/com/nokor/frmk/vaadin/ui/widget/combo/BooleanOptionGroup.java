package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author prasnar
 *
 */
public class BooleanOptionGroup extends SimpleOptionGroup {
	/** */
	private static final long serialVersionUID = -8911086777953275359L;

	/**
	 * 
	 */
	public BooleanOptionGroup() {
		super(Arrays.asList(Boolean.TRUE , Boolean.FALSE), Arrays.asList("Yes" , "No"));
	}


	/**
	 * 
	 * @param defaultValue
	 */
	public BooleanOptionGroup(boolean defaultValue) {
		super(Arrays.asList(Boolean.TRUE , Boolean.FALSE), Arrays.asList("Yes" , "No"));
		setValue(Arrays.asList(defaultValue));
	}
	
	/**
	 * 
	 * @param defaultValues
	 */
	public BooleanOptionGroup(List<Boolean> defaultValues) {
		super(Arrays.asList(Boolean.TRUE , Boolean.FALSE), Arrays.asList("Yes" , "No"));
		setValue(defaultValues);
	}

}

