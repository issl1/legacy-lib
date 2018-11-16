package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.nokor.frmk.vaadin.ui.widget.component.shared.Constants;
import com.vaadin.client.ui.VTextField;

/**
 * @author ky.nora
 */
public class NumberFieldWidget extends VTextField implements KeyPressHandler {
	public NumberFieldWidget() {
		setAlignment(TextAlignment.RIGHT);
		
		addKeyPressHandler(this);
		addKeyDownHandler(this);
		sinkEvents(Event.ONPASTE);
	}
	
	protected void refreshValue(String value) {		
	    String str = value == null ? getText() : value;
		try {
			Long.parseLong(str);
		} catch (Exception ex) {
			super.setText("0");
		}
	}
	
	private boolean isCopyOrPasteEvent(KeyPressEvent evt) {
		if(evt.isControlKeyDown()) {
			return Character.toString(evt.getCharCode()).toLowerCase().equals("c") ||
					Character.toString(evt.getCharCode()).toLowerCase().equals("v");
		}
		return false;
	}
	
	@Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (event.getTypeInt() == Event.ONPASTE) {
            onPaste(event);
        }
    }
	
	public void onPaste(Event event) {
		refreshValue(null);
	}
	
	@Override
	public void onBlur(BlurEvent event) {
		super.onBlur(event);
		refreshValue(null);
	}
	
	@Override
	public void onKeyDown(KeyDownEvent event) {
		super.onKeyDown(event);
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			refreshValue(null);			
		}
	}
	
	@Override
	public void onKeyPress(KeyPressEvent event) {
		if(!isCopyOrPasteEvent(event)) {
			char charCode = event.getCharCode();
			if (charCode == Constants.EMPTY_CHAR) {
				return;
			}
			
			if (charCode == KeyCodes.KEY_BACKSPACE
					|| charCode == KeyCodes.KEY_DELETE
					|| charCode == KeyCodes.KEY_END
					|| charCode == KeyCodes.KEY_ENTER
					|| charCode == KeyCodes.KEY_ESCAPE
					|| charCode == KeyCodes.KEY_HOME
					|| charCode == KeyCodes.KEY_LEFT
					|| charCode == KeyCodes.KEY_PAGEDOWN
					|| charCode == KeyCodes.KEY_PAGEUP
					|| charCode == KeyCodes.KEY_RIGHT
					|| charCode == KeyCodes.KEY_TAB)
				return;
			if (charCode == '-') {
				if (getText().isEmpty() || (getText().charAt(0) != '-' && getCursorPos() == 0)) {
					return;
				}
			}
			if (!Character.isDigit(charCode)) {
				cancelKey();
			}
		}
	}
	
}
