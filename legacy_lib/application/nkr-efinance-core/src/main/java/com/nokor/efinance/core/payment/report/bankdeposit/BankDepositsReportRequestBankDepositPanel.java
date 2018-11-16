package com.nokor.efinance.core.payment.report.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Request bank deposit report panel
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BankDepositsReportRequestBankDepositPanel.NAME)
public class BankDepositsReportRequestBankDepositPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "report.request.bankdeposit";
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private Set<Long> selectedItemIds;
	
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Indexed indexedContainer;
	private BankDepositTableReportPanel bankDepositTableReportPanel;
	
	private Date startDate;
	private Date endDate;
	
	private int numTabChange = 0;
	
	/**
	 * 
	 */
	public BankDepositsReportRequestBankDepositPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (numTabChange > 1) {
					if (bankDepositTableReportPanel != null) {
						tabSheet.removeComponent(bankDepositTableReportPanel);
						bankDepositTableReportPanel = null;
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
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 6205787213725631493L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
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
					BankDeposit bankDeposit = (BankDeposit) item.getItemProperty("bank.deposit").getValue();
					bankDepositTableReportPanel = new BankDepositTableReportPanel();
					bankDepositTableReportPanel.assignValues(bankDeposit);
					tabSheet.addTab(bankDepositTableReportPanel, I18N.message("bank.deposit"));
					tabSheet.setSelectedTab(bankDepositTableReportPanel);
				}
			}
        });
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("bank.deposits"));
        return tabSheet;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		this.startDate = DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue());
		this.endDate = DateUtils.getDateAtEndOfDay(dfEndDate.getValue());
		setIndexedContainer(cbxDealer.getSelectedEntity(), startDate, endDate);
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings({ "unchecked", "serial" })
	private void setIndexedContainer(Dealer dealer, Date startDate, Date endDate) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		List<Dealer> dealers = new ArrayList<Dealer>();
		if (dealer == null) {
			dealers = DataReference.getInstance().getDealers();
		} else {
			dealers.add(dealer);
		}
		int index = 0;
		for(Dealer dealer2 : dealers) {
			List<BankDeposit> bankDeposits = getBankDeposits(dealer2, startDate, endDate);
			if (bankDeposits != null && !bankDeposits.isEmpty()) {
				for (final BankDeposit bankDeposit : bankDeposits) {
					final Item item = indexedContainer.addItem(index);
					Button btnPrint = new Button();
					btnPrint.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
					btnPrint.addClickListener(new ClickListener() {
						private static final long serialVersionUID = -7165734546798826698L;
						@Override
						public void buttonClick(ClickEvent event) {
							try {
								ReportService reportService = SpringUtils.getBean(ReportService.class);
								String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
								ReportParameter reportParameter = new ReportParameter();
								BankDeposit bankDeposit = (BankDeposit) item.getItemProperty("bank.deposit").getValue();
								reportParameter.addParameter("bankDeposit", bankDeposit);
								String fileName = reportService.extract(BankDepositReportExtraction.class, reportParameter);
								DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
								UI.getCurrent().addWindow(documentViewver);
							} catch (Exception e) {
								logger.error("", e);
							}
						}
					});
					Button btnDelete = ComponentFactory.getButton();
					btnDelete.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
					btnDelete.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							BaseRestrictions<BankDeposit> restrictions = new BaseRestrictions<>(BankDeposit.class);
							restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, bankDeposit.getDealer().getId()));
							restrictions.addCriterion(Restrictions.ge("requestDepositDate", DateUtils.getDateAtBeginningOfDay(bankDeposit.getRequestDepositDate())));
							restrictions.addCriterion(Restrictions.le("requestDepositDate", DateUtils.getDateAtEndOfDay(bankDeposit.getRequestDepositDate())));
							restrictions.addOrder(Order.desc("requestDepositDate"));
							List<BankDeposit> bankDepositToDelete = ENTITY_SRV.list(restrictions);
							if (bankDepositToDelete != null && !bankDepositToDelete.isEmpty()) {
								for (BankDeposit bankDeposit2 : bankDepositToDelete) {
									bankDeposit2.setRequestDate(null);
									bankDeposit2.setRequestDepositDate(null);
									ENTITY_SRV.saveOrUpdate(bankDeposit2);
								}
							}
							search();
						}
					});
					
					List<Payment> payments = bankDeposit.getPayments();
					BankDepositVO bankDepositVO = getBankDepositVO(payments, bankDeposit.getRequestDepositDate());
					
					item.getItemProperty("index").setValue(index);
					item.getItemProperty("bank.deposit").setValue(bankDeposit);
					item.getItemProperty("btnDelete").setValue(btnDelete);
					item.getItemProperty("btnPrint").setValue(btnPrint);
					item.getItemProperty("request.date").setValue(bankDeposit.getRequestDate());
					item.getItemProperty("request.deposit.on").setValue(bankDepositVO.getRequestDepositOn());
					item.getItemProperty("num.installment").setValue(bankDepositVO.getNumInstallment());
					item.getItemProperty(DEALER + "." + NAME_EN).setValue(bankDeposit.getDealer().getNameEn());
					item.getItemProperty("installment.amount").setValue(bankDepositVO.getInstallmentAmount());
					item.getItemProperty("total.other.amount").setValue(bankDepositVO.getTotalOtherAmount());
					item.getItemProperty("total.penalty").setValue(bankDepositVO.getTotalPenalty());
					item.getItemProperty("total.amount").setValue(bankDepositVO.getTotalAmount());
					//item.getItemProperty("deposited").setValue(getDepositedAmount(bankDeposit.getDealer(), bankDeposit.getRequestDate()));
					index++;	
				}
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
		columnDefinitions.add(new ColumnDefinition("bank.deposit", I18N.message("id"), BankDeposit.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("btnDelete", "", Button.class, Align.CENTER, 80));
		columnDefinitions.add(new ColumnDefinition("btnPrint", "", Button.class, Align.CENTER, 80));
		columnDefinitions.add(new ColumnDefinition("request.date", I18N.message("request.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("request.deposit.on", I18N.message("request.deposit.on"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("num.installment", I18N.message("num.installment"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Double.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("total.other.amount", I18N.message("other.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.penalty", I18N.message("total.penalty"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Double.class, Align.RIGHT, 90));
		//columnDefinitions.add(new ColumnDefinition("deposited", I18N.message("deposited"), Double.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * 
	 * @param dealer
	 * @param dealerPaymentDate
	 * @return deposited amount
	 */
	@SuppressWarnings("unused")
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
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<BankDeposit> getBankDeposits(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<BankDeposit> restrictions = new BaseRestrictions<>(BankDeposit.class);
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		/*restrictions.addCriterion(Restrictions.ge("requestDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("requestDate", DateUtils.getDateAtEndOfDay(endDate)));*/
		restrictions.addCriterion(Restrictions.ge("requestDepositDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("requestDepositDate", DateUtils.getDateAtEndOfDay(endDate)));
		restrictions.addOrder(Order.desc("requestDepositDate"));
		List<BankDeposit> bankDeposits = ENTITY_SRV.list(restrictions);
		
		// Group by Date
		BankDeposit bankDeposit = null;
		List<BankDeposit> depositByDates = new ArrayList<BankDeposit>();
		if (bankDeposits != null && !bankDeposits.isEmpty()) {
			bankDeposit = bankDeposits.get(0);
			for(int i = 1; i < bankDeposits.size(); i++) {
				if (DateUtils.isSameDay(bankDeposit.getRequestDepositDate(), bankDeposits.get(i).getRequestDepositDate())) {
					if (bankDeposits.get(i).getAmountReceivedFromDealerUSD() != null) {
						if (bankDeposit.getAmountReceivedFromDealerUSD() != null) {
							bankDeposit.setAmountReceivedFromDealerUSD(bankDeposit.getAmountReceivedFromDealerUSD()
									+ bankDeposits.get(i).getAmountReceivedFromDealerUSD());
						}
					}
					bankDeposit.getPayments().addAll(bankDeposits.get(i).getPayments());
				} else {
					depositByDates.add(bankDeposit);
					bankDeposit = bankDeposits.get(i);
				}
			}
		}
		if (bankDeposit != null) {
			depositByDates.add(bankDeposit);
		}
		
		return depositByDates;
	}
	
	/**
	 * 
	 * @param payments
	 * @param requestDepositOn
	 * @return
	 */
	private BankDepositVO getBankDepositVO(List<Payment> payments, Date requestDepositOn) {		
		int numInstallment = 0;
		double installmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double totalAmount = 0d;
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
		}
		totalAmount += installmentAmount + totalOtherAmount + totalPenalty;
		BankDepositVO bankDeposit = new BankDepositVO();
		bankDeposit.setNumInstallment(numInstallment);
		bankDeposit.setInstallmentAmount(installmentAmount);
		bankDeposit.setTotalOtherAmount(totalOtherAmount);
		bankDeposit.setTotalPenalty(totalPenalty);
		bankDeposit.setTotalAmount(totalAmount);
		bankDeposit.setRequestDepositOn(requestDepositOn);
		return bankDeposit;
	}
	
	/**
	 * @author sok.vina
	 */
	private class BankDepositVO {
		
		private int numInstallment;
		private double installmentAmount;
		private double totalOtherAmount;
		private double totalPenalty;
		private double totalAmount;
		private Date requestDepositOn;
		
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
		
		/**
		 * @return the requestDepositOn
		 */
		public Date getRequestDepositOn() {
			return requestDepositOn;
		}
		
		/**
		 * @param requestDepositOn the requestDepositOn to set
		 */
		public void setRequestDepositOn(Date requestDepositOn) {
			this.requestDepositOn = requestDepositOn;
		}
	}
}
