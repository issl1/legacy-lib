package com.nokor.frmk.vaadin.ui.widget.table;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ly.youhort
 *
 */
public class DateColumnRenderer extends PropertyColumnRenderer {
	/** */
	private static final long serialVersionUID = 2220356195204621112L;
	private String format;
	
	public DateColumnRenderer(String format) {
		this.format = format;
	}
	
	@Override
	public Object getValue() {
		Date dateValue = (Date) getPropertyValue();
		if (dateValue != null) {
			return new SimpleDateFormat(format).format(dateValue);
		}
		return null;
	}
}
