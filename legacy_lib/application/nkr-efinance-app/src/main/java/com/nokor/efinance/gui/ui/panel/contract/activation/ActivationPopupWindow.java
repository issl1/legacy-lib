package com.nokor.efinance.gui.ui.panel.contract.activation;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * For Activation Contract
 * @author buntha.chea
 */
public class ActivationPopupWindow extends Window implements FinServicesHelper, MContract, ValueChangeListener {

	/**	 * */
	private static final long serialVersionUID = 7762394057333300798L;
	
	private ERefDataComboBox<EHistoReason> cbxForceReason;
	private ERefDataComboBox<EHistoReason> cbxHoldPaymentReason;
	private TextArea txtForceComment;
	private TextArea txtHoldPaymentComment;
	private CheckBox cbForce;
	private CheckBox cbHoldPayment;
	private boolean isForceMandatory;
	private boolean isHoldPaymentMandatory;
	
	private VerticalLayout messagePanel;
	
	/**
	 * @return the cbForce
	 */
	public CheckBox getCbForce() {
		return cbForce;
	}

	/**
	 * @return the cbHoldPayment
	 */
	public CheckBox getCbHoldPayment() {
		return cbHoldPayment;
	}

	/**
	 * 
	 * @return
	 */
	private TextArea getTextArea() {
		TextArea textArea = ComponentFactory.getTextArea(false, 250, 100);
		textArea.setMaxLength(1000);
		return textArea;
	}
	
	/**
	 * 
	 * @param caption
	 */
	public ActivationPopupWindow(Contract contract, EWkfStatus wkfStatus, ClickListener onSaveListener) {
		setModal(true);		
		setClosable(false);
		setResizable(false);
		
		txtForceComment = getTextArea();
		txtHoldPaymentComment = getTextArea();
		txtForceComment.setEnabled(false);
		txtHoldPaymentComment.setEnabled(false);
		
		cbForce = new CheckBox(I18N.message("force"));
		cbHoldPayment = new CheckBox(I18N.message("hold.payment"));
		cbForce.addValueChangeListener(this);
		cbHoldPayment.addValueChangeListener(this);
		
		isForceMandatory = false;
		isHoldPaymentMandatory = false;
		
		cbxForceReason = new ERefDataComboBox<>(EHistoReason.getForceHistoriesReason());
		cbxForceReason.setImmediate(true);
		cbxForceReason.setWidth(170, Unit.PIXELS);
		cbxForceReason.setEnabled(false);
		
		cbxHoldPaymentReason = new ERefDataComboBox<>(EHistoReason.getHoldPaymentHistoriesReason());
		cbxHoldPaymentReason.setImmediate(true);
		cbxHoldPaymentReason.setWidth(170, Unit.PIXELS);
		cbxHoldPaymentReason.setEnabled(false);
		
		Button btnconfirm = ComponentLayoutFactory.getButtonStyle("confirm", FontAwesome.CHECK, 80, "btn btn-success button-small");
		Button btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.BAN, 80, "btn btn-success button-small");
		
		btnconfirm.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -6182677160749225701L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (validation(contract)) {
					if (isForceMandatory) {
						if (cbxForceReason.getSelectedEntity() == null) {
							MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
									MessageBox.Icon.ERROR, I18N.message("please.select.a.reason"), 
									Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
						} else if (isHoldPaymentMandatory) {
							checkHoldPaymentMandatory(onSaveListener, event, contract);
						} else {
							updateContractHistory(onSaveListener, event, contract);
							//loanFormPanel.setLoanContent(contract);
						}
					} else {
						if (isHoldPaymentMandatory) {
							checkHoldPaymentMandatory(onSaveListener, event, contract);
						} else {
							updateContractHistory(onSaveListener, event, contract);
						}
					}
				} else {
					displayAllErrorsPanel();
				}
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -421984394158414972L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnconfirm);
		buttonLayout.addComponent(btnCancel);
		
		HorizontalLayout forceInputLayout = new HorizontalLayout();
		forceInputLayout.setSpacing(true);
		forceInputLayout.addComponent(cbxForceReason);
		forceInputLayout.addComponent(ComponentFactory.getLabel("remark"));
		forceInputLayout.addComponent(txtForceComment);
		
		HorizontalLayout cbForceLayout = new HorizontalLayout();
		cbForceLayout.setWidth(100, Unit.PIXELS);
		cbForceLayout.addComponent(cbForce);
		
		HorizontalLayout cbHoldPayLayout = new HorizontalLayout();
		cbHoldPayLayout.setWidth(100, Unit.PIXELS);
		cbHoldPayLayout.addComponent(cbHoldPayment);
		
		HorizontalLayout forceLayout = new HorizontalLayout();
		forceLayout.setSpacing(true);
		forceLayout.addComponent(cbForceLayout);
		forceLayout.addComponent(forceInputLayout);
		
		HorizontalLayout holdPaymentInputLayout = new HorizontalLayout();
		holdPaymentInputLayout.setSpacing(true);
		holdPaymentInputLayout.addComponent(cbxHoldPaymentReason);
		holdPaymentInputLayout.addComponent(ComponentFactory.getLabel("remark"));
		holdPaymentInputLayout.addComponent(txtHoldPaymentComment);
		
		HorizontalLayout holdPaymentLayout = new HorizontalLayout();
		holdPaymentLayout.setSpacing(true);
		holdPaymentLayout.addComponent(cbHoldPayLayout);
		holdPaymentLayout.addComponent(holdPaymentInputLayout);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(new MarginInfo(false, true, true, true));
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		contentLayout.setSizeUndefined();
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(forceLayout);
		contentLayout.addComponent(holdPaymentLayout);
		contentLayout.addComponent(buttonLayout);
		contentLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
        setContent(contentLayout);
	}
	
	/**
	 * Display error message panel
	 */
	protected void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Removed error message panel
	 */
	protected void removedAllErrorsPanel() {
		ValidateUtil.clearErrors();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private boolean validation(Contract contract) {
		ValidateUtil.clearErrors();
		DealerPaymentMethod dealerPaymentMethod = contract.getDealer().getDealerPaymentMethod(EPaymentFlowType.FIN);	
		if (dealerPaymentMethod == null) {
			ValidateUtil.addError(I18N.message("msg.null.dealer.payment.method"));
		}
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
	/**
	 * 
	 * @param onSaveListener
	 * @param event
	 * @param contract
	 */
	private void checkHoldPaymentMandatory(ClickListener onSaveListener, ClickEvent event, Contract contract) {
		if (cbxHoldPaymentReason.getSelectedEntity() == null || StringUtils.isEmpty(txtHoldPaymentComment.getValue())) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("please.select.a.reason.type.and.enter.remark.hold.payment"), 
					Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			updateContractHistory(onSaveListener, event, contract);
		}	
	}
	
	/**
	 * 
	 * @param onSaveListener
	 * @param event
	 * @param contract
	 */
	private void updateContractHistory(ClickListener onSaveListener, ClickEvent event, Contract contract) {
		contract.setWkfReason(cbxForceReason.getSelectedEntity());
		contract.setHistComment(txtForceComment.getValue());
		contract.setWkfReason2(cbxHoldPaymentReason.getSelectedEntity());
		contract.setHistComment2(txtHoldPaymentComment.getValue());
		contract.setForcedHistory(true);
		CONT_SRV.saveOrUpdate(contract);
		if (onSaveListener != null) {
			onSaveListener.buttonClick(event);
		}
		close();
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbForce)) {
			cbxForceReason.setEnabled(cbForce.getValue());
			txtForceComment.setEnabled(cbForce.getValue());
			isForceMandatory = cbForce.getValue();
		} else if (event.getProperty().equals(cbHoldPayment)) {
			cbxHoldPaymentReason.setEnabled(cbHoldPayment.getValue());
			txtHoldPaymentComment.setEnabled(cbHoldPayment.getValue());
			isHoldPaymentMandatory = cbHoldPayment.getValue();
		} 
	}

}
