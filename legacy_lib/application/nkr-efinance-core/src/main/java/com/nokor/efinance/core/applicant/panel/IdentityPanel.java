package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Customer identity panel
 * @author ly.youhort
 */
public class IdentityPanel extends AbstractControlPanel {
	
	private static final long serialVersionUID = 710425425958548975L;
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
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
	
	private Button btnAddPhoneNumber;
	private Button btnShowPhoneNumber;
	
	private Quotation quotation;
	
	public IdentityPanel(String template) {
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
		
		txtMobilePhone1 = ComponentFactory.getTextField(false, 30, 100);
		txtMobilePhoneSpouse = ComponentFactory.getTextField(false, 30, 150);
		txtMobilePhone2 = ComponentFactory.getTextField(false, 30, 150);
		
		btnAddPhoneNumber = new Button();
		btnAddPhoneNumber.setStyleName(Runo.BUTTON_LINK);
		btnAddPhoneNumber.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		
		btnShowPhoneNumber = new Button();
		btnShowPhoneNumber.setStyleName(Runo.BUTTON_LINK);
		btnShowPhoneNumber.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		
		btnAddPhoneNumber.addClickListener(new ClickListener() {			
			private static final long serialVersionUID = -3348402917006996671L;
			@Override
			public void buttonClick(ClickEvent event) {
				
		        final Window winAddPhone = new Window();
				winAddPhone.setModal(true);
			    winAddPhone.setWidth(300, Unit.PIXELS);
			    winAddPhone.setHeight(220, Unit.PIXELS);
		        winAddPhone.setCaption(I18N.message("phone.number"));
		        
		        final TextField txtPhoneNumber = ComponentFactory.getTextField("phone.number", true, 30, 130);
		        
		        Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
					private static final long serialVersionUID = 3975121141565713259L;
					public void buttonClick(ClickEvent event) {
						winAddPhone.close();
		            }
		        });
				btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
				Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
					private static final long serialVersionUID = 8088485001713740490L;
					public void buttonClick(ClickEvent event) {
						if (!txtPhoneNumber.getValue().equals("")) {
							SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
							Comment comment = new Comment();
							comment.setDesc("Add a new phone number " + txtPhoneNumber.getValue() + " to this applicant");
							comment.setQuotation(quotation);
							comment.setUser(secUser);
							entityService.saveOrUpdate(comment);
							winAddPhone.close();
						} else {
							MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
									MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
									new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
						}
		            }
		        });
				btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
				
				NavigationPanel navigationPanel = new NavigationPanel();
				navigationPanel.addButton(btnSave);
				navigationPanel.addButton(btnCancel);
				VerticalLayout contentLayout = new VerticalLayout();
				contentLayout.setMargin(true);
				contentLayout.addComponent(new FormLayout(txtPhoneNumber));
				winAddPhone.setContent(new VerticalLayout(navigationPanel, contentLayout));
				
		        UI.getCurrent().addWindow(winAddPhone);
			}
		});
		
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
		identityLayout.addComponent(btnAddPhoneNumber, "btnAddPhoneNumber");
		identityLayout.addComponent(btnShowPhoneNumber, "btnShowPhoneNumber");
		
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
	
	/**
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		Individual individual = applicant.getIndividual();
		individual.setFirstName(txtFirstName.getValue());
		individual.setFirstNameEn(txtFirstNameEn.getValue());
		individual.setLastName(txtLastName.getValue());
		individual.setLastNameEn(txtLastNameEn.getValue());
		individual.setNickName(txtNickName.getValue());
		// TODO YLY
		// applicant.setFirstNameEnSpouse(txtFirstNameEnSpouse.getValue());
		// applicant.setLastNameEnSpouse(txtLastNameEnSpouse.getValue());
		individual.setNumberOfChildren(getInteger(txtNumberOfChildren));
		individual.setBirthDate(dfDateOfBirth.getValue());
		individual.setPlaceOfBirth(cbxPlaceOfBirth.getSelectedEntity());
		individual.setCivility(cbxCivility.getSelectedEntity());
		individual.setGender(cbxGender.getSelectedEntity());
		individual.setMaritalStatus(cbxMaritalStatus.getSelectedEntity());
		individual.setNationality(cbxNationality.getSelectedEntity());
		// TODO YLY
		// applicant.setMobilePhone(txtMobilePhone1.getValue());
		// applicant.setMobilePhone2(txtMobilePhone2.getValue());
		// applicant.setMobilePhoneSpouse(txtMobilePhoneSpouse.getValue());
		return applicant;
	}
	
	/**
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {
		Individual individual = applicant.getIndividual();
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
		// txtMobilePhone1.setValue(getDefaultString(applicant.getMobilePhone()));
		// txtMobilePhone2.setValue(getDefaultString(applicant.getMobilePhone2()));
		// txtFirstNameEnSpouse.setValue(getDefaultString(applicant.getFirstNameEnSpouse()));
		// txtLastNameEnSpouse.setValue(getDefaultString(applicant.getLastNameEnSpouse()));
		// txtMobilePhoneSpouse.setValue(getDefaultString(applicant.getMobilePhoneSpouse()));
		txtNumberOfChildren.setValue(getDefaultString(individual.getNumberOfChildren()));
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
	public List<String> validate() {
		super.reset();
		checkMandatoryField(txtFirstNameEn, "firstname.en");
		checkMandatoryField(txtLastNameEn, "lastname.en");
		checkMandatoryDateField(dfDateOfBirth, "dateofbirth");
		return errors;
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {
		super.reset();
		checkMandatorySelectField(cbxCivility, "civility");
		checkMandatoryField(txtFirstName, "firstname");
		checkMandatoryField(txtFirstNameEn, "firstname.en");
		checkMandatoryField(txtLastName, "lastname");
		checkMandatoryField(txtLastNameEn, "lastname.en");
		checkMandatorySelectField(cbxGender, "gender");
		checkMandatorySelectField(cbxMaritalStatus, "marital.status");
		checkMandatorySelectField(cbxNationality, "nationality");
		checkMandatorySelectField(cbxPlaceOfBirth, "placeofbirth");
		checkMandatoryDateField(dfDateOfBirth, "dateofbirth");
		checkMandatoryField(txtMobilePhone1, "mobile.phone1");
		return errors;
	}
	
	public void setEnableIdentityPanel(boolean istrue) {
		txtFirstName.setEnabled(istrue);
		txtFirstNameEn.setEnabled(istrue);
		txtLastName.setEnabled(istrue);
		txtLastNameEn.setEnabled(istrue);
		txtNickName.setEnabled(istrue);
		dfDateOfBirth.setEnabled(istrue);
		cbxPlaceOfBirth.setEnabled(istrue);
		cbxCivility.setEnabled(istrue);
		cbxGender.setEnabled(istrue);
		cbxMaritalStatus.setEnabled(istrue);
		cbxNationality.setEnabled(istrue);
		txtMobilePhone1.setEnabled(istrue);
		txtMobilePhone2.setEnabled(istrue);
		txtFirstNameEnSpouse.setEnabled(istrue);
		txtLastNameEnSpouse.setEnabled(istrue);
		txtMobilePhoneSpouse.setEnabled(istrue);
		txtNumberOfChildren.setEnabled(istrue);
	}
}
