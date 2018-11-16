package com.nokor.efinance.gui.ui.panel.contract.loan.summary;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;


/**
 * Loan top panel in summary tab
 * @author uhout.cheng
 */
public class LoanApplicationContractPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 8373307338296937599L;

	private static final String TEMPLATE = "loan/summary/loanApplicationContractLayout";
	
	private LoanSummaryPanel loanSummaryPanel;
	
	private Label lblContractStatusValue;
	private Label lblBalanceLoanValue;
//	private Label lblDPDODMValue;
	private Label lblPaidRemineTermValue;
	private Label lblNextDueDateValue;
	private Label lblDueDateValue;
	private Label lblODIValue;
	private Label lblAmountDueValue;
	private Label lblDueInstallmentValue;
	private Label lblPayoffAmountValue;
	private Label lblLatestPaymentDateValue;
	private Label lblDueOthersValue;
	
	private Label lblDPDValue;
	private Label lblODMValue;
	private Label lblMPDValue;
	
//	private Label lblContractStartDateValue;
//	private Label lblBorrowerStatusValue;
//	private Label lblBalanceOtherDueValue;
//	private Label lblLastPaymentDateValue;
	
	/**
	 * 
	 * @param loanSummaryPanel
	 */
	public LoanApplicationContractPanel(LoanSummaryPanel loanSummaryPanel) {
		this.loanSummaryPanel = loanSummaryPanel;
		init();
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "";
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = ComponentFactory.getLabel(null, ContentMode.HTML);
		label.setSizeUndefined();
		return label;
		
	}
	
	/**
	 * 
	 */
	private void init() {
//		lblContractStartDateValue = getLabel();
//		lblLastPaymentDateValue = getLabel();
//		lblRemainingTermValue = getLabel();
//		lblPaymentPatternValue = getLabel();
//		lblBorrowerStatusValue = getLabel();
//		lblBalancePenaltyValue = getLabel();
//		lblBalanceOtherDueValue = getLabel();
//		lblAPDInstallmentValue = getLabel();
//		lblPayOffAmountValue = getLabel();
//		lblAPDAllValue = getLabel();
		
		lblNextDueDateValue = getLabel();
		lblContractStatusValue = getLabel();
		lblBalanceLoanValue = getLabel();
		//lblDPDODMValue = getLabel();
		lblPaidRemineTermValue = getLabel();
		lblDueDateValue = getLabel();
		lblODIValue = getLabel();
		lblAmountDueValue = getLabel();
		lblDueInstallmentValue = getLabel();
		lblPayoffAmountValue = getLabel();
		lblLatestPaymentDateValue = getLabel();
		lblDueOthersValue = getLabel();
		
		lblDPDValue = getLabel();
		lblODMValue = getLabel();
		lblMPDValue = getLabel();
		
		Panel panel = loanSummaryPanel.getPanelCaptionColor("loan", getCustomLayout(), true);
		addComponent(panel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assigValues(Contract contract) {
		reset();
		Collection collection = contract.getCollection();
		lblContractStatusValue.setValue(getDescription(contract.getWkfStatus().getDescLocale()));
		
		double[] contractDueAmount = getContractDueAmount(contract.getId());
		double dueInstallment = contractDueAmount[0];
		double dueOther = contractDueAmount[1];
		double dueAmount = contractDueAmount[2];
		
		lblDueInstallmentValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(dueInstallment, "###,##0.00")));
		lblDueOthersValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(dueOther, "###,##0.00")));
		lblAmountDueValue.setValue(getDescription(MyNumberUtils.formatDoubleToString(dueAmount, "###,##0.00")));
				
		if (collection != null) {
			
			String balanceInstallment = AmountUtils.format(MyNumberUtils.getDouble(collection.getTiBalanceCapital()) + MyNumberUtils.getDouble(collection.getTiBalanceInterest()));
			lblBalanceLoanValue.setValue(getDescription(balanceInstallment));
			
			String odm = getDefaultString(MyNumberUtils.getInteger(collection.getDebtLevel()));
			String odi = getDefaultString(MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue()));
			int dpd = MyNumberUtils.getInteger(collection.getNbOverdueInDays());
			
			Integer mpd = dpd / 30;
			
				
			lblNextDueDateValue.setValue(getDescription(getDateFormat(collection.getNextDueDate())));
			int installmentsPaid = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
			int remainingIns = contract.getTerm() - installmentsPaid;
			
			lblDPDValue.setValue(getDescription(getDefaultString(dpd)));
			lblODMValue.setValue(getDescription(odm));
			lblMPDValue.setValue(getDescription(getDefaultString(mpd)));
			
			lblPayoffAmountValue.setValue(getDescription("N/A"));
			lblPaidRemineTermValue.setValue(getDescription(getDefaultString(installmentsPaid + "/" + remainingIns)));
			lblODIValue.setValue(getDescription(getDefaultString(odi)));
			
			lblLatestPaymentDateValue.setValue(getDescription(getDateFormat(collection.getLastPaymentDate())));
			
//			lblAPDInstallmentValue.setValue(getDescription("N/A"));
//			lblRemainingTermValue.setValue(getDescription(getDefaultString(remainingIns)));
//			lblPaymentPatternValue.setValue(getDescription("N/A"));
//			lblBalancePenaltyValue.setValue(getDescription(AmountUtils.format(collection.getTiPenaltyAmount())));
//			lblAPDAllValue.setValue(getDescription("N/A"));
//			lblLastPaymentDateValue.setValue(getDescription(getDateFormat(collection.getLastPaymentDate())));
		}
		if (contract.getFirstDueDate() != null) {
			lblDueDateValue.setValue(getDescription(getDefaultString(DateUtils.getDay(contract.getFirstDueDate()))));
		} else {
			lblDueDateValue.setValue("");
		}
	}
	
	/**
	 * 
	 * @param contraId
	 * @return
	 */
	private double[] getContractDueAmount(Long cotraId) {
		double dueInstallment = 0d;
		double dueOther = 0d;
		double dueAmount = 0d;
		List<Cashflow> cashflows = CASHFLOW_SRV.getCashflowsToPaidLessThanToday(cotraId, DateUtils.today());
		if (cashflows != null && !cashflows.isEmpty()) {
			for (Cashflow cashflow : cashflows) {
				if (ECashflowType.CAP.equals(cashflow.getCashflowType()) || ECashflowType.IAP.equals(cashflow.getCashflowType())) {
					dueInstallment += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
				} else {
					dueOther += MyNumberUtils.getDouble(cashflow.getTiInstallmentAmount());
				}
			}
		}
		dueAmount = dueInstallment + dueOther;
		return new double[] { dueInstallment, dueOther, dueAmount };
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getCustomLayout() {
		CustomLayout customLayout = loanSummaryPanel.getCustomLayout(TEMPLATE);
		customLayout.addComponent(ComponentFactory.getLabel("contract.status"), "lblContractStatus");
		customLayout.addComponent(lblContractStatusValue, "lblContractStatusValue");
		customLayout.addComponent(ComponentFactory.getLabel("due.date"), "lblDueDate");
		customLayout.addComponent(lblDueDateValue, "lblDueDateValue");
		customLayout.addComponent(ComponentFactory.getLabel("balance.loan"), "lblBalanceLoan");
		customLayout.addComponent(lblBalanceLoanValue, "lblBalanceLoanValue");
		customLayout.addComponent(ComponentFactory.getLabel("paid.remaining.terms"), "lblPaidRemineTerm");
		customLayout.addComponent(lblPaidRemineTermValue, "lblPaidRemineTermValue");
		
		customLayout.addComponent(ComponentFactory.getLabel("dpd"), "lblDPD");
		customLayout.addComponent(lblDPDValue, "lblDPDValue");
		customLayout.addComponent(ComponentFactory.getLabel("odm"), "lblODM");
		customLayout.addComponent(lblODMValue, "lblODMValue");
		customLayout.addComponent(ComponentFactory.getLabel("mpd"), "lblMPD");
		customLayout.addComponent(lblMPDValue, "lblMPDValue");
		
		customLayout.addComponent(ComponentFactory.getLabel("odi"), "lblODI");
		customLayout.addComponent(lblODIValue, "lblODIValue");
		customLayout.addComponent(ComponentFactory.getLabel("amount.due"), "lblAmountDue");
		customLayout.addComponent(lblAmountDueValue, "lblAmountDueValue");
		customLayout.addComponent(ComponentFactory.getLabel("due.installment"), "lblDueInstallment");
		customLayout.addComponent(lblDueInstallmentValue, "lblDueInstallmentValue");
		customLayout.addComponent(ComponentFactory.getLabel("due.others"), "lblDueOthers");
		customLayout.addComponent(lblDueOthersValue, "lblDueOthersValue");
		customLayout.addComponent(ComponentFactory.getLabel("next.due.date"), "lblNextDueDate");
		customLayout.addComponent(lblNextDueDateValue, "lblNextDueDateValue");
		customLayout.addComponent(ComponentFactory.getLabel("latest.payment.date"), "lblLatestPaymentDate");
		customLayout.addComponent(lblLatestPaymentDateValue, "lblLatestPaymentDateValue");
		customLayout.addComponent(ComponentFactory.getLabel("payoff.amount"), "lblPayoffAmount");
		customLayout.addComponent(lblPayoffAmountValue, "lblPayoffAmountValue");

		return customLayout;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		/*if (event.getButton().equals(btnSeeAllContract)) {
			ContractInfosPopupWindow window = new ContractInfosPopupWindow(loanSummaryPanel, contract);
			UI.getCurrent().addWindow(window);
		}*/
	}
	
	/**
	 * 
	 */
	protected void reset() {
		lblContractStatusValue.setValue("");
		lblNextDueDateValue.setValue("");
		lblBalanceLoanValue.setValue("");
		lblPaidRemineTermValue.setValue("");
		lblDueDateValue.setValue("");
		lblODIValue.setValue("");
		lblAmountDueValue.setValue("");
		lblDueInstallmentValue.setValue("");
		lblPayoffAmountValue.setValue("");
		lblLatestPaymentDateValue.setValue("");
		lblDueOthersValue.setValue("");
		
		lblDPDValue.setValue("");
		lblODMValue.setValue("");
		lblMPDValue.setValue("");
		
//		lblContractStartDateValue.setValue("");
//		lblLastPaymentDateValue.setValue("");
//		lblBorrowerStatusValue.setValue("");
//		lblBalanceOtherDueValue.setValue("");
	}
	
}
