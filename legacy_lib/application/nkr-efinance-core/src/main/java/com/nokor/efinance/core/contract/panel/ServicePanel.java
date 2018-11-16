package com.nokor.efinance.core.contract.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 */
public class ServicePanel extends AbstractTabPanel implements FinServicesHelper{
	
	/** */
	private static final long serialVersionUID = -2456724092864285465L;
	
	private TextField txtODAmount;
	private TextField txtCurrentPenalty;
	private TextField txtPressingFee;
	private TextField txtFollowingFee;
	private TextField txtTotal;
	
	
	/**
	 * 
	 */
	public ServicePanel() {
		
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {		
		txtODAmount = ComponentFactory.getTextField("",false, 60, 90);
        txtCurrentPenalty = ComponentFactory.getTextField("",false, 60, 90);
        txtPressingFee = ComponentFactory.getTextField("",false, 60, 90);
        txtFollowingFee = ComponentFactory.getTextField("",false, 60, 90);
        txtTotal = ComponentFactory.getTextField("",false, 60, 90);
        txtODAmount.setEnabled(false);
        txtCurrentPenalty.setEnabled(false);
        txtPressingFee.setEnabled(false);
        txtFollowingFee.setEnabled(false);
        txtTotal.setEnabled(false);
        
		final String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:0\" >";
		final String OPEN_TR = "<tr>";
		final String CLOSE_TR = "</tr>";
		final String OPEN_TD = "<td class=\"align-left\" >";
		final String CLOSE_TD = "</td>";
		final String CLOSE_TABLE = "</table>";
		
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblODAmount\" />");
		locations.add("<div location =\"txtODAmount\" />");
		locations.add("<div location =\"lblCurrentPenalty\" />");
		locations.add("<div location =\"txtCurrentPenalty\" />");
		locations.add("<div location =\"lblPressingFee\" />");
		locations.add("<div location =\"txtPressingFee\" />");
		locations.add("<div location =\"lblFollowingFee\" />");
		locations.add("<div location =\"txtFollowingFee\" />");
		locations.add("<div location =\"lblTotal\" />");
		locations.add("<div location =\"txtTotal\" />");
		CustomLayout feeAmountCustomLayout = new CustomLayout("xxx");
		String feeAmountTemplate = OPEN_TABLE;
		feeAmountTemplate += OPEN_TR;
		for (String string : locations) {
			feeAmountTemplate += OPEN_TD;
			feeAmountTemplate += string;
			feeAmountTemplate += CLOSE_TD;
		}
		feeAmountTemplate += CLOSE_TR;
		feeAmountTemplate += CLOSE_TABLE;
		
		feeAmountCustomLayout.addComponent(new Label(I18N.message("od.amount")), "lblODAmount");
		feeAmountCustomLayout.addComponent(new Label(I18N.message("current.penalty")), "lblCurrentPenalty");
		feeAmountCustomLayout.addComponent(new Label(I18N.message("pressing.fee")), "lblPressingFee");
		feeAmountCustomLayout.addComponent(new Label(I18N.message("following.fee")), "lblFollowingFee");
		feeAmountCustomLayout.addComponent(new Label(I18N.message("total.amountabcd")), "lblTotal");
		feeAmountCustomLayout.addComponent(txtODAmount, "txtODAmount");
		feeAmountCustomLayout.addComponent(txtCurrentPenalty, "txtCurrentPenalty");
		feeAmountCustomLayout.addComponent(txtPressingFee, "txtPressingFee");
		feeAmountCustomLayout.addComponent(txtFollowingFee, "txtFollowingFee");
		feeAmountCustomLayout.addComponent(txtTotal, "txtTotal");
		feeAmountCustomLayout.setTemplateContents(feeAmountTemplate);
		HorizontalLayout contentLayout = ComponentFactory.getHorizontalLayout();
		contentLayout.addComponent(feeAmountCustomLayout);
		return contentLayout;
	}	
		
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			
		}
	}
	
		/**
	 * Reset controls
	 */
	public void reset() {
		removeErrorsPanel();
	
	}

}
