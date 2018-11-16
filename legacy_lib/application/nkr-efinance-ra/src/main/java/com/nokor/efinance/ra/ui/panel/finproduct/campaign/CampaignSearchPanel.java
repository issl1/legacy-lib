package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.statusrecord.StatusRecordField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignSearchPanel extends AbstractSearchPanel<Campaign> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2588689947982406735L;

	private StatusRecordField statusRecordField;
	private TextField txtCode;
	private TextField txtDescEn;
	private AutoDateField dfStartDate;
	
	/**
	 * 
	 * @param campaignTablePanel
	 */
	public CampaignSearchPanel(CampaignTablePanel campaignTablePanel) {
		super(I18N.message("search"), campaignTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDescEn.setValue("");
		dfStartDate.setValue(null);
		statusRecordField.clearValues();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		txtCode = ComponentFactory.getTextField(60, 130);        
		txtDescEn = ComponentFactory.getTextField(60, 150);
		
        dfStartDate = ComponentFactory.getAutoDateField();
        statusRecordField = new StatusRecordField();
		
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(ComponentLayoutFactory.getLabelCaption("code"));
		mainLayout.addComponent(txtCode);
		mainLayout.addComponent(ComponentLayoutFactory.getLabelCaption("campaign.name"));
		mainLayout.addComponent(txtDescEn);
		mainLayout.addComponent(ComponentLayoutFactory.getLabelCaption("startdate"));
		mainLayout.addComponent(dfStartDate);
		mainLayout.addComponent(statusRecordField);
		mainLayout.setComponentAlignment(statusRecordField, Alignment.MIDDLE_RIGHT);
		
		return mainLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Campaign> getRestrictions() {
		
		BaseRestrictions<Campaign> restrictions = new BaseRestrictions<>(Campaign.class);
		
		if (StringUtils.isNotEmpty(txtCode.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(CODE, txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotEmpty(txtDescEn.getValue())) { 
			restrictions.addCriterion(Restrictions.ilike(DESC_EN, txtDescEn.getValue(), MatchMode.ANYWHERE));
		}
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(START_DATE, dfStartDate.getValue()));
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

}
