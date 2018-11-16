
package com.nokor.frmk.vaadin.ui.widget.component;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

/**
 * Server side component
 * @author ky.nora
 */
public class NumberField extends TextField {

	private static final long serialVersionUID = 4546502525546603481L;

	public NumberField() {
		super();
	}

	public NumberField(Property<Long> dataSource) {
		super(dataSource);
	}

	public NumberField(String caption, Property<Long> dataSource) {
		super(caption, dataSource);
	}

	public NumberField(String caption, Long number) {
		super(caption, number == null ? "" : number.toString());
	}

	public NumberField(String caption) {
		super(caption);
	}

}
