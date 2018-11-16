package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.areacode;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColArea;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Area code form panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreaFormPanel extends AbstractFormPanel {
	
	/** */
	private static final long serialVersionUID = -1489190810291183140L;
	
	private EColArea colArea;
	private TextField txtDescEn;
	private TextField txtDesc;
	private TextField txtPostalCode;
	private TextField txtAreaCode;
	private TextField txtStreet;
	private TextField txtSoi;
//	private EntityRefComboBox<Province> cbxProvince;
//	private EntityRefComboBox<District> cbxDistrict;
//	private EntityRefComboBox<Commune> cbxSubDistrict; 
	
	private EntityComboBox<Province> cbxProvince;
	private EntityComboBox<District> cbxDistrict;
	private EntityComboBox<Commune> cbxSubDistrict;
	private TextArea txtRemark;
	
	
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
		colArea.setCode(txtAreaCode.getValue());
		colArea.setDescEn(txtDescEn.getValue());
		colArea.setDesc(txtDesc.getValue());
		colArea.setPostalCode(txtPostalCode.getValue());
		colArea.setProvince(cbxProvince.getSelectedEntity() != null ? cbxProvince.getSelectedEntity() : null);
		colArea.setDistrict(cbxDistrict.getSelectedEntity() != null ? cbxDistrict.getSelectedEntity() : null);
		colArea.setCommune(cbxSubDistrict.getSelectedEntity() != null ? cbxSubDistrict.getSelectedEntity() : null);
		colArea.setStreet(txtStreet.getValue());
		colArea.setLine1(txtSoi.getValue());
		colArea.setRemark(txtRemark.getValue());
		return colArea;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.renderer();
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxSubDistrict = new EntityComboBox<>(Commune.class, "desc");
		txtAreaCode = ComponentFactory.getTextField("area.code", true, 60, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		txtPostalCode = ComponentFactory.getTextField("postal.code", false, 60, 200);
		txtStreet = ComponentFactory.getTextField("street", false, 60, 200);
		txtSoi = ComponentFactory.getTextField("soi", false, 60, 200);
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -572281953646438700L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq("province", cbxProvince.getSelectedEntity()));
					cbxDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
				}
				cbxSubDistrict.clear();
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -470992549634354195L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq("district", cbxDistrict.getSelectedEntity()));
					cbxSubDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxSubDistrict.renderer();
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		cbxSubDistrict.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = -4700289434066242228L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (cbxSubDistrict.getSelectedEntity() != null) {
					    Commune commune = ENTITY_SRV.getById(Commune.class, cbxSubDistrict.getSelectedEntity().getId());
						txtPostalCode.setValue(commune.getPostalCode());
					} else {
						txtPostalCode.setValue("");
					}
				}
		});
		VerticalLayout teamLayout = ComponentFactory.getVerticalLayout();
		teamLayout.setSpacing(true);
		teamLayout.addComponent(new FormLayout(txtAreaCode));
		teamLayout.addComponent(new FormLayout(txtDescEn));
		teamLayout.addComponent(new FormLayout(txtDesc));
		teamLayout.addComponent(new FormLayout(txtPostalCode));
		teamLayout.addComponent(new FormLayout(cbxProvince));
		teamLayout.addComponent(new FormLayout(cbxDistrict));
		teamLayout.addComponent(new FormLayout(cbxSubDistrict));
		teamLayout.addComponent(new FormLayout(txtStreet));
		teamLayout.addComponent(new FormLayout(txtSoi));
		teamLayout.addComponent(new FormLayout(txtRemark));
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setSizeFull();
		mainPanel.setContent(teamLayout);
		return mainPanel;
	}
	
	/**
	 * @param colAreaCodeID
	 */
	public void assignValues(Long colAreaCodeID) {
		super.reset();
		if (colAreaCodeID != null) {
			colArea = ENTITY_SRV.getById(EColArea.class, colAreaCodeID);
			txtAreaCode.setValue(colArea.getCode());
			txtDescEn.setValue(colArea.getDescEn());
			txtDesc.setValue(colArea.getDesc());
			txtPostalCode.setValue(colArea.getPostalCode());
			cbxProvince.setSelectedEntity(colArea.getProvince() != null ? colArea.getProvince() : null);
			cbxDistrict.setSelectedEntity(colArea.getDistrict() != null ? colArea.getDistrict() : null);
			cbxSubDistrict.setSelectedEntity(colArea.getCommune() != null ? colArea.getCommune() : null);
			txtSoi.setValue(colArea.getLine1());
			txtStreet.setValue(colArea.getStreet());
			txtRemark.setValue(colArea.getRemark());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		colArea = new EColArea();
		txtAreaCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtPostalCode.setValue("");
		txtStreet.setValue("");
		txtSoi.setValue("");
		txtRemark.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxSubDistrict.setSelectedEntity(null);
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtAreaCode, "area.code");
		checkMandatoryField(txtDescEn, "desc.en");
		return errors.isEmpty();
	}
}
