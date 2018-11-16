package com.nokor.efinance.gui.ui.panel.payment.blocked;

import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class RefundsLayout extends VerticalLayout implements ClickListener {

	/** */
	private static final long serialVersionUID = 8430186432228718218L;
	
	private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtBankName;
	private TextField txtBranch;
	private TextField txtAccountNO;
	private TextField txtAccountName;
	
	private Button btnUpdate;
	
	/**
	 * 
	 */
	public RefundsLayout() {
		cbxPaymentMethod = new ERefDataComboBox<EPaymentMethod>(EPaymentMethod.values());
		cbxPaymentMethod.setWidth(150, Unit.PIXELS);
		txtBankName = ComponentFactory.getTextField(false, 150, 150);
		txtBranch = ComponentFactory.getTextField(false, 150, 150);
		txtAccountNO = ComponentFactory.getTextField(false, 100, 150);
		txtAccountName = ComponentFactory.getTextField(false, 100, 150);
		
		btnUpdate = ComponentLayoutFactory.getButtonUpdate();
		btnUpdate.addClickListener(this);
		
		Label lblPaymentMethod = ComponentFactory.getLabel("payment.method");
		Label lblBankName = ComponentFactory.getLabel("bank.name");
		Label lblBranch = ComponentFactory.getLabel("branch");
		Label lblAccountNO = ComponentFactory.getLabel("account.no");
		Label lblAccountName = ComponentFactory.getLabel("account.name");
		
		GridLayout gridLayout = ComponentLayoutFactory.getGridLayout(6, 2);
		gridLayout.addComponent(lblPaymentMethod);
		gridLayout.addComponent(cbxPaymentMethod);
		gridLayout.addComponent(lblBankName);
		gridLayout.addComponent(txtBankName);
		gridLayout.addComponent(lblBranch);
		gridLayout.addComponent(txtBranch);
		
		gridLayout.addComponent(lblAccountNO);
		gridLayout.addComponent(txtAccountNO);
		gridLayout.addComponent(lblAccountName);
		gridLayout.addComponent(txtAccountName);
		
		gridLayout.setComponentAlignment(lblPaymentMethod, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblBankName, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblBranch, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblAccountNO, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblAccountName, Alignment.MIDDLE_RIGHT);
		
		setMargin(true);
		addComponent(gridLayout);
		addComponent(btnUpdate);
		setComponentAlignment(btnUpdate, Alignment.BOTTOM_RIGHT);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnUpdate)) {
			UI.getCurrent().addWindow(new RefundsConfirmPopupWindow());
		}
	}
}
