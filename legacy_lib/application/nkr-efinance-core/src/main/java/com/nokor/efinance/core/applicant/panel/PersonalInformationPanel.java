package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EEducation;
import com.nokor.common.app.eref.ELanguage;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.EReligion;
import com.nokor.ersys.core.hr.model.eref.ETitle;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Personal Information Panel
 * @author buntha.chea
 * */
public class PersonalInformationPanel extends AbstractTabPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	//Personal Information Part
	private ERefDataComboBox<ETypeIdNumber> cbxIDType;
	private ERefDataComboBox<ECivility> cbxPrefix;
	private ERefDataComboBox<ETitle> cbxTitle;
	private ERefDataComboBox<EMaritalStatus> cbxStatus;
	private ERefDataComboBox<EEducation> cbxEducation;
	private ERefDataComboBox<EReligion> cbxReligion;
	private ERefDataComboBox<ELanguage> cbxSecondLanguage;
	private ERefDataComboBox<ENationality> cbxNationality;
			
	private AutoDateField dfDateOfBirth;
	private AutoDateField dfIssuingDate;
	private AutoDateField dfExpiringDate;
	
	private TextField txtIDNumber;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtNickName;
	private TextField txtAge;
	private TextField txtHousehold;
	private TextField txtNbChildren;
	private Button btnCheckGuarantor;
	private CheckBox cbUndefine;
	private Applicant applicant;

	/** */
	public PersonalInformationPanel() {
		super();
		setSizeFull();	
	}	
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T> getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(180, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxIDType = new ERefDataComboBox<>(null, ETypeIdNumber.class);
		cbxIDType.setWidth(120, Unit.PIXELS);
		cbxPrefix = getERefDataComboBox(ECivility.values());
		cbxTitle = getERefDataComboBox(ETitle.values());
		cbxStatus = getERefDataComboBox(EMaritalStatus.values());
		cbxEducation = getERefDataComboBox(EEducation.values());
		cbxReligion = getERefDataComboBox(EReligion.values());
		cbxSecondLanguage = getERefDataComboBox(ELanguage.values());
		cbxNationality = new ERefDataComboBox<>(null, ENationality.class);
		
		txtNbChildren = ComponentFactory.getTextField(false, 60, 180);
		txtIDNumber = ComponentFactory.getTextField(false, 60, 150);
		txtFirstName = ComponentFactory.getTextField(false, 60, 180);
		txtLastName = ComponentFactory.getTextField(false, 60, 180);
		txtNickName = ComponentFactory.getTextField(false, 60, 180);
		txtAge = ComponentFactory.getTextField(false, 60, 180);
		txtHousehold = ComponentFactory.getTextField(false, 60, 180);
		
		dfDateOfBirth = ComponentFactory.getAutoDateField();
		dfIssuingDate = ComponentFactory.getAutoDateField();
		dfExpiringDate = ComponentFactory.getAutoDateField();
		
		btnCheckGuarantor = ComponentFactory.getButton("check");
		btnCheckGuarantor.setStyleName(Runo.BUTTON_SMALL);
		
		cbUndefine = new CheckBox(I18N.message("undefine"));
		cbUndefine.addListener(new Listener() {

			/** */
			private static final long serialVersionUID = 5529625002644788063L;

			/**
			 * @see com.vaadin.ui.Component.Listener#componentEvent(com.vaadin.ui.Component.Event)
			 */
			@Override
			public void componentEvent(Event event) {
				if (cbUndefine.getValue()) {
					dfExpiringDate.setEnabled(false);
					dfExpiringDate.setValue(null);
				} else {
					dfExpiringDate.setEnabled(true);
					dfExpiringDate.setValue(applicant.getIndividual().getExpiringIdNumberDate());
				}
			}
		});
		return createPersonalInformationPanel();
	}
	
	/**
	 * 
	 * @param applicant
	 * @return
	 */
	public Applicant getPersonalInformation(Applicant applicant) {
		if (applicant != null ) {
			Individual individual = applicant.getIndividual();  
			individual.setTypeIdNumber(cbxIDType.getSelectedEntity());
			individual.setIdNumber(txtIDNumber.getValue());
			individual.setTitle(cbxTitle.getSelectedEntity());
			individual.setCivility(cbxPrefix.getSelectedEntity());
			individual.setFirstNameEn(txtFirstName.getValue());
			individual.setLastNameEn(txtLastName.getValue());
			individual.setNickName(getDefaultString(txtNickName.getValue()));
			individual.setBirthDate(dfDateOfBirth.getValue());
			individual.setIssuingIdNumberDate(dfIssuingDate.getValue());
			individual.setExpiringIdNumberDate(dfExpiringDate.getValue());
			individual.setMaritalStatus(cbxStatus.getSelectedEntity());
			individual.setNumberOfChildren(getInteger(txtNbChildren));
			individual.setNationality(cbxNationality.getSelectedEntity());
			individual.setEducation(cbxEducation.getSelectedEntity());
			individual.setNumberOfHousehold(getInteger(txtHousehold));
			individual.setReligion(cbxReligion.getSelectedEntity());
			individual.setSecondLanguage(cbxSecondLanguage.getSelectedEntity());
		}
		return applicant;
	}
	
	/**
	 * 
	 * @param applicant
	 */
	public void assignValuePersonalInformaion(Applicant applicant) {
		this.applicant = applicant;
		if (applicant != null) {
			Individual individual = applicant.getIndividual();  
			cbxIDType.setSelectedEntity(individual.getTypeIdNumber());
			txtIDNumber.setValue(getDefaultString(individual.getIdNumber()));
			cbxTitle.setSelectedEntity(individual.getTitle());
			cbxPrefix.setSelectedEntity(individual.getCivility());
			txtFirstName.setValue(getDefaultString(individual.getFirstNameEn()));
			txtLastName.setValue(getDefaultString(individual.getLastNameEn()));
			txtNickName.setValue(getDefaultString(individual.getNickName()));
			dfDateOfBirth.setValue(individual.getBirthDate());
			Integer age = null;
			if (individual.getBirthDate() != null) {
				age = DateUtils.getYear(DateUtils.today()) - DateUtils.getYear(individual.getBirthDate());
			}
			txtAge.setValue(getDefaultString(age));
			dfIssuingDate.setValue(individual.getIssuingIdNumberDate());
			dfExpiringDate.setValue(individual.getExpiringIdNumberDate());
			cbxStatus.setSelectedEntity(individual.getMaritalStatus());
			txtNbChildren.setValue(getDefaultString(individual.getNumberOfChildren()));
			cbxEducation.setSelectedEntity(individual.getEducation());
			txtHousehold.setValue(getDefaultString(individual.getNumberOfHousehold()));
			cbxNationality.setSelectedEntity(individual.getNationality());
			cbxReligion.setSelectedEntity(individual.getReligion());
			cbxSecondLanguage.setSelectedEntity(individual.getSecondLanguage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Panel createPersonalInformationPanel(){
		String template = "personalInformation";
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
		customLayout.addComponent(new Label(I18N.message("title")), "lblTitle");
		customLayout.addComponent(cbxTitle, "cbxTitle");
		customLayout.addComponent(new Label(I18N.message("prefix")), "lblPrefix");
		customLayout.addComponent(cbxPrefix, "cbxprefixs");
		customLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		customLayout.addComponent(txtFirstName, "txtFirstName");
		customLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		customLayout.addComponent(txtLastName, "txtLastName");
		customLayout.addComponent(new Label(I18N.message("nickname")), "lblNickName");
		customLayout.addComponent(txtNickName, "txtNickName");
		customLayout.addComponent(new Label(I18N.message("dob")), "lblDateOfBirth");
		customLayout.addComponent(dfDateOfBirth, "dfDateOfBirth");
		customLayout.addComponent(new Label(I18N.message("age")), "lblAge");
		customLayout.addComponent(txtAge, "txtAge");
		customLayout.addComponent(new Label(I18N.message("issuing.id.date")), "lblIssuingDate");
		customLayout.addComponent(dfIssuingDate, "dfIssuingDate");
		customLayout.addComponent(new Label(I18N.message("expiring.id.date")), "lblExpiringDate");
		customLayout.addComponent(dfExpiringDate, "dfExpiringDate");
		customLayout.addComponent(cbUndefine, "cbUndefine");
		customLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		customLayout.addComponent(cbxStatus, "cbxStatus");
		customLayout.addComponent(new Label(I18N.message("number.of.children")), "lblChildren");
		customLayout.addComponent(txtNbChildren, "txtNbChildren");
		customLayout.addComponent(new Label(I18N.message("education")), "lblEducation");
		customLayout.addComponent(cbxEducation, "cbxEducation");
		customLayout.addComponent(new Label(I18N.message("household")), "lblHousehold");
		customLayout.addComponent(txtHousehold, "txtHousehold");
		customLayout.addComponent(new Label(I18N.message("nationality")), "lblNationality");
		customLayout.addComponent(cbxNationality, "cbxNationality");
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
	 * @return
	 */
	public boolean isValid() {		
		super.removeErrorsPanel();
		checkMandatoryField(txtIDNumber, "id.type");
		checkMandatoryField(txtLastName, "lastname.en");
		checkMandatoryField(txtFirstName, "firstname.en");
		checkMandatoryField(txtAge, "age");
		checkMandatoryField(txtNbChildren, "number.of.children");
		checkMandatoryDateField(dfDateOfBirth, "dob");
		checkMandatoryDateField(dfIssuingDate, "issuing.id.date");
		checkMandatoryDateField(dfExpiringDate, "expiring.id.date");
		checkMandatorySelectField(cbxIDType, "id.type");
		checkMandatorySelectField(cbxPrefix, "prefix");
		checkMandatorySelectField(cbxTitle, "title");
		checkMandatorySelectField(cbxStatus, "status");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		cbxIDType.setSelectedEntity(null);
		cbxPrefix.setSelectedEntity(null);
		cbxTitle.setSelectedEntity(null);
		cbxStatus.setSelectedEntity(null);
		cbxEducation.setSelectedEntity(null);
		cbxReligion.setSelectedEntity(null);
		cbxSecondLanguage.setSelectedEntity(null);
		dfDateOfBirth.setValue(null);
		dfIssuingDate.setValue(null);
		dfExpiringDate.setValue(null);	
		txtIDNumber.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtAge.setValue("");
		txtHousehold.setValue("");
		txtNbChildren.setValue("");
		cbUndefine.setValue(false);
	}
}
