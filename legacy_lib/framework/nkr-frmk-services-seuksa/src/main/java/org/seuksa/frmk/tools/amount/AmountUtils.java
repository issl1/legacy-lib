package org.seuksa.frmk.tools.amount;

import java.text.DecimalFormat;

/**
 * @author ly.youhort
 */
public class AmountUtils {
	/**
	 * It is replaced by format(Double value, int nbDecimal) 
	 * @param value
	 * @return
	 */
	@Deprecated
	public static String format4Decimals(Double value) {
		if (value != null) {
			DecimalFormat stringValue = new DecimalFormat("##0.0000");
			return stringValue.format(value);
		}
		return "";
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String formatUSD(Double value) {
		if (value != null) {
			DecimalFormat stringValue = new DecimalFormat("$###,###.00");
			return stringValue.format(value);
		}
		return "";
	}

	/**
	 * @param value
	 * @return
	 */
	public static String format(Double value) {
		if (value != null) {
			DecimalFormat stringValue = new DecimalFormat("##0.00");
			return stringValue.format(value);
		}
		return "";
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String format(Double value, int nbDecimal) {
		if (value != null) {
			String format = "##0.";
			for (int i = 0; i < nbDecimal; i++) {
				format += "0";
			}
			DecimalFormat stringValue = new DecimalFormat(format);
			return stringValue.format(value);
		}
		return "";
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static Amount convertToAmount(Double value) {
		Amount amount = new Amount();
		amount.setTiAmount(value);
		amount.setVatAmount(0d);
		amount.setTeAmount(value);
		return amount;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static Amount convertToAmount(Double value, int nbDecimal) {
		Amount amount = convertToAmount(value);
		amount.setNbDecimal(nbDecimal);
		return amount;
	}
}

