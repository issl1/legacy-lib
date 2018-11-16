package com.nokor.ersys.core.hr.holiday.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.PublicHoliday;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Holiday Form Panel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HolidayFormPanel extends AbstractFormPanel implements VaadinServicesHelper {
	/** */
	private static final long serialVersionUID = 6643485790099621425L;
	
	private Long entityId;
	private TextField txtCode;
	private AutoDateField dfDay;
	private TextField txtDescEn;
	private TextField txtDesc;
	private CheckBox cbActive;

	/**
	 * 
	 */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
	/**
	 * 
	 */
	@Override
	protected Entity getEntity() {
		PublicHoliday holiday = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			holiday = ENTITY_SRV.getById(PublicHoliday.class, getEntityId());
		} else {
			holiday = PublicHoliday.createInstance();
		}
		holiday.setCode(txtCode.getValue());
		holiday.setDay(dfDay.getValue());
		holiday.setDescEn(txtDescEn.getValue());
		holiday.setDesc(txtDesc.getValue());
		holiday.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return holiday;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		PublicHoliday holiday = (PublicHoliday) getEntity();
		boolean isUpdate = holiday.getId() != null && holiday.getId() > 0;
		if (isUpdate) {
			ENTITY_SRV.update(holiday);
		} else {
			ENTITY_SRV.create(holiday);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 50, 180);
		dfDay = ComponentFactory.getAutoDateField("holiday.date", true);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 80, 180);
		txtDesc = ComponentFactory.getTextField35("desc", true, 80, 180);
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);

		FormLayout formPanel = new FormLayout();
		formPanel.addComponent(txtCode);
		formPanel.addComponent(dfDay);
		formPanel.addComponent(txtDescEn);
		formPanel.addComponent(txtDesc);
		formPanel.addComponent(cbActive);
		
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(formPanel);
        
        Panel mainPanel = ComponentFactory.getPanel();        
		mainPanel.setContent(verticalLayout);        
		return mainPanel;
	}
	
	/**
	 * @param holId
	 */
	public void assignValues(Long holId) {
		if (holId != null) {
			reset();
			setEntityId(holId);
			PublicHoliday holiday = ENTITY_SRV.getById(PublicHoliday.class, holId);
			txtCode.setValue(holiday.getCode());
			dfDay.setValue(holiday.getDay());
			txtDescEn.setValue(holiday.getDescEn());
			txtDesc.setValue(holiday.getDesc());
			cbActive.setValue(holiday.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		txtCode.setValue("");
		dfDay.setValue(null);
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbActive.setValue(true);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryDateField(dfDay, "holiday.date");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryField(txtDesc, "desc");
		
		return errors.isEmpty();
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
