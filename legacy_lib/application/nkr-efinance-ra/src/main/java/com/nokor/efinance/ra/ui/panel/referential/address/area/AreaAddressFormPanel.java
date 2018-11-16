package com.nokor.efinance.ra.ui.panel.referential.address.area;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
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

/**
 * Area code form panel in collection
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreaAddressFormPanel extends AbstractFormPanel {
	
	/** */
	private static final long serialVersionUID = -1489190810291183140L;
	
	private Area area;
	private TextField txtDescEn;
	private TextField txtDesc;
	private TextField txtPostalCode;
	private TextField txtAreaCode;
	private TextField txtStreet;
	private TextField txtSoi;
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
		area.setCode(txtAreaCode.getValue());
		area.setDescEn(txtDescEn.getValue());
		area.setDesc(txtDesc.getValue());
		area.setPostalCode(txtPostalCode.getValue());
		area.setProvince(cbxProvince.getSelectedEntity() != null ? cbxProvince.getSelectedEntity() : null);
		area.setDistrict(cbxDistrict.getSelectedEntity() != null ? cbxDistrict.getSelectedEntity() : null);
		area.setCommune(cbxSubDistrict.getSelectedEntity() != null ? cbxSubDistrict.getSelectedEntity() : null);
		area.setStreet(txtStreet.getValue());
		area.setLine1(txtSoi.getValue());
		area.setRemark(txtRemark.getValue());
		return area;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setCaption(I18N.message("province"));
		cbxProvince.renderer();
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setCaption(I18N.message("district"));
		cbxSubDistrict = new EntityComboBox<>(Commune.class, "desc");
		cbxSubDistrict.setCaption(I18N.message("sub.district"));
		
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
		FormLayout teamLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		teamLayout.addComponent(txtAreaCode);
		teamLayout.addComponent(txtDescEn);
		teamLayout.addComponent(txtDesc);
		teamLayout.addComponent(cbxProvince);
		teamLayout.addComponent(cbxDistrict);
		teamLayout.addComponent(cbxSubDistrict);
		teamLayout.addComponent(txtPostalCode);
		teamLayout.addComponent(txtStreet);
		teamLayout.addComponent(txtSoi);
		teamLayout.addComponent(txtRemark);
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
			area = ENTITY_SRV.getById(Area.class, colAreaCodeID);
			txtAreaCode.setValue(area.getCode());
			txtDescEn.setValue(area.getDescEn());
			txtDesc.setValue(area.getDesc());
			txtPostalCode.setValue(area.getPostalCode());
			cbxProvince.setSelectedEntity(area.getProvince() != null ? area.getProvince() : null);
			cbxDistrict.setSelectedEntity(area.getDistrict() != null ? area.getDistrict() : null);
			cbxSubDistrict.setSelectedEntity(area.getCommune() != null ? area.getCommune() : null);
			txtSoi.setValue(area.getLine1());
			txtStreet.setValue(area.getStreet());
			txtRemark.setValue(area.getRemark() != null ? area.getRemark() : "");
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		area = new Area();
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
