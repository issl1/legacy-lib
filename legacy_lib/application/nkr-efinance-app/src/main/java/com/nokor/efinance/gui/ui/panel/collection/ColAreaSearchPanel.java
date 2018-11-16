package com.nokor.efinance.gui.ui.panel.collection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Area code search panel in collection
 * @author buntha.chea
 */
public class ColAreaSearchPanel extends AbstractSearchPanel<Area> implements FMEntityField {

    /** */
	private static final long serialVersionUID = -2663537408529173899L;

	private TextField txtAreaCode;
	private EntityRefComboBox<Province> cbxProvince;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Commune> cbxSubDistrict;
	private TextField txtPostalCode;
   
    /**
     * @param areaCodeTablePanel
     * @param colType
     */
	public ColAreaSearchPanel(ColAreaTablePanel areaTablePanel) {
		super(I18N.message("search"), areaTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtAreaCode.setValue("");
		cbxProvince.setSelectedEntity(null);
		txtPostalCode.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtAreaCode = ComponentFactory.getTextField(false, 60, 120);
		cbxProvince = getEntityRefComboBox(null, new BaseRestrictions<>(Province.class));
		cbxProvince.setWidth("180px");
		cbxDistrict = getEntityRefComboBox(null, new BaseRestrictions<>(District.class));
		cbxDistrict.setWidth("180px");
		cbxSubDistrict = getEntityRefComboBox(null, new BaseRestrictions<>(Commune.class));
		cbxSubDistrict.setWidth("180px");
		txtPostalCode = ComponentFactory.getTextField(60, 120);
		
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
		
		Label lblProvince = ComponentFactory.getLabel("province");
		Label lblDistrict = ComponentFactory.getLabel("district");
		Label lblSubDistrict = ComponentFactory.getLabel("commune");
		Label lblCode = ComponentFactory.getLabel("code");
		Label lblPostalCode = ComponentFactory.getLabel("postal.code");
		
		GridLayout searchLayout = new GridLayout(11, 2);
		searchLayout.setSpacing(true);
		int iCol = 0;
		searchLayout.addComponent(lblProvince, iCol++, 0);
		searchLayout.addComponent(cbxProvince, iCol++, 0);
		searchLayout.addComponent(lblDistrict, iCol++, 0);
		searchLayout.addComponent(cbxDistrict, iCol++, 0);
		searchLayout.addComponent(lblSubDistrict, iCol++, 0);
		searchLayout.addComponent(cbxSubDistrict, iCol++, 0);
		searchLayout.addComponent(lblPostalCode, iCol++, 0);
		searchLayout.addComponent(txtPostalCode, iCol++, 0);
		
		iCol = 0;
		searchLayout.addComponent(lblCode, iCol++, 1);
		searchLayout.addComponent(txtAreaCode, iCol++, 1);
		searchLayout.setComponentAlignment(lblProvince, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblDistrict, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblSubDistrict, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblCode, Alignment.MIDDLE_LEFT);
		searchLayout.setComponentAlignment(lblPostalCode, Alignment.MIDDLE_LEFT);
       
		return searchLayout;
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
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Area> getRestrictions() {	
		
		BaseRestrictions<Area> restrictions = new BaseRestrictions<>(Area.class);	
		
		if (ProfileUtil.isColFieldSupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.FIELD));
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.INSIDE_REPO));
		} else if (ProfileUtil.isMarketingLeader()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.MKT));
		} else if (ProfileUtil.isColOASupervisor()) {
			restrictions.addCriterion(Restrictions.eq("colType", EColType.OA));
		}
		
		if (StringUtils.isNotEmpty(txtAreaCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtAreaCode.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(PROVINCE, cbxProvince.getSelectedEntity()));
		} 
		if (cbxDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DISTRICT, cbxDistrict.getSelectedEntity()));
		}
		if (cbxSubDistrict.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(COMMUNE, cbxSubDistrict.getSelectedEntity()));
		}
		if (StringUtils.isNotEmpty(txtPostalCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(POSTAL_CODE, txtPostalCode.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}

}
