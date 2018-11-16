package com.nokor.frmk.vaadin.ui.widget.component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

public class NumericField extends TextField {

	private static final long serialVersionUID = -2118344057018950676L;
	
	public static String RIGHT = "right";
	public static String LEFT = "left";

	private String alignment = RIGHT;
	private String decimalSeperator = ".";

	private String groupingSeperator = ",";
	private int scaleSize = 2;
	private int precisionSize = 12;
	private boolean useGroupingSeperator = true;

	public NumericField() {
		super.setTextType(TextField.TEXTTYPE_DIGIT);

	}

	public NumericField(String caption) {
		super(caption);
		super.setTextType(TextField.TEXTTYPE_DIGIT);

	}

	public void setLocale(Locale locale) {
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
		decimalSeperator = String.valueOf(symbols.getDecimalSeparator());
		groupingSeperator = String.valueOf(symbols.getGroupingSeparator());
		markAsDirty();
	}

	/**
	 * Set text Type
	 * @param textType
	 */
	public void setTextType(String textType) {
		throw new UnsupportedOperationException();
	}

	/**
	 *@param exceptionalChars 
	 */
	public void setExceptionalChars(String exceptionalChars) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get decimal separtor
	 * @return
	 */
	public String getDecimalSeperator() {
		return decimalSeperator;
	}

	/**
	 * Set decimal separator
	 * @param decimalSeperator
	 */
	public void setDecimalSeperator(String decimalSeperator) {
		this.decimalSeperator = decimalSeperator;
		markAsDirty();
	}

	/**
	 * Get grouping separator
	 * @return
	 */
	public String getGroupingSeperator() {
		return groupingSeperator;
	}

	/**
	 * Set grouping separator
	 * @param groupingSeperator
	 */
	public void setGroupingSeperator(String groupingSeperator) {
		this.groupingSeperator = groupingSeperator;
		setUseGroupingSeperator(groupingSeperator != null);
		markAsDirty();
	}

	/**
	 * Get scale size
	 * @return
	 */
	public int getScaleSize() {
		return scaleSize;
	}

	/**
	 * Set scale size
	 * @param scaleSize
	 */
	public void setScaleSize(int scaleSize) {
		this.scaleSize = scaleSize;
		markAsDirty();
	}

	/**
	 * Get precision size
	 * @return
	 */
	public int getPrecisionSize() {
		return precisionSize;
	}

	/**
	 * Set precision size
	 * @param precisionSize
	 */
	public void setPrecisionSize(int precisionSize) {
		this.precisionSize = precisionSize;
		markAsDirty();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("decimalsep", decimalSeperator);
		target.addAttribute("groupingsep", groupingSeperator);
		target.addAttribute("scale", scaleSize);
		target.addAttribute("precision", precisionSize);
		target.addAttribute("usegrouping", useGroupingSeperator);
		target.addAttribute("alignment", alignment);

	}

	/**
	 * Is yse grouping separator
	 * @return
	 */
	public boolean isUseGroupingSeperator() {
		return useGroupingSeperator;
	}

	/**
	 * Set grouping separator
	 * @param useGroupingSeperator
	 */
	public void setUseGroupingSeperator(boolean useGroupingSeperator) {
		this.useGroupingSeperator = useGroupingSeperator;
	}

	/**
	 * Get text aligment
	 * @return
	 */
	public String getTextAlignment() {
		return alignment;
	}

	/**
	 * Set text alignment
	 * @param alignment
	 */
	public void setTextAlignment(String alignment) {
		this.alignment = alignment;
	}

	/**
	 * Set numeric value
	 * @param numericVal
	 */
	public void setNumericValue(BigDecimal numericVal) {
		DecimalFormat df = new DecimalFormat("##0.0#", new DecimalFormatSymbols(Locale.ENGLISH));
		setValue(df.format(numericVal));
	}

	/**
	 * Get numeric value
	 * @return
	 */
	public BigDecimal getNumericValue() {
		BigDecimal bd = new BigDecimal((String) getValue());
		bd.setScale(getScaleSize());
		return bd;
	}
}
