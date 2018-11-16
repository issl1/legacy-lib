package com.nokor.efinance.gui.ui.panel.contract.loan;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.LossService;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateRequest;
import com.nokor.efinance.core.contract.service.aftersales.LossSimulateResponse;
import com.nokor.efinance.core.contract.service.aftersales.LossValidateRequest;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.CommentReasonFormPanel;
import com.nokor.efinance.gui.ui.panel.contract.ContractUtils;
import com.nokor.efinance.gui.ui.panel.contract.activation.ActivationTransferPopupWindow;
import com.nokor.efinance.gui.ui.panel.contract.activation.ContractStatusDetailPanel;
import com.nokor.efinance.gui.ui.panel.contract.activation.IssuesPanel;
import com.nokor.efinance.gui.ui.panel.contract.activation.UpdateDataPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class LoanPanel extends AbstractTabPanel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = 8606542317467706778L;
	
	private Contract contract;
	
	private UpdateDataPanel updateDataPanel;
	private IssuesPanel issuesLayout;
	private ContractStatusDetailPanel contractStatusDetailPanel;
	private CommentReasonFormPanel cmtReasonFormPanel;
	
	private Button btnPay;
	private Button btnTerminate;
	private Button btnModify;
	
	private Button btnBlock;
	private Button btnBlockTransfer;
	private Button btnActivateTransfer;
	private Button btnCancelContract;
	private Button btnCancelTransfer;
	
	private Button btnPrintSchedule;
	private Button btnWriteOff;
	private Button btnSellOff;
	private ActivationTransferPopupWindow activationTransferPopupWindow;
	
	/**
	 * 
	 */
	public LoanPanel() {
		super();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setHeight(580, Unit.PIXELS);
		updateDataPanel = new UpdateDataPanel();
		issuesLayout = new IssuesPanel();
		contractStatusDetailPanel = new ContractStatusDetailPanel();
		
		btnPay = ComponentLayoutFactory.getButtonStyle("pay", null, 70, "btn btn-success");
		btnPay.addClickListener(this);
		
		btnTerminate = ComponentLayoutFactory.getButtonStyle("terminate", null, 70, "btn btn-danger");
		btnTerminate.addClickListener(this);
		
		btnModify = ComponentLayoutFactory.getButtonStyle("modify", null, 70, "btn btn-success");
		btnModify.addClickListener(this);
		
		btnBlock = new NativeButton(I18N.message("block"));
		btnBlock.addClickListener(this);
		btnBlock.addStyleName("btn btn-danger button-small");
		btnBlock.setIcon(FontAwesome.BAN);
		
		btnBlockTransfer = new NativeButton(I18N.message("block"));
		btnBlockTransfer.addClickListener(this);
		btnBlockTransfer.addStyleName("btn btn-danger button-small");
		btnBlockTransfer.setIcon(FontAwesome.BAN);
//		
//		btnActivate = new NativeButton(I18N.message("activate"));
//		btnActivate.addClickListener(this);
//		btnActivate.addStyleName("btn btn-success button-small");
//		btnActivate.setIcon(FontAwesome.CHECK);
//		
		btnActivateTransfer = new NativeButton(I18N.message("approve.transfer"));
		btnActivateTransfer.addClickListener(this);
		btnActivateTransfer.addStyleName("btn btn-success button-small");
		btnActivateTransfer.setIcon(FontAwesome.CHECK);
		
		btnCancelContract = new NativeButton(I18N.message("cancel.contract.title"));
		btnCancelContract.addClickListener(this);
		btnCancelContract.addStyleName("btn btn-danger button-small");
		btnCancelContract.setIcon(FontAwesome.TIMES);
		
		btnCancelTransfer = new NativeButton(I18N.message("cancel.contract.title"));
		btnCancelTransfer.addClickListener(this);
		btnCancelTransfer.addStyleName("btn btn-danger button-small");
		btnCancelTransfer.setIcon(FontAwesome.TIMES);
//		
//		btnTerminate = new NativeButton(I18N.message("terminate"));
//		btnTerminate.addClickListener(this);
//		btnTerminate.addStyleName("btn btn-danger button-small");
//		btnTerminate.setIcon(FontAwesome.TIMES);
		
		btnWriteOff = new NativeButton(I18N.message("write.off"));
		btnWriteOff.addClickListener(this);
		btnWriteOff.addStyleName("btn btn-success button-small");
		btnWriteOff.setIcon(FontAwesome.BOOK);
		
		btnSellOff = new NativeButton(I18N.message("sell.off"));
		btnSellOff.addClickListener(this);
		btnSellOff.addStyleName("btn btn-success button-small");
		btnSellOff.setIcon(FontAwesome.MONEY);
		
		btnPrintSchedule = new NativeButton(I18N.message("print.schedule"));
		btnPrintSchedule.addClickListener(this);
		btnPrintSchedule.addStyleName("btn btn-success button");
		btnPrintSchedule.setIcon(FontAwesome.PRINT);
		
		HorizontalLayout summaryButton = new HorizontalLayout();
		summaryButton.setSpacing(true);
		summaryButton.addComponent(btnPay);
		summaryButton.addComponent(btnTerminate);
		summaryButton.addComponent(btnModify);
		
		HorizontalLayout summaryButtonUnder = ComponentLayoutFactory.getHorizontalLayout(false, true);
		summaryButtonUnder.addComponent(btnBlock);
		summaryButtonUnder.addComponent(btnBlockTransfer);
		summaryButtonUnder.addComponent(btnActivateTransfer);
		summaryButtonUnder.addComponent(btnCancelContract);
		summaryButtonUnder.addComponent(btnCancelTransfer);
		summaryButtonUnder.addComponent(btnPrintSchedule);
		//summaryButtonUnder.addComponent(btnReleasePayment);
		summaryButtonUnder.addComponent(btnWriteOff);
		summaryButtonUnder.addComponent(btnSellOff);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, true, false), true);
		mainLayout.addComponent(summaryButton);
		mainLayout.addComponent(updateDataPanel);
		mainLayout.addComponent(issuesLayout);
		mainLayout.addComponent(contractStatusDetailPanel);
		mainLayout.addComponent(summaryButtonUnder);
		mainLayout.setComponentAlignment(summaryButton, Alignment.TOP_RIGHT);

		return mainLayout;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract, boolean contractAssigned) {
		this.contract = contract;
	
		updateDataPanel.assignValue(contract);
		issuesLayout.assignValue(contract);
		
		contractStatusDetailPanel.assignValues(contract);
		btnCancelContract.setVisible(ProfileUtil.isCMLeader() && ContractUtils.isBeforeActive(contract));
		btnBlock.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isPending(contract) && contractAssigned);
		btnTerminate.setVisible(ProfileUtil.isCMLeader() && (ContractUtils.isHoldPayment(contract) || ContractUtils.isActivated(contract)));
		btnBlockTransfer.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isActivated(contract) && com.nokor.efinance.core.contract.service.ContractUtils.isSubStatusPendingTransfer(contract) && contractAssigned);
		btnActivateTransfer.setVisible(ProfileUtil.isCMProfile() && ContractUtils.isActivated(contract) && com.nokor.efinance.core.contract.service.ContractUtils.isSubStatusBeforeActive(contract) && contractAssigned);
		btnCancelTransfer.setVisible(ProfileUtil.isCMLeader() && ContractUtils.isActivated(contract) && com.nokor.efinance.core.contract.service.ContractUtils.isSubStatusBeforeActive(contract));
		btnPrintSchedule.setVisible(ContractWkfStatus.HOLD_PAY.equals(contract.getWkfStatus()));
		btnPrintSchedule.setEnabled(false);
		btnWriteOff.setVisible(ProfileUtil.isAccounting());
		btnSellOff.setVisible(ProfileUtil.isAccounting());
	}
	
	/**
	 * Clean successfully message
	 */
	public void cleanMessage() {
		updateDataPanel.removeErrorsPanel();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		updateDataPanel.reset();
	}
	
	/**
	 * 
	 * @param description
	 * @return
	 */
	private Notification getNotificationDesc(String description) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ contract.getReference() }));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		Page.getCurrent().setUriFragment("!dashboard");
		return notification;
	}
	
	/**
	 * Terminate
	 */
	private void confirmTerminate() {
		LossService lossService = SpringUtils.getBean(LossService.class);
		LossSimulateRequest simulateRequest = new LossSimulateRequest();
		simulateRequest.setCotraId(contract.getId());
		simulateRequest.setEventDate(DateUtils.today());
		LossSimulateResponse simulateResponse = lossService.simulate(simulateRequest);
		
		LossValidateRequest validateRequest = new LossValidateRequest();
		validateRequest.setCotraId(simulateResponse.getCotraId());
		validateRequest.setCashflows(simulateResponse.getCashflows());
		validateRequest.setTotalPrincipal(simulateResponse.getTotalPrincipal());
		validateRequest.setTotalInterest(simulateResponse.getTotalInterest());
		validateRequest.setTotalOther(simulateResponse.getTotalOther());
		validateRequest.setInsuranceFee(simulateResponse.getInsuranceFee());
		validateRequest.setServicingFee(simulateResponse.getServicingFee());
		validateRequest.setEventDate(simulateResponse.getEventDate());
		validateRequest.setWkfStatus(ContractWkfStatus.WRI);
		lossService.validate(validateRequest);
		getNotificationDesc("terminate.contract.message");
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {	
		if (event.getButton() == btnPay) {
			
		} else if (event.getButton() == btnTerminate) {
			showCommentReasonForm("CONTRACT_WRI", ContractWkfStatus.WRI, "terminate.contract.title");
		} else if (event.getButton() == btnActivateTransfer) {
			showActivationTransferPopup("approve.transfer");
		}  else if (event.getButton() == btnCancelContract) {
		   showCommentReasonForm("CONTRACT_CAN", ContractWkfStatus.CAN, "cancel.contract.title");
		} else if (event.getButton() == btnBlock) {
			confirmBlock();
		} else if (event.getButton() == btnCancelTransfer) {
			showCommentReasonForm("CONTRACT_CAN", ContractWkfStatus.FIN, "cancel.contract.title");
		} else if (event.getButton() == btnBlockTransfer) {
			confirmBlock();
		} else if (event.getButton() == btnWriteOff) {
			ComponentLayoutFactory.displaySuccessMsg("Write Off Clicked");
		} else if (event.getButton() == btnSellOff) {
			ComponentLayoutFactory.displaySuccessMsg("Sell Off Clicked");
		}
	}
	
	/**
	 * Window pop up for Hold, Cancel & UnHold Payment
	 * @param code
	 * @param status
	 * @param caption
	 */
	private void showCommentReasonForm(String code, EWkfStatus status, String caption) {
		cmtReasonFormPanel = new CommentReasonFormPanel(code, contract, status, new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -1993460501061546685L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (status.equals(ContractWkfStatus.WRI)) {
    				confirmTerminate();
    			} else if (status.equals(ContractWkfStatus.CAN)) {
    				CONT_SRV.cancelContract(contract.getId());
    				getNotificationDesc("cancel.contract.message");
    			} else if (status.equals(ContractWkfStatus.FIN)) {
    				TRANSFERT_SRV.cancel(contract);
    				getNotificationDesc("cancel.contract.message");
    			}
			}
		});
		cmtReasonFormPanel.setCaption(I18N.message(caption));
		UI.getCurrent().addWindow(cmtReasonFormPanel);
	}
	
	/**
	 * Window pop up for activation contract
	 * @param code
	 * @param status
	 * @param caption
	 */
	private void showActivationTransferPopup(String caption) {
		activationTransferPopupWindow = new ActivationTransferPopupWindow(contract, new ClickListener() {
			private static final long serialVersionUID = 4233840824306926207L;

			@Override
			public void buttonClick(ClickEvent event) {
				TRANSFERT_SRV.validate(contract, activationTransferPopupWindow.getCbForce().getValue());
				getNotificationDesc("activate.contract");
			}
		});
		activationTransferPopupWindow.setCaption(I18N.message(caption));
		UI.getCurrent().addWindow(activationTransferPopupWindow);
	}
	
	/**
	 * confirm dialog box with click button cancel
	 */
	private void confirmUnholdDealerPayment() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.unhold.application"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_ACTIVATION_SRV.unholdDealerPayment(contract);
							getNotificationDesc("unhold.contract.message");
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * confirm dialog box with click button hold
	 */
	private void confirmBlock() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.block.this.contract"),
				new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							CONT_SRV.hold(contract.getId());
							getNotificationDesc("block.contract");
						} 
					}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	/**
	 * @return the updateDataPanel
	 */
	public UpdateDataPanel getUpdateDataPanel() {
		return updateDataPanel;
	}
}
