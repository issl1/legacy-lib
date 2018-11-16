package com.nokor.efinance.ra.ui.panel.credit.risksegment.list;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.scoring.MRiskSegment;
import com.nokor.efinance.core.scoring.RiskSegment;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Risk Segment Search Panel
 * @author bunlong.taing
 */
public class RiskSegmentSearchPanel extends AbstractSearchPanel<RiskSegment> implements MRiskSegment {
	/** */
	private static final long serialVersionUID = -994640974214784483L;
	
	private TextField txtName;

	/**
	 * @param tablePanel
	 */
	public RiskSegmentSearchPanel(RiskSegmentTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtName = ComponentFactory.getTextField(100, 200);
		
		GridLayout mainLayout = new GridLayout(2, 1);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(ComponentFactory.getLabel("name"));
		mainLayout.addComponent(txtName);
		
		return mainLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<RiskSegment> getRestrictions() {
		BaseRestrictions<RiskSegment> restrictions = new BaseRestrictions<RiskSegment>(RiskSegment.class);
		if (StringUtils.isNotEmpty(txtName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(NAME, txtName.getValue(), MatchMode.ANYWHERE));
		}
		restrictions.addOrder(Order.asc(ID));
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtName.setValue("");
	}

}
