/**
 * 
 */
package com.nokor.frmk.vaadin.ui.menu;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.nokor.ersys.vaadin.ui.security.PasswordPanel;
import com.vaadin.ui.UI;

/**
 * @author youhort
 *
 */
public class FMPasswordCommand implements Command {
	/** */
	private static final long serialVersionUID = -6026686819456987525L;
	
	/**
	 * 
	 */
	public FMPasswordCommand() {
	}
	
	@Override
	public void menuSelected(MenuItem selectedItem) {
		UI.getCurrent().addWindow(new PasswordPanel());
	}
}
