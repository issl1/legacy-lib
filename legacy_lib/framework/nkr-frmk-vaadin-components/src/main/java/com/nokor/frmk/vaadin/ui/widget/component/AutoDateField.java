
package com.nokor.frmk.vaadin.ui.widget.component;

import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.ui.datefield.PopupDateFieldState;
import com.vaadin.ui.PopupDateField;

/** 
 * Auto masked date field
 *<br>Use setDateFormat("dd-MM-yyyy");
 * 
 *<br>		- dd : Day
 *<br>		- MM : Month
 *<br>    	- yy : Year
 *<br>    	- hh : Hour
 *<br>    	- mm : Minute
 *<br>    	- ss : Second
 *<br>
 * @author ky.nora
 */
public class AutoDateField extends PopupDateField {
	private static final long serialVersionUID = -8326484494885322293L;
    
	private static final String defaultDateFormat = "dd/MM/yyyy";
    
	private String mask = null;
	
	public AutoDateField() {
		super();
	    setDateFormat(defaultDateFormat);
	}

	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public AutoDateField(Property dataSource) throws IllegalArgumentException {
		super(dataSource);
		setDateFormat(defaultDateFormat);
	}

	/**
	 * @param caption
	 * @param value
	 */
	public AutoDateField(String caption, Date value) {
		super(caption, value);
		setDateFormat(defaultDateFormat);
	}

	/**
	 * @param caption
	 * @param dataSource
	 */
	public AutoDateField(String caption, Property dataSource) {
		super(caption, dataSource);
		setDateFormat(defaultDateFormat);
	}

	/**
	 * @param caption
	 */
	public AutoDateField(String caption) {
		super(caption);
		setDateFormat(defaultDateFormat);
	}
	
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (mask != null) {
            target.addAttribute("mask", mask);
        }
    }
    
    /**
     * @return
     */
    public String getMask() {
        return mask;
    }

    /**
     * @param mask
     */
    private void setMask(String mask) {
        this.mask  = mask;
        markAsDirty();
    }
    
	@Override
    protected PopupDateFieldState getState() {
        return (PopupDateFieldState) super.getState();
    }

	@Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        setMask(dateFormat);
    }
    
}
