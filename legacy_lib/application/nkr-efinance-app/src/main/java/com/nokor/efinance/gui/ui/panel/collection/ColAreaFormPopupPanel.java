package com.nokor.efinance.gui.ui.panel.collection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColAreaFormPopupPanel extends Window implements ClickListener, FinServicesHelper, FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8317951727357487130L;
	
	private Area area;
	private TextField txtDesc;
	private TextField txtPostalCode;
	private TextField txtAreaCode;
	private TextField txtStreet;
	private TextField txtSoi;
	private TextField txtMoo;
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict; 
	private TextArea txtRemark;
	
	private Button btnSave;
	private Button btnCancel;
	
	private ColAreaTablePanel areaTablePanel;
	private VerticalLayout messagePanel;
	
	public ColAreaFormPopupPanel(ColAreaTablePanel areaTablePanel) {
		super.center();
		setCaption(I18N.message("area"));
		this.areaTablePanel = areaTablePanel;
		init();
	}
	
	private void init() {
		cbxProvince = getEntityRefComboBox("province", new BaseRestrictions<>(Province.class));
		cbxProvince.setWidth("180px");
		cbxProvince.setRequired(true);
		cbxDistrict = getEntityRefComboBox("district", new BaseRestrictions<>(District.class));
		cbxDistrict.setWidth("180px");
		cbxDistrict.setRequired(true);
		cbxSubDistrict = getEntityRefComboBox("subdistrict", new BaseRestrictions<>(Commune.class));
		cbxSubDistrict.setWidth("180px");
		cbxSubDistrict.setRequired(true);
		
		txtAreaCode = ComponentFactory.getTextField("area.code", true, 60, 150);
		txtDesc = ComponentFactory.getTextField("desc", true, 60, 200);
		txtPostalCode = ComponentFactory.getTextField("postal.code", true, 60, 200);
		txtStreet = ComponentFactory.getTextField("street", false, 60, 200);
		txtSoi = ComponentFactory.getTextField("soi", false, 60, 200);
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		txtMoo = ComponentFactory.getTextField("moo", false, 300, 100);
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -572281953646438700L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					Province province = cbxProvince.getSelectedEntity();
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, province.getId()));
					cbxDistrict.setRestrictions(restrictions);
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
					District district = cbxDistrict.getSelectedEntity();
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, district.getId()));
					cbxSubDistrict.setRestrictions(restrictions);
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
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		formLayout.addComponent(txtAreaCode);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(cbxProvince);
		formLayout.addComponent(cbxDistrict);
		formLayout.addComponent(cbxSubDistrict);
		formLayout.addComponent(txtPostalCode);
		formLayout.addComponent(txtStreet);
		formLayout.addComponent(txtMoo);
		formLayout.addComponent(txtSoi);
		formLayout.addComponent(txtRemark);	
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(formLayout);
		
		setContent(mainLayout);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	public void reset() {
		area = new Area();
		txtAreaCode.setValue("");
		txtDesc.setValue("");
		txtPostalCode.setValue("");
		txtStreet.setValue("");
		txtSoi.setValue("");
		txtMoo.setValue("");
		txtRemark.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxSubDistrict.setSelectedEntity(null);
	}
	
	/**
	 * 
	 * @param colAreaCodeID
	 */
	public void assignValues(Long areaId) {
		reset();
		if (areaId != null) {
			area = ENTITY_SRV.getById(Area.class, areaId);
			txtAreaCode.setValue(area.getCode());
			txtDesc.setValue(area.getDesc());
			txtPostalCode.setValue(area.getPostalCode());
			cbxProvince.setSelectedEntity(area.getProvince() != null ? area.getProvince() : null);
			cbxDistrict.setSelectedEntity(area.getDistrict() != null ? area.getDistrict() : null);
			cbxSubDistrict.setSelectedEntity(area.getCommune() != null ? area.getCommune() : null);
			txtMoo.setValue(area.getLine1());
			txtSoi.setValue(area.getLine2());
			txtStreet.setValue(area.getStreet());
			txtRemark.setValue(area.getRemark() == null ? "" : area.getRemark());
		}
	}
	
	/**
	 * get entity
	 * @return
	 */
	protected Entity getEntity() {
		area.setCode(txtAreaCode.getValue());
		area.setDescEn(txtDesc.getValue());
		area.setDesc(txtDesc.getValue());
		area.setPostalCode(txtPostalCode.getValue());
		area.setProvince(cbxProvince.getSelectedEntity() != null ? cbxProvince.getSelectedEntity() : null);
		area.setDistrict(cbxDistrict.getSelectedEntity() != null ? cbxDistrict.getSelectedEntity() : null);
		area.setCommune(cbxSubDistrict.getSelectedEntity() != null ? cbxSubDistrict.getSelectedEntity() : null);
		area.setStreet(txtStreet.getValue());
		area.setLine1(txtMoo.getValue());
		area.setLine2(txtSoi.getValue());
		area.setRemark(txtRemark.getValue());
		
		if (ProfileUtil.isColFieldSupervisor()) {
			area.setColType(EColType.FIELD);
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			area.setColType(EColType.INSIDE_REPO);
		} else if (ProfileUtil.isMarketingLeader()) {
			area.setColType(EColType.MKT);
		} else if (ProfileUtil.isColOASupervisor()) {
			area.setColType(EColType.OA);
		}
		
		return area;
	}
	
	private boolean validate() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		List<String> errors = new ArrayList<String>();
		Label messageLabel;
		
		if (StringUtils.isEmpty(txtAreaCode.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("area.code") }));
		} 
		
		if (StringUtils.isEmpty(txtDesc.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("desc") }));
		}
		
		if (cbxProvince.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("province") }));
		}
		
		if (cbxDistrict.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("district") }));
		}
		
		if (cbxSubDistrict.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("commune") }));
		}
		
		if (StringUtils.isEmpty(txtPostalCode.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("postal.code") }));
		}
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}

	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(String caption, BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>(I18N.message(caption));
		comboBox.setWidth(200, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}		
	/**
	 * 
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				ENTITY_SRV.saveOrUpdate(getEntity());
				ComponentLayoutFactory.displaySuccessfullyMsg();
				close();
				areaTablePanel.refresh();
			}
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

}
