package com.nokor.efinance.ra.ui.panel.organization.employees;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.SecUserRestriction;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
/**
 * 
 * @author buntha.chea
 *
 */
public class OrgEmployeePopUpPanel extends Window implements FinServicesHelper, ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -524587346631689451L;
	
	private Employee employee;
	
	private Button btnSave;
	private Button btnCancel;
	private VerticalLayout messagePanel;
	private OrgEmployeesTablePanel tablePanel;
	
	private TextField txtReference;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfBirthdate;
	private Label lblAge;
	private ERefDataComboBox<ECivility> cbxCivility;
	private ERefDataComboBox<EGender> cbxGender;
	private ERefDataComboBox<ENationality> cbxNationality;
	private ERefDataComboBox<EJobPosition> cbxJobPosition;
	
	private TabSheet tabSheet;
	private OrgEmployeeAddressTabPanel empAddressTabPanel;
	
	private TextField txtPersonalEmail;
	private TextField txtMobile;
	
	private EntityComboBox<OrgStructure> cbxDepartment;
	private SecUserComboBox cbxSecUser;
	private List<SecUser> secUsers;
	
	public OrgEmployeePopUpPanel(OrgEmployeesTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		setModal(true);
		setCaption(I18N.message("employee"));
		
		init();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		txtFirstName = ComponentFactory.getTextField("fristname", true, 60, 200);
		
		VerticalLayout employeeLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		employeeLayout.addComponent(getPersonalInformation());
		employeeLayout.addComponent(getContactPanel());
		employeeLayout.addComponent(tabSheet);
			
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(employeeLayout);
		
		setContent(mainLayout);
	}
	
	private void init() {
		setHeight("100%");
		setWidth("70%");
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		empAddressTabPanel = new OrgEmployeeAddressTabPanel();
		
		tabSheet = new TabSheet();
		tabSheet.addTab(empAddressTabPanel, I18N.message("address"));
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getPersonalInformation() {
		txtReference = ComponentFactory.getTextField(10, 180); // Limit 10 Digit
		
		txtFirstName = ComponentFactory.getTextField35(false, 50, 180);
		txtLastName = ComponentFactory.getTextField35(false, 50, 180);
		
		txtFirstNameEn = ComponentFactory.getTextField( 50, 180);
		txtLastNameEn = ComponentFactory.getTextField( 50, 180);
		
		dfBirthdate = ComponentFactory.getAutoDateField();
		dfBirthdate.setDescription(I18N.message("dateField.tooltip"));
		dfBirthdate.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				lblAge.setValue(String.valueOf(DateUtils.getAge(dfBirthdate.getValue())) + " " + "year old");
			}
		});
		
		lblAge = ComponentFactory.getLabel();
		
		cbxCivility = new ERefDataComboBox<ECivility>(ECivility.class);
		cbxCivility.setWidth(180, Unit.PIXELS);

		cbxGender = new ERefDataComboBox<EGender>(EGender.values());
		cbxGender.setWidth(180, Unit.PIXELS);

		cbxNationality = new ERefDataComboBox<ENationality>(ENationality.values());
		cbxNationality.setWidth(180, Unit.PIXELS);
		
		cbxJobPosition = new ERefDataComboBox<EJobPosition>(EJobPosition.values());
		cbxJobPosition.setWidth(180, Unit.PIXELS);
		
		cbxDepartment = new EntityComboBox<OrgStructure>(OrgStructure.class, "nameEn");
		cbxDepartment.setImmediate(true);
		cbxDepartment.setEntities(getDepartments());
		secUsers = getSecUsers();
		for (Employee employee : getEmployeeSecUser(null)) {
			secUsers.remove(employee.getSecUser());
		}
		cbxSecUser = new SecUserComboBox(secUsers);
		
		String template = "employeepersonalInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/employee/" + template + ".html");
		CustomLayout personalInfoLayout = null;
		try {
			personalInfoLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		personalInfoLayout.setSizeFull();
		personalInfoLayout.addComponent(ComponentFactory.getLabel("code"), "lblCode");
		personalInfoLayout.addComponent(txtReference, "txtReference");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("civility"), "lblCivility");
		personalInfoLayout.addComponent(cbxCivility, "cbxCivility");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("department"), "lblDepartment");
		personalInfoLayout.addComponent(cbxDepartment, "cbxDepartment");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("firstname"), "lblFirstName");
		personalInfoLayout.addComponent(txtFirstName, "txtFirstName");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("lastname"), "lblLastName");
		personalInfoLayout.addComponent(txtLastName, "txtLastName");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("firstname.en"), "lblFirstNameEn");
		personalInfoLayout.addComponent(txtFirstNameEn, "txtFirstNameEn");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("lastname.en"), "lblLastNameEn");
		personalInfoLayout.addComponent(txtLastNameEn, "txtLastNameEn");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("date.birth"), "lblBirthdate");
		personalInfoLayout.addComponent(dfBirthdate, "dfBirthdate");
		personalInfoLayout.addComponent(lblAge,"lblAge");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("gender"), "lblGender");
		personalInfoLayout.addComponent(cbxGender, "cbxGender");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("nationality"), "lblNationality");
		personalInfoLayout.addComponent(cbxNationality, "cbxNationality");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("job.position"), "lblJobposition");
		personalInfoLayout.addComponent(cbxJobPosition, "cbxJobPosition");
		
		personalInfoLayout.addComponent(ComponentFactory.getLabel("user"), "lblUser");
		personalInfoLayout.addComponent(cbxSecUser, "cbxSecUser");
		
		Panel panel = new Panel(personalInfoLayout);
		panel.setCaption(I18N.message("personal.information"));
		
		return panel;
	}

	/**
	 * AssignValues
	 * @param orgEmpId
	 */
	public void assignValues(Long orgEmpId, Long orgId) {
		reset();
		if (orgEmpId != null) {
			employee = ENTITY_SRV.getById(Employee.class, orgEmpId);
			
			secUsers = getSecUsers();
			for (Employee employee : getEmployeeSecUser(employee)) {
				secUsers.remove(employee.getSecUser());
			}
			cbxSecUser.setValues(secUsers);
			
			txtReference.setValue(employee.getReference());
			cbxCivility.setSelectedEntity(employee.getCivility());
			txtFirstName.setValue(employee.getFirstName());
			txtFirstNameEn.setValue(employee.getFirstNameEn());
			txtLastName.setValue(employee.getLastName());
			txtLastNameEn.setValue(employee.getLastNameEn());
			dfBirthdate.setValue(employee.getBirthDate());
			cbxGender.setSelectedEntity(employee.getGender());
			cbxNationality.setSelectedEntity(employee.getNationality());
			cbxJobPosition.setSelectedEntity(employee.getJobPosition());
			txtPersonalEmail.setValue(employee.getEmailPerso());
			txtMobile.setValue(employee.getMobilePerso());
			cbxDepartment.setSelectedEntity(employee.getBranch());
			cbxSecUser.setSelectedEntity(employee.getSecUser());
		} else {
			employee = Employee.createInstance();
			employee.setOrganization(ENTITY_SRV.getById(Organization.class, orgId));
		}
		empAddressTabPanel.assignValue(employee);
	}
	
	/**
	 * Contact Information
	 * @return
	 */
	private Panel getContactPanel() {
		
		txtPersonalEmail = ComponentFactory.getTextField(false, 60, 220);
		txtMobile = ComponentFactory.getTextField(false, 60, 220);
		
		Label lblPersonalEmail = ComponentFactory.getLabel("email");
		Label lblMobile = ComponentFactory.getLabel("mobile");
		
		GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		gridLayout.addComponent(lblPersonalEmail, 0, 0);
		gridLayout.addComponent(txtPersonalEmail, 1, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS));
		gridLayout.addComponent(lblMobile, 3, 0);
		gridLayout.addComponent(txtMobile, 4, 0);
		
		gridLayout.setComponentAlignment(lblPersonalEmail, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblMobile, Alignment.MIDDLE_CENTER);
		
		Panel contactPanel = new Panel(gridLayout);
		contactPanel.setCaption(I18N.message("contact.information"));
		
		return contactPanel;
	}
	
	/**
	 * Show POPup window
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	private boolean validate() {
		List<String> errors = new ArrayList<String>();
		messagePanel.removeAllComponents();
		Label messageLabel;
		
		if (StringUtils.isEmpty(txtReference.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("code") }));
		}
		if (cbxCivility.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("civility") }));
		}
		if (StringUtils.isEmpty(txtFirstName.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("firstname") }));
		}
		if (StringUtils.isEmpty(txtLastName.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("lastname") }));
		}
		
		if (StringUtils.isEmpty(txtFirstNameEn.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("firstname.en") }));
		}
		if (StringUtils.isEmpty(txtLastNameEn.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("lastname.en") }));
		}
		if (cbxGender.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("gender") }));
		}
		if (dfBirthdate.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("birthdate") }));
		}
		
		if (cbxDepartment.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("department") }));
		}
		
		if (cbxSecUser.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("user") }));
		}
				
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * Save
	 */
	private void save() {
		try {
			employee.setReference(txtReference.getValue());
			employee.setCivility(cbxCivility.getSelectedEntity());
			employee.setFirstName(txtFirstName.getValue());
			employee.setFirstNameEn(txtFirstNameEn.getValue());
			employee.setLastName(txtLastName.getValue());
			employee.setLastNameEn(txtLastNameEn.getValue());
			employee.setBirthDate(dfBirthdate.getValue());
			employee.setGender(cbxGender.getSelectedEntity());
			employee.setNationality(cbxNationality.getSelectedEntity());
			employee.setJobPosition(cbxJobPosition.getSelectedEntity());
			employee.setEmailPerso(txtPersonalEmail.getValue());
			employee.setMobilePerso(txtMobile.getValue());
			employee.setBranch(cbxDepartment.getSelectedEntity());
			employee.setSecUser(cbxSecUser.getSelectedEntity());
			ENTITY_SRV.saveOrUpdate(employee.getAddress());
			ENTITY_SRV.saveOrUpdate(employee);
			Notification.show("", I18N.message("msg.info.save.successfully"), Type.HUMANIZED_MESSAGE);
			tablePanel.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			Notification.show("", ex.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtReference.setValue("");
		cbxCivility.setSelectedEntity(null);
		txtFirstName.setValue("");
		txtFirstNameEn.setValue("");
		txtLastName.setValue("");
		txtLastNameEn.setValue("");
		dfBirthdate.setValue(null);
		cbxGender.setSelectedEntity(null);
		cbxNationality.setSelectedEntity(null);
		cbxJobPosition.setSelectedEntity(null);
		cbxDepartment.setSelectedEntity(null);
		cbxSecUser.setSelectedEntity(null);
		empAddressTabPanel.reset();
	}
	
	/**
	 * get all department
	 * @return
	 */
	private List<OrgStructure> getDepartments() {
		BaseRestrictions<OrgStructure> restrictions = new BaseRestrictions<OrgStructure>(OrgStructure.class);
		restrictions.addCriterion(Restrictions.eq("level", EOrgLevel.DEPARTMENT));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * get all secuser
	 * @return
	 */
	private List<SecUser> getSecUsers() {
		SecUserRestriction restrictions = new SecUserRestriction();
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * get employee that have user already
	 * @return
	 */
	private List<Employee> getEmployeeSecUser(Employee employee) {
		BaseRestrictions<Employee> restrictions = new BaseRestrictions<Employee>(Employee.class);
		if (employee != null) {
			restrictions.addCriterion(Restrictions.ne(Employee.ID, employee.getId()));
		}
		restrictions.addCriterion(Restrictions.isNotNull("secUser"));
		return ENTITY_SRV.list(restrictions);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				save();
			}
		} if (event.getButton() == btnCancel) {
			close();
		}
		
	}
}
