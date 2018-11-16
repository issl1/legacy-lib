package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ContractOperation;
import com.nokor.efinance.core.collection.model.ELockSplitCategory;
import com.nokor.efinance.core.collection.model.EOperationType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;

/**
 * Lock Split table in collection phone staff 
 * @author uhout.cheng
 */
public class ColPhoneLockSplitPaymentsTablePanel extends TreeTable implements FinServicesHelper, MLockSplit {
	
	/** */
	private static final long serialVersionUID = 78057784151263574L;
	
	protected final static Logger logger = LoggerFactory.getLogger(ColPhoneLockSplitPaymentsTablePanel.class);
	
	private ColPhoneLockSplitPaymentsPanel colPhoneLckSplPaymentsPanel;

	/**
	 * 
	 * @param colPhoneLckSplPaymentsPanel
	 */
	public ColPhoneLockSplitPaymentsTablePanel(ColPhoneLockSplitPaymentsPanel colPhoneLckSplPaymentsPanel) {
		this.colPhoneLckSplPaymentsPanel = colPhoneLckSplPaymentsPanel;
		setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("current.lock.split") + "</h3>");
		setCaptionAsHtml(true);
		setPageLength(10);
		setSelectable(true);
		setSizeFull();
		setImmediate(true);
		setColumnCollapsingAllowed(true);
		setUpColumnDefinitions(this);
		setFooterVisible(true);
		setColumnFooter(LockSplit.LOCKSPLITTYPE, I18N.message("total"));
	}
	
	/**
	 * 
	 * @param lockSplits
	 */
	public void assignValues(List<LockSplit> lockSplits) {
		setIndexedContainer(lockSplits, 0, 0);
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("payment.date.from"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(EXPIRYDATE, I18N.message("payment.date.to"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(LOCKSPLITTYPE, I18N.message("codes"), String.class, Align.LEFT, 85));
		columnDefinitions.add(new ColumnDefinition(TOTALAMOUNT, I18N.message("total"), String.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), HorizontalLayout.class, Align.CENTER, 40));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param lockSplits
	 * @param parent
	 * @param index
	 */
	private void setIndexedContainer(List<LockSplit> lockSplits, int parent, int index) {
		this.removeAllItems();
		if (lockSplits != null) {
			int subParent = parent;
			double totalAmount = 0d;
			for (LockSplit lckSpt : lockSplits) {
				subParent = renderParentRow(String.valueOf(lckSpt.getId()), parent, index, lckSpt);
				index++;
				if (lckSpt.getItems() != null && !lckSpt.getItems().isEmpty()) {
					for (LockSplitItem lckSplItem : lckSpt.getItems()) {
						Item item = addItem(index);
						setParent(index, subParent);
						setCollapsed(subParent, true);
						setChildrenAllowed(index, false);
						index = renderRow(item, index, lckSplItem);
						index++;
						totalAmount += MyNumberUtils.getDouble(lckSplItem.getTiAmount());
					} 
				} else {
					setChildrenAllowed(subParent, false);
				}
				parent = index;
			}
			setColumnFooter(LockSplit.TOTALAMOUNT, MyNumberUtils.formatDoubleToString(totalAmount, "###,##0.00"));
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param parent
	 * @param index
	 * @param lckSpt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, int parent, int index, LockSplit lckSpt) {
		Contract contract = lckSpt.getContract();
		
		Item item = addItem(index);
		
		setParent(index, parent);
		setCollapsed(parent, true);
				
		item.getItemProperty(ID).setValue(key);
		item.getItemProperty(CREATEUSER).setValue(lckSpt.getCreateUser());
		item.getItemProperty(CREATEDATE).setValue(DateUtils.getDateLabel(lckSpt.getCreateDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(PAYMENTDATE).setValue(DateUtils.getDateLabel(lckSpt.getFrom(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(EXPIRYDATE).setValue(DateUtils.getDateLabel(lckSpt.getTo(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(LOCKSPLITTYPE).setValue(LCK_SPL_SRV.getLockSplitTypeCode(lckSpt.getItems()));
		item.getItemProperty(TOTALAMOUNT).setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(lckSpt.getTotalAmount()), "###,##0.00"));
		item.getItemProperty(STATUS).setValue(lckSpt.getWkfStatus().getDescLocale());
		
		Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TRASH_O);
		Button btnUpdate = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		item.getItemProperty(ACTIONS).setValue(getButtonLayouts(btnUpdate, btnDelete));
		
		btnUpdate.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -7822700855783737634L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				colPhoneLckSplPaymentsPanel.assignToControls(lckSpt);
			}
		});
		
		btnDelete.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 2196776209551434249L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				try {	
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {		

						/** */
						private static final long serialVersionUID = 1306692248563937146L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								try {
									LCK_SPL_SRV.deleteLockSplit(lckSpt);
									setIndexedContainer(colPhoneLckSplPaymentsPanel.getLockSplits(), 0, 0);
									colPhoneLckSplPaymentsPanel.assignValues(contract);
									dialog.close();
								} catch (Exception e) {
									logger.error(e.getMessage());
								}
					        }
						}
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");	
					
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		
		return index;
	}
	
	/**
	 * 
	 * @param item
	 * @param index
	 * @param lckSplitItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderRow(Item item, int index, LockSplitItem lckSplitItem) {
		JournalEvent journalEvent = lckSplitItem.getJournalEvent();
		if (ELockSplitCategory.DUE.equals(lckSplitItem.getLockSplitCategory())) {
			item.getItemProperty(ID).setValue(journalEvent != null ? journalEvent.getDescLocale() : "N/A");
		} else if (ELockSplitCategory.OPERATION.equals(lckSplitItem.getLockSplitCategory())) {
			ContractOperation operation = lckSplitItem.getOperation();
			if (operation != null) {
				EOperationType operationType = operation.getOperationType();
				item.getItemProperty(ID).setValue(operationType != null ? operationType.getDescLocale() : "N/A");
			}
		} else {
			item.getItemProperty(ID).setValue(journalEvent != null ? journalEvent.getDescLocale() : "N/A");
		}
		item.getItemProperty(CREATEUSER).setValue(lckSplitItem.getCreateUser());
		item.getItemProperty(CREATEDATE).setValue(DateUtils.getDateLabel(lckSplitItem.getCreateDate(), DateUtils.FORMAT_DDMMYYYY_SLASH));
		item.getItemProperty(LOCKSPLITTYPE).setValue(journalEvent != null ? journalEvent.getCode() : "");
		item.getItemProperty(TOTALAMOUNT).setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(lckSplitItem.getTiAmount()), "###,##0.00"));
		item.getItemProperty(STATUS).setValue(lckSplitItem.getWkfStatus().getDescLocale());
		return index;
	}
	
	/**
	 * 
	 * @param btnUpdate
	 * @param btnDelete
	 * @return
	 */
	private HorizontalLayout getButtonLayouts(Button btnUpdate, Button btnDelete) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(btnUpdate);
		layout.addComponent(btnDelete);
		return layout;
	}
	
	/**
	 * Set Up Column Definitions
	 * @param table
	 */
	private void setUpColumnDefinitions(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.removeContainerProperty(columnDefinition.getPropertyId());
		}
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.addContainerProperty(
				columnDefinition.getPropertyId(),
				columnDefinition.getPropertyType(),
				null,
				columnDefinition.getPropertyCaption(),
				null,
				columnDefinition.getPropertyAlignment());
			table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
		}
	}
	
}
