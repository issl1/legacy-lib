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
public class InformationGuarantorPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	//private EnumComboBox<DealerType> cbxIDType;
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
		private TextField txtGuarantorID;
		private TextField txtFirstName;
		private TextField txtLastName;
		private TextField txtAge;
		private TextField txtHousehold;
		
		private Button btnCheckGuarantor;
		
		// Financial information part
		private ComboBox cbxIncomeType;
		private ComboBox cbxCategoryIncome;
		private TextField txtIncome;
		private TextField txtJobTitle;
		private TextField txtCompanyName;
		private TextField txtBuilding;
		private TextField txtMoo;
		private TextField txtSoi;
		private TextField txtStreet;
		private TextField txtSubDistrict;
		private TextField txtDistrict;
		private TextField txtProvince;
		private TextField txtPostalCode;
		private TextField txtTimeWorkingInYear;
		private TextField txtTimeWorkingInMonth;
		private TextField txtCompanyPhone;
			
	
	public InformationGuarantorPanel() {
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
		//spouseLayout.addComponent(createContractPanel());              
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
		cbxIDType.setWidth("150px");
		cbxPrefix = ComponentFactory.getComboBox();
		cbxStatus = ComponentFactory.getComboBox();
		cbxChildren= ComponentFactory.getComboBox();
		cbxEducation = ComponentFactory.getComboBox();
		cbxReligion = ComponentFactory.getComboBox();
		cbxType = ComponentFactory.getComboBox();
		cbxSecondLanguage = ComponentFactory.getComboBox();
		
		txtIDNumber = ComponentFactory.getTextField(false, 60, 150);
		txtGuarantorID = ComponentFactory.getTextField(false, 60, 220);
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
		
		String template = "informationGuarantorPersonalPanel";
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
		customLayout.addComponent(new Label(I18N.message("guarantor.id")), "lblGuarantorID");
		customLayout.addComponent(txtGuarantorID, "txtGuarantorID");
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
	
	public VerticalLayout createFinancialPanel(){
		cbxIncomeType = ComponentFactory.getComboBox();
		cbxCategoryIncome = ComponentFactory.getComboBox();
		
		txtIncome = ComponentFactory.getTextField(false, 60, 220);
		txtJobTitle = ComponentFactory.getTextField(false, 60, 220);
		txtCompanyName = ComponentFactory.getTextField(false, 60, 220);
		txtBuilding = ComponentFactory.getTextField("number.building",false, 60, 220);
		txtMoo = ComponentFactory.getTextField("moo",false, 60, 220);
		txtSoi = ComponentFactory.getTextField("soi",false, 60, 220);
		txtStreet = ComponentFactory.getTextField("street",false, 60, 220);
		txtSubDistrict = ComponentFactory.getTextField("subdistrict",false, 60, 220);
		txtDistrict = ComponentFactory.getTextField("district",false, 60, 220);
		txtProvince = ComponentFactory.getTextField("province",false, 60, 220);
		txtPostalCode = ComponentFactory.getTextField("postal.code",false, 60, 220);
		txtTimeWorkingInYear = ComponentFactory.getTextField(false, 60, 50);
		txtTimeWorkingInMonth = ComponentFactory.getTextField(false, 60, 50);
		txtCompanyPhone = ComponentFactory.getTextField("company.phone",false, 60, 220);
		
		String template = "informationGuarantorFinancialPanel";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("income.type")), "lblIncomeType");
		customLayout.addComponent(cbxIncomeType, "cbxIncomeType");
		customLayout.addComponent(new Label(I18N.message("category.income")), "lblCategoryIncome");
		customLayout.addComponent(cbxCategoryIncome, "cbxCategoryIncome");
		customLayout.addComponent(new Label(I18N.message("income")), "lblIncome");
		customLayout.addComponent(txtIncome, "txtIncome");
		customLayout.addComponent(new Label(I18N.message("job.title")), "lblJobTitle");
		customLayout.addComponent(txtJobTitle, "txtJobTitle");
		customLayout.addComponent(new Label(I18N.message("working.period")), "lblWorkingPeriod");
		customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
		customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
		customLayout.addComponent(txtTimeWorkingInYear, "txtTimeWorkingInYear");
		customLayout.addComponent(txtTimeWorkingInMonth, "txtTimeWorkingInMonth");
		customLayout.addComponent(new Label(I18N.message("company.name")), "lblCompanyName");
		customLayout.addComponent(txtCompanyName, "txtCompanyName");
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtBuilding);
		formLayout.addComponent(txtMoo);
		formLayout.addComponent(txtSoi);
		formLayout.addComponent(txtStreet);
		formLayout.addComponent(txtSubDistrict);
		formLayout.addComponent(txtDistrict);
		formLayout.addComponent(txtProvince);
		formLayout.addComponent(txtPostalCode);
		formLayout.addComponent(txtCompanyPhone);
		
		Panel addressJobPanel = new Panel();
		addressJobPanel.setCaption(I18N.message("address.job"));
		addressJobPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addressJobPanel.setContent(formLayout);
		
		
		Panel financialPanel = new Panel();
		financialPanel.setCaption(I18N.message("financial.information"));
		financialPanel.setStyleName(Reindeer.PANEL_LIGHT);
		financialPanel.setContent(customLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(financialPanel);
		verticalLayout.addComponent(addressJobPanel);
		
		return verticalLayout;
	}
	
	
}
