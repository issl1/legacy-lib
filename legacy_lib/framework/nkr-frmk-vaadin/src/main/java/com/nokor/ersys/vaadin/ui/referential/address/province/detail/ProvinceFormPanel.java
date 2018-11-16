/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.province.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.eref.ECountry;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
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
public class ProvinceFormPanel extends AbstractFormPanel implements AppServicesHelper {
	/**	 */
	private static final long serialVersionUID = -4136626859626344397L;

	private Province province;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtShortCode;
    private TextField txtLatitude;
    private TextField txtLongitude;
    private ERefDataComboBox<ECountry> cbxCountry;
    private CheckBox cbActive;
    
    @PostConstruct
   	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   	}
   	
   	@Override
   	protected Entity getEntity() {
   		province.setCode(txtCode.getValue());
   		province.setDesc(txtDesc.getValue());
   		province.setDescEn(txtDescEn.getValue());
   		province.setShortCode(txtShortCode.getValue());
   		province.setLongitude(getDouble(txtLongitude));
   		province.setLatitude(getDouble(txtLatitude));
   		province.setCountry(cbxCountry.getSelectedEntity());
   		province.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
   		return province;
   	}

   	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		txtCode = ComponentFactory.getTextField("code", true, 50, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtShortCode = ComponentFactory.getTextField35("short.code", true, 15, 150);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		
		cbxCountry = new ERefDataComboBox<ECountry>(I18N.message("country"), ECountry.values());
		cbxCountry.setRequired(true);
		cbxCountry.setWidth(200, Unit.PIXELS);
		
		txtLatitude = ComponentFactory.getTextField("latitude", false, 50, 150);
		txtLongitude = ComponentFactory.getTextField("longitude", false, 50, 150);
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);
		formPanel.addComponent(txtCode);
		formPanel.addComponent(txtDesc);
		formPanel.addComponent(txtDescEn);
		formPanel.addComponent(txtShortCode);
		formPanel.addComponent(txtLatitude);
		formPanel.addComponent(txtLongitude);
		formPanel.addComponent(cbxCountry);
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
   			province = ENTITY_SRV.getById(Province.class, id);
   			txtCode.setValue(province.getCode());
   			txtDescEn.setValue(province.getDescEn());
   			txtDesc.setValue(province.getDesc());
   			txtShortCode.setValue(province.getShortCode());
   			txtLatitude.setValue(getDefaultString(province.getLatitude()));
   			txtLongitude.setValue(getDefaultString(province.getLongitude()));
   			cbxCountry.setSelectedEntity(province.getCountry());
   			cbActive.setValue(province.getStatusRecord() == EStatusRecord.ACTIV);
   		}
   	}
   	
   	/**
   	 * Reset
   	 */
   	@Override
   	public void reset() {
   		super.reset();
   		province = new Province();
   		txtCode.setValue("");
   		txtDescEn.setValue("");
   		txtDesc.setValue("");
   		txtShortCode.setValue("");
   		txtLongitude.setValue("");
   		txtLatitude.setValue("");
   		cbxCountry.setSelectedEntity(null);
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
   		checkMandatorySelectField(cbxCountry, "country");
   		checkDoubleField(txtLatitude, "latitude");
		checkDoubleField(txtLongitude, "longitude");
   		return errors.isEmpty();
   	}
}
