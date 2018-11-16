package com.nokor.efinance.gui.ui.panel.collection.filter;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.gui.ui.panel.collection.ColPhoLeadReqContractTablePanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author uhout.cheng
 */
public class ColFlagContractFilterPanel extends AbstractColPhoLeadReqConFilterPanel implements MCollection, ValueChangeListener {

	/** */
	private static final long serialVersionUID = -4727063871074051648L;

	private final static String COL = "col";
	private final static String DOT = ".";
	private final static String FLAG = "flag";
	
	private CheckBox cbPending;
	private CheckBox cbApprove;
	
	/** 
	 * @param collectionContractTablePanel
	 */
	public ColFlagContractFilterPanel(final ColPhoLeadReqContractTablePanel contractTablePanel) {
		super(contractTablePanel);
		setWidth(600, Unit.PIXELS);
		addComponent(createForm());
	}	
			
	/**
	 * @return
	 */
	protected Component createForm() {
		cbPending = getCheckBox(ERequestStatus.PENDING.getDescLocale());
		cbApprove = getCheckBox(ERequestStatus.APPROVE.getDescLocale());
		cbPending.setValue(true);
		
		FieldSet requestStatusFieldSet = new FieldSet();
		HorizontalLayout requestStatus = new HorizontalLayout();
		requestStatus.addComponent(cbPending);
		requestStatus.addComponent(cbApprove);
		requestStatusFieldSet.setLegend(I18N.message("status"));
		requestStatusFieldSet.setContent(requestStatus);
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.addComponent(requestStatusFieldSet);
		return searchLayout;		
	}
	
	/** 
	 * @param caption
	 * @return
	 */
	private CheckBox getCheckBox(String caption) {
		CheckBox checkBox = new CheckBox(caption);
		checkBox.addValueChangeListener(this);
		return checkBox;
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		super.contractTablePanel.refresh(getRestrictions());
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.collection.filter.AbstractContractFilterPanel#getRestrictions()
	 */
	@Override
	public ContractRestriction getRestrictions() {
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addAssociation(Contract.COLLECTIONS, COL, JoinType.INNER_JOIN);
		restrictions.addAssociation(COL + DOT + LASTCOLLECTIONFLAG, FLAG, JoinType.INNER_JOIN);
		
		List<Criterion> criterions = new ArrayList<>();
		if (cbPending.getValue()) {
			criterions.add(Restrictions.eq(FLAG + DOT + CollectionFlag.REQUESTSTATUS, ERequestStatus.PENDING));
		}
		if (cbApprove.getValue()) {
			criterions.add(Restrictions.eq(FLAG + DOT + CollectionFlag.REQUESTSTATUS, ERequestStatus.APPROVE));
		}
		if (!criterions.isEmpty()) {
			restrictions.addCriterion(Restrictions.or(criterions.toArray(new Criterion[criterions.size()])));
		} else {
			restrictions.addCriterion(Restrictions.eq(COL + DOT + ID, -99999l));
		}
		return restrictions;		
	}
	
}
