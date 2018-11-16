package com.nokor.efinance.gui.ui.panel.cartography;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Village search
 * @author youhort.ly
 *
 */
public class CommuneSearchPanel extends VerticalLayout implements FMEntityField {

	private static final long serialVersionUID = 2337729438174372754L;
	
	private EntityRefComboBox<Commune> cbxCommune;
	private EntityRefComboBox<Province> cbxProvince;
    private EntityRefComboBox<District> cbxDistrict;
    
	public CommuneSearchPanel() {
		setMargin(true);
		
		addComponent(createForm());
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4118688754099624519L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = cbxDistrict.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("province.id", cbxProvince.getSelectedEntity().getId()));
					restrictions.setCriterions(criterions);
					restrictions.addOrder(Order.asc("desc"));
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
					cbxCommune.clear();
				}
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -8133350088716486511L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = cbxCommune.getRestrictions();
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("district.id", cbxDistrict.getSelectedEntity().getId()));
					criterions.add(Restrictions.isNotNull("latitude"));
					criterions.add(Restrictions.isNotNull("longitude"));
					restrictions.setCriterions(criterions);
					restrictions.addOrder(Order.asc("desc"));
					cbxCommune.renderer();
				} else {
					cbxCommune.clear();
				}
			}
		});
	}


	/**
	 * @return
	 */
	protected Component createForm() {		
		cbxProvince = new EntityRefComboBox<Province>();
		cbxProvince.setRestrictions(new BaseRestrictions<Province>(Province.class));
		List<Criterion> criterions = cbxProvince.getRestrictions().getCriterions();
		criterions.add(Restrictions.isNotNull("latitude"));
		criterions.add(Restrictions.isNotNull("longitude"));
		cbxProvince.setImmediate(true);
		cbxProvince.renderer();
		
		cbxDistrict = new EntityRefComboBox<District>();
		cbxDistrict.setRestrictions(new BaseRestrictions<District>(District.class));
		cbxDistrict.setImmediate(true);
		cbxDistrict.renderer();
		
		cbxCommune = new EntityRefComboBox<Commune>();
		cbxCommune.setRestrictions(new BaseRestrictions<Commune>(Commune.class));
		cbxCommune.renderer();
		cbxCommune.setImmediate(true);
		
		int col = 0;
		final GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
        gridLayout.addComponent(new Label(I18N.message("province")), col++, 0);
        gridLayout.addComponent(cbxProvince, col++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), col++, 0);
        gridLayout.addComponent(new Label(I18N.message("district")), col++, 0);
        gridLayout.addComponent(cbxDistrict, col++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), col++, 0);
        gridLayout.addComponent(new Label(I18N.message("commune")), col++, 0);
        gridLayout.addComponent(cbxCommune, col++, 0);
        
        VerticalLayout contenLayout = new VerticalLayout();
        contenLayout.setMargin(true);
        contenLayout.addComponent(gridLayout);
		return contenLayout;
	}
	
	/**
	 * @return district.province.id
	 */
	public BaseRestrictions<Commune> getRestrictions() {		
		BaseRestrictions<Commune> restrictions = new BaseRestrictions<Commune>(Commune.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (cbxProvince.getSelectedEntity() != null) {
			restrictions.addAssociation(DISTRICT, "dist", JoinType.INNER_JOIN);
			restrictions.addAssociation("dist." + PROVINCE, "prov", JoinType.INNER_JOIN);
			criterions.add(Restrictions.eq("prov." + ID, cbxProvince.getSelectedEntity().getId()));
		}
		if (cbxDistrict.getSelectedEntity() != null) {
			restrictions.addAssociation(DISTRICT, "dist", JoinType.INNER_JOIN);
			criterions.add(Restrictions.eq("dist." + ID, cbxDistrict.getSelectedEntity().getId()));
		}
		if (cbxCommune.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(ID, cbxCommune.getSelectedEntity().getId()));
		}
		restrictions.setCriterions(criterions);
		return restrictions;
	}

}
