package com.nokor.efinance.gui.ui.panel.loss.repossess;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.workflow.AuctionWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Loss on Repossess Search Panel
 * @author bunlong.taing
 */
public class LossOnRepossessSearchPanel extends AbstractSearchPanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -3708635195125773075L;
	
	private TextField txtReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	
	/**
	 * Contructor
	 * @param tablePanel
	 */
	public LossOnRepossessSearchPanel(LossOnRepossessTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtReference = ComponentFactory.getTextField("reference", false, 50, 200);
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		
		GridLayout gridLayout = new GridLayout(7, 1);
		
		FormLayout formLayoutLeft =  new FormLayout();
		formLayoutLeft.addComponent(txtReference);
		formLayoutLeft.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		FormLayout formLayoutMiddle = new FormLayout();
		formLayoutMiddle.addComponent(dfStartDate);
		formLayoutMiddle.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		FormLayout formLayoutRight = new FormLayout();
		formLayoutRight.addComponent(dfEndDate);
		formLayoutRight.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());
		
		int iCol = 0;
		
		gridLayout.addComponent(formLayoutLeft, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);	
		gridLayout.addComponent(formLayoutMiddle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(formLayoutRight, iCol++, 0);
		
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		restrictions.addCriterion(Restrictions.eq("contractStatus", ContractWkfStatus.REP));
		restrictions.addCriterion(Restrictions.eq("auctionStatus", AuctionWkfStatus.SOL));
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("reference", txtReference.getValue(), MatchMode.ANYWHERE));
		}

		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(START_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtReference.setValue("");
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}

}
