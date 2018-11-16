package com.nokor.frmk.vaadin.ui.widget.component;

import com.vaadin.data.Property;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;

public class TextField extends com.vaadin.ui.TextField {

	private static final long serialVersionUID = 931484499872567932L;
	
	public final static String TEXTTYPE_PLAIN = "PLAIN";
	public final static String TEXTTYPE_ALPHANUMERIC = "ALPHANUMERIC";
	public final static String TEXTTYPE_DIGIT = "DIGIT";
	public final static String TEXTTYPE_LETTER = "LETTER";

	private String textType = TEXTTYPE_PLAIN;
	private String exceptionalChars = "";

	public TextField() {
	}

	public TextField(Property dataSource) {
		super(dataSource);
	}

	public TextField(String caption, Property dataSource) {
		super(caption, dataSource);
	}

	public TextField(String caption, String value) {
		super(caption, value);
	}

	public TextField(String caption) {
		super(caption);
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		if (textType.equals(TEXTTYPE_ALPHANUMERIC) || textType.equals(TEXTTYPE_DIGIT) || textType.equals(TEXTTYPE_LETTER)
				|| textType.equals(TEXTTYPE_PLAIN)) {
			this.textType = textType;
		} else {
			this.textType = TEXTTYPE_PLAIN;
		}
	}

	public String getExceptionalChars() {
		return exceptionalChars;
	}

	public void setExceptionalChars(String exceptionalChars) {
		if (exceptionalChars == null) {
			this.exceptionalChars = "";
		} else {
			this.exceptionalChars = exceptionalChars;
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("textType", getTextType());
		target.addAttribute("exceptionalChars", getExceptionalChars());
	}
}
