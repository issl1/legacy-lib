package com.nokor.efinance.ra.ui.panel.subdealer;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author bunlong.taing
 *
 */
public class SubDealerSearchPanel extends AbstractSearchPanel<Dealer> implements DealerEntityField {

	private static final long serialVersionUID = 5489374367808132695L;

	private CheckBox cbActive;
	private CheckBox cbInactive;
	private TextField txtIntCode;
	private TextField txtExtCode;
	private TextField txtName;
	private TextField txtNameEn;
	
	public SubDealerSearchPanel(SubDealerTablePanel dealerTablePanel) {
		super(I18N.message("dealer.search"), dealerTablePanel);
	}
	
	@Override
	protected void reset() {
		txtIntCode.setValue("");
		txtExtCode.setValue("");
	}

	@Override
	protected Component createForm() {

		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		txtIntCode = ComponentFactory.getTextField("internal.code", false, 60, 150);         
		txtExtCode = ComponentFactory.getTextField("external.code", false, 60, 150);
		
		txtName = ComponentFactory.getTextField35("name", false, 60, 150);         
		txtNameEn = ComponentFactory.getTextField("name.en", false, 60, 150);
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbInactive = new CheckBox(I18N.message("inactive"));
        cbInactive.setValue(false);        
        
        final FormLayout formLayout1 = new FormLayout();
        final FormLayout formLayout2 = new FormLayout();
        
        
        formLayout1.addComponent(txtIntCode);
        formLayout1.addComponent(txtNameEn);
        
        formLayout2.addComponent(txtExtCode);
        formLayout2.addComponent(txtName);
        
        final HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setSpacing(true);
        horizontalLayout3.addComponent(new FormLayout(cbActive));
        horizontalLayout3.addComponent(new FormLayout(cbInactive));
        
        horizontalLayout.addComponent(formLayout1);
        horizontalLayout.addComponent(formLayout2);
        horizontalLayout.addComponent(horizontalLayout3);
        
		return horizontalLayout;
	}
	
	@Override
	public BaseRestrictions<Dealer> getRestrictions() {
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
		
		restrictions.addCriterion(Restrictions.eq("dealerType", EDealerType.BRANCH));
		
		if (StringUtils.isNotEmpty(txtIntCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(INTERNAL_CODE, txtIntCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtExtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(EXTERNAL_CODE, txtExtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtName.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(NAME, txtName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtNameEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(NAME_EN, txtNameEn.getValue(), MatchMode.ANYWHERE));
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
		
		restrictions.addCriterion(Restrictions.ne(STATUS_RECORD, EStatusRecord.RECYC));
		restrictions.addOrder(Order.asc(NAME_EN));
		
		return restrictions;
	}

}
