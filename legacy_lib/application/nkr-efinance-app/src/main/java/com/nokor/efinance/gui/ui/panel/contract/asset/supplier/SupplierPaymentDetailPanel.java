package com.nokor.efinance.gui.ui.panel.contract.asset.supplier;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.gl.finwiz.share.domain.AP.BankBranchDTO;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.dealer.model.DealerPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * Payment detail in supplier tab
 * @author uhout.cheng
 */
public class SupplierPaymentDetailPanel extends Panel {

	/** */
	private static final long serialVersionUID = -4789024939528640745L;
	
	private static final String TEMPLATE = "asset/assetSupplierPaymentDetailsLayout";

	private String chanelOfPaymentValue;
	private String nameOfPayeeValue;
	private String bankNameValue;
	private String accountNOValue;
	private String brandValue;
	private VerticalLayout verLayout;
	
	/**
	 * 
	 */
	public SupplierPaymentDetailPanel() {
		setCaption(I18N.message("payment.details"));
		verLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		setContent(verLayout);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		verLayout.removeAllComponents();
		List<DealerPaymentMethod> dealerPaymentMethods = dealer.getDealerPaymentMethods();
		if (dealerPaymentMethods != null && !dealerPaymentMethods.isEmpty()) {
			for (DealerPaymentMethod dealerPaymentMethod : dealerPaymentMethods) {
				EPaymentMethod paymentMethod = dealerPaymentMethod.getPaymentMethod();
				DealerBankAccount dealerBankAccount = dealerPaymentMethod.getDealerBankAccount();
				BankAccountDTO bankAccount = null;
				if (dealerBankAccount != null && dealerBankAccount.getBankAccount() != null) {
					bankAccount = ClientBankAccount.getBankAccountById(dealerBankAccount.getBankAccount());
				}
				DealerAccountHolder dealerAccHolder = dealerPaymentMethod.getDealerAccountHolder();
				AccountHolderDTO accHolder = null;
				if (dealerAccHolder != null && dealerAccHolder.getAccountHolder() != null) {
					accHolder = ClientAccountHolder.getAccountHolderById(dealerAccHolder.getAccountHolder());
				}
				chanelOfPaymentValue = paymentMethod != null ? paymentMethod.getDescLocale() : StringUtils.EMPTY;
				nameOfPayeeValue = accHolder != null ? accHolder.getName() : StringUtils.EMPTY;
				bankNameValue = bankAccount != null ? bankAccount.getAccountName() : StringUtils.EMPTY;
				accountNOValue = bankAccount != null ? bankAccount.getPayeeAccountNumber() : StringUtils.EMPTY;
				BankBranchDTO bankBranch = null;
				brandValue = StringUtils.EMPTY;
				if (bankAccount != null) {
					bankBranch = bankAccount.getBankBranch();
					if (bankBranch != null) {
						brandValue = bankBranch.getName();
					}
				}
				if (EPaymentFlowType.FIN.equals(dealerPaymentMethod.getType())) {
					verLayout.addComponent(getLayout("motorbike.price.title", chanelOfPaymentValue, 
							nameOfPayeeValue, bankNameValue, accountNOValue, brandValue));
				} else {
					verLayout.addComponent(getLayout("commissions", chanelOfPaymentValue, 
							nameOfPayeeValue, bankNameValue, accountNOValue, brandValue));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Label getLabelValue(String value) {
		return ComponentFactory.getLabel("<b>" + I18N.message(value) + "</b>", ContentMode.HTML);
	}
	
	/**
	 * 
	 * @param title
	 * @return
	 */
	private Component getLayout(String title, String chanelOfPaymentValue, String nameOfPayeeValue, String bankNameValue, String accountNOValue, String brandValue) {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(getLabelValue(title), "lblTitle");
		customLayout.addComponent(ComponentFactory.getLabel("chanel.payment"), "lblChanelOfPayment");
		customLayout.addComponent(getLabelValue(chanelOfPaymentValue), "lblChanelOfPaymentValue");
		customLayout.addComponent(ComponentFactory.getLabel("name.payee"), "lblNamePayee");
		customLayout.addComponent(getLabelValue(nameOfPayeeValue), "lblNamePayeeValue");
		customLayout.addComponent(ComponentFactory.getLabel("bank.name"), "lblBankName");
		customLayout.addComponent(getLabelValue(bankNameValue), "lblBankNameValue");
		customLayout.addComponent(ComponentFactory.getLabel("account.no"), "lblAccountNO");
		customLayout.addComponent(getLabelValue(accountNOValue), "lblAccountNOValue");
		customLayout.addComponent(ComponentFactory.getLabel("brand"), "lblBrand");
		customLayout.addComponent(getLabelValue(brandValue), "lblBrandValue");
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(true, true, false, true),  false);
		horLayout.addComponent(customLayout);
		return horLayout;
	}
	
}
