package com.nokor.efinance.gui.ui.panel.contract.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualSpouse;
import com.nokor.efinance.core.applicant.service.ApplicantSequenceImpl;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.SequenceManager;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.SequenceGenerator;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.ApplicantWkfStatus;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EEducation;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.EReligion;
import com.nokor.ersys.core.hr.model.eref.ETitle;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
 * User general tab 
 * @author uhout.cheng
 */
public class UserGeneralPanel extends AbstractTabPanel implements FinServicesHelper, ValueChangeListener, ClickListener {
	
	/** */
	private static final long serialVersionUID = -4700059647786180293L;
	
	private final static String TEMPLATE = "user/userGeneralPageLayout";
	private ERefDataComboBox<EApplicantCategory> cbxCustomerTypeValue;
	private ERefDataComboBox<ETitle> cbxTitleValue;
	private ERefDataComboBox<ETitle> cbxSpouseTitleValue;
	private ERefDataComboBox<EEducation> cbxEducationValue;
	private ERefDataComboBox<ENationality> cbxNationalityValue;
	private ERefDataComboBox<ECivility> cbxPrefixValue;
	private ERefDataComboBox<ECivility> cbxSpousePrefixValue;
	private ERefDataComboBox<EReligion> cbxReligionValue;
	private ERefDataComboBox<ETypeIdNumber> cbxIDTypeValue;
	private ERefDataComboBox<EMaritalStatus> cbxMaritalStatusValue;
	private ERefDataComboBox<ELanguage> cbxPreferedLanguageValue;
	private TextField txtFirstNameValue;
	private TextField txtIDNumberValue;
	private TextField txtLastNameValue;
	private TextField txtChildrenValue;
	private TextField txtMiddleNameValue;
	private TextField txtHouseHoldValue;
	private TextField txtNickNameValue;
	private TextField txtCustomerIDValue;
	private TextField txtContactNOValue;
	private TextField txtAgeValue;
	private ERefDataComboBox<EWkfStatus> cbxAppStatus;
	private TextField txtSpouseFirstNameValue;
	private TextField txtSpouseLastNameValue;
	private TextField txtSpouseMiddleNameValue;
	private TextField txtSpouseNickNameValue;
	private TextField txtSpouseAgeValue;
	private AutoDateField dfIssuingDateValue;
	private AutoDateField dfExpiringDateValue;
	private AutoDateField dfDOBValue;
	private AutoDateField dfCreationDateValue;
	private AutoDateField dfSpouseDOBValue;
	private Applicant applicant;
	
	private Button btnSave;
	private Button btnEdit;
	
	private ApplicantIndividualPanel delegate;
	
	private Contract contract;
	
	/**
	 * @param contract the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * 
	 * @param delegate
	 */
	public UserGeneralPanel(ApplicantIndividualPanel delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(200, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		List<EApplicantCategory> applicantCategories = new ArrayList<>();
		applicantCategories.add(EApplicantCategory.INDIVIDUAL);
		applicantCategories.add(EApplicantCategory.GLSTAFF);
		
		cbxCustomerTypeValue = getERefDataComboBox(applicantCategories);
		cbxTitleValue = getERefDataComboBox(ETitle.values());
		cbxSpouseTitleValue = getERefDataComboBox(ETitle.values());
		cbxEducationValue = getERefDataComboBox(EEducation.values());
		cbxNationalityValue = getERefDataComboBox(ENationality.values());
		cbxPrefixValue = getERefDataComboBox(ECivility.values());
		cbxSpousePrefixValue = getERefDataComboBox(ECivility.values());
		cbxReligionValue = getERefDataComboBox(EReligion.values());
		cbxIDTypeValue = getERefDataComboBox(ETypeIdNumber.values());
		cbxMaritalStatusValue = getERefDataComboBox(EMaritalStatus.values());
		cbxPreferedLanguageValue = getERefDataComboBox(ELanguage.values());
		
		txtFirstNameValue = ComponentFactory.getTextField(30, 200);
		txtIDNumberValue = ComponentFactory.getTextField(30, 200);
		txtLastNameValue = ComponentFactory.getTextField(30, 200);
		txtChildrenValue = ComponentFactory.getTextField(30, 200);
		txtMiddleNameValue = ComponentFactory.getTextField(30, 200);
		txtHouseHoldValue = ComponentFactory.getTextField(30, 200);
		txtNickNameValue = ComponentFactory.getTextField(30, 200);
		txtCustomerIDValue = ComponentFactory.getTextField(30, 200);
		txtContactNOValue = ComponentFactory.getTextField(30, 200);
		txtAgeValue = ComponentFactory.getTextField(5, 200);
		cbxAppStatus = getERefDataComboBox(ApplicantWkfStatus.values());
		txtSpouseFirstNameValue = ComponentFactory.getTextField(30, 200);
		txtSpouseLastNameValue = ComponentFactory.getTextField(30, 200);
		txtSpouseMiddleNameValue = ComponentFactory.getTextField(30, 200);
		txtSpouseNickNameValue = ComponentFactory.getTextField(30, 200);
		txtSpouseAgeValue = ComponentFactory.getTextField(30, 200);
		
		dfIssuingDateValue = ComponentFactory.getAutoDateField();
		dfExpiringDateValue = ComponentFactory.getAutoDateField();
		dfDOBValue = ComponentFactory.getAutoDateField();
		dfCreationDateValue = ComponentFactory.getAutoDateField();
		dfSpouseDOBValue = ComponentFactory.getAutoDateField();
		
		txtCustomerIDValue.setEnabled(false);
		cbxAppStatus.setEnabled(false);
		txtAgeValue.setEnabled(false);
		dfCreationDateValue.setEnabled(false);
		
		dfDOBValue.addValueChangeListener(this);
		dfSpouseDOBValue.addValueChangeListener(this);
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.setVisible(false);
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setEnabled(true);
		btnEdit.addClickListener(this);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(getSummaryDetailPanel());
		return mainLayout;
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
		customLayout.addComponent(getLabel("customer.type"), "lblCustomerType");
		customLayout.addComponent(cbxCustomerTypeValue, "cbxCustomerTypeValue");
		customLayout.addComponent(getLabel("title"), "lblTitle");
		customLayout.addComponent(cbxTitleValue, "cbxTitleValue");
		customLayout.addComponent(getLabel("education"), "lblEducation");
		customLayout.addComponent(cbxEducationValue, "cbxEducationValue");
		customLayout.addComponent(getLabel("nationality"), "lblNationality");
		customLayout.addComponent(cbxNationalityValue, "cbxNationalityValue");
		
		customLayout.addComponent(getLabel("prefix"), "lblPrefix");
		customLayout.addComponent(cbxPrefixValue, "cbxPrefixValue");
		customLayout.addComponent(getLabel("religion"), "lblReligion");
		customLayout.addComponent(cbxReligionValue, "cbxReligionValue");
		customLayout.addComponent(getLabel("id.type"), "lblIDType");
		customLayout.addComponent(cbxIDTypeValue, "cbxIDTypeValue");
		customLayout.addComponent(getLabel("firstname"), "lblFirstName");
		customLayout.addComponent(txtFirstNameValue, "txtFirstNameValue");
		
		customLayout.addComponent(getLabel("marital.status"), "lblMaritalStatus");
		customLayout.addComponent(cbxMaritalStatusValue, "cbxMaritalStatusValue");
		customLayout.addComponent(getLabel("id.number"), "lblIDNumber");
		customLayout.addComponent(txtIDNumberValue, "txtIDNumberValue");
		customLayout.addComponent(getLabel("lastname"), "lblLastName");
		customLayout.addComponent(txtLastNameValue, "txtLastNameValue");
		customLayout.addComponent(getLabel("children"), "lblChildren");
		customLayout.addComponent(txtChildrenValue, "txtChildrenValue");
		
		customLayout.addComponent(getLabel("issuing.date"), "lblIssuingDate");
		customLayout.addComponent(dfIssuingDateValue, "dfIssuingDateValue");
		customLayout.addComponent(getLabel("middlename"), "lblMiddleName");
		customLayout.addComponent(txtMiddleNameValue, "txtMiddleNameValue");
		customLayout.addComponent(getLabel("household"), "lblHouseHold");
		customLayout.addComponent(txtHouseHoldValue, "txtHouseHoldValue");
		customLayout.addComponent(getLabel("expiring.date"), "lblExpiringDate");
		customLayout.addComponent(dfExpiringDateValue, "dfExpiringDateValue");
		
		customLayout.addComponent(getLabel("nickname"), "lblNickName");
		customLayout.addComponent(txtNickNameValue, "txtNickNameValue");
		customLayout.addComponent(getLabel("prefered.language"), "lblPreferedLanguage");
		customLayout.addComponent(cbxPreferedLanguageValue, "cbxPreferedLanguageValue");
		customLayout.addComponent(getLabel("customer.id"), "lblCustomerID");
		customLayout.addComponent(txtCustomerIDValue, "txtCustomerIDValue");
		customLayout.addComponent(getLabel("dob"), "lblDOB");
		customLayout.addComponent(dfDOBValue, "dfDOBValue");
		
		customLayout.addComponent(getLabel("contact.no"), "lblContactNO");
		customLayout.addComponent(txtContactNOValue, "txtContactNOValue");
		customLayout.addComponent(getLabel("creation.date"), "lblCreationDate");
		customLayout.addComponent(dfCreationDateValue, "dfCreationDateValue");
		customLayout.addComponent(getLabel("age"), "lblAge");
		customLayout.addComponent(txtAgeValue, "txtAgeValue");
		customLayout.addComponent(getLabel("status"), "lblAppStatus");
		customLayout.addComponent(cbxAppStatus, "cbxAppStatus");
		
		customLayout.addComponent(getLabel("spouse.title"), "lblSpouseTitleValue");
		customLayout.addComponent(cbxSpouseTitleValue, "cbxSpouseTitleValue");
		customLayout.addComponent(getLabel("spouse.prefix"), "lblSpousePrefixValue");
		customLayout.addComponent(cbxSpousePrefixValue, "cbxSpousePrefixValue");
		
		customLayout.addComponent(getLabel("spouse.firstname"), "lblSpouseFirstName");
		customLayout.addComponent(txtSpouseFirstNameValue, "txtSpouseFirstNameValue");
		customLayout.addComponent(getLabel("spouse.lastname"), "lblSpouseLastName");
		customLayout.addComponent(txtSpouseLastNameValue, "txtSpouseLastNameValue");
		customLayout.addComponent(getLabel("spouse.middlename"), "lblSpouseMiddleName");
		customLayout.addComponent(txtSpouseMiddleNameValue, "txtSpouseMiddleNameValue");
		customLayout.addComponent(getLabel("spouse.nickname"), "lblSpouseNickName");
		customLayout.addComponent(txtSpouseNickNameValue, "txtSpouseNickNameValue");
		
		customLayout.addComponent(getLabel("spouse.dob"), "lblSpouseDOB");
		customLayout.addComponent(dfSpouseDOBValue, "dfSpouseDOBValue");
		customLayout.addComponent(getLabel("spouse.age"), "lblSpouseAge");
		customLayout.addComponent(txtSpouseAgeValue, "txtSpouseAgeValue");
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, false);
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
			cbxCustomerTypeValue.setSelectedEntity(applicant.getApplicantCategory());
			Individual individual = applicant.getIndividual();
			cbxTitleValue.setSelectedEntity(individual.getTitle());
			cbxEducationValue.setSelectedEntity(individual.getEducation());
			cbxNationalityValue.setSelectedEntity(individual.getNationality());
			cbxPrefixValue.setSelectedEntity(individual.getCivility());
			cbxReligionValue.setSelectedEntity(individual.getReligion());
			cbxIDTypeValue.setSelectedEntity(individual.getTypeIdNumber());
			txtFirstNameValue.setValue(getDefaultString(individual.getFirstNameLocale()));
			cbxMaritalStatusValue.setSelectedEntity(individual.getMaritalStatus());
			txtIDNumberValue.setValue(getDefaultString(individual.getIdNumber()));
			txtLastNameValue.setValue(getDefaultString(individual.getLastNameLocale()));
			txtChildrenValue.setValue(getDefaultString(getDefaultString(MyNumberUtils.getInteger(individual.getNumberOfChildren()))));
			dfIssuingDateValue.setValue(individual.getIssuingIdNumberDate());
			txtMiddleNameValue.setValue(individual.getMiddleNameEn());
			txtHouseHoldValue.setValue(getDefaultString(MyNumberUtils.getInteger(individual.getNumberOfHousehold())));
			dfExpiringDateValue.setValue(individual.getExpiringIdNumberDate());
			txtNickNameValue.setValue(getDefaultString(individual.getNickName()));
			cbxPreferedLanguageValue.setSelectedEntity(individual.getPreferredLanguage());
			txtCustomerIDValue.setValue(getDefaultString(individual.getReference()));
			dfDOBValue.setValue(individual.getBirthDate());
			txtContactNOValue.setValue(getDefaultString(individual.getMobilePerso()));
			dfCreationDateValue.setValue(individual.getCreateDate());
			txtAgeValue.setValue(getDefaultString(calculateAge(individual.getBirthDate())));
			cbxAppStatus.setSelectedEntity(individual.getWkfStatus());
			txtMiddleNameValue.setValue(getDefaultString(individual.getMiddleNameEn()));
			
			// Spouse
			IndividualSpouse spouse = individual.getIndividualSpouse();
			if (spouse != null) {
				cbxSpouseTitleValue.setSelectedEntity(spouse.getTitle());
				cbxSpousePrefixValue.setSelectedEntity(spouse.getCivility());
				txtSpouseFirstNameValue.setValue(getDefaultString(spouse.getFirstName()));
				txtSpouseLastNameValue.setValue(getDefaultString(spouse.getLastName()));
				txtSpouseMiddleNameValue.setValue(getDefaultString(spouse.getMiddleName()));
				txtSpouseNickNameValue.setValue(getDefaultString(spouse.getNickName()));
				dfSpouseDOBValue.setValue(spouse.getBirthDate());
				txtSpouseAgeValue.setValue(getDefaultString(calculateAge(spouse.getBirthDate())));
			}
		}
		setEnableControls(btnSave.isVisible());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String calculateAge(Date date) {
		String age = "";
		if (date != null) {
			int year = DateUtils.getYear(date);
			int month = DateUtils.getMonth(date);
			int day = DateUtils.getDay(date);
			
			LocalDate birthdate = new LocalDate(year, month, day);// Birthday 
			LocalDate now = new LocalDate();//Today
			Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
			
			age = period.getYears() + " Years " + period.getMonths() + " Months " + period.getDays() + " Days";
		}
		return age;
	}
	
	/**
	 * 
	 */
	public void reset() {
		cbxCustomerTypeValue.setSelectedEntity(null);
		cbxTitleValue.setSelectedEntity(null);
		cbxEducationValue.setSelectedEntity(null);
		cbxNationalityValue.setSelectedEntity(null);
		cbxPrefixValue.setSelectedEntity(null);
		cbxReligionValue.setSelectedEntity(null);
		cbxIDTypeValue.setSelectedEntity(null);
		cbxMaritalStatusValue.setSelectedEntity(null);
		cbxPreferedLanguageValue.setSelectedEntity(null);
		txtFirstNameValue.setValue("");
		txtIDNumberValue.setValue("");
		txtLastNameValue.setValue("");
		txtChildrenValue.setValue("");
		txtMiddleNameValue.setValue("");
		txtHouseHoldValue.setValue("");
		txtNickNameValue.setValue("");
		txtCustomerIDValue.setValue("");
		txtContactNOValue.setValue("");
		txtAgeValue.setValue("");
		cbxAppStatus.setSelectedEntity(null);
		dfIssuingDateValue.setValue(null);
		dfExpiringDateValue.setValue(null);
		dfDOBValue.setValue(null);
		dfCreationDateValue.setValue(null);
		resetSpouseInfo();
	}
	
	/**
	 */
	private void resetSpouseInfo() {
		cbxSpousePrefixValue.setSelectedEntity(null);
		cbxSpouseTitleValue.setSelectedEntity(null);
		txtSpouseFirstNameValue.setValue("");
		txtSpouseLastNameValue.setValue("");
		txtSpouseMiddleNameValue.setValue("");
		txtSpouseNickNameValue.setValue("");
		dfSpouseDOBValue.setValue(null);
		txtSpouseAgeValue.setValue("");
	}

	/**
	 * Save Entity
	 */
	private void saveEntity() {
		if (this.applicant == null) {
			this.applicant = Applicant.createInstance(cbxCustomerTypeValue.getSelectedEntity());
		}
		Individual individual = applicant.getIndividual();
		if (individual == null) {
			individual = Individual.createInstance();
			if (StringUtils.isNotEmpty(txtCustomerIDValue.getValue())) {
				individual.setReference(txtCustomerIDValue.getValue());
			} else {
				if (StringUtils.isEmpty(individual.getReference())) {
					Long sequence = SequenceManager.getInstance().getSequenceApplicant();
					SequenceGenerator sequenceGenerator = new ApplicantSequenceImpl(sequence);
					individual.setReference(sequenceGenerator.generate());
				}
			}
		}
		if (individual != null) {
			individual.setWkfStatus(cbxAppStatus.getSelectedEntity());
			individual.setTitle(cbxTitleValue.getSelectedEntity());
			individual.setEducation(cbxEducationValue.getSelectedEntity());
			individual.setNationality(cbxNationalityValue.getSelectedEntity());
			individual.setCivility(cbxPrefixValue.getSelectedEntity());
			individual.setReligion(cbxReligionValue.getSelectedEntity());
			individual.setTypeIdNumber(cbxIDTypeValue.getSelectedEntity());
			individual.setMaritalStatus(cbxMaritalStatusValue.getSelectedEntity());
			individual.setPreferredLanguage(cbxPreferedLanguageValue.getSelectedEntity());
			individual.setIssuingIdNumberDate(dfIssuingDateValue.getValue());
			individual.setExpiringIdNumberDate(dfExpiringDateValue.getValue());
			individual.setBirthDate(dfDOBValue.getValue());
			individual.setFirstNameEn(txtFirstNameValue.getValue());
			individual.setIdNumber(txtIDNumberValue.getValue());
			individual.setLastNameEn(txtLastNameValue.getValue());
			individual.setNumberOfChildren(getInteger(txtChildrenValue));
			individual.setNumberOfHousehold(getInteger(txtHouseHoldValue));
			individual.setNickName(txtNickNameValue.getValue());
			individual.setMobilePerso(txtContactNOValue.getValue());
			individual.setMiddleNameEn(txtMiddleNameValue.getValue());
			
			// Spouse
			IndividualSpouse spouse = individual.getIndividualSpouse();
			if (isSpouseDataFilled() || spouse != null) {
				if (spouse == null) {
					spouse = IndividualSpouse.createInstance();
					individual.getIndividualSpouses().add(spouse);
				} else {
					spouse.setCrudAction(CrudAction.UPDATE);
				}
				spouse.setTitle(cbxSpouseTitleValue.getSelectedEntity());
				spouse.setCivility(cbxSpousePrefixValue.getSelectedEntity());
				spouse.setFirstName(txtSpouseFirstNameValue.getValue());
				spouse.setLastName(txtSpouseLastNameValue.getValue());
				spouse.setNickName(txtSpouseNickNameValue.getValue());
				spouse.setMiddleName(txtSpouseMiddleNameValue.getValue());
				spouse.setBirthDate(dfSpouseDOBValue.getValue());
			}
			if (individual.getId() != null) {
				INDIVI_SRV.saveOrUpdateIndividual(individual);
			} else {
				this.applicant.setIndividual(individual);
				APP_SRV.createNewGuarantor(this.applicant, this.contract);
			}
		}
	}
	
	/**
	 * @return
	 */
	private boolean isSpouseDataFilled() {
		return cbxSpouseTitleValue.getSelectedEntity() != null
				|| cbxSpousePrefixValue.getSelectedEntity() != null
				|| StringUtils.isNotEmpty(txtSpouseFirstNameValue.getValue())
				|| StringUtils.isNotEmpty(txtSpouseLastNameValue.getValue())
				|| StringUtils.isNotEmpty(txtSpouseMiddleNameValue.getValue())
				|| StringUtils.isNotEmpty(txtSpouseNickNameValue.getValue())
				|| dfSpouseDOBValue.getValue() != null;
	}

	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		checkIntegerField(txtChildrenValue, "children");
		checkIntegerField(txtHouseHoldValue, "household");
		checkMandatoryDateField(dfDOBValue, "dob");
		checkMandatoryDateField(dfIssuingDateValue, "issuing.date");
		checkMandatoryDateField(dfExpiringDateValue, "expiring.date");
		checkMandatorySelectField(cbxCustomerTypeValue, "customer.type");
		checkMandatorySelectField(cbxPrefixValue, "prefix");
		checkMandatorySelectField(cbxMaritalStatusValue, "marital.status");
		checkMandatorySelectField(cbxIDTypeValue, "id.type");
		checkMandatorySelectField(cbxAppStatus, "status");
		checkMandatoryField(txtFirstNameValue, "firstname");
		checkMandatoryField(txtLastNameValue, "lastname");
		checkMandatoryField(txtIDNumberValue, "id.number");
		checkMandatoryField(txtChildrenValue, "children");
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	public void createNewGuarantor() {
		setEnableControls(true);
		btnSave.setVisible(true);
		btnEdit.setVisible(false);
	}
	
	/**
	 * Set enable
	 * @param isEnable
	 */
	private void setEnableControls(boolean isEnable) {
		cbxCustomerTypeValue.setEnabled(isEnable);
		cbxTitleValue.setEnabled(isEnable);
		cbxEducationValue.setEnabled(isEnable);
		cbxNationalityValue.setEnabled(isEnable);
		cbxPrefixValue.setEnabled(isEnable);
		cbxReligionValue.setEnabled(isEnable);
		cbxIDTypeValue.setEnabled(isEnable);
		cbxMaritalStatusValue.setEnabled(isEnable);
		cbxPreferedLanguageValue.setEnabled(isEnable);
		txtFirstNameValue.setEnabled(isEnable);
		txtIDNumberValue.setEnabled(isEnable);
		txtLastNameValue.setEnabled(isEnable);
		txtChildrenValue.setEnabled(isEnable);
		txtMiddleNameValue.setEnabled(isEnable);
		txtHouseHoldValue.setEnabled(isEnable);
		txtNickNameValue.setEnabled(isEnable);
		txtContactNOValue.setEnabled(isEnable);
		txtAgeValue.setEnabled(isEnable);
		cbxAppStatus.setEnabled(isEnable);
		dfIssuingDateValue.setEnabled(isEnable);
		dfExpiringDateValue.setEnabled(isEnable);
		dfDOBValue.setEnabled(isEnable);
		dfCreationDateValue.setEnabled(isEnable);
		
		// Spouse
		cbxSpousePrefixValue.setEnabled(isEnable);
		cbxSpouseTitleValue.setEnabled(isEnable);
		txtSpouseFirstNameValue.setEnabled(isEnable);
		txtSpouseLastNameValue.setEnabled(isEnable);
		txtSpouseMiddleNameValue.setEnabled(isEnable);
		txtSpouseNickNameValue.setEnabled(isEnable);
		dfSpouseDOBValue.setEnabled(isEnable);
		txtSpouseAgeValue.setEnabled(isEnable);
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(dfDOBValue)) {
			txtAgeValue.setValue(getDefaultString(calculateAge(dfDOBValue.getValue())));
		} else if (event.getProperty().equals(dfSpouseDOBValue)) {
			txtSpouseAgeValue.setValue(getDefaultString(calculateAge(dfSpouseDOBValue.getValue())));
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			setEnableControls(true);
			btnEdit.setVisible(false);
			btnSave.setVisible(true);
		} else if (event.getButton() == btnSave) {
			errors.clear();
			if (validate()) {
				try {
					saveEntity();
					delegate.refresh();
					displaySuccess();
					
					setEnableControls(false);
					btnSave.setVisible(false);
					btnEdit.setVisible(true);
				} catch (Exception ex) {
					errors.add(I18N.message("msg.error.technical"));
					errors.add(ex.getMessage());
				}
				if (!errors.isEmpty()) {
					displayErrorsPanel();
				}
			} else {
				displayErrorsPanel();
			}
		}
	}
	
}
