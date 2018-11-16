package com.nokor.efinance.gui.ui.panel.payment;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.contract.service.cashflow.impl.CashflowUtils;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 *
 */
public class OverduePopupPanel extends Window {
	
	private static final long serialVersionUID = 1899042462222038683L;
	
	private PaymentService paymentService = (PaymentService) SecApplicationContextHolder.getContext().getBean("paymentService");
	private CashflowService cashflowService = (CashflowService) SecApplicationContextHolder.getContext().getBean("cashflowService");
	private TextField txtNumPenaltyDays;
	private TextField txtPenaltyAmount;
	private Long paymentId;
	private ClickListener saveClicklistener;

	public OverduePopupPanel(String caption) {
		setModal(true);
		final Window winAddService = new Window(caption);
		winAddService.setModal(true);
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
		txtNumPenaltyDays = ComponentFactory.getTextField(false, 10, 150);  
		txtNumPenaltyDays.setRequired(true);
		txtPenaltyAmount = ComponentFactory.getTextField(false, 10, 150);
		txtPenaltyAmount.setRequired(true);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(txtNumPenaltyDays);
        verticalLayout.addComponent(txtPenaltyAmount);
        
        final GridLayout gridLayout = new GridLayout(4, 3);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("num.penalty.days")), iCol++, 0);
        gridLayout.addComponent(txtNumPenaltyDays, iCol++, 0);
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("num.overdue.amount")), iCol++, 1);
        gridLayout.addComponent(txtPenaltyAmount, iCol++, 1);
        verticalLayout.addComponent(gridLayout);
	    formLayout.addComponent(verticalLayout);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
        	
			private static final long serialVersionUID = 8088485001713740490L;

					public void buttonClick(ClickEvent event) {

						if (validate()) {
							Payment payment = paymentService.getById(Payment.class, paymentId);
							int numOverdueDays = 0;
							if (payment.getNumPenaltyDays() != null) {
								numOverdueDays = payment.getNumPenaltyDays();
							}
							payment.setNumPenaltyDays(getInteger(txtNumPenaltyDays) + numOverdueDays);
							payment.setTiPaidAmount(payment.getTiPaidAmount() + getDouble(txtPenaltyAmount));
	    					Cashflow cashflow = payment.getCashflows().get(0);	
	    					Cashflow overdueCashflow = CashflowUtils.createCashflow(cashflow.getProductLineType(), cashflow.getProductLine(),
	    							null, cashflow.getContract(), cashflow.getVatValue(),
	    							ECashflowType.PEN, ETreasuryType.APP, cashflow.getJournalEvent(), cashflow.getPaymentMethod(), 
	    							getDouble(txtPenaltyAmount), 0d, getDouble(txtPenaltyAmount),
	    							cashflow.getInstallmentDate(), cashflow.getPeriodStartDate(), cashflow.getPeriodEndDate(), cashflow.getNumInstallment());
	    					overdueCashflow.setPaid(true);
	    					overdueCashflow.setConfirm(cashflow.isConfirm());
	    					overdueCashflow.setPayment(payment);
	    					cashflowService.saveOrUpdate(overdueCashflow);
	    					payment.getCashflows().add(overdueCashflow);
	    					paymentService.saveOrUpdate(payment);
	    					winAddService.close();
	    					saveClicklistener.buttonClick(null);
						} else {
							MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
									MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
									new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
						}
					}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAddService.close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
        contentLayout.addComponent(formLayout);
        winAddService.setContent(contentLayout);
        winAddService.setWidth(400, Unit.PIXELS);
        winAddService.setHeight(250, Unit.PIXELS);
        UI.getCurrent().addWindow(winAddService);
	}
	
	/**
	 * @param saveClicklistener the saveClicklistener to set
	 */
	public void setSaveClicklistener(ClickListener saveClicklistener) {
		this.saveClicklistener = saveClicklistener;
	}
	
	/**
	 * 
	 * @param payment
	 */
	public void assignValues(Payment payment) {
		
		if (payment != null) {
			paymentId = payment.getId();
		}
	}
	
	private boolean validate() {
		boolean isValide = true;
		if (txtNumPenaltyDays.getValue().equals("") || txtPenaltyAmount.getValue().equals("")) {
			isValide = false;
		}
		return isValide;
	}
	
	protected Integer getInteger(AbstractTextField field) {
		try {
			return Integer.parseInt(field.getValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private Double getDouble(AbstractTextField field) {
		try {
			return Double.parseDouble(field.getValue());
		} catch (NumberFormatException e) {
			return 0d;
		}
	}
}
