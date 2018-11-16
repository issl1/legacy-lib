package com.nokor.efinance.core.applicant.panel.applicant.individual;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.panel.contact.dealer.ContactDealerPanel;
import com.nokor.efinance.core.applicant.panel.contact.dealer.DealerPanel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
/**
 * Contact panel
 * @author ly.youhort
 */
public class ContactPanel extends VerticalLayout implements ClickListener {
	
	/** */
	private static final long serialVersionUID = 4370636624280316255L;
	
	private ContactInformationPanel contactLesseePanel;
	
	private ContactInformationPanel contactSpousePanel;
	
	private ContactInformationPanel contactGuarantorPanel;
				
	private ContactReferencePanel contactReferencePanel;
	
	private ContactDealerPanel contactDealerPanel;
	
	private VerticalLayout contentLayout;
	private VerticalLayout contactLayout;

	private Contract contract;
	
	/**
	 * 
	 */
	public ContactPanel() {
		setMargin(false);
		setSpacing(true);
		setSizeFull();
		addComponent(createForm());
	}
		
	/** */
	private Component createForm() {
		
		contactLesseePanel = new ContactInformationPanel("lessee");
		
		contactSpousePanel = new ContactInformationPanel("spouse");
		
		contactGuarantorPanel = new ContactInformationPanel("guarantor");
				
		contactReferencePanel = new ContactReferencePanel();
		
		contactDealerPanel = new ContactDealerPanel();
		
		contactLesseePanel.getBtnFullDetail().addClickListener(this);
		contactSpousePanel.getBtnFullDetail().addClickListener(this);
		contactGuarantorPanel.getBtnFullDetail().addClickListener(this);
		contactReferencePanel.getBtnFullDetail().addClickListener(this);
		contactDealerPanel.getBtnFullDetail().addClickListener(this);
		
		contentLayout = new VerticalLayout();
		
		contactLayout = new VerticalLayout();
		contactLayout.setSpacing(true);
		contactLayout.addComponent(contactLesseePanel);
		contactLayout.addComponent(contactSpousePanel);
		contactLayout.addComponent(contactReferencePanel);
		contactLayout.addComponent(contactGuarantorPanel);
		contactLayout.addComponent(contactDealerPanel);
		
		contentLayout.addComponent(contactLayout);
		
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		
		contactLesseePanel.assignValues(contract.getApplicant(), EApplicantType.C);		
		contactReferencePanel.assignValues(contract.getApplicant());
		contactDealerPanel.assignValues(contract.getDealer());
			
		ContractApplicant spouseApplicant = contract.getContractApplicant(EApplicantType.S);
		contactSpousePanel.setVisible(spouseApplicant != null);
		if (spouseApplicant != null) {
			Applicant spouse = spouseApplicant.getApplicant();
			if (spouse.getIndividual() != null) {
				contactSpousePanel.assignValues(spouse, EApplicantType.S);
			} 
		} 
		
		ContractApplicant guarantorApplicant = contract.getContractApplicant(EApplicantType.G);
		contactGuarantorPanel.setVisible(guarantorApplicant != null);
		if (guarantorApplicant != null) {
			Applicant guarantor = guarantorApplicant.getApplicant();
			if (guarantor.getIndividual() != null) {
				contactGuarantorPanel.assignValues(guarantor, EApplicantType.G);
			} 
		}
	}
		
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		contentLayout.removeAllComponents();
		if (event.getButton() == contactDealerPanel.getBtnFullDetail()) {
			DealerPanel dealerPanel = new DealerPanel();
			dealerPanel.getBtnBack().addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = 9045012181104401588L;
				
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					assignValues(contract);
					dealerPanel.reset();
					contentLayout.removeAllComponents();
					contentLayout.addComponent(contactLayout);
				}
			});
			dealerPanel.assignValues(contract);
			contentLayout.addComponent(dealerPanel);
		} else if (event.getButton() == contactReferencePanel.getBtnFullDetail()) {
			ReferencePanel referencePanel = new ReferencePanel();
			referencePanel.getBtnBack().addClickListener(new ClickListener() {				
				/** */
				private static final long serialVersionUID = -8631750287212234174L;
				
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					assignValues(contract);
					contentLayout.removeAllComponents();
					contentLayout.addComponent(contactLayout);
				}
			});
			referencePanel.assignValues(contract.getApplicant());
			contentLayout.addComponent(referencePanel);
		} else if (event.getButton() == contactLesseePanel.getBtnFullDetail()) {
			ApplicantPanel lesseePanel = new ApplicantPanel();
			lesseePanel.getBtnBack().addClickListener(new ClickListener() {				
				/** */
				private static final long serialVersionUID = -487919221705352254L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					assignValues(contract);
					contentLayout.removeAllComponents();
					contentLayout.addComponent(contactLayout);
				}
			});
			lesseePanel.assignValues(contract.getApplicant(), EApplicantType.C);
			contentLayout.addComponent(lesseePanel);
		} else if (event.getButton() == contactSpousePanel.getBtnFullDetail()) {
			ApplicantPanel spousePanel = new ApplicantPanel(); 
			spousePanel.getBtnBack().addClickListener(new ClickListener() {				
				/** */
				private static final long serialVersionUID = 7042104162135453061L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					assignValues(contract);
					contentLayout.removeAllComponents();
					contentLayout.addComponent(contactLayout);
				}
			});
			spousePanel.assignValues(contract.getContractApplicant(EApplicantType.S).getApplicant(), EApplicantType.S);
			contentLayout.addComponent(spousePanel);
		} else if (event.getButton() == contactGuarantorPanel.getBtnFullDetail()) {
			ApplicantPanel guarantorPanel = new ApplicantPanel();
			guarantorPanel.getBtnBack().addClickListener(new ClickListener() {				
				/** */
				private static final long serialVersionUID = -6565188569589319377L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					assignValues(contract);
					contentLayout.removeAllComponents();
					contentLayout.addComponent(contactLayout);
				}
			});
			guarantorPanel.assignValues(contract.getContractApplicant(EApplicantType.G).getApplicant(), EApplicantType.G);
			contentLayout.addComponent(guarantorPanel);
		}
	}
			
	/**
	 * Reset panel
	 */
	public void reset() {
		contactLesseePanel.reset();
		contactSpousePanel.reset();
		contactGuarantorPanel.reset();
		contactReferencePanel.reset();;
		contactDealerPanel.reset();
	}
}
