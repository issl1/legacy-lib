package com.nokor.efinance.core.quotation.panel.history;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * Operate suspense layout for lock split history
 * @author uhout.cheng
 */
public class OperateSuspensePopupWindow extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -2453373932168518391L;
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String CLOSE_TABLE = "</table>";
	private static String OPEN_TR = "<tr>";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String OPEN_TD = "<td>";
	private static String CLOSE_TD = "</td>";
	
	/** */
	public OperateSuspensePopupWindow() {
		addComponent(createOperateSuspenseTableLayout());
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createOperateSuspenseTableLayout(){
		CustomLayout operateSuspenseCustomLayoutTop = new CustomLayout("xxx");
		String operateSuspenseTemplateTop = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\" >";
		operateSuspenseTemplateTop += OPEN_TR;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"lblPayDate\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<td width=\"5px\" >";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"dfPayDate\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += CLOSE_TR;
		
		operateSuspenseTemplateTop += OPEN_TR;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"lblDeadLine\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<td width=\"5px\" >";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"dfDeadLine\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += CLOSE_TR;
		
		operateSuspenseTemplateTop += OPEN_TR;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"lblLockSplitReference\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<td width=\"5px\" >";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"txtLockSplitReference\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += CLOSE_TR;
		
		operateSuspenseTemplateTop += OPEN_TR;
		operateSuspenseTemplateTop += OPEN_TD;
		operateSuspenseTemplateTop += "<div location =\"cbCurrentDue\" />";
		operateSuspenseTemplateTop += CLOSE_TD;
		operateSuspenseTemplateTop += CLOSE_TR;
		operateSuspenseTemplateTop += CLOSE_TABLE;
		
		AutoDateField dfPayDate = ComponentFactory.getAutoDateField();
		AutoDateField dfDeadLine = ComponentFactory.getAutoDateField();
		TextField txtLockSplitReference = ComponentFactory.getTextField(60, 180);
		CheckBox cbCurrentDue = new CheckBox(I18N.message("current.due"));
		
		operateSuspenseCustomLayoutTop.addComponent(new Label(I18N.message("pay.date")), "lblPayDate");
		operateSuspenseCustomLayoutTop.addComponent(dfPayDate, "dfPayDate");
		operateSuspenseCustomLayoutTop.addComponent(new Label(I18N.message("dead.line")), "lblDeadLine");
		operateSuspenseCustomLayoutTop.addComponent(dfDeadLine, "dfDeadLine");
		operateSuspenseCustomLayoutTop.addComponent(new Label(I18N.message("lock.split.reference")), "lblLockSplitReference");
		operateSuspenseCustomLayoutTop.addComponent(txtLockSplitReference, "txtLockSplitReference");
		operateSuspenseCustomLayoutTop.addComponent(cbCurrentDue, "cbCurrentDue");
		
		CustomLayout operateSuspenseCustomLayoutTable = new CustomLayout("xxx");
		String operateSuspenseTemplateTable = OPEN_TABLE;
		operateSuspenseTemplateTable += OPEN_TR;
		operateSuspenseTemplateTable += "<th class=\"align-center\" width=\"250px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black; border-collapse:collapse;\" >";
		operateSuspenseTemplateTable += "<div location =\"lblAccruedList\" />";
		operateSuspenseTemplateTable += CLOSE_TH;
		operateSuspenseTemplateTable += "<th class=\"align-center\" width=\"180px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black; border-collapse:collapse;\" >";
		operateSuspenseTemplateTable += "<div location =\"lblAmountInVat\" />";
		operateSuspenseTemplateTable += CLOSE_TH;
		operateSuspenseTemplateTable += "<th class=\"align-center\" width=\"30px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black; border-collapse:collapse;\" >";
		operateSuspenseTemplateTable += "<div location =\"lblPaymentPriority\" />";
		operateSuspenseTemplateTable += CLOSE_TH;
		operateSuspenseTemplateTable += CLOSE_TR;
		
		operateSuspenseCustomLayoutTable.addComponent(new Label(I18N.message("accrued.list")), "lblAccruedList");
		operateSuspenseCustomLayoutTable.addComponent(new Label(I18N.message("amount.in.vat")), "lblAmountInVat");
		operateSuspenseCustomLayoutTable.addComponent(new Label(I18N.message("payment.priority")), "lblPaymentPriority");
		
		for (int i = 0; i < 8; i++) {
			operateSuspenseTemplateTable += OPEN_TR;
			operateSuspenseTemplateTable += "<td class=\"align-left\" style=\"border:1px solid black;\" >";
			operateSuspenseTemplateTable += "<div location =\"lblAccruedListValue" + i + "\" />";
			operateSuspenseTemplateTable += CLOSE_TD;
			operateSuspenseTemplateTable += "<td class=\"align-left\" style=\"border:1px solid black;\" >";
			operateSuspenseTemplateTable += "<div location =\"txtAmountInVatValue" + i + "\" />";
			operateSuspenseTemplateTable += CLOSE_TD;
			operateSuspenseTemplateTable += "<td class=\"align-left\" style=\"border:1px solid black;\" >";
			operateSuspenseTemplateTable += "<div location =\"cbxPaymentPriorityValue" + i + "\" />";
			operateSuspenseTemplateTable += CLOSE_TD;
			operateSuspenseTemplateTable += CLOSE_TR;
			
			Label lblAccruedListValue = ComponentFactory.getLabel();
			TextField txtAmountInVatValue = ComponentFactory.getTextField(30, 170);
			ComboBox cbxPaymentPriorityValue = ComponentFactory.getComboBox();
			cbxPaymentPriorityValue.setWidth(100, Unit.PIXELS);
			
			lblAccruedListValue.setValue(getDefaultString(i));
			txtAmountInVatValue.setValue(getDefaultString(i));
			cbxPaymentPriorityValue.addItem(i);
			
			operateSuspenseCustomLayoutTable.addComponent(lblAccruedListValue, "lblAccruedListValue" + i);
			operateSuspenseCustomLayoutTable.addComponent(txtAmountInVatValue, "txtAmountInVatValue" + i);
			operateSuspenseCustomLayoutTable.addComponent(cbxPaymentPriorityValue, "cbxPaymentPriorityValue" + i);
		}
		
		operateSuspenseTemplateTable += CLOSE_TABLE;
		
		CustomLayout operateSuspenseCustomLayoutTotal = new CustomLayout("xxx");
		String operateSuspenseTemplateTotal = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\" >";
		operateSuspenseTemplateTotal += OPEN_TR;
		operateSuspenseTemplateTotal += OPEN_TD;
		operateSuspenseTemplateTotal += "<div location =\"lblTotalAmountOfPayment\" />";
		operateSuspenseTemplateTotal += CLOSE_TD;
		operateSuspenseTemplateTotal += OPEN_TD;
		operateSuspenseTemplateTotal += "<td width=\"5px\" >";
		operateSuspenseTemplateTotal += CLOSE_TD;
		operateSuspenseTemplateTotal += OPEN_TD;
		operateSuspenseTemplateTotal += "<div location =\"txtTotalAmountOfPayment\" />";
		operateSuspenseTemplateTotal += CLOSE_TD;
		operateSuspenseTemplateTotal += CLOSE_TR;
		operateSuspenseTemplateTotal += CLOSE_TABLE;
		
		TextField txtTotalAmountOfPayment = ComponentFactory.getTextField(60, 200);
		operateSuspenseCustomLayoutTotal.addComponent(new Label(I18N.message("total.amount.of.payment")), "lblTotalAmountOfPayment");
		operateSuspenseCustomLayoutTotal.addComponent(txtTotalAmountOfPayment, "txtTotalAmountOfPayment");
		
		operateSuspenseCustomLayoutTop.setTemplateContents(operateSuspenseTemplateTop);
		operateSuspenseCustomLayoutTable.setTemplateContents(operateSuspenseTemplateTable);
		operateSuspenseCustomLayoutTotal.setTemplateContents(operateSuspenseTemplateTotal);
		
		VerticalLayout layout = ComponentFactory.getVerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(operateSuspenseCustomLayoutTop);
		layout.addComponent(operateSuspenseCustomLayoutTable);
		layout.addComponent(operateSuspenseCustomLayoutTotal);
		
		return layout;
	}
}
