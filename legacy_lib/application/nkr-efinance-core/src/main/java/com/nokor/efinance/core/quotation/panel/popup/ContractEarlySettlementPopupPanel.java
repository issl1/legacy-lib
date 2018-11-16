package com.nokor.efinance.core.quotation.panel.popup;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.exception.EntityNotFoundException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSaveRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementService;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.EarlySettlementSimulateResponse;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Early Settlement Pop up Panel
 * @author bunlong.taing
 */
public class ContractEarlySettlementPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -956669297440899031L;
	
	private EarlySettlementService earlySettlementService = SpringUtils.getBean(EarlySettlementService.class);
	
	private NativeButton btnSimulate;
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	
	private AutoDateField dfClosingDate;
	
	private TextField txtOutstandingAR;
	private TextField txtOtherMandatoryDebts;
	private TextField txtOtherOptionalDebts;
	private TextField txtAmountDiscounted;
	private TextField txtTotalAmountToPay;
	
	private Contract contract;
	private HorizontalLayout messagePanel;
	private EarlySettlementSimulateResponse simulateResponse;
	
	/**
	 * Contract Early Settlement Pop up Panel
	 */
	public ContractEarlySettlementPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(420, Unit.PIXELS);
		setHeight(320, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init components
	 */
	private void init() {
		messagePanel = new HorizontalLayout();
		messagePanel.addStyleName("message");
		messagePanel.setVisible(false);
		
		btnSimulate = new NativeButton(I18N.message("simulate"));
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnSimulate.addClickListener(this);
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		btnSubmit.setVisible(false);
		
		dfClosingDate = ComponentFactory.getAutoDateField("closing.date", true);
		dfClosingDate.setValue(DateUtils.today());
		txtOutstandingAR = ComponentFactory.getTextField("outstanding.ar", false, 50, 200);
		txtOtherMandatoryDebts = ComponentFactory.getTextField("other.mandatory.debts", false, 50, 200);
		txtOtherOptionalDebts = ComponentFactory.getTextField("other.optional.debts", false, 50, 200);
		txtAmountDiscounted = ComponentFactory.getTextField("amount.discounted", false, 50, 200);
		txtTotalAmountToPay = ComponentFactory.getTextField("total.amount.to.pay", false, 50, 200);
		
		txtOutstandingAR.setEnabled(false);
		txtOtherMandatoryDebts.setEnabled(false);
		txtOtherOptionalDebts.setEnabled(false);
		txtAmountDiscounted.setEnabled(false);
		txtTotalAmountToPay.setEnabled(false);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSimulate);
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(dfClosingDate);
		formLayout.addComponent(txtOutstandingAR);
		formLayout.addComponent(txtOtherMandatoryDebts);
		formLayout.addComponent(txtOtherOptionalDebts);
		formLayout.addComponent(txtAmountDiscounted);
		formLayout.addComponent(txtTotalAmountToPay);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		setContent(verticalLayout);
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		if (contract != null) {
			this.contract = contract;
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("contract.cannot.be.null"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("300px");
			mb.setHeight("150px");
			mb.show();
			close();
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSimulate) {
			if (validate()) {
				simulate();
				btnSubmit.setVisible(true);
			} else {
				displayMessage(I18N.message("field.required.1", new String[] {dfClosingDate.getCaption()}));
			}
		} else if (event.getButton() == btnSubmit) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(
					UI.getCurrent(),
					I18N.message("confirm.earlysettlement.contract"),
					new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = 5948261755857464781L;
						@Override
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								submit();
							}
						}
					});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Simulate
	 */
	private void simulate() {
		EarlySettlementSimulateRequest request = new EarlySettlementSimulateRequest();
		request.setCotraId(contract.getId());
		request.setEventDate(dfClosingDate.getValue());
		request.setIncludePenalty(true);
		simulateResponse = earlySettlementService.simulate(request);
		
		double outstandingAR = getDouble(simulateResponse.getBalanceCapital().getTiAmount()) + getDouble(simulateResponse.getBalanceInterest().getTiAmount());
		double otherMandatoryDebt = getDouble(simulateResponse.getBalancePenalty().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalanceCollectionFee().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalanceFollowingFee().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalanceOperationFee().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalancePressingFee().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalanceRepossessionFee().getTiAmount());
		otherMandatoryDebt += getDouble(simulateResponse.getBalanceTransferFee().getTiAmount());
		
		double amountDiscounted = getDouble(simulateResponse.getBalanceInterest().getTiAmount()) / 2;
		double totalAmount = outstandingAR + otherMandatoryDebt - amountDiscounted;
		
		txtOutstandingAR.setValue(AmountUtils.format(outstandingAR));
		txtOtherMandatoryDebts.setValue(AmountUtils.format(otherMandatoryDebt));
		txtAmountDiscounted.setValue(AmountUtils.format(amountDiscounted));
		txtTotalAmountToPay.setValue(AmountUtils.format(totalAmount));
	}
	
	/**
	 * Submit
	 */
	private void submit() {    	
    	EarlySettlementSaveRequest request = new EarlySettlementSaveRequest();
    	request.setCotraId(contract.getId());
    	request.setEarlySettlementDate(simulateResponse.getEventDate());
    	request.setCashflows(simulateResponse.getCashflows());
    	request.setBalanceCapital(simulateResponse.getBalanceCapital());
    	request.setBalanceInterest(simulateResponse.getBalanceInterest());
    	request.setBalanceCollectionFee(simulateResponse.getBalanceCollectionFee());
    	request.setBalanceFollowingFee(simulateResponse.getBalanceFollowingFee());
    	request.setBalanceOperationFee(simulateResponse.getBalanceOperationFee());
    	request.setBalancePenalty(simulateResponse.getBalancePenalty());
    	request.setBalancePressingFee(simulateResponse.getBalancePressingFee());
    	request.setBalanceRepossessionFee(simulateResponse.getBalanceRepossessionFee());
    	request.setBalanceTransferFee(simulateResponse.getBalanceTransferFee());    	
    	request.setAdjustmentInterest(simulateResponse.getAdjustmentInterest());
    	try {
    		earlySettlementService.save(request);
    		close();
    	} catch (EntityNotFoundException e) {
    		displayMessage(e.getMessage());
    	}
	}
	
	/**
	 * Validate component value
	 * @return
	 */
	private boolean validate() {
		boolean valid = true;
		if (dfClosingDate.getValue() == null) {
			valid = false;
		}
		return valid;
	}
	
	/**
	 * Display message panel
	 */
	private void displayMessage(String messages) {
		Label messageLabel = new Label(messages);
		messageLabel.addStyleName("error");
		
		messagePanel.removeAllComponents();
		messagePanel.setVisible(true);
		messagePanel.setSpacing(true);
		messagePanel.setWidth(100, Unit.PERCENTAGE);
		messagePanel.addComponent(messageLabel);
	}
	
	/**
	 * Get double value
	 * @param value
	 * @return
	 */
	private double getDouble(Double value) {
		return value == null ? 0d : value;
	}

}
