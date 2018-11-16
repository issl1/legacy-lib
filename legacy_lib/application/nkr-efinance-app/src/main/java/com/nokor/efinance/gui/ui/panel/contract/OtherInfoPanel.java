package com.nokor.efinance.gui.ui.panel.contract;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Customer identity panel
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OtherInfoPanel extends AbstractTabPanel implements FMEntityField {
	
	private static final long serialVersionUID = -3020972492621000892L;
	
	 protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	private Panel contentPanel;
	private NavigationPanel navigationPanel;
	private VerticalLayout verticalLayout;
	private FormLayout formLayout;
	
	private Contract contract;
		
	public OtherInfoPanel() {
		super();
		setMargin(true);
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		contentPanel = new Panel(I18N.message("other.info"));
		contentPanel.setSizeFull();
		navigationPanel = new NavigationPanel();
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			
					private static final long serialVersionUID = 7148778578554352522L;

					public void buttonClick(ClickEvent event) {
					}
				});
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		navigationPanel.addButton(btnSave);
				
		formLayout = new FormLayout();
		formLayout.setMargin(true);
				
		verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		contentPanel.setContent(verticalLayout);
		addComponent(contentPanel);		
		return contentPanel;
	}

	
	/**
	 * Reset panel
	 */
	public void reset() {
	}
	/**
	 * 
	 * @param Contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
	}
}
