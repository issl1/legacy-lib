/**
 * 
 */
package com.nokor.frmk.vaadin.ui.menu;

import com.nokor.frmk.vaadin.ui.panel.system.BatchPanel;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

/**
 * @author ky.nora
 *
 */
public class FMBatchWindowCommand implements Command {
	/** */
	private static final long serialVersionUID = -7918601065639015167L;
	
	/**
	 * 
	 */
	public FMBatchWindowCommand() {
	}
	@Override
	public void menuSelected(MenuItem selectedItem) {
		UI.getCurrent().addWindow(new BatchPanel());
	}
}
