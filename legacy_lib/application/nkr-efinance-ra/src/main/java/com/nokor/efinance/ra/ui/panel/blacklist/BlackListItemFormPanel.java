package com.nokor.efinance.ra.ui.panel.blacklist;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.common.reference.model.EBlackListReason;
import com.nokor.efinance.core.common.reference.model.EBlackListSource;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BlackListItemFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = -6978374607842167163L;

	private BlackListItem blackListItem;
	
	private ERefDataComboBox<ETypeIdNumber> cbxIDType;
	private ERefDataComboBox<ECivility> cbxPrefix;
	private ERefDataComboBox<EMaritalStatus> cbxStatus;
	private ERefDataComboBox<ENationality> cbxNationality;
	private ERefDataComboBox<EBlackListSource> cbxSource;
	private ERefDataComboBox<EBlackListReason> cbxReason;
	private ERefDataComboBox<EGender> cbxGender;
	private ERefDataComboBox<EApplicantCategory> cbxApplicantCategory;
	private AutoDateField dfDateOfBirth;
	private AutoDateField dfIssuingDate;
	private AutoDateField dfExpiringDate;
	private TextField txtIDNumber;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtAge;
	private TextField txtPhoneNumber;
	private CheckBox cbActive;
	
	private TextField txtDetail;
	private TextField txtRemark;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		blackListItem.setTypeIdNumber(cbxIDType.getSelectedEntity());
		blackListItem.setIdNumber(txtIDNumber.getValue());
		blackListItem.setCivility(cbxPrefix.getSelectedEntity());
		blackListItem.setFirstNameEn(txtFirstName.getValue());
		blackListItem.setLastNameEn(txtLastName.getValue());
		blackListItem.setMobilePerso(txtPhoneNumber.getValue());
		blackListItem.setBirthDate(dfDateOfBirth.getValue());
		blackListItem.setIssuingIdNumberDate(dfIssuingDate.getValue());
		blackListItem.setExpiringIdNumberDate(dfExpiringDate.getValue());
		blackListItem.setMaritalStatus(cbxStatus.getSelectedEntity());
		blackListItem.setNationality(cbxNationality.getSelectedEntity());
		blackListItem.setSource(cbxSource.getSelectedEntity());
		blackListItem.setReason(cbxReason.getSelectedEntity());
		blackListItem.setGender(cbxGender.getSelectedEntity());
		blackListItem.setApplicantCategory(cbxApplicantCategory.getSelectedEntity());
		blackListItem.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		blackListItem.setDetails(txtDetail.getValue());
		blackListItem.setRemarks(txtRemark.getValue());
		return blackListItem;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxIDType = getERefDataComboBox("id.type", ETypeIdNumber.values(), true);
		cbxPrefix = getERefDataComboBox("prefix", ECivility.values(), true);
		cbxStatus = getERefDataComboBox("status", EMaritalStatus.values(), false);
		cbxSource = getERefDataComboBox("source", EBlackListSource.values(), false);
		cbxReason = getERefDataComboBox("reason", EBlackListReason.values(), false);
		cbxNationality = getERefDataComboBox("nationality", ENationality.values(), false);
		cbxGender = getERefDataComboBox("gender", EGender.values(), false);
		cbxApplicantCategory = getERefDataComboBox("customer.type", EApplicantCategory.values(), false);
		txtIDNumber = ComponentFactory.getTextField("id.number", true, 60, 170);
		txtFirstName = ComponentFactory.getTextField("firstname.en" ,false, 60, 170);
		txtLastName = ComponentFactory.getTextField("lastname.en", false, 60, 170);
		txtPhoneNumber = ComponentFactory.getTextField("phone.number", false, 10, 170);
		txtAge = ComponentFactory.getTextField("age", false, 5, 170);
		txtAge.setEnabled(false);
		dfDateOfBirth = ComponentFactory.getAutoDateField("dob", false);
		dfIssuingDate = ComponentFactory.getAutoDateField("issuing.id.date", false);
		dfExpiringDate = ComponentFactory.getAutoDateField("expiring.id.date", false);
		
		txtDetail = ComponentFactory.getTextField("detail", false, 60, 170);
		txtRemark = ComponentFactory.getTextField("remark", false, 60, 170);
		
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
		
		dfDateOfBirth.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 8833253039851225608L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				displayAge();
			}
		});
		
		cbxSource.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -6795864587792456206L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxSource.getSelectedEntity() != null) {
					List<EBlackListReason> blackListReasons = EBlackListReason.values(cbxSource.getSelectedEntity());
					cbxReason.assignValueMap(blackListReasons);
				} else {
					cbxReason.assignValueMap(EBlackListReason.values());
				}
			}
		});
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		
		FormLayout frmLayout = getFormLayout();
		frmLayout.addComponent(cbxIDType);
		frmLayout.addComponent(cbxPrefix);
		frmLayout.addComponent(txtLastName);
		frmLayout.addComponent(dfDateOfBirth);
		frmLayout.addComponent(dfIssuingDate);
		frmLayout.addComponent(cbxStatus);
		frmLayout.addComponent(txtPhoneNumber);
		frmLayout.addComponent(cbxSource);
		frmLayout.addComponent(txtDetail);
		frmLayout.addComponent(cbActive);
		horLayout.addComponent(frmLayout);
		
		frmLayout = getFormLayout();
		frmLayout.addComponent(txtIDNumber);
		frmLayout.addComponent(cbxGender);
		frmLayout.addComponent(txtFirstName);
		frmLayout.addComponent(txtAge);
		frmLayout.addComponent(dfExpiringDate);
		frmLayout.addComponent(cbxNationality);
		frmLayout.addComponent(cbxApplicantCategory);
		frmLayout.addComponent(cbxReason);
		frmLayout.addComponent(txtRemark);
		horLayout.addComponent(frmLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setContent(new VerticalLayout(horLayout));
		return mainPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout frmLayout = new FormLayout();
		frmLayout.setMargin(true);
		frmLayout.setStyleName("myform-align-left");
		return frmLayout;
	}
	
	/**
	 * @param blstId
	 */
	public void assignValues(Long blstId) {
		super.reset();
		if (blstId != null) {
			blackListItem = ENTITY_SRV.getById(BlackListItem.class, blstId);
			cbxIDType.setSelectedEntity(blackListItem.getTypeIdNumber());
			txtIDNumber.setValue(getDefaultString(blackListItem.getIdNumber()));
			cbxPrefix.setSelectedEntity(blackListItem.getCivility());
			txtFirstName.setValue(getDefaultString(blackListItem.getFirstNameEn()));
			txtLastName.setValue(getDefaultString(blackListItem.getLastNameEn()));
			txtPhoneNumber.setValue(getDefaultString(blackListItem.getMobilePerso()));
			dfDateOfBirth.setValue(blackListItem.getBirthDate());
			dfIssuingDate.setValue(blackListItem.getIssuingIdNumberDate());
			dfExpiringDate.setValue(blackListItem.getExpiringIdNumberDate());
			cbxStatus.setSelectedEntity(blackListItem.getMaritalStatus());
			cbxGender.setSelectedEntity(blackListItem.getGender());
			cbxApplicantCategory.setSelectedEntity(blackListItem.getApplicantCategory());
			cbxNationality.setSelectedEntity(blackListItem.getNationality());
			cbxSource.setSelectedEntity(blackListItem.getSource());
			cbxReason.setSelectedEntity(blackListItem.getReason());
			cbActive.setValue(blackListItem.getStatusRecord().equals(EStatusRecord.ACTIV));
			
			txtDetail.setValue(getDefaultString(blackListItem.getDetails()));
			txtRemark.setValue(getDefaultString(blackListItem.getRemarks()));
			displayAge();
		}
	}
	
	/**
	 * Calculate age with DOB
	 */
	private void displayAge() {
		Integer age = null;
		if (dfDateOfBirth.getValue() != null) {
			age = DateUtils.getYear(DateUtils.today()) - DateUtils.getYear(dfDateOfBirth.getValue());
		}
		txtAge.setValue(getDefaultString(age));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		blackListItem = BlackListItem.createInstance();
		cbxIDType.setSelectedEntity(null);
		cbxPrefix.setSelectedEntity(null);
		cbxStatus.setSelectedEntity(null);
		dfDateOfBirth.setValue(null);
		dfIssuingDate.setValue(null);
		dfExpiringDate.setValue(null);	
		txtIDNumber.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtPhoneNumber.setValue("");
		txtAge.setValue("");
		cbxGender.setSelectedEntity(null);
		cbxApplicantCategory.setSelectedEntity(null);
		cbxSource.setSelectedEntity(null);
		cbxReason.setSelectedEntity(null);
		cbxNationality.setSelectedEntity(null);
		cbActive.setValue(true);
		
		txtDetail.setValue("");
		txtRemark.setValue("");
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtIDNumber, "id.number");
		checkMandatorySelectField(cbxIDType, "id.type");
		checkMandatorySelectField(cbxPrefix, "prefix");
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @param caption
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T> getERefDataComboBox(String caption, List<T> values, boolean required) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(I18N.message(caption), values);
		comboBox.setWidth(170, Unit.PIXELS);
		comboBox.setRequired(required);
		return comboBox;
	}
}
