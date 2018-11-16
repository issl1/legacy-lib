package com.nokor.frmk.vaadin.ui.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.DateField;

/**
 * 
 * @author pengleng.huot
 *
 */
public class ValidateUtil {

	private static List<String> errors;

	/**
	 * Clear error messages.
	 */
	public static void clearErrors() {
		errors = new ArrayList<String>();
	}
	
	public static void addError(String errorMsg) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		errors.add(errorMsg);
	}
	
	/**
	 * Get error messages.
	 * 
	 * @return
	 */
	public static String getErrorMessages() {
		String errorMessages = "";
		
		if (errors != null) {
			for (String error: errors) {
				if (!errorMessages.isEmpty()) {
					errorMessages += "<br/>";
				}
				errorMessages += "- " + error;
			}
		}
		return errorMessages;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	public static boolean checkMandatorySelectField(AbstractSelect field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			addError(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	public static boolean checkMandatoryField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty(field.getValue())) {
			addError(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	public static boolean checkMandatoryDateField(DateField field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			addError(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean checkRangeDateField(DateField start, DateField end) {
		boolean isValid = true;
		if (start.getValue() != null && end.getValue() != null &&
				end.getValue().before(start.getValue())) {
			addError(I18N.message("field.range.date.incorrect"));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	public static boolean checkDoubleField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Double.parseDouble(field.getValue());
			} catch (NumberFormatException e) {
				addError(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	public static boolean checkIntegerField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Integer.parseInt(field.getValue());
			} catch (NumberFormatException e) {
				addError(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param messageKey
	 * @return
	 */
	public static boolean checkBigDecimalField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				new BigDecimal(field.getValue());
			} catch (NumberFormatException e) {
				addError(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}

}
