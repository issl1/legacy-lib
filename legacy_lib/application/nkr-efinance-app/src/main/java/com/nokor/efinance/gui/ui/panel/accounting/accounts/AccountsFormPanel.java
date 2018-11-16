package com.nokor.efinance.gui.ui.panel.accounting.accounts;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountsFormPanel extends AbstractFormPanel implements ErsysAccountingAppServicesHelper{

	/** */
	private static final long serialVersionUID = -3876723102451387549L;

	private TextField txtCode;
	private TextField txtName;
	private TextField txtNameEn;
	private TextField txtReference;
	private TextField txtInfo;
	private TextField txtOtherInfo;
	private TextField txtStartingBalance;
	
	private Account account;
	
	private EntityComboBox<AccountCategory> cbxAccountCateg;
	
	/**
     * 
     */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxAccountCateg = new EntityComboBox<AccountCategory>(AccountCategory.class, AccountCategory.DESC);
		cbxAccountCateg.renderer();
		cbxAccountCateg.setRequired(true);
		cbxAccountCateg.setCaption(I18N.message("category"));
		
		txtCode = ComponentFactory.getTextField("code", true, 100, 200);
		txtName = ComponentFactory.getTextField("name", false, 100, 200);
		txtNameEn = ComponentFactory.getTextField("name.en", false, 100, 200);
		txtStartingBalance = ComponentFactory.getTextField("starting.balance", true, 100, 200);
		txtReference = ComponentFactory.getTextField("reference", false, 100, 200);
		txtInfo = ComponentFactory.getTextField("information", false, 100, 200);
		txtOtherInfo = ComponentFactory.getTextField("other.info", false, 100, 200);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		
		//formLayout.addComponent(cbxAccountCateg);
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtNameEn);
		formLayout.addComponent(txtStartingBalance);
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtInfo);
		formLayout.addComponent(txtOtherInfo);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		//account.setCategory(cbxAccountCateg.getSelectedEntity());
		account.setCode(txtCode.getValue());
		account.setName(txtName.getValue());
		account.setNameEn(txtNameEn.getValue());
		account.setStartingBalance(MyNumberUtils.getBigDecimal(txtStartingBalance.getValue()));
		account.setReference(txtReference.getValue());
		account.setInfo(txtInfo.getValue());
		account.setOtherInfo(txtOtherInfo.getValue());
		return account;
	}
	
	/**
	 * assign values
	 */
	public void assignValues(Long accId) {
		super.reset();
		if (accId != null) {
			account = ACCOUNTING_SRV.getById(Account.class, accId);
			//cbxAccountCateg.setSelectedEntity(account.getCategory());
			txtCode.setValue(account.getCode());
			txtName.setValue(account.getName());
			txtNameEn.setValue(account.getNameEn());
			
			BigDecimal startingBalance = account.getStartingBalance();
			txtStartingBalance.setValue(startingBalance != null ? startingBalance.toPlainString() : "");
			txtReference.setValue(account.getReference());
			txtInfo.setValue(account.getInfo());
			txtOtherInfo.setValue(account.getOtherInfo());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		txtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		txtStartingBalance.setValue("");
		txtReference.setValue("");
		txtInfo.setValue("");
		txtOtherInfo.setValue("");
		// cbxAccountCateg.setSelectedEntity(null);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		account = new Account();
		checkMandatorySelectField(cbxAccountCateg, "category");
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtStartingBalance, "starting.balance");
		checkBigDecimalField(txtStartingBalance, "starting.balance");
		return errors.isEmpty();
	}

}
