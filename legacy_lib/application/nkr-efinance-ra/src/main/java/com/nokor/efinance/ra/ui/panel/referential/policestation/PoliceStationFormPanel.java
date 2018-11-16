package com.nokor.efinance.ra.ui.panel.referential.policestation;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PoliceStationFormPanel extends AbstractFormPanel {

	
	private static final long serialVersionUID = 6815491670858108459L;
	
	private PoliceStation policeStation;
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private EntityComboBox<Province> cbxProvince;
	
	private CheckBox cbActive;
	
	@javax.annotation.PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	@Override
	protected Entity getEntity() {
		policeStation.setCode(txtCode.getValue());
		policeStation.setDescEn(txtDescEn.getValue());
		policeStation.setDesc(txtDesc.getValue());
		policeStation.setProvince(cbxProvince.getSelectedEntity());
		policeStation.setActive(cbActive.getValue());
		return policeStation;
	}

	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		cbxProvince = new EntityComboBox<>(Province.class, I18N.message("province"), "desc", null);
		cbxProvince.setWidth(200, Unit.PIXELS);
		cbxProvince.renderer();
		cbActive = new CheckBox(I18N.message("active"));
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(cbxProvince);
		formLayout.addComponent(cbActive);
		
		return formLayout;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			policeStation = ENTITY_SRV.getById(PoliceStation.class, id);
			txtCode.setValue(policeStation.getCode());
			txtDescEn.setValue(policeStation.getDescEn());
			txtDesc.setValue(policeStation.getDesc());
			cbActive.setValue(policeStation.isActive());
			cbxProvince.setSelectedEntity(policeStation.getProvince());
		}
	}
	
	/**
	 * reset
	 */
	public void reset() {
		super.reset();
		policeStation = new PoliceStation();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbActive.setValue(true);	
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
}
