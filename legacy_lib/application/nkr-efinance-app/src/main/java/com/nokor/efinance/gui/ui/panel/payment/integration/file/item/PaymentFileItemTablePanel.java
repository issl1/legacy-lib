package com.nokor.efinance.gui.ui.panel.payment.integration.file.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * PaymentFileItem Table Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentFileItemTablePanel extends AbstractTabPanel implements FinServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = -2475990293479321273L;
	
	private SimpleTable<Entity> simpleTable;
	private Long selectedItemId;
	private Long paymentFileId;
	private PaymentFileItemPopupPanel popupPanel;
	
	private ERefDataComboBox<EPaymentChannel> cbxChannel;
	private AutoDateField dfPaymentDateFrom;
	private AutoDateField dfPaymentDateTo;

	private TextField txtContractId;
	
	private Button btnSearch;
	private Button btnReset;
	
	private NativeButton btnAllocate;
	
	/**
	 * PaymentFileItem Table Panel
	 */
	public PaymentFileItemTablePanel() {
		setSizeFull();
		setCaption(I18N.message("payment.file.items"));
		popupPanel = new PaymentFileItemPopupPanel(I18N.message("payment.file.item"), this);
		
		NativeButton btnView = new NativeButton(I18N.message("view"));
		btnView.setIcon(FontAwesome.EYE);
		btnView.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -4533453227364402353L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItemId == null) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
							MessageBox.Icon.WARN, I18N.message("msg.info.view.item.not.selected"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					popupPanel.assignValues(selectedItemId);
					UI.getCurrent().addWindow(popupPanel);
				}
			}
		});
		
		btnAllocate = new NativeButton(I18N.message("allocate"));
		btnAllocate.setVisible(false);
		btnAllocate.setIcon(FontAwesome.SHARE_SQUARE);
		btnAllocate.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -5696303596172437928L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItemId == null) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
							MessageBox.Icon.WARN, I18N.message("msg.info.allocate.item.not.selected"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.allocate"), new ConfirmDialog.Listener() {
						/** */
						private static final long serialVersionUID = -8062374836771869149L;
						/**
						 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
						 */
						@Override
						public void onClose(ConfirmDialog dialog) {
					        if (dialog.isConfirmed()) {
					        	PaymentFileItem item = PAYMENT_ALLOCATION_SRV.getById(PaymentFileItem.class, selectedItemId);
								PAYMENT_ALLOCATION_SRV.allocatePaymentFileItem(item);
								refresh();
					    		Notification.show(I18N.message("msg.info.allocate.successfully"), Type.HUMANIZED_MESSAGE);
					        }
					    }
					});
					
				}
			}
		});
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnView);
		navigationPanel.addButton(btnAllocate);
		
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getSearchLayout() {
		cbxChannel = new ERefDataComboBox<>(EPaymentChannel.values());
		cbxChannel.setWidth(180, Unit.PIXELS);
		dfPaymentDateFrom = ComponentFactory.getAutoDateField();
		dfPaymentDateTo = ComponentFactory.getAutoDateField();
		
		
		txtContractId = ComponentFactory.getTextField(50, 180);
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		GridLayout filterGridLayout = ComponentLayoutFactory.getGridLayout(8, 1);
		filterGridLayout.setSpacing(true);
		
		filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("channel"));
		filterGridLayout.addComponent(cbxChannel);
		filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.id"));
		filterGridLayout.addComponent(txtContractId);
		filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("payment.from"));
		filterGridLayout.addComponent(dfPaymentDateFrom);
		filterGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("to"));
		filterGridLayout.addComponent(dfPaymentDateTo);
	
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout filterLayout = new VerticalLayout();
		filterLayout.setSpacing(true);
		filterLayout.addComponent(filterGridLayout);
		filterLayout.addComponent(buttonLayout);
		filterLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filters"));
		fieldSet.setContent(filterLayout);
		
		Panel filterPanel = new Panel(fieldSet);
		filterPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return filterPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		cbxChannel.setSelectedEntity(null);
		dfPaymentDateFrom.setValue(null);
		dfPaymentDateTo.setValue(null);
		txtContractId.setValue(StringUtils.EMPTY);
		simpleTable.removeAllItems();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		simpleTable = new SimpleTable<Entity>(createColumnDefinitions());
		simpleTable.setCaption(I18N.message("payment.file.items"));
		simpleTable.setPageLength(20);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 8255295426958144769L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Long) event.getItem().getItemProperty(PaymentFileItem.ID).getValue();
				PaymentFileItem item = PAYMENT_ALLOCATION_SRV.getById(PaymentFileItem.class, selectedItemId);
				if (PaymentFileWkfStatus.NEW.equals(item.getWkfStatus())) {
					btnAllocate.setVisible(true);
				} else {
					btnAllocate.setVisible(false);
				}
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					popupPanel.assignValues(selectedItemId);
					UI.getCurrent().addWindow(popupPanel);
				}
			}
		});
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(getSearchLayout());
		content.addComponent(simpleTable);
        return content;
	}
	
	/**
	 * Create ColumnDefinitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.ID, I18N.message("id"), Long.class, Align.LEFT, 50));		
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.SEQUENCE, I18N.message("sequence"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.PAYMENTCHANNEL, I18N.message("channel"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.CREATEDATE, I18N.message("upload.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.CUSTOMERID, I18N.message("customer.id"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.CUSTOMERNAME, I18N.message("customer.name"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.CUSTOMERREF1, I18N.message("contract.id"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.CUSTOMERREF2, I18N.message("engine.number"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.TRANSACTIONKIND, I18N.message("transaction.kind"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.TRANSACTIONCODE, I18N.message("transaction.code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.AMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 100));
//		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.RECEIVENO, I18N.message("receive.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PaymentFileItem.WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 80));
		
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		this.reset();
		btnAllocate.setVisible(false);
		simpleTable.removeAllItems();
		selectedItemId = null;
		paymentFileId = null;
		if (id != null) {
			paymentFileId = id;
			setIndexedContainer(getPaymentFileItems());
		}
	}
	
	/**
	 * 
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<PaymentFileItem> items) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (items != null && !items.isEmpty()) {
			long index = 0l;
			for (PaymentFileItem fileItem : items) {
				Item item = container.addItem(index);
				item.getItemProperty(PaymentFileItem.ID).setValue(fileItem.getId());
//				item.getItemProperty(PaymentFileItem.SEQUENCE).setValue(fileItem.getSequence());
				item.getItemProperty(PaymentFileItem.PAYMENTCHANNEL).setValue(fileItem.getPaymentChannel() == null ? StringUtils.EMPTY
						: fileItem.getPaymentChannel().getDescLocale());
				item.getItemProperty(PaymentFileItem.CREATEDATE).setValue(fileItem.getCreateDate());
				item.getItemProperty(PaymentFileItem.PAYMENTDATE).setValue(fileItem.getPaymentDate());
//				item.getItemProperty(PaymentFileItem.CUSTOMERID).setValue(fileItem.getCustomerId());
//				item.getItemProperty(PaymentFileItem.CUSTOMERNAME).setValue(fileItem.getCustomerName());
				item.getItemProperty(PaymentFileItem.CUSTOMERREF1).setValue(fileItem.getCustomerRef1());
//				item.getItemProperty(PaymentFileItem.CUSTOMERREF2).setValue(fileItem.getCustomerRef2());
//				item.getItemProperty(PaymentFileItem.TRANSACTIONKIND).setValue(fileItem.getTransactionKind());
//				item.getItemProperty(PaymentFileItem.TRANSACTIONCODE).setValue(fileItem.getTransactionCode());
				item.getItemProperty(PaymentFileItem.AMOUNT).setValue(fileItem.getAmount());
//				item.getItemProperty(PaymentFileItem.RECEIVENO).setValue(fileItem.getReceiveNo());
				item.getItemProperty(PaymentFileItem.WKFSTATUS).setValue(fileItem.getWkfStatus().getDescLocale());
				index++;
			}
		}
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		assignValues(paymentFileId);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<PaymentFileItem> getPaymentFileItems() {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		restrictions.setPaymentFileId(this.paymentFileId);
		if (StringUtils.isNotEmpty(txtContractId.getValue())) {
			restrictions.setContractReference(txtContractId.getValue());
		}
		
		if (dfPaymentDateFrom.getValue() != null) {
			restrictions.setPaymentDateFrom(dfPaymentDateFrom.getValue());
		}
		if (dfPaymentDateTo.getValue() != null) {
			restrictions.setPaymentDateTo(dfPaymentDateTo.getValue());
		}
		
		if (cbxChannel.getSelectedEntity() != null) {
			List<EPaymentChannel> channels = new ArrayList<EPaymentChannel>();
			channels.add(cbxChannel.getSelectedEntity());
			restrictions.setPaymentChannels(channels);
		}
		restrictions.addOrder(Order.desc(PaymentFileItem.ID));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSearch)) {
			setIndexedContainer(getPaymentFileItems());
		} else if (event.getButton().equals(btnReset)) {
			this.reset();
		}
	}

}
