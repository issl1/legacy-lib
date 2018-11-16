package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.other;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ContactInfoDetailLayout;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ContactInfoTitleLayout;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Collection contact other panel
 * @author uhout.cheng
 */
public class ColContactOtherPanel extends AbstractControlPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 7656745498047208394L;
	
	private VerticalLayout layout;
	
	private Contract contract;
	
	private ColContractHistoryFormPanel contractHistoriesFormPanel;
	
	/**
	 * 
	 * @param contractHistoriesFormPanel
	 */
	public ColContactOtherPanel(ColContractHistoryFormPanel contractHistoriesFormPanel) {
		this.contractHistoriesFormPanel = contractHistoriesFormPanel;
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, true, false));
		
		Panel mainPanel = new Panel(layout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		setMargin(new MarginInfo(true, false, false, true));
		addComponent(mainPanel);
		setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		Map<EApplicantType, Applicant> applicants = new LinkedHashMap<>();
		applicants.put(EApplicantType.C, contract.getApplicant());
		ContractApplicant conApp = contract.getContractApplicant(EApplicantType.G);
		if (conApp != null) {
			applicants.put(EApplicantType.G, conApp.getApplicant());
		}
		
		List<IndividualReferenceInfo> indRefInfos = new ArrayList<IndividualReferenceInfo>();
		
		if (!applicants.isEmpty()) {
			for (EApplicantType key : applicants.keySet()) {
				Applicant applicant = applicants.get(key);
				Individual individual = applicant.getIndividual();
				Company company = applicant.getCompany();
				
				if (EApplicantType.C.equals(key)) {
					ContactInfoTitleLayout lesseeLayout = new ContactInfoTitleLayout(1, 0, 0, "lessee", applicant.getNameLocale(),
							individual, IndividualContactInfo.createInstance(), company, CompanyContactInfo.createInstance());
					lesseeLayout.setContactOtherPanel(this);;
					lesseeLayout.setPhone(false);
					lesseeLayout.setApplicantId(applicant.getId());
					lesseeLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
					layout.addComponent(lesseeLayout);
					
					if (individual != null) {
						setIndividualLayout(individual.getIndividualContactInfos());
					
						indRefInfos = individual.getIndividualReferenceInfos();

					} else {
						setCompanyLayout(company.getCompanyContactInfos());
					}
				} else if (EApplicantType.G.equals(key)) {
					ContactInfoTitleLayout guarantorLayout = new ContactInfoTitleLayout(1, 0, 0, "guarantor", applicant.getNameLocale(),
							individual, IndividualContactInfo.createInstance(), company, CompanyContactInfo.createInstance());
					guarantorLayout.setContactOtherPanel(this);
					guarantorLayout.setPhone(false);
					guarantorLayout.setApplicantId(applicant.getId());
					guarantorLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
					layout.addComponent(guarantorLayout);
					if (individual != null) {
						setIndividualLayout(individual.getIndividualContactInfos());
					} else {
						setCompanyLayout(company.getCompanyContactInfos());
					}
				}
			}	
			
			if (indRefInfos != null && !indRefInfos.isEmpty()) {
				ContactInfoTitleLayout referenceLayout = new ContactInfoTitleLayout(1, 0, 0, "reference", indRefInfos.get(0).getFullNameEn(),
						indRefInfos.get(0), IndividualReferenceContactInfo.createInstance());
				referenceLayout.setContactOtherPanel(this);
				referenceLayout.setPhone(false);
				referenceLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
				layout.addComponent(referenceLayout);
				List<IndividualReferenceContactInfo> indRefConInfos =  indRefInfos.get(0).getIndividualReferenceContactInfos();
				if (indRefConInfos != null && !indRefConInfos.isEmpty()) {
					List<ContactInfo> refConInfos = getOtherReferenceConInfos(indRefConInfos);
					if (!refConInfos.isEmpty()) {
						int row = 0;
						for (ContactInfo refConInfo : refConInfos) {
							ContactInfoDetailLayout conInfoDetailLayout = new ContactInfoDetailLayout(refConInfos.size(), 0, row++,
									refConInfo.getTypeInfo().getDescLocale(), refConInfo, contractHistoriesFormPanel);
							conInfoDetailLayout.setConOtherPanel(this);
							layout.addComponent(conInfoDetailLayout);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void refresh() {
		if (this.contract != null) {
			CONT_SRV.refresh(this.contract);
			assignValues(this.contract);
		}
	}
	
	/**
	 * 
	 * @param contactInfos
	 */
	private void setContactInfoLayout(List<ContactInfo> contactInfos) {
		if (!contactInfos.isEmpty()) {
			int row = 0;
			for (ContactInfo conInfo : contactInfos) {
				ContactInfoDetailLayout conInfoDetailLayout = new ContactInfoDetailLayout(contactInfos.size(), 0, row++,
						conInfo.getTypeInfo().getDescLocale(), conInfo, contractHistoriesFormPanel);
				conInfoDetailLayout.setConOtherPanel(this);
				layout.addComponent(conInfoDetailLayout);
			}	
		}
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	private void setIndividualLayout(List<IndividualContactInfo> individualContactInfos) {
		List<ContactInfo> indPrimariesContactInfo = getOtherContactInfos(individualContactInfos);
		setContactInfoLayout(indPrimariesContactInfo);
	}
	
	/**
	 * 
	 * @param companyContactInfos
	 */
	private void setCompanyLayout(List<CompanyContactInfo> companyContactInfos) {
		List<ContactInfo> comPrimariesContactInfo = getOtherCompanyContactInfos(companyContactInfos);
		setContactInfoLayout(comPrimariesContactInfo);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		layout.removeAllComponents();
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 * @return
	 */
	private List<ContactInfo> getOtherContactInfos(List<IndividualContactInfo> individualContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : individualContactInfos) {
				ContactInfo info = indContactInfo.getContactInfo();
				if ((!ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						&& !ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))) {
					contactInfos.add(info);
				}
			}
		}
		return contactInfos;
	}
	
	/**
	 * 
	 * @param referenceContactInfos
	 * @return
	 */
	private List<ContactInfo> getOtherReferenceConInfos(List<IndividualReferenceContactInfo> referenceContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (referenceContactInfos != null && !referenceContactInfos.isEmpty()) {
			for (IndividualReferenceContactInfo indContactInfo : referenceContactInfos) {
				ContactInfo info = indContactInfo.getContactInfo();
				if ((!ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						&& !ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))) {
					contactInfos.add(info);
				}
			}
		}
		return contactInfos;
	}
	
	/**
	 * 
	 * @param companyContactInfos
	 * @return
	 */
	private List<ContactInfo> getOtherCompanyContactInfos(List<CompanyContactInfo> companyContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (companyContactInfos != null && !companyContactInfos.isEmpty()) {
			for (CompanyContactInfo companyContactInfo : companyContactInfos) {
				ContactInfo info = companyContactInfo.getContactInfo();
				if ((!ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						&& !ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))) {
					contactInfos.add(info);
				}
			}
		}
		return contactInfos;
	}
	
}
