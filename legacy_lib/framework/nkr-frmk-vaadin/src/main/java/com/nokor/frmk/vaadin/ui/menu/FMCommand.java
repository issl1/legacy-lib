/**
 * 
 */
package com.nokor.frmk.vaadin.ui.menu;

import com.vaadin.server.Page;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * @author youhort
 *
 */
public class FMCommand implements Command {
	/** */
	private static final long serialVersionUID = 2775522526297792259L;
	
	private String viewName = "";
	
	/**
	 * 
	 * @param viewName
	 */
	public FMCommand(String viewName) {
		this.viewName = viewName;
	}
	
	@Override
	public void menuSelected(MenuItem selectedItem) {
		Page.getCurrent().setUriFragment("!" + viewName);
	}
}
