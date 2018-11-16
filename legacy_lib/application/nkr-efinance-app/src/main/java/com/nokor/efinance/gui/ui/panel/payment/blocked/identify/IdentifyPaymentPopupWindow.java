package com.nokor.efinance.gui.ui.panel.payment.blocked.identify;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.BlockedPaymentDetailTablePanel;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.allocation.IdentifyPaymentAllocationPanel;
import com.nokor.efinance.gui.ui.panel.payment.blocked.identify.locksplit.IdentifyPaymentLockSplitTab;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Identify Payments window
 * @author uhout.cheng
 */
public class IdentifyPaymentPopupWindow extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -7862028290696405083L;
	
	private final Logger LOG = LoggerFactory.getLogger(IdentifyPaymentPopupWindow.class);

	private ERefDataComboBox<EPaymentChannel> cbxChannel;
	private TextField txtAmount;
	private AutoDateField dfUploadDate;
	private AutoDateField dfPaymentDate;
	
	private IdentifyPaymentLockSplitTab lockSplitTab;
	private IdentifyPaymentAllocationPanel allocationPanel;
	
	private PaymentFileItem paymentFileItem;
	
	/**
	 * 
	 * @param tablePanel
	 * @param wkfStatus
	 */
	public IdentifyPaymentPopupWindow(BlockedPaymentDetailTablePanel tablePanel, EWkfStatus wkfStatus) {
		setModal(true);		
		setCaption(I18N.message("identify.payments"));
		setWidth(70, Unit.PERCENTAGE);
		setHeight(85, Unit.PERCENTAGE);
		
		cbxChannel = new ERefDataComboBox<EPaymentChannel>(EPaymentChannel.values());
		cbxChannel.setWidth(180, Unit.PIXELS);
		txtAmount = ComponentFactory.getTextField(50, 180);
		dfUploadDate = ComponentFactory.getAutoDateField();
		dfUploadDate.setEnabled(false);
		dfPaymentDate = ComponentFactory.getAutoDateField();
		
		Button btnSubmit = ComponentLayoutFactory.getDefaultButton("submit", FontAwesome.ARROW_CIRCLE_O_RIGHT, 70);
		allocationPanel = new IdentifyPaymentAllocationPanel(btnSubmit, wkfStatus);
		
		lockSplitTab = new IdentifyPaymentLockSplitTab(allocationPanel);
		btnSubmit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 6000795733466650720L;

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
					LOG.error(e.getMessage());
				}
			}
		});
		
		Button btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		Button btnError = new NativeButton(I18N.message("error"));
		btnSave.setIcon(FontAwesome.MINUS_SQUARE);
		Button btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.BAN);
		Button btnAllocate = new NativeButton(I18N.message("allocate"));
		btnAllocate.setIcon(FontAwesome.SHARE_SQUARE);
		
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
						ENTITY_SRV.update(getEntity());
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
					LOG.error(e.getMessage());
				}
			}
		});
		
		btnError.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -7446155476761086462L;

			@Override
			public void buttonClick(ClickEvent event) {
				paymentFileItem.setWkfStatus(PaymentFileWkfStatus.ERROR);
				ENTITY_SRV.saveOrUpdate(paymentFileItem);
				tablePanel.refreshAfterSaveOrAllocation();
				close();
			}
		});
		btnError.setVisible(PaymentFileWkfStatus.UNIDENTIFIED.equals(wkfStatus));
		
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
		
		btnAllocate.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 5303962460462519773L;

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
					LOG.error(e.getMessage());
				}
			}
		});
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verLayout.addComponent(getEditableLayout("channel", cbxChannel, false));
		verLayout.addComponent(getEditableLayout("amount", txtAmount, true));
		verLayout.addComponent(getEditableLayout("upload.date", dfUploadDate, false));
		verLayout.addComponent(getEditableLayout("payment.date", dfPaymentDate, true));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnError);
		navigationPanel.addButton(btnCancel);
		navigationPanel.addButton(btnAllocate);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, false, false), false, verLayout));
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
	 * @param caption
	 * @param component
	 * @param isEdited
	 * @return
	 */
	private Component getEditableLayout(String caption, Component component, boolean isEdited) {
		component.setEnabled(false);
		Button btnEdit = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		btnEdit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -4375713600629565977L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				component.setEnabled(!component.isEnabled());
			}
		});

		GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setMargin(new MarginInfo(false, true, false, true));
		gridLayout.setSpacing(true);
		gridLayout.addComponent(getLabel(caption));
		gridLayout.addComponent(component);
		if (isEdited) {
			gridLayout.addComponent(btnEdit);
			gridLayout.setComponentAlignment(btnEdit, Alignment.MIDDLE_RIGHT);
		}
		
		return gridLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = ComponentLayoutFactory.getLabelCaption(caption);
		label.setWidth(120, Unit.PIXELS);
		return label;
	}
	
	/**
	 * 
	 * @param paymentFileItem
	 */
	public void assignValues(PaymentFileItem paymentFileItem) {
		this.paymentFileItem = paymentFileItem;
		cbxChannel.setSelectedEntity(paymentFileItem.getPaymentChannel());
		lockSplitTab.getTxtContractRef().setValue(paymentFileItem.getCustomerRef1());
		txtAmount.setValue(AmountUtils.format(paymentFileItem.getAmount()));
		dfUploadDate.setValue(paymentFileItem.getCreateDate());
		dfPaymentDate.setValue(paymentFileItem.getPaymentDate());
		Contract contract = CONT_SRV.getByField(Contract.class, Contract.REFERENCE, StringUtils.trim(paymentFileItem.getCustomerRef1()));
		lockSplitTab.assignValues(paymentFileItem);
		allocationPanel.setPaymentFileItem(this.paymentFileItem);
		allocationPanel.assignValues(contract);
	}
	
	/**
	 * 
	 * @return
	 */
	private PaymentFileItem getEntity() {
		this.paymentFileItem.setPaymentMethod(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.TRANSFER.getCode()));
		this.paymentFileItem.setPaymentChannel(cbxChannel.getSelectedEntity());
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

}
