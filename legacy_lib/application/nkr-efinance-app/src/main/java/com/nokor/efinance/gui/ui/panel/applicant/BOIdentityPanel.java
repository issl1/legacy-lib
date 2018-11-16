package com.nokor.efinance.gui.ui.panel.applicant;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Customer identity panel
 * @author sok.vina
 */
public class BOIdentityPanel extends AbstractControlPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
		
	private TextField txtFirstName;
	private TextField txtFirstNameEn;
	private TextField txtFirstNameEnSpouse;
	private TextField txtLastName;
	private TextField txtLastNameEn;
	private TextField txtNickName;
	private TextField txtLastNameEnSpouse;
	private TextField txtNumberOfChildren;
	private AutoDateField dfDateOfBirth;
	private EntityRefComboBox<Province> cbxPlaceOfBirth; 
	
	private ERefDataComboBox<ECivility> cbxCivility;
	private ERefDataComboBox<EMaritalStatus> cbxMaritalStatus;
	private ERefDataComboBox<EGender> cbxGender;
	private ERefDataComboBox<ENationality> cbxNationality;
	
	private TextField txtMobilePhone1;
	private TextField txtMobilePhone2;
	private TextField txtMobilePhoneSpouse;
			
	public BOIdentityPanel(String template) {

		setMargin(true);
		setSizeFull();
		
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout identityLayout = null;
		try {
			identityLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		txtFirstName = ComponentFactory.getTextField35(false, 50, 150);
		txtFirstNameEn = ComponentFactory.getTextField(false, 50, 150);
		txtFirstNameEnSpouse = ComponentFactory.getTextField(false, 50, 150);
		txtLastName = ComponentFactory.getTextField35(false, 50, 150);
		txtLastNameEn = ComponentFactory.getTextField(false, 50, 150);
		txtNickName = ComponentFactory.getTextField35(false, 50, 150);
		txtLastNameEnSpouse = ComponentFactory.getTextField(false, 50, 150);
		txtNumberOfChildren = ComponentFactory.getTextField(false, 50, 100);
		dfDateOfBirth = ComponentFactory.getAutoDateField("", false);
		
		cbxPlaceOfBirth = new EntityRefComboBox<Province>();
		cbxPlaceOfBirth.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxPlaceOfBirth.renderer();
		
		cbxNationality = new ERefDataComboBox<ENationality>(ENationality.values());
						
		cbxCivility = new ERefDataComboBox<ECivility>(ECivility.values());
		cbxCivility.setSelectedEntity(ECivility.MR);
		
		cbxMaritalStatus = new ERefDataComboBox<EMaritalStatus>(EMaritalStatus.values());
		cbxMaritalStatus.setSelectedEntity(EMaritalStatus.SINGLE);
		
		cbxGender = new ERefDataComboBox<EGender>(EGender.values());
		cbxGender.setSelectedEntity(EGender.M);
		
		txtMobilePhone1 = ComponentFactory.getTextField(false, 30, 150);
		txtMobilePhoneSpouse = ComponentFactory.getTextField(false, 30, 150);
		txtMobilePhone2 = ComponentFactory.getTextField(false, 30, 150);
        
		identityLayout.addComponent(new Label(I18N.message("civility")), "lblCivility");
		identityLayout.addComponent(cbxCivility, "cbxCivility");
		
		identityLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstNameEn");
		identityLayout.addComponent(txtFirstNameEn, "txtFirstNameEn");
		
		identityLayout.addComponent(new Label(I18N.message("firstname.en.spouse")), "lblFirstNameEnSpouse");
		identityLayout.addComponent(txtFirstNameEnSpouse, "txtFirstNameEnSpouse");
		
		identityLayout.addComponent(new Label(I18N.message("firstname")), "lblFirstName");		
		identityLayout.addComponent(txtFirstName, "txtFirstName");
		
		identityLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastNameEn");
		identityLayout.addComponent(txtLastNameEn, "txtLastNameEn");
		
		identityLayout.addComponent(new Label(I18N.message("nickname")), "lblNickName");
		identityLayout.addComponent(txtNickName, "txtNickName");
		
		identityLayout.addComponent(new Label(I18N.message("lastname.en.spouse")), "lblLastNameEnSpouse");
		identityLayout.addComponent(txtLastNameEnSpouse, "txtLastNameEnSpouse");
		
		identityLayout.addComponent(new Label(I18N.message("number.of.children")), "lblNumberOfChildren");
		identityLayout.addComponent(txtNumberOfChildren, "txtNumberOfChildren");
		
		identityLayout.addComponent(new Label(I18N.message("lastname")), "lblLastName");
		identityLayout.addComponent(txtLastName, "txtLastName");
		
		identityLayout.addComponent(new Label(I18N.message("marital.status")), "lblMaritalStatus");
		identityLayout.addComponent(cbxMaritalStatus, "cbxMaritalStatus");
		
		identityLayout.addComponent(new Label(I18N.message("gender")), "lblGender");
		identityLayout.addComponent(cbxGender, "cbxGender");
		
		identityLayout.addComponent(new Label(I18N.message("placeofbirth")), "lblPlaceOfBirth");
		identityLayout.addComponent(cbxPlaceOfBirth, "cbxPlaceOfBirth");
		
		identityLayout.addComponent(new Label(I18N.message("nationality")), "lblNationality");
		identityLayout.addComponent(cbxNationality, "cbxNationality");
		
		identityLayout.addComponent(new Label(I18N.message("dateofbirth")), "lblDateOfBirth");
		identityLayout.addComponent(dfDateOfBirth, "dfDateOfBirth");
		
		identityLayout.addComponent(new Label(I18N.message("mobile.phone1")), "lblMobilePhone1");
		identityLayout.addComponent(txtMobilePhone1, "txtMobilePhone1");
		
		identityLayout.addComponent(new Label(I18N.message("mobile.phone2")), "lblMobilePhone2");
		identityLayout.addComponent(txtMobilePhone2, "txtMobilePhone2");
		
		identityLayout.addComponent(new Label(I18N.message("mobile.phone.spouse")), "lblMobilePhoneSpouse");
		identityLayout.addComponent(txtMobilePhoneSpouse, "txtMobilePhoneSpouse");
		        
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(identityLayout);
		
		Panel contentPanel = new Panel(I18N.message("identity"));
		contentPanel.setSizeFull();
		contentPanel.setContent(verticalLayout);
        addComponent(contentPanel);
	}
	
	public Applicant getApplicant(Applicant applicant) {
		Individual individual = applicant.getIndividual();
		individual.setFirstName(txtFirstName.getValue());
		individual.setFirstNameEn(txtFirstNameEn.getValue());
		individual.setLastName(txtLastName.getValue());
		individual.setLastNameEn(txtLastNameEn.getValue());
		individual.setNickName(txtNickName.getValue());
		// TODO YLY
		// individual.setFirstNameEnSpouse(txtFirstNameEnSpouse.getValue());
		// individual.setLastNameEnSpouse(txtLastNameEnSpouse.getValue());
		individual.setNumberOfChildren(getInteger(txtNumberOfChildren));
		individual.setBirthDate(dfDateOfBirth.getValue());
		individual.setPlaceOfBirth(cbxPlaceOfBirth.getSelectedEntity());
		individual.setCivility(cbxCivility.getSelectedEntity());
		individual.setGender(cbxGender.getSelectedEntity());
		individual.setMaritalStatus(cbxMaritalStatus.getSelectedEntity());
		individual.setNationality(cbxNationality.getSelectedEntity());
		// TODO YLY
		// individual.setMobilePhone(txtMobilePhone1.getValue());
		// individual.setMobilePhone2(txtMobilePhone2.getValue());
		// individual.setMobilePhoneSpouse(txtMobilePhoneSpouse.getValue());
		
		return applicant;
	}
	
	/**
	 * @param boApplicant
	 */
	public void assignValues(Applicant boApplicant) {
		Individual individual = boApplicant.getIndividual();
		txtFirstName.setValue(getDefaultString(individual.getFirstName()));
		txtFirstNameEn.setValue(getDefaultString(individual.getFirstNameEn()));
		txtLastName.setValue(getDefaultString(individual.getLastName()));
		txtLastNameEn.setValue(getDefaultString(individual.getLastNameEn()));
		txtNickName.setValue(getDefaultString(individual.getNickName()));
		dfDateOfBirth.setValue(individual.getBirthDate());
		cbxPlaceOfBirth.setSelectedEntity(individual.getPlaceOfBirth());
		cbxCivility.setSelectedEntity(individual.getCivility());
		cbxGender.setSelectedEntity(individual.getGender());
		cbxMaritalStatus.setSelectedEntity(individual.getMaritalStatus());
		cbxNationality.setSelectedEntity(individual.getNationality());
		// TODO YLY
		// txtMobilePhone1.setValue(getDefaultString(individual.getMobilePhone()));
		// txtMobilePhone2.setValue(getDefaultString(individual.getMobilePhone2()));
		// txtFirstNameEnSpouse.setValue(getDefaultString(individual.getFirstNameEnSpouse()));
		// txtLastNameEnSpouse.setValue(getDefaultString(individual.getLastNameEnSpouse()));
		// txtMobilePhoneSpouse.setValue(getDefaultString(individual.getMobilePhoneSpouse()));
		txtNumberOfChildren.setValue(getDefaultString(individual.getNumberOfChildren()));
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Applicant());
	}
}
