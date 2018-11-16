package com.nokor.frmk.vaadin.ui.widget.component;

import java.util.Arrays;
import java.util.Locale;

import com.nokor.frmk.vaadin.ui.widget.component.client.MaskedTextFieldState;
import com.nokor.frmk.vaadin.ui.widget.component.shared.Utils;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.TextField;

/**
 * Server side component
 *<br>Available masks:
 * 
 *<br>		# - any digit
 *<br>		U - upper-case letter
 *<br>    	L - lower-case letter
 *<br>    	? - any letter
 *<br>    	A - any number or character
 *<br>    	* - anything
 *<br>    	H - hex sign (0-9, a-f or A-F)
 *<br>    	' - Escape character, used to escape any of the special formatting characters.
 *<br>    	~ - +/- sign
 *<br>
 *
 * @author ky.nora
 */
public class MaskedTextField extends TextField {
	
	private static final long serialVersionUID = -6565140290462097050L;

	private char[] maskRepresentations = {'#', 'U', 'L', '?', 'A', '*', 'H', '~'};
	
	private char digitRepresentation = '#';
	
	private Boolean maskClientOnly = false;
	
	public MaskedTextField() {
		super();
		Arrays.sort(maskRepresentations);
	}

	/**
	 * @param caption
	 */
	public MaskedTextField(String caption) {
		setCaption(caption);
		setConverter(new UnmaskModelConverter(this));
	}

	/**
	 * @param caption
	 * @param mask
	 */
	public MaskedTextField(String caption, String mask) {
		setCaption(caption);
		setMask(mask);
	}

	/**
	 * @param dataSource
	 */
	public MaskedTextField(Property<?> dataSource) {
		super(dataSource);
		setConverter(new UnmaskModelConverter(this));
	}

	/**
	 * @param caption
	 * @param dataSource
	 */
	public MaskedTextField(String caption, Property<?> dataSource) {
		super(caption, dataSource);
		setConverter(new UnmaskModelConverter(this));
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		super.setPropertyDataSource(newDataSource);
		if(Number.class.isAssignableFrom(newDataSource.getType())) {
			validateNumberPropertyWithMask();
			setConverter(new MaskNumberConverter());
		} else if (char.class.isAssignableFrom(newDataSource.getType()) || String.class.isAssignableFrom(newDataSource.getType())) {
			setConverter(new UnmaskModelConverter(this));
		}
	}
	
	/**
	 */
	private void validateNumberPropertyWithMask() {
		char[] maskChars = getMask().replaceAll("\\+", "").toCharArray();
		for(char s : maskChars) {
			if(Arrays.binarySearch(maskRepresentations, s) >= 0 && s != digitRepresentation) {
				throw new IllegalArgumentException("This mask is not compatible with numeric datasources");
			}
		}
	}

	/**
	 * @return
	 */
	public String getMask() {
		return getState().mask;
	}
	
	/**
	 * @param mask
	 */
	public void setMask(String mask) {
		getState().mask = mask;
	}
	
	/**
	 * @return
	 */
	public char getPlaceHolder() {
		return getState().placeHolder;
	}
	
	/**
	 * @param placeHolder
	 */
	public void setPlaceHolder(char placeHolder) {
		getState().placeHolder = placeHolder;
	}
	
	/**
	 * @param isMaskClientOnly
	 */
	public void setMaskClientOnly(boolean isMaskClientOnly) {
		this.maskClientOnly = isMaskClientOnly;
		setConverter(new UnmaskModelConverter(this));
	}
	
	/**
	 * @return
	 */
	public boolean isMaskClientOnly() {
		return maskClientOnly.booleanValue();
	}

	@Override
	protected MaskedTextFieldState getState() {
		return (MaskedTextFieldState) super.getState();
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String unmask(final String value) {
		if(value == null || value.trim().isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder(value);
		String mask = getMask().replaceAll("\\+", "");
		int removedChars = 0;
		for(int i = 0; i<mask.length(); i++) {
			char s = mask.charAt(i);
			if(Arrays.binarySearch(maskRepresentations, s) < 0) {
				if(i < value.length() && sb.charAt(i-removedChars) == s) {
					sb.deleteCharAt(i-removedChars);
					removedChars++;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 *
	 */
	private class MaskNumberConverter extends StringToDoubleConverter {

		private static final long serialVersionUID = 1L;
		
		// @Override
		public Number convertToModel(String value, Locale locale) throws ConversionException {
			String unmasked = unmask(value);
			if(unmasked != null) {
				try {
					Number n = super.convertToModel(unmasked, null, locale);
					return Utils.convertToDataSource(n, getPropertyDataSource());
				} catch (NumberFormatException ne) {
					return Utils.convertToDataSource(0, getPropertyDataSource());
				}
			}
			return Utils.convertToDataSource(0, getPropertyDataSource());
		}
	}
	
	/**
	 *
	 */
	private static class UnmaskModelConverter implements Converter<String, String> {

		private static final long serialVersionUID = 1L;

		private MaskedTextField field;
		
		public UnmaskModelConverter(MaskedTextField field) {
			this.field = field;
		}
		
		// @Override
		public String convertToModel(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
			return value;
		}

		// @Override
		public String convertToPresentation(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
			if(field.isMaskClientOnly()) {
				String unmasked = field.unmask(value);
				if(unmasked != null) {
					return unmasked;
				}
			} 
			return value;
		}

		@Override
		public Class<String> getModelType() {
			return String.class;
		}

		@Override
		public Class<String> getPresentationType() {
			return String.class;
		}

		@Override
		public String convertToModel(String value,
				Class<? extends String> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public String convertToPresentation(String value,
				Class<? extends String> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}
		
	}

}