package com.nokor.ersys.finance.accounting.ui.panel.main.list;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

/**
 * Accounting List Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountingListPanel extends AbstractTablePanel<AccountLedger> implements ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = -5943279774081183369L;
	
	private Tree accountTree;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("accountings"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("account.entries"));
		removeAllComponents();
		addDefaultNavigation();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		if (searchPanel != null) {
			verticalLayout.addComponent(searchPanel);
		}
		verticalLayout.addComponent(pagedTable);
		verticalLayout.addComponent(pagedTable.createControls());
		
		accountTree = createAccountTree();
		accountTree.setContainerDataSource(generateTreeContainerDataSource());
		
		Panel panel = new Panel(accountTree);
		panel.setWidth(200, Unit.PIXELS);
		panel.setHeight(100, Unit.PERCENTAGE);
		panel.setCaption(I18N.message("accounts"));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(panel);
		horizontalLayout.addComponent(verticalLayout);
		horizontalLayout.setExpandRatio(verticalLayout, 1);
		
		addComponent(horizontalLayout);
	}	

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<AccountLedger> createPagedDataProvider() {
		PagedDefinition<AccountLedger> pagedDefinition = new PagedDefinition<AccountLedger>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(AccountLedger.ID, I18N.message("id"), Long.class, Align.LEFT, 50, false);
		
		EntityPagedDataProvider<AccountLedger> pagedDataProvider = new EntityPagedDataProvider<AccountLedger>();
  		pagedDataProvider.setPagedDefinition(pagedDefinition);
  		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AccountLedger getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(AccountLedger.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<AccountLedger> createSearchPanel() {
		return new AccountingSearchPanel(this);
	}
	
	/**
	 * Create Account Tree
	 * @return
	 */
	private Tree createAccountTree() {
		final Tree tree = new Tree();
		tree.setStyleName("v-workplace-tree");
		tree.setImmediate(true);
		tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tree.setItemCaptionPropertyId("caption");
		
		return tree;
	}
	
	/**
	 * Generate Tree Container Data Source
	 * @return
	 */
	private HierarchicalContainer generateTreeContainerDataSource() {
		HierarchicalContainer containerTree = new HierarchicalContainer();
    	containerTree.addContainerProperty("caption", String.class, "");
 		
        List<ECategoryRoot> categoryRoots = ECategoryRoot.values();
        if (categoryRoots != null) {
        	int index = 0;
        	for (int i = 0; i < categoryRoots.size(); i++) {
    			ECategoryRoot categoryRoot = categoryRoots.get(i);

    			Item item = containerTree.addItem(index);
    			String caption = categoryRoot.getDescLocale();
    			item.getItemProperty("caption").setValue(caption);
    			
    			index = buildAccountCategory(containerTree, ACCOUNTING_SRV.listAccountCategoryParentByRoot(categoryRoot), index);
    			index++;
    		}
        }
        return containerTree;
	}
	
	/**
	 * Guild Account Category
	 * @param containerTree
	 * @param accCategories
	 * @param index
	 * @return
	 */
	private int buildAccountCategory(HierarchicalContainer containerTree, List<AccountCategory> accCategories, int index) {
		int parentId = index;
		for (AccountCategory accountCategory : accCategories) {
			index++;
			Item item = containerTree.addItem(index);
			String caption = accountCategory.getDescLocale();
			item.getItemProperty("caption").setValue(caption);
			containerTree.setParent(index, parentId);
			
			List<AccountCategory> childAccCategories = ACCOUNTING_SRV.listAccountCategoryByParent(accountCategory);
			List<Account> accounts = ACCOUNTING_SRV.listAccountByAccountCategory(accountCategory);
			int subParentId = index;
			
			if ((childAccCategories == null || childAccCategories.isEmpty()) && accounts.isEmpty()) {
				containerTree.setChildrenAllowed(index, false);
			} else {
				index = buildAccountCategory(containerTree, childAccCategories, index);
			}
			
			for (Account account : accounts) {
				index++;
				item = containerTree.addItem(index);
				caption = account.getName();
				item.getItemProperty("caption").setValue(caption);
				containerTree.setParent(index, subParentId);
				containerTree.setChildrenAllowed(index, false);
			}
		}
		return index;
	}

}
