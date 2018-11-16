package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.closing;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneClosingLoanPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 3999665972802713542L;

	private AutoDateField dfPayOfDate;
	
	private TextField txtBalanceLoan;
	private TextField txtBalancePenaltyFee;
	private TextField txtClosingFee;
	private TextField txtAdditionalFee;
	private TextField txtSuspendedPayment;
	private TextField txtAdvances;
	private TextField txtEarlyDiscount;
	private TextField txtAdditionalDiscount;
	
	private Button btnOk;
	private Button btnAdd;
	
	private Label lblTotalToPayValue;
	
	/**
	 * 
	 */
	public ColPhoneClosingLoanPanel() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		dfPayOfDate = ComponentFactory.getAutoDateField();
		dfPayOfDate.setWidth(200, Unit.PIXELS);
		txtBalanceLoan = ComponentFactory.getTextField(false, 100, 200);
		txtBalancePenaltyFee = ComponentFactory.getTextField(false, 100, 200);
		txtClosingFee = ComponentFactory.getTextField(false, 100, 200);
		txtAdditionalFee = ComponentFactory.getTextField(false, 100, 200);
		txtSuspendedPayment = ComponentFactory.getTextField(false, 100, 200);
		txtAdvances = ComponentFactory.getTextField(false, 100, 200);
		txtEarlyDiscount = ComponentFactory.getTextField(false, 100, 200);
		txtAdditionalDiscount = ComponentFactory.getTextField(false, 100, 200);
		
		btnOk = new NativeButton(I18N.message("ok"), this);
		btnOk.setStyleName("btn btn-success button-small");
		btnOk.setWidth(40, Unit.PIXELS);
		
		btnAdd = new Button();
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.setStyleName(Reindeer.BUTTON_LINK);
		
		lblTotalToPayValue = getLabelValue();
		Label lblPayOfDateTitle = ComponentFactory.getLabel("pay.of.date");
		Label lblBalanceLoanTitle = ComponentFactory.getLabel("balance.loan");
		Label lblBalancePenaltyFeeTitle = ComponentFactory.getLabel("balance.penalty.fee");
		Label lblClosingFeeTitle = ComponentFactory.getLabel("closing.fee");
		Label lblAdditionalFeeTitle = ComponentFactory.getLabel("additional.fee");
		Label lblSuspendedPaymentTitle = ComponentFactory.getLabel("suspended.payment");
		Label lblAdvancesTitle = ComponentFactory.getLabel("advances");
		Label lblEarlyDiscountTitle = ComponentFactory.getLabel("early.discount");
		Label lblAdditionalDiscountTitle = ComponentFactory.getLabel("additional.discount");
		Label lblTotalToPayTitle = getLabel("total.to.pay");
		
		GridLayout leftLayout = new GridLayout(4, 5);
		leftLayout.setMargin(true);
		leftLayout.setSpacing(true);
		
		int iCol = 0;
		leftLayout.addComponent(lblPayOfDateTitle, iCol++, 0);
		leftLayout.addComponent(dfPayOfDate, iCol++, 0);
		leftLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCol++, 0);
		leftLayout.addComponent(btnOk, iCol++, 0);
		
		iCol = 0;
		leftLayout.addComponent(lblBalanceLoanTitle, iCol++, 1);
		leftLayout.addComponent(txtBalanceLoan, iCol++, 1);
		
		
		iCol = 0;
		leftLayout.addComponent(lblBalancePenaltyFeeTitle, iCol++, 2);
		leftLayout.addComponent(txtBalancePenaltyFee, iCol++, 2);
		leftLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCol++, 2);
		leftLayout.addComponent(btnAdd, iCol++, 2);
		
		iCol = 0;
		leftLayout.addComponent(lblClosingFeeTitle, iCol++, 3);
		leftLayout.addComponent(txtClosingFee, iCol++, 3);
		
		iCol = 0;
		leftLayout.addComponent(lblAdditionalFeeTitle, iCol++, 4);
		leftLayout.addComponent(txtAdditionalFee, iCol++, 4);
		
		leftLayout.setComponentAlignment(lblPayOfDateTitle, Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(lblBalanceLoanTitle, Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(lblBalancePenaltyFeeTitle, Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(lblClosingFeeTitle, Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(lblAdditionalFeeTitle, Alignment.MIDDLE_LEFT);
		
		GridLayout rightLayout = new GridLayout(2, 4);
		rightLayout.setMargin(true);
		rightLayout.setSpacing(true);
		
		rightLayout.addComponent(lblSuspendedPaymentTitle, 0, 0);
		rightLayout.addComponent(txtSuspendedPayment, 1, 0);
		rightLayout.addComponent(lblAdvancesTitle, 0, 1);
		rightLayout.addComponent(txtAdvances, 1, 1);
		rightLayout.addComponent(lblEarlyDiscountTitle, 0, 2);
		rightLayout.addComponent(txtEarlyDiscount, 1, 2);
		rightLayout.addComponent(lblAdditionalDiscountTitle, 0, 3);
		rightLayout.addComponent(txtAdditionalDiscount, 1, 3);
		
		rightLayout.setComponentAlignment(lblSuspendedPaymentTitle, Alignment.MIDDLE_LEFT);
		rightLayout.setComponentAlignment(lblAdvancesTitle, Alignment.MIDDLE_LEFT);
		rightLayout.setComponentAlignment(lblEarlyDiscountTitle, Alignment.MIDDLE_LEFT);
		rightLayout.setComponentAlignment(lblAdditionalDiscountTitle, Alignment.MIDDLE_LEFT);
		
		GridLayout bellowLayout = new GridLayout(2, 1);
		bellowLayout.setMargin(true);
		bellowLayout.setSpacing(true);
		
		bellowLayout.addComponent(lblTotalToPayTitle, 0, 0);
		bellowLayout.addComponent(lblTotalToPayValue, 1, 0);
		
		HorizontalLayout middleLayout = new HorizontalLayout();
		middleLayout.setMargin(true);
		middleLayout.setSpacing(true);
		
		middleLayout.addComponent(leftLayout);
		middleLayout.addComponent(rightLayout);
		
		middleLayout.setComponentAlignment(rightLayout, Alignment.TOP_RIGHT);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(middleLayout);
		mainLayout.addComponent(bellowLayout);
		
		addComponent(mainLayout);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
		
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	@Override
	protected void reset() {
		super.reset();
		dfPayOfDate.setValue(null);
		txtBalanceLoan.setValue(StringUtils.EMPTY);
		txtBalancePenaltyFee.setValue(StringUtils.EMPTY);
		txtClosingFee.setValue(StringUtils.EMPTY);
		txtAdditionalFee.setValue(StringUtils.EMPTY);
		txtSuspendedPayment.setValue(StringUtils.EMPTY);
		txtAdvances.setValue(StringUtils.EMPTY);
		txtAdditionalFee.setValue(StringUtils.EMPTY);
		txtEarlyDiscount.setValue(StringUtils.EMPTY);
		txtAdditionalDiscount.setValue(StringUtils.EMPTY);
		lblTotalToPayValue.setValue(StringUtils.EMPTY);
	}

}
