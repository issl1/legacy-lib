package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;



/**
 * 
 * @author uhout.cheng
 */
public class LoanDetailTablePanel extends AbstractControlPanel implements FinServicesHelper, MBaseAddress {

	/** */
	private static final long serialVersionUID = 7652184009318961580L;
	
	
	/**
	 * 
	 */
	public LoanDetailTablePanel() {
		
	}
	
	/**
	 * 
	 */
	public void assignValues(Contract contract) {
		Cashflow commission = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.COMM);
		Amount commissionAmount = new Amount(0d, 0d, 0d);
		if (commission != null) {
			commissionAmount = new Amount(Math.abs(commission.getTeInstallmentAmount()), Math.abs(commission.getVatInstallmentAmount()), Math.abs(commission.getTiInstallmentAmount()));
		}
		
		Cashflow serviceFee = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.SRVFEE);
		Amount serviceFeeAmount = new Amount(0d, 0d, 0d);
		
		if (serviceFee != null) {
			serviceFeeAmount = new Amount(serviceFee.getTeInstallmentAmount(), serviceFee.getVatInstallmentAmount(), serviceFee.getTiInstallmentAmount());
		}
		
		Cashflow insuranceFee = CASHFLOW_SRV.getServiceTypeCashflowOfContract(contract, EServiceType.INSFEE);
		Amount insuranceFeeAmount = new Amount(0d, 0d, 0d);
		
		if (insuranceFee != null) {
			insuranceFeeAmount = new Amount(insuranceFee.getTeInstallmentAmount(), insuranceFee.getVatInstallmentAmount(), insuranceFee.getTiInstallmentAmount());
		}
		
		FinancialItem[] items = {
				new FinancialItem(I18N.message("down.payment"), new Amount(MyNumberUtils.getDouble(contract.getTeInvoiceAmount()) - MyNumberUtils.getDouble(contract.getTeFinancedAmount()), 
						MyNumberUtils.getDouble(contract.getVatInvoiceAmount()) - MyNumberUtils.getDouble(contract.getVatFinancedAmount()), 
						MyNumberUtils.getDouble(contract.getTiInvoiceAmount()) - MyNumberUtils.getDouble(contract.getTiFinancedAmount()))),
				new FinancialItem(I18N.message("finance.amount"), new Amount(contract.getTeFinancedAmount(), contract.getVatFinancedAmount(), contract.getTiFinancedAmount())),
				new FinancialItem(I18N.message("net.finance.amount"), contract.getNetFinanceAmount()),
				new FinancialItem(I18N.message("loan.amount"), contract.getLoanAmount()),
				new FinancialItem(I18N.message("installment"), new Amount(contract.getTeInstallmentAmount(), contract.getVatInstallmentAmount(), contract.getTiInstallmentAmount())),
				new FinancialItem(I18N.message("commission.1"), commissionAmount),
				new FinancialItem(I18N.message("commission.2"), new Amount(0d, 0d, 0d)),
				new FinancialItem(I18N.message("insurance.premium"), insuranceFeeAmount),
				new FinancialItem(I18N.message("service.fee"), serviceFeeAmount),
				new FinancialItem(I18N.message("subsidy"), new Amount(0d, 0d, 0d))
		};
		
		String template = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\">"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("item") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("ex.vat") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("vat.amount") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("in.vat") + "</b></th>"
				+ "</tr>";
		
		for (int i = 0; i < items.length; i++) {
			String location0 = " location=\"lblItem" + i;
			String location1 = " location=\"lblExVat" + i;
			String location2 = " location=\"lblVatAmount" + i;
			String location3 = " location=\"lblInVat" + i;
			template += "<tr><td" + location0 + "\"></td><td " + location1 + "\"></td><td " + location2 + "\"></td><td " + location3 + "\"></td></tr>";
		}
		template += "</table>";
		
		CustomLayout tableLayout = new CustomLayout();
		tableLayout.setTemplateContents(template);

		for (int i = 0; i < items.length; i++) {
			FinancialItem item = items[i];
			tableLayout.addComponent(new Label(item.label), "lblItem" + i);
			tableLayout.addComponent(new Label(AmountUtils.format(item.amount.getTeAmount())), "lblExVat" + i);
			tableLayout.addComponent(new Label(AmountUtils.format(item.amount.getVatAmount())), "lblVatAmount"+ i);
			tableLayout.addComponent(new Label(AmountUtils.format(item.amount.getTiAmount())), "lblInVat" + i);
		}

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		removeAllComponents();
		addComponent(new Panel(content));
	}
	
	private class FinancialItem implements Serializable {
		/**
		 */
		private static final long serialVersionUID = -4791427988900490680L;
		public String label;
		public Amount amount;
		
		/**
		 * @param label
		 * @param teAmount
		 * @param vatAmount
		 * @param tiAmount
		 */
		public FinancialItem(String label, Amount amount) {
			this.label = label;
			this.amount = amount;
		}
	}
}
