package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Information Spouse Panel in Quotation
 * @author buntha.chea
 */
public class InformationSpousePanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	
	private ComboBox cbxIDType;
	private ComboBox cbxPrefix;
	private ComboBox cbxStatus;
	private ComboBox cbxChildren;
	private ComboBox cbxEducation;
	private ComboBox cbxType;
	private ComboBox cbxReligion;
	private ComboBox cbxSecondLanguage;
		
	private AutoDateField dfDateOfBirth;
	private AutoDateField dfIssuingDate;
	private AutoDateField dfExpiringDate;
		
	private TextField txtIDNumber;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtAge;
	private TextField txtHousehold;
	private Button btnCheckGuarantor;
	
	// Financial information part
	private ComboBox cbxIncomeType;
	private ComboBox cbxCategoryIncome;
	private TextField txtIncome;
	private TextField txtBuilding;
	private TextField txtMoo;
	private TextField txtSoi;
	private TextField txtStreet;
	private TextField txtSubDistrict;
	private TextField txtDistrict;
	private TextField txtProvince;
	private TextField txtPostalCode;
	private TextField txtCompanyPhone;
	private TextField txtSSN;
	private TextField txtPersonalMonthlyExpense;
	private TextField txtHouseholdMonthlyExpense;
	private TextField txtHouseholdIncome;
	
	//Contract Information Path
	private ComboBox cbxAddressType;
	private ComboBox cbxResidenceStatus;
	private ComboBox cbxPhoneType;
	private ComboBox cbxAddress;
	private TextField txtContractBuilding;
	private TextField txtContractMoo;
	private TextField txtContractSoi;
	private TextField txtContractStreet;
	private TextField txtContractSubDistrict;
	private TextField txtContractDistrict;
	private TextField txtContractProvince;
	private TextField txtContractPostalCode;
	private TextField txtTimeLiveInYear;
	private TextField txtTimeLiveInMonth;
	private TextField txtPhoneNumber;
	private TextField txtEmail;
	private TextField txtFacebookId;
	private Button btnCheckExistingPhone;
	private Button btnCheckExistingEmail;
			
	public InformationSpousePanel() {
		super();
		setSizeFull();
	}	
	
	@Override
	protected Component createForm() {		
		HorizontalLayout personeAndFinancialLayout = new HorizontalLayout();
		personeAndFinancialLayout.addComponent(createPersonPanel());
		personeAndFinancialLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS));
		personeAndFinancialLayout.addComponent(createFinancialPanel());
		
		VerticalLayout spouseLayout = new VerticalLayout();
		spouseLayout.setSpacing(true);
		spouseLayout.addComponent(personeAndFinancialLayout);
		spouseLayout.addComponent(createContractPanel());              
        return spouseLayout;
    }
	
	/**
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {			
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Applicant());
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		return errors.isEmpty();
	}
	
	/**
	 * PersonPanel In Quotation Spouse Information
	 */
	public Panel createPersonPanel(){
		cbxIDType = ComponentFactory.getComboBox();
		cbxPrefix = ComponentFactory.getComboBox();
		cbxStatus = ComponentFactory.getComboBox();
		cbxChildren= ComponentFactory.getComboBox();
		cbxEducation = ComponentFactory.getComboBox();
		cbxType = ComponentFactory.getComboBox();
		cbxReligion = ComponentFactory.getComboBox();
		cbxSecondLanguage = ComponentFactory.getComboBox();
		
		txtIDNumber = ComponentFactory.getTextField(false, 60, 150);
		txtFirstName = ComponentFactory.getTextField(false, 60, 220);
		txtLastName = ComponentFactory.getTextField(false, 60, 220);
		txtAge = ComponentFactory.getTextField(false, 60, 220);
		txtHousehold = ComponentFactory.getTextField(false, 60, 220);
		
		dfDateOfBirth = ComponentFactory.getAutoDateField();
		dfDateOfBirth.setValue(DateUtils.today());
		dfIssuingDate = ComponentFactory.getAutoDateField();
		dfIssuingDate.setValue(DateUtils.today());
		dfExpiringDate = ComponentFactory.getAutoDateField();
		dfExpiringDate.setValue(DateUtils.today());
		
		btnCheckGuarantor = ComponentFactory.getButton("check");
		btnCheckGuarantor.setStyleName(Runo.BUTTON_SMALL);
		
		String template = "spousePersonalInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("id.type")), "lblIDType");
		customLayout.addComponent(cbxIDType, "cbxIDType");
		customLayout.addComponent(txtIDNumber, "txtIDNumber");
		customLayout.addComponent(btnCheckGuarantor, "btnCheckGuarantor");
		
		customLayout.addComponent(new Label(I18N.message("prefix")), "lblprefix");
		customLayout.addComponent(cbxPrefix, "cbxprefix");
		
		
		customLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		customLayout.addComponent(txtFirstName, "txtFirstName");
		customLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		customLayout.addComponent(txtLastName, "txtLastName");
		customLayout.addComponent(new Label(I18N.message("dob")), "lblDateOfBirth");
		customLayout.addComponent(dfDateOfBirth, "dfDateOfBirth");
		customLayout.addComponent(new Label(I18N.message("age")), "lblAge");
		customLayout.addComponent(txtAge, "txtAge");
		customLayout.addComponent(new Label(I18N.message("prefix")), "lblPrefix");
		customLayout.addComponent(cbxPrefix, "cbxPrefix");
		customLayout.addComponent(new Label(I18N.message("issuing.id.date")), "lblIssuingDate");
		customLayout.addComponent(dfIssuingDate, "dfIssuingDate");
		customLayout.addComponent(new Label(I18N.message("expiring.id.date")), "lblExpiringDate");
		customLayout.addComponent(dfExpiringDate, "dfExpiringDate");
		customLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		customLayout.addComponent(cbxStatus, "cbxStatus");
		customLayout.addComponent(new Label(I18N.message("number.of.children")), "lblChildren");
		customLayout.addComponent(cbxChildren, "cbxChildren");
		customLayout.addComponent(new Label(I18N.message("education")), "lblEducation");
		customLayout.addComponent(cbxEducation, "cbxEducation");
		customLayout.addComponent(new Label(I18N.message("household")), "lblHousehold");
		customLayout.addComponent(txtHousehold, "txtHousehold");
		customLayout.addComponent(new Label(I18N.message("type")), "lblType");
		customLayout.addComponent(cbxType, "cbxType");
		customLayout.addComponent(new Label(I18N.message("religion")), "lblReligion");
		customLayout.addComponent(cbxReligion, "cbxReligion");
		customLayout.addComponent(new Label(I18N.message("second.language")), "lblSecondLanguage");
		customLayout.addComponent(cbxSecondLanguage, "cbxSecondLanguage");
		
		Panel personPanel = new Panel();
		personPanel.setCaption(I18N.message("personal.information"));
		personPanel.setStyleName(Reindeer.PANEL_LIGHT);
		personPanel.setContent(customLayout);
		
		return personPanel;
	}
	
	/**
	 * 
	 */
	public Panel createFinancialPanel(){
		// Financial information part
		cbxIncomeType = ComponentFactory.getComboBox("income.type", null);
		cbxIncomeType.setRequired(true);
		cbxCategoryIncome = ComponentFactory.getComboBox("category.income", null);
		cbxCategoryIncome.setRequired(true);
				
		txtIncome = ComponentFactory.getTextField("income", true, 60, 220);
		txtBuilding = ComponentFactory.getTextField("number.building", true, 60, 220);
		txtMoo = ComponentFactory.getTextField("moo", false, 60, 220);
		txtSoi = ComponentFactory.getTextField("soi", false, 60, 220);
		txtStreet = ComponentFactory.getTextField("street", true, 60, 220);
		txtSubDistrict = ComponentFactory.getTextField("subdistrict", true, 60, 220);
		txtDistrict = ComponentFactory.getTextField("district", true, 60, 220);
		txtProvince = ComponentFactory.getTextField("province", true, 60, 220);
		txtPostalCode = ComponentFactory.getTextField("postal.code", true, 60, 220);
		txtCompanyPhone = ComponentFactory.getTextField("company.phone", true, 60, 220);
		txtSSN = ComponentFactory.getTextField("ssn", false, 60, 220);
		txtPersonalMonthlyExpense = ComponentFactory.getTextField("personal.monthly.expense", false, 60, 220);
		txtHouseholdMonthlyExpense = ComponentFactory.getTextField("household.monthly.expense", false, 60, 220);
		txtHouseholdIncome = ComponentFactory.getTextField("household.income", false, 60, 220);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(cbxIncomeType);
		formLayout.addComponent(cbxCategoryIncome);
		formLayout.addComponent(txtIncome);
		formLayout.addComponent(txtBuilding);
		formLayout.addComponent(txtMoo);
		formLayout.addComponent(txtSoi);
		formLayout.addComponent(txtStreet);
		formLayout.addComponent(txtSubDistrict);
		formLayout.addComponent(txtDistrict);
		formLayout.addComponent(txtProvince);
		formLayout.addComponent(txtPostalCode);
		formLayout.addComponent(txtCompanyPhone);
		formLayout.addComponent(txtSSN);
		formLayout.addComponent(txtPersonalMonthlyExpense);
		formLayout.addComponent(txtHouseholdMonthlyExpense);
		formLayout.addComponent(txtHouseholdIncome);
		
		Panel financialInformationPanel = new Panel(I18N.message("financial.information"));
		financialInformationPanel.setWidth("450px");
		financialInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		financialInformationPanel.setContent(formLayout);
		financialInformationPanel.setCaption(I18N.message("financial.information"));
		
		return financialInformationPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	public Panel createContractPanel(){
		cbxAddressType = ComponentFactory.getComboBox();
		cbxResidenceStatus = ComponentFactory.getComboBox();
		cbxPhoneType = ComponentFactory.getComboBox();
		cbxAddress = ComponentFactory.getComboBox();
		cbxPhoneType.setWidth("100px");
		cbxAddress.setWidth("100px");

		txtContractBuilding = ComponentFactory.getTextField(false, 60, 220);
		txtContractMoo = ComponentFactory.getTextField(false, 60, 220);
		txtContractSoi = ComponentFactory.getTextField(false, 60, 220);
		txtContractStreet = ComponentFactory.getTextField(false, 60, 220);
		txtContractSubDistrict = ComponentFactory.getTextField(false, 60, 220);
		txtContractDistrict = ComponentFactory.getTextField(false, 60, 220);
		txtContractProvince = ComponentFactory.getTextField(false, 60, 220);
		txtContractPostalCode = ComponentFactory.getTextField(false, 60, 220);
		txtTimeLiveInYear = ComponentFactory.getTextField(false, 60, 50);
		txtTimeLiveInMonth = ComponentFactory.getTextField(false, 60, 50);
		txtPhoneNumber = ComponentFactory.getTextField(false, 60, 90);
		txtEmail = ComponentFactory.getTextField(false, 60, 220);
		txtFacebookId = ComponentFactory.getTextField(false, 60, 220);

		btnCheckExistingPhone = ComponentFactory.getButton("check.existing");
		btnCheckExistingEmail = ComponentFactory.getButton("check.existing");
		btnCheckExistingEmail.setStyleName(Runo.BUTTON_SMALL);
		btnCheckExistingPhone.setStyleName(Runo.BUTTON_SMALL);
		
		String template = "contactInformation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("address.type")), "lblAddressType");
		customLayout.addComponent(cbxAddressType, "cbxAddressType");
		customLayout.addComponent(new Label(I18N.message("number.building")), "lblBuilding");
		customLayout.addComponent(txtContractBuilding, "txtBuilding");
		customLayout.addComponent(new Label(I18N.message("moo")), "lblMoo");
		customLayout.addComponent(txtContractMoo, "txtMoo");
		customLayout.addComponent(new Label(I18N.message("soi")), "lblSoi");
		customLayout.addComponent(txtContractSoi, "txtSoi");
		customLayout.addComponent(new Label(I18N.message("street")), "lblStreet");
		customLayout.addComponent(txtContractStreet, "txtStreet");
		customLayout.addComponent(new Label(I18N.message("subdistrict")), "lblSubDistrict");
		customLayout.addComponent(txtContractSubDistrict, "txtSubDistrict");
		customLayout.addComponent(new Label(I18N.message("district")), "lblDistrict");
		customLayout.addComponent(txtContractDistrict, "txtDistrict");
		customLayout.addComponent(new Label(I18N.message("province")), "lblProvince");
		customLayout.addComponent(txtContractProvince, "txtProvince");
		customLayout.addComponent(new Label(I18N.message("postal.code")), "lblPostalCode");
		customLayout.addComponent(txtContractPostalCode, "txtPostalCode");
		customLayout.addComponent(new Label(I18N.message("living.period")), "lblTimeLive");
		customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
        customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
		customLayout.addComponent(txtTimeLiveInYear, "txtTimeLiveInYear");
		customLayout.addComponent(txtTimeLiveInMonth, "txtTimeLiveInMonth");
		customLayout.addComponent(new Label(I18N.message("residence.status")), "lblResidenceStatus");
		customLayout.addComponent(cbxResidenceStatus, "cbxResidenceStatus");
		customLayout.addComponent(new Label(I18N.message("phone.type")), "lblPhoneType");
		customLayout.addComponent(cbxPhoneType, "cbxPhoneType");
		customLayout.addComponent(cbxAddress, "cbxAddress");
		customLayout.addComponent(txtPhoneNumber, "txtPhoneNumber");
		customLayout.addComponent(btnCheckExistingPhone, "btnCheckExistingPhone");
		customLayout.addComponent(new Label(I18N.message("email")), "lblEmail");
		customLayout.addComponent(txtEmail, "txtEmail");
		customLayout.addComponent(btnCheckExistingEmail, "btnCheckExistingEmail");
		customLayout.addComponent(new Label(I18N.message("facebook.id")), "lblFacebookId");
		customLayout.addComponent(txtFacebookId, "txtFacebookId");
		
		Panel contractPanel = new Panel();
		contractPanel.setCaption(I18N.message("contact.information"));
		contractPanel.setStyleName(Reindeer.PANEL_LIGHT);
		contractPanel.setContent(customLayout);
		return contractPanel;
	}
	
	
}
