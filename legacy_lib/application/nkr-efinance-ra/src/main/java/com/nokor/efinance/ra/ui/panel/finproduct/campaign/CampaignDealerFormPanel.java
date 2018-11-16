package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignDealer;
import com.nokor.efinance.core.financial.service.CampaignDealerRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.efinance.ra.ui.panel.dealer.DealersSelectPanel;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignDealerFormPanel extends AbstractTabPanel implements FinServicesHelper, ValueChangeListener, DealerEntityField {

	/** */
	private static final long serialVersionUID = -8528436215976239342L;
	
	private Long campaignId;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<CampaignDealer> pagedTable;
	private Item selectedItem = null;
	
	private CheckBox cbValidForAllDealers;
	private VerticalLayout dealerContentPanel;
	
	public CampaignDealerFormPanel() {
		super();
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbValidForAllDealers = new CheckBox(I18N.message("valid.for.all.dealers"));
		cbValidForAllDealers.addValueChangeListener(this);
				
		NavigationPanel navigationPanel = new NavigationPanel();
		Button btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -283512118526842700L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				DealersSelectPanel dialog = new DealersSelectPanel();
				dialog.setCampaignId(campaignId);
				
				dialog.show(new DealersSelectPanel.Listener() {
					/** */
					private static final long serialVersionUID = -5890999882421853005L;
					/**
					 * @see com.nokor.efinance.ra.ui.panel.dealer.DealersSelectPanel.Listener#onClose(com.nokor.efinance.ra.ui.panel.dealer.DealersSelectPanel)
					 */
					@Override
					public void onClose(DealersSelectPanel dialog) {
						List<Long> ids = dialog.getSelectedIds();
						if (ids != null && !ids.isEmpty()) {
							List<CampaignDealer> campaignDealers = new ArrayList<>();
							Campaign campaign = ENTITY_SRV.getById(Campaign.class, campaignId);
							for (Long id : ids) {
								CampaignDealer campaignDealer = new CampaignDealer();
								campaignDealer.setDealer(ENTITY_SRV.getById(Dealer.class, id));
								campaignDealer.setCampaign(campaign);
								campaignDealers.add(campaignDealer);
							}
							ENTITY_SRV.saveOrUpdateBulk(campaignDealers);
							assignValues(campaignId);
						}
					}
				});
				dialog.setWidth("780px");
				dialog.setHeight("520px");
			}
		});
			
		Button btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 4827578661874246954L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
					
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);
			
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<CampaignDealer>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = -6871704272222980434L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
		
		dealerContentPanel = new VerticalLayout();
		dealerContentPanel.setSpacing(true);
		dealerContentPanel.addComponent(navigationPanel);
		dealerContentPanel.addComponent(pagedTable);
		dealerContentPanel.addComponent(pagedTable.createControls());
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(cbValidForAllDealers);
		contentLayout.addComponent(dealerContentPanel);
		
		return contentLayout;
	}
	
	/**
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -1278300263633872114L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
					    logger.debug("[>> deleteCampaignDealer]");
						ENTITY_SRV.delete(CampaignDealer.class, id);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteCampaignDealer]");
					    dialog.close();
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
								new String[]{id.toString()}), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
						assignValues(campaignId);
						selectedItem = null;
					}
				}
			});
		}
	}
	
	/**
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		reset();
		if (campaignId != null) {
			this.campaignId = campaignId;
			Campaign campaign = ENTITY_SRV.getById(Campaign.class, campaignId);
			cbValidForAllDealers.removeValueChangeListener(this);
			cbValidForAllDealers.setValue(campaign.isValidForAllDealers());
			cbValidForAllDealers.addValueChangeListener(this);
			dealerContentPanel.setVisible(!campaign.isValidForAllDealers());
			
			if (!campaign.isValidForAllDealers()) {
				CampaignDealerRestriction restriction = new CampaignDealerRestriction();
				restriction.setCampaignId(campaignId);
				pagedTable.setContainerDataSource(getCampaignDealerIndexedContainer(ENTITY_SRV.list(restriction)));
			}
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * 
	 * @param campaign
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getCampaignDealerIndexedContainer(List<CampaignDealer> campaignDealers) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!campaignDealers.isEmpty()) {
			for (CampaignDealer campaignDealer : campaignDealers) {
				Dealer dealer = campaignDealer.getDealer();
				Item item = indexedContainer.addItem(campaignDealer.getId());
				if (dealer != null) {
					String dealerType = "";
					if (dealer.getParent() != null) {
						dealerType = I18N.message("brand");
					} else {
						dealerType = I18N.message("head");
					}
					List<DealerAddress> dealerAddresses = dealer.getDealerAddresses();
					String subDistrict = "";
					String district = "";
					String province = "";
					if (!dealerAddresses.isEmpty()) {
						Address address = dealerAddresses.get(0).getAddress();
						if (address != null) {
							subDistrict = address.getCommune() == null ? "" : address.getCommune().getDesc();
							district = address.getDistrict() == null ? "" :  address.getDistrict().getDesc();
							province = address.getProvince() == null ? "" : address.getProvince().getDesc();
						}
					}
					item.getItemProperty(ID).setValue(campaignDealer.getId());
					item.getItemProperty(NAME_EN).setValue(dealer.getNameEn());
					item.getItemProperty(DEALER_TYPE).setValue(dealerType);
					item.getItemProperty(COMMUNE).setValue(subDistrict);
					item.getItemProperty(DISTRICT).setValue(district);
					item.getItemProperty(PROVINCE).setValue(province);
				}
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(NAME_EN, I18N.message("dealer.name"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(COMMUNE, I18N.message("subdistrict"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DISTRICT, I18N.message("district"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PROVINCE, I18N.message("province"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		campaignId = null;
		cbValidForAllDealers.setValue(false);
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (campaignId != null) {
			Campaign campaign = CAM_SRV.getById(Campaign.class, campaignId);
			if (cbValidForAllDealers.getValue()) {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.valid.all.dealers"), new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -1278300263633872114L;
	
					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							campaign.setValidForAllDealers(true);
							CAM_SRV.validAllDealers(campaign);
						}
						if (dialog.isCanceled()) {
							cbValidForAllDealers.setValue(false);
						}
					}				
				});
			} else {
				campaign.setValidForAllDealers(false);
				CAM_SRV.validAllDealers(campaign);
			}
			dealerContentPanel.setVisible(!cbValidForAllDealers.getValue());
		}
	}	
}
