package com.nokor.efinance.core.applicant.panel.guarantor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.panel.CurrentEmploymentPanel;
import com.nokor.efinance.core.applicant.panel.IdentityPanel;
import com.nokor.efinance.core.applicant.panel.SecondaryEmploymentPanel;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ERelationship;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Guarantor panel
 * @author ly.youhort
 */
public class GuarantorPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = -8442994066145205792L;
	
	private IdentityPanel identityPanel;
	private AddressPanel addressFormPanel;
	private CurrentEmploymentPanel currentEmploymentPanel;
	private SecondaryEmploymentPanel secondaryEmploymentPanel;
	private OtherInformationPanel otherInformationPanel;
	
	private TabSheet guarantorTabSheet;
	private VerticalLayout identityTab;
	private VerticalLayout employmentTab;
	private VerticalLayout otherInformationTab;
	
	private ERefDataComboBox<ERelationship> cbxRelationship;
	private CheckBox cbSameApplicantAddress;
	
	private Applicant mainApplicant;
	private Applicant guarantor;
	
	public Button btnChangeGuarantor;
	
	/**
	 * @param quotationFormPanel
	 */
	public GuarantorPanel(/*QuotationFormPanel quotationFormPanel*/) {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		
		cbxRelationship = new ERefDataComboBox<ERelationship>(I18N.message("relationship.with.applicant"), ERelationship.class);
		cbxRelationship.setRequired(true);
		
		cbSameApplicantAddress = new CheckBox(I18N.message("live.with.applicant"));
		cbSameApplicantAddress.setImmediate(true);
		cbSameApplicantAddress.setValue(false);
		
		btnChangeGuarantor = new NativeButton(I18N.message("change.guarantor"));
		btnChangeGuarantor.setIcon(new ThemeResource("../nkr-default/icons/16/edit.png"));
		btnChangeGuarantor.setVisible(false);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(new FormLayout(cbxRelationship));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
		horizontalLayout.addComponent(new FormLayout(cbSameApplicantAddress));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(100, Unit.PIXELS));
		horizontalLayout.addComponent(btnChangeGuarantor);
		btnChangeGuarantor.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 233733154766622576L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.change.guarantor"),
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				                	// quotationFormPanel.changeGuarantor();
				                }
				            }
				    });
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
			}
		});
		
		guarantorTabSheet = new TabSheet();
		guarantorTabSheet.setSizeFull();
		
		identityTab = new VerticalLayout();
		identityPanel = new IdentityPanel("guarantorIdentity");		
		final Panel addressPanel = new Panel(I18N.message("current.address"));
        addressFormPanel = new AddressPanel(true, ETypeAddress.HOME, "addressHorizontal");
        addressFormPanel.setMargin(true);
        addressPanel.setContent(addressFormPanel);
        
        identityTab.setSpacing(true);
        identityTab.setMargin(true);
        identityTab.addComponent(identityPanel);
        identityTab.addComponent(addressPanel);
        
        employmentTab = new VerticalLayout();
        currentEmploymentPanel = new CurrentEmploymentPanel();
        final Panel currentEmploymentLayout = new Panel(I18N.message("current.employment"));
		currentEmploymentLayout.setContent(currentEmploymentPanel);
        
		secondaryEmploymentPanel = new SecondaryEmploymentPanel("secondaryEmploymentHorizontal");
		final Panel secondaryEmploymentLayout = new Panel(I18N.message("secondary.employment"));
		secondaryEmploymentLayout.setContent(secondaryEmploymentPanel);
		
		employmentTab.setSpacing(true);
		employmentTab.setMargin(true);
		employmentTab.addComponent(currentEmploymentLayout);
		employmentTab.addComponent(secondaryEmploymentLayout);
		
		otherInformationTab = new VerticalLayout();
		otherInformationTab.setSpacing(true);
		otherInformationTab.setMargin(true);
		otherInformationPanel = new OtherInformationPanel();
		otherInformationTab.addComponent(otherInformationPanel);
		
		guarantorTabSheet.addTab(identityTab, I18N.message("identity"));
		guarantorTabSheet.addTab(employmentTab, I18N.message("employment"));
		guarantorTabSheet.addTab(otherInformationTab, I18N.message("other.information"));
		
		guarantorTabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2275900953290360358L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				
			}
		});
		
		cbSameApplicantAddress.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2120119835501936565L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				addressFormPanel.setAddressEnabled(!cbSameApplicantAddress.getValue());
				Address address = (guarantor != null) ? guarantor.getIndividual().getMainAddress() : null;
				if (address == null) {
					address = new Address();
					address.setCountry(ECountry.KHM);
				}
				if (mainApplicant != null) {
					Address applicantAddress = mainApplicant.getIndividual().getMainAddress();
					if (applicantAddress != null && cbSameApplicantAddress.getValue()) {
						address = AddressUtils.copy(applicantAddress, address);
					}
				}
				
				addressFormPanel.assignValues(address);
				
				if (currentEmploymentPanel.isSameApplicantAddress()) {
					currentEmploymentPanel.assignAddressValues(address);
				}
			}
		});
		
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.addComponent(guarantorTabSheet);
        return verticalLayout;
    }
	
	/**
	 * @return
	 */
	public QuotationApplicant getQuotationApplicant(QuotationApplicant quotationApplicant) {
		quotationApplicant.setRelationship(cbxRelationship.getSelectedEntity());
		quotationApplicant.setSameApplicantAddress(cbSameApplicantAddress.getValue());
		return quotationApplicant;
	}
	
	/**
	 * @param mainApplicant
	 */
	public void setMainApplicant(Applicant mainApplicant) {
		this.mainApplicant = mainApplicant;
	}
	
	/**
	 * Get applicant
	 * @param applicant
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		identityPanel.getApplicant(applicant);
		Individual individual = applicant.getIndividual();
		if (cbxRelationship.getSelectedEntity() != null 
				|| cbSameApplicantAddress.getValue()
				|| StringUtils.isNotEmpty(individual.getFirstNameEn())
				|| StringUtils.isNotEmpty(individual.getLastNameEn())) {
			
			IndividualAddress individualAddress = individual.getIndividualAddress(ETypeAddress.MAIN);
			if (individualAddress == null || individualAddress.getId() == null) {
				Address address = addressFormPanel.getAddress(new Address());
				individual.setAddress(address, ETypeAddress.MAIN);
			} else {
				addressFormPanel.getAddress(individualAddress.getAddress());
			}
			
			Employment currentEmployment = individual.getCurrentEmployment();
			if (currentEmployment != null) {	
				currentEmploymentPanel.getEmployment(currentEmployment);
			} else {
				currentEmployment = currentEmploymentPanel.getEmployment(new Employment());
				if (currentEmployment.isPersistent()) {
					individual.addEmployment(currentEmployment);
				}
			}
			if (currentEmployment.isSameApplicantAddress()) {
				AddressUtils.copy(individualAddress.getAddress(), currentEmployment.getAddress());
			}
		
			List<Employment> secondaryEmployments = individual.getEmployments(EEmploymentType.SECO);
			if (secondaryEmployments != null && !secondaryEmployments.isEmpty()) {
				Employment secondaryEmployment = secondaryEmploymentPanel.getEmployment(secondaryEmployments.get(0));
				if (secondaryEmployment == null) {
					secondaryEmployments.get(0).setCrudAction(CrudAction.DELETE);
				}
			} else {
				Employment secondaryEmployment = secondaryEmploymentPanel.getEmployment(new Employment());
				if (secondaryEmployment != null) {
					individual.addEmployment(secondaryEmployment);
				}
			}
			
			otherInformationPanel.getApplicant(applicant);
			
			return applicant;
		}
		return null;		
	}
	
	/**
	 * Set applicant
	 * @param guarantor
	 */
	public void assignValues(QuotationApplicant quotationApplicant) {
		boolean enabled = true;
		if (quotationApplicant != null) {
			this.guarantor = quotationApplicant.getApplicant();
			cbxRelationship.setSelectedEntity(quotationApplicant.getRelationship());
			cbSameApplicantAddress.setValue(quotationApplicant.isSameApplicantAddress());
			
			Applicant applicant = quotationApplicant.getApplicant();
			Individual individual = applicant.getIndividual();
			identityPanel.assignValues(applicant);
			Address address = individual.getMainAddress();
			if (address == null) {
				address = new Address();
				address.setCountry(ECountry.KHM);
				individual.setAddress(address, ETypeAddress.MAIN);
			}
			addressFormPanel.assignValues(address);
			Employment currentEmployment = null;
			if (individual.getEmployments() != null) {
				currentEmployment = individual.getCurrentEmployment();
				if (currentEmployment != null) {
					currentEmploymentPanel.setApplicant(applicant, EApplicantType.G);
					currentEmploymentPanel.assignValues(currentEmployment);
				}
			}
			
			if (currentEmployment == null) {
				currentEmploymentPanel.reset();
			}
			List<Employment> secondaryEmployments = individual.getEmployments(EEmploymentType.SECO);
			if (secondaryEmployments != null && !secondaryEmployments.isEmpty()) {
				secondaryEmploymentPanel.assignValues(secondaryEmployments.get(0));
			} else {
				secondaryEmploymentPanel.reset();
			}
			otherInformationPanel.assignValues(applicant);
			
			Quotation quotation = quotationApplicant.getQuotation();
			enabled = (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) && !quotation.getPreviousWkfStatus().equals(QuotationWkfStatus.APV));
			if((ProfileUtil.isUnderwriter() || ProfileUtil.isUnderwriterSupervisor() || ProfileUtil.isManager())
					&& (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRO)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.DEC)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRA)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.SUB)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.REJ)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.REU)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.APU)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.APS)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWS)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
						|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWU)
						)){	
						enabled = true;
					}
		
			btnChangeGuarantor.setVisible(false);
		} else {
			this.reset();
			currentEmploymentPanel.setApplicant(null, EApplicantType.G);
			secondaryEmploymentPanel.reset();
		}
		if (ProfileUtil.isAdmin()) {
			enabled = true;
		}
		setEnabledGuarantor(enabled);
		guarantorTabSheet.setSelectedTab(identityTab);
	}

	/**
	 * @param enabled
	 */
	public void setEnabledGuarantor(boolean enabled) {
		cbxRelationship.setEnabled(enabled);
		cbSameApplicantAddress.setEnabled(enabled);
		identityPanel.setEnableIdentityPanel(enabled);
		addressFormPanel.setEnabled(enabled);
		currentEmploymentPanel.setEnabled(enabled);
		secondaryEmploymentPanel.setEnabled(enabled);
		otherInformationPanel.setEnabled(enabled);
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		errors.addAll(identityPanel.validate());
		// errors.addAll(addressFormPanel.validate());
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {
		super.removeErrorsPanel();
		checkMandatorySelectField(cbxRelationship, "relationship.with.applicant");
		errors.addAll(identityPanel.fullValidate());
		errors.addAll(addressFormPanel.fullValidate());
		errors.addAll(currentEmploymentPanel.fullValidate());
		errors.addAll(otherInformationPanel.fullValidate());
		return errors;
	}

	/**
	 * Reset panel
	 */
	public void reset() {
		cbxRelationship.setSelectedEntity(null);
		cbSameApplicantAddress.setValue(false);
		identityPanel.reset();
		addressFormPanel.reset();
		currentEmploymentPanel.reset();
		otherInformationPanel.reset();
	}
}
