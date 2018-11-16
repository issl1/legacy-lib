package com.nokor.efinance.ra.ui.panel.dealer;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author youhort.ly
 *
 */
public class DealerSearchPanel extends AbstractSearchPanel<Dealer> implements DealerEntityField {

	private static final long serialVersionUID = 5489374367808132695L;

	private TextField txtIntCode;
	private TextField txtExtCode;
	private TextField txtName;
	private TextField txtNameEn;
	private StatusRecordField statusRecordField;
	
	/**
	 * 
	 * @param dealerTablePanel
	 */
	public DealerSearchPanel(DealerTablePanel dealerTablePanel) {
		super(I18N.message("dealer.search"), dealerTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtIntCode.setValue("");
		txtExtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		statusRecordField.clearValues();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		//final GridLayout gridLayout = new GridLayout(4, 2);
		
		txtIntCode = ComponentFactory.getTextField("internal.code", false, 60, 150);         
		txtExtCode = ComponentFactory.getTextField("external.code", false, 60, 150);
		
		txtName = ComponentFactory.getTextField("name", false, 60, 150);
		txtName.setHeight(30, Unit.PIXELS);
		txtNameEn = ComponentFactory.getTextField("name.en", false, 60, 150);
		
		statusRecordField = new StatusRecordField();
        
        /*gridLayout.addComponent(new FormLayout(txtIntCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtExtCode), 1, 0);
        gridLayout.addComponent(new FormLayout(cbActive), 2, 0);
        gridLayout.addComponent(new FormLayout(cbInactive), 3, 0);
        
        gridLayout.addComponent(new FormLayout(txtNameEn), 0, 1);
        gridLayout.addComponent(new FormLayout(txtName), 1, 1);*/
        
        final FormLayout formLayout1 = new FormLayout();
        final FormLayout formLayout2 = new FormLayout();
        
        
        formLayout1.addComponent(txtIntCode);
        formLayout1.addComponent(txtNameEn);
        
        formLayout2.addComponent(txtExtCode);
        formLayout2.addComponent(txtName);
        
        final HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.setSpacing(true);
        horizontalLayout3.addComponent(new FormLayout(statusRecordField));
        
        horizontalLayout.addComponent(formLayout1);
        horizontalLayout.addComponent(formLayout2);
        horizontalLayout.addComponent(horizontalLayout3);
        
		return horizontalLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Dealer> getRestrictions() {
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);	
				
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
				
		if (statusRecordField.isInactiveAllValues()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (statusRecordField.getActiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}
		
		restrictions.addCriterion(Restrictions.ne(STATUS_RECORD, EStatusRecord.RECYC));
		restrictions.addOrder(Order.asc(NAME_EN));
		
		return restrictions;
	}
}
