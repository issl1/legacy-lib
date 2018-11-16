package com.nokor.frmk.vaadin.ui.panel;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.helper.AppSettingConfigHelper;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;

/**
 * @author ly.youhort
 *
 */
public abstract class AbstractControlPanel extends VerticalLayout implements VaadinServicesHelper {

	private static final long serialVersionUID = 5503932281636575767L;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected List<String> errors = new ArrayList<String>();


    /**
     * Sets component error to field group layout
     *
     * @param localizedMessage - localized message
     */
    public void setComponentError(String localizedMessage) {
        setComponentError(new UserError(localizedMessage));
    }
    
    /**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatorySelectField(AbstractSelect field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatoryField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty(field.getValue())) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMandatoryDateField(DateField field, String messageKey) {
		boolean isValid = true;
		if (field.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message(messageKey)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkMaxLengthField(AbstractTextField field, String messageKey, int maxLength) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue()) && field.getValue().length() < maxLength) {
			errors.add(I18N.message("field.max.length.incorrect", I18N.message(messageKey), String.valueOf(maxLength)));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean checkDuplicatedCode(Class entityClass, AbstractTextField field, Long exceptedId, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			boolean isFound = ENTITY_SRV.checkByCodeExcept(entityClass, String.valueOf(field.getValue()), exceptedId);
			if (isFound) {
				errors.add(I18N.message("field.already.exists.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean checkDuplicatedField(Class entityClass, String fieldName, AbstractTextField field, Long exceptedId, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			boolean isFound = ENTITY_SRV.checkByFieldExcept(entityClass, fieldName, String.valueOf(field.getValue()), exceptedId);
			if (isFound) {
				errors.add(I18N.message("field.already.exists.1", I18N.message(messageKey)));
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	protected boolean checkRangeDateField(DateField start, DateField end) {
		boolean isValid = true;
		if (start.getValue() != null && end.getValue() != null &&
				end.getValue().before(start.getValue())) {
			errors.add(I18N.message("field.range.date.incorrect"));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param messageKey
	 * @return
	 */
	protected boolean checkFloatField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Float.parseFloat(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	protected boolean checkDoubleField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Double.parseDouble(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	public boolean checkBigDecimalField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				new BigDecimal(field.getValue());
			} catch (NumberFormatException e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
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
	protected boolean checkIntegerField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Integer.parseInt(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param messageKay
	 * @return
	 */
	protected boolean checkLongField(AbstractTextField field, String messageKay) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Long.parseLong(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKay)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param messageKey
	 * @return
	 */
	protected boolean checkBooleanField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Boolean.parseBoolean(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param messageKey
	 * @return
	 */
	protected boolean checkDateField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				DateUtils.string2DateDDMMYYYY(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return isValid;
	}
	
	/**
	 * Validate Percentage field between 0 and 100
	 * @param field
	 * @param messageKey
	 * @return
	 */
	public boolean checkPercentageField(AbstractTextField field, String messageKey) {
		boolean isValid = true;
		Double value = getDouble(field);
		if (value == null || value < 0 || value > 100) {
			this.errors.add(I18N.message("field.incorect.percentage.range",
					new String[] { I18N.message(messageKey) }));
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Float getFloat(AbstractTextField field, float defaultValue) {
		try {
			return Float.parseFloat(field.getValue());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Float getFloat(AbstractTextField field) {
		try {
			return Float.parseFloat(field.getValue());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(AbstractTextField field, double defaultValue) {
		try {
			return Double.parseDouble(field.getValue());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Double getDouble(AbstractTextField field) {
		try {
			return Double.parseDouble(field.getValue());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Integer getInteger(AbstractTextField field, int defaultValue) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Integer getInteger(AbstractTextField field) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public Long getLong(AbstractTextField field, long defaultValue) {
		try {
			return Long.parseLong(field.getValue());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	public Long getLong(AbstractTextField field) {
		try {
			return Long.parseLong(field.getValue());
		} catch (Exception e) {
			return null;
		}
	}
	
		
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(String value) {
		return value == null ? "" : value;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Integer value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Double value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * @param value
	 * @return
	 */
	public String getDefaultString(Long value) {
		return value == null ? "" : String.valueOf(value);
	}
	
	/**
	 * @param templateName
	 * @return
	 */
	protected CustomLayout initCustomLayout(String templateName) {
		if (StringUtils.isEmpty(AppSettingConfigHelper.getVaadinCustomLayoutsFolder())) {
			throw new IllegalArgumentException("The Vaadin custom layouts folder is not configured yet.");
		}
		return initCustomLayout(AppSettingConfigHelper.getVaadinCustomLayoutsFolder(), templateName);
	}
	
	/**
	 * @param customLayoutsFolder
	 * @param templateName
	 * @return
	 */
	protected CustomLayout initCustomLayout(String customLayoutsFolder, String templateName) {		
		String fullPath = customLayoutsFolder + templateName;
		logger.debug("Customer layout: [" + fullPath + "]");
		InputStream layoutFile = getClass().getResourceAsStream(fullPath);
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (Exception e) {
			String errMsg = "Could not locate template " + templateName;
			logger.error(errMsg, e);
			Notification.show(errMsg, e.getMessage(), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	/**
	 * Reset
	 */
	protected void reset() {
		errors = new ArrayList<String>();
	}
}
