package com.nokor.frmk.vaadin.ui.panel.template;

import com.nokor.frmk.vaadin.ui.panel.template.basic.TopPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author prasnar
 *
 */
public class SimpleMasterUI extends MasterUI {
	
	private VerticalLayout parentTopPanel;
	private VerticalLayout topPanel;
	private VerticalLayout contentPanel;
	
	/**
	 * 
	 */
	public SimpleMasterUI() {
	}
	
	/**
	 * 
	 * @param topPanel
	 * @param contentPanel
	 */
	public SimpleMasterUI(VerticalLayout topPanel, VerticalLayout contentPanel) {
		this();
		this.topPanel = topPanel;
		this.contentPanel = contentPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#initLayout()
	 */
	@Override
	public VerticalLayout initLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addStyleName("addBorder");
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);
		
		if (topPanel == null) {
			topPanel = new VerticalLayout();
		}
		
		parentTopPanel = new VerticalLayout();
		parentTopPanel.addComponent(topPanel);

		if (contentPanel == null) {
			contentPanel = new VerticalLayout();
		}
		contentPanel.setSizeFull();
		contentPanel.setMargin(true);

		mainLayout.addComponent(parentTopPanel);
		mainLayout.addComponent(contentPanel);
		mainLayout.setExpandRatio(contentPanel, 1.0f);
		mainLayout.setComponentAlignment(parentTopPanel, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(contentPanel, Alignment.MIDDLE_LEFT);
		
		return mainLayout;
	}


	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#render(boolean)
	 */
	@Override
	public void render(boolean display) {
		((TopPanel) topPanel).render(display);
		
		if (display) {
			parentTopPanel.addStyleName("top-wrap");
			contentPanel.addStyleName("content-wrap");
		}
		else {
			parentTopPanel.removeStyleName("top-wrap");
			contentPanel.removeStyleName("content-wrap");
		}
	}
	
	/**
	 * Get main menu bar.
	 * @return Main menu bar
	 */
	@Override
	public MenuBar getMainMenuBar() {
		return ((TopPanel) topPanel).getMainMenuBar();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#getTopPanel()
	 */
	@Override
	public VerticalLayout getTopPanel() {
		return topPanel;
	}

	/**
	 * @param topPanel the topPanel to set
	 */
	public void setTopPanel(VerticalLayout topPanel) {
		this.topPanel = topPanel;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#getContentPanel()
	 */
	@Override
	public VerticalLayout getContentPanel() {
		return contentPanel;
	}

	/**
	 * @param contentPanel the contentPanel to set
	 */
	public void setContentPanel(VerticalLayout contentPanel) {
		this.contentPanel = contentPanel;
	}

	@Override
	public VerticalLayout getLeftPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerticalLayout getRightPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerticalLayout getBottomPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
