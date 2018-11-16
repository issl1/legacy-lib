package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.model.LockSplitItem;
import com.nokor.efinance.core.contract.model.MLockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.JournalEventComboBox;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * Collection lock split item table
 * @author uhout.cheng
 */
public class ColLockSplitItemTablePanel extends AbstractControlPanel implements FinServicesHelper, MLockSplit, ClickListener {
	
	/** */
	private static final long serialVersionUID = 6708867572147410051L;
	
	private TextField txtLckTypeCode;
	private JournalEventComboBox cbxLockSplitType;
	private TextField txtAmount;
	private Button btnAdd;
	private Item selectedItem;
	
	private SimpleTable<Entity> simpleTable;
	
	private List<LockSplitItem> lockSplitItems;
	private List<Long> lckSplitItemIds;
	
	private LockSplit lockSplit;
	
	/**
	 * @return the lockSplitItems
	 */
	public List<LockSplitItem> getLockSplitItems() {
		if (lckSplitItemIds != null && !lckSplitItemIds.isEmpty()) {
			for (Long lckId : lckSplitItemIds) {
				try {
					LCK_SPL_SRV.deleteLockSplitItem(lockSplit, LCK_SPL_SRV.getById(LockSplitItem.class, lckId));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
		return lockSplitItems;
	}
	
	/**
	 * 
	 */
	public ColLockSplitItemTablePanel() {
		JournalEventRestriction journalEventRestriction = new JournalEventRestriction();
		journalEventRestriction.setJournalId(JournalEvent.RECEIPTS);
		List<JournalEvent> lockSplitTypes = LCK_SPL_SRV.list(journalEventRestriction);
		cbxLockSplitType = new JournalEventComboBox(lockSplitTypes);
		cbxLockSplitType.setWidth(150, Unit.PIXELS);
		cbxLockSplitType.setImmediate(true);
		txtAmount = ComponentFactory.getTextField(15, 150);
		txtLckTypeCode = ComponentFactory.getTextField(15, 100);
		txtLckTypeCode.setImmediate(true);
		btnAdd = ComponentLayoutFactory.getButtonStyle("add", FontAwesome.PLUS, 60, "btn btn-success button-small");
		btnAdd.addClickListener(this);
		
		txtLckTypeCode.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 4353319200795790557L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (StringUtils.isNotEmpty(txtLckTypeCode.getValue())) {
					JournalEventRestriction restrictions = new JournalEventRestriction();
					restrictions.setJournalId(JournalEvent.RECEIPTS);
					restrictions.addCriterion(Restrictions.in(JournalEvent.CODE, new String[] {txtLckTypeCode.getValue()}));
					cbxLockSplitType.setValues(LCK_SPL_SRV.list(restrictions));
				} else {
					cbxLockSplitType.setValues(lockSplitTypes);
				}
			}
		});
		
		simpleTable = getSimpleTable(getColumnDefinitions());
		simpleTable.setFooterVisible(true);
		simpleTable.setColumnFooter(DETAIL, I18N.message("total"));
		simpleTable.setSelectable(false);
		
		setSpacing(true);
		addComponent(getBeforeTableLayout());
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		return simpleTable;
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		reset();
		this.lockSplit = lockSplit;
		if (lockSplit.getItems() != null) {
			this.lockSplitItems.addAll(lockSplit.getItems());
		}
		setTableIndexedContainer(this.lockSplitItems);
	}
	
	/**
	 * 
	 * @param lstLockSplitItem
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<LockSplitItem> lstLockSplitItem) {
		simpleTable.removeAllItems();
		this.selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		double totalAmount = 0d;
		if (lstLockSplitItem != null && !lstLockSplitItem.isEmpty()) {
			int index = 0;
			for (LockSplitItem lockSplitItem : lstLockSplitItem) {
				JournalEvent journalEvent = lockSplitItem.getJournalEvent();
				Button btnDelete = getButton(FontAwesome.TRASH_O);
				Button btnUpdate = getButton(FontAwesome.EDIT);
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(ID).setValue(index);
				item.getItemProperty(LOCKSPLITTYPE).setValue(journalEvent != null ? journalEvent.getCode() : "");
				item.getItemProperty(DETAIL).setValue(journalEvent != null ? journalEvent.getDescLocale() : "");
				item.getItemProperty(TOTALAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(lockSplitItem.getTiAmount())));
				item.getItemProperty(ACTIONS).setValue(getButtonLayouts(btnUpdate, btnDelete));

				int actionsIndex = index;
				
				btnUpdate.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 2593314391195046138L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						selectedItem = item;
						LockSplitItem lckItem = lockSplitItems.get(getItemSelectedId());
						txtLckTypeCode.setValue(lckItem.getJournalEvent() == null ? StringUtils.EMPTY : lckItem.getJournalEvent().getCode());
						cbxLockSplitType.setSelectedEntity(lckItem.getJournalEvent());
						txtAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(lckItem.getTiAmount())));
					}
				});
				
				btnDelete.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = -5099873189823182922L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						if (lockSplitItem.getId() != null) {
							lckSplitItemIds.add(lockSplitItem.getId());
						}
						lockSplitItems.remove(actionsIndex);
						setTableIndexedContainer(lockSplitItems);
					}
				});
	
				index++;
				totalAmount += MyNumberUtils.getDouble(lockSplitItem.getTiAmount());
			}
			simpleTable.setColumnFooter(TOTALAMOUNT, AmountUtils.format(totalAmount));
		} else {
			simpleTable.setColumnFooter(TOTALAMOUNT, AmountUtils.format(0d));
		}
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
	 * 
	 * @param resource
	 * @return
	 */
	private Button getButton(Resource resource) {
		Button button = new Button();
		button.setIcon(resource);
		button.setStyleName(Reindeer.BUTTON_LINK);
		return button;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(LOCKSPLITTYPE, I18N.message("code"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DETAIL, I18N.message("detail"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(TOTALAMOUNT, I18N.message("amount"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), HorizontalLayout.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			if (getErrorsMsg().isEmpty()) {
				LockSplitItem lockSplitItem = null;
				if (this.selectedItem != null) {
					lockSplitItem = lockSplitItems.get(getItemSelectedId());
				} else {
					lockSplitItem = LockSplitItem.createInstance();
					lockSplitItems.add(lockSplitItem);
				}
				lockSplitItem.setLockSplit(this.lockSplit);
				lockSplitItem.setJournalEvent(cbxLockSplitType.getSelectedEntity());
				lockSplitItem.setTiAmount(getDouble(txtAmount));
				setTableIndexedContainer(lockSplitItems);
				if (StringUtils.isNotEmpty(txtAmount.getValue())
						&& cbxLockSplitType.getSelectedEntity() != null) {
					txtLckTypeCode.setValue(StringUtils.EMPTY);
					cbxLockSplitType.setSelectedEntity(null);
					txtAmount.setValue(StringUtils.EMPTY);
				}
			} else {
				Notification notification = new Notification("", Type.ERROR_MESSAGE);
				notification.setDescription(getErrorsMsg().toString());
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<String> getErrorsMsg() {
		errors.clear();
		checkMandatorySelectField(cbxLockSplitType, "lock.split.type");
		checkMandatoryField(txtAmount, "amount");
		checkDoubleField(txtAmount, "amount");
		return errors;
	}
	
	/**
	 * 
	 */
	protected void reset() {
		lockSplitItems = new ArrayList<LockSplitItem>();
		lckSplitItemIds = new ArrayList<Long>();
		txtAmount.setValue(StringUtils.EMPTY);
		cbxLockSplitType.setSelectedEntity(null);
	}

	/**
	 * 
	 * @return
	 */
	private Component getBeforeTableLayout() {
		Label lblCode = ComponentFactory.getLabel("code");
		Label lblLockSplitType = ComponentFactory.getLabel("lock.split.type");
		Label lblAmount = ComponentFactory.getLabel("amount");
		
		GridLayout gridLayout = new GridLayout(15, 1);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblCode, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtLckTypeCode, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblLockSplitType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxLockSplitType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblAmount, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtAmount, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(50, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(btnAdd, iCol++, 0);
		
		gridLayout.setComponentAlignment(lblCode, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblLockSplitType, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAmount, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnAdd, Alignment.TOP_RIGHT);
	
		return gridLayout;
	}
		
	/**
	 * @return
	 */
	private Integer getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Integer) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
}
