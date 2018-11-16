package com.nokor.efinance.ra.ui.panel.finproduct.creditcontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.financial.model.CreditControl;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
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

public class CreditControlsSearchPanel extends AbstractSearchPanel<CreditControl> implements FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5494579010719968857L;

	private TextField txtDescEn;

	public CreditControlsSearchPanel(CreditControlTablePanel creditControlTablePanel) {
		super(I18N.message("search"), creditControlTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDescEn.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(4, 1);		
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);		        
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<CreditControl> getRestrictions() {
		BaseRestrictions<CreditControl> restrictions = new BaseRestrictions<CreditControl>(CreditControl.class);		
		List<Criterion> criterions = new ArrayList<Criterion>();
	
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.desc(CreditControl.ID));
		return restrictions;
	}

}

	
