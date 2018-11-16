package com.nokor.efinance.ra.ui.panel.vat;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.util.StringUtils;

import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
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
public class VatsSearchPanel extends AbstractSearchPanel<Vat> implements FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TextField txtDescEn;
	private TextField txtValue;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	private CheckBox cbActive;
	private CheckBox cbInActive;

	public VatsSearchPanel(VatsTablePanel vatsTablePanel) {
		super(I18N.message("vat.search"), vatsTablePanel);
	}

	@Override
	protected Component createForm() {
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 150);
		txtValue = ComponentFactory.getTextField("value", false, 60, 150);
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		
		cbActive = new CheckBox(I18N.message("active"));
		cbInActive = new CheckBox(I18N.message("inactive"));
		
		
		GridLayout gridLayout = new GridLayout(4, 2);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(new FormLayout(txtDescEn), 0, 0);
		gridLayout.addComponent(new FormLayout(cbActive), 1, 0);
		gridLayout.addComponent(new FormLayout(cbInActive), 2, 0);
		
		gridLayout.addComponent(new FormLayout(txtValue), 0, 1);
		gridLayout.addComponent(new FormLayout(dfStartDate), 1, 1);
		gridLayout.addComponent(new FormLayout(dfEndDate), 2, 1);
		
		return gridLayout;
	}

	@Override
	public BaseRestrictions<Vat> getRestrictions() {
		BaseRestrictions<Vat> restrictions = new BaseRestrictions<>(Vat.class);
		
		if (!StringUtils.isEmpty(txtDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtValue.getValue())) {
			restrictions.addCriterion(Restrictions.eq("value", txtValue.getValue()));
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(START_DATE, dfStartDate.getValue()));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(END_DATE, dfEndDate.getValue()));
		}
		
		if (cbActive.getValue()) {
			restrictions.addCriterion(Restrictions.eq(STATUS_RECORD, EStatusRecord.ACTIV));
		}
		
		if (cbInActive.getValue()) {
			restrictions.addCriterion(Restrictions.eq(STATUS_RECORD, EStatusRecord.INACT));
		}
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

	@Override
	protected void reset() {
		txtDescEn.setValue("");
		txtValue.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbActive.setValue(false);
		cbInActive.setValue(false);
	}

}
