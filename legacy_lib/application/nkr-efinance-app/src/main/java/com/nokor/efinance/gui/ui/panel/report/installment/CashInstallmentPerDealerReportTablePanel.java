package com.nokor.efinance.gui.ui.panel.report.installment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.InstallmentChecked;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CashInstallmentPerDealerReportTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {
	/** */
	private static final long serialVersionUID = -3726758692524818819L;
	private static final String CASH_INSTALLMENT = "cash.installments.per.dealer";
	private CashInstallmentPerDealerReportSearchPanel reportSearchPanel;
	private List<DealerInstallmentVO> lstDealerInstallmentVO;
	private Map<Item, DealerInstallmentVO> mapItem;
	private Button btnSave;
	private boolean needRefresh = false;
	private PaymentService paymentService = SpringUtils.getBean(PaymentService.class);
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message(CASH_INSTALLMENT));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createBeforeTablePanel()
	 */
	@Override
	protected Panel createBeforeTablePanel() {
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		btnSave = ComponentFactory.getButton("save");
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(getButtonClickListener());
		horizontalLayout.addComponent(btnSave);
		horizontalLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
		panel.setContent(horizontalLayout);
		
		return panel;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Payment getEntity() {
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CashInstallmentPerDealerReportSearchPanel createSearchPanel() {
		this.reportSearchPanel = new CashInstallmentPerDealerReportSearchPanel(this);
		return this.reportSearchPanel;		
	}

	/**
	 * The Button save Listener
	 * @return
	 */
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = 4272510506571645225L;
			@Override
			public void buttonClick(ClickEvent event) {
				saveOrUpdateBankDepositCheck();
			}
		};
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		lstDealerInstallmentVO = null;
		needRefresh = true;
		super.refresh();
	}
	
	/**
	 * 
	 */
	private void saveOrUpdateBankDepositCheck () {
		if (this.lstDealerInstallmentVO == null || this.lstDealerInstallmentVO.isEmpty()) {
			return;
		}
		for (DealerInstallmentVO VO : lstDealerInstallmentVO) {
			List<InstallmentChecked> lstInstallmentCheckeds = paymentService.getInstallmentChecked(VO.getDealer(), VO.getDate(), VO.getDate());
			if (lstInstallmentCheckeds != null && !lstInstallmentCheckeds.isEmpty()) {
				if (VO.getCheckBox().getValue() == false) {
					ENTITY_SRV.delete(lstInstallmentCheckeds.get(0));
				}
			} else {
				if (VO.getCheckBox().getValue() == true) {
					InstallmentChecked installmentChecked = new InstallmentChecked();
					installmentChecked.setDealer(VO.getDealer());
					installmentChecked.setCheckedDate(VO.getDate());
					ENTITY_SRV.saveOrUpdate(installmentChecked);
				}
			}
		}
		MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
				MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
				new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.show();
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("num.installment", I18N.message("num.installment"), Integer.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 120);
		pagedDefinition.addColumnDefinition("total.other.amount", I18N.message("other.amount"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("total.penalty", I18N.message("total.penalty"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("cbxCheck", I18N.message("check"), CheckBox.class, Align.CENTER, 60);
		pagedDefinition.addColumnDefinition("amount.per.wing", I18N.message("amount.received.per.wing"), Amount.class, Align.RIGHT, 165);
		pagedDefinition.addColumnDefinition("amount.per.cash", I18N.message("amount.received.per.cash"), Amount.class, Align.RIGHT, 175);
		
		PaymentPagedDataProvider<Payment> pagedDataProvider = new PaymentPagedDataProvider<Payment>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * 
	 * @param indexedContainer
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 */
	private void createRow (IndexedContainer indexedContainer, DealerInstallmentVO dealerInstallmentVO) {
		Item item = indexedContainer.addItem(dealerInstallmentVO);
		
		item.getItemProperty("date").setValue(dealerInstallmentVO.getDate());
		item.getItemProperty("num.installment").setValue(dealerInstallmentVO.getNumInstallment());
		item.getItemProperty(DEALER + "." + NAME_EN).setValue(dealerInstallmentVO.getDealer().getNameEn());
		item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getInstallmentAmount()));
		item.getItemProperty("total.other.amount").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getTotalOtherAmount()));
		item.getItemProperty("total.penalty").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getTotalPenalty()));
		item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getTotalAmount()));
		item.getItemProperty("cbxCheck").setValue(dealerInstallmentVO.getCheckBox());
		item.getItemProperty("amount.per.wing").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getAmountPerWing()));
		item.getItemProperty("amount.per.cash").setValue(AmountUtils.convertToAmount(dealerInstallmentVO.getAmountPerCash()));
		mapItem.put(item, dealerInstallmentVO);
	}
	
	/**
	 * 
	 * @author prasnar
	 *
	 * @param <T>
	 */
	private class PaymentPagedDataProvider<T> extends EntityPagedDataProvider<Payment> {
		/** */
		private static final long serialVersionUID = -5515736145662778914L;
		private IndexedContainer indexedContainer;

		@Override
		public IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults) {
			indexedContainer = new IndexedContainer();

			// Fetch data 
			if (needRefresh) {
				List<Payment> payments = fetchPayments();
				lstDealerInstallmentVO = buildInstallmentsPerDealer(payments, reportSearchPanel.getStartDate(), reportSearchPanel.getEndDate());
				needRefresh = false;
			}
			
			// Build columns names
			for (ColumnDefinition columnDefinition : getPagedDefinition().getColumnDefinitions()) {
				indexedContainer.addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
			}
			
			if (lstDealerInstallmentVO == null || lstDealerInstallmentVO.isEmpty()) {
				return indexedContainer;
			}
			
			// Build rows data
			mapItem = new HashMap<Item, DealerInstallmentVO>();
			int index = firstResult;
			while (index < lstDealerInstallmentVO.size()
					&& index < firstResult + maxResults) {
				
				createRow(indexedContainer, lstDealerInstallmentVO.get(index));
				index++;
			}
			createTableFooter(lstDealerInstallmentVO);
			
			return indexedContainer;
		}
		
		/**
		 * 
		 */
		private List<Payment> fetchPayments() {
			try {
				BaseRestrictions<Payment> baseRestrictions = getPagedDefinition().getRestrictions();
				List<Payment> entities = ENTITY_SRV.list(baseRestrictions);
				return entities;
			} catch (Exception e) {
				logger.error("Error at FecthEntities", e);
				return new ArrayList<Payment>();
			}
		}
		
		/**
		 * 
		 */
		@Override
		public long getTotalRecords() {
			List<Dealer> dealers = reportSearchPanel.getSelectedDealer();
			Date startDate = reportSearchPanel.getStartDate();
			Date endDate = reportSearchPanel.getEndDate();
			long nbDate = DateUtils.getDiffInDays(endDate, startDate) + 1;
			if (dealers != null && !dealers.isEmpty()) {
				return dealers.size() * nbDate;
			}
			return 0;
		}
		
	}
	
	/**
	 * 
	 * @param lstVO
	 */
	private void createTableFooter (List<DealerInstallmentVO> lstVO) {
		double totalInstallmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double totalAmount = 0d;
		double totalReceivedPerWing = 0d;
		double totalReceivedPerCash = 0d;
		
		if (lstVO == null) {
			return;
		}
		
		for (DealerInstallmentVO dealerInstallmentVO : lstVO) {
			totalInstallmentAmount += dealerInstallmentVO.getInstallmentAmount();
			totalOtherAmount += dealerInstallmentVO.getTotalOtherAmount();
			totalPenalty += dealerInstallmentVO.getTotalPenalty();
			totalAmount += dealerInstallmentVO.getTotalAmount();
			totalReceivedPerWing += dealerInstallmentVO.getAmountPerWing();
			totalReceivedPerCash += dealerInstallmentVO.getAmountPerCash();
		}
		
		pagedTable.setFooterVisible(true);
		DecimalFormat df = new DecimalFormat("##0.00");
		pagedTable.setColumnFooter(DEALER + "." + NAME_EN, I18N.message("total"));
		pagedTable.setColumnFooter("installment.amount", df.format(totalInstallmentAmount));
		pagedTable.setColumnFooter("total.other.amount", df.format(totalOtherAmount));
		pagedTable.setColumnFooter("total.penalty", df.format(totalPenalty));
		pagedTable.setColumnFooter("total.amount", df.format(totalAmount));
		pagedTable.setColumnFooter("amount.per.wing", df.format(totalReceivedPerWing));
		pagedTable.setColumnFooter("amount.per.cash", df.format(totalReceivedPerCash));
	}
	
	/**
	 * 
	 * @param dealers
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<DealerInstallmentVO> buildInstallmentsPerDealer(List<Payment> payments, Date startDate, Date endDate) {
		List<DealerInstallmentVO> lstVO = new ArrayList<>();
    	if (payments == null) {
    		return lstVO;
    	}

    	long numDay = DateUtils.getDiffInDaysPlusOneDay(endDate, startDate);
    	
		List<Dealer> dealers = reportSearchPanel.getSelectedDealer();

		for (Dealer dealer : dealers) {
			List<Payment> paymentsByDealer = new ArrayList<Payment>();
			for (Payment payment : payments) {
				if (dealer.getId().equals(payment.getDealer().getId())) {
					paymentsByDealer.add(payment);
				}
			}
			for (int i = 0; i < numDay; i++) {
				Date searchDate = DateUtils.addDaysDate(startDate, i);
				List<Payment> paymentsByDate = new ArrayList<Payment>();
				for (Payment payment : paymentsByDealer) {
					if (DateUtils.isSameDay(searchDate, payment.getPaymentDate())) {
						paymentsByDate.add(payment);
					}
				}
				CheckBox cbxCheck = getCheckBox(dealer, searchDate);
				DealerInstallmentVO dealerInstallmentVO = getDealerInstallmentVOPerDay(paymentsByDate, dealer, searchDate, cbxCheck);
				lstVO.add(dealerInstallmentVO);
			}
		}
		
		return lstVO;
	}
	
	/**
	 * @param dealer
	 * @param date
	 * @return
	 */
	private CheckBox getCheckBox (Dealer dealer, Date date) {
		CheckBox cbxCheck = new CheckBox();
		List<InstallmentChecked> lstInstallmentCheckeds = paymentService.getInstallmentChecked(dealer, date, date);
		if (lstInstallmentCheckeds != null && !lstInstallmentCheckeds.isEmpty()) {
			cbxCheck.setValue(true);
		}
		if (lstDealerInstallmentVO == null) {
			return cbxCheck;
		}
		for (DealerInstallmentVO dealerInstallmentVO : lstDealerInstallmentVO) {
			if (dealerInstallmentVO.getDealer().getId().equals(dealer.getId())
					&& DateUtils.isSameDay(dealerInstallmentVO.getDate(), date)) {
				cbxCheck.setValue(dealerInstallmentVO.getCheckBox().getValue());
			}
		}
		return cbxCheck;
	}

	/**
	 * @param payments
	 * @param dealer
	 * @param date
	 * @param cbxCheck
	 * @return
	 */
	private DealerInstallmentVO getDealerInstallmentVOPerDay (List<Payment> payments, Dealer dealer, Date date, CheckBox checkBox) {
		DealerInstallmentVO installmentVO = new DealerInstallmentVO();
		installmentVO.setDealer(dealer);
		installmentVO.setDate(date);
		installmentVO.setCheckBox(checkBox);
		if (payments == null || payments.isEmpty()) {
			return installmentVO;
		}
		
		int numInstallment = 0;
		double installmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double amountPerWing = 0d;
		double amountPerCash = 0d;
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
					
					if ("CASH".equals(cashflow.getPaymentMethod().getCode())) {
						amountPerCash += cashflow.getTiInstallmentAmount();
					} else if ("WING".equals(cashflow.getPaymentMethod().getCode())) {
						amountPerWing += cashflow.getTiInstallmentAmount();
					}
				}
			}
		}
		totalAmount += amountPerCash + amountPerWing;
		
		installmentVO.setNumInstallment(numInstallment);
		installmentVO.setInstallmentAmount(installmentAmount);
		installmentVO.setTotalOtherAmount(totalOtherAmount);
		installmentVO.setTotalPenalty(totalPenalty);
		installmentVO.setTotalAmount(totalAmount);
		installmentVO.setAmountPerWing(amountPerWing);
		installmentVO.setAmountPerCash(amountPerCash);
		installmentVO.setPayments(payments);
		
		return installmentVO;
	}
	
	
	/**
	 * Get the dealer selected
	 * @return Dealer
	 */
	public Dealer getDealerSelected () {
		return mapItem.get(getSelectedItem()).getDealer();
	}
	
	/**
	 * Get the date selected
	 * @return Date
	 */
	public Date getDateSelected () {
		return mapItem.get(getSelectedItem()).getDate();
	}
}
