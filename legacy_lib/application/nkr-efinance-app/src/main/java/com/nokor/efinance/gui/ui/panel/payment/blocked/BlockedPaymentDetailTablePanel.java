package com.nokor.efinance.gui.ui.panel.payment.blocked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.MPaymentFileItem;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.ARPaymentPopupWindowPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
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
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class BlockedPaymentDetailTablePanel extends VerticalLayout implements FinServicesHelper, MPaymentFileItem, ItemClickListener, SelectedItem, ClickListener {

	/** */
	private static final long serialVersionUID = 5317369363607245155L;
	
	private final static Logger logger = LoggerFactory.getLogger(BlockedPaymentDetailTablePanel.class);
	
	//private SimpleTable<PaymentFileItem> simpleTable;
	
	private SimplePagedTable<PaymentFileItem> simplePagedTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private Item selectedItem;
	
//	private BlockedPaymentDetailInfosPanel infosPanel;
	private EWkfStatus wkfStatus;
	
	private PaymentFileItemRestriction restrictions;
	
	private Button btnSubmit;
	private List<Long> selectedIds = new ArrayList<>();
	private boolean selectAll = false;

	/**
	 * 
	 * @param infosPanel
	 * @param wkfStatus
	 */
	public BlockedPaymentDetailTablePanel(BlockedPaymentDetailInfosPanel infosPanel, EWkfStatus wkfStatus) {
//		this.infosPanel = infosPanel;
		this.wkfStatus = wkfStatus;
		this.columnDefinitions = getColumnDefinitions();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
		simplePagedTable.setSizeFull();
		simplePagedTable.setCaption(I18N.message("details"));
		simplePagedTable.addItemClickListener(this);
		simplePagedTable.setFooterVisible(true);
		simplePagedTable.setColumnFooter(PAYMENTDATE, I18N.message("total"));
		
		btnSubmit = ComponentLayoutFactory.getDefaultButton("submit", FontAwesome.ARROW_CIRCLE_O_RIGHT, 70);
		btnSubmit.setVisible(PaymentFileWkfStatus.MATCHED.equals(wkfStatus));
		btnSubmit.addClickListener(this);
		
		if (PaymentFileWkfStatus.MATCHED.equals(wkfStatus)) {
			simplePagedTable.setColumnIcon(SELECT, FontAwesome.CHECK);
			simplePagedTable.addHeaderClickListener(new HeaderClickListener() {
				
				/** */
				private static final long serialVersionUID = 6354185744451187290L;

				/**
				 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
				 */
				@Override
				public void headerClick(HeaderClickEvent event) {
					if (event.getPropertyId() == SELECT) {
						selectAll = !selectAll;
						@SuppressWarnings("unchecked")
						Collection<Long> ids = (Collection<Long>) simplePagedTable.getItemIds();
						for (Long id : ids) {
							Item item = simplePagedTable.getItem(id);
							CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
							cbSelect.setImmediate(true);
							cbSelect.setValue(selectAll);
						}
					}
				}
			});
		}
		
		setSpacing(true);
		addComponent(btnSubmit);
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
		simplePagedTable.setPageLength(25);
	}
	
	/**
	 * 
	 * @param paymentFileItems
	 */
	private void assignValues(List<PaymentFileItem> paymentFileItems) {
//		reset();
		simplePagedTable.setContainerDataSource(setContainer(paymentFileItems));
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
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		if (PaymentFileWkfStatus.MATCHED.equals(this.wkfStatus)) {
			columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 40));
		}
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_ID, I18N.message("contract.id"), String.class, Align.LEFT, 70));
		if (PaymentFileWkfStatus.UNMATCHED.equals(this.wkfStatus)) {
			columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 70));
		}
		if (PaymentFileWkfStatus.UNMATCHED.equals(this.wkfStatus) || PaymentFileWkfStatus.OVER.equals(this.wkfStatus)
				|| PaymentFileWkfStatus.SUSPENDED.equals(this.wkfStatus) || PaymentFileWkfStatus.MATCHED.equals(this.wkfStatus)) {
			columnDefinitions.add(new ColumnDefinition("contract.status", I18N.message("contract.status"), String.class, Align.LEFT, 130));
		}
		columnDefinitions.add(new ColumnDefinition(STAFF_IN_CHARGE, I18N.message("staff.in.charge"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("previousWkfStatus", I18N.message("previous.status"), String.class, Align.LEFT, 130));		
		columnDefinitions.add(new ColumnDefinition(PAYMENTMETHOD, I18N.message("payment.method"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(PAYMENTCHANNEL, I18N.message("channel"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("upload.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(AMOUNT, I18N.message("amount"), String.class, Align.RIGHT, 130));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param paymentFileItems
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer setContainer(List<PaymentFileItem> paymentFileItems) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		double totalAmount = 0d;
		if (paymentFileItems != null && !paymentFileItems.isEmpty()) {
			for (PaymentFileItem paymentFileItem : paymentFileItems) {
				Item item = indexedContainer.addItem(paymentFileItem.getId());
				if (PaymentFileWkfStatus.MATCHED.equals(this.wkfStatus)) {
					item.getItemProperty(SELECT).setValue(getRenderSelected(paymentFileItem.getId()));
				}
				
				String paymentMethodDesc = StringUtils.EMPTY;
				if (paymentFileItem.getPaymentMethod() != null) {
					paymentMethodDesc = paymentFileItem.getPaymentMethod().getDescLocale();
				}
				
				item.getItemProperty(ID).setValue(paymentFileItem.getId());
				
				Contract contract = CONT_SRV.getByReference(StringUtils.trim(paymentFileItem.getCustomerRef1()));
				
				if (PaymentFileWkfStatus.UNMATCHED.equals(this.wkfStatus)) {
					Dealer dealer = DEA_SRV.getByCode(Dealer.class, StringUtils.trim(paymentFileItem.getDealerNo()));
					String type = StringUtils.EMPTY;
					if (contract != null) {
						type = I18N.message("customer");
					} else if (dealer != null) {
						type = I18N.message("dealer");
					}
					item.getItemProperty(TYPE).setValue(type);
				}
				
				if (contract != null) {
					SecUser collectionUser = COL_SRV.getCollectionUser(contract.getId());
					if (collectionUser != null) {
						item.getItemProperty(STAFF_IN_CHARGE).setValue(collectionUser.getDesc());
					}
					item.getItemProperty(CONTRACT_ID).setValue(contract.getReference());
				}
				
				if (PaymentFileWkfStatus.UNMATCHED.equals(this.wkfStatus) || PaymentFileWkfStatus.OVER.equals(this.wkfStatus)
						|| PaymentFileWkfStatus.SUSPENDED.equals(this.wkfStatus) || PaymentFileWkfStatus.MATCHED.equals(this.wkfStatus)) {
					if (contract != null) {
						item.getItemProperty("contract.status").setValue(contract.getWkfStatus().getDescLocale());	
					}
				}
				
				item.getItemProperty("previousWkfStatus").setValue(paymentFileItem.getPreviousWkfStatus() != null ? paymentFileItem.getPreviousWkfStatus().getDescLocale() : "");
				item.getItemProperty(PAYMENTMETHOD).setValue(paymentMethodDesc);
				item.getItemProperty(PAYMENTCHANNEL).setValue(getPaymentChannelCodeDesc(paymentFileItem));
				item.getItemProperty(CREATEDATE).setValue(paymentFileItem.getCreateDate());
				item.getItemProperty(PAYMENTDATE).setValue(paymentFileItem.getPaymentDate());
				item.getItemProperty(AMOUNT).setValue(MyNumberUtils.formatDoubleToString(
						MyNumberUtils.getDouble(paymentFileItem.getAmount()), "###,##0.00"));
				item.getItemProperty(ACTIONS).setValue(getRenderButton(paymentFileItem.getId()));
				totalAmount += MyNumberUtils.getDouble(paymentFileItem.getAmount());
			}
		}
		simplePagedTable.setColumnFooter(AMOUNT, MyNumberUtils.formatDoubleToString(totalAmount, "###,##0.00"));
		return indexedContainer;
	}
	
	/**
	 * @param payId
	 * @return
	 */
	private CheckBox getRenderSelected(Long payId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(payId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -5541476954662309106L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (checkBox.getValue()) {
					selectedIds.add(id);
				} else {
					selectedIds.remove(id);
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * 
	 * @param paymentFileItem
	 * @return
	 */
	private String getPaymentChannelCodeDesc(PaymentFileItem paymentFileItem) {
		if (paymentFileItem.getPaymentChannel() != null) {
			return paymentFileItem.getPaymentChannel().getCode() + StringUtils.SPACE + "-" + StringUtils.SPACE + paymentFileItem.getPaymentChannel().getDescLocale();
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * @param paymentFileItemId
	 * @return
	 */
	private Button getRenderButton(Long paymentFileItemId) {
		Button btnEdit = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		btnEdit.setData(paymentFileItemId);
		btnEdit.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1212839396830570942L;

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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSubmit)) {
			if (!selectedIds.isEmpty()) {
				for (int i = 0; i < selectedIds.size(); i++) {
					PaymentFileItem paymentFileItem = ENTITY_SRV.getById(PaymentFileItem.class, selectedIds.get(i));
					try {
						PAYMENT_ALLOCATION_SRV.allocatedPayment(paymentFileItem);
						if (PaymentFileWkfStatus.ALLOCATED.equals(paymentFileItem.getWkfStatus())) {
							ComponentLayoutFactory.displaySuccessMsg("msg.info.allocate.successfully");
							refreshAfterSaveOrAllocation();
						} else {
							ComponentLayoutFactory.displayErrorMsg("msg.info.can.not.allocate");
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (!PaymentFileWkfStatus.UNIDENTIFIED.equals(this.wkfStatus)) {
//			infosPanel.assignValue(ENTITY_SRV.getById(PaymentFileItem.class, getItemSelectedId()));
		}
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			showWindow(getItemSelectedId());
		}
	}
	
	/**
	 * 
	 * @param payFileItemId
	 */
	private void showWindow(Long payFileItemId) {
//		IdentifyPaymentPopupWindow window = new IdentifyPaymentPopupWindow(this, this.wkfStatus);
		ARPaymentPopupWindowPanel window = new ARPaymentPopupWindowPanel(null, this, null, this.wkfStatus, false);
		window.assignValues(ENTITY_SRV.getById(PaymentFileItem.class, payFileItemId));
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
	
	/**
	 * 
	 */
	/*private void reset() {
		infosPanel.reset();
	}*/
	
}
