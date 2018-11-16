package com.nokor.frmk.vaadin.ui.panel;

import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract form panel
 * @author ly.youhort
 */
public abstract class AbstractTabPanel extends AbstractControlPanel {

	private static final long serialVersionUID = -2973580642171768437L;
	
	private VerticalLayout messagePanel; 
		
	public AbstractTabPanel() {
		init();
	}
	
	/**
	 * Initialization
	 */
	public void init() {
		setMargin(true);
		setSpacing(true);
				
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		addComponent(messagePanel);		
		addComponent(createForm());
	}
	
	/**
	 * Display success message
	 */
	protected void displaySuccess() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		messagePanel.removeAllComponents();
		messagePanel.addComponent(new HorizontalLayout(iconLabel, messageLabel));
		messagePanel.setVisible(true);
	}
		
	/**
	 * Remove error or success message panel
	 */
	public void removeMessagePanel() {
		removeErrorsPanel();
	}
	
	/**
	 * Display errors messages
	 */
	protected void displayErrorsPanel() {
		messagePanel.removeAllComponents();
		for (String error : errors) {
			Label messageLabel = new Label(error);
			messageLabel.addStyleName("error");
			messagePanel.addComponent(messageLabel);
		}
		messagePanel.setVisible(true);
	}
	
	/**
	 * Reset
	 */
	public void removeErrorsPanel() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}
	
	/**
	 * Update navigation panel
	 * @param navigationPanel
	 * @param defaultNavigationPanel
	 */
	public void updateNavigationPanel(VerticalLayout navigationLayout, NavigationPanel defaultNavigationPanel) {
		navigationLayout.removeAllComponents();
		navigationLayout.addComponent(defaultNavigationPanel);
	}
	
	protected abstract Component createForm();
	
}
