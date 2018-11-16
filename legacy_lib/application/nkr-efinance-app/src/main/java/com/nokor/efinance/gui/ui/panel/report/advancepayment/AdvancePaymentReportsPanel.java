package com.nokor.efinance.gui.ui.panel.report.advancepayment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AdvancePaymentReportsPanel.NAME)
public class AdvancePaymentReportsPanel extends AbstractTabPanel implements View, CashflowEntityField {
	private static final long serialVersionUID = 148711582191868605L;

	public static final String NAME = "advance.payment.report";
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<CashflowPayment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ComboBox cbxAdvancePayment;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	public AdvancePaymentReportsPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -3403059921454308342L;
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7165734546798826698L;
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(7, 1);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		cbxAdvancePayment = new ComboBox(I18N.message("advance.payment.percentage"));
		cbxAdvancePayment.setImmediate(true);
		cbxAdvancePayment.setItemCaption(null, I18N.message("all"));
		cbxAdvancePayment.addItem(10);
		cbxAdvancePayment.setItemCaption(10, I18N.message("10%"));
		cbxAdvancePayment.addItem(20);
		cbxAdvancePayment.setItemCaption(20, I18N.message("20%"));
		cbxAdvancePayment.addItem(30);
		cbxAdvancePayment.setItemCaption(30, I18N.message("30%"));
		cbxAdvancePayment.addItem(40);
		cbxAdvancePayment.setItemCaption(40, I18N.message("40%"));
		cbxAdvancePayment.addItem(50);
		cbxAdvancePayment.setItemCaption(50, I18N.message("50%"));
		cbxAdvancePayment.addItem(60);
		cbxAdvancePayment.setItemCaption(60, I18N.message("60%"));
		cbxAdvancePayment.addItem(70);
		cbxAdvancePayment.setItemCaption(70, I18N.message("70%"));
		
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new FormLayout(dfStartDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(dfEndDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxDealer), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxAdvancePayment), iCol++, 0);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<CashflowPayment>(this.columnDefinitions);
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("advance.payment"));
        
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addAssociation("payment", "pay", JoinType.INNER_JOIN);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.contractApplicants", "contractapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.TRUE));
		restrictions.addCriterion(Restrictions.eq(UNPAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.eq(NUM_INSTALLMENT, 0));
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("cont."+ DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge("pay." + PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("pay." + PAYMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (cbxAdvancePayment.getValue() != null) {
			restrictions.addCriterion(Restrictions.eq("cont.advancePaymentPercentage", Double.parseDouble(cbxAdvancePayment.getValue().toString())));
		}
		restrictions.addOrder(Order.asc("cont." + ID));
		return restrictions;
	}
	

	public void reset() {
		cbxAdvancePayment.setValue(null);
		cbxDealer.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {	
		List<Cashflow> cashflows = cashflowService.getListCashflow(getRestrictions());
		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment != null) {
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiAdvancePaymentUsd(MyNumberUtils.getDouble(cashflowPayment.getTiAdvancePaymentUsd()));
				}
			} else {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setId(cashflow.getId());
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setApplicant(cashflow.getContract().getApplicant());
				cashflowPayment.setPayment(cashflow.getPayment());
				cashflowPayment.setTiAdvancePaymentUsd(cashflow.getContract().getTiAdvancePaymentAmount());
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiAdvancePaymentUsd(cashflow.getContract().getTiAdvancePaymentAmount());
				}
				cashflowPayments.add(cashflowPayment);
			}
		}
		setIndexedContainer(cashflowPayments);
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<CashflowPayment> cashflowPayments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			Contract contract = cashflowPayment.getContract();
			Payment payment = cashflowPayment.getPayment();
			Item item = indexedContainer.addItem(cashflowPayment.getId());
			item.getItemProperty(ID).setValue(cashflowPayment.getId());
			item.getItemProperty("payment.id").setValue(payment.getId());
			item.getItemProperty(CONTRACT).setValue(contract.getReference());
			item.getItemProperty("official.payment.no").setValue(payment.getReference().replaceAll("-OR", ""));
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
			item.getItemProperty("motor.model").setValue(contract.getAsset().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(contract.getAsset().getTiAssetPrice()));
			item.getItemProperty("down.payment.date").setValue(payment.getPaymentDate());
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(contract.getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(contract.getTiAdvancePaymentAmount()));	
		}						
		pagedTable.refreshContainerDataSource();
		
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60, false));
		columnDefinitions.add(new ColumnDefinition("payment.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("official.payment.no", I18N.message("official.payment.no"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("down.payment.date", I18N.message("down.payment.date"), Date.class, Align.CENTER, 150));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 100));
		return columnDefinitions;

	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getReference() !=null && cashflow.getContract().getReference() !=null) {
				if (cashflowPayment.getContract().getReference().equals(cashflow.getContract().getReference())
						&& DateUtils.getDateWithoutTime(cashflowPayment.getPayment().getPaymentDate())
							.compareTo(DateUtils.getDateWithoutTime(cashflow.getPayment().getPaymentDate())) == 0) {
					return cashflowPayment;
				}
			}
		}
		return null;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	/**
	 * @author ly.youhort
	 */
	private class CashflowPayment implements Serializable, Entity {
		private static final long serialVersionUID = 3112339520304252300L;
		private Long id;
		private Applicant applicant;
		private Contract contract;
		private Payment payment;
		private Double tiAdvancePaymentUsd;
		
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
		 * @return the applicant
		 */
		public Applicant getApplicant() {
			return applicant;
		}
		/**
		 * @param applicant the applicant to set
		 */
		public void setApplicant(Applicant applicant) {
			this.applicant = applicant;
		}
		/**
		 * @return the contract
		 */
		public Contract getContract() {
			return contract;
		}
		/**
		 * @param contract the contract to set
		 */
		public void setContract(Contract contract) {
			this.contract = contract;
		}
		/**
		 * @return the payment
		 */
		public Payment getPayment() {
			return payment;
		}
		/**
		 * @param payment the payment to set
		 */
		public void setPayment(Payment payment) {
			this.payment = payment;
		}
		
		/**
		 * @return the teAdvancePaymentUsd
		 */
		public Double getTiAdvancePaymentUsd() {
			return tiAdvancePaymentUsd;
		}
		/**
		 * @param teAdvancePaymentUsd the teAdvancePaymentUsd to set
		 */
		public void setTiAdvancePaymentUsd(Double tiAdvancePaymentUsd) {
			this.tiAdvancePaymentUsd = tiAdvancePaymentUsd;
		}
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
