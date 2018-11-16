package com.nokor.efinance.gui.ui.panel.contract.user.driver;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Driver;
import com.nokor.efinance.core.applicant.service.DriverRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ENationality;
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
 * Driver information 
 * @author uhout.cheng
 */
public class DriverGeneralInfoPanel extends AbstractTabPanel implements FinServicesHelper, ValueChangeListener, ClickListener {
	
	/** */
	private static final long serialVersionUID = -5274817440354654657L;
	
	private final static String TEMPLATE = "user/driver/driverInformationLayout";
	
	private ERefDataComboBox<ENationality> cbxNationalityValue;
	private ERefDataComboBox<ETypeIdNumber> cbxIDTypeValue;
	private TextField txtFirstNameValue;
	private TextField txtIDNumberValue;
	private TextField txtLastNameValue;
	private TextField txtNickNameValue;
	private TextField txtAgeValue;
	private AutoDateField dfIssuingDateValue;
	private AutoDateField dfExpiringDateValue;
	private AutoDateField dfDOBValue;
	
	private Button btnSave;
	private Button btnEdit;
	
	private Driver driver;
	
	private DriverInformationTab driverInformationTab;
	
	/**
	 * 
	 * @param driverInformationTab
	 */
	public DriverGeneralInfoPanel(DriverInformationTab driverInformationTab) {
		this.driverInformationTab = driverInformationTab;
	}

	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(160, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxNationalityValue = getERefDataComboBox(ENationality.values());
		cbxIDTypeValue = getERefDataComboBox(ETypeIdNumber.values());
		
		txtFirstNameValue = ComponentFactory.getTextField(30, 160);
		txtIDNumberValue = ComponentFactory.getTextField(30, 160);
		txtLastNameValue = ComponentFactory.getTextField(30, 160);
		txtNickNameValue = ComponentFactory.getTextField(30, 160);
		txtAgeValue = ComponentFactory.getTextField(5, 160);
		
		dfIssuingDateValue = ComponentFactory.getAutoDateField();
		dfExpiringDateValue = ComponentFactory.getAutoDateField();
		dfDOBValue = ComponentFactory.getAutoDateField();
		txtAgeValue.setEnabled(false);
		
		dfDOBValue.addValueChangeListener(this);
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.setVisible(false);
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setEnabled(true);
		btnEdit.addClickListener(this);
		
		return getSummaryDetailPanel();
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
		customLayout.addComponent(getLabel("nationality"), "lblNationality");
		customLayout.addComponent(cbxNationalityValue, "cbxNationalityValue");
		
		customLayout.addComponent(getLabel("id.type"), "lblIDType");
		customLayout.addComponent(cbxIDTypeValue, "cbxIDTypeValue");
		customLayout.addComponent(getLabel("firstname"), "lblFirstName");
		customLayout.addComponent(txtFirstNameValue, "txtFirstNameValue");
		
		customLayout.addComponent(getLabel("id.number"), "lblIDNumber");
		customLayout.addComponent(txtIDNumberValue, "txtIDNumberValue");
		customLayout.addComponent(getLabel("lastname"), "lblLastName");
		customLayout.addComponent(txtLastNameValue, "txtLastNameValue");
		
		customLayout.addComponent(getLabel("issuing.date"), "lblIssuingDate");
		customLayout.addComponent(dfIssuingDateValue, "dfIssuingDateValue");
		customLayout.addComponent(getLabel("expiring.date"), "lblExpiringDate");
		customLayout.addComponent(dfExpiringDateValue, "dfExpiringDateValue");
		
		customLayout.addComponent(getLabel("nickname"), "lblNickName");
		customLayout.addComponent(txtNickNameValue, "txtNickNameValue");
		customLayout.addComponent(getLabel("dob"), "lblDOB");
		customLayout.addComponent(dfDOBValue, "dfDOBValue");
		
		customLayout.addComponent(getLabel("age"), "lblAge");
		customLayout.addComponent(txtAgeValue, "txtAgeValue");
		
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
		
		Panel panel = new Panel(generalLayout);
		return panel;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		DriverRestriction restrictions = new DriverRestriction();
		restrictions.setConId(contract.getId());
		
		if (ENTITY_SRV.list(restrictions) != null && !ENTITY_SRV.list(restrictions).isEmpty()) {
			this.driver = ENTITY_SRV.list(restrictions).get(0);
		}
		if (this.driver == null) {
			this.driver = Driver.createInstance();
			this.driver.setContract(contract);
			driverInformationTab.removedAddressTab();
		} else {
			cbxNationalityValue.setSelectedEntity(this.driver.getNationality());
			cbxIDTypeValue.setSelectedEntity(this.driver.getTypeIdNumber());
			txtFirstNameValue.setValue(getDefaultString(this.driver.getFirstNameLocale()));
			txtIDNumberValue.setValue(getDefaultString(this.driver.getIdNumber()));
			txtLastNameValue.setValue(getDefaultString(this.driver.getLastNameLocale()));
			dfIssuingDateValue.setValue(this.driver.getIssuingIdNumberDate());
			dfExpiringDateValue.setValue(this.driver.getExpiringIdNumberDate());
			txtNickNameValue.setValue(getDefaultString(this.driver.getNickName()));
			dfDOBValue.setValue(this.driver.getBirthDate());
			txtAgeValue.setValue(getDefaultString(calculateAge(this.driver.getBirthDate())));
			driverInformationTab.displayAddressTab(this.driver, false);
		}
		setEnableControls(btnSave.isVisible());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private Integer calculateAge(Date date) {
		Integer age = 0;
		if (date != null) {
			age = DateUtils.getNumberYearOfTwoDates(DateUtils.today(), date);
		}
		return age;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	public void reset() {
		super.removeErrorsPanel();
		this.driver = null;
		cbxNationalityValue.setSelectedEntity(null);
		cbxIDTypeValue.setSelectedEntity(null);
		txtFirstNameValue.setValue("");
		txtIDNumberValue.setValue("");
		txtLastNameValue.setValue("");
		txtNickNameValue.setValue("");
		txtAgeValue.setValue("");
		dfIssuingDateValue.setValue(null);
		dfExpiringDateValue.setValue(null);
		dfDOBValue.setValue(null);
	}

	/**
	 * Save Entity
	 */
	private void saveEntity() {
		this.driver.setNationality(cbxNationalityValue.getSelectedEntity());
		this.driver.setTypeIdNumber(cbxIDTypeValue.getSelectedEntity());
		this.driver.setIssuingIdNumberDate(dfIssuingDateValue.getValue());
		this.driver.setExpiringIdNumberDate(dfExpiringDateValue.getValue());
		this.driver.setBirthDate(dfDOBValue.getValue());
		this.driver.setFirstNameEn(txtFirstNameValue.getValue());
		this.driver.setIdNumber(txtIDNumberValue.getValue());
		this.driver.setLastNameEn(txtLastNameValue.getValue());
		this.driver.setNickName(txtNickNameValue.getValue());
		ENTITY_SRV.saveOrUpdate(this.driver);
	}

	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		checkMandatoryField(txtFirstNameValue, "firstname");
		checkMandatoryField(txtLastNameValue, "lastname");
		checkMandatoryDateField(dfDOBValue, "dob");
		return errors.isEmpty();
	}
	
	/**
	 * Set enable
	 * @param isEnable
	 */
	private void setEnableControls(boolean isEnable) {
		cbxNationalityValue.setEnabled(isEnable);
		cbxIDTypeValue.setEnabled(isEnable);
		txtFirstNameValue.setEnabled(isEnable);
		txtIDNumberValue.setEnabled(isEnable);
		txtLastNameValue.setEnabled(isEnable);
		txtNickNameValue.setEnabled(isEnable);
		txtAgeValue.setEnabled(isEnable);
		dfIssuingDateValue.setEnabled(isEnable);
		dfExpiringDateValue.setEnabled(isEnable);
		dfDOBValue.setEnabled(isEnable);
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(dfDOBValue)) {
			txtAgeValue.setValue(getDefaultString(calculateAge(dfDOBValue.getValue())));
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
					displaySuccess();
					driverInformationTab.displayAddressTab(this.driver, true);
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
