package com.nokor.efinance.gui.ui.panel.contract.user;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyEmployee;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.ECompanySize;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.ELegalForm;
import com.nokor.ersys.core.hr.model.eref.ETitle;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * User general tab for applicant is Company
 * @author buntha.chea
 */
public class UserCompanyGeneralPanel extends AbstractTabPanel implements FinServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = -4700059647786180293L;
	
	private final static String TEMPLATE = "user/userCompanyGeneralPageLayout";
	
	private TextField txtCustomerIDValue;
	private AutoDateField dfCreationDateValue;
	private TextField txtStatus;
	private TextField txtGrade;
	private ERefDataComboBox<EApplicantCategory> cbxCustomerType;
	private TextField txtCompanyName;
	
	private AutoDateField dfOpeningDate;
	private TextField txtCompanyOwnFullName;
	private ERefDataComboBox<EEmploymentIndustry> cbxCompanySector;
	private TextField txtLicenceNo;
	private ERefDataComboBox<ECompanySize> cbxCompanySize;
	
	private ERefDataComboBox<ETitle> cbxTitle;
	private ERefDataComboBox<ECivility> cbxPrefix;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtMiddleName;
	private TextField txtNickName;
	private ERefDataComboBox<ELegalForm> cbxLegalType;
	
	private AutoDateField dfDOB;
	private ERefDataComboBox<ETypeIdNumber> cbxIDType;
	private TextField txtIDNumber;
	private ERefDataComboBox<ETypeContact> cbxRelationShipWithCompany;
	
	private Applicant applicant;
	
	private Button btnSave;
	private Button btnEdit;
	
	private ApplicantCompanyPanel delegate;
	
	public UserCompanyGeneralPanel(ApplicantCompanyPanel delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(150, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		txtCustomerIDValue = ComponentFactory.getTextField("", false, 30, 150);
		dfCreationDateValue = ComponentFactory.getAutoDateField();
		txtStatus = ComponentFactory.getTextField("", false, 30, 150);
		
		txtGrade = ComponentFactory.getTextField("", false, 30, 150);
		cbxCustomerType = getERefDataComboBox(EApplicantCategory.values());
		txtCompanyName = ComponentFactory.getTextField("", false, 30, 150);
		cbxLegalType = getERefDataComboBox(ELegalForm.values());
		
		dfOpeningDate = ComponentFactory.getAutoDateField();
		txtCompanyOwnFullName = ComponentFactory.getTextField("", false, 30, 150);
		
		cbxCompanySector = getERefDataComboBox(EEmploymentIndustry.values());
		txtLicenceNo = ComponentFactory.getTextField("", false, 30, 150);
		cbxCompanySize = getERefDataComboBox(ECompanySize.values());
		
		cbxTitle = getERefDataComboBox(ETitle.values());
		cbxPrefix = getERefDataComboBox(ECivility.values());
		txtFirstName = ComponentFactory.getTextField("", false, 30, 150);
		txtLastName = ComponentFactory.getTextField("", false, 30, 150);
		txtMiddleName = ComponentFactory.getTextField("", false, 30, 150);
		txtNickName = ComponentFactory.getTextField("", false, 30, 150);
		
		dfDOB = ComponentFactory.getAutoDateField();
		cbxIDType = getERefDataComboBox(ETypeIdNumber.values());
		txtIDNumber = ComponentFactory.getTextField("", false, 30, 150);
		cbxRelationShipWithCompany = getERefDataComboBox(ETypeContact.values());
		
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.setVisible(false);
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setEnabled(true);
		btnEdit.addClickListener(this);
		
		return getSummaryDetailPanel();
	}

	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(caption);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getSummaryDetailPanel() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(getLabel("customer.id"), "lblCustomerId");
		customLayout.addComponent(txtCustomerIDValue, "txtCustomerIDValue");
		customLayout.addComponent(getLabel("creation.date"), "lblCreationDate");
		customLayout.addComponent(dfCreationDateValue, "dfCreationDateValue");
		customLayout.addComponent(getLabel("status"), "lblStatus");
		customLayout.addComponent(txtStatus, "txtStatus");
		
		customLayout.addComponent(getLabel("grade"), "lblGrade");
		customLayout.addComponent(txtGrade, "txtGrade");
		customLayout.addComponent(getLabel("customer.type"), "lblCustomerType");
		customLayout.addComponent(cbxCustomerType, "cbxCustomerType");
		customLayout.addComponent(getLabel("company.name"), "lblCompanyName");
		customLayout.addComponent(txtCompanyName, "txtCompanyName");
		
		customLayout.addComponent(getLabel("legal.type"), "lblLegalType");
		customLayout.addComponent(cbxLegalType, "cbxLegalType");
		customLayout.addComponent(getLabel("opening.date"), "lblOpeningDate");
		customLayout.addComponent(dfOpeningDate, "dfOpeningDate");
		customLayout.addComponent(getLabel("company.owner.fullname"), "lblCompanyOwnerFullname");
		customLayout.addComponent(txtCompanyOwnFullName, "txtCompanyOwnFullName");
		
		customLayout.addComponent(getLabel("company.sector"), "lblCompanySector");
		customLayout.addComponent(cbxCompanySector, "cbxCompanySector");
		customLayout.addComponent(getLabel("licence.no"), "lblLicenceNo");
		customLayout.addComponent(txtLicenceNo, "txtLicenceNo");
		customLayout.addComponent(getLabel("company.size"), "lblCompanySize");
		customLayout.addComponent(cbxCompanySize, "cbxCompanySize");	
		
		customLayout.addComponent(getLabel("title"), "lblTitle");
		customLayout.addComponent(cbxTitle, "cbxTitle");	
		customLayout.addComponent(getLabel("prefix"), "lblPrefix");
		customLayout.addComponent(cbxPrefix, "cbxPrefix");	
		customLayout.addComponent(getLabel("firstname"), "lblFirstName");
		customLayout.addComponent(txtFirstName, "txtFirstName");	
		
		customLayout.addComponent(getLabel("lastname"), "lblLastName");
		customLayout.addComponent(txtLastName, "txtLastName");	
		customLayout.addComponent(getLabel("middlename"), "lblMiddleName");
		customLayout.addComponent(txtMiddleName, "txtMiddleName");	
		customLayout.addComponent(getLabel("nickname"), "lblNickName");
		customLayout.addComponent(txtNickName, "txtNickName");	
		
		customLayout.addComponent(getLabel("dob"), "lblDOB");
		customLayout.addComponent(dfDOB, "dfDOB");	
		customLayout.addComponent(getLabel("id.type"), "lblIDType");
		customLayout.addComponent(cbxIDType, "cbxIDType");	
		customLayout.addComponent(getLabel("id.number"), "lblIDNumber");
		customLayout.addComponent(txtIDNumber, "txtIDNumber");	
		
		customLayout.addComponent(getLabel("relationship.with.company"), "lblRelationShipWithCompany");
		customLayout.addComponent(cbxRelationShipWithCompany, "cbxRelationShipWithCompany");	

		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(true, false);
		horLayout.addComponent(customLayout);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(btnEdit);
		horizontalLayout.addComponent(btnSave);
		
		VerticalLayout generalLayout = new VerticalLayout();
		generalLayout.setSpacing(true);
		generalLayout.setMargin(true);
		generalLayout.addComponent(horLayout);
		generalLayout.addComponent(horizontalLayout);
		generalLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
		
		Panel panel = new Panel(new VerticalLayout(generalLayout));
		panel.setCaption(I18N.message("general"));
		return panel;
	}
	
	/**
	 * 
	 * @param applicant
	 * @param individual
	 */
	public void assignValues(Applicant applicant) {
		this.applicant = applicant;		
		reset();
		if (applicant != null) {
			Company company = applicant.getCompany();
			dfCreationDateValue.setValue(company.getCreateDate());
			txtLicenceNo.setValue(getDefaultString(company.getLicenceNo()));
			txtStatus.setValue(company.getWkfStatus().getDescLocale());
			dfOpeningDate.setValue(company.getStartDate());
			cbxLegalType.setSelectedEntity(company.getLegalForm());
			cbxCompanySize.setSelectedEntity(company.getCompanySize());
			cbxCompanySector.setSelectedEntity(company.getEmploymentIndustry());
			txtCustomerIDValue.setValue(getDefaultString(company.getLicenceNo()));
			
			List<CompanyEmployee> companyEmployees = company.getCompanyEmployees();
			for (CompanyEmployee companyEmployee : companyEmployees) {
				if (ETypeContact.OWNER.equals(companyEmployee.getTypeContact())) {
					txtCompanyOwnFullName.setValue(getDefaultString(companyEmployee.getFullNameEn()));
				} else if (ETypeContact.MAIN.equals(companyEmployee.getTypeContact())) {
					//cbxTitle.setSelectedEntity(company.getti);
					cbxPrefix.setSelectedEntity(companyEmployee.getCivility());
					txtFirstName.setValue(getDefaultString(companyEmployee.getFirstNameEn()));
					txtLastName.setValue(getDefaultString(companyEmployee.getLastNameEn()));
					//txtMiddleName.setValue(companyEmployee.getmidd);
					txtNickName.setValue(getDefaultString(companyEmployee.getNickName()));
					dfDOB.setValue(companyEmployee.getBirthDate());
					cbxIDType.setSelectedEntity(companyEmployee.getTypeIdNumber());
					txtIDNumber.setValue(getDefaultString(companyEmployee.getIdNumber()));
					cbxRelationShipWithCompany.setSelectedEntity(companyEmployee.getTypeContact());
				}
			}
		}
		cbxCustomerType.setSelectedEntity(applicant.getApplicantCategory());
		txtCompanyName.setValue(getDefaultString(applicant.getNameEn()));
		setEnableControls(false);
	}
	
	/**
	 * 
	 * @param company
	 * @param type
	 * @return
	 */
	private CompanyEmployee getCompanyEmployee(Company company, ETypeContact type) {
		BaseRestrictions<CompanyEmployee> restrictions = new BaseRestrictions<>(CompanyEmployee.class);
		restrictions.addCriterion(Restrictions.eq("company", company));
		restrictions.addCriterion(Restrictions.eq("typeContact", type));
		List<CompanyEmployee> companyEmployees = ENTITY_SRV.list(restrictions);
		if (!companyEmployees.isEmpty()) {
			return companyEmployees.get(0);
		}
		return null;
	}
	
	
	/**
	 * Reset
	 */
	public void reset() {
		txtCustomerIDValue.setValue("");
		dfCreationDateValue.setValue(null);
		txtStatus.setValue("");
		txtGrade.setValue("");
		cbxCustomerType.setSelectedEntity(null);
		txtCompanyName.setValue("");
		cbxLegalType.setValue(null);
		dfOpeningDate.setValue(null);
		txtCompanyOwnFullName.setValue("");
		cbxCompanySector.setSelectedEntity(null);
		txtLicenceNo.setValue("");
		cbxCompanySize.setSelectedEntity(null);
		cbxTitle.setSelectedEntity(null);
		cbxPrefix.setSelectedEntity(null);
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtMiddleName.setValue("");
		txtNickName.setValue("");
		dfDOB.setValue(null);
		cbxIDType.setSelectedEntity(null);
		txtIDNumber.setValue("");
		cbxRelationShipWithCompany.setSelectedEntity(null);
	}
	
	

	/**
	 * Save Entity
	 */
	private void saveEntity() {
		removeErrorsPanel();
		Company company = applicant.getCompany();
		if (company != null) {
			company.setLicenceNo(txtLicenceNo.getValue());
			company.setStartDate(dfOpeningDate.getValue());
			company.setLegalForm(cbxLegalType.getSelectedEntity());
			company.setCompanySize(cbxCompanySize.getSelectedEntity());
			company.setEmploymentIndustry(cbxCompanySector.getSelectedEntity());
			company.setNameEn(txtCompanyName.getValue());
			
			CompanyEmployee companyEmployee = this.getCompanyEmployee(company, cbxRelationShipWithCompany.getSelectedEntity());
			if (companyEmployee == null) {
				companyEmployee = CompanyEmployee.createInstance();
				companyEmployee.setCompany(company);
			}
			companyEmployee.setCivility(cbxPrefix.getSelectedEntity());
			companyEmployee.setFirstNameEn(txtFirstName.getValue());
			companyEmployee.setLastNameEn(txtLastName.getValue());
			companyEmployee.setNickName(txtNickName.getValue());
			companyEmployee.setBirthDate(dfDOB.getValue());
			companyEmployee.setTypeIdNumber(cbxIDType.getSelectedEntity());
			companyEmployee.setIdNumber(txtIDNumber.getValue());
			companyEmployee.setTypeContact(cbxRelationShipWithCompany.getSelectedEntity());
			ENTITY_SRV.saveOrUpdate(companyEmployee);
			ENTITY_SRV.saveOrUpdate(company);
			displaySuccess();
		}
	}
	
	/**
	 * Set enable
	 * @param isEnable
	 */
	private void setEnableControls(boolean isEnable) {
		txtCustomerIDValue.setEnabled(false);
		dfCreationDateValue.setEnabled(false);
		txtStatus.setEnabled(false);
		txtGrade.setEnabled(isEnable);
		cbxCustomerType.setEnabled(false);
		txtCompanyName.setEnabled(isEnable);
		cbxLegalType.setEnabled(isEnable);
		dfOpeningDate.setEnabled(isEnable);
		txtCompanyOwnFullName.setEnabled(false);
		cbxCompanySector.setEnabled(isEnable);
		txtLicenceNo.setEnabled(isEnable);
		cbxCompanySize.setEnabled(isEnable);
		cbxTitle.setEnabled(isEnable);
		cbxPrefix.setEnabled(isEnable);
		txtFirstName.setEnabled(isEnable);
		txtLastName.setEnabled(isEnable);
		txtMiddleName.setEnabled(isEnable);
		txtNickName.setEnabled(isEnable);
		dfDOB.setEnabled(isEnable);
		cbxIDType.setEnabled(isEnable);
		txtIDNumber.setEnabled(isEnable);
		cbxRelationShipWithCompany.setEnabled(isEnable);
	}
/**
 * Button Click listener
 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			btnEdit.setVisible(false);
			btnSave.setVisible(true);
			setEnableControls(true);
		} else if (event.getButton() == btnSave) {
			saveEntity();
			btnSave.setVisible(false);
			btnEdit.setVisible(true);
			setEnableControls(false);
		}
		
	}
	
}
