package com.nokor.efinance.core.applicant.panel.applicant.company;

import com.nokor.efinance.core.contract.model.Contract;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
/**
 * Contact panel
 * @author ly.youhort
 */
public class ContactPanel extends VerticalLayout implements ClickListener {
	
	private static final long serialVersionUID = 966783199811064295L;
	
	private VerticalLayout mainContactLayout;

	private ContactInformationPanel lesseePanel;
	private ContactInformationPanel guarantorPanel;
	
	/**
	 * 
	 */
	public ContactPanel() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		addComponent(createForm());
	}
	 
	
	/** */
	private Component createForm() {
		
		lesseePanel = new ContactInformationPanel("lessee");
		guarantorPanel = new ContactInformationPanel("guarantor");
		
		mainContactLayout = new VerticalLayout();
		mainContactLayout.addComponent(lesseePanel);
		mainContactLayout.addComponent(guarantorPanel);
		
		return mainContactLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		lesseePanel.assignValues(contract.getApplicant().getCompany());
		guarantorPanel.assignValues(contract.getApplicant().getCompany());		
	}
	
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		lesseePanel.reset();
		guarantorPanel.reset();
	}
}
