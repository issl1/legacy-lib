package com.nokor.frmk.vaadin.ui.panel;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.security.service.SecUserCreationException;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract form panel
 * @author ly.youhort
 */
public abstract class AbstractFormPanel extends AbstractControlPanel implements SaveClickListener, AppServicesHelper {

	private static final long serialVersionUID = 7009973308855388332L;

	private VerticalLayout messagePanel; 
	
	
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
	 * @return
	 */
	public NavigationPanel addNavigationPanel() {
		return addNavigationPanel(0);
	}
	
	/**
	 * @return
	 */
	public NavigationPanel addNavigationPanel(int index) {
		NavigationPanel navigationPanel = new NavigationPanel(); 
		addComponent(navigationPanel, index);
		return navigationPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		messagePanel.setVisible(false);
		errors.clear();
		if (validate()) {
			try {
				saveEntity();
				displaySuccess();
				processAfterSuccess();
				if (getParent() instanceof TabSheet) {
					((TabSheet) getParent()).setNeedRefresh(true);
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				
				if (ex instanceof HibernateOptimisticLockingFailureException) {
					errors.add(I18N.message("msg.error.save.by.another.user"));
				} else if (ex instanceof SecUserCreationException) {
					errors.add(ex.getMessage());
				} else {
					errors.add(I18N.message("msg.error.technical"));
				}
			}
			if (!errors.isEmpty()) {
				displayErrors();
				processAfterError();
			}
		} else {
			displayErrors();
			processAfterError();
		}
	}

	/**
	 * Display errors messages
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}
	
	/**
	 * This method is allowed to override
	 */
	public void processAfterError() {
		// Do nothing
	}
	
	/**
	 * Display success message
	 */
	public void displaySuccess() {
		String msg = getSuccessfullyMessage();
//		Label messageLabel = new Label(msg);
//		messageLabel.addStyleName("success");
//		Label iconLabel = new Label();
//		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
//		iconLabel.addStyleName("success-icon");
//		messagePanel.removeAllComponents();
//		messagePanel.addComponent(new HorizontalLayout(iconLabel, messageLabel));
//		messagePanel.setVisible(true);
		Notification.show("", msg, Type.HUMANIZED_MESSAGE);
	}
	
	/**
	 * This method is allowed to override
	 * @return
	 */
	public String getSuccessfullyMessage() {
		return I18N.message("msg.info.save.successfully");
	}
	
	/**
	 * This method is allowed to override
	 */
	public void processAfterSuccess() {
		// Do nothing
	}
	
	/**
	 * 
	 */
	public void saveEntity() {
		ENTITY_SRV.saveOrUpdate(getEntity());
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		messagePanel.setVisible(false);
		errors.clear();
	}
		
	/**
	 * @return
	 */
	protected boolean validate() {
		return true;
	}

	protected abstract Entity getEntity();
	protected abstract Component createForm();

}
