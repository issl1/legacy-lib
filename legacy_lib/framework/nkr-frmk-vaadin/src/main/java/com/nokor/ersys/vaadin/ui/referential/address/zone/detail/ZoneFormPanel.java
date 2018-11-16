/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.zone.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Zone;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ZoneFormPanel extends AbstractFormPanel implements AppServicesHelper {

	/**	 */
	private static final long serialVersionUID = -5658300670903170527L;
	
	private Zone zone;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtLatitude;
    private TextField txtLongitude;
    private EntityRefComboBox<Province> cbxProvince;
    private CheckBox cbActive;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		zone.setCode(txtCode.getValue());
		zone.setDesc(txtDesc.getValue());
		zone.setDescEn(txtDescEn.getValue());
		zone.setLongitude(getDouble(txtLongitude));
		zone.setLatitude(getDouble(txtLatitude));
		zone.setProvince(cbxProvince.getSelectedEntity());
//		zone.setCollectionGroup(cbxCollectionGroup.getSelectedEntity());
		zone.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return zone;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 50, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		
		txtLatitude = ComponentFactory.getTextField("latitude", false, 50, 150);
		txtLongitude = ComponentFactory.getTextField("longitude", false, 50, 150);
		
		cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.setRequired(true);
		cbxProvince.setWidth(200, Unit.PIXELS);
		cbxProvince.renderer();
		
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtLatitude);
		formPanel.addComponent(txtLongitude);
        formPanel.addComponent(cbxProvince);
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
	 * @param asmakId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			zone = ENTITY_SRV.getById(Zone.class, id);
			txtCode.setValue(zone.getCode());
			txtDescEn.setValue(zone.getDescEn());
			txtDesc.setValue(zone.getDesc());
			txtLatitude.setValue(getDefaultString(zone.getLatitude()));
   			txtLongitude.setValue(getDefaultString(zone.getLongitude()));
			cbxProvince.setSelectedEntity(zone.getProvince());
//			cbxCollectionGroup.setSelectedEntity(zone.getCollectionGroup());
			cbActive.setValue(zone.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		zone = new Zone();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtLongitude.setValue("");
   		txtLatitude.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbActive.setValue(true);
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
   		checkMandatoryField(txtDesc, "desc");
   		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxProvince, "province");
		checkDoubleField(txtLatitude, "latitude");
		checkDoubleField(txtLongitude, "longitude");
		return errors.isEmpty();
	}
}
