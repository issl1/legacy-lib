package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;


/**
 * Highlight layout in collection phone staff
 * @author uhout.cheng
 */
public class CollectionHighlightsPanel extends AbstractControlPanel implements MCollection {

	/** */
	private static final long serialVersionUID = -9024768396455322002L;

	private Label lblBalanceLoan;
	private Label lblBalanceOther;
	private Label lblBalanceRepossessionFee;
	private Label lblBalanceCollectionFee;
	private Label lblBalancePenaltyIns;
	private Label lblTermPaid;
	private Label lblRemainingTerm;
	
	private PaymentsReceivedTablePanel paymentReceivedTablePanel;
	private CollectionLockSplitsPanel lockSplitsPanel;
	
	/**
	 * @return the lockSplitsPanel
	 */
	public CollectionLockSplitsPanel getLockSplitsPanel() {
		return lockSplitsPanel;
	}

	/**
	 * 
	 */
	public CollectionHighlightsPanel() {
		setMargin(true);
		setSpacing(true);
		setWidth(525, Unit.PIXELS);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		lblTermPaid = getLabelValue();
		lblBalanceRepossessionFee = getLabelValue();
		lblBalanceLoan = getLabelValue();
		lblBalancePenaltyIns = getLabelValue();
		lblBalanceCollectionFee = getLabelValue();
		lblBalanceOther = getLabelValue();
		lblRemainingTerm = getLabelValue();
		
		paymentReceivedTablePanel = new PaymentsReceivedTablePanel();
		lockSplitsPanel = new CollectionLockSplitsPanel();
	
		GridLayout leftGrid = getLeftLayout();
		GridLayout rightGrid = getRightLayout();
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.setWidth(525, Unit.PIXELS);
		horLayout.addComponent(leftGrid);
		horLayout.addComponent(rightGrid);
		horLayout.setComponentAlignment(rightGrid, Alignment.TOP_RIGHT);
		
		addComponent(paymentReceivedTablePanel);
		addComponent(lockSplitsPanel);
		addComponent(horLayout);
	}
	
	/**
	 * Left grid layout
	 * @return
	 */
	private GridLayout getLeftLayout() {
		GridLayout gridLayoutLeft = new GridLayout(10, 5);
		gridLayoutLeft.setSpacing(true);
		
		Label lblBalanceLoanTitle = getLabelCaption("balance.loan");
		Label lblBalanceRepossessionFeeTitle = getLabelCaption("balance.repossession.fee");
		Label lblBalanceOtherTitle = getLabelCaption("balance.others");
		
		int iCol = 0;
		gridLayoutLeft.addComponent(lblBalanceLoanTitle, iCol++, 0);
		gridLayoutLeft.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayoutLeft.addComponent(lblBalanceLoan, iCol++, 0);
		iCol = 0;
		gridLayoutLeft.addComponent(lblBalanceOtherTitle, iCol++, 1);
		gridLayoutLeft.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayoutLeft.addComponent(lblBalanceOther, iCol++, 1);
		iCol = 0;
		gridLayoutLeft.addComponent(lblBalanceRepossessionFeeTitle, iCol++, 2);
		gridLayoutLeft.addComponent(getLabelCaption(":"), iCol++, 2);
		gridLayoutLeft.addComponent(lblBalanceRepossessionFee, iCol++, 2);
		
		gridLayoutLeft.setComponentAlignment(lblBalanceLoanTitle, Alignment.TOP_RIGHT);
		gridLayoutLeft.setComponentAlignment(lblBalanceRepossessionFeeTitle, Alignment.TOP_RIGHT);
		gridLayoutLeft.setComponentAlignment(lblBalanceOtherTitle, Alignment.TOP_RIGHT);
		return gridLayoutLeft;
	}
	
	/**
	 * Right grid layout
	 * @return
	 */
	private GridLayout getRightLayout() {
		GridLayout gridLayoutRight = new GridLayout(10, 5);
		gridLayoutRight.setSpacing(true);
		
		Label lblBalancePenaltyTitle = getLabelCaption("balance.penalty");
		Label lblBalanceCollectionFeeTitle = getLabelCaption("balance.collection.fee");
		Label lblTermPaidTitle = getLabelCaption("terms.paid");
		Label lblRemainingTermTitle = getLabelCaption("remaining.term");
		
		int iCol = 0;
		gridLayoutRight.addComponent(lblBalanceCollectionFeeTitle, iCol++, 0);
		gridLayoutRight.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayoutRight.addComponent(lblBalanceCollectionFee, iCol++, 0);
		iCol = 0;
		gridLayoutRight.addComponent(lblBalancePenaltyTitle, iCol++, 1);
		gridLayoutRight.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayoutRight.addComponent(lblBalancePenaltyIns, iCol++, 1);
		iCol = 0;
		gridLayoutRight.addComponent(lblTermPaidTitle, iCol++, 2);
		gridLayoutRight.addComponent(getLabelCaption(":"), iCol++, 2);
		gridLayoutRight.addComponent(lblTermPaid, iCol++, 2);
		iCol = 0;
		gridLayoutRight.addComponent(lblRemainingTermTitle, iCol++, 3);
		gridLayoutRight.addComponent(getLabelCaption(":"), iCol++, 3);
		gridLayoutRight.addComponent(lblRemainingTerm, iCol++, 3);
		
		gridLayoutRight.setComponentAlignment(lblBalancePenaltyTitle, Alignment.TOP_RIGHT);
		gridLayoutRight.setComponentAlignment(lblBalanceCollectionFeeTitle, Alignment.TOP_RIGHT);
		gridLayoutRight.setComponentAlignment(lblTermPaidTitle, Alignment.TOP_RIGHT);
		gridLayoutRight.setComponentAlignment(lblRemainingTermTitle, Alignment.TOP_RIGHT);
		return gridLayoutRight;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(null);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		Collection collection = contract.getCollection();
		int currentTerm = MyNumberUtils.getInteger(collection.getCurrentTerm());
		if (currentTerm > 1) {
			lblRemainingTerm.setValue(getDescription(getDefaultString((contract.getTerm() - (currentTerm - 1)))));
		} else if (currentTerm == 1) {
			lblRemainingTerm.setValue(getDescription(getDefaultString(contract.getTerm())));
		} 
		if (collection != null) {
			lblTermPaid.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(collection.getNbInstallmentsPaid()))));
			lblBalanceLoan.setValue(getDescription("0.00"));
			lblBalancePenaltyIns.setValue(getDescription("0.00"));
			lblBalanceCollectionFee.setValue(getDescription("0.00"));
			lblBalanceRepossessionFee.setValue(getDescription("0.00"));
			lblBalanceOther.setValue(getDescription("0.00"));
		}
		paymentReceivedTablePanel.assignValues(contract);
		lockSplitsPanel.assignValues(contract);
	}
	
	/**
	 * 
	 */
	protected void reset() {
		lblTermPaid.setValue(getDescription("0"));
		lblBalanceRepossessionFee.setValue(getDescription("0"));
		lblBalanceLoan.setValue(getDescription("0.00"));
		lblBalancePenaltyIns.setValue(getDescription("0.00"));
		lblBalanceCollectionFee.setValue(getDescription("0.00"));
		lblRemainingTerm.setValue(getDescription("0"));
		lblBalanceOther.setValue(getDescription("0.00"));
		lblRemainingTerm.setValue(getDescription("N/A"));
	}
	
}
