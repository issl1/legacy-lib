package org.seuksa.frmk.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author prasnar
 *
 */
public class MyNumberUtils {
	
	/**
	 * Get long
	 * @param value
	 * @return
	 */
	public static long getLong(Long value) {
		return ((value == null) ? 0L : value.longValue());
	}
	
	/**
	 * Get integer
	 * @param value
	 * @return
	 */
	public static int getInteger(Integer value) {
		return (value == null) ? 0 : value.intValue();
	}

	/**
	 * Get integer
	 * @param value
	 * @return
	 */
	public static Integer getInteger(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * Get double
	 * @param value
	 * @return
	 */
	public static double getDouble(Double value) {
		return (value == null) ? 0.0 : value.doubleValue();
	}
	
	/**
	 * Get Double
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public static Double getDouble(String value, double defaultValue) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	/**
	 * Get BigDecimal
	 * @param field
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal getBigDecimal(String value, BigDecimal defaultValue) {
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get BigDecimal with null as default value
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimal(String value) {
		return getBigDecimal(value, null);
	}
	
    /**
     * 
     * @param amount
     * @return
     */
    public static Double formatDouble(Double amount) {
    	DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat formatter = (DecimalFormat) nf;
		formatter.applyPattern("##0.00");
		formatter.setDecimalFormatSymbols(unusualSymbols);
		String value = formatter.format(amount);
		amount = Double.parseDouble(value);
		return amount;
    }
    
    /**
     *  Format double with ##0.00
     * @param amount
     * @return
     */
    public static String formatDoubleToString(Double amount) {
    	return formatDoubleToString(amount, null);
    }
    
    /**
     * 
     * @param amount
     * @param format
     * @return
     */
    public static String formatDoubleToString(Double amount, String format) {
    	DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.US);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat formatter = (DecimalFormat) nf;
		
		if (StringUtils.isEmpty(format)) {
			formatter.applyPattern("##0.00");
		}else {
			formatter.applyPattern(format);
		}
		formatter.setDecimalFormatSymbols(unusualSymbols);
		String value = formatter.format(amount);
		return value;
    }
}
