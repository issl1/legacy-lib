package com.nokor.frmk.vaadin.ui.widget.tabsheet;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;

/**
 * Tabsheet
 * @author ly.youhort
 */
public class TabSheet extends com.vaadin.ui.TabSheet {

	private static final long serialVersionUID = -3662702639227744966L;
			
	private Component tablePanel;
	private List<Component> formsPanel;
	
	private boolean add = false;
	private boolean forceSelected = false;
	private boolean needRefresh = false;
		
	public TabSheet() {
		super();
		formsPanel = new ArrayList<Component>();
		setSizeFull();
	}
			

	/**
	 * @return the tablePanel
	 */
	public Component getTablePanel() {
		return tablePanel;
	}

	/**
	 * Set table panel to the tabSheet
	 * @param tablePanel
	 */
	public void setTablePanel(Component tablePanel) {
		this.tablePanel = tablePanel;
		addTab(tablePanel, tablePanel.getCaption(), null, 0);
	}
			
	/**
	 * Add form panel
	 * @param formPanel
	 */
	public void addFormPanel(Component formPanel) {
		addFormPanel(formPanel, formPanel.getCaption());
	}
	
	/**
	 * Add form panel
	 * @param formPanel
	 */
	public void addFormPanel(Component formPanel, String caption) {
		formPanel.setCaption(caption);
		if (!formsPanel.contains(formPanel)) {
			this.formsPanel.add(formPanel);
		}
		if (getTab(formPanel) == null) {
			addTab(formPanel, formPanel.getCaption());
		}
	}
	
	/**
	 * Display others panel
	 */
	public void displayFormsPanel() {
		for (Component formPanel : formsPanel) {
			addTab(formPanel, formPanel.getCaption());
		}
	}
	
	/**
	 * Remove others panel
	 */
	public void removeFormsPanel() {
		for (Component formPanel : formsPanel) {
			removeComponent(formPanel);
		}
	}

	/**
	 * Remove form Panel
	 * @param formPanel
	 */
	public void removeFormPanel(Component formPanel) {
		removeComponent(formPanel);
	}
	
	public void disableFormPanel() {
		for (Component formPanel : formsPanel) {
			if (formPanel != getSelectedTab()) {
				formPanel.setEnabled(false);
			}
		}
	}
	
	public void enableFormPanel() {
		for (Component formPanel : formsPanel) {
			formPanel.setEnabled(true);
		}
	}
	
	/**
	 * @return the add
	 */
	public boolean isAdd() {
		return add;
	}

	/**
	 * @param add the add to set
	 */
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	/**
	 * @return the forceSelected
	 */
	public boolean isForceSelected() {
		return forceSelected;
	}


	/**
	 * @param forceSelected the forceSelected to set
	 */
	public void setForceSelected(boolean forceSelected) {
		this.forceSelected = forceSelected;
	}


	/**
	 * @return the needRefresh
	 */
	public boolean isNeedRefresh() {
		return needRefresh;
	}

	/**
	 * @param needRefresh the needRefresh to set
	 */
	public void setNeedRefresh(boolean needRefresh) {
		this.needRefresh = needRefresh;
	}

}
