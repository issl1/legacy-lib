package com.nokor.efinance.ra.ui.panel.insurance.campaign;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.util.StringUtils;

import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
public class InsuranceCampaignsSearchPanel extends AbstractSearchPanel<InsuranceCampaign> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 4506598922638509602L;

	private TextField txtCode;
	private TextField txtDescEn;
	private StatusRecordField statusRecordField;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	/**
	 * 
	 * @param insCampaignsTablePanel
	 */
	public InsuranceCampaignsSearchPanel(InsuranceCampaignsTablePanel insCampaignsTablePanel) {
		super(I18N.message("insurance.campaigns.search"), insCampaignsTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(3, 2);
		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);
		statusRecordField = new StatusRecordField();
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		
		gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(statusRecordField), 2, 0);
        
        gridLayout.addComponent(new FormLayout(dfStartDate), 0, 1);
        gridLayout.addComponent(new FormLayout(dfEndDate), 1, 1);
        
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<InsuranceCampaign> getRestrictions() {
		BaseRestrictions<InsuranceCampaign> restrictions = new BaseRestrictions<>(InsuranceCampaign.class);
		
		if (!StringUtils.isEmpty(txtCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtDescEn.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(START_DATE, dfStartDate.getValue()));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(END_DATE, dfEndDate.getValue()));
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
		restrictions.addOrder(Order.asc(DESC_EN));
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		statusRecordField.clearValues();
	}
}
