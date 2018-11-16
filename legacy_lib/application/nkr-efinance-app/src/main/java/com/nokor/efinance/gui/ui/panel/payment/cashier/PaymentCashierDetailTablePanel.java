package com.nokor.efinance.gui.ui.panel.payment.cashier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.gl.finwiz.share.domain.AP.BankBranchDTO;
import com.gl.finwiz.share.domain.AP.BankDTO;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.MPaymentFileItem;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.payment.ARPaymentPopupWindowPanel;
import com.nokor.efinance.gui.ui.panel.payment.cashier.filter.PaymentCashierFilterPanel;
import com.nokor.efinance.third.finwiz.client.bank.ClientBank;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentCashierDetailTablePanel extends AbstractControlPanel implements FinServicesHelper, MPaymentFileItem, ItemClickListener, SelectedItem, ClickListener {

	/** */
	private static final long serialVersionUID = -1803577292894993099L;
	
	private Button btnAdd;
	private Button btnRefresh;

	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private PaymentFileItemRestriction restrictions;
	
	private PaymentCashierFilterPanel filterPanel;
	
	private boolean isPendingCheque = false;

	/**
	 * @return the isPendingCheque
	 */
	public boolean isPendingCheque() {
		return isPendingCheque;
	}

	/**
	 * 
	 * @param isPendingCheque
	 */
	public PaymentCashierDetailTablePanel(boolean isPendingCheque) {
		this.isPendingCheque = isPendingCheque;
		btnAdd = new NativeButton(I18N.message("add"), this);
		btnAdd.setIcon(FontAwesome.PLUS);
		btnRefresh = new NativeButton(I18N.message("refresh"), this);
		btnRefresh.setIcon(FontAwesome.REFRESH);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnRefresh);
		
		navigationPanel.setVisible(!isPendingCheque);
		
		simpleTable = new SimpleTable<Entity>(I18N.message("details"), getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(20);
		simpleTable.setFooterVisible(true);
		simpleTable.setColumnFooter(PAYMENTDATE, I18N.message("total"));
		simpleTable.addItemClickListener(this);
		
		filterPanel = new PaymentCashierFilterPanel(this);
		
		setSpacing(true);
		setMargin(true);
		addComponent(navigationPanel);
		addComponent(filterPanel);
		addComponent(simpleTable);
	}
	
	/**
	 * 
	 * @param paymentFileItems
	 */
	private void assignValues(List<PaymentFileItem> paymentFileItems) {
		setIndexedContainer(paymentFileItems);
	}
	
	/**
	 * 
	 * @param restrictions
	 */
	public void refresh(PaymentFileItemRestriction restrictions) {
		this.restrictions = restrictions;
		List<PaymentFileItem> paymentFileItems = ENTITY_SRV.list(restrictions);	
		assignValues(paymentFileItems);
	}
	
	/**
	 * Refresh data after saved/allocation
	 */
	public void refreshAfterSaveOrAllocation() {
		assignValues(ENTITY_SRV.list(this.restrictions));
	}
	
	/**
	 * 
	 * @return
	 */
	private static List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(PAYMENTMETHOD, I18N.message("payment.method"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CHEQUENO, I18N.message("cheque.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(BANKNAME, I18N.message("bank.name"), String.class, Align.LEFT, 100)); 
		columnDefinitions.add(new ColumnDefinition(BANKBRANCH, I18N.message("bank.branch"), String.class, Align.LEFT, 100)); 
		columnDefinitions.add(new ColumnDefinition(OWNER, I18N.message("owner"), String.class, Align.LEFT, 100));		
		columnDefinitions.add(new ColumnDefinition(PAYMENTCHANNEL, I18N.message("channel"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("create.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), HorizontalLayout.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param paymentFileItems
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<PaymentFileItem> paymentFileItems) {
		simpleTable.removeAllItems();
		this.selectedItem = null;
		Container container = simpleTable.getContainerDataSource();
		double totalAmount = 0d;
		if (paymentFileItems != null && !paymentFileItems.isEmpty()) {
			for (PaymentFileItem paymentFileItem : paymentFileItems) {
				Item item = container.addItem(paymentFileItem);
				
				String paymentMethodDesc = StringUtils.EMPTY;
				if (paymentFileItem.getPaymentMethod() != null) {
					paymentMethodDesc = paymentFileItem.getPaymentMethod().getDescLocale();
				}
				
				String paymentChannelDesc = StringUtils.EMPTY;
				if (paymentFileItem.getPaymentChannel() != null) {
					paymentChannelDesc = paymentFileItem.getPaymentChannel().getDescLocale();
				}
				BankBranchDTO bankBranch = null;
				BankDTO bankName = null;
				
				if (paymentFileItem.getBankBranchId() != null) {
					bankBranch = ClientBank.getBankBranchDTO(paymentFileItem.getBankBranchId());
					bankName = ClientBank.getBankDTO(paymentFileItem.getBankId());
				}
				item.getItemProperty(ID).setValue(paymentFileItem.getId());
				item.getItemProperty(PAYMENTMETHOD).setValue(paymentMethodDesc);
				item.getItemProperty(CHEQUENO).setValue(paymentFileItem.getChequeNo());
				item.getItemProperty(BANKBRANCH).setValue(bankBranch == null ? null : bankBranch.getName());
				item.getItemProperty(BANKNAME).setValue(bankName == null ? null : bankName.getName());
				item.getItemProperty(OWNER).setValue(paymentFileItem.getOwner());
				item.getItemProperty(PAYMENTCHANNEL).setValue(paymentChannelDesc);
				item.getItemProperty(CREATEDATE).setValue(paymentFileItem.getCreateDate());
				item.getItemProperty(PAYMENTDATE).setValue(paymentFileItem.getPaymentDate());
				item.getItemProperty(AMOUNT).setValue(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(paymentFileItem.getAmount()), "###,##0.00"));
				item.getItemProperty(PaymentFileItem.WKFSTATUS).setValue(paymentFileItem.getWkfStatus().getDescLocale());
				item.getItemProperty(ACTIONS).setValue(getButtonLayouts(getRenderButtonEdit(paymentFileItem.getId()), getRenderButtonDelete(paymentFileItem.getId())));
				totalAmount += MyNumberUtils.getDouble(paymentFileItem.getAmount());
			}
		}
		simpleTable.setColumnFooter(AMOUNT, MyNumberUtils.formatDoubleToString(totalAmount, "###,##0.00"));
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
	 * @param paymentFileItemId
	 * @return
	 */
	private Button getRenderButtonEdit(Long paymentFileItemId) {
		Button btnEdit = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		btnEdit.setData(paymentFileItemId);
		btnEdit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 3484969484882104383L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Long id = (Long) btnEdit.getData();
				showWindow(id);
			}
		});
		return btnEdit;
	}
	
	/**
	 * @param paymentFileItemId
	 * @return
	 */
	private Button getRenderButtonDelete(Long paymentFileItemId) {
		Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TRASH_O);
		btnDelete.setData(paymentFileItemId);
		btnDelete.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -5004891390482566100L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Long id = (Long) btnDelete.getData();
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {id.toString()}), new ConfirmDialog.Listener() {
					
					/** */
					private static final long serialVersionUID = -2865169763529982560L;

					/**
					 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
					 */
					@Override
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								ENTITY_SRV.delete(PaymentFileItem.class, id);
								ComponentLayoutFactory.displaySuccessMsg(I18N.message("item.deleted.successfully", 
										new String[]{id.toString()}));
								refresh(filterPanel.getRestrictions());
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}
					}
				});
			}
		});
		return btnDelete;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			showWindow(null);
		} else if (event.getButton().equals(btnRefresh)) {
			refresh(filterPanel.getRestrictions());
		} 
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			showWindow(getItemSelectedId());
		}
	}
	
	/**
	 * 
	 * @param payFileItemId
	 */
	private void showWindow(Long payFileItemId) {
		PaymentFileItem paymentFileItem = null;
		EWkfStatus status = null;
		if (payFileItemId != null) {
			paymentFileItem = ENTITY_SRV.getById(PaymentFileItem.class, payFileItemId);
			if (paymentFileItem != null) {
				status = paymentFileItem.getWkfStatus();
			}
		} 
//		PaymentCashierPopupWindow window = new PaymentCashierPopupWindow(this, null, this.isPendingCheque, status);
		ARPaymentPopupWindowPanel window = new ARPaymentPopupWindowPanel(this, null, null, status, true);
		window.assignValues(paymentFileItem);
		UI.getCurrent().addWindow(window);
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
	
}
