package com.nokor.frmk.vaadin.ui.widget.table.columngenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.seuksa.frmk.tools.DateUtils;

import com.vaadin.data.Property;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * @author ly.youhort
 */
public class DateColumnGenerator implements ColumnGenerator {

	private static final long serialVersionUID = -4466553524184109244L;
	
	private String format;
	
	public DateColumnGenerator() {
        this(DateUtils.FORMAT_DDMMYYYY_SLASH);
    }
	
	public DateColumnGenerator(String format) {
        this.format = format;
    }
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
        Property prop = source.getItem(itemId).getItemProperty(columnId);
        if (prop.getType().equals(Date.class)) {
            Label label = new Label(new SimpleDateFormat(format).format((Date) prop.getValue()));
            return label;
        }
        return null;
	}

}
