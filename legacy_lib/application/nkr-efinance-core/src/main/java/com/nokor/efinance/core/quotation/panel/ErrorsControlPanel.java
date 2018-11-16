package com.nokor.efinance.core.quotation.panel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nokor.efinance.core.shared.exception.ErrorMessage;
import com.nokor.efinance.core.shared.system.DomainType;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Errors control panel
 * @author ly.youhort
 */
public class ErrorsControlPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	private Map<DomainType, Panel> domainMap = null;
	
	private VerticalLayout contentLayout;
	
	public ErrorsControlPanel() {
		super();
		setSizeFull();
		domainMap = new LinkedHashMap<DomainType, Panel>();		
	}
	
	@Override
	protected Component createForm() {
		contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.setMargin(true);				
        return contentLayout;
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
	}
	
	/**
	 * Display errors message
	 * @return
	 */
	public void displayErrors(List<ErrorMessage> errors) {
		
		domainMap.clear();
		contentLayout.removeAllComponents();
		
		for (ErrorMessage error : errors) {			
			Panel errorsDomainPanel = domainMap.get(error.getDomainType());
			if (errorsDomainPanel == null) {
				errorsDomainPanel = new Panel(error.getDomainType().getDesc());
				errorsDomainPanel.setSizeFull();
				domainMap.put(error.getDomainType(), errorsDomainPanel);
				VerticalLayout errorsDomainLayout = new VerticalLayout();
				errorsDomainLayout.setSizeFull();
				errorsDomainLayout.setMargin(true);
				errorsDomainLayout.setSpacing(true);
				errorsDomainPanel.setContent(errorsDomainLayout);
				contentLayout.addComponent(errorsDomainPanel);
			}
			
			Button btnError = new Button(error.getMessage());
			btnError.setIcon(new ThemeResource("../nkr-default/icons/16/error_2.png"));
			btnError.setStyleName(Reindeer.BUTTON_LINK);
			
			((VerticalLayout) errorsDomainPanel.getContent()).addComponent(btnError);
		}
	}
}
