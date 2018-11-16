package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyAddress;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact phone details in collection left panel
 * @author uhout.cheng
 */
public class CollectionContactsPhoneDetailPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = -7353287992849934945L;

	private VerticalLayout borrowerLayout;
	private VerticalLayout spouseLayout;
	private List<VerticalLayout> guarantorLayouts;
	private List<VerticalLayout> referenceLayouts;
	
	private Map<EApplicantType, List<String>> contactInfos;
	private Map<EApplicantType, List<Address>> addresses;
	private Button btnSeeDetail;
	
	private HorizontalLayout guarantorLayout;
	private HorizontalLayout referenceLayout;
	
	/**
	 * 
	 */
	public CollectionContactsPhoneDetailPanel() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		borrowerLayout = getVerticalLayout("borrower");
		spouseLayout = getVerticalLayout("spouse");
		guarantorLayout = new HorizontalLayout();
		guarantorLayouts = new ArrayList<VerticalLayout>();
		referenceLayout = new HorizontalLayout();
		referenceLayouts = new ArrayList<VerticalLayout>();
		btnSeeDetail = ComponentLayoutFactory.getButtonStyle(I18N.message("see.all"), FontAwesome.TABLE, 70, "btn btn-success button-small");
		btnSeeDetail.addClickListener(this);
		
		HorizontalLayout mainLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		mainLayout.addComponent(borrowerLayout);
		mainLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
		mainLayout.addComponent(spouseLayout);
		mainLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
		mainLayout.addComponent(guarantorLayout);
		mainLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
		mainLayout.addComponent(referenceLayout);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, true, false), true);
		verLayout.addComponent(mainLayout);
		verLayout.addComponent(btnSeeDetail);
		verLayout.setComponentAlignment(btnSeeDetail, Alignment.BOTTOM_RIGHT);
		
		Panel mainPanel = new Panel(verLayout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @param applicants
	 */
	public void assignValues(Map<EApplicantType, Applicant> applicants) {
		borrowerLayout.removeAllComponents();
		spouseLayout.removeAllComponents();
		guarantorLayout.removeAllComponents();
		guarantorLayouts.clear();
		referenceLayout.removeAllComponents();
		referenceLayouts.clear();
		
		contactInfos = new LinkedHashMap<>();
		addresses = new LinkedHashMap<>();
		
		if (!applicants.isEmpty()) {
			for (EApplicantType key : applicants.keySet()) {
				Applicant applicant = applicants.get(key);
				Individual individual = applicant.getIndividual();
				Company company = applicant.getCompany();
				if (EApplicantType.C.equals(key)) {
					if (individual != null) {
						// borrower
						List<IndividualContactInfo> indContactInfos = individual.getIndividualContactInfos();
						if (indContactInfos != null && !indContactInfos.isEmpty()) {
							for (IndividualContactInfo indContactInfo : indContactInfos) {
								ContactInfo contactInfo = indContactInfo.getContactInfo();
								if (contactInfo != null) {
									if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo()) 
											|| ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
										if (StringUtils.isNotEmpty(contactInfo.getValue())) {
											borrowerLayout.addComponent(getLabelValue(contactInfo.getValue()));
										}
										setPhones(EApplicantType.C, individual, null, contactInfo);
									}
								}
							}
						}
			
						setIndividualAddressReference(individual, EApplicantType.C);
					} else if (company != null) {
						if (StringUtils.isNotEmpty(company.getMobile())) {
							borrowerLayout.addComponent(getLabelValue(company.getMobile()));
						}
						if (StringUtils.isNotEmpty(company.getTel())) {
							borrowerLayout.addComponent(getLabelValue(company.getTel()));
						}
						setPhones(EApplicantType.C, null, company, null);
						setCompanyAddress(company, EApplicantType.C);
					}
				} else if (EApplicantType.G.equals(key)) {
					if (individual != null) {
						// GUARANTOR
						List<IndividualContactInfo> indContactInfos = individual.getIndividualContactInfos();
						if (indContactInfos != null && !indContactInfos.isEmpty()) {
							for (IndividualContactInfo indContactInfo : indContactInfos) {
								VerticalLayout guaLayout = getVerticalLayout("guarantor");
								guarantorLayouts.add(guaLayout);
								ContactInfo contactInfo = indContactInfo.getContactInfo();
								if (contactInfo != null) {
									if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo()) 
											|| ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
										if (StringUtils.isNotEmpty(contactInfo.getValue())) {
											guaLayout.addComponent(getLabelValue(contactInfo.getValue()));
										}
										setPhones(EApplicantType.G, individual, null, contactInfo);
									}
								}
							}
						}
						
						setIndividualAddressReference(individual, EApplicantType.G);
					} else if (company != null) {
						VerticalLayout guaLayout = getVerticalLayout("guarantor");
						if (StringUtils.isNotEmpty(company.getMobile())) {
							guaLayout.addComponent(getLabelValue(company.getMobile()));
						}
						if (StringUtils.isNotEmpty(company.getTel())) {
							guaLayout.addComponent(getLabelValue(company.getTel()));
						}
						guarantorLayouts.add(guaLayout);
						setPhones(EApplicantType.G, null, company, null);
						setCompanyAddress(company, EApplicantType.G);
					}
				}
			}
		}
		
		if (!referenceLayouts.isEmpty()) {
			for (int i = 0; i < referenceLayouts.size(); i++) {
				referenceLayout.addComponent(referenceLayouts.get(i));
				if (i != (referenceLayouts.size() - 1)) {
					referenceLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
				}
			}
		}
		
		if (!guarantorLayouts.isEmpty()) {
			for (int i = 0; i < guarantorLayouts.size(); i++) {
				guarantorLayout.addComponent(guarantorLayouts.get(i));
				if (i != (guarantorLayouts.size() - 1)) {
					guarantorLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param appType
	 * @param individual
	 * @param company
	 * @param contactInfo
	 */
	private void setPhones(EApplicantType appType, Individual individual, Company company, ContactInfo contactInfo) {
		if (!contactInfos.containsKey(appType)) {
			contactInfos.put(appType, new ArrayList<String>());
		}
		if (individual != null) {
			if (contactInfo != null && StringUtils.isNoneEmpty(contactInfo.getValue())) {
				contactInfos.get(appType).add(contactInfo.getValue());
			}
		} else if (company != null) {
			if (StringUtils.isNotEmpty(company.getMobile())) {
				contactInfos.get(appType).add(company.getMobile());
			}
			if (StringUtils.isNotEmpty(company.getTel())) {
				contactInfos.get(appType).add(company.getTel());
			}	
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSeeDetail)) {
			ContactPhoneAddressPopup window = new ContactPhoneAddressPopup();
			window.assignValues(contactInfos, addresses);
			UI.getCurrent().addWindow(window);
		}
	}
	
	/**
	 * @return the addresses
	 */
	public Map<EApplicantType, List<Address>> getAddresses() {
		return addresses;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private Label getLabelValue(String value) {
		Label label = ComponentFactory.getHtmlLabel("<b>" + value + "</b>");
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private VerticalLayout getVerticalLayout(String caption) {
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verLayout.setCaption(I18N.message(caption));
		return verLayout;
	}
	
	/**
	 * 
	 * @param individual
	 * @param appType
	 */
	private void setIndividualAddressReference(Individual individual, EApplicantType appType) {
		// Address
		List<IndividualAddress> indAddresses = individual.getIndividualAddresses();
		if (indAddresses != null && !indAddresses.isEmpty()) {
			for (IndividualAddress indAddress : indAddresses) {
				Address address = indAddress.getAddress();
				if (address != null) {
					if (!addresses.containsKey(appType)) {
						addresses.put(appType, new ArrayList<Address>());
					}
					addresses.get(appType).add(address);
				}
			}
		}
		
		// Reference
		List<IndividualReferenceInfo> indReferenceInfos = individual.getIndividualReferenceInfos();
		if (indReferenceInfos != null && !indReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfo indReferenceInfo : indReferenceInfos) {
				List<IndividualReferenceContactInfo> indRefContactInfos = indReferenceInfo.getIndividualReferenceContactInfos();
				if (indRefContactInfos != null && !indRefContactInfos.isEmpty()) {
					VerticalLayout refLayout = getVerticalLayout("reference");
					referenceLayouts.add(refLayout);
					for (IndividualReferenceContactInfo indRefContactInfo : indRefContactInfos) {
						ContactInfo contactInfo = indRefContactInfo.getContactInfo();
						if (contactInfo != null) {
							if (ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo()) 
									|| ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo())) {
								refLayout.addComponent(getLabelValue(contactInfo.getValue()));
								if (!contactInfos.containsKey(EApplicantType.O)) {
									contactInfos.put(EApplicantType.O, new ArrayList<String>());
								}
								contactInfos.get(EApplicantType.O).add(contactInfo.getValue());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param company
	 * @param appType
	 */
	private void setCompanyAddress(Company company, EApplicantType appType) {
		// Company address
		List<CompanyAddress> comAddresses = company.getCompanyAddresses();
		if (comAddresses != null && !comAddresses.isEmpty()) {
			for (CompanyAddress comAddress : comAddresses) {
				Address address = comAddress.getAddress();
				if (address != null) {
					if (!addresses.containsKey(appType)) {
						addresses.put(appType, new ArrayList<Address>());
					}
					addresses.get(appType).add(address);
				}
			}
		}
	}
	
}
