/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.village.detail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
public class VillageFormPanel extends AbstractFormPanel implements AppServicesHelper {
	/**	 */
	private static final long serialVersionUID = 3147486922604240605L;

	private Village village;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private EntityRefComboBox<Commune> cbxCommune;
    private EntityRefComboBox<Province> cbxProvince;
    private EntityRefComboBox<District> cbxDistrict;
    private TextField txtLatitude;
    private TextField txtLongitude;
    private CheckBox cbActive;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		village.setCode(txtCode.getValue());
		village.setDesc(txtDesc.getValue());
		village.setDescEn(txtDescEn.getValue());
		village.setLongitude(getDouble(txtLongitude));
		village.setLatitude(getDouble(txtLatitude));
		village.setCommune(cbxCommune.getSelectedEntity());
		village.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return village;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 50, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		
		cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		cbxProvince.setImmediate(true);
		cbxProvince.setRequired(true);
		cbxProvince.renderer();
		cbxProvince.setWidth(200, Unit.PIXELS);
		
		cbxDistrict = new EntityRefComboBox<District>(I18N.message("district"));
		cbxDistrict.setRestrictions(new BaseRestrictions<District>(District.class));
		cbxDistrict.setImmediate(true);
		cbxDistrict.setRequired(true);
		cbxDistrict.renderer();
		cbxDistrict.clear();
		cbxDistrict.setWidth(200, Unit.PIXELS);
	
		cbxCommune = new EntityRefComboBox<Commune>(I18N.message("commune"));
		cbxCommune.setRestrictions(new BaseRestrictions<Commune>(Commune.class));
		cbxCommune.setImmediate(true);
		cbxCommune.setRequired(true);
		cbxCommune.renderer();
		cbxCommune.clear();
		cbxCommune.setWidth(200, Unit.PIXELS);
		cbxProvince.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2721518521355651182L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = cbxDistrict.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("province.id", cbxProvince.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					restrictions.addOrder(Order.asc("desc"));
					cbxDistrict.renderer();
				} 
				else {
					cbxDistrict.clear();
					cbxCommune.clear();
					}
			}
		});
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 173396071614269815L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = cbxCommune.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("district.id", cbxDistrict.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					restrictions.addOrder(Order.asc("desc"));
					cbxCommune.renderer();
				} 
				else {
					cbxCommune.clear();
					}
			}
		});
		txtLatitude = ComponentFactory.getTextField("latitude", false, 20, 150);
		txtLongitude = ComponentFactory.getTextField("longitude", false, 20, 150);
		cbActive = new CheckBox(I18N.message("active"));
	    cbActive.setValue(true);
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtLatitude);
        formPanel.addComponent(txtLongitude);
        formPanel.addComponent(cbxProvince);
        formPanel.addComponent(cbxDistrict);
        formPanel.addComponent(cbxCommune);
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
			village = ENTITY_SRV.getById(Village.class, id);
			txtCode.setValue(village.getCode());
			txtDescEn.setValue(village.getDescEn());
			txtDesc.setValue(village.getDesc());
			txtLatitude.setValue(getDefaultString(village.getLatitude()));
			txtLongitude.setValue(getDefaultString(village.getLongitude()));
			cbxProvince.setSelectedEntity(village.getCommune().getDistrict().getProvince());
			cbxDistrict.setSelectedEntity(village.getCommune().getDistrict());
			cbxCommune.setSelectedEntity(village.getCommune());
			cbActive.setValue(village.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		village = new Village();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtLongitude.setValue("");
		txtLatitude.setValue("");
		cbxCommune.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
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
		checkMandatorySelectField(cbxCommune, "commune");
		return errors.isEmpty();
	}

}
