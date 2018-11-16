package com.nokor.efinance.ra.ui.panel.insurance.campaign;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.CampaignDealer;
import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.efinance.core.financial.model.InsuranceCampaignDealer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.ra.ui.panel.dealer.DealersSelectPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class InsuranceCampaignDealerPanel extends AbstractTabPanel implements AppServicesHelper, FMEntityField {

	/** */
	private static final long serialVersionUID = -2742220260490298569L;

	private InsuranceCampaign insuranceCampaign;	
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<CampaignDealer> pagedTable;
	
    /**
     */
   	public InsuranceCampaignDealerPanel() {
        super();
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
				DealersSelectPanel dialog = new DealersSelectPanel();
				dialog.setInsuranceCampaignId(insuranceCampaign.getId());
				
				dialog.show(new DealersSelectPanel.Listener() {
					/**
					 */
					private static final long serialVersionUID = 3391607182647042363L;

					@Override
					public void onClose(DealersSelectPanel dialog) {
						List<Long> ids = dialog.getSelectedIds();
						if (ids != null && !ids.isEmpty()) {
							List<InsuranceCampaignDealer> insuranceCampaignDealers = new ArrayList<>();
							for (Long id : ids) {
								insuranceCampaignDealers.add(new InsuranceCampaignDealer(ENTITY_SRV.getById(Dealer.class, id), insuranceCampaign));
							}
							ENTITY_SRV.saveOrUpdateBulk(insuranceCampaignDealers);
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
			}
		});
		
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnDelete);		
		addComponent(navigationPanel, 0);
   	}
        
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected Component createForm() {		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<CampaignDealer>(this.columnDefinitions);
		
	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);		
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		
		return contentLayout;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(INTERNAL_CODE, I18N.message("dealershop.id"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(NAME_EN, I18N.message("dealer.name"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(LICENCE_NO, I18N.message("commercial.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("dealer.type", I18N.message("dealer.type"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	
	/**
	 * @param dealers
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<InsuranceCampaignDealer> insuranceCampaignDealers) {
		Container indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (insuranceCampaignDealers != null && !insuranceCampaignDealers.isEmpty()) {
			for (InsuranceCampaignDealer insuranceCampaignDealer : insuranceCampaignDealers) {
				Item item = indexedContainer.addItem(insuranceCampaignDealer.getId());
				item.getItemProperty(ID).setValue(insuranceCampaignDealer.getId());
				item.getItemProperty(INTERNAL_CODE).setValue(insuranceCampaignDealer.getDealer().getCode());
				item.getItemProperty(NAME_EN).setValue(insuranceCampaignDealer.getDealer().getNameEn());
				item.getItemProperty(LICENCE_NO).setValue(insuranceCampaignDealer.getDealer().getLicenceNo());
				item.getItemProperty("dealer.type").setValue(insuranceCampaignDealer.getDealer().getDealerType() != null ? insuranceCampaignDealer.getDealer().getDealerType().getDescEn() : "");
			}
		}
	}
	
	/**
	 * 
	 * @param insCamId
	 */
	public void assignValues(InsuranceCampaign insuranceCampaign) {
		super.reset();
		if (insuranceCampaign != null) {
			this.insuranceCampaign = insuranceCampaign;
			setIndexedContainer(this.insuranceCampaign.getInsuranceCampaignDealers());
		}
	}
	
	/**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
     */
	@Override
	public void reset() {
		super.reset();
		insuranceCampaign = new InsuranceCampaign();
	}
}
