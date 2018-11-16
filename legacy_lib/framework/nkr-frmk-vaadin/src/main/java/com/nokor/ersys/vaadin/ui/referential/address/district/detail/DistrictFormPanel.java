/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.district.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
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
public class DistrictFormPanel extends AbstractFormPanel implements AppServicesHelper {
	/**	 */
	private static final long serialVersionUID = -1450335904436408562L;

	private District district;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
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
		district.setCode(txtCode.getValue());
		district.setDesc(txtDesc.getValue());
		district.setDescEn(txtDescEn.getValue());
		district.setProvince(cbxProvince.getSelectedEntity());
//		district.setCollectionGroup(cbxCollectionGroup.getSelectedEntity());
		district.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return district;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 50, 150);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 200, 350);
		txtDesc = ComponentFactory.getTextField35("desc", true, 200, 350);
		
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
			district = ENTITY_SRV.getById(District.class, id);
			txtCode.setValue(district.getCode());
			txtDescEn.setValue(district.getDescEn());
			txtDesc.setValue(district.getDesc());
			cbxProvince.setSelectedEntity(district.getProvince());
//			cbxCollectionGroup.setSelectedEntity(district.getCollectionGroup());
			cbActive.setValue(district.getStatusRecord() == EStatusRecord.ACTIV);
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		district = new District();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
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
		return errors.isEmpty();
	}
}
