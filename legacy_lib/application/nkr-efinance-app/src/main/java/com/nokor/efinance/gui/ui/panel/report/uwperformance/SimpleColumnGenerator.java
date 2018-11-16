package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class SimpleColumnGenerator implements ColumnGenerator{
	
	public SimpleColumnGenerator(){
		
	}

	private static final long serialVersionUID = 5686537192564968936L;

	private String timeToString(Long millis) {
		if (millis != null) {
			String s = "" + (millis / 1000) % 60;
			String m = "" + (millis / (1000 * 60)) % 60;
			String h = "" + (millis / (1000 * 60 * 60)) % 24;
			String d = "" + (millis / (1000 * 60 * 60 * 24));
			return d + "d " + h + "h:" + m + "m:" + s + "s";
		}
		return "N/A";
	}
	
	private String timeToString(Float hrs){
		if(hrs != null){
			return String.format("%.2f h", hrs.floatValue());
		}
		
		return "N/A";
	}
	
	@Override
	public com.vaadin.ui.Component generateCell(Table source, Object itemId,	Object columnId) {
		Item item = source.getItem(itemId);
					
		Object value = item.getItemProperty(columnId).getValue();
		if(value == null)
			return new Label("");
		
		if(value instanceof Float){
			return new Label(timeToString(((Float)value)));			
		}
		return new Label(value.toString());
	}
}
