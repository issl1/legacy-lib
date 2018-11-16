package com.nokor.efinance.batch.report;

import org.apache.poi.hssf.usermodel.HSSFFont;

import com.nokor.efinance.batch.report.BeanToExcel.FormatType;

/**
 * @author ky.nora
 *
 */
public class ReportColumn {
	private String method;
	private String header;
	private FormatType type;
	private HSSFFont font;
	private Short color;

	/**
	 * @param method
	 * @param header
	 * @param type
	 * @param font
	 * @param color
	 */
	public ReportColumn(String method, String header, FormatType type,
			HSSFFont font, Short color) {
		this.method = method;
		this.header = header;
		this.type = type;
		this.font = font;
		this.color = color;
	}

	/**
	 * @param method
	 * @param header
	 * @param type
	 * @param font
	 */
	public ReportColumn(String method, String header, FormatType type,
			HSSFFont font) {
		this(method, header, type, font, null);
	}

	/**
	 * @param method
	 * @param header
	 * @param type
	 * @param color
	 */
	public ReportColumn(String method, String header, FormatType type,
			Short color) {
		this(method, header, type, null, color);
	}

	/**
	 * @param method
	 * @param header
	 * @param type
	 */
	public ReportColumn(String method, String header, FormatType type) {
		this(method, header, type, null, null);
	}

	/**
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return
	 */
	public FormatType getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(FormatType type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public HSSFFont getFont() {
		return font;
	}

	/**
	 * @param font
	 */
	public void setFont(HSSFFont font) {
		this.font = font;
	}

	/**
	 * @return
	 */
	public Short getColor() {
		return color;
	}

	/**
	 * @param color
	 */
	public void setColor(Short color) {
		this.color = color;
	}
	
}