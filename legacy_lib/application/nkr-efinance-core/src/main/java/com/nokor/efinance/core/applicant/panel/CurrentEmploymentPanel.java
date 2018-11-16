package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.ersys.core.hr.model.eref.ESeniorityLevel;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 */
public class CurrentEmploymentPanel extends AbstractControlPanel {
	
	private static final long serialVersionUID = -1284665400383791317L;
	
	private Employment employment;
	
	private TextField txtPosition;
	private TextField txtEmployerName;
	private TextField txtWorkPhone;
	private TextField txtTimeWithEmployerInYear;
	private TextField txtTimeWithEmployerInMonth;
	private Label lblRevenue;
	private TextField txtRevenue;
	private TextField txtAllowance;
	private TextField txtBusinessExpense;
	private CheckBox cbAllowCallToWorkPlace;
	private CheckBox cbSameApplicantAddress;
	private ERefDataComboBox<EEmploymentStatus> cbxEmploymentStatus;
	private ERefDataComboBox<EEmploymentIndustry> cbxEmploymentIndustry;
	private ERefDataComboBox<ESeniorityLevel> cbxSeniorityLevel;
	private AddressPanel addressFormPanel;
	private HorizontalLayout contentPanel;
	private Applicant applicant;
	private CustomLayout customLayout;
	public CurrentEmploymentPanel() {
		
		txtPosition = ComponentFactory.getTextField(false, 100, 250);
		txtTimeWithEmployerInYear = ComponentFactory.getTextField(false, 20, 50);
		txtTimeWithEmployerInMonth = ComponentFactory.getTextField(false, 20, 50);
		txtRevenue = ComponentFactory.getTextField(false, 50, 150);
		txtAllowance = ComponentFactory.getTextField(false, 50, 150);
		txtBusinessExpense = ComponentFactory.getTextField(false, 50, 150);
		txtEmployerName = ComponentFactory.getTextField(false, 100, 300);
		txtWorkPhone = ComponentFactory.getTextField(false, 30, 150);
		cbxEmploymentStatus = new ERefDataComboBox<EEmploymentStatus>("", EEmploymentStatus.class);
		cbxEmploymentStatus.setImmediate(true);
		cbxEmploymentIndustry = new ERefDataComboBox<EEmploymentIndustry>("", EEmploymentIndustry.class);
		cbxSeniorityLevel = new ERefDataComboBox<ESeniorityLevel>("", ESeniorityLevel.class);
		cbxSeniorityLevel.setImmediate(true);
		
		cbAllowCallToWorkPlace = new CheckBox();
		cbAllowCallToWorkPlace.setValue(false);
		cbSameApplicantAddress = new CheckBox(I18N.message("same.applicant.address"));
		cbSameApplicantAddress.setValue(false);
	
		lblRevenue = new Label(I18N.message("basic.salary"));
		
		String template = "currentEmployment";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("per.month")), "lblPerMonth");		
		customLayout.addComponent(new Label(I18N.message("work.place.name")), "lblEmployerName");
		customLayout.addComponent(txtEmployerName, "txtEmployerName");
		customLayout.addComponent(new Label(I18N.message("work.phone")), "lblWorkPhone");
		customLayout.addComponent(txtWorkPhone, "txtWorkPhone");
		customLayout.addComponent(new Label(I18N.message("time.with.employer")), "lblTimeWithEmployer");
		customLayout.addComponent(new Label(I18N.message("years")), "lblYears");
        customLayout.addComponent(new Label(I18N.message("months")), "lblMonths");
		customLayout.addComponent(txtTimeWithEmployerInYear, "txtTimeWithEmployerInYear");
		customLayout.addComponent(txtTimeWithEmployerInMonth, "txtTimeWithEmployerInMonth");
		customLayout.addComponent(new Label(I18N.message("position")), "lblPosition");
		customLayout.addComponent(txtPosition, "txtPosition");
		
		cbxEmploymentStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 3039130459507880370L;
			public void valueChange(ValueChangeEvent event) {
				if(cbxEmploymentStatus.getSelectedEntity() != null){
//					EEmploymentStatus status = ;
					cbxSeniorityLevel = new ERefDataComboBox<ESeniorityLevel>("", ESeniorityLevel.getByField(cbxEmploymentStatus.getSelectedEntity()));
				}else {
					cbxSeniorityLevel.setSelectedEntity(null);
				}
				if (cbxEmploymentStatus.getSelectedEntity() !=null 
						&& cbxEmploymentStatus.getSelectedEntity().getCode().equals("N")) {
					lblRevenue.setValue(I18N.message("basic.salary"));
					txtBusinessExpense.setValue("");
					txtBusinessExpense.setEnabled(false);
					txtAllowance.setEnabled(true);
				} else {
					lblRevenue.setValue(I18N.message("total.sales"));
					txtAllowance.setValue("");
					txtAllowance.setEnabled(false);
					txtBusinessExpense.setEnabled(true);
				}
				
			}
		});
		customLayout.addComponent(lblRevenue, "lblRevenue");
		customLayout.addComponent(txtRevenue, "txtRevenue");
		customLayout.addComponent(new Label(I18N.message("allowance.etc")), "lblAverageMonthlyAllowance");
		customLayout.addComponent(txtAllowance, "txtAverageMonthlyAllowance");
		customLayout.addComponent(new Label(I18N.message("business.expense")), "lblBusinessExpense");
		customLayout.addComponent(txtBusinessExpense, "txtBusinessExpense");
		customLayout.addComponent(new Label(I18N.message("employment.status")), "lblEmploymentStatus");
		customLayout.addComponent(cbxEmploymentStatus, "cbxEmploymentStatus");
		customLayout.addComponent(new Label(I18N.message("current.address")), "lblCurrentAddress");
		customLayout.addComponent(new Label(I18N.message("employment.industry")), "lblEmploymentIndustry");
		customLayout.addComponent(cbxEmploymentIndustry, "cbxEmploymentIndustry");
		customLayout.addComponent(new Label(I18N.message("seniorities.level")), "lblSeniorityLevel");
		customLayout.addComponent(cbxSeniorityLevel, "cbxSeniorityLevel");
		customLayout.addComponent(new Label(I18N.message("allow.call.to.work.place")), "lblAllowCallToWorkPlace");
		customLayout.addComponent(cbAllowCallToWorkPlace, "cbAllowCallToWorkPlace");
				
        addressFormPanel = new AddressPanel(true, ETypeAddress.WORK, "companyAddress");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(cbSameApplicantAddress);
        verticalLayout.addComponent(addressFormPanel);
        contentPanel = new HorizontalLayout();
        contentPanel.setMargin(true);
        contentPanel.setSpacing(true);
        contentPanel.addComponent(customLayout);
        contentPanel.addComponent(verticalLayout);
        addComponent(contentPanel);	
        
		cbSameApplicantAddress.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4186841321484425109L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Address applicantAddress = applicant != null ? applicant.getIndividual().getMainAddress() : null;
				Address address = (employment != null) ? employment.getAddress() : null;
				if (address == null) {
					address = new Address();
					address.setCountry(ECountry.KHM);
				}
				
				if (cbSameApplicantAddress.getValue()) {
					address = AddressUtils.copy(applicantAddress, address);
				}
				addressFormPanel.assignValues(address);
				addressFormPanel.setAddressEnabled(!cbSameApplicantAddress.getValue());
			}
		});
	}
	
	/**
	 * @param applicant
	 */
	public void setApplicant(Applicant applicant, EApplicantType applicantType) {
		this.applicant = applicant;
		if (applicantType == EApplicantType.C) {
			cbSameApplicantAddress.setCaption(I18N.message("same.applicant.address"));
		} else if (applicantType == EApplicantType.G) {
			cbSameApplicantAddress.setCaption(I18N.message("same.guarantor.address"));
		}
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Employment employment) {
		this.employment = employment;
		txtPosition.setValue(getDefaultString(employment.getPosition()));
		txtTimeWithEmployerInYear.setValue(getDefaultString(employment.getTimeWithEmployerInYear()));
		txtTimeWithEmployerInMonth.setValue(getDefaultString(employment.getTimeWithEmployerInMonth()));
		txtRevenue.setValue(AmountUtils.format(employment.getRevenue()));
		txtAllowance.setValue(AmountUtils.format(employment.getAllowance()));
		txtBusinessExpense.setValue(AmountUtils.format(employment.getBusinessExpense()));
		txtEmployerName.setValue(getDefaultString(employment.getEmployerName()));
		txtWorkPhone.setValue(getDefaultString(employment.getWorkPhone()));
		cbxEmploymentStatus.setSelectedEntity(employment.getEmploymentStatus());
		cbxEmploymentIndustry.setSelectedEntity(employment.getEmploymentIndustry());
		cbxSeniorityLevel.setSelectedEntity(employment.getSeniorityLevel());
		cbAllowCallToWorkPlace.setValue(employment.isAllowCallToWorkPlace());
		cbSameApplicantAddress.setValue(employment.isSameApplicantAddress());
		Address address = employment.getAddress();
		if (address == null) {
			address = new Address();
			address.setCountry(ECountry.KHM);
		}
		addressFormPanel.assignValues(address);
	}
	
	/**
	 * @param address
	 */
	public void assignAddressValues(Address address) {
		addressFormPanel.assignValues(address);
	}
	
	/**
	 * Get employment
	 * @param employment
	 * @return
	 */
	public Employment getEmployment(Employment employment) {
		employment.setPosition(txtPosition.getValue());
		employment.setTimeWithEmployerInYear(getInteger(txtTimeWithEmployerInYear));
		employment.setTimeWithEmployerInMonth(getInteger(txtTimeWithEmployerInMonth));
		employment.setRevenue(getDouble(txtRevenue));
		employment.setAllowance(getDouble(txtAllowance));
		employment.setBusinessExpense(getDouble(txtBusinessExpense));		
		employment.setEmployerName(txtEmployerName.getValue());
		employment.setWorkPhone(txtWorkPhone.getValue());
		employment.setEmploymentStatus(cbxEmploymentStatus.getSelectedEntity());
		employment.setEmploymentIndustry(cbxEmploymentIndustry.getSelectedEntity());
		employment.setSeniorityLevel(cbxSeniorityLevel.getSelectedEntity());
		employment.setEmploymentType(EEmploymentType.CURR);
		employment.setAllowCallToWorkPlace(cbAllowCallToWorkPlace.getValue());
		employment.setSameApplicantAddress(cbSameApplicantAddress.getValue());
		if (employment.getAddress() == null) {
			Address address = new Address();
			address.setCountry(ECountry.KHM);
			employment.setAddress(address);
		}
		employment.setAddress(addressFormPanel.getAddress(employment.getAddress()));
		return employment;
	}
	
	/**
	 * @return
	 */
	public boolean isSameApplicantAddress() {
		return cbSameApplicantAddress.getValue();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		applicant = null;
		assignValues(new Employment());
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {
		super.reset();
		checkMandatoryField(txtPosition, "position");
		checkMandatorySelectField(cbxEmploymentStatus, "employment.status");
		checkMandatorySelectField(cbxEmploymentIndustry, "employment.industry");
		checkMandatorySelectField(cbxSeniorityLevel, "seniorities.level");
		checkMandatoryField(txtRevenue, "revenue");
		// checkDoubleField(txtRevenue, "revenue");
		checkMandatoryField(txtEmployerName, "employer");
		checkMandatoryField(txtTimeWithEmployerInYear, "time.with.employer");
		checkMandatoryField(txtTimeWithEmployerInMonth, "time.with.employer");
		// checkMandatoryField(txtBusinessExpense, "business.expense");
		// checkDoubleField(txtBusinessExpense, "business.expense");
		errors.addAll(addressFormPanel.partialValidate());			
		return errors;
	}
}
