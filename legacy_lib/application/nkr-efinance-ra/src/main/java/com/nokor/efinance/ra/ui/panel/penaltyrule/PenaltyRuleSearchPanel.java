package com.nokor.efinance.ra.ui.panel.penaltyrule;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.payment.model.EPenaltyCalculMethod;
import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Penalty Rule Search
 * @author sok.vina
 *
 */
public class PenaltyRuleSearchPanel extends AbstractSearchPanel<PenaltyRule> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -8607606334058508733L;

	private ERefDataComboBox<EPenaltyCalculMethod> cbxpenaltyCalculMethod;
	private TextField txtDescEn;
	
	/**
	 * 
	 * @param penaltyRuleTablePanel
	 */
	public PenaltyRuleSearchPanel(PenaltyRuleTablePanel penaltyRuleTablePanel) {
		super(I18N.message("search"), penaltyRuleTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxpenaltyCalculMethod.setSelectedEntity(null);	
		txtDescEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
	    final FormLayout formLayout = new FormLayout();
	    
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);	
		cbxpenaltyCalculMethod = new ERefDataComboBox<EPenaltyCalculMethod>(I18N.message("penalty.method"), EPenaltyCalculMethod.class);
		formLayout.addComponent(cbxpenaltyCalculMethod);
        horizontalLayout.addComponent(formLayout);
        
		final FormLayout formLayout1 = new FormLayout();
	    final FormLayout formLayout2 = new FormLayout();
	    
	    formLayout1.addComponent(txtDescEn);
        
        formLayout2.addComponent(cbxpenaltyCalculMethod);

        horizontalLayout.addComponent(formLayout1);
        horizontalLayout.addComponent(formLayout2);
        
		return horizontalLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<PenaltyRule> getRestrictions() {		
		BaseRestrictions<PenaltyRule> restrictions = new BaseRestrictions<PenaltyRule>(PenaltyRule.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxpenaltyCalculMethod.getSelectedEntity() != null) { 
			criterions.add(Restrictions.eq(PENALTY_CALCULMETHOD, cbxpenaltyCalculMethod.getSelectedEntity()));
		}
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.desc(ID));
		
		return restrictions;
	}
}
