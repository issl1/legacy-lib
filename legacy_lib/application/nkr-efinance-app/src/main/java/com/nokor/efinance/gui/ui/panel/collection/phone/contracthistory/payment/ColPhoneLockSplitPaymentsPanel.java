package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.collection.service.ContractOperationRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.service.cashflow.CashflowRestriction;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.JournalEventComboBox;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class ColPhoneLockSplitPaymentsPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2285193621893542136L;
	
	private static final String OPEN_TABLE = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:1px solid black; border-collapse:collapse;\" >";
	private static final String OPEN_TR = "<tr>";
	private static final String OPEN_TH = "<th class=\"align-center\" width=\"150px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static final String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
	private static final String CLOSE_TR = "</tr>";
	private static final String CLOSE_TH = "</th>";
	private static final String CLOSE_TD = "</td>";
	private static final String CLOSE_TABLE = "</table>";
	
	private AutoDateField dfPaymentDateFrom;
	private AutoDateField dfPaymentDateTo;
	private TextArea txtRemark;
	private ERefDataComboBox<EPaymentChannel> cbxChannel;
	private EntityComboBox<Dealer> cbxDealerShipName;
	
	private Button btnSave;
	private Button btnReset;
	private Button btnAdd;
	
	private ColPhoneLockSplitPaymentsTablePanel lockSplitsTablePanel;
	private Contract contract;
	
	private List<ERefDataComboBox<ELockSplitCategory>> cbxLockSplitCategories;
	private List<JournalEventComboBox> cbxLockSplitTypes;
	private List<ComboBox> cbxOperations;
	private List<TextField> txtAmounts;
	private List<TextField> txtBalances;
	
	private CustomLayout customLayout;
	private VerticalLayout locksplitItemLayout;
	
	private double totalAmountOfPayment;
	private TextField txtTotalAmount;
	
	private double totalBalanceOfPayment;
	private TextField txtTotalBalance;
	
	private LockSplit lockSplit;
	
	private Label lblDealerTitle;
	
	private ColGroupLockSplitTypeTablePanel colGroupLockSplitTypeTablePanel;
	
	/**
	 * 
	 */
	public ColPhoneLockSplitPaymentsPanel() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		dfPaymentDateFrom = ComponentFactory.getAutoDateField();
		dfPaymentDateFrom.setValue(DateUtils.today());
		
		dfPaymentDateTo = ComponentFactory.getAutoDateField();
		txtRemark = ComponentFactory.getTextArea(false, 150, 50);
		
		dfPaymentDateFrom.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 222535772791606942L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfPaymentDateFrom.getValue() != null) {
					Date paymentDateFromSelected = DateUtils.getDateAtBeginningOfDay(dfPaymentDateFrom.getValue());
					Date maxPaymentDateFrom = DateUtils.getDateAtBeginningOfDay(DateUtils.addMonthsDate(DateUtils.today(), 1));
					if (paymentDateFromSelected.after(maxPaymentDateFrom)) {
						Notification notification = ComponentLayoutFactory.getNotification(Type.ERROR_MESSAGE);
						notification.setDescription(I18N.message("payment.date.from.max.1month", 
								new String[]{ DateUtils.getDateLabel(maxPaymentDateFrom, DateUtils.FORMAT_DDMMYYYY_SLASH) }));
						dfPaymentDateFrom.setValue(maxPaymentDateFrom);
					} else {
						dfPaymentDateTo.setValue(DateUtils.addMonthsDate(dfPaymentDateFrom.getValue(), 1));
					}
				} else {
					dfPaymentDateTo.setValue(null);
				}
			}
		});
		
		dfPaymentDateTo.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 2482106495282808382L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfPaymentDateTo.getValue() != null) {
					Date paymentDateToSelected = DateUtils.getDateAtBeginningOfDay(dfPaymentDateTo.getValue());
					Date maxPaymentDateTo = DateUtils.getDateAtBeginningOfDay(DateUtils.addMonthsDate(DateUtils.today(), 2));
					if (paymentDateToSelected.after(maxPaymentDateTo)) {
						Notification notification = ComponentLayoutFactory.getNotification(Type.ERROR_MESSAGE);
						notification.setDescription(I18N.message("payment.date.to.max.2month", 
								new String[]{ DateUtils.getDateLabel(maxPaymentDateTo, DateUtils.FORMAT_DDMMYYYY_SLASH) }));
						dfPaymentDateTo.setValue(maxPaymentDateTo);
					}
				}
			}
		});
		
		cbxLockSplitCategories = new ArrayList<>();
		cbxOperations = new ArrayList<>();
		cbxLockSplitTypes = new ArrayList<>();
		txtAmounts = new ArrayList<>();
		txtBalances = new ArrayList<>();
		
		List<EPaymentChannel> channels = EPaymentChannel.getValues(EPaymentChannel.class);
		cbxChannel = new ERefDataComboBox<EPaymentChannel>(channels);
		cbxChannel.setWidth(150, Unit.PIXELS);
		
		cbxDealerShipName = new EntityComboBox<Dealer>(Dealer.class, Dealer.NAMELOCALE);
		cbxDealerShipName.setWidth(150, Unit.PIXELS);
		cbxDealerShipName.renderer();
		
		cbxChannel.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 222535772791606942L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxChannel.getSelectedEntity() != null) {
					if (EPaymentChannel.DEALERCOUNTER.getCode().equals(cbxChannel.getSelectedEntity().getCode())) {
						showDealerShipData();
					} else {
						cbxDealerShipName.setSelectedEntity(null);
						setVisibledControl(false);
					}
				} else {
					cbxDealerShipName.setSelectedEntity(null);
					setVisibledControl(false);
				}
			}
		});
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		btnReset = ComponentLayoutFactory.getDefaultButton("reset", FontAwesome.ERASER, 60);
		btnReset.addClickListener(this);
		
		btnAdd = ComponentLayoutFactory.getButtonIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		
		txtTotalAmount = ComponentLayoutFactory.getTextFieldAlignRight(50, 120);
		txtTotalAmount.setEnabled(false);
		
		txtTotalBalance = ComponentLayoutFactory.getTextFieldAlignRight(50, 120);
		txtTotalBalance.setEnabled(false);
		
		lockSplitsTablePanel = new ColPhoneLockSplitPaymentsTablePanel(this);
		
		locksplitItemLayout = new VerticalLayout();
		locksplitItemLayout.setSizeUndefined();
		
		colGroupLockSplitTypeTablePanel = new ColGroupLockSplitTypeTablePanel();
		
		setMargin(true);
		setSpacing(true);
		addComponent(colGroupLockSplitTypeTablePanel);
		addComponent(getTopLayout());
		addComponent(locksplitItemLayout);
		addComponent(lockSplitsTablePanel);
		setComponentAlignment(locksplitItemLayout, Alignment.TOP_CENTER);
		
		setVisibledControl(false);
	}
	
	/**
	 * 
	 */
	private void showDealerShipData() {
		if (this.lockSplit != null) {
			Contract contract = this.lockSplit.getContract();
			Dealer dealer = null;
			if (contract != null) {
				if (EPaymentChannel.DEALERCOUNTER.getCode().equals(cbxChannel.getSelectedEntity().getCode())) {
					dealer = contract.getDealer();
				}	 			
			}
			cbxDealerShipName.setSelectedEntity(dealer);
			setVisibledControl(true);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getTopLayout() {
		Label lblChannelTitle = ComponentLayoutFactory.getLabelCaption("channel");
		lblDealerTitle = ComponentLayoutFactory.getLabelCaption("dealer.ship.name");
		Label lblDueDateFromTitle = ComponentLayoutFactory.getLabelCaptionRequired("payment.date.from");
		Label lblDueDateToTitle = ComponentLayoutFactory.getLabelCaptionRequired("payment.date.to");
		Label lblRemarkTitle = ComponentLayoutFactory.getLabelCaption("remark");
		
		GridLayout gridLayout = new GridLayout(15, 2);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblDueDateFromTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfPaymentDateFrom, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDateToTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfPaymentDateTo, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblChannelTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxChannel, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblRemarkTitle, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(txtRemark, iCol++, 1);
		iCol = 7;
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblDealerTitle, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(cbxDealerShipName, iCol++, 1);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verLayout.addComponent(gridLayout);
		
		return verLayout;
	}
	
	/**
	 * 
	 */
	private void lockSplitItemClear() {
		cbxLockSplitCategories.clear();
		cbxOperations.clear();
		cbxLockSplitTypes.clear();
		txtAmounts.clear();
		txtBalances.clear();
	}
	
	/**
	 * 
	 * @param visible
	 */
	private void setVisibledControl(boolean visible) {
		cbxDealerShipName.setVisible(visible);
		lblDealerTitle.setVisible(visible);
	}
	
	/**
	 * Build Lock Split Item Table
	 * @param lockSplitItems
	 */
	private void buildLockSplitItemTable(List<LockSplitItem> lockSplitItems){
		lockSplitItemClear();
		
		customLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TH;
		template += "<div location =\"lblCategory\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblItem\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblBalance\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblAmount\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"btnAdd\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
	
		totalAmountOfPayment = 0d;
		totalBalanceOfPayment = 0d;
		boolean isItemHasId = false;
		int i = 0;
		for (LockSplitItem item : lockSplitItems) {
			if (i == 0 && item.getId() != null) {
				isItemHasId = true;
			}
			template += OPEN_TR;
			template += OPEN_TD;
			template += "<div location =\"cbxCategory" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"item" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"txtBalance" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"txtAmount" + i + "\" />";
			template += CLOSE_TD;
			template += OPEN_TD;
			template += "<div location =\"btnDelete" + i + "\" />";
			template += CLOSE_TD;
			template += CLOSE_TR;
			
			ERefDataComboBox<ELockSplitCategory> cbxLockSplitCategory = new ERefDataComboBox<ELockSplitCategory>(ELockSplitCategory.values());
			cbxLockSplitCategory.setWidth(150, Unit.PIXELS);
			cbxLockSplitCategory.setImmediate(true);
			cbxLockSplitCategory.setNullSelectionAllowed(false);
			cbxLockSplitCategory.setSelectedEntity(item.getLockSplitCategory() == null ? ELockSplitCategory.DUE : item.getLockSplitCategory());
			cbxLockSplitCategories.add(cbxLockSplitCategory);
			
			JournalEventRestriction journalEventRestriction = new JournalEventRestriction();
			journalEventRestriction.setJournalId(JournalEvent.RECEIPTS);
			List<JournalEvent> lockSplitTypes = LCK_SPL_SRV.list(journalEventRestriction);
			JournalEventComboBox cbxLockSplitType = new JournalEventComboBox(lockSplitTypes);
			cbxLockSplitType.setWidth(150, Unit.PIXELS);
			cbxLockSplitType.setImmediate(true);
			cbxLockSplitType.setSelectedEntity(item.getJournalEvent());
			cbxLockSplitTypes.add(cbxLockSplitType);
			
			ContractOperationRestriction restrictions = new ContractOperationRestriction();
			restrictions.setConId(contract.getId());
			List<ContractOperation> conOperations = ENTITY_SRV.list(restrictions);
			
			ComboBox cbxOperation = ComponentFactory.getComboBox();
			if (conOperations != null && !conOperations.isEmpty()) {
				for (ContractOperation conOperation : conOperations) {
					String operationType = conOperation.getOperationType() == null ? StringUtils.EMPTY : conOperation.getOperationType().getDescLocale();
					cbxOperation.addItem(conOperation.getId() + " - " + operationType);
				}
			}
			cbxOperation.setWidth(150, Unit.PIXELS);
			cbxOperation.setImmediate(true);
			
			String operationDesc = null;
			if (item.getOperation() != null) {
				String desc = item.getOperation().getOperationType() != null ? item.getOperation().getOperationType().getDescLocale() 
						: StringUtils.EMPTY; 
				operationDesc = item.getOperation().getId() + " - " + desc;
			}
			
			cbxOperation.setValue(operationDesc);
			cbxOperations.add(cbxOperation);
			
			TextField txtBalance = ComponentLayoutFactory.getTextFieldAlignRight(20, 120);
			txtBalance.setEnabled(false);
			txtBalance.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = 1341936676292282576L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					totalBalanceOfPayment = 0d;
					for (int j = 0; j < txtBalances.size(); j++) {
						totalBalanceOfPayment += MyNumberUtils.getDouble(txtBalances.get(j).getValue(), 0d);
						txtTotalBalance.setValue(MyNumberUtils.formatDoubleToString(totalBalanceOfPayment, "##0.00"));
					}
				}
			});
			txtBalances.add(txtBalance);
			TextField txtAmount = ComponentLayoutFactory.getTextFieldAlignRight(20, 120);
			txtAmount.setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(item.getTiAmount()), "##0.00"));
			txtAmount.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = 4866884364725964205L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					double balance = MyNumberUtils.getDouble(getDouble(txtBalance));
					double amount = MyNumberUtils.getDouble(getDouble(txtAmount));
					if (balance < amount) {
						ComponentLayoutFactory.displayErrorMsg("amount.cannot.higher.than.balance");
						txtAmount.setValue(MyNumberUtils.formatDoubleToString(balance, "##0.00"));
					}
					if (checkAmountValue(txtAmount)) {
						totalAmountOfPayment = 0d;
						for (int j = 0; j < txtAmounts.size(); j++) {
							totalAmountOfPayment += MyNumberUtils.getDouble(txtAmounts.get(j).getValue(), 0d);
							txtTotalAmount.setValue(MyNumberUtils.formatDoubleToString(totalAmountOfPayment, "##0.00"));
						}
					} else {
						ComponentLayoutFactory.displayErrorMsg(errors.get(0).toString());
						txtAmount.focus();
						txtAmount.setValue(MyNumberUtils.formatDoubleToString(0d, "##0.00"));
					}
				}
			});
			txtAmounts.add(txtAmount);
			
			cbxLockSplitType.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = 2482106495282808382L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					assignValueToBalance(txtBalance, cbxLockSplitType);
				}
			});
			if (cbxLockSplitType.getSelectedEntity() != null) {
				assignValueToBalance(txtBalance, cbxLockSplitType);
			}
			Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TIMES);
			btnDelete.addClickListener(this);
			btnDelete.addClickListener(new DeleteLockSplitItem(i));
			
			int index = i;
			customLayout.addComponent(cbxLockSplitCategory, "cbxCategory" + i);
			cbxLockSplitCategory.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = -210879127611072438L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (ELockSplitCategory.DUE.equals(cbxLockSplitCategory.getSelectedEntity())) {
						customLayout.addComponent(cbxLockSplitType, "item" + index);
					} else {
						customLayout.addComponent(cbxOperation, "item" + index);
					}
				}
			});
			if (ELockSplitCategory.DUE.equals(cbxLockSplitCategory.getSelectedEntity())) {
				customLayout.addComponent(cbxLockSplitType, "item" + i);
			} else {
				customLayout.addComponent(cbxOperation, "item" + i);
			}
			customLayout.addComponent(txtBalance, "txtBalance" + i);
			customLayout.addComponent(txtAmount, "txtAmount" + i);
			customLayout.addComponent(btnDelete, "btnDelete" + i);
			i++;
		}
		
		for (int j = 0; j < txtAmounts.size(); j++) {
			totalAmountOfPayment += MyNumberUtils.getDouble(txtAmounts.get(j).getValue(), 0d);
			txtTotalAmount.setValue(MyNumberUtils.formatDoubleToString(totalAmountOfPayment, "##0.00"));
		}
		
		if (txtAmounts.size() == 0) {
			txtTotalAmount.setValue(MyNumberUtils.formatDoubleToString(0d));
		}
		
		if (!isItemHasId) {
			for (int j = 0; j < txtBalances.size(); j++) {
				totalBalanceOfPayment += MyNumberUtils.getDouble(txtBalances.get(j).getValue(), 0d);
				txtTotalBalance.setValue(MyNumberUtils.formatDoubleToString(totalBalanceOfPayment, "##0.00"));
			}
		}
		
		if (txtBalances.size() == 0) {
			txtTotalBalance.setValue(MyNumberUtils.formatDoubleToString(0d));
		}
		
		template += OPEN_TR;
		template += "<td></td>";
		template += "<td class=\"align-right\" style=\"border-right:1px solid black\" >";
		template += "<div location =\"lblTotalAmount\" />";
		template += CLOSE_TD;
		template += "<td class=\"align-center\" style=\"border-right:1px solid black\" >";
		template += "<div location =\"txtTotalBalance\" />";
		template += CLOSE_TD;
		template += "<td class=\"align-center\" style=\"border-right:1px solid black\" >";
		template += "<div location =\"txtTotalAmount\" />";
		template += CLOSE_TD;
		template += "<td align=\"center\" >";
		template += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >";
		template += "<td>";
		template += "<div location =\"btnSave\" />";
		template += CLOSE_TD;
		template += "<td width=\"5\">&nbsp;</td>";
		template += "<td>";
		template += "<div location =\"btnReset\" />";
		template += CLOSE_TD;
		template += "</table>";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		customLayout.addComponent(ComponentFactory.getLabel("category"), "lblCategory");
		customLayout.addComponent(ComponentFactory.getLabel("item"), "lblItem");
		customLayout.addComponent(ComponentFactory.getLabel("balance"), "lblBalance");
		customLayout.addComponent(ComponentFactory.getLabel("amount.in.vat"), "lblAmount");
		customLayout.addComponent(btnAdd, "btnAdd");
		customLayout.addComponent(ComponentFactory.getLabel("total.amount"), "lblTotalAmount");
		customLayout.addComponent(txtTotalBalance, "txtTotalBalance");
		customLayout.addComponent(txtTotalAmount, "txtTotalAmount");
		customLayout.addComponent(btnSave, "btnSave");
		customLayout.addComponent(btnReset, "btnReset");
		
		template += CLOSE_TABLE;
		customLayout.setTemplateContents(template);
		locksplitItemLayout.addComponent(customLayout);
	}
	
	/**
	 * 
	 * @param txtBalance
	 * @param cbxLockSplitType
	 */
	private void assignValueToBalance(TextField txtBalance, JournalEventComboBox cbxLockSplitType) {
		if (cbxLockSplitType.getSelectedEntity() != null) {
			
			JournalEvent journalEvent = ENTITY_SRV.getByCode(JournalEvent.class, cbxLockSplitType.getSelectedEntity().getCode());

			CashflowRestriction cflwRestriction = new CashflowRestriction();
			cflwRestriction.setContractId(contract.getId());
			cflwRestriction.setExcludeCancel(true);
			cflwRestriction.setExcludePaid(true);
			cflwRestriction.setJournalEventId(journalEvent == null ? null : journalEvent.getId());
			cflwRestriction.addOrder(Order.asc(Cashflow.NUMINSTALLMENT));
			List<Cashflow> cashflows = ENTITY_SRV.list(cflwRestriction);
			double balance = 0d;
			if (!cashflows.isEmpty()) {
				for (Cashflow cflw : cashflows) {
					balance += MyNumberUtils.getDouble(cflw.getTiInstallmentAmount());
				}
				txtBalance.setValue(MyNumberUtils.formatDoubleToString(balance, "##0.00"));
			} else {
				txtBalance.setValue(AmountUtils.format(0d));
			}
		} else {
			txtBalance.setValue(AmountUtils.format(0d));
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.lockSplit = LockSplit.createInstance();
		this.lockSplit.setContract(contract);
		
		this.contract = contract;
		
		if (ProfileUtil.isCollection() || ProfileUtil.isCallCenter()) {
			colGroupLockSplitTypeTablePanel.assignValues(contract);
			colGroupLockSplitTypeTablePanel.setVisible(true);
		} else {
			colGroupLockSplitTypeTablePanel.setVisible(false);
		}
		
		lockSplitsTablePanel.assignValues(getLockSplits());
		List<LockSplitItem> lockSplitItems = new ArrayList<LockSplitItem>();
		buildLockSplitItemTable(lockSplitItems);
	}

	/**
	 * 
	 * @param lckSplt
	 */
	public void assignToControls(LockSplit lckSplt) {
		this.lockSplit = lckSplt;
		dfPaymentDateFrom.setValue(lckSplt.getFrom() == null ? DateUtils.today() : lckSplt.getFrom());
		dfPaymentDateTo.setValue(lckSplt.getTo());
		txtRemark.setValue(lckSplt.getComment());
		cbxChannel.setSelectedEntity(lckSplt.getPaymentChannel());
		cbxDealerShipName.setSelectedEntity(lckSplt.getDealer());
		locksplitItemLayout.removeAllComponents();
		List<LockSplitItem> items = lckSplt.getItems();
		if (items == null) {
			items = new ArrayList<LockSplitItem>();
		}
		buildLockSplitItemTable(items);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			addLockSplitItem();
		} else if (event.getButton().equals(btnSave)) {
			if (StringUtils.isEmpty(validatedDate())) {
				saveLockSplit();
			} else {
				ComponentLayoutFactory.displayErrorMsg(validatedDate());
			}
		} else if (event.getButton().equals(btnReset)) {
			assignValues(this.contract);
		}
	}
	
	/**
	 * Save Lock Split
	 */
	private void saveLockSplit() {
		this.lockSplit.setFrom(dfPaymentDateFrom.getValue());
		this.lockSplit.setTo(dfPaymentDateTo.getValue());
		this.lockSplit.setTotalAmount(MyNumberUtils.getDouble(txtTotalAmount.getValue(), 0d));
		this.lockSplit.setPaymentChannel(cbxChannel.getSelectedEntity());
		this.lockSplit.setDealer(cbxDealerShipName.getSelectedEntity());
		this.lockSplit.setComment(txtRemark.getValue());
		getLockSplitItemValue(this.lockSplit);
		try {
			LCK_SPL_SRV.saveLockSplit(this.lockSplit);
			displayMsg();
			lockSplitsTablePanel.assignValues(getLockSplits());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/***
	 * 
	 */
	private void displayMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
		assignValues(this.contract);
	}
	
	/**
	 * Add Lock Split Item
	 */
	private void addLockSplitItem() {
		locksplitItemLayout.removeComponent(customLayout);
		getLockSplitItemValue(this.lockSplit);
		
		LockSplitItem lockSplitItem = LockSplitItem.createInstance();
		lockSplitItem.setLockSplit(this.lockSplit);
		
		List<LockSplitItem> lockSplitItems = this.lockSplit.getItems();
		if (lockSplitItems == null) {
			lockSplitItems = new ArrayList<LockSplitItem>();
		}
		lockSplitItems.add(lockSplitItem);
		
		this.lockSplit.setItems(lockSplitItems);
		buildLockSplitItemTable(this.lockSplit.getItems());
	}
	
	/**
	 * 
	 */
	public void reset() {
		locksplitItemLayout.removeAllComponents();
		dfPaymentDateFrom.setValue(DateUtils.today());
		txtRemark.setValue(StringUtils.EMPTY);
		cbxChannel.setSelectedEntity(null);
		cbxDealerShipName.setSelectedEntity(null);
		lockSplitsTablePanel.removeAllItems();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<LockSplit> getLockSplits() {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setContractID(this.contract.getReference());
		return LCK_SPL_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class DeleteLockSplitItem implements ClickListener {

		/** */
		private static final long serialVersionUID = -5791444064665105025L;

		private int index;
		
		public DeleteLockSplitItem(int index) {
			this.index = index;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			getLockSplitItemValue(lockSplit);
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {		

				/** */
				private static final long serialVersionUID = -1836501190967969811L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
							locksplitItemLayout.removeComponent(customLayout);
							LockSplitItem item = lockSplit.getItems().get(index);
							LCK_SPL_SRV.deleteLockSplitItem(lockSplit, item);
							lockSplit.getItems().remove(index);
							buildLockSplitItemTable(lockSplit.getItems());
							lockSplitsTablePanel.assignValues(getLockSplits());
							dialog.close();
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
			        }
				}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");	
		}
	}
	
	/**
	 * Error if date from bigger than date to
	 * @return
	 */
	private String validatedDate() {
		Date from = DateUtils.getDateAtBeginningOfDay(dfPaymentDateFrom.getValue());
		Date to = DateUtils.getDateAtBeginningOfDay(dfPaymentDateTo.getValue());
		if (from != null && to != null) {
			if (from.after(to)) {
				return I18N.message("payment.date.from.less.than.payment.date.to");
			} else {
				if (from.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
					if (this.lockSplit.getId() == null) {
						return I18N.message("payment.date.from.less.than.today", 
								new String[] {DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH)});
					}
				} else if (to.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
					if (this.lockSplit.getId() == null) {
						return I18N.message("payment.date.to.less.than.today", 
								new String[] {DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH)});
					}
				}
			} 
		} else {
			if (from == null) {
				return I18N.message("field.required.1", new String[] {I18N.message("payment.date.from")});
			} else if (to == null) {
				return I18N.message("field.required.1", new String[] {I18N.message("payment.date.to")});
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	private boolean checkAmountValue(AbstractTextField field) {
		errors.clear();
		checkDoubleField(field, "amount.in.vat");
		return errors.isEmpty();
	}
	
	/**
	 * Get Lock Split Item Value
	 * @param lockSplit
	 */
	private void getLockSplitItemValue(LockSplit lockSplit) {
		int index = 0;
		List<LockSplitItem> items = lockSplit.getItems();
		if (items == null) {
			items = new ArrayList<LockSplitItem>();
		}
		if (!items.isEmpty()) {
			for (LockSplitItem lockSplitItem : items) {
				if (lockSplitItem != null) {
					lockSplitItem.setLockSplit(lockSplit);
					lockSplitItem.setLockSplitCategory(cbxLockSplitCategories.get(index).getSelectedEntity());
					if (ELockSplitCategory.DUE.equals(lockSplitItem.getLockSplitCategory())) {
						lockSplitItem.setJournalEvent(cbxLockSplitTypes.get(index).getSelectedEntity());
						lockSplitItem.setOperation(null);
					} else {
						String splitId = String.valueOf(cbxOperations.get(index).getValue() == null ? StringUtils.EMPTY :
							cbxOperations.get(index).getValue());
						splitId = splitId.replaceAll("\\s", "");
						String[] splitIds = splitId.split("-");
						ContractOperation operation = null;
						Long id = null;
						if (splitIds[0] != null && StringUtils.isNotEmpty(splitIds[0])) {
							id = Long.parseLong(splitIds[0]);
						}	
						if (id != null) {
							operation = ENTITY_SRV.getById(ContractOperation.class, id);
						}
						lockSplitItem.setOperation(operation);
						lockSplitItem.setJournalEvent(null);
					}
					lockSplitItem.setTiAmount(MyNumberUtils.getDouble(txtAmounts.get(index).getValue(), 0d));
					index++;
				}
			}
		}
		lockSplit.setItems(items);
	}
	
}
