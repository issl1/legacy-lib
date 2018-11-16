package com.nokor.efinance.ra.ui.panel.directcost;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
public class DirectCostSearchPanel extends AbstractSearchPanel<FinService> implements ServiceEntityField {

	private static final long serialVersionUID = 9198990923233644456L;
	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtCode;
	private TextField txtDescEn;
	private ERefDataComboBox<ECalculMethod> cbxCalculMethod;
	
	public DirectCostSearchPanel(DirectCostTablePanel driectCostTablePanel) {
		super(I18N.message("direct.cost.search"), driectCostTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		cbxCalculMethod.setSelectedEntity(null);
	}


	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(5, 1);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		cbxCalculMethod = new ERefDataComboBox<ECalculMethod>(I18N.message("calculate.method"), ECalculMethod.class);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);        
        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(cbxCalculMethod),2,0);
        gridLayout.addComponent(new FormLayout(cbActive), 3, 0);
        gridLayout.addComponent(new FormLayout(cbInactive), 4, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<FinService> getRestrictions() {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		
		restrictions.addCriterion(Restrictions.in("serviceType", EServiceType.listDirectCosts()));
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxCalculMethod.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(CALCUL_METHOD, cbxCalculMethod.getSelectedEntity()));
		}
		
		if (!cbActive.getValue() && !cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		
		if (cbActive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		
		if (cbInactive.getValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		
		return restrictions;
	}

}
