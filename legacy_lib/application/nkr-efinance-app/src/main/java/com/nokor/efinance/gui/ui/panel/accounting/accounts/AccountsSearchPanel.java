package com.nokor.efinance.gui.ui.panel.accounting.accounts;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.service.AccountRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountsSearchPanel extends AbstractSearchPanel<Account> {

	/** */
	private static final long serialVersionUID = 2130984047204974611L;
	
	private TextField txtCode;
	private TextField txtName;
	private TextField txtDesc;
	
	/**
	 * 
	 * @param paymentCodeTablePanel
	 */
	public AccountsSearchPanel(AccountsTablePanel accountsTablePanel) {
		super(I18N.message("search"), accountsTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtCode.setValue(StringUtils.EMPTY);
		txtName.setValue(StringUtils.EMPTY);
		txtDesc.setValue(StringUtils.EMPTY);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtCode = ComponentFactory.getTextField(false, 50, 150);
		txtName = ComponentFactory.getTextField(false, 100, 150);
		txtDesc = ComponentFactory.getTextField(false, 100, 150);
		
		Label lblCodeTitle = ComponentFactory.getLabel(I18N.message("code"));
		Label lblNameTitle = ComponentFactory.getLabel(I18N.message("name"));
		Label lblDescTitle = ComponentFactory.getLabel(I18N.message("desc"));
		
		GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(lblCodeTitle, 0, 0);
		gridLayout.addComponent(txtCode, 1, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceHeight(5, Unit.PIXELS), 2, 0);
		gridLayout.addComponent(lblNameTitle, 3, 0);
		gridLayout.addComponent(txtName, 4, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceHeight(5, Unit.PIXELS), 5, 0);
		gridLayout.addComponent(lblDescTitle, 6, 0);
		gridLayout.addComponent(txtDesc, 7, 0);
		
		gridLayout.setComponentAlignment(lblCodeTitle, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblNameTitle, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblDescTitle, Alignment.MIDDLE_CENTER);
		
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Account> getRestrictions() {
		AccountRestriction accountRestriction = new AccountRestriction();
		accountRestriction.setCode(txtCode.getValue());
		accountRestriction.setName(txtName.getValue());
		accountRestriction.setDesc(txtDesc.getValue());
		
		return accountRestriction;
	}

}
