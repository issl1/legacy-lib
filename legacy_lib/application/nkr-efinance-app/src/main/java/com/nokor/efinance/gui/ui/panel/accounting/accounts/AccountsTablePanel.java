package com.nokor.efinance.gui.ui.panel.accounting.accounts;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountsTablePanel extends AbstractTablePanel<Account> implements ErsysAccountingAppServicesHelper {

	/** */
	private static final long serialVersionUID = -8431523648467949946L;

	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("accounts"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("accounts"));
		
		addDefaultNavigation();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Account> createPagedDataProvider() {
		PagedDefinition<Account> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(Account.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(Account.CODE, I18N.message("code"), String.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(Account.NAME, I18N.message("name"), String.class, Align.LEFT, 170);
		pagedDefinition.addColumnDefinition(Account.DESC, I18N.message("desc"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Account.STARTINGBALANCE, I18N.message("starting.balance"), BigDecimal.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Account.INFO, I18N.message("info"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Account> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Account getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ACCOUNTING_SRV.getById(Account.class, id);
		}
		return null;
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(Account.ID).getValue();
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AccountsSearchPanel createSearchPanel() {
		return new AccountsSearchPanel(this);
	}

}
