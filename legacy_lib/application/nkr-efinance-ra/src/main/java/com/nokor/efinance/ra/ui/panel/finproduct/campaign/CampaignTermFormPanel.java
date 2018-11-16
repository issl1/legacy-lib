package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignTerm;
import com.nokor.efinance.core.financial.model.MCampaignTerm;
import com.nokor.efinance.core.financial.model.Term;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignTermFormPanel extends AbstractTabPanel implements SeuksaServicesHelper, MCampaignTerm {

	/** */
	private static final long serialVersionUID = -1562241538368200896L;
	
	private Campaign campaign;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<CampaignTerm> pagedTable;
	private Item selectedItem = null;
	private EntityComboBox<Term> cbxTerm;
	
	private Window windowTerm;
	private VerticalLayout messagePanel;
	
	public CampaignTermFormPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
				
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -6599613713224048575L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				createTermForm();
			}
		});
			
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -8537132060408859267L;

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
		pagedTable = new SimplePagedTable<CampaignTerm>(this.columnDefinitions);

		pagedTable.addItemClickListener(new ItemClickListener() {
		
			/** */
			private static final long serialVersionUID = -8059311979010964367L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					createTermForm();
					setValueToFrom();
				} 
				
				
			}
		});
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		return contentLayout;
	}
	
	/**
	 * 
	 */
	private void save() {
		CampaignTerm campaignTerm;
		if (selectedItem == null) {
			campaignTerm = new CampaignTerm();
		} else {
			Long id = (Long) selectedItem.getItemProperty("id").getValue();
			campaignTerm = ENTITY_SRV.getById(CampaignTerm.class, id);
		}
			
		campaignTerm.setTerm(cbxTerm.getSelectedEntity());
		campaignTerm.setCampaign(campaign);
		ENTITY_SRV.saveOrUpdate(campaignTerm);
		windowTerm.close();
		assignValues(campaign.getId());
		reset();
		//displaySuccess();
		selectedItem = null;
		
	}

	/**
	 * 
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
				private static final long serialVersionUID = 7461719069188162133L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						logger.debug("[>> deleteCampaignTerm]");
						ENTITY_SRV.delete(CampaignTerm.class, id);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteCampaignTerm]");
					    dialog.close();
						MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
								new String[]{id.toString()}), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
						assignValues(campaign.getId());
						selectedItem = null;
					}
				}
			});
		}
	}
	
	/**
	 * 
	 * @param campaignId
	 */
	public void assignValues(Long campaignId) {
		if (campaignId != null) {
			campaign = ENTITY_SRV.getById(Campaign.class, new Long(campaignId));
			pagedTable.setContainerDataSource(getCampaignTermIndexedContainer(campaign));
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
	private IndexedContainer getCampaignTermIndexedContainer(Campaign campaign) {
		List<CampaignTerm> campaignDealers = getTermsByCampaignId(campaign);
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!campaignDealers.isEmpty()) {
			for (CampaignTerm campaignTerm : campaignDealers) {
				Item item = indexedContainer.addItem(campaignTerm.getId());
				item.getItemProperty(ID).setValue(campaignTerm.getId());
				item.getItemProperty(TERM).setValue(campaignTerm.getTerm() != null ? campaignTerm.getTerm().getDescLocale() : "");
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param campaign
	 * @return
	 */
	private List<CampaignTerm> getTermsByCampaignId(Campaign campaign) {
		BaseRestrictions<CampaignTerm> restrictions = new BaseRestrictions<>(CampaignTerm.class);
		restrictions.addCriterion(Restrictions.eq(CAMPAIGN + "." + ID, campaign.getId()));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(TERM, I18N.message("term"), String.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * Reset
	 */
	protected void reset() {
		removeErrorsPanel();
		cbxTerm.setSelectedEntity(null);
	}
	
	/**
	 * Create Form For term
	 */
	public void createTermForm() {
		windowTerm = new Window(I18N.message("term"));
		windowTerm.setModal(true);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		
		cbxTerm = new EntityComboBox<Term>(Term.class, Term.DESCLOCALE);
		cbxTerm.setCaption(I18N.message("term"));
		cbxTerm.setRequired(true);
		cbxTerm.renderer();
		reset();
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addStyleName("myform-align-left");
		formLayout.addComponent(cbxTerm);
	        
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -6054556325085450076L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					save();
				}
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
	     
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -5500496561140835223L;

			public void buttonClick(ClickEvent event) {
				windowTerm.close();
			}
		});
		btnCancel.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
			
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(formLayout);
	        
		windowTerm.setContent(contentLayout);
		UI.getCurrent().addWindow(windowTerm);
	}
	
	private boolean validate() {
		removeErrorsPanel();
		checkMandatorySelectField(cbxTerm, "term");
		if (!errors.isEmpty()) {
			messagePanel.removeAllComponents();
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	private void setValueToFrom() {
		Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		CampaignTerm campaignTerm = ENTITY_SRV.getById(CampaignTerm.class, id);
		cbxTerm.setSelectedEntity(campaignTerm.getTerm());
	}
}
