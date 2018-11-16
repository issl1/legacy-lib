package com.nokor.efinance.core.payment.report.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BankDepositsReportPerDealerPerDayPanel.NAME)
public class BankDepositsReportPerDealerPerDayPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "bank.deposits.report.dealer.day";
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Set<Long> selectedItemIds;
	private List<Dealer> dealers;
	private Indexed indexedContainer;
	private BankDepositReportTablePanel bankDepositReportTablePanel;
	private int numTabChange = 0;
	
	public BankDepositsReportPerDealerPerDayPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (numTabChange > 1) {
					if (bankDepositReportTablePanel != null) {
						tabSheet.removeComponent(bankDepositReportTablePanel);
						bankDepositReportTablePanel = null;
						numTabChange = -1;
					}
				}
				numTabChange++;
				}
			});
		selectedItemIds = new HashSet<Long>();
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
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 0);
        gridLayout.addComponent(dfStartDate, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 0);
        gridLayout.addComponent(dfEndDate, iCol++, 0);
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
       
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Payment>(this.columnDefinitions);
        pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					numTabChange++;
					Item item = event.getItem();
					Long dealerId = (Long) item.getItemProperty(ID).getValue();
					Date date = (Date) item.getItemProperty("date").getValue();
					bankDepositReportTablePanel = new BankDepositReportTablePanel();
					bankDepositReportTablePanel.assignValues(ENTITY_SRV.getById(Dealer.class, dealerId), date);
					tabSheet.addTab(bankDepositReportTablePanel, I18N.message("bank.deposit"));
					tabSheet.setSelectedTab(bankDepositReportTablePanel);
				}
			}
        });
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("bank.deposits"));
        return tabSheet;
	}
	
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		dealers = new ArrayList<>();
		if (cbxDealer.getSelectedEntity() == null) {
			dealers = DataReference.getInstance().getDealers();
		} else {
			dealers.add(cbxDealer.getSelectedEntity());
		}
		setIndexedContainer(dealers,
				DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue()),
				DateUtils.getDateAtBeginningOfDay(dfEndDate.getValue()));
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Dealer> dealers, Date startDate, Date endDate) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
    	long dateLongValue = DateUtils.getDiffInDays(endDate, startDate);
    	int numDay = (int) (dateLongValue) + 1;
    	int index = 0;
		for (final Dealer dealer : dealers) {
			for (int i = 0; i < numDay; i++) {
				final Item item = indexedContainer.addItem(index);
				Date date = DateUtils.addDaysDate(startDate, i);
				BankDeposit bankDeposit = getBankDeposit(dealer, date);
				
				item.getItemProperty("index").setValue(index);
				item.getItemProperty(ID).setValue(dealer.getId());
				item.getItemProperty("date").setValue(date);
				item.getItemProperty("num.installment").setValue(bankDeposit.getNumInstallment());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(dealer.getNameEn());
				item.getItemProperty("installment.amount").setValue(bankDeposit.getInstallmentAmount());
				item.getItemProperty("total.other.amount").setValue(bankDeposit.getTotalOtherAmount());
				item.getItemProperty("total.penalty").setValue(bankDeposit.getTotalPenalty());
				item.getItemProperty("total.amount").setValue(bankDeposit.getTotalAmount());
				item.getItemProperty("deposited").setValue(getDepositedAmount(dealer, date));
				index++;
			}
		}
		
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("num.installment", I18N.message("num.installment"), Integer.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Double.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("total.other.amount", I18N.message("other.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.penalty", I18N.message("total.penalty"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("deposited", I18N.message("deposited"), Double.class, Align.RIGHT, 100));
		// columnDefinitions.add(new ColumnDefinition("remaining.balance", I18N.message("remaining.balance"), Double.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * 
	 * @param dealer
	 * @param dealerPaymentDate
	 * @return deposited amount
	 */
	private double getDepositedAmount(Dealer dealer, Date dealerPaymentDate) {

		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		restrictions.addCriterion(Restrictions.isNotNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("dealerPaymentDate", DateUtils.getDateAtBeginningOfDay(dealerPaymentDate)));
		restrictions.addCriterion(Restrictions.le("dealerPaymentDate", DateUtils.getDateAtEndOfDay(dealerPaymentDate)));
		List<Payment> payments = ENTITY_SRV.list(restrictions);
		double depositedAmount = 0d;
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				depositedAmount += payment.getTiPaidAmount();
			}
		}
		return depositedAmount;
	}

	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return bankDeposit
	 */
	private BankDeposit getBankDeposit(Dealer dealer, Date date) {

		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		restrictions.addCriterion(Restrictions.isNotNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(date)));
		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(date)));
		List<Payment> payments = ENTITY_SRV.list(restrictions);
		
		int numInstallment = 0;
		double installmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double totalAmount = 0d;
		List<Payment> paymentsByDealer = new ArrayList<>();
		
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {				
				numInstallment++;
				List<Cashflow> cashflows = payment.getCashflows();
				if (cashflows != null && !cashflows.isEmpty()) {
					for (Cashflow cashflow : cashflows) {
						if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
							totalPenalty += cashflow.getTiInstallmentAmount();
						} else if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
							installmentAmount += cashflow.getTiInstallmentAmount();
						} else {
							totalOtherAmount += cashflow.getTiInstallmentAmount();
						} 
					}
				}
				paymentsByDealer.add(payment);
			}
		}
		totalAmount += installmentAmount + totalOtherAmount + totalPenalty;
		BankDeposit bankDeposit = new BankDeposit();
		bankDeposit.setNumInstallment(numInstallment);
		bankDeposit.setInstallmentAmount(installmentAmount);
		bankDeposit.setTotalOtherAmount(totalOtherAmount);
		bankDeposit.setTotalPenalty(totalPenalty);
		bankDeposit.setTotalAmount(totalAmount);
		return bankDeposit;
	}
	
	/**
	 * @author sok.vina
	 */
	private class BankDeposit {
		
		private int numInstallment;
		private double installmentAmount;
		private double totalOtherAmount;
		private double totalPenalty;
		private double totalAmount;
		/**
		 * @return the numInstallment
		 */
		public int getNumInstallment() {
			return numInstallment;
		}
		/**
		 * @param numInstallment the numInstallment to set
		 */
		public void setNumInstallment(int numInstallment) {
			this.numInstallment = numInstallment;
		}
		/**
		 * @return the installmentAmount
		 */
		public double getInstallmentAmount() {
			return installmentAmount;
		}
		/**
		 * @param installmentAmount the installmentAmount to set
		 */
		public void setInstallmentAmount(double installmentAmount) {
			this.installmentAmount = installmentAmount;
		}
		/**
		 * @return the totalOtherAmount
		 */
		public double getTotalOtherAmount() {
			return totalOtherAmount;
		}
		/**
		 * @param totalOtherAmount the totalOtherAmount to set
		 */
		public void setTotalOtherAmount(double totalOtherAmount) {
			this.totalOtherAmount = totalOtherAmount;
		}
		/**
		 * @return the totalPenalty
		 */
		public double getTotalPenalty() {
			return totalPenalty;
		}
		/**
		 * @param totalPenalty the totalPenalty to set
		 */
		public void setTotalPenalty(double totalPenalty) {
			this.totalPenalty = totalPenalty;
		}
		/**
		 * @return the totalAmount
		 */
		public double getTotalAmount() {
			return totalAmount;
		}
		/**
		 * @param totalAmount the totalAmount to set
		 */
		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}
	}
}
