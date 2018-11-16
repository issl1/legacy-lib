package com.nokor.efinance.gui.ui.panel.payment.cashier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.gl.finwiz.share.domain.AP.BankDTO;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.BankBranchComboBox;
import com.nokor.efinance.core.widget.BankComboBox;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.allocation.IdentifyPaymentAllocationPanel;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.locksplit.IdentifyPaymentLockSplitTab;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.efinance.third.finwiz.client.bank.ClientBank;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Payment cashier window
 * @author uhout.cheng
 */
public class PaymentCashierPopupWindow extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 9217947540387577910L;

	private final static Logger logger = LoggerFactory.getLogger(PaymentCashierPopupWindow.class);

	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtChequeNO;
	private BankBranchComboBox cbxBankBranch;
	private BankComboBox cbxBankName;
	private TextField txtOwner;
	private TextField txtAmount;
	private AutoDateField dfPaymentDate;
	
	private IdentifyPaymentLockSplitTab lockSplitTab;
	private IdentifyPaymentAllocationPanel allocationPanel;
	
	private PaymentFileItem paymentFileItem;
	
	/**
	 * 
	 * @param tablePanel
	 * @param isPendingCheque
	 * @param status
	 */
	public PaymentCashierPopupWindow(PaymentCashierDetailTablePanel tablePanel, Long bankAccId, boolean isPendingCheque, EWkfStatus status) {
		setModal(true);		
		if (isPendingCheque) {
			setCaption(I18N.message("pending.cheques"));
		} else {
			setCaption(I18N.message("payment.cashier.cheque"));
		}
		setWidth(70, Unit.PERCENTAGE);
		setHeight(85, Unit.PERCENTAGE);
		
		List<EPaymentMethod> values = new ArrayList<>();
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CASH.getCode()));
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CHEQUE.getCode()));
		
		cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"), values);
		cbxPaymentMethod.setImmediate(true);
		cbxPaymentMethod.setWidth(180, Unit.PIXELS);
		
		BankAccountDTO bankAccountDTO = null;
		if (bankAccId != null) {
			bankAccountDTO = ClientBankAccount.getBankAccountById(bankAccId);
		}
		
		cbxBankBranch = new BankBranchComboBox();
		cbxBankBranch.setCaption(I18N.message("bank.branch"));
		cbxBankBranch.setWidth(180, Unit.PIXELS);
		
		List<BankDTO> banks = ClientBank.getBankDTOs();
		cbxBankName = new BankComboBox(banks);
		cbxBankName.setCaption(I18N.message("bank.name"));
		cbxBankName.setWidth(180, Unit.PIXELS);
		
		cbxBankName.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = -2787983493296541609L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxBankName.getSelectedEntity() != null) {
					cbxBankBranch.setBankBranches(ClientBank.getBankBranchsByBankId(cbxBankName.getSelectedEntity().getId()));
				} else {
					cbxBankBranch.setBankBranches(ClientBank.getBankBranchDTOs());
				}
				
			}
			
		});
		
		if (bankAccountDTO != null) {
			if (bankAccountDTO.getBankBranch() != null && bankAccountDTO.getBankBranch().getBank() != null) {
				cbxBankName.setSelectedEntity(bankAccountDTO.getBankBranch().getBank());
				cbxBankBranch.setSelectedEntity(bankAccountDTO.getBankBranch());
			} else {
				cbxBankName.setSelectedEntity(banks != null && !banks.isEmpty() ? banks.get(0) : null);
				cbxBankBranch.setSelectedEntity(null);
			}
		} else {
			cbxBankName.setSelectedEntity(banks != null && !banks.isEmpty() ? banks.get(0) : null);
		}
		
		txtChequeNO = ComponentFactory.getTextField("cheque.no", false, 8, 180);
		txtOwner = ComponentFactory.getTextField("owner", false, 100, 180);
		txtAmount = ComponentFactory.getTextField("amount", false, 50, 180);
		dfPaymentDate = ComponentFactory.getAutoDateField("payment.date", false);
		
		setVisibleChequeControls();
		
		cbxPaymentMethod.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -2787983493296541609L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				setVisibleChequeControls();
			}
		});
		
		Button btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		Button btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.BAN);
		Button btnAllocate = new NativeButton(I18N.message("submit"));
		btnAllocate.setIcon(FontAwesome.SHARE_SQUARE);
		btnAllocate.setVisible(!isPendingCheque);
		
		Button btnSubmit = ComponentLayoutFactory.getDefaultButton("submit", FontAwesome.ARROW_CIRCLE_O_RIGHT, 70);
		btnSubmit.setVisible(isPendingCheque);
		allocationPanel = new IdentifyPaymentAllocationPanel(btnSubmit, status);
		
		lockSplitTab = new IdentifyPaymentLockSplitTab(allocationPanel);
		
		//mainTab = new TabSheet();
		//mainTab.addTab(ComponentLayoutFactory.setMargin(lockSplitTab), I18N.message("customer"));
		
		btnSubmit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8926211724840450055L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (isValidAllocatedAmount()) {
						PAYMENT_ALLOCATION_SRV.allocatedPayment(paymentFileItem);
						if (PaymentFileWkfStatus.ALLOCATED.equals(paymentFileItem.getWkfStatus())) {
							ComponentLayoutFactory.displaySuccessMsg("msg.info.allocate.successfully");
							tablePanel.refreshAfterSaveOrAllocation();
							close();
						} else {
							ComponentLayoutFactory.displayErrorMsg("msg.info.can.not.allocate");
						}
					} else {
						ComponentLayoutFactory.displayErrorMsg("amount.not.equal");
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		
		btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4131525901685614595L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (StringUtils.isEmpty(checkDoubleField())) {
						// update payment file item information
						ENTITY_SRV.saveOrUpdate(getEntity());
						ComponentLayoutFactory.displaySuccessfullyMsg();
						Contract contract = CONT_SRV.getByField(Contract.class, Contract.REFERENCE, StringUtils.trim(paymentFileItem.getCustomerRef1()));
						lockSplitTab.assignValues(paymentFileItem);
						allocationPanel.setPaymentFileItem(paymentFileItem);
						allocationPanel.assignValues(contract);
						tablePanel.refreshAfterSaveOrAllocation();
						
					} else {
						ComponentLayoutFactory.displayErrorMsg(checkDoubleField());
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		
		btnAllocate.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1527746878465409111L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (isValidAllocatedAmount()) {
						PAYMENT_ALLOCATION_SRV.allocatePaymentFileItem(paymentFileItem);
						ComponentLayoutFactory.displaySuccessMsg("msg.info.allocate.successfully");
						tablePanel.refreshAfterSaveOrAllocation();
					} else {
						ComponentLayoutFactory.displayErrorMsg("amount.not.equal");
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -6882253476186663374L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		frmLayout.addComponent(cbxPaymentMethod);
		frmLayout.addComponent(txtChequeNO);
		frmLayout.addComponent(cbxBankName);
		frmLayout.addComponent(cbxBankBranch);
		frmLayout.addComponent(txtOwner);
		frmLayout.addComponent(txtAmount);
		frmLayout.addComponent(dfPaymentDate);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnAllocate);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, false, false), false, frmLayout));
		contentLayout.addComponent(ComponentLayoutFactory.getVerticalLayout(new MarginInfo(true, true, false, true), false, new Panel(lockSplitTab)));
		setContent(contentLayout);
	}
	
	/**
	 * Cannot allocate if amount difference
	 * @return
	 */
	private boolean isValidAllocatedAmount() {
		double amountHeader = MyNumberUtils.getDouble(txtAmount.getValue(), 0d);
		double amountAllocated = allocationPanel.getTotalAmountAllocated();
		if (amountHeader == amountAllocated) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 */
	private void setVisibleChequeControls() {
		EPaymentMethod chequePaymentMethod = ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CHEQUE.getCode());
		txtChequeNO.setVisible(false);
		cbxBankBranch.setVisible(false);
		cbxBankName.setVisible(false);
		txtOwner.setVisible(false);
		if (cbxPaymentMethod.getSelectedEntity() != null) {
			if (cbxPaymentMethod.getSelectedEntity().equals(chequePaymentMethod)) {
				dfPaymentDate.setCaption(I18N.message("promised.date"));
				txtChequeNO.setVisible(true);
				cbxBankBranch.setVisible(true);
				cbxBankName.setVisible(true);
				txtOwner.setVisible(true);
				if (this.paymentFileItem != null && this.paymentFileItem.getId() != null) {
					assignValueToChequeControls();
				} else {
					resetChequeControls();
				}
			} else {
				dfPaymentDate.setCaption(I18N.message("payment.date"));
			}
		} else {
			resetChequeControls();
		}
	}
	
	/**
	 * 
	 */
	private void assignValueToChequeControls() {
		txtChequeNO.setValue(this.paymentFileItem.getChequeNo());
		if (this.paymentFileItem.getBankId() != null) {
			cbxBankName.setSelectedEntity(ClientBank.getBankDTO(this.paymentFileItem.getBankId()));
		}
		if (this.paymentFileItem.getBankBranchId() != null) {
			cbxBankBranch.setSelectedEntity(ClientBank.getBankBranchDTO(this.paymentFileItem.getBankBranchId()));
		}
		txtOwner.setValue(this.paymentFileItem.getOwner());
	}
	
	/**
	 * 
	 */
	private void resetChequeControls() {
		txtChequeNO.setValue(StringUtils.EMPTY);
		cbxBankBranch.setSelectedEntity(null);
		cbxBankName.setSelectedEntity(null);
		txtOwner.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param paymentFileItem
	 */
	public void assignValues(PaymentFileItem paymentFileItem) {
		reset();
		if (paymentFileItem != null) {
			this.paymentFileItem = paymentFileItem;
			cbxPaymentMethod.setSelectedEntity(paymentFileItem.getPaymentMethod());
			lockSplitTab.getTxtContractRef().setValue(paymentFileItem.getCustomerRef1());
			txtAmount.setValue(AmountUtils.format(paymentFileItem.getAmount()));
			dfPaymentDate.setValue(paymentFileItem.getPaymentDate());
			Contract contract = CONT_SRV.getByField(Contract.class, Contract.REFERENCE, StringUtils.trim(paymentFileItem.getCustomerRef1()));
			lockSplitTab.assignValues(paymentFileItem);
			allocationPanel.setPaymentFileItem(this.paymentFileItem);
			allocationPanel.assignValues(contract);
		} else {
			this.paymentFileItem = PaymentFileItem.createInstance();
		} 
	}
	
	/**
	 * 
	 * @return
	 */
	private PaymentFileItem getEntity() {
		this.paymentFileItem.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
		this.paymentFileItem.setPaymentChannel(EPaymentChannel.KEYSYSTEMBYBILL);
		this.paymentFileItem.setChequeNo(txtChequeNO.getValue());
		this.paymentFileItem.setBankId(cbxBankName.getSelectedEntity() == null ? null : cbxBankName.getSelectedEntity().getId());
		this.paymentFileItem.setBankBranchId(cbxBankBranch.getSelectedEntity() == null ? null : cbxBankBranch.getSelectedEntity().getId());	 
		this.paymentFileItem.setOwner(txtOwner.getValue());			
		this.paymentFileItem.setCustomerRef1(lockSplitTab.getTxtContractRef().getValue());
		this.paymentFileItem.setAmount(Double.parseDouble(StringUtils.isEmpty(txtAmount.getValue()) ? "0.0" : txtAmount.getValue()));
		this.paymentFileItem.setPaymentDate(dfPaymentDate.getValue());
		return this.paymentFileItem;
	}
	
	/**
	 * 
	 * @return
	 */
	private String checkDoubleField() {
		if (StringUtils.isNotEmpty(txtAmount.getValue())) {
			try {
				Double.parseDouble(txtAmount.getValue());
			} catch (Exception e) {
				return I18N.message("field.value.incorrect.1", I18N.message("amount"));
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 */
	private void reset() {
		cbxPaymentMethod.setSelectedEntity(null);
		resetChequeControls();
		lockSplitTab.getTxtContractRef().setValue(StringUtils.EMPTY);
		txtAmount.setValue(StringUtils.EMPTY);
		dfPaymentDate.setValue(null);
	}

}
