package com.nokor.efinance.core.applicant.panel.contact;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.contract.model.Contract;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
/**
 * Contact panel
 * @author ly.youhort
 */
public class ContactPanel extends VerticalLayout {
	
	private static final long serialVersionUID = 966783199811064295L;
	
	private VerticalLayout mainLayout;
	
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
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);		
		return mainLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		mainLayout.removeAllComponents();
		if (EApplicantCategory.INDIVIDUAL.equals(contract.getApplicant().getApplicantCategory())) {
			com.nokor.efinance.core.applicant.panel.applicant.individual.ContactPanel individualContactPanel = new com.nokor.efinance.core.applicant.panel.applicant.individual.ContactPanel();
			individualContactPanel.assignValues(contract);
			mainLayout.addComponent(individualContactPanel);
		} else if (EApplicantCategory.COMPANY.equals(contract.getApplicant().getApplicantCategory())) {
			com.nokor.efinance.core.applicant.panel.applicant.company.ContactPanel companyContactPanel = new com.nokor.efinance.core.applicant.panel.applicant.company.ContactPanel();
			companyContactPanel.assignValues(contract);
			mainLayout.addComponent(companyContactPanel);
		} 
	}
		
	/**
	 * Reset panel
	 */
	public void reset() {
		mainLayout.removeAllComponents();
	}
}
