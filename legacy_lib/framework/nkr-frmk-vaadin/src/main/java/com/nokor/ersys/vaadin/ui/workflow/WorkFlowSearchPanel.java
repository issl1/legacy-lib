/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.MEWkfFlow;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author pengleng.huot
 *
 */
public class WorkFlowSearchPanel extends AbstractSearchPanel<EWkfFlow> implements MEWkfFlow {
	/** */
	private static final long serialVersionUID = -5603397585593562937L;

	private StatusRecordField statusRecordField;
	private TextField txtCode;
	private TextField txtDescEn;
	
	public WorkFlowSearchPanel(WorkFlowTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
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
		statusRecordField = new StatusRecordField();

		final GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode));
        gridLayout.addComponent(new FormLayout(txtDescEn));
        gridLayout.addComponent(new FormLayout(statusRecordField));
        
        return gridLayout;
	}

	@Override
	public BaseRestrictions<EWkfFlow> getRestrictions() {
		BaseRestrictions<EWkfFlow> restrictions = new BaseRestrictions<EWkfFlow>(EWkfFlow.class);
		List<Criterion> criterions = new ArrayList<Criterion>();
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			criterions.add(Restrictions.like("code", txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			criterions.add(Restrictions.like("descEn", txtDescEn.getValue(), MatchMode.ANYWHERE));
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
		
		restrictions.setCriterions(criterions);
		restrictions.addOrder(Order.asc("descEn"));
		
		return restrictions;
	}
}
