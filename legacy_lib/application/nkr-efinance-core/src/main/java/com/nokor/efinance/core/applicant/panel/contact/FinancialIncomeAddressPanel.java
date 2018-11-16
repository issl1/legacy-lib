package com.nokor.efinance.core.applicant.panel.contact;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.panel.AddressPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ECompanySize;
import com.nokor.ersys.core.hr.model.eref.EEmploymentCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustryCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact financial and address layout full detail 
 * @author uhout.cheng
 */
public class FinancialIncomeAddressPanel extends AbstractControlPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 8735845928687915990L;
	
	private ERefDataComboBox<EEmploymentStatus> cbxEmploymentType;
	private ERefDataComboBox<EEmploymentCategory> cbxEmploymentCategory;
	private ERefDataComboBox<EEmploymentIndustryCategory> cbxCompanySector;
	private ERefDataComboBox<EEmploymentIndustry> cbxOccupationGroup;
	private ERefDataComboBox<ECompanySize> cbxCompanySize;
	private TextField txtJobPosition;
//	private AutoDateField dfSince;
	private TextField txtTimeWorkingInYear;
	private TextField txtTimeWorkingInMonth;
	private TextField txtCompanyName;
	private TextField txtManagerName;
	private TextField txtManagerPhone;
	private TextField txtCompanyPhone;
	
	private TextField txtDepartmentPhone;
	
	private AddressPanel addressPanel;
	
	private boolean required;
	
	/** */
	public FinancialIncomeAddressPanel() {
		super();
		setSizeFull();
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(createContractInfoPanel(true));
		addComponent(verticalLayout);
	}	
	
	/**
	 * 
	 * @param individual
	 * @param employment
	 * @param employmentType
	 */
	public void setEmploymentInfoAddress(Individual individual, Employment employment, EEmploymentType employmentType) {
		employment.setEmployerName(txtCompanyName.getValue());
		employment.setPosition(txtJobPosition.getValue());
//		employment.setSince(dfSince.getValue());
		employment.setTimeWithEmployerInYear(getInteger(txtTimeWorkingInYear));
		employment.setTimeWithEmployerInMonth(getInteger(txtTimeWorkingInMonth));
		employment.setWorkPhone(txtCompanyPhone.getValue());
		employment.setEmploymentStatus(cbxEmploymentType.getSelectedEntity());
		employment.setEmploymentCategory(cbxEmploymentCategory.getSelectedEntity());
		//employment.setEmploymentIndustry(cbxCompanySector.getSelectedEntity());
		employment.setEmploymentIndustryCategory(cbxCompanySector.getSelectedEntity());
		employment.setEmploymentIndustry(cbxOccupationGroup.getSelectedEntity());
		employment.setManagerName(txtManagerName.getValue());
		employment.setManagerPhone(txtManagerPhone.getValue());
		employment.setEmploymentType(employmentType);
		employment.setCompanySize(cbxCompanySize.getSelectedEntity());
		employment.setDepartmentPhone(txtDepartmentPhone.getValue());
		employment.setIndividual(individual);
		
		Address address = employment.getAddress();
		if (address == null) {
			address = Address.createInstance();
		}
		employment.setAddress(addressPanel.getAddress(address));
		INDIVI_SRV.saveOrUpdateEmployment(employment);
	}
	
	/**
	 * 
	 * @param individual
	 * @param employment
	 */
	public void assignValue(Individual individual, Employment employment) {
		cbxEmploymentType.setSelectedEntity(employment.getEmploymentStatus());
		cbxEmploymentCategory.setSelectedEntity(employment.getEmploymentCategory());
//		cbxCompanySector.setSelectedEntity(employment.getEmploymentIndustry());
		cbxCompanySector.setSelectedEntity(employment.getEmploymentIndustryCategory());
		cbxOccupationGroup.setSelectedEntity(employment.getEmploymentIndustry());
		cbxCompanySize.setSelectedEntity(employment.getCompanySize());
		txtCompanyName.setValue(getDefaultString(employment.getEmployerName()));
		txtJobPosition.setValue(getDefaultString(employment.getPosition()));
		txtTimeWorkingInYear.setValue(getDefaultString(employment.getTimeWithEmployerInYear()));
		txtTimeWorkingInMonth.setValue(getDefaultString(employment.getTimeWithEmployerInMonth()));
		txtCompanyPhone.setValue(getDefaultString(employment.getWorkPhone()));
		txtManagerName.setValue(getDefaultString(employment.getManagerName()));
		txtManagerPhone.setValue(getDefaultString(employment.getManagerPhone()));
		txtDepartmentPhone.setValue(getDefaultString(employment.getDepartmentPhone()));
//		dfSince.setValue(employment.getSince());
		//assign address Employment
		Address empAddress = employment.getAddress();
		addressPanel.setIndividual(individual);
		addressPanel.assignValue(empAddress);
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(190, Unit.PIXELS);
		comboBox.setImmediate(true);
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout createContractInfoPanel(boolean required){
		this.required = required;
		cbxEmploymentType = getERefDataComboBox(EEmploymentStatus.values());
		cbxEmploymentCategory = getERefDataComboBox(EEmploymentCategory.values());
		cbxCompanySector = getERefDataComboBox(EEmploymentIndustryCategory.values());
		cbxOccupationGroup = getERefDataComboBox(EEmploymentIndustry.values());
		cbxCompanySector.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 7068898637956472751L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				cbxOccupationGroup.clear();
				if (cbxCompanySector.getSelectedEntity() != null) {
					List<EEmploymentIndustry> lstEmploymentIndustries = EEmploymentIndustry.values(cbxCompanySector.getSelectedEntity());
					cbxOccupationGroup.assignValueMap(lstEmploymentIndustries);
				} else {
					cbxOccupationGroup.assignValueMap(EEmploymentIndustry.values());
				}				
			}
		});
		cbxCompanySize = getERefDataComboBox(ECompanySize.values());
		txtJobPosition = ComponentFactory.getTextField(false, 60, 190);
		txtCompanyName = ComponentFactory.getTextField(false, 60, 190);
		txtManagerName = ComponentFactory.getTextField(false, 60, 190);
		txtManagerPhone = ComponentFactory.getTextField(false, 10, 190);
		txtTimeWorkingInYear = ComponentFactory.getTextField(false, 60, 50);
		txtTimeWorkingInMonth = ComponentFactory.getTextField(false, 60, 50);
		txtDepartmentPhone = ComponentFactory.getTextField(false, 60, 190);
		txtCompanyPhone = ComponentFactory.getTextField(10, 190);
//		dfSince = ComponentFactory.getAutoDateField();
		
		addressPanel = new AddressPanel();
		addressPanel.setMargin(false);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(createFinancialInformaionPanel());
		return verticalLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public HorizontalLayout createFinancialInformaionPanel(){
		String template = "user/userFinancialAddressLayout";
		CustomLayout customLayout = LayoutHelper.createCustomLayout(template);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(template), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("employment.type")), "lblEmploymentType");
		customLayout.addComponent(cbxEmploymentType, "cbxEmploymentType");
		customLayout.addComponent(new Label(I18N.message("employment.category")), "lblEmploymentCategory");
		customLayout.addComponent(cbxEmploymentCategory, "cbxEmploymentCategory");
		customLayout.addComponent(new Label(I18N.message("job.position")), "lblJobPosition");
		customLayout.addComponent(txtJobPosition, "txtJobPosition");
//		customLayout.addComponent(new Label(I18N.message("since")), "lblSince");
//		customLayout.addComponent(dfSince, "dfSince");	
		customLayout.addComponent(new Label(I18N.message("company.name")), "lblCompanyName");
		customLayout.addComponent(txtCompanyName, "txtCompanyName");
		customLayout.addComponent(new Label(I18N.message("company.sector")), "lblCompanySector");
		customLayout.addComponent(cbxCompanySector, "cbxCompanySector");
		customLayout.addComponent(new Label(I18N.message("occupation.group")), "lblOccupationGroup");
		customLayout.addComponent(cbxOccupationGroup, "cbxOccupationGroup");
		customLayout.addComponent(new Label(I18N.message("company.size")), "lblCompanySize");
		customLayout.addComponent(cbxCompanySize, "cbxCompanySize");
		customLayout.addComponent(new Label(I18N.message("working.period")), "lblWorkingPeriod");
		customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
		customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
		customLayout.addComponent(txtTimeWorkingInYear, "txtTimeWorkingInYear");
		customLayout.addComponent(txtTimeWorkingInMonth, "txtTimeWorkingInMonth");
		customLayout.addComponent(new Label(I18N.message("manager.name")), "lblManagerName");
		customLayout.addComponent(txtManagerName, "txtManagerName");
		customLayout.addComponent(new Label(I18N.message("manager.phone")), "lblManagerPhone");
		customLayout.addComponent(txtManagerPhone, "txtManagerPhone");
		customLayout.addComponent(new Label(I18N.message("department.phone")), "lblDepartmentPhone");
		customLayout.addComponent(txtDepartmentPhone, "txtDepartmentPhone");
			
		Panel financialInformationPanel = new Panel();
		financialInformationPanel.setWidth(350, Unit.PIXELS);
		financialInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		financialInformationPanel.setContent(customLayout);
		
		VerticalLayout addressLayout = new VerticalLayout();
		addressLayout.addComponent(addressPanel);
		addressLayout.addComponent(getCompanyPhoneLayout());
		
		Panel addressJobInformationPanel = new Panel(I18N.message("address.job"));
		addressJobInformationPanel.setWidth(550, Unit.PIXELS);
		addressJobInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addressJobInformationPanel.setContent(addressLayout);
	
		HorizontalLayout financialAndAddressJobLayout = new HorizontalLayout();
		financialAndAddressJobLayout.setSpacing(true);
		financialAndAddressJobLayout.addComponent(financialInformationPanel);
		financialAndAddressJobLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS));
		financialAndAddressJobLayout.addComponent(addressJobInformationPanel);
		
		return financialAndAddressJobLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getCompanyPhoneLayout() {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"1\" cellpadding=\"1\" >";
		template += "<tr>";
		template += "<td align=\"left\" width=\"120\">";
		template += "<div location=\"lblCompanyPhone\" class=\"inline-block\"></div>";
		template += "<div class=\"inline-block requiredfield\">&nbsp;*</div>";
		template += "</td>";
		template += "<td>";
		template += "<div location =\"txtCompanyPhone\" />";
		template += "</td>";
		template += "</tr>";
		template += "</table>";
		
		cusLayout.addComponent(new Label(I18N.message("company.phone")), "lblCompanyPhone");
		cusLayout.addComponent(txtCompanyPhone, "txtCompanyPhone");
		cusLayout.setTemplateContents(template);
		
		return cusLayout;
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		super.reset();
		if (required) {
			checkIntegerField(txtTimeWorkingInYear, "years");
			checkIntegerField(txtTimeWorkingInMonth, "months");
			checkMandatoryField(txtCompanyName, "company.name");
			checkMandatoryField(txtJobPosition, "job.position");
//			checkMandatoryField(txtManagerName, "manager.name");
//			checkMandatoryField(txtManagerPhone, "manager.phone");
//			checkMandatoryDateField(dfSince, "since");
			checkMandatoryField(txtTimeWorkingInMonth, "months");
			checkMandatoryField(txtTimeWorkingInYear, "years");
			checkMandatorySelectField(cbxEmploymentType, "employment.type");
			checkMandatorySelectField(cbxEmploymentCategory, "employment.category");
			checkMandatorySelectField(cbxCompanySector, "company.sector");
			checkMandatorySelectField(cbxOccupationGroup, "occupation.group");
			errors.addAll(addressPanel.fullValidate());
			checkMandatoryField(txtCompanyPhone, "company.phone");
		}
		return errors;
	}
	
	/**
	 * Reset controls
	 */
	public void setEnabledControls(boolean isEnabled) {
		cbxEmploymentType.setEnabled(isEnabled);
		cbxEmploymentCategory.setEnabled(isEnabled);
		txtJobPosition.setEnabled(isEnabled);
		cbxCompanySector.setEnabled(isEnabled);
		cbxCompanySize.setEnabled(isEnabled);
		txtCompanyName.setEnabled(isEnabled);
		txtManagerName.setEnabled(isEnabled);
		txtManagerPhone.setEnabled(isEnabled);
		txtTimeWorkingInYear.setEnabled(isEnabled);
		txtTimeWorkingInMonth.setEnabled(isEnabled);
		txtCompanyPhone.setEnabled(isEnabled);
//		dfSince.setEnabled(isEnabled);
		cbxOccupationGroup.setEnabled(isEnabled);
		txtDepartmentPhone.setEnabled(isEnabled);
		addressPanel.setEnabledControls(isEnabled);
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxEmploymentType.setSelectedEntity(null);
		cbxEmploymentCategory.setSelectedEntity(null);
		cbxCompanySize.setSelectedEntity(null);
		txtJobPosition.setValue("");
		cbxCompanySector.setSelectedEntity(null);
		txtCompanyName.setValue("");
		txtManagerName.setValue("");
		txtManagerPhone.setValue("");
		txtTimeWorkingInYear.setValue("");
		txtTimeWorkingInMonth.setValue("");
		txtCompanyPhone.setValue("");
		cbxOccupationGroup.setSelectedEntity(null);
		txtDepartmentPhone.setValue("");
//		dfSince.setValue(null);
		addressPanel.reset();
	}
}
