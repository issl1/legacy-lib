package com.nokor.efinance.gui.ui.panel.contract.accounting;

import java.io.Serializable;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountingSummaryBottomPanel extends AbstractControlPanel{

	/** */
	private static final long serialVersionUID = 2972752638924816838L;
	
	private String contractStatus;
	private String assetStatus;
	private String terminationDate;
	private String writeOffDate;
	private String foreClosureDate;
	private double assetForeClosedValue;
	private String saleDate;
	private double saleAmount;
	
	public void assignValues() {
		
		AccountingItems[] items = {
				new AccountingItems(I18N.message("contract.status"), getDescription()),
				new AccountingItems(I18N.message("asset.status"), getDescription()),
				new AccountingItems(I18N.message("termination.date"), getDescription()),
				new AccountingItems(I18N.message("write.off.date"), getDescription()),
				new AccountingItems(I18N.message("foreclosure.date"), getDescription()),
				new AccountingItems(I18N.message("asset.foreclosured.value"), getDescription()),
				new AccountingItems(I18N.message("sale.date"), getDescription()),
				new AccountingItems(I18N.message("sale.amount"), getDescription())
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
