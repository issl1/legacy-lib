package com.nokor.efinance.gui.ui.panel.contract.cashflow.locksplit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Lock-Split panel in cash flow
 * @author uhout.cheng
 */
public class CashflowLockSplitPanel extends VerticalLayout implements MLockSplit, FinServicesHelper, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = 4881333239445449803L;
	
	private Logger LOG = LoggerFactory.getLogger(CashflowLockSplitPanel.class);

	private SimplePagedTable<LockSplit> pagedTable;
	private List<ColumnDefinition> lckColumnDefinitions;
	private Item selectedItem;
	private List<CheckBox> cbLockSplitStatuses;
	private Contract contract;
	
	/**
	 * 
	 */
	public CashflowLockSplitPanel() {
		super();
		setSizeFull();
		setMargin(true);
		
		HorizontalLayout frmLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		Panel searchPanel = new Panel();
		
		cbLockSplitStatuses = new ArrayList<CheckBox>(); 
		List<EWkfStatus> lockSplitStatuses = LockSplitWkfStatus.listLockSplitStatus();
		if (lockSplitStatuses != null && !lockSplitStatuses.isEmpty()) {
			for (EWkfStatus lckStatus : lockSplitStatuses) {
				CheckBox checkBox = new CheckBox();
				checkBox.addValueChangeListener(new ValueChangeListener() {
					
					/** */
					private static final long serialVersionUID = -9019614376913376025L;

					/**
					 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
					 */
					@Override
					public void valueChange(ValueChangeEvent event) {
						refresh();
					}
				});
				checkBox.setData(lckStatus);
				checkBox.setCaption(lckStatus.getDescLocale());
				cbLockSplitStatuses.add(checkBox);
				frmLayout.addComponent(checkBox);
			}
			searchPanel.setContent(frmLayout);
		}
		pagedTable = new SimplePagedTable<LockSplit>(getLockSplitColumnDefinitions());
		pagedTable.addItemClickListener(this);
		
		VerticalLayout tableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		tableLayout.addComponent(pagedTable);
		tableLayout.addComponent(pagedTable.createControls());
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(searchPanel);
		contentLayout.addComponent(tableLayout);  
        addComponent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getLockSplitColumnDefinitions() {
		lckColumnDefinitions = new ArrayList<ColumnDefinition>();
		lckColumnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		lckColumnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 80));
		lckColumnDefinitions.add(new ColumnDefinition(DEPARTMENT, I18N.message("department"), String.class, Align.LEFT, 120));
		lckColumnDefinitions.add(new ColumnDefinition(EXPIRYDATE, I18N.message("expiration.date"), Date.class, Align.LEFT, 80));
		lckColumnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("due.date"), Date.class, Align.LEFT, 80));
		lckColumnDefinitions.add(new ColumnDefinition(TOTALAMOUNT, I18N.message("amount"), String.class, Align.LEFT, 100));
		lckColumnDefinitions.add(new ColumnDefinition(LOCKSPLITTYPE, I18N.message("codes"), String.class, Align.LEFT, 130));
		lckColumnDefinitions.add(new ColumnDefinition(LockSplit.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 130));
		lckColumnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 50));
		return lckColumnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		refresh();
	}
	
	/**
	 * 
	 */
	protected void refresh() {
		pagedTable.setContainerDataSource(setLockSplitIndexedContainer(getLockSplits()));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<LockSplit> getLockSplits() {
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, contract.getId()));
		if (cbLockSplitStatuses != null && !cbLockSplitStatuses.isEmpty()) {
			for (int i = 0; i < cbLockSplitStatuses.size(); i++) {
				CheckBox checkBox = cbLockSplitStatuses.get(i);
				EWkfStatus lckStatus = (EWkfStatus) checkBox.getData();
				if (Boolean.TRUE.equals(checkBox.getValue())) {
					restrictions.getWkfStatusList().add(lckStatus);
				}
			}
		}
		return LCK_SPL_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private Button getButtonView() {
		Button button = new Button();
		button.setStyleName(Reindeer.BUTTON_LINK);
		button.setIcon(FontAwesome.EYE);
		return button;
	}
	
	/**
	 * 
	 * @param lockSplits
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer setLockSplitIndexedContainer(List<LockSplit> lockSplits) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		try {	
			for (ColumnDefinition column : this.lckColumnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			if (lockSplits != null && !lockSplits.isEmpty()) {
				for (LockSplit lockSplit : lockSplits) {
					Item item = indexedContainer.addItem(lockSplit.getId());
					item.getItemProperty(ID).setValue(lockSplit.getId());
					item.getItemProperty(CREATEDATE).setValue(lockSplit.getCreateDate());
					item.getItemProperty(DEPARTMENT).setValue("");
					item.getItemProperty(EXPIRYDATE).setValue(lockSplit.getTo());
					item.getItemProperty(PAYMENTDATE).setValue(lockSplit.getFrom());
					item.getItemProperty(TOTALAMOUNT).setValue(AmountUtils.format(lockSplit.getTotalAmount()));
					item.getItemProperty(LOCKSPLITTYPE).setValue(getLockSplitTypeCode(lockSplit.getItems()));
					item.getItemProperty(LockSplit.WKFSTATUS).setValue(lockSplit.getWkfStatus().getDescLocale());
					
					Button btnEdit = getButtonView();
					btnEdit.addClickListener(new ClickListener() {
						
						/** */
						private static final long serialVersionUID = -8351033288992216505L;

						/**
						 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
						 */
						@Override
						public void buttonClick(ClickEvent event) {
							displayPopup(lockSplit);
						}
					});
					item.getItemProperty(ACTIONS).setValue(btnEdit);
				}
			}	
		} catch (DaoException e) {
			LOG.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param lockSplitItems
	 */
	private String getLockSplitTypeCode(List<LockSplitItem> lockSplitItems) {
		StringBuffer lockSplitCode = new StringBuffer(); 
		if (lockSplitItems != null && !lockSplitItems.isEmpty()) {
			List<String> descriptions = new ArrayList<>();
			for (LockSplitItem lockSplitItem : lockSplitItems) {
				if (lockSplitItem != null && lockSplitItem.getJournalEvent() != null) {
					descriptions.add(lockSplitItem.getJournalEvent().getCode());
				}
			}
			if (!descriptions.isEmpty()) {
				for (String string : descriptions) {
					lockSplitCode.append(string);
					if (StringUtils.isNotEmpty(string)) {
						lockSplitCode.append("/");
					}
				}
				int lastIndex = lockSplitCode.lastIndexOf("/");
				lockSplitCode.replace(lastIndex, lastIndex + 1, "");
			}
		}
		return lockSplitCode.toString();
	}
	
	/**
	 * 
	 */
	public void reset() {
		if (!cbLockSplitStatuses.isEmpty()) {
			for (int i = 0; i < cbLockSplitStatuses.size(); i++) {
				CheckBox checkBox = cbLockSplitStatuses.get(i);
				checkBox.setValue(false);
			}
		}
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			LockSplit lockSplit = LCK_SPL_SRV.getById(LockSplit.class, getItemSelectedId());
			displayPopup(lockSplit);
		}
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	private void displayPopup(LockSplit lockSplit) {
		CashflowLockSplitPopupWindow popupWindow = new CashflowLockSplitPopupWindow(this);
		popupWindow.assignValue(lockSplit);
		UI.getCurrent().addWindow(popupWindow);
	}
	
}
