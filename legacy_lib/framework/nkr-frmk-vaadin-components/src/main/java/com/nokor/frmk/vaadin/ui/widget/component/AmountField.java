
package com.nokor.frmk.vaadin.ui.widget.component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import com.nokor.frmk.vaadin.ui.widget.component.client.AmountFieldState;
import com.nokor.frmk.vaadin.ui.widget.component.shared.Constants;
import com.nokor.frmk.vaadin.ui.widget.component.shared.Utils;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.TextField;

/**
 * 
 * @author ly.youhort
 *
 */
public class AmountField extends TextField {

	private static final long serialVersionUID = 4315540860634118853L;

	public AmountField() {
		super();
		setConverter(new MaskNumberConverter());
	}

	public AmountField(Property<?> dataSource) {
		super(dataSource);
		setConverter(new MaskNumberConverter());
	}

	public AmountField(String caption, Property<?> dataSource) {
		super(caption, dataSource);
		setConverter(new MaskNumberConverter());
	}

	public AmountField(String caption, String value) {
		super(caption, value);
		setConverter(new MaskNumberConverter());
	}

	public AmountField(String caption) {
		super(caption);
		setConverter(new MaskNumberConverter());
	}

	public AmountField(String mask, char separator, char groupingSeparator) {
		this();
		setMask(mask);
		setDecimalSeparator(separator);
		setGroupingSeparator(groupingSeparator);
	}
	
	public void setMask(String mask) {
		if(mask == null) {
			throw new NullPointerException("The format mask cannot be null");
		}
		if(mask.trim().isEmpty()) {
			throw new IllegalStateException("The format mask cannot be empty");
		}
		getState().mask = mask;
	}
	
	public String getMask() {
		return getState().mask;
	}
	
	public void setDecimalSeparator(char decimalSeparator) {
		getState().decimalSeparator = decimalSeparator;
	}
	
	public char getDecimalSeparator() {
		return getState().decimalSeparator;
	}
	
	public void setGroupingSeparator(char groupingSeparator) {
		getState().groupingSeparator = groupingSeparator;
	}
	
	public char getGroupingSeparator() {
		return getState().groupingSeparator;
	}
	
	/**
	 * 
	 * @return Double
	 */
	public Double getDouble() {
		String str = getValue();
		char groupingSeparator = getState().groupingSeparator;
		char decimalSeparator = getState().decimalSeparator;
		if(groupingSeparator != Constants.EMPTY_CHAR) {
			str = str.replaceAll(groupingSeparator == '.' ? "\\." : Character.toString(groupingSeparator), "");
		}
		if(decimalSeparator != Constants.EMPTY_CHAR) {
			if(decimalSeparator != '.') {
				str = str.replaceAll(Character.toString(decimalSeparator), ".");
			}
		}
		try {
			return new Double(str);
		} catch (NumberFormatException ex) {
			return new Double(0.0);
		}
	}
	
	@Override
	public AmountFieldState getState() {
		return (AmountFieldState) super.getState();
	}
	
	@Override
	public void setPropertyDataSource(Property newDataSource) {
		if(!Number.class.isAssignableFrom(newDataSource.getType())) {
			throw new IllegalArgumentException("This field is compatible with number datasources only");
		}
		super.setPropertyDataSource(newDataSource);
	}

	/**
	 *
	 */
	private class MaskNumberConverter extends StringToDoubleConverter {

		private static final long serialVersionUID = 1L;

		private DecimalFormat formatter;
		
		public MaskNumberConverter() {
			refreshFormatter();
		}
		
		private void refreshFormatter() {
			if(formatter == null || 
					(	formatter.getDecimalFormatSymbols().getGroupingSeparator() != getGroupingSeparator()
					||  formatter.getDecimalFormatSymbols().getDecimalSeparator() != getDecimalSeparator()
					)
			) 
			{
				DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
				decimalSymbols.setGroupingSeparator(getGroupingSeparator());
				decimalSymbols.setDecimalSeparator(getDecimalSeparator());
				formatter = new DecimalFormat();
				formatter.setDecimalFormatSymbols(decimalSymbols);
			}
		}
		
		// @Override
		public Number convertToModel(String value, Locale locale) throws ConversionException {
			refreshFormatter();
			try {
				if(value == null || value.trim().isEmpty()) {
					return null;
				}
				Number number = formatter.parse(value);
				if(getPropertyDataSource() != null) {
					return Utils.convertToDataSource(number, getPropertyDataSource());
				}
				return number;
			} catch (ParseException e) {
				return Utils.convertToDataSource(new Double(0.0), getPropertyDataSource());
			}
		}

		// @Override
		public String convertToPresentation(Number value, Locale locale) throws ConversionException {
			return formatter.format(value);
		}

	}

}
