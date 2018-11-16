package com.nokor.efinance.ra.ui.panel.collections.minimunreturnrate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.common.reference.model.MinReturnRate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
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
public class MinReturnRateSearchPanel extends AbstractSearchPanel<MinReturnRate> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = -3010417083411566019L;
	
	private TextField txtDescEn;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;

	public MinReturnRateSearchPanel(MinReturnRateTablePanel minReturnRateTablePanel) {
		super(I18N.message("min.return.rate"), minReturnRateTablePanel);
	}

	@Override
	protected Component createForm() {
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 50, 200);
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);;
		
		GridLayout gridLayout = new GridLayout(3, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(new FormLayout(txtDescEn), 0, 0);
		gridLayout.addComponent(new FormLayout(dfStartDate), 1, 0);
		gridLayout.addComponent(new FormLayout(dfEndDate), 2, 0);
		return gridLayout;
	}

	@Override
	public BaseRestrictions<MinReturnRate> getRestrictions() {
		BaseRestrictions<MinReturnRate> restrictions = new BaseRestrictions<>(MinReturnRate.class);

		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}

		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(START_DATE, dfStartDate.getValue()));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(END_DATE, dfEndDate.getValue()));
		}
		
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

	@Override
	protected void reset() {
		txtDescEn.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
}
