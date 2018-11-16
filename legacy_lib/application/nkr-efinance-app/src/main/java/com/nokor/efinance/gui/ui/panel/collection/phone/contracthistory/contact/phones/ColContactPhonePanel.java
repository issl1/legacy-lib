package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.phones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ContactInfoDetailLayout;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.ContactInfoTitleLayout;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Collection contact phone panel
 * @author uhout.cheng
 */
public class ColContactPhonePanel extends AbstractControlPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 1296699331996453864L;

	private VerticalLayout layout;
	
	private Contract contract;
	
	private ColContractHistoryFormPanel contractHistoriesFormPanel;
	
	/**
	 * 
	 * @param contractHistoriesFormPanel
	 */
	public ColContactPhonePanel(ColContractHistoryFormPanel contractHistoriesFormPanel) {
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
					lesseeLayout.setContactPhonePanel(this);
					lesseeLayout.setPhone(true);
					lesseeLayout.setApplicantId(applicant.getId());
					lesseeLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
					layout.addComponent(lesseeLayout);
					
					if (individual != null) {
						setIndividualLayout(individual.getIndividualContactInfos());
						
						Employment employment = individual.getCurrentEmployment();
						if (employment != null) {
							layout.addComponent(setDetailLayoutEmployment(1, 0, 0, "work.place", employment));
						}
					
						indRefInfos = individual.getIndividualReferenceInfos();

					} else {
						setCompanyLayout(company.getCompanyContactInfos());
					}
				} else if (EApplicantType.G.equals(key)) {
					ContactInfoTitleLayout guarantorLayout = new ContactInfoTitleLayout(1, 0, 0, "guarantor", applicant.getNameLocale(),
							individual, IndividualContactInfo.createInstance(), company, CompanyContactInfo.createInstance());
					guarantorLayout.setContactPhonePanel(this);
					guarantorLayout.setPhone(true);
					guarantorLayout.setApplicantId(applicant.getId());
					guarantorLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
					layout.addComponent(guarantorLayout);
					guarantorLayout.setPhone(true);
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
				referenceLayout.setContactPhonePanel(this);
				referenceLayout.setPhone(true);
				referenceLayout.setContractHistoriesFormPanel(contractHistoriesFormPanel);
				layout.addComponent(referenceLayout);
				List<IndividualReferenceContactInfo> indRefConInfos =  indRefInfos.get(0).getIndividualReferenceContactInfos();
				if (indRefConInfos != null && !indRefConInfos.isEmpty()) {
					List<ContactInfo> refConInfos = getPrimariesReference(indRefConInfos);
					if (!refConInfos.isEmpty()) {
						int row = 0;
						for (ContactInfo refConInfo : refConInfos) {
							ContactInfoDetailLayout conInfoDetailLayout = new ContactInfoDetailLayout(refConInfos.size(), 0, row++, "primary", refConInfo,
									contractHistoriesFormPanel);
							conInfoDetailLayout.setConPhonePanel(this);
							layout.addComponent(conInfoDetailLayout);
						}
					}
				}
			}
		}
		
		Dealer dealer = contract.getDealer();
		GridLayout dealerLayout = new GridLayout(2, 5);
		Label lblDealerShipName = ComponentFactory.getHtmlLabel("<b>" + I18N.message("dealer.ship.name") + "</b>");
		Label lblCheckerName = ComponentFactory.getHtmlLabel("<b>" + I18N.message("checker.name") + "</b>");
		Label lblPhoneNumber = ComponentFactory.getHtmlLabel("<b>" + I18N.message("phone") + "</b>");
		TextField txtCheckerName = ComponentFactory.getTextField(100, 140);
		txtCheckerName.setEnabled(false);
		TextField txtPhoneNumber = ComponentFactory.getTextField(100, 140);
		txtPhoneNumber.setEnabled(false);
		lblDealerShipName.setWidth(100, Unit.PIXELS);
		TextField txtDealerShipName = ComponentFactory.getTextField(100, 140);
		txtDealerShipName.setEnabled(false);
		txtDealerShipName.setValue(getDefaultString(dealer == null ? StringUtils.EMPTY : dealer.getNameLocale()));
		txtCheckerName.setValue(getDefaultString(contract == null ? StringUtils.EMPTY : contract.getCheckerName()));
		txtPhoneNumber.setValue(getDefaultString(contract == null ? StringUtils.EMPTY : contract.getCheckerPhoneNumber()));
		dealerLayout.addComponent(lblDealerShipName, 0, 0);
		dealerLayout.addComponent(txtDealerShipName, 1, 0);
		dealerLayout.addComponent(ComponentFactory.getSpaceHeight(6, Unit.PIXELS), 1, 1);
		dealerLayout.addComponent(lblCheckerName, 0, 2);
		dealerLayout.addComponent(txtCheckerName, 1, 2);
		dealerLayout.addComponent(ComponentFactory.getSpaceHeight(6, Unit.PIXELS), 1, 3);
		dealerLayout.addComponent(lblPhoneNumber, 0, 4);
		dealerLayout.addComponent(txtPhoneNumber, 1, 4);
		dealerLayout.setComponentAlignment(lblDealerShipName, Alignment.MIDDLE_LEFT);
		dealerLayout.setComponentAlignment(lblCheckerName, Alignment.MIDDLE_LEFT);
		dealerLayout.setComponentAlignment(lblPhoneNumber, Alignment.MIDDLE_LEFT);
		layout.addComponent(dealerLayout);
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
	 * @param caption
	 */
	private void setContactInfoLayout(List<ContactInfo> contactInfos, String caption) {
		if (!contactInfos.isEmpty()) {
			int row = 0;
			for (ContactInfo gPriConInfo : contactInfos) {
				ContactInfoDetailLayout conInfoDetailLayout = new ContactInfoDetailLayout(contactInfos.size(), 0, row++, caption, gPriConInfo,
						contractHistoriesFormPanel);
				conInfoDetailLayout.setConPhonePanel(this);
				layout.addComponent(conInfoDetailLayout);
			}	
		}
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	private void setIndividualLayout(List<IndividualContactInfo> individualContactInfos) {
		List<ContactInfo> indPrimariesContactInfo = getPrimaries(individualContactInfos);
		setContactInfoLayout(indPrimariesContactInfo, "primary");
		
		List<ContactInfo> indSecondariesContactInfo = getSecondariesPhone(individualContactInfos);
		setContactInfoLayout(indSecondariesContactInfo, "secondary");
	}
	
	/**
	 * 
	 * @param companyContactInfos
	 */
	private void setCompanyLayout(List<CompanyContactInfo> companyContactInfos) {
		List<ContactInfo> comPrimariesContactInfo = getPrimariesContactInfo(companyContactInfos);
		setContactInfoLayout(comPrimariesContactInfo, "primary");
		
		List<ContactInfo> comSecondariesContactInfo = getSecondariesContactInfo(companyContactInfos);
		setContactInfoLayout(comSecondariesContactInfo, "secondary");
	}

	/**
	 * 
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param employment
	 */
	private GridLayout setDetailLayoutEmployment(int nbRows, int iCol, int iRow, String caption, Employment employment) {
		GridLayout gridLayout = new GridLayout(4, nbRows);
		Label lblCaption = null;
		if (nbRows > 1) {
			lblCaption = ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + (iRow + 1));
		} else {
			lblCaption = ComponentFactory.getLabel(caption);
		}
		lblCaption.setWidth(100, Unit.PIXELS);
		TextField txtValue = ComponentFactory.getTextField(10, 140);
		txtValue.setEnabled(false);
		txtValue.setValue(getDefaultString(employment.getWorkPhone()));
		Button btnAction = new Button();
		btnAction.setIcon(FontAwesome.EDIT);
		btnAction.setStyleName(Reindeer.BUTTON_LINK);
		
		gridLayout.addComponent(lblCaption, iCol++, iRow);
		gridLayout.addComponent(txtValue, iCol++, iRow);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, iRow);
		gridLayout.addComponent(btnAction, iCol++, iRow);
		gridLayout.setComponentAlignment(lblCaption, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnAction, Alignment.MIDDLE_CENTER);
		
		btnAction.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1251936706680945654L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (!txtValue.isEnabled()) {
					txtValue.setEnabled(true);
					btnAction.setIcon(FontAwesome.SAVE);
				} else {
					employment.setWorkPhone(txtValue.getValue());
					try {
						if (StringUtils.isNotEmpty(txtValue.getValue())) {
							EMPL_SRV.update(employment);
							ComponentLayoutFactory.displaySuccessfullyMsg();
							txtValue.setEnabled(false);
							btnAction.setIcon(FontAwesome.EDIT);
							txtValue.setRequired(false);
						} else {
							txtValue.setRequired(true);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		});
		return gridLayout;
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
	private List<ContactInfo> getPrimaries(List<IndividualContactInfo> individualContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : individualContactInfos) {
				ContactInfo info = indContactInfo.getContactInfo();
				if ((ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))
						&& info.isPrimary()) {
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
	private List<ContactInfo> getPrimariesReference(List<IndividualReferenceContactInfo> referenceContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (referenceContactInfos != null && !referenceContactInfos.isEmpty()) {
			for (IndividualReferenceContactInfo indContactInfo : referenceContactInfos) {
				ContactInfo info = indContactInfo.getContactInfo();
				if ((ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))
						&& info.isPrimary()) {
					contactInfos.add(info);
				}
			}
		}
		return contactInfos;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 * @return
	 */
	private List<ContactInfo> getSecondariesPhone(List<IndividualContactInfo> individualContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (individualContactInfos != null && !individualContactInfos.isEmpty()) {
			for (IndividualContactInfo indContactInfo : individualContactInfos) {
				ContactInfo info = indContactInfo.getContactInfo();
				if ((ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))
						&& !info.isPrimary()) {
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
	private List<ContactInfo> getPrimariesContactInfo(List<CompanyContactInfo> companyContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (companyContactInfos != null && !companyContactInfos.isEmpty()) {
			for (CompanyContactInfo companyContactInfo : companyContactInfos) {
				ContactInfo info = companyContactInfo.getContactInfo();
				if ((ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))
						&& info.isPrimary()) {
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
	private List<ContactInfo> getSecondariesContactInfo(List<CompanyContactInfo> companyContactInfos) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		if (companyContactInfos != null && !companyContactInfos.isEmpty()) {
			for (CompanyContactInfo companyContactInfo : companyContactInfos) {
				ContactInfo info = companyContactInfo.getContactInfo();
				if ((ETypeContactInfo.LANDLINE.equals(info.getTypeInfo())
						|| ETypeContactInfo.MOBILE.equals(info.getTypeInfo()))
						&& !info.isPrimary()) {
					contactInfos.add(info);
				}
			}
		}
		return contactInfos;
	}
	
}
