package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.service.EntityService;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EEmploymentCategory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ELegalForm;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Financial and Address layout 
 * @author uhout.cheng
 */
public class FinancialIncomeAddressPanel extends AbstractControlPanel {
	
	/** */
	private static final long serialVersionUID = 8695564995522528997L;
	
	private final EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	private ERefDataComboBox<EEmploymentType> cbxIncomeType;
	private ERefDataComboBox<EEmploymentCategory> cbxCategoryIncome;
	private TextField txtIncome;
	private TextField txtJobTitle;
	private TextField txtCompanyName;
	private TextField txtTimeWorkingInYear;
	private TextField txtTimeWorkingInMonth;
	private TextField txtCompanyPhone;
	private ERefDataComboBox<ELegalForm> cbxCompanyLegalTitle;
	
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
	 * @param financialIncomeAddressPanels
	 * @param applicant
	 */
	public void getEmploymentInfo(List<FinancialIncomeAddressPanel> financialIncomeAddressPanels, Applicant applicant){
		List<Employment> employments = applicant.getIndividual().getEmployments();
		int numAddNewIncome = financialIncomeAddressPanels.size() - employments.size();
		int index = 0;
		List<Employment> newEmployments = new ArrayList<Employment>();
		boolean isNewEmployment = false;
		for (int i = 0; i <= numAddNewIncome; i++) {
			if (!isNewEmployment) {
				for (Employment employment : employments) {
					if (employment != null) {
						employment.setEmployerName(financialIncomeAddressPanels.get(index).txtCompanyName.getValue());
						employment.setPosition(financialIncomeAddressPanels.get(index).txtJobTitle.getValue());
						employment.setTimeWithEmployerInYear(getInteger(financialIncomeAddressPanels.get(index).
								txtTimeWorkingInYear));
						employment.setTimeWithEmployerInMonth(getInteger(financialIncomeAddressPanels.get(index).
								txtTimeWorkingInMonth));
						employment.setRevenue(getDouble(financialIncomeAddressPanels.get(index).txtIncome));
						employment.setWorkPhone(financialIncomeAddressPanels.get(index).txtCompanyPhone.getValue());
						employment.setEmploymentType(financialIncomeAddressPanels.get(index).cbxIncomeType.getSelectedEntity());
						employment.setEmploymentCategory(financialIncomeAddressPanels.get(index).cbxCategoryIncome.getSelectedEntity());
						employment.setLegalForm(financialIncomeAddressPanels.get(index).cbxCompanyLegalTitle.getSelectedEntity());
						if (employment.getAddress() != null) {
							Address address = employment.getAddress();
							address = financialIncomeAddressPanels.get(index).addressPanel.getAddress(address);
//							address.setHouseNo(financialIncomeAddressPanels.get(index).addressPanel.getTxtBuilding().getValue());
//							address.setLine1(financialIncomeAddressPanels.get(index).addressPanel.getTxtMoo().getValue());
//							address.setLine2(financialIncomeAddressPanels.get(index).addressPanel.getTxtSoi().getValue());
//							address.setStreet(financialIncomeAddressPanels.get(index).addressPanel.getTxtStreet().getValue());
//							address.setPostalCode(financialIncomeAddressPanels.get(index).addressPanel.getTxtPostalCode().getValue());
//							address.setProvince(financialIncomeAddressPanels.get(index).addressPanel.getCbxProvince().getSelectedEntity());
//							address.setDistrict(financialIncomeAddressPanels.get(index).addressPanel.getCbxDistrict().getSelectedEntity());
//							address.setCommune(financialIncomeAddressPanels.get(index).addressPanel.getCbxSubDistrict().getSelectedEntity());
//							address.setArea(financialIncomeAddressPanels.get(index).addressPanel.getCbxArea().getSelectedEntity());
							logger.debug("[>> saveOrUpdateAddress]");
							
							entityService.saveOrUpdate(address);
							
							logger.debug("Updated address for applicant = " + applicant.getIndividual().getFirstNameEn() + " " + applicant.getIndividual().getLastNameEn());
							logger.debug("[<< saveOrUpdateAddress]");
			
							employment.setAddress(address);
			
							logger.debug("[>> saveOrUpdateEmployment]");
							
							entityService.saveOrUpdate(employment);
							
							logger.debug("[<< saveOrUpdateEmployment]");
							
							newEmployments.add(employment);
							applicant.getIndividual().setEmployments(newEmployments);
						}
					}
					index++;
				}
				isNewEmployment = true;
			} else {
				Employment employment = new Employment();
				employment.setEmployerName(financialIncomeAddressPanels.get(index).txtCompanyName.getValue());
				employment.setPosition(financialIncomeAddressPanels.get(index).txtJobTitle.getValue());
				employment.setTimeWithEmployerInYear(getInteger(financialIncomeAddressPanels.get(index).txtTimeWorkingInYear));
				employment.setTimeWithEmployerInMonth(getInteger(financialIncomeAddressPanels.get(index).txtTimeWorkingInMonth));
				employment.setRevenue(getDouble(financialIncomeAddressPanels.get(index).txtIncome));
				employment.setWorkPhone(financialIncomeAddressPanels.get(index).txtCompanyPhone.getValue());
				employment.setEmploymentType(financialIncomeAddressPanels.get(index).cbxIncomeType.getSelectedEntity());
				employment.setEmploymentCategory(financialIncomeAddressPanels.get(index).cbxCategoryIncome.getSelectedEntity());
				employment.setLegalForm(financialIncomeAddressPanels.get(index).cbxCompanyLegalTitle.getSelectedEntity());
				Address address = new Address();
				address = financialIncomeAddressPanels.get(index).addressPanel.getAddress(address);
//				address.setHouseNo(financialIncomeAddressPanels.get(index).addressPanel.getTxtBuilding().getValue());
//				address.setLine1(financialIncomeAddressPanels.get(index).addressPanel.getTxtMoo().getValue());
//				address.setLine2(financialIncomeAddressPanels.get(index).addressPanel.getTxtSoi().getValue());
//				address.setStreet(financialIncomeAddressPanels.get(index).addressPanel.getTxtStreet().getValue());
//				address.setPostalCode(financialIncomeAddressPanels.get(index).addressPanel.getTxtPostalCode().getValue());
//				address.setProvince(financialIncomeAddressPanels.get(index).addressPanel.getCbxProvince().getSelectedEntity());
//				address.setDistrict(financialIncomeAddressPanels.get(index).addressPanel.getCbxDistrict().getSelectedEntity());
//				address.setCommune(financialIncomeAddressPanels.get(index).addressPanel.getCbxSubDistrict().getSelectedEntity());
				
				logger.debug("[>> saveOrUpdateAddress]");
				
				entityService.saveOrUpdate(address);
				
				logger.debug("Added new address for applicant = " + applicant.getIndividual().getFirstNameEn() + " " + applicant.getIndividual().getLastNameEn());
				logger.debug("[<< saveOrUpdateAddress]");
				
				employment.setIndividual(applicant.getIndividual());
				employment.setAddress(address);
				
				logger.debug("[>> saveOrUpdateEmployment]");
				
				entityService.saveOrUpdate(employment);
				
				logger.debug("[<< saveOrUpdateEmployment]");
				
				newEmployments.add(employment);
				applicant.getIndividual().setEmployments(newEmployments);
				
				index++;
			}
		}
	}
	
	public void assignValue(Employment employment) {
		cbxIncomeType.setSelectedEntity(employment.getEmploymentType());
		cbxCategoryIncome.setSelectedEntity(employment.getEmploymentCategory());
		cbxCompanyLegalTitle.setSelectedEntity(employment.getLegalForm());
		txtCompanyName.setValue(getDefaultString(employment.getEmployerName()));
		txtJobTitle.setValue(getDefaultString(employment.getPosition()));
		txtTimeWorkingInYear.setValue(getDefaultString(employment.getTimeWithEmployerInYear()));
		txtTimeWorkingInMonth.setValue(getDefaultString(employment.getTimeWithEmployerInMonth()));
		txtIncome.setValue(getDefaultString(employment.getRevenue()));
		txtCompanyPhone.setValue(getDefaultString(employment.getWorkPhone()));
		
		//assign address Employment
		Address empAddress = employment.getAddress();
		if (empAddress != null) {
			addressPanel.assignValue(empAddress);
		}
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(190, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout createContractInfoPanel(boolean required){
		this.required = required;
		cbxIncomeType = getERefDataComboBox(EEmploymentType.values());
		cbxCategoryIncome = getERefDataComboBox(EEmploymentCategory.values());
		txtIncome = ComponentFactory.getTextField(false, 60, 190);
		txtJobTitle = ComponentFactory.getTextField(false, 60, 190);
		txtCompanyName = ComponentFactory.getTextField(false, 60, 190);
		
		txtTimeWorkingInYear = ComponentFactory.getTextField(false, 60, 50);
		txtTimeWorkingInMonth = ComponentFactory.getTextField(false, 60, 50);
		txtCompanyPhone = ComponentFactory.getTextField("company.phone",true, 60, 190);
		cbxCompanyLegalTitle = getERefDataComboBox(ELegalForm.values());
		
		addressPanel = new AddressPanel();
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(createFinancialInformaionPanel());
		return verticalLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public VerticalLayout createFinancialInformaionPanel(){
		String template = "financialAddressLayout";
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
		customLayout.addComponent(new Label(I18N.message("company.legal.title")), "lblCompanyLegalTitle");
		customLayout.addComponent(cbxCompanyLegalTitle, "cbxCompanyLegalTitle");	
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
			
		Panel financialInformationPanel = new Panel(I18N.message("financial.information"));
		financialInformationPanel.setWidth("420px");
		financialInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		financialInformationPanel.setContent(customLayout);
		
		VerticalLayout addressLayout = new VerticalLayout();
		addressLayout.addComponent(addressPanel);
		addressLayout.addComponent(new FormLayout(txtCompanyPhone));
		
		Panel addressJobInformationPanel = new Panel(I18N.message("address.job"));
		addressJobInformationPanel.setWidth("420px");
		addressJobInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addressJobInformationPanel.setContent(addressLayout);
	
		VerticalLayout financialAndAddressJobLayout = new VerticalLayout();
		financialAndAddressJobLayout.addComponent(financialInformationPanel);
		financialAndAddressJobLayout.addComponent(addressJobInformationPanel);
		
		return financialAndAddressJobLayout;
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {		
		super.reset();
		if (required) {
			checkMandatoryField(txtCompanyName, "company.name");
			checkMandatoryField(txtJobTitle, "job.title");
			checkMandatoryField(txtTimeWorkingInMonth, "months");
			checkMandatoryField(txtTimeWorkingInYear, "years");
			checkMandatoryField(txtIncome, "income");
			checkMandatoryField(txtCompanyPhone, "company.phone");
			checkMandatorySelectField(cbxIncomeType, "income.type");
			checkMandatorySelectField(cbxCategoryIncome, "category.income");
			checkMandatorySelectField(cbxCompanyLegalTitle, "company.legal.title");
			addressPanel.isValid();
		}
		return errors;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxIncomeType.setSelectedEntity(null);
		cbxCategoryIncome.setSelectedEntity(null);
		cbxCompanyLegalTitle.setSelectedEntity(null);
		txtIncome.setValue("");
		txtJobTitle.setValue("");
		txtCompanyName.setValue("");
		txtTimeWorkingInYear.setValue("");
		txtTimeWorkingInMonth.setValue("");
		txtCompanyPhone.setValue("");
		addressPanel.reset();
	}
}
