package com.nokor.efinance.gui.ui.panel.contract.promises;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractPromise;
import com.nokor.efinance.core.contract.service.PromiseRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;


/**
 * 
 * @author uhout.cheng
 */
public class PromiseTablePanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = 8596757919943029768L;
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Item selectedItem;
	
	private Button btnAdd;
	
	private Contract contract;

	/**
	 * 
	 */
	public PromiseTablePanel() {
		this.columnDefinitions = getColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setCaption(I18N.message("promises"));
		simpleTable.addItemClickListener(this);
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		addComponent(btnAdd);
		addComponent(simpleTable);
		
		setComponentAlignment(btnAdd, Alignment.BOTTOM_RIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ContractPromise.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.CREATEDBY, I18N.message("created.by"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.PROMISETYPE, I18N.message("type"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.PROMISEAMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.PROMISEDATE, I18N.message("promised.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.REMARK, I18N.message("remark"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.PROMISESTATUS, I18N.message("status"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ContractPromise.OPTIONS, StringUtils.EMPTY, HorizontalLayout.class, Align.CENTER, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param transactionVOs
	 */
	protected void assignValues(Contract contract) {
		this.contract = contract;
		PromiseRestriction restrictions = new PromiseRestriction();
		restrictions.setContraId(contract.getId());
		setIndexedContainer(ENTITY_SRV.list(restrictions));
	}
	
	/**
	 * 
	 * @param mapCashflows
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<ContractPromise> promises) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (promises != null && !promises.isEmpty()) {
			for (ContractPromise promise : promises) {
				Item item = indexedContainer.addItem(promise);
				
				Button btnUpdate = ComponentLayoutFactory.getDefaultButton("edit", FontAwesome.EDIT, 70);
				Button btnDelete = ComponentLayoutFactory.getDefaultButton("delete", FontAwesome.TRASH_O, 70);
				btnUpdate.setData(promise.getId());
				btnDelete.setData(promise.getId());
				
				item.getItemProperty(ContractPromise.ID).setValue(promise.getId());
				item.getItemProperty(ContractPromise.CREATEDATE).setValue(promise.getCreateDate());
				item.getItemProperty(ContractPromise.CREATEDBY).setValue(promise.getCreatedBy());
				item.getItemProperty(ContractPromise.PROMISETYPE).setValue(promise.getPromiseType() != null ?
						promise.getPromiseType().getDescLocale() : StringUtils.EMPTY);
				item.getItemProperty(ContractPromise.PROMISEAMOUNT).setValue(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(promise.getPromiseAmount()), "###,##0.00"));
				item.getItemProperty(ContractPromise.PROMISEDATE).setValue(promise.getPromiseDate());
				item.getItemProperty(ContractPromise.REMARK).setValue(promise.getRemark());
				item.getItemProperty(ContractPromise.PROMISESTATUS).setValue(promise.getPromiseStatus() != null ? 
						promise.getPromiseStatus().getDescLocale() : StringUtils.EMPTY);
				item.getItemProperty(ContractPromise.OPTIONS).setValue(getButtonLayouts(btnUpdate, btnDelete));
				
				btnUpdate.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = -6661121931448622453L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnUpdate.getData();
						ContractPromise promise = ENTITY_SRV.getById(ContractPromise.class, id);
						UI.getCurrent().addWindow(new PromiseWindowFormPanel(PromiseTablePanel.this, promise));
					}
				});
				
				btnDelete.addClickListener(new ClickListener() {


					/** */
					private static final long serialVersionUID = -6994585149308456922L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						Long id = (Long) btnDelete.getData();
						ContractPromise promise = ENTITY_SRV.getById(ContractPromise.class, id);
						ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
								new String[] {promise.getId().toString()}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = -103949213884672107L;

							/**
							 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
							 */
							@Override
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									try {
										logger.debug("[>> deletePromise]");
										
										ENTITY_SRV.delete(promise);
										
										logger.debug("This item " + promise.getId() + "deleted successfully !");
										logger.debug("[<< deletePromise]");
										dialog.close();
										ComponentLayoutFactory.getNotificationDesc(promise.getId().toString(), "item.deleted.successfully");
										assignValues(contract);
									} catch (Exception e) {
										e.printStackTrace();
										ComponentLayoutFactory.displayErrorMsg("msg.error.technical");
									}
								}
							}
						});
					}
				});
			}
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			ContractPromise promise = ContractPromise.createInstance();
			promise.setContract(this.contract);
			UI.getCurrent().addWindow(new PromiseWindowFormPanel(this, promise));
		}
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			if (getItemSelectedId() != null) {
				ContractPromise promise = ENTITY_SRV.getById(ContractPromise.class, getItemSelectedId());
				UI.getCurrent().addWindow(new PromiseWindowFormPanel(PromiseTablePanel.this, promise));
			}
		}
	}
		
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ContractPromise.ID).getValue());
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
}
