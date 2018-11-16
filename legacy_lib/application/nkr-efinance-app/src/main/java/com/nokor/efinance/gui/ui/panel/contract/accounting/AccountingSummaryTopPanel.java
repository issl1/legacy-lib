package com.nokor.efinance.gui.ui.panel.contract.accounting;

import java.io.Serializable;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountingSummaryTopPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -6811170477415458269L;
	
	private String firstDueDate;
	private String lastDueDate;
	private double lastPaidInstallment;
	private double directIncome;
	private double directCost;
	private double effectiveRate;
	private double netEffectiveRate;
	private double flatRate;
	private double balanceAR;
	private double balanceInterest;
	private double balancePrinceple;
	private double balanceVat;
	private double balanceFeesPenalty;
	private int totalDueToDate;
	private double interestCollected;
	private double princepleCollected;
	private double vatCollected;
	private double feesPenaltyCollected;
	private double othersCollected;
	private double totalExpenses;
	private double totalIncome;
	private double netAmount;
	private double CurrentROI;
	private double accruedInterests;
	private double  accruedVat;
	private double odm;
	
	/**
	 * 
	 */
	public void assignValues() {
		AccountingItems[] items = {
			new AccountingItems(I18N.message("first.due.date"), getDescription()),
			new AccountingItems(I18N.message("last.due.date"), getDescription()),
			new AccountingItems(I18N.message("last.paid.installment"), getDescription()),
			new AccountingItems(I18N.message("direct.income"), getDescription()),
			new AccountingItems(I18N.message("direct.cost"), getDescription()),
			new AccountingItems(I18N.message("effective.rate"), getDescription()),
			new AccountingItems(I18N.message("net.effective.rate"), getDescription()),
			new AccountingItems(I18N.message("flat.rate"), getDescription()),
			new AccountingItems(I18N.message("balance.ar"), getDescription()),
			new AccountingItems(I18N.message("balance.interest"), getDescription()),
			new AccountingItems(I18N.message("balance.princeple"), getDescription()),
			new AccountingItems(I18N.message("balance.vat"), getDescription()),
			new AccountingItems(I18N.message("balance.fees.penalty"), getDescription()),
			new AccountingItems(I18N.message("total.due.to.date"), getDescription()),
			new AccountingItems(I18N.message("interest.collected"), getDescription()),
			new AccountingItems(I18N.message("princeple.collected"), getDescription()),
			new AccountingItems(I18N.message("vat.collected"), getDescription()),
			new AccountingItems(I18N.message("fees.penalty.collected"), getDescription()),
			new AccountingItems(I18N.message("others.collected"), getDescription()),
			new AccountingItems(I18N.message("total.expenses"), getDescription()),
			new AccountingItems(I18N.message("total.income"), getDescription()),
			new AccountingItems(I18N.message("net.amount"), getDescription()),
			new AccountingItems(I18N.message("current.roi"), getDescription()),
			new AccountingItems(I18N.message("accrued.interests"), getDescription()),
			new AccountingItems(I18N.message("accrued.vat"), getDescription()),
			new AccountingItems(I18N.message("odm"), getDescription())
		};
		
		String template = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\">"
				+ "<th width=\"" + 500 + "\"><b>" + I18N.message("item") + "</b></th>"
				+ "<th width=\"" + 500 + "\"><b>" + I18N.message("value") + "</b></th>"
				+ "</tr>";
		
		for (int i = 0; i < items.length; i++) {
			String location0 = " location=\"lblItem" + i;
			String location1 = " location=\"lblValue" + i;
			template += "<tr><td" + location0 + "\"></td><td " + location1 + "\"></td></tr>";
		}
		template += "</table>";
		
		CustomLayout tableLayout = new CustomLayout();
		tableLayout.setTemplateContents(template);
		
		for (int i = 0; i < items.length; i++) {
			AccountingItems item = items[i];
			tableLayout.addComponent(new Label(item.item), "lblItem" + i);
			tableLayout.addComponent(new Label(item.value), "lblValue" + i);
		}
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		removeAllComponents();
		addComponent(new Panel(content));
	}
	
	private class AccountingItems implements Serializable {
		
		/** */
		private static final long serialVersionUID = 3229759442647819416L;
		
		private String item;
		private String value;
		
		/**
		 * 
		 * @param item
		 * @param value
		 */
		public AccountingItems(String item, String value) {
			this.item = item;
			this.value = value;
		}
	}

}
