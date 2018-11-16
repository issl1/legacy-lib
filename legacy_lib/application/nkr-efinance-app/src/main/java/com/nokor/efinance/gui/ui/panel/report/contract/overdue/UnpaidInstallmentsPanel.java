package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author bunlong.taing
 */
public class UnpaidInstallmentsPanel extends AbstractTabPanel implements CashflowEntityField {

	/** */
	private static final long serialVersionUID = -267789039567819096L;
	
	private ContractService contractService = SpringUtils.getBean(ContractService.class);
	private CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	
	private SimplePagedTable<Cashflow> pagedTable;
	private Contract contract;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		pagedTable = new SimplePagedTable<Cashflow>(createColumnDefinitions());
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(pagedTable);
		verticalLayout.addComponent(pagedTable.createControls());
		
		return verticalLayout;
	}
	
	/**
	 * @return
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.ge(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.todayH00M00S00()));
		
		restrictions.addOrder(Order.desc(INSTALLMENT_DATE));
		return restrictions;
	}
	
	/**
	 * @param cashflows
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Cashflow> cashflows) {
		
		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment != null) {
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				}
			} else {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setId(cashflow.getId());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				cashflowPayment.setNumInstallment(cashflow.getNumInstallment());
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(cashflow.getTiInstallmentAmount());
				}
				cashflowPayments.add(cashflowPayment);
			}
		}
		
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		for (CashflowPayment cashflowPayment : cashflowPayments) {
		
			PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(),  
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
			double penaltyAmountUsd = penaltyVo.getPenaltyAmount() != null ? penaltyVo.getPenaltyAmount().getTiAmount() : 0d;
			
			Date installmentDate = DateUtils.getDateAtBeginningOfDay(cashflowPayment.getInstallmentDate());
			Date paymentDate = DateUtils.getDateAtBeginningOfDay(DateUtils.todayH00M00S00());
			Integer nbOverdueDays = DateUtils.getDiffInDays(paymentDate, installmentDate).intValue();
			nbOverdueDays = nbOverdueDays <= 0 ? null : nbOverdueDays;
			
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflowPayment.getNumInstallment());
			item.getItemProperty(DUE_DATE).setValue(cashflowPayment.getInstallmentDate());
			item.getItemProperty("no.penalty.days").setValue(penaltyVo.getNumPenaltyDays());
			item.getItemProperty("no.overdue.days").setValue(nbOverdueDays);
			item.getItemProperty("installment.amount").setValue(AmountUtils.format(cashflowPayment.getTiInstallmentAmount()));
			item.getItemProperty("other.amount").setValue(AmountUtils.format(cashflowPayment.getTiOtherInstallmentAmount()));
			item.getItemProperty("penalty.amount").setValue(AmountUtils.format(penaltyAmountUsd));
			item.getItemProperty("total.amount").setValue(AmountUtils.format(
					MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()) +
					MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentAmount()) +
					penaltyAmountUsd));
		}		
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT , I18N.message("num.installment"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DUE_DATE , I18N.message("due.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("no.overdue.days", I18N.message("no.overdue.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("other.amount", I18N.message("other.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("penalty.amount", I18N.message("penalty.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), String.class, Align.LEFT, 100));
		
		return columnDefinitions;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues (Contract contract) {
		this.contract = contract;
		setIndexedContainer(cashflowService.getListCashflow(getRestrictions()));
	}
	
	/**
	 * @author youhort.ly
	 *
	 */
	private class CashflowPayment implements Serializable, Entity {
		/** */
		private static final long serialVersionUID = -5486480101145087373L;
		
		private Long id;
		private Date installmentDate;
		private Double tiInstallmentUsd;
		private Double tiOtherInstallmentUsd;
		private Integer numInstallment;
		
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}	
		
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		
		/**
		 * @return the installmentDate
		 */
		public Date getInstallmentDate() {
			return installmentDate;
		}
		
		/**
		 * @param installmentDate the installmentDate to set
		 */
		public void setInstallmentDate(Date installmentDate) {
			this.installmentDate = installmentDate;
		}
		
		/**
		 * @return the tiInstallmentUsd
		 */
		public Double getTiInstallmentAmount() {
			return tiInstallmentUsd;
		}
		
		/**
		 * @param tiInstallmentUsd the tiInstallmentUsd to set
		 */
		public void setTiInstallmentAmount(Double tiInstallmentUsd) {
			this.tiInstallmentUsd = tiInstallmentUsd;
		}
		
		/**
		 * @return the tiOtherInstallmentUsd
		 */
		public Double getTiOtherInstallmentAmount() {
			return tiOtherInstallmentUsd;
		}
		
		/**
		 * @param tiOtherInstallmentUsd the tiOtherInstallmentUsd to set
		 */
		public void setTiOtherInstallmentAmount(Double tiOtherInstallmentUsd) {
			this.tiOtherInstallmentUsd = tiOtherInstallmentUsd;
		}

		/**
		 * @return the numInstallment
		 */
		public Integer getNumInstallment() {
			return numInstallment;
		}
		
		/**
		 * @param numInstallment the numInstallment to set
		 */
		public void setNumInstallment(Integer numInstallment) {
			this.numInstallment = numInstallment;
		}
		

	}

}
