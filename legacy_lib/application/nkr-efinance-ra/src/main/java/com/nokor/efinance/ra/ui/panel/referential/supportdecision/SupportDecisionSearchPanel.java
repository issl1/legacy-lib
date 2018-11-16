package com.nokor.efinance.ra.ui.panel.referential.supportdecision;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * SupportDecision search
 * @author youhort.ly
 *
 */
public class SupportDecisionSearchPanel extends AbstractSearchPanel<SupportDecision> implements FMEntityField {

	private static final long serialVersionUID = 7367454989981817685L;
	private TextField txtCode;
	private TextField txtDescEn;
	private StatusRecordField statusRecordField;
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatus;
	
	public SupportDecisionSearchPanel(SupportDecisionTablePanel supportdecisionTablePanel) {
		super(I18N.message("support.decisions.search"), supportdecisionTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		
		statusRecordField.clearValues();
	}


	@Override
	protected Component createForm() {		
		txtCode = ComponentFactory.getTextField("code", false, 60, 200);        
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);	
		cbxQuotationStatus = new ERefDataComboBox<EWkfStatus>(I18N.message("quotation.status"), QuotationWkfStatus.values());
		statusRecordField = new StatusRecordField();
		
		final GridLayout gridLayout = new GridLayout(4, 1);	
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode), 0, 0);
        gridLayout.addComponent(new FormLayout(txtDescEn), 1, 0);
        gridLayout.addComponent(new FormLayout(cbxQuotationStatus), 2, 0);
        gridLayout.addComponent(new FormLayout(statusRecordField), 3, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<SupportDecision> getRestrictions() {		
		BaseRestrictions<SupportDecision> restrictions = new BaseRestrictions<SupportDecision>(SupportDecision.class);		
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (cbxQuotationStatus.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq("quotationStatus", cbxQuotationStatus.getSelectedEntity()));
		}
		
		if (!statusRecordField.getActiveValue() 
				&& !statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		} 
		if (statusRecordField.getActiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		}
		if (statusRecordField.getInactiveValue()) {
			restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		}

		restrictions.addOrder(Order.asc(CODE));
		return restrictions;
	}

}
