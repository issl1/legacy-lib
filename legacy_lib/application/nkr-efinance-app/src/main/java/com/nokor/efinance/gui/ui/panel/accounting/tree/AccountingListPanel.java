package com.nokor.efinance.gui.ui.panel.accounting.tree;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.gui.ui.panel.accounting.detail.AccountCategoryPopupPanel;
import com.nokor.efinance.gui.ui.panel.accounting.detail.AccountPopupPanel;
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
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.server.FontAwesome;
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
	
	private static final String ICON = "icon";
	private static final String CAPTION = "caption";
	private static final String IS_ACCOUNT = "isAccount";
	
	private Tree accountTree;
	private AccountCategoryPopupPanel categoryWindow;
	private AccountPopupPanel accountWindow;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		categoryWindow = new AccountCategoryPopupPanel(this);
		accountWindow = new AccountPopupPanel(this);
		setCaption(I18N.message("accounts"));
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
		accountTree.setItemIconPropertyId(ICON);
		
		Panel panel = new Panel(accountTree);
		panel.setWidth(200, Unit.PIXELS);
		panel.setHeight(100, Unit.PERCENTAGE);
		panel.setCaption(I18N.message("accounts"));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(panel);
//		horizontalLayout.addComponent(verticalLayout);
//		horizontalLayout.setExpandRatio(verticalLayout, 1);
		
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
		tree.setItemCaptionPropertyId(CAPTION);
		Action addSubAccountCategoryAction = new Action(I18N.message("add.sub.account.category"));
		Action addAccount = new Action(I18N.message("add.account"));
		Action edit = new Action(I18N.message("edit"));
		Action refresh = new Action(I18N.message("refresh"));
		tree.addActionHandler(new Handler() {
			/** */
			private static final long serialVersionUID = -7579640590062861130L;

			/**
			 * @see com.vaadin.event.Action.Handler#handleAction(com.vaadin.event.Action, java.lang.Object, java.lang.Object)
			 */
			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (target != null) {
					Item item = tree.getItem(target);
					boolean isAccount = (boolean) tree.getItem(target).getItemProperty(IS_ACCOUNT).getValue();
					AccountCategory category = (AccountCategory) item.getItemProperty(AccountCategory.PARENT).getValue();
					if (action == addAccount) {
						accountWindow.reset();
						accountWindow.setCategory(category);
						accountWindow.show();
					} else if (action == addSubAccountCategoryAction) {
						ECategoryRoot root = (ECategoryRoot) item.getItemProperty(AccountCategory.ROOT).getValue();
						categoryWindow.reset();
						categoryWindow.setCategoryParent(category);
						categoryWindow.setRoot(root);
						categoryWindow.show();
					} else if (action == edit) {
						Long id = (Long) tree.getItem(target).getItemProperty(AccountCategory.ID).getValue();
						if (isAccount) {
							accountWindow.assignValues(id);
							accountWindow.show();
						} else {
							categoryWindow.assignValues(id);
							categoryWindow.show();
						}
					} else if (action == refresh) {
						refreshAccountList();
					}
				}
			}
			
			/**
			 * @see com.vaadin.event.Action.Handler#getActions(java.lang.Object, java.lang.Object)
			 */
			@Override
			public Action[] getActions(Object target, Object sender) {
				if (target != null) {
					boolean isAccount = (boolean) tree.getItem(target).getItemProperty(IS_ACCOUNT).getValue();
					List<Action> actions = new ArrayList<Action>();
					Long id = (Long) tree.getItem(target).getItemProperty(AccountCategory.ID).getValue();
					if (!isAccount) {
						actions.add(addSubAccountCategoryAction);
						if (id != null) {
							actions.add(addAccount);
						}
					}
					if (id != null) {
						actions.add(edit);
					}
					actions.add(refresh);
					return actions.toArray(new Action[actions.size()]);
				}
				return null;
			}
		});
		
		return tree;
	}
	
	/**
	 * Generate Tree Container Data Source
	 * @return
	 */
	private HierarchicalContainer generateTreeContainerDataSource() {
		HierarchicalContainer containerTree = new HierarchicalContainer();
		containerTree.addContainerProperty(AccountCategory.ID, Long.class, null);
		containerTree.addContainerProperty(ICON, FontAwesome.class, null);
    	containerTree.addContainerProperty(CAPTION, String.class, "");
    	containerTree.addContainerProperty(IS_ACCOUNT, Boolean.class, true);
    	containerTree.addContainerProperty(AccountCategory.PARENT, AccountCategory.class, null);
    	containerTree.addContainerProperty(AccountCategory.ROOT, ECategoryRoot.class, null);
 		
        List<ECategoryRoot> categoryRoots = ECategoryRoot.values();
        if (categoryRoots != null) {
        	int index = 0;
        	for (int i = 0; i < categoryRoots.size(); i++) {
    			ECategoryRoot categoryRoot = categoryRoots.get(i);
    			String caption = categoryRoot.getDescLocale();
    			addItem(containerTree, index, null, caption, null, categoryRoot, false);
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
			String caption = accountCategory.getNameLocale();
			addItem(containerTree, index, accountCategory.getId(), caption, accountCategory, accountCategory.getRoot(), false);
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
				caption = account.getNameLocale();
				addItem(containerTree, index, account.getId(), caption, null, null, true);
				containerTree.setParent(index, subParentId);
				containerTree.setChildrenAllowed(index, false);
			}
		}
		return index;
	}
	
	/**
	 * Add Item
	 * @param containerTree
	 * @param index
	 * @param caption
	 * @param isAccount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void addItem(HierarchicalContainer containerTree, int index, Long id, String caption, AccountCategory parent, ECategoryRoot root, boolean isAccount) {
		Item item = containerTree.addItem(index);
		item.getItemProperty(AccountCategory.ID).setValue(id);
		if (isAccount) {
			item.getItemProperty(ICON).setValue(FontAwesome.FILE_O);
		} else {
			item.getItemProperty(ICON).setValue(FontAwesome.FOLDER_O);
		}
		item.getItemProperty(CAPTION).setValue(caption);
		item.getItemProperty(IS_ACCOUNT).setValue(isAccount);
		item.getItemProperty(AccountCategory.PARENT).setValue(parent);
		item.getItemProperty(AccountCategory.ROOT).setValue(root);
	}
	
	/**
	 * Refresh Account List
	 */
	public void refreshAccountList() {
		accountTree.setContainerDataSource(generateTreeContainerDataSource());
	}

}
