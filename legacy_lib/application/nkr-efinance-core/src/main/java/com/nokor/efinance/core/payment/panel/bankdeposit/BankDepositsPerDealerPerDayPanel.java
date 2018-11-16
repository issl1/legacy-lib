package com.nokor.efinance.core.payment.panel.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.BankDepositChecked;
import com.nokor.efinance.core.payment.model.BankDepositReceivedFromDealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.bankdeposit.BankDepositService;
import com.nokor.efinance.core.shared.bank.BankDepositCheck;
import com.nokor.efinance.core.shared.bank.BankDepositVO;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
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
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(BankDepositsPerDealerPerDayPanel.NAME)
public class BankDepositsPerDealerPerDayPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "bank.deposits.dealer.day";
	private BankDepositService bankDepositService = SpringUtils.getBean(BankDepositService.class);
	private TabSheet tabSheet;
	private BankDepositsPerDealerPerDayPanel bankDepositPanel;
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private CheckBox cbxDeposited;
	private CheckBox cbxCheck;
	private Button btnReceive;
	private Button btnSave;
	private Button btnDeposited;
	private TextField txtTotalAmount;
	private Indexed indexedContainer;
	private Set<Long> selectedItemIds;
	private List<Integer> listIndex;
	private List<AutoDateField> listPaymentDealerDates;
	private List<Dealer> dealers;
	private List<DealerCheckBox> dealerCheckboxs;
	private List<BankDepositChecked> bankDepositCheckeds;
	private List<CheckboxIndex> checkboxIndexs;
	private List<Date> dateCheckeds;
	private List<Long> dealerChecks;
	private int numTabChange = 0;
	private BankDepositTablePanel bankDepositTablePanel;
	private AutoDateField dfRequestDepositOn;
	private List<AutoDateField> dfRequestDepositOns;
	private Map<Integer, BankDeposit> bankDeposits;
	private List<Payment> paymentBankDeposit;
	private TextField txtAmountReceivedFromDealer;
	private Map<Integer, BankDepositVO> amountReceivedFromDealers;
	
	
	public BankDepositsPerDealerPerDayPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		this.bankDepositPanel = this;
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2435529941310008060L;
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (numTabChange > 1) {
					if (bankDepositTablePanel != null) {
						tabSheet.removeComponent(bankDepositTablePanel);
						bankDepositTablePanel = null;
						numTabChange = -1;
					}
				}
				numTabChange++;
				}
			});
		bankDeposits = new HashMap<>();
		dfRequestDepositOns = new ArrayList<AutoDateField>();
		selectedItemIds = new HashSet<Long>();
		dealerCheckboxs = new ArrayList<DealerCheckBox>();
		listPaymentDealerDates = new ArrayList<AutoDateField>();
		listIndex = new ArrayList<Integer>();
		amountReceivedFromDealers = new HashMap<>();
		
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
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        txtTotalAmount = new TextField();
        txtTotalAmount.setEnabled(false);
        txtTotalAmount.setImmediate(true);
        
        btnDeposited = new NativeButton(I18N.message("deposited"));
        btnDeposited.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
        btnDeposited.setVisible(ProfileUtil.isAccountingController());
        btnDeposited.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				if (bankDeposits != null && !bankDeposits.isEmpty()) {
					boolean validateDateRequestBankDeposit = true;
					for (Integer index : listIndex) {
						final Item item = indexedContainer.getItem(index);
						AutoDateField dfrequestBankdepositOn = (AutoDateField) item.getItemProperty("request.deposit.on").getValue();
						if (dfrequestBankdepositOn == null
								|| dfrequestBankdepositOn.getValue() == null) {
							validateDateRequestBankDeposit = false;
							break;
						}
					}
					if (validateDateRequestBankDeposit) {
						ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.process.this.dealer.deposit"),
	    				        new ConfirmDialog.Listener() {
	    							private static final long serialVersionUID = 2380193173874927880L;
	    							public void onClose(ConfirmDialog dialog) {
	    				                if (dialog.isConfirmed()) {	
											for (Iterator<BankDeposit> bankdeposit = bankDeposits.values().iterator(); bankdeposit.hasNext();) {
												BankDeposit bankDeposit = bankdeposit.next();
												
												List<BankDeposit> paymentsByDate = getBankDepositByDate(bankDeposit);
												for (BankDeposit preDeposit : paymentsByDate) {
													BankDeposit previousDeposit = bankDepositService.getBankDepositByDealerAndRequestDate(bankDeposit.getDealer(), preDeposit.getRequestDate());
													if (previousDeposit == null) {
														preDeposit.setRequestDepositDate(bankDeposit.getRequestDepositDate());
														ENTITY_SRV.saveOrUpdate(preDeposit);
														
														for (Payment payment : preDeposit.getPayments()) {
											         		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				    				                		payment.setDealerPaymentReceivedUser(secUser);
															payment.setBankDeposit(preDeposit);
															ENTITY_SRV.saveOrUpdate(payment);
														}
													} else {
														break;
													}
												}
												
												ENTITY_SRV.saveOrUpdate(bankDeposit);
												if (bankDeposit.getPayments() != null) {
													for (Payment payment : bankDeposit.getPayments()) {
										         		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			    				                		payment.setDealerPaymentReceivedUser(secUser);
														payment.setBankDeposit(bankDeposit);
														ENTITY_SRV.saveOrUpdate(payment);
													}
												}
											}
	    				                	search();
										}
									}
						});
						confirmDialog.setWidth("400px");
	    				confirmDialog.setHeight("150px");
					} else {
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.select.the.date.request.bankdeposit.on"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}
					
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.the.dealer.deposit.you.want.processing"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
				
			}
		});
        
        btnReceive = new NativeButton(I18N.message("receive"));
        btnReceive.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
        btnReceive.setVisible(ProfileUtil.isAccountingController());
        
        btnReceive.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				for (Iterator<BankDepositVO> bankdeposit = amountReceivedFromDealers.values().iterator(); bankdeposit.hasNext();) {
					BankDepositVO bankDeposit = bankdeposit.next();
					logger.debug("totalAmount - [" + bankDeposit.getTotalAmount() + "]");

					Date requestDate = bankDeposit.getRequestDate();
					Dealer dealer = bankDeposit.getDealer();
					BankDepositReceivedFromDealer bankDepositReceivedFromDealer = bankDepositService.getBankDepositReceivedFromDealer(dealer, requestDate);
					if (bankDepositReceivedFromDealer == null)
					{
						bankDepositReceivedFromDealer = new BankDepositReceivedFromDealer();
						bankDepositReceivedFromDealer.setDealer(dealer);
						bankDepositReceivedFromDealer.setPassToDealerPaymentDate(requestDate);
					}
					Double previousAmount = bankDepositReceivedFromDealer.getAmountReceivedFromDealerUSD() != null ?
							bankDepositReceivedFromDealer.getAmountReceivedFromDealerUSD() : 0d;
					if (bankDeposit.getTotalAmount() + previousAmount < bankDeposit.getAmountReceivedFromDealerUSD()) {
						MessageBox mb = new MessageBox(UI.getCurrent(),
								"400px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message(
										"bank.deposit.receive.amount.error",
										DateUtils.getDateLabel(bankDeposit
												.getRequestDate())),
								Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N
										.message("ok")));
						mb.show();
					} else {
						double amountReceivedFromDealerUSD = bankDeposit.getAmountReceivedFromDealerUSD();
						bankDepositReceivedFromDealer.setAmountReceivedFromDealerUSD(amountReceivedFromDealerUSD);
						ENTITY_SRV.saveOrUpdate(bankDepositReceivedFromDealer);
					}
				}
				search();
			}
		});
        
        btnSave = new NativeButton(I18N.message("save"));
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
        btnSave.setVisible(ProfileUtil.isAccountingController());
        btnSave.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if ((dealerChecks != null && !dealerChecks.isEmpty())
						|| bankDepositCheckeds != null
						&& !bankDepositCheckeds.isEmpty()) {
					getRemovedBankDepositUnChecked(bankDepositCheckeds, dateCheckeds, dealerChecks);
					getUpdateBankDespositChecked(dateCheckeds, dealerChecks);
					Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
					notification.setDescription(I18N.message("save.successfully"));
					notification.setDelayMsec(5000);
					notification.show(Page.getCurrent());
					search();	
				}
			}
		});
        horizontalLayout.addComponent(btnSave);
        horizontalLayout.addComponent(btnDeposited);
        horizontalLayout.addComponent(btnReceive);
        horizontalLayout.addComponent(new Label(I18N.message("total.amount")));
        horizontalLayout.addComponent(txtTotalAmount);
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
					bankDepositTablePanel = new BankDepositTablePanel(bankDepositPanel);
					bankDepositTablePanel.assignValues(ENTITY_SRV.getById(Dealer.class, dealerId), date);
					tabSheet.addTab(bankDepositTablePanel, I18N.message("bank.deposit"));
					tabSheet.setSelectedTab(bankDepositTablePanel);
				}
			}
        });
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("bank.deposits"));
        return tabSheet;
	}
	
	/**
	 * 
	 * @param bankDeposit
	 * @return
	 */
	private List<BankDeposit> getBankDepositByDate (BankDeposit bankDeposit) {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, bankDeposit.getDealer().getId()));
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate",
				DateUtils.getDateAtEndOfDay(DateUtils.addDaysDate(bankDeposit.getRequestDate(), -1))));
		restrictions.addOrder(Order.desc("passToDealerPaymentDate"));
		List<Payment> payments = ENTITY_SRV.list(restrictions);
		
		List<BankDeposit> bankDeposits = new ArrayList<BankDeposit>();
		List<Payment> paymentsByDate = new ArrayList<Payment>();
		if (payments != null && !payments.isEmpty()) {
			paymentsByDate.add(payments.get(0));
			for(Payment payment : payments) {
				if (DateUtils.isSameDay(paymentsByDate.get(0).getPassToDealerPaymentDate(), payment.getPassToDealerPaymentDate())) {
					paymentsByDate.add(payment);
				} else {
					BankDeposit deposit = new BankDeposit();
					deposit.setPayments(paymentsByDate);
					deposit.setRequestDate(paymentsByDate.get(0).getPassToDealerPaymentDate());
					deposit.setDealer(bankDeposit.getDealer());
					bankDeposits.add(deposit);
					paymentsByDate = new ArrayList<Payment>();
					paymentsByDate.add(payment);
				}
			}
			if (!paymentsByDate.isEmpty()) {
				BankDeposit deposit = new BankDeposit();
				deposit.setPayments(paymentsByDate);
				deposit.setRequestDate(paymentsByDate.get(0).getPassToDealerPaymentDate());
				deposit.setDealer(bankDeposit.getDealer());
				bankDeposits.add(deposit);
			}
		}
		
		return bankDeposits;
	}

	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	public void search() {
		bankDeposits.clear();
		listIndex.clear();
		dealerCheckboxs.clear();
		listPaymentDealerDates.clear();
		dfRequestDepositOns.clear();
		amountReceivedFromDealers.clear();
		txtTotalAmount.setValue("");
		this.bankDepositCheckeds = new ArrayList<BankDepositChecked>();
		dateCheckeds = new ArrayList<Date>();
		dealerChecks = new ArrayList<Long>();
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("id", cbxDealer.getSelectedEntity().getId()));
		}
		dealers = ENTITY_SRV.list(restrictions);
		setIndexedContainer(dealers, bankDepositCheckeds, 
				DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue()),
				DateUtils.getDateAtBeginningOfDay(dfEndDate.getValue()));
		
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Dealer> dealers,
			List<BankDepositChecked> bankDepositCheckeds, Date startDate,
			Date endDate) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
    	long dateLongValue = DateUtils.getDiffInDays(endDate, startDate);
    	int numDay = (int) (dateLongValue) + 1;
    	int index = 0;
		for (final Dealer dealer : dealers) {
			checkboxIndexs = new ArrayList<CheckboxIndex>();
			double totalRemainingBalance = getBankDepositVO(dealer, null, DateUtils.addDaysDate(startDate, -1)).getRemainingAmount();
			for (int i = 0; i < numDay; i++) {
				final Item item = indexedContainer.addItem(index);
				final Date date = DateUtils.addDaysDate(startDate, i);
				cbxDeposited = new CheckBox();
				cbxDeposited.setImmediate(true);
				cbxCheck = new CheckBox();
				cbxCheck.setImmediate(true);
				dfRequestDepositOn = ComponentFactory.getAutoDateField();
				dfRequestDepositOn.setEnabled(false);
				dfRequestDepositOn.setImmediate(true);
				dfRequestDepositOns.add(dfRequestDepositOn);
				dfRequestDepositOn.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -3833196516974751279L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						AutoDateField dfrequestBankdepositOn = (AutoDateField) item.getItemProperty("request.deposit.on").getValue();
						if (dfrequestBankdepositOn != null && dfrequestBankdepositOn.getValue() != null) {

							BankDeposit bankDeposit = bankDepositService.getBankDepositByDealerAndRequestDate(dealer, date);
							if (bankDeposit == null) {
								bankDeposit = new BankDeposit();
							}
							DealerCheckBox dealerCheckbox = getCheckBoxByDealer(dealer);
							if (dealerCheckbox != null) {
								List<CheckboxIndex> lsDealerCheckbox = dealerCheckbox.getCheckboxBankDeposits();
								if (lsDealerCheckbox != null && !lsDealerCheckbox.isEmpty()) {
									for (CheckboxIndex checkboxIndex : lsDealerCheckbox) {
										if(checkboxIndex.getIndex() == (int) item.getItemProperty("index").getValue()) {
											if (checkboxIndex.getPayments() != null && !checkboxIndex.getPayments().isEmpty()) {
												paymentBankDeposit = new ArrayList<Payment>();
												for (Payment payment : checkboxIndex.getPayments()) {
													payment.setDealerPaymentDate(date);
													paymentBankDeposit.add(payment);
												}
											}
										}
									}
								}
							}
							bankDeposit.setRequestDepositDate(DateUtils.getDateAtBeginningOfDay(dfrequestBankdepositOn.getValue()));
							bankDeposit.setPayments(paymentBankDeposit);
							bankDeposit.setRequestDate(date);
							bankDeposit.setDealer(dealer);
							bankDeposits.put((Integer) item.getItemProperty("index").getValue(), bankDeposit);
						}
						
					}
				});
				List<BankDepositChecked> lsBankDepositChecked = BankDepositCheck.getBankDepositChecked(dealer, date, date);
				if (lsBankDepositChecked != null
						&& !lsBankDepositChecked.isEmpty()) {
					cbxCheck.setValue(true);
				}
				
				BankDeposit bankDeposit = bankDepositService.getBankDepositByDealerAndRequestDate(dealer, date);
				if (bankDeposit != null) {
					dfRequestDepositOn.setValue(bankDeposit.getRequestDepositDate());
					cbxDeposited.setValue(true);
					dfRequestDepositOn.setEnabled(false);
					cbxDeposited.setEnabled(false);
				}
				cbxCheck.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -3787569480332378546L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if ((boolean) event.getProperty().getValue()) {
							dateCheckeds.add(DateUtils.getDateAtBeginningOfDay((Date) item.getItemProperty("date").getValue()));
							dealerChecks.add((Long) item.getItemProperty(ID).getValue());
						} else {
							dateCheckeds.remove(DateUtils.getDateAtBeginningOfDay((Date) item.getItemProperty("date").getValue()));
							dealerChecks.remove((Long) item.getItemProperty(ID).getValue());
						}
						
					}
				});
				
				if (i == numDay - 1) {
					DealerCheckBox dealerCheckBox = new DealerCheckBox();
					dealerCheckBox.setDealer(dealer);
					dealerCheckBox.setCheckboxBankDeposits(checkboxIndexs);
					dealerCheckboxs.add(dealerCheckBox);
				}
				
				cbxDeposited.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2120119835501936565L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						double totalAmount = Double.parseDouble(getDefaultString(txtTotalAmount));
						if ((boolean) event.getProperty().getValue()) {
							selectedItemIds.add(dealer.getId());
							setVisibleCheckboxDealer(dealer, item, false);
							listIndex.add((Integer) item.getItemProperty("index").getValue());
							totalAmount += (double) item.getItemProperty("remaining.balance").getValue();
							setEnableAutoDateField(item, true);
						} else {
							selectedItemIds.remove(dealer.getId());
							setVisibleCheckboxDealer(dealer, item, true);
							listIndex.remove((Integer) item.getItemProperty("index").getValue());
							totalAmount -= (double) item.getItemProperty("remaining.balance").getValue();
							setEnableAutoDateField(item, false);
							bankDeposits.remove((Integer) item.getItemProperty("index").getValue());
						}
						txtTotalAmount.setValue(AmountUtils.format((totalAmount <= 0d ? 0d : totalAmount)));
					}
				});
				BankDepositVO bankDepositVO = getBankDepositVO(dealer, date);
				final CheckboxIndex checkboxIndex = new CheckboxIndex();
				checkboxIndex.setIndex(index);
				checkboxIndex.setCbCheck(cbxDeposited);
				List<Payment> paymentsBySearch = bankDepositVO.getPayments();

				totalRemainingBalance += bankDepositVO.getRemainingAmount();
				txtAmountReceivedFromDealer = ComponentFactory.getTextField(60, 120);
				if (DateUtils.isAfterDay(date, DateUtils.today())) {
					txtAmountReceivedFromDealer.setEnabled(false);
				}
				/*if (bankDepositVO.getTotalAmount() > 0) {
					txtAmountReceivedFromDealer.setEnabled(true);
				} else {
					txtAmountReceivedFromDealer.setEnabled(false);
				}*/
				txtAmountReceivedFromDealer.setImmediate(true);
				txtAmountReceivedFromDealer.setValue(String.valueOf(bankDepositVO.getAmountReceivedFromDealerUSD()));			
				checkboxIndex.setPayments(paymentsBySearch);
				checkboxIndexs.add(checkboxIndex);
				final double totalAmount = totalRemainingBalance;//bankDepositVO.getTotalAmount();
				txtAmountReceivedFromDealer.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2120119835501936565L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						
						BankDepositVO bankDepositVO = new BankDepositVO();
					
						bankDepositVO.setRequestDate(DateUtils.getDateAtBeginningOfDay((Date) item.getItemProperty("date").getValue()));
						bankDepositVO.setPayments(paymentBankDeposit);
						bankDepositVO.setDealer(dealer);
						double amountReceived = Double.parseDouble(String.valueOf((String) event.getProperty().getValue()));
						bankDepositVO.setAmountReceivedFromDealerUSD(amountReceived);
						bankDepositVO.setTotalAmount(totalAmount);
						amountReceivedFromDealers.put((Integer) item.getItemProperty("index").getValue(), bankDepositVO);
					}
				});
				
				item.getItemProperty("index").setValue(index);
				item.getItemProperty(ID).setValue(dealer.getId());
				item.getItemProperty("date").setValue(date);
				item.getItemProperty("num.installment").setValue(bankDepositVO.getNumInstallment());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(dealer.getNameEn());
				item.getItemProperty("installment.amount").setValue(bankDepositVO.getInstallmentAmount());
				item.getItemProperty("total.other.amount").setValue(bankDepositVO.getTotalOtherAmount());
				item.getItemProperty("total.penalty").setValue(bankDepositVO.getTotalPenalty());
				item.getItemProperty("total.amount").setValue(bankDepositVO.getTotalAmount());
				item.getItemProperty("cbxCheck").setValue(cbxCheck);
				item.getItemProperty("cbxDeposited").setValue(cbxDeposited);
				
				item.getItemProperty("remaining.balance").setValue(totalRemainingBalance);
				item.getItemProperty("request.deposit.on").setValue(dfRequestDepositOn);
				item.getItemProperty("amount.received.from.dealer").setValue(txtAmountReceivedFromDealer);
				index++;
				List<BankDepositChecked> list = BankDepositCheck.getBankDepositChecked(dealer, date, date);
				if (list != null && !list.isEmpty()) {
					bankDepositCheckeds.add(list.get(0));
					dateCheckeds.add(list.get(0).getCheckedDate());
					dealerChecks.add(list.get(0).getDealer().getId());
				}
			}
		}
		
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * 
	 * @param dateCheckeds
	 * @param dealerChecks
	 */
	private void getUpdateBankDespositChecked(List<Date> dateCheckeds,
			List<Long> dealerChecks) {
		if (dateCheckeds != null && !dateCheckeds.isEmpty()) {
			for (int i = 0; i < dateCheckeds.size(); i++) {
				Dealer dealer = ENTITY_SRV.getById(Dealer.class, dealerChecks.get(i));
				List<BankDepositChecked> bankDepositCheckeds = BankDepositCheck.getBankDepositChecked(
						dealer, dateCheckeds.get(i),
						dateCheckeds.get(i));
				if (bankDepositCheckeds == null || bankDepositCheckeds.isEmpty()) {
					BankDepositChecked bankDepositChecked = new BankDepositChecked();
					bankDepositChecked.setCheckedDate(dateCheckeds.get(i));
					bankDepositChecked.setDealer(dealer);
					ENTITY_SRV.saveOrUpdate(bankDepositChecked);
				}
			}
			
		}
	}
	/**
	 * 
	 * @param bankDepositCheckeds
	 * @param dateCheckeds
	 * @param dealerChecks
	 */
	private void getRemovedBankDepositUnChecked(
			List<BankDepositChecked> bankDepositCheckeds,
			List<Date> dateCheckeds, List<Long> dealerChecks) {
		if (bankDepositCheckeds != null && !bankDepositCheckeds.isEmpty()) {
			boolean isRemove = false;
			for (BankDepositChecked bankDepositChecked : bankDepositCheckeds) {
				if (dateCheckeds != null && !dateCheckeds.isEmpty()) {
					for (int i = 0; i < dateCheckeds.size(); i++) {
						Dealer dealer = ENTITY_SRV.getById(Dealer.class, dealerChecks.get(i));
						if (bankDepositChecked.getDealer() == dealer
								&& bankDepositChecked.getCheckedDate() == dateCheckeds.get(i)) {
							isRemove = false;
							break;
						} else {
							isRemove = true;
						}
					}
				} else {
					isRemove = true;
				}
				if (isRemove) {
					ENTITY_SRV.delete(bankDepositChecked);
				}
			}
		}
	}
	
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("pass.to.dealer.date"), Date.class, Align.LEFT, 115));
		columnDefinitions.add(new ColumnDefinition("num.installment", I18N.message("num.installment"), Integer.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Double.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("total.other.amount", I18N.message("other.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.penalty", I18N.message("total.penalty"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("cbxCheck", I18N.message("check"), CheckBox.class, Align.CENTER, 60));
		columnDefinitions.add(new ColumnDefinition("cbxDeposited", I18N.message("deposited"), CheckBox.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("remaining.balance", I18N.message("remaining.balance"), Double.class, Align.RIGHT, 125));
		columnDefinitions.add(new ColumnDefinition("request.deposit.on", I18N.message("request.deposit.on"), AutoDateField.class, Align.CENTER, 125));
		columnDefinitions.add(new ColumnDefinition("amount.received.from.dealer", I18N.message("amount.received.from.dealer"), TextField.class, Align.CENTER, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param item
	 * @param dealer
	 * @param isChecked
	 *//*
	private void refreshBankDepositTable(Item item, Dealer dealer, boolean isChecked, Date date, Date requestBankdepositOn) {
		DealerCheckBox dealerCheckbox = getCheckBoxByDealer(dealer);
		if (dealerCheckbox != null) {
			List<CheckboxIndex> lsDealerCheckbox = dealerCheckbox.getCheckboxBankDeposits();
			if (lsDealerCheckbox != null && !lsDealerCheckbox.isEmpty()) {
				double totalRemainingBanlanceOld = (double) item.getItemProperty("remaining.balance").getValue();
				double totalRemainingBanlance = 0d;
				for (CheckboxIndex checkboxIndex : lsDealerCheckbox) {
					if (isChecked && checkboxIndex.getIndex() > (int) item.getItemProperty("index").getValue()) {
						final Item itemByIndex = indexedContainer.getItem(checkboxIndex.getIndex());
						totalRemainingBanlance += (double) itemByIndex.getItemProperty("total.amount").getValue();
						itemByIndex.getItemProperty("remaining.balance").setValue(totalRemainingBanlance);
					}
					if (!isChecked && checkboxIndex.getIndex() > (int) item.getItemProperty("index").getValue()) {
						final Item itemByIndex = indexedContainer.getItem(checkboxIndex.getIndex());
						totalRemainingBanlanceOld += (double) itemByIndex.getItemProperty("total.amount").getValue();
						itemByIndex.getItemProperty("remaining.balance").setValue(totalRemainingBanlanceOld);
					}
					if(isChecked && checkboxIndex.getIndex() <= (int) item.getItemProperty("index").getValue()) {
						if (checkboxIndex.getPayments() != null
								&& !checkboxIndex.getPayments().isEmpty()) {
							if (requestBankdepositOn != null) {
								paymentBankDeposit = new ArrayList<Payment>();
							}
							for (Payment payment : checkboxIndex.getPayments()) {
								payment.setDealerPaymentDate(date);
								if (requestBankdepositOn != null) {
									paymentBankDeposit.add(payment);
								}
							}
							if (requestBankdepositOn == null) {
								listIndex.add((Integer) item.getItemProperty("index").getValue());
							} else {
								BankDeposit bankDeposit = new BankDeposit();
								bankDeposit.setRequestDate(DateUtils.getDateAtBeginningOfDay((Date) item.getItemProperty("date").getValue()));
								bankDeposit.setRequestDepositDate(requestBankdepositOn);
								bankDeposit.setPayments(paymentBankDeposit);
								bankDeposit.setDealer(paymentBankDeposit.get(0).getDealer());
								bankDeposits.put((Integer) item.getItemProperty("index").getValue(), bankDeposit);
							}
						}
						
					}
					if(!isChecked && checkboxIndex.getIndex() <= (int) item.getItemProperty("index").getValue()) {
						if (checkboxIndex.getPayments() != null
								&& !checkboxIndex.getPayments().isEmpty()) {
							if (requestBankdepositOn == null) {
								listIndex.remove((Integer) item.getItemProperty("index").getValue());
								}
							if(bankDeposits.containsKey((Integer) item.getItemProperty("index").getValue())){
								bankDeposits.remove((Integer) item.getItemProperty("index").getValue());
							}	
						}
						
					}
				}
			}
		}
	}*/
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return totalRemaining
	 *//*
	private double getRemainingBalance(Dealer dealer, Date date) {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, PaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentStatus.PAI));
		restrictions.addCriterion(Restrictions.isNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		// restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(date)));
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(date)));
		List<Payment> payments = ENTITY_SRV.list(restrictions);
		double installmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double totalRemaining = 0d;
		List<Payment> paymentsByDealer = new ArrayList<Payment>();
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				List<Cashflow> cashflows = payment.getCashflows();
				if (cashflows != null && !cashflows.isEmpty()) {
					for (Cashflow cashflow : cashflows) {
						if (cashflow.getCashflowType() == CashflowType.PER) {
							totalPenalty += cashflow.getTiInstallmentAmount();
						} else if (cashflow.getCashflowType() == CashflowType.CAP || cashflow.getCashflowType() == CashflowType.IAP) {
							installmentAmount += cashflow.getTiInstallmentAmount();
						} else {
							totalOtherAmount += cashflow.getTiInstallmentAmount();
						}
					}
				}
				paymentsByDealer.add(payment);
			}
		}
		totalRemaining += installmentAmount + totalOtherAmount + totalPenalty;
		return totalRemaining;
	}*/

	/**
	 * 
	 * @param dealer
	 * @param item
	 */
	private void setVisibleCheckboxDealer(Dealer dealer, Item item, boolean isVisible) {
		DealerCheckBox dealerCheckbox = getCheckBoxByDealer(dealer);
		if (dealerCheckbox != null) {
			List<CheckboxIndex> lsDealerCheckbox = dealerCheckbox.getCheckboxBankDeposits();
			if (lsDealerCheckbox != null && !lsDealerCheckbox.isEmpty()) {
				for (CheckboxIndex checkboxIndex : lsDealerCheckbox) {
					int index = Integer.parseInt(item.getItemProperty("index").getValue().toString());
					if (checkboxIndex.getCbCheck().getValue()) {
						checkboxIndex.getCbCheck().setEnabled(false);
					} else {
						checkboxIndex.getCbCheck().setEnabled(isVisible);
					}
					if (checkboxIndex.getIndex() == index){
						checkboxIndex.getCbCheck().setEnabled(true);
					}
					
				}	
			}
		}
	}

	private void setEnableAutoDateField(Item item, boolean isVisible) {
	 	int dfRequestDepositOnIndex = 0;
		if (dfRequestDepositOns != null && !dfRequestDepositOns.isEmpty()) {
			for (AutoDateField dfRequestDepositOn : dfRequestDepositOns) {
				int index = Integer.parseInt(item.getItemProperty("index").getValue().toString());
				if (dfRequestDepositOnIndex == index){
					if (!isVisible) {
						dfRequestDepositOn.setValue(null);
					}
					dfRequestDepositOn.setEnabled(isVisible);
					break;
				}
				dfRequestDepositOnIndex++;
			}	
		}
	}
	/**
	 * 
	 * @param dealer
	 * @return list checkbox by dealer
	 */
	private DealerCheckBox getCheckBoxByDealer(Dealer dealer) {
		
		if (dealerCheckboxs != null && !dealerCheckboxs.isEmpty()) {
			for (DealerCheckBox dealerCheckBox : dealerCheckboxs) {
				if (dealerCheckBox.getDealer() == dealer) {
					return dealerCheckBox;
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

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	public TabSheet getTabSheet() {
		return this.tabSheet;
	}
	
	/**
	 * 
	 * @param txtField
	 * @return
	 */
	private String getDefaultString(TextField txtField) {
		String value = "0";
		if (!"".equals(txtField.getValue())) {
			value = txtField.getValue();
		}
		return value;
	}
	
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return
	 */
	private BankDepositVO getBankDepositVO(Dealer dealer, Date date) {
		return getBankDepositVO(dealer, date, date);
	}
	
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return bankDeposit
	 */
	private BankDepositVO getBankDepositVO(Dealer dealer, Date startDate, Date endDate) {

		BaseRestrictions<Payment> restrictions = new BaseRestrictions<>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		if (startDate != null) {
			restrictions.addCriterion(Restrictions.ge("passToDealerPaymentDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(endDate)));
		List<Payment> payments = ENTITY_SRV.list(restrictions);
		int numInstallment = 0;
		double installmentAmount = 0d;
		double totalOtherAmount = 0d;
		double totalPenalty = 0d;
		double totalAmount = 0d;
		List<Payment> paymentsByDealer = new ArrayList<Payment>();
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
		BankDepositVO bankDeposit = new BankDepositVO();
		bankDeposit.setNumInstallment(numInstallment);
		bankDeposit.setInstallmentAmount(installmentAmount);
		bankDeposit.setTotalOtherAmount(totalOtherAmount);
		bankDeposit.setTotalPenalty(totalPenalty);
		bankDeposit.setTotalAmount(totalAmount);
		bankDeposit.setPayments(paymentsByDealer);
		
		double amountReceivedFromDealerUSD = getBankDepositAmountReceivedFromDealer(dealer, startDate, endDate);
		double remainingAmount = totalAmount - amountReceivedFromDealerUSD;
		
		bankDeposit.setAmountReceivedFromDealerUSD(amountReceivedFromDealerUSD);
		bankDeposit.setRemainingAmount(remainingAmount);
		
		return bankDeposit;
	}
	
	/**
	 * 
	 * @param dealer
	 * @param date
	 * @return
	 */
	private double getBankDepositAmountReceivedFromDealer(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<BankDepositReceivedFromDealer> restrictions = new BaseRestrictions<>(BankDepositReceivedFromDealer.class);
		
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		if (startDate != null) {
			restrictions.addCriterion(Restrictions.ge("passToDealerPaymentDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(endDate)));
		List<BankDepositReceivedFromDealer> bankDepositReceivedFromDealers = ENTITY_SRV.list(restrictions);
		double amountReceivedFromDealer = 0.0;
		for (BankDepositReceivedFromDealer bankDepositReceivedFromDealer :  bankDepositReceivedFromDealers) {
			amountReceivedFromDealer += bankDepositReceivedFromDealer.getAmountReceivedFromDealerUSD();
		}
		
		return amountReceivedFromDealer;
	}
	
	/**
	 * 
	 * @author sok.vina
	 *
	 */
	private class DealerCheckBox {
		private Dealer dealer;
		private List<CheckboxIndex> checkboxBankDeposits;
		
		/**
		 * @return the dealer
		 */
		public Dealer getDealer() {
			return dealer;
		}
		/**
		 * @param dealer the dealer to set
		 */
		public void setDealer(Dealer dealer) {
			this.dealer = dealer;
		}
		/**
		 * @return the checkboxBankDeposit
		 */
		public List<CheckboxIndex> getCheckboxBankDeposits() {
			return checkboxBankDeposits;
		}
		/**
		 * @param checkboxBankDeposit the checkboxBankDeposit to set
		 */
		public void setCheckboxBankDeposits(List<CheckboxIndex> checkboxBankDeposits) {
			this.checkboxBankDeposits = checkboxBankDeposits;
		}
	}
	/**
	 * 
	 * @author sok.vina
	 *
	 */
	private class CheckboxIndex {
		private List<Payment> payments;
		private CheckBox cbCheck;
		private int index;
		/**
		 * @return the cbCheck
		 */
		public CheckBox getCbCheck() {
			return cbCheck;
		}
		/**
		 * @param cbCheck the cbCheck to set
		 */
		public void setCbCheck(CheckBox cbCheck) {
			this.cbCheck = cbCheck;
		}
		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
		/**
		 * @param index the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}
		/**
		 * @return the payments
		 */
		public List<Payment> getPayments() {
			return payments;
		}
		/**
		 * @param payments the payments to set
		 */
		public void setPayments(List<Payment> payments) {
			this.payments = payments;
		}
	}
}
