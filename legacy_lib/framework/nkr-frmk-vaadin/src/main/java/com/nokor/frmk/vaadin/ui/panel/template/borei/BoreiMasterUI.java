package com.nokor.frmk.vaadin.ui.panel.template.borei;

import com.nokor.frmk.vaadin.ui.panel.template.MasterUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author prasnar
 *
 */
public class BoreiMasterUI extends MasterUI {
	
	private VerticalLayout parentTopPanel;
	private HorizontalLayout parentContentPanel;
	private VerticalLayout topPanel;
	private BoreiLeftPanel leftPanel;
	private VerticalLayout contentPanel;
	
	/**
	 * 
	 */
	public BoreiMasterUI(String comLogoPath, String comURL) {
		super(comLogoPath, comURL);
		
		topPanel = new BoreiTopPanel(comLogoPath, comURL);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#initLayout()
	 */
	@Override
	public VerticalLayout initLayout() {
		VerticalLayout mainLayout = new VerticalLayout();
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
		contentPanel.addStyleName("right-wrap");
		contentPanel.setSizeFull();
		contentPanel.setMargin(true);
		
		leftPanel = new BoreiLeftPanel(getComLogoPath(), getComURL());
		leftPanel.setWidth("175px");
		leftPanel.setHeight("100%");
		leftPanel.setMargin(false);
		
		parentContentPanel = new HorizontalLayout();
		parentContentPanel.setSizeFull();
		parentContentPanel.setMargin(false);
		
		parentContentPanel.addComponent(leftPanel);
		parentContentPanel.addComponent(contentPanel);
		
		parentContentPanel.setExpandRatio(contentPanel, 1.0f);

		mainLayout.addComponent(parentTopPanel);
		mainLayout.addComponent(parentContentPanel);
		
		mainLayout.setExpandRatio(parentContentPanel, 1.0f);
		
		mainLayout.setComponentAlignment(parentTopPanel, Alignment.TOP_LEFT);
		mainLayout.setComponentAlignment(parentContentPanel, Alignment.MIDDLE_LEFT);
		return mainLayout;
	}


	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.template.MasterUI#render(boolean)
	 */
	@Override
	public void render(boolean display) {
		((BoreiTopPanel) topPanel).render(display);
		
		if (display) {
			leftPanel.setVisible(true);
			
			parentTopPanel.addStyleName("top-wrap");
			parentContentPanel.addStyleName("content-wrap");
		}
		else {
			leftPanel.setVisible(false);
			leftPanel.reset();
			
			parentTopPanel.removeStyleName("top-wrap");
			parentContentPanel.removeStyleName("content-wrap");
		}
	}
	
	/**
	 * Get main menu bar.
	 * @return Main menu bar
	 */
	@Override
	public MenuBar getMainMenuBar() {
		return ((BoreiTopPanel) topPanel).getMainMenuBar();
	}
	
	public HorizontalLayout getTopInfoLayout() {
		if (topPanel instanceof BoreiTopPanel) {
			return ((BoreiTopPanel) topPanel).getInfoLayout();
		}
		return null;
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
		return leftPanel;
	}

	@Override
	public VerticalLayout getRightPanel() {
		return null;
	}

	@Override
	public VerticalLayout getBottomPanel() {
		return null;
	}

}
