package com.nokor.efinance.ra.ui.panel.dealer.attribute;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAttribute;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Dealer Attribute Form Panel
 * @author bunlong.taing
 */
public class DealerAttributeFormPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {
	/** */
	private static final long serialVersionUID = 7553102937844629943L;
	
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private SimplePagedTable<DealerAttribute> pagedTable;

	private Dealer dealer;
	private Item selectedItem;
	
	public DealerAttributeFormPanel() {
		setSizeFull();
		setMargin(false);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		btnEdit = new NativeButton(I18N.message("edit"));
		btnEdit.setIcon(FontAwesome.EDIT);
		btnEdit.addClickListener(this);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(this);
		createPagedTable();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * Create table
	 */
	private void createPagedTable() {
		pagedTable = new SimplePagedTable<DealerAttribute>(createColumnDefinition());
		pagedTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = 4679300819463910744L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					edit();
				}
			}
		});
	}
	
	/**
	 * Create ColumnDefinition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.ASSETMAKE + "." + AssetMake.DESCLOCALE, I18N.message("asset.make"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.ASSETCATEGORY + "." + AssetCategory.DESCLOCALE, I18N.message("asset.category"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.INSURANCECOVERAGEDURATION, I18N.message("insurance.coverage.duration"), Integer.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.CONTRACTFEE, I18N.message("contract.fee"), Double.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.TICOMMISSION1AMOUNT, I18N.message("commission.1"), Double.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DealerAttribute.COMMISSION2ENABLED, I18N.message("commission.2.enabled"), Boolean.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * Set table data Source
	 * @param campaignAssetMakes
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<DealerAttribute> dealerAttributes) {
		Container container = pagedTable.getContainerDataSource();
		if (dealerAttributes != null) {
			for (DealerAttribute attribute : dealerAttributes) {
				Item item = container.addItem(attribute.getId());
				item.getItemProperty(DealerAttribute.ID).setValue(attribute.getId());
				item.getItemProperty(DealerAttribute.ASSETMAKE + "." + AssetMake.DESCLOCALE).setValue(attribute.getAssetMake() != null ? attribute.getAssetMake().getDescLocale() : "");
				item.getItemProperty(DealerAttribute.ASSETCATEGORY + "." + AssetCategory.DESCLOCALE).setValue(attribute.getAssetCategory() != null ? attribute.getAssetCategory().getDescLocale() : "");
				item.getItemProperty(DealerAttribute.INSURANCECOVERAGEDURATION).setValue(attribute.getInsuranceCoverageDuration());
				item.getItemProperty(DealerAttribute.CONTRACTFEE).setValue(attribute.getTiContractFeeAmount());
				item.getItemProperty(DealerAttribute.TICOMMISSION1AMOUNT).setValue(attribute.getTiCommission1Amount());
				item.getItemProperty(DealerAttribute.COMMISSION2ENABLED).setValue(attribute.isCommission2Enabled());
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Assign values to form
	 * @param campaignId
	 */
	public void assignValues(Dealer dealer) {
		reset();
		if (dealer != null) {
			this.dealer = dealer;
			setTableDataSource(dealer.getDealerAttributes());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		dealer = null;
		selectedItem = null;
		pagedTable.removeAllItems();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			DealerAttributePopupPanel popupPanel = new DealerAttributePopupPanel(this);
			popupPanel.assignValue(null);
			UI.getCurrent().addWindow(popupPanel);
		} else if (event.getButton() == btnEdit) {
			edit();
		} else if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * Edit
	 */
	private void edit() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			Long attributeId = (Long) selectedItem.getItemProperty(DealerAttribute.ID).getValue();
			DealerAttributePopupPanel popupPanel = new DealerAttributePopupPanel(this);
			popupPanel.assignValue(attributeId);
			UI.getCurrent().addWindow(popupPanel);
		}
	}
	
	/**
	 * Delete
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty(DealerAttribute.ID).getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -9064618014268961083L;
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						logger.debug("[ >> Delete DealerAttribute ]");
						DEA_SRV.delete(DealerAttribute.class, id);
						logger.debug("This DealerAttribute: " + id + " deleted successfully!");
						logger.debug("[ << Delete DealerAttribute ]");
					    dialog.close();
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
								new String[]{id.toString()}), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
						refresh();
					}
				}
			});
		}
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		assignValues(DEA_SRV.getById(Dealer.class, dealer.getId()));
	}
	
	/**
	 * @return
	 */
	public Dealer getDealer() {
		return dealer;
	}

}
