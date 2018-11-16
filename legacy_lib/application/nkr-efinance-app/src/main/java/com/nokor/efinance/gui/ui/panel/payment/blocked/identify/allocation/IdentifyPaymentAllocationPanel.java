package com.nokor.efinance.gui.ui.panel.payment.blocked.identify.allocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;


/**
 * Allocation panel
 * @author uhout.cheng
 */
public class IdentifyPaymentAllocationPanel extends AbstractControlPanel implements FinServicesHelper, ValueChangeListener, ClickListener {

	/** */
	private static final long serialVersionUID = 6212767964435406220L;
	
	private static final String LOCKSPLIT = I18N.message("lock.split");
	private static final String DUES = I18N.message("dues");
	private static final String REFUND = I18N.message("refund");
	
	private ComboBox cbxAllocationType;
	private ComboBox cbxLckSplitType;
	
	private Label lblAmount;
	private TextField txtAmount;
	
	private Button btnAdd;
	
	private SimpleTable<Entity> simpleTable;
	
	private Contract contract;
	
	private List<LockSplitItem> lockSplitItems;

	private LockSplit lockSplit;
	
	private EWkfStatus wkfStatus;
	
	private double totalAmountAllocated = 0d;
	
	private PaymentFileItem paymentFileItem;
	
	/**
	 * @param paymentFileItem the paymentFileItem to set
	 */
	public void setPaymentFileItem(PaymentFileItem paymentFileItem) {
		this.paymentFileItem = paymentFileItem;
	}

	/**
	 * @return the totalAmountAllocated
	 */
	public double getTotalAmountAllocated() {
		return totalAmountAllocated;
	}
	
	/**
	 * @return the lockSplitItems
	 */
	public List<LockSplitItem> getLockSplitItems() {
		return lockSplitItems;
	}

	/**
	 * 
	 * @param btnSubmit
	 * @param wkfStatus
	 */
	public IdentifyPaymentAllocationPanel(Button btnSubmit, EWkfStatus wkfStatus) {
		this.wkfStatus = wkfStatus;
		setSpacing(true);
		setMargin(true);
		
		cbxAllocationType = ComponentFactory.getComboBox();
		cbxAllocationType.setWidth(100, Unit.PIXELS);
		cbxAllocationType.setImmediate(true);
		cbxAllocationType.addItem(LOCKSPLIT);
		cbxAllocationType.addItem(DUES);
		if (PaymentFileWkfStatus.OVER.equals(wkfStatus)) {
			cbxAllocationType.addItem(REFUND);
		}
		cbxAllocationType.addValueChangeListener(this);
		
		cbxLckSplitType = ComponentFactory.getComboBox();
		cbxLckSplitType.setWidth(300, Unit.PIXELS);
		cbxLckSplitType.setImmediate(true);
		cbxLckSplitType.addValueChangeListener(this);
		
		txtAmount = ComponentLayoutFactory.getTextFieldAlignRight(20, 120);
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(5);
		simpleTable.setSizeUndefined();
		simpleTable.setFooterVisible(true);
		simpleTable.setColumnFooter(LockSplit.LOCKSPLITTYPE, I18N.message("total"));
		
		addComponent(createTopLayout());
		addComponent(simpleTable);
		addComponent(btnSubmit);
		setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		
		if (PaymentFileWkfStatus.MATCHED.equals(this.wkfStatus)) {
			List<Cashflow> cashflowsToPaid = CASHFLOW_SRV.getCashflowsToPaid(contract.getId());
			Double totalPaymentAmount = MyNumberUtils.getDouble(paymentFileItem.getAmount());
			List<LockSplit> lockSplits = getLockSplitToAllocate(contract);
			if (lockSplits != null && !lockSplits.isEmpty()) {
				
				double totalLockSplitAmount = getTotalLockSplitAmount(lockSplits, totalPaymentAmount);
		
				if (totalPaymentAmount == totalLockSplitAmount) {
					setIndexedContainer(this.lockSplitItems);
				}
			} else {
				processCashflowPayments(totalPaymentAmount, cashflowsToPaid, contract);
				setIndexedContainer(this.lockSplitItems);
			} 
		} else {
			setIndexedContainer(this.lockSplitItems);
		}
	}
	
	/**
	 * 
	 * @param totalPayment
	 * @param cashflowsToPaid
	 * @param contract
	 */
	private void processCashflowPayments(double totalPayment, List<Cashflow> cashflowsToPaid, Contract contract) {
		List<Cashflow> cashflowsPaid = new ArrayList<Cashflow>();
		for (Cashflow cashflow : cashflowsToPaid) {
			if (totalPayment >= cashflow.getTiInstallmentAmount()) {
				totalPayment -= cashflow.getTiInstallmentAmount();
			} else if (totalPayment > 0) {
				totalPayment = 0d;
			} else {
				break;
			}
			cashflowsPaid.add(cashflow);
		}
		if (!cashflowsPaid.isEmpty()) {
			List<Cashflow> caflws = null;
			Map<String, List<Cashflow>> mapLckSplitTypes = new HashMap<String, List<Cashflow>>();
			for (Cashflow caflw : cashflowsPaid) {
				
				String receiptCode = StringUtils.EMPTY;
				JournalEvent journalEvent = caflw.getJournalEvent();
				
				if (journalEvent != null) {
					receiptCode = journalEvent.getCode();
				}
				
				caflw.setJournalEvent(journalEvent);
				if (!mapLckSplitTypes.containsKey(receiptCode)) {
					caflws = new ArrayList<Cashflow>();
					caflws.add(caflw);
					mapLckSplitTypes.put(receiptCode, caflws);
				} else {
					caflws.add(caflw);
				}
			}
			for (Iterator<String> iter = mapLckSplitTypes.keySet().iterator(); iter.hasNext(); ) {
				List<Cashflow> caflwMaps = mapLckSplitTypes.get(iter.next());
				if (caflwMaps != null && !caflwMaps.isEmpty()) {
					LockSplitItem item = LockSplitItem.createInstance();
					item.setJournalEvent(caflwMaps.get(0).getJournalEvent());
					double totalAmount = 0d;
					for (Cashflow cashflow : caflwMaps) {
						totalAmount += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
						item.setTiAmount(totalAmount); 
					}
					this.lockSplitItems.add(item);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param lockSplits
	 * @param totalAmount
	 * @return
	 */
	private Double getTotalLockSplitAmount(List<LockSplit> lockSplits, double totalAmount) {
		double total = 0d;
		if (lockSplits != null) {
			for (LockSplit lockSplit : lockSplits) {
				List<LockSplitItem> items = lockSplit.getItems();
				total = 0d;
				if (items != null) {
					for (LockSplitItem item : items) {
						total += MyNumberUtils.getDouble(item.getTiAmount());
					}
					if (total == totalAmount) {
						this.lockSplitItems.addAll(items);
						return total;
					}
				}
			}
		}
		return total;
	}
	
	/**
	 * @param contract
	 * @return
	 */
	private List<LockSplit> getLockSplitToAllocate(Contract contract) {
		LockSplitRestriction restriction = new LockSplitRestriction();
		restriction.setContractID(contract.getReference());
		List<EWkfStatus> pendingStatus = new ArrayList<>();
		pendingStatus.add(LockSplitWkfStatus.LNEW);
		pendingStatus.add(LockSplitWkfStatus.LPEN);
		pendingStatus.add(LockSplitWkfStatus.LPAR);
		restriction.setWkfStatusList(pendingStatus);
		return ENTITY_SRV.list(restriction);
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout createTopLayout() {
		lblAmount = ComponentLayoutFactory.getLabelCaptionRequired("amount");
		GridLayout gridLayout = ComponentLayoutFactory.getGridLayout(8, 1);
		gridLayout.addComponent(cbxAllocationType);
		gridLayout.addComponent(cbxLckSplitType);
		gridLayout.addComponent(lblAmount);
		gridLayout.addComponent(txtAmount);
		gridLayout.addComponent(btnAdd);
		return gridLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(LockSplit.ID, I18N.message("id"), Integer.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(LockSplit.REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(LockSplit.LOCKSPLITTYPE, I18N.message("type"), String.class, Align.LEFT, 330));
		columnDefinitions.add(new ColumnDefinition(LockSplit.TOTALAMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition(LockSplit.ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	private void setVisibleAmount(boolean isVisible) {
		lblAmount.setVisible(isVisible);
		txtAmount.setVisible(isVisible);
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		List<LockSplit> lockSplits = null;
		if (this.contract != null) {
			List<EWkfStatus> pendingStatus = new ArrayList<>();
			pendingStatus.add(LockSplitWkfStatus.LNEW);
			pendingStatus.add(LockSplitWkfStatus.LPEN);
			pendingStatus.add(LockSplitWkfStatus.LPAR);
			lockSplits = LCK_SPL_SRV.getLockSplits(this.contract.getReference(), pendingStatus);
		}
		if (event.getProperty().equals(cbxAllocationType)) {
			cbxLckSplitType.removeAllItems();
			setVisibleAmount(true);
			if (LOCKSPLIT.equals(cbxAllocationType.getValue())) {
				setVisibleAmount(false);
				
				if (lockSplits != null) {
					for (LockSplit lockSplit : lockSplits) {
						cbxLckSplitType.addItems(lockSplit.getId()); 
						String lckSplitDetail = I18N.message("from.to.amount", new String[] {
								getDefaultString(DateUtils.getDateLabel(lockSplit.getFrom(), DateUtils.FORMAT_DDMMYYYY_SLASH)),
								getDefaultString(DateUtils.getDateLabel(lockSplit.getTo(), DateUtils.FORMAT_DDMMYYYY_SLASH)),
								MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(lockSplit.getTotalAmount()), "###,##0.00")
						});
						cbxLckSplitType.setItemCaption(lockSplit.getId(), lckSplitDetail);
					}
				}
			} else if (DUES.equals(cbxAllocationType.getValue())) {
				JournalEventRestriction restrictions = new JournalEventRestriction();
				restrictions.setJournalId(JournalEvent.RECEIPTS);
				List<JournalEvent> journalEvents = LCK_SPL_SRV.list(restrictions);
				if (journalEvents != null) {
					for (JournalEvent journalEvent : journalEvents) {
						cbxLckSplitType.addItem(journalEvent.getCode() + " - " + journalEvent.getDescLocale());
					}
				}
			} else if (REFUND.equals(cbxAllocationType.getValue())) {
				
			}
		} else if (event.getProperty().equals(cbxLckSplitType)) {
			if (LOCKSPLIT.equals(cbxAllocationType.getValue()) && cbxLckSplitType.getValue() != null) {
				this.lockSplit = LCK_SPL_SRV.getById(LockSplit.class, Long.parseLong(cbxLckSplitType.getValue().toString()));
			}
		}
	}
	
	/**
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<LockSplitItem> items) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		double totalAmount = 0d;
		int index = 0;
		if (items != null) {
			for (LockSplitItem lckItem : items) {
				Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TRASH_O);
				
				Item item = container.addItem(index);
				String lckType = lckItem.getJournalEvent() != null ? lckItem.getJournalEvent().getDescLocale() : null;
				item.getItemProperty(LockSplit.ID).setValue(index + 1);
				String conRef = StringUtils.EMPTY;
				if (this.contract != null) {
					conRef = this.contract.getReference();
				}
				item.getItemProperty(LockSplit.REFERENCE).setValue(conRef);
				item.getItemProperty(LockSplit.LOCKSPLITTYPE).setValue(lckType);
				item.getItemProperty(LockSplit.TOTALAMOUNT).setValue(AmountUtils.format(MyNumberUtils.getDouble(lckItem.getTiAmount())));
				item.getItemProperty(LockSplit.ACTIONS).setValue(btnDelete);
				
				int actionsIndex = index;
				
				btnDelete.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = -5099873189823182922L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						lockSplitItems.remove(actionsIndex);
						setIndexedContainer(lockSplitItems);
					}
				});
				index++;
				totalAmount += MyNumberUtils.getDouble(lckItem.getTiAmount());
			}
			simpleTable.setColumnFooter(LockSplit.TOTALAMOUNT, AmountUtils.format(totalAmount));
			totalAmountAllocated = totalAmount;
		} else {
			simpleTable.setColumnFooter(LockSplit.TOTALAMOUNT, AmountUtils.format(0d));
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			if (this.lockSplitItems != null) {
				if (getErrorsMsg().isEmpty()) {
					if (DUES.equals(cbxAllocationType.getValue())) {
						String lckSplitTypeCode = String.valueOf(cbxLckSplitType.getValue() != null ? cbxLckSplitType.getValue() : StringUtils.EMPTY);
						lckSplitTypeCode = lckSplitTypeCode.replaceAll("\\s", StringUtils.EMPTY);
						String[] lckSplitTypeCodes = lckSplitTypeCode.split("-");
						JournalEvent lckType = LCK_SPL_SRV.getByCode(JournalEvent.class, lckSplitTypeCodes[0]);
						
						LockSplitItem lockSplitItem = LockSplitItem.createInstance();
						lockSplitItem.setLockSplit(this.lockSplit);
						lockSplitItem.setJournalEvent(lckType);
						lockSplitItem.setTiAmount(getDouble(txtAmount));
						this.lockSplitItems.add(lockSplitItem);
					} else if (LOCKSPLIT.equals(cbxAllocationType.getValue())) {
						if (this.lockSplit != null) {
							this.lockSplitItems.addAll(LCK_SPL_SRV.getLockSplitItemByLockSplit(this.lockSplit.getId()));
						}
					} else if (REFUND.equals(cbxAllocationType.getValue())) {
						
					}
					setIndexedContainer(this.lockSplitItems);
				} else {
					ComponentLayoutFactory.displayErrorMsg(getErrorsMsg().toString());
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<String> getErrorsMsg() {
		errors.clear();
		checkMandatorySelectField(cbxLckSplitType, "lock.split.type");
		if (DUES.equals(cbxAllocationType.getValue())) {
			checkMandatoryField(txtAmount, "amount");
			checkDoubleField(txtAmount, "amount");
		}
		return errors;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		this.lockSplit = null;
		this.lockSplitItems = new ArrayList<LockSplitItem>();
		txtAmount.setValue(StringUtils.EMPTY);
		cbxAllocationType.setValue(null);
		cbxLckSplitType.removeAllItems();
	}
	
}
