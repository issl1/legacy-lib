package com.nokor.frmk.vaadin.ui.menu;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author bunlong.taing
 *
 */
public class FMWindowPopupCommand implements Command {
	/** */
	private static final long serialVersionUID = -6423686152351576193L;
	private Window window;
	
	/**
	 * 
	 * @param window
	 */
	public FMWindowPopupCommand(Window window) {
		this.window = window;
	}

	@Override
	public void menuSelected(MenuItem selectedItem) {
		UI.getCurrent().addWindow(this.window);
	}
}
