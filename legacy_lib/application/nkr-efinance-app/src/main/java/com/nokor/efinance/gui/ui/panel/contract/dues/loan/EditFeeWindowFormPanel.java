package com.nokor.efinance.gui.ui.panel.contract.dues.loan;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class EditFeeWindowFormPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3744280662175764559L;

	private TextField txtBalance;
	private TextField txtDiscount;
	private TextArea txtReason;
	private VerticalLayout messagePanel;
	
	/**
	 * 
	 * @param loanFeePenaltyTablePanel
	 * @param cashflow
	 * @param isWaived
	 */
	public EditFeeWindowFormPanel(LoanFeePenaltyTablePanel loanFeePenaltyTablePanel, Cashflow cashflow, boolean isWaived) {
		setModal(true);
		setResizable(false);
		setCaption(I18N.message("edit.fee"));
		
		txtBalance = ComponentFactory.getTextField(30, 150);
		txtBalance.setEnabled(false);
		txtDiscount = ComponentFactory.getTextField(30, 150);
		
		txtReason = ComponentFactory.getTextArea(false, 250, 60);
		
		txtBalance.setValue(AmountUtils.format(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount())));
		txtReason.setValue(cashflow.getReason() == null ? StringUtils.EMPTY : cashflow.getReason());
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8280963423991976656L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
        		if (validate(isWaived)) {
        			if (!isWaived) {
        				double balance = MyNumberUtils.getDouble(txtBalance.getValue(), 0d);
        				double discount = MyNumberUtils.getDouble(txtDiscount.getValue(), 0d);
        				double installmentAmount = balance - discount;
        				double tiInstallmentAmt = installmentAmount + MyNumberUtils.getDouble(cashflow.getVatInstallmentAmount());
        				cashflow.setTiInstallmentAmount(tiInstallmentAmt);
        				cashflow.setTeInstallmentAmount(installmentAmount);
        			}
        			cashflow.setReason(txtReason.getValue());
            		try {
            			if (isWaived) {
            				cashflow.setCancel(true);
            				cashflow.setCancelationDate(DateUtils.today());
            			}
            			CASHFLOW_SRV.update(cashflow);
        				ComponentLayoutFactory.displaySuccessfullyMsg();
                		close();
                		loanFeePenaltyTablePanel.assignValues(cashflow.getContract());
					} catch (Exception e) {
						e.printStackTrace();
						ValidateUtil.addError(I18N.message("msg.error.technical"));
						displayAllErrorsPanel();
					}
        		} else {
        			displayAllErrorsPanel();
        		}
        	}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 8308962816655942755L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.BAN);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
	
		Label lblAmount = ComponentLayoutFactory.getLabelCaptionRequired("balance");
		Label lblDiscount = ComponentFactory.getLabel("discount.title");
		Label lblReason = ComponentFactory.getHtmlLabel(I18N.message("reason") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");

		gridLayout.addComponent(lblAmount);
		gridLayout.addComponent(txtBalance);
		gridLayout.addComponent(lblDiscount);
		
		Label lblMaxDiscount = ComponentFactory.getLabel("(" + I18N.message("max") + StringUtils.SPACE + 
				AmountUtils.format(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount())) + ")");
		HorizontalLayout discountLayout = new HorizontalLayout();
		discountLayout.setSpacing(true);
		discountLayout.addComponent(txtDiscount);
		discountLayout.addComponent(lblMaxDiscount);
		discountLayout.setComponentAlignment(lblMaxDiscount, Alignment.MIDDLE_LEFT);
		
		gridLayout.addComponent(discountLayout);

		gridLayout.addComponent(lblReason);
		gridLayout.addComponent(txtReason);
		
		gridLayout.setComponentAlignment(lblDiscount, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblReason, Alignment.MIDDLE_LEFT);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		if (isWaived) {
			contentLayout.addComponent(getWaiveLayout(cashflow));
		} else {
			contentLayout.addComponent(gridLayout);
		}
        
        setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param cashflow
	 * @return
	 */
	private VerticalLayout getWaiveLayout(Cashflow cashflow) {
		HorizontalLayout reasonLayout = new HorizontalLayout();
		reasonLayout.setSpacing(true);
		Label lblReason = ComponentFactory.getHtmlLabel(I18N.message("reason") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		reasonLayout.addComponent(lblReason);
		reasonLayout.addComponent(txtReason);
		
		reasonLayout.setComponentAlignment(lblReason, Alignment.MIDDLE_LEFT);
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		vLayout.addComponent(ComponentFactory.getLabel(I18N.message("confirm.waive.amount", new String[] { 
				MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount()), "###,##0") })));
		vLayout.addComponent(reasonLayout);
		return vLayout;
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @param isWaived
	 * @return
	 */
	private boolean validate(boolean isWaived) {
		ValidateUtil.clearErrors();
		if (!isWaived) {
			ValidateUtil.checkMandatoryField(txtBalance, "balance");
			ValidateUtil.checkDoubleField(txtBalance, "balance");
			ValidateUtil.checkDoubleField(txtDiscount, "discount.title");
			double balance = MyNumberUtils.getDouble(txtBalance.getValue(), 0d);
			double discount = MyNumberUtils.getDouble(txtDiscount.getValue(), 0d);
			if (discount > balance) {
				ValidateUtil.addError(I18N.message("discount.should.be.less.than.balance"));
			}
		}
		ValidateUtil.checkMandatoryField(txtReason, "reason");
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
}
