package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAssetMake;
import com.nokor.efinance.core.dealer.service.DealerAssetMakeRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.ra.ui.panel.asset.make.popup.AssetMakeSelectPanel;
import com.nokor.efinance.ra.ui.panel.asset.make.popup.AssetMakeSelectPanel.SelectListener;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * Dealer brands form Panel
 * @author uhout.cheng
 */
public class DealerAssetMakesFormPanel extends AbstractControlPanel implements ClickListener, SelectListener {

	/** */
	private static final long serialVersionUID = 8405862504207302833L;

	private Button btnAdd;
	private Button btnDelete;
	
	private SimplePagedTable<DealerAssetMake> pagedTable;
	private AssetMakeSelectPanel selectPopup;
	private Dealer dealer;
	private Item selectedItem = null;
	
	/**
	 * 
	 */
	public DealerAssetMakesFormPanel() {
		selectPopup = new AssetMakeSelectPanel();
		selectPopup.setSelectListener(this);
		setMargin(true);
		setSpacing(true);
		createForm();
	}
	
	/**
	 * 
	 */
	private void createForm() {
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(this);
		createPagedTable();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 */
	private void createPagedTable() {
		pagedTable = new SimplePagedTable<DealerAssetMake>(createColumnDefinition());
		pagedTable.addItemClickListener(new ItemClickListener() {
		
			/** */
			private static final long serialVersionUID = 4781857379260589711L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(DealerAssetMake.ASSETMAKE, I18N.message("name"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
	
	/**
	 * @param dealerAssetMakes
	 */
	@SuppressWarnings("unchecked")
	private void setTableDataSource(List<DealerAssetMake> dealerAssetMakes) {
		selectedItem = null;
		pagedTable.removeAllItems();
		Container container = pagedTable.getContainerDataSource();
		if (dealerAssetMakes != null) {
			for (DealerAssetMake dealerAssetMake : dealerAssetMakes) {
				Item item = container.addItem(dealerAssetMake.getId());
				item.getItemProperty(DealerAssetMake.ID).setValue(dealerAssetMake.getId());
				AssetMake assetMake = dealerAssetMake.getAssetMake();
				if (assetMake != null) {
					item.getItemProperty(DealerAssetMake.ASSETMAKE).setValue(assetMake.getDescLocale());
				}
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		reset();
		if (dealer != null) {
			this.dealer = dealer;
			DealerAssetMakeRestriction restriction = new DealerAssetMakeRestriction();
			restriction.setDealerId(dealer.getId());
			setTableDataSource(ENTITY_SRV.list(restriction));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		dealer = null;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			selectPopup.reset();
			selectPopup.show();
		} else if (event.getButton() == btnDelete) {
			delete();
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
			final Long id = (Long) selectedItem.getItemProperty(DealerAssetMake.ID).getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {
				
				/** */
				private static final long serialVersionUID = 9211251115698033475L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[ >> Delete DealerBrand ]");
						ENTITY_SRV.delete(DealerAssetMake.class, id);
						logger.debug("DealerBrand id: " + id + " deleted successfully!");
						logger.debug("[ << Delete DealerBrand ]");
					    dialog.close();
						ComponentLayoutFactory.getNotificationDesc(id.toString(), "item.deleted.successfully");
						assignValues(dealer);
					}
				}
			});
		}
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.dealer.brand.popup.DealerBrandPopupSelectPanel.SelectListener#onSelected(java.util.List, java.util.List)
	 */
	@Override
	public void onSelected(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			List<DealerAssetMake> dealerAssetMakes = new ArrayList<>();
			for (Long assMakId : ids) {
				DealerAssetMake dealerAssetMake = new DealerAssetMake();
				dealerAssetMake.setDealer(dealer);
				dealerAssetMake.setAssetMake(ENTITY_SRV.getById(AssetMake.class, assMakId));
				dealerAssetMakes.add(dealerAssetMake);
			}
			ENTITY_SRV.saveOrUpdateBulk(dealerAssetMakes);
			assignValues(dealer);
		}
	}

}
