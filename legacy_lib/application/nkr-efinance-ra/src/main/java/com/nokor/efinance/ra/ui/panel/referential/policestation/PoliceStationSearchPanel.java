package com.nokor.efinance.ra.ui.panel.referential.policestation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */

public class PoliceStationSearchPanel extends AbstractSearchPanel<PoliceStation> implements FMEntityField {

	/**
	 * 
	 */
	private TextField txtCode;
	private TextField txtDescEn;
	private EntityComboBox<Province> cbxProvince;
	
	
	private static final long serialVersionUID = 1L;

	public PoliceStationSearchPanel(PoliceStationTablePanel smsTemplateTablePanel) {
		super(I18N.message("search"), smsTemplateTablePanel);
	}

	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxProvince.setSelectedEntity(null);
	}

	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(4, 1);		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);		  
		
		cbxProvince = new EntityComboBox<>(Province.class, I18N.message("province"), "desc", null);
		cbxProvince.renderer();
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(cbxProvince), 2, 0);
        
		return gridLayout;
	}

	@Override
	public BaseRestrictions<PoliceStation> getRestrictions() {
		BaseRestrictions<PoliceStation> restrictions = new BaseRestrictions<PoliceStation>(PoliceStation.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}	
		if (cbxProvince.getSelectedEntity() != null) {
			criterions.add(Restrictions.eq(PROVINCE, cbxProvince.getSelectedEntity()));
		}
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

}

	
