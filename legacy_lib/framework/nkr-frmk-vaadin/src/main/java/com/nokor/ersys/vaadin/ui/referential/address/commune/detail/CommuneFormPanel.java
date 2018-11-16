/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.commune.detail;

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
public class CommuneFormPanel extends AbstractFormPanel implements AppServicesHelper {
	/**	 */
	private static final long serialVersionUID = 8178953758494848483L;

	private Commune commune;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
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
		commune.setCode(txtCode.getValue());
		commune.setDesc(txtDesc.getValue());
		commune.setDescEn(txtDescEn.getValue());
		commune.setLongitude(getDouble(txtLongitude));
		commune.setLatitude(getDouble(txtLatitude));
		commune.setDistrict(cbxDistrict.getSelectedEntity());
		commune.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return commune;
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
		cbxProvince.setWidth(200, Unit.PIXELS);
		cbxProvince.renderer();
		
		cbxDistrict = new EntityRefComboBox<District>(I18N.message("district"));
		cbxDistrict.setRestrictions(new BaseRestrictions<District>(District.class));
		cbxDistrict.setImmediate(true);
		cbxDistrict.setRequired(true);
		cbxDistrict.renderer();
		cbxDistrict.setWidth(200, Unit.PIXELS);
		cbxDistrict.clear();
		cbxProvince.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 6613715128059756285L;

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
			commune = ENTITY_SRV.getById(Commune.class, id);
			txtCode.setValue(commune.getCode());
			txtDescEn.setValue(commune.getDescEn());
			txtDesc.setValue(commune.getDesc());
			txtLatitude.setValue(getDefaultString(commune.getLatitude()));
			txtLongitude.setValue(getDefaultString(commune.getLongitude()));
			cbxProvince.setSelectedEntity(commune.getDistrict().getProvince());
			cbxDistrict.setSelectedEntity(commune.getDistrict());
			cbActive.setValue(commune.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		commune = new Commune();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtLongitude.setValue("");
		txtLatitude.setValue("");
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
		checkMandatorySelectField(cbxDistrict, "district");
		return errors.isEmpty();
	}

}
