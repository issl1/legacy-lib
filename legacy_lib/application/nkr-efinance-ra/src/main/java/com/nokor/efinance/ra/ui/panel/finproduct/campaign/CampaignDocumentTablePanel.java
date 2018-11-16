package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignDocument;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
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
 * Add Document In Campaign
 * @author buntha.chea
 *
 */
public class CampaignDocumentTablePanel extends AbstractTabPanel implements FMEntityField, SeuksaServicesHelper {
	
	private static final long serialVersionUID = -7538697565112892189L;
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<CampaignDocument> pagedTable;
	private Item selectedItem = null;
	private Long campaignDocumentId;
	
	private CampaignDocument campaignDocument;
	private Campaign campaign;
	
	private VerticalLayout messagePanel;
	
	private EntityRefComboBox<Document> cbxDocument;

	public CampaignDocumentTablePanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
			
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddDocument = new NativeButton(I18N.message("add"));
		NativeButton btnDeleteDocument = new NativeButton(I18N.message("delete"));
		btnDeleteDocument.setIcon(FontAwesome.TRASH_O);
		btnDeleteDocument.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4081118849408805432L;

			@Override
			public void buttonClick(ClickEvent event) {
				delete();
				
			}
		});
		btnAddDocument.setIcon(FontAwesome.PLUS);
		btnAddDocument.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				campaignDocumentId = null;
				getFinProductForm(campaignDocumentId);
			}
		});		
		
		navigationPanel.addButton(btnAddDocument);	
		navigationPanel.addButton(btnDeleteDocument);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<CampaignDocument>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				campaignDocumentId = (Long) selectedItem.getItemProperty(ID).getValue();
				
				if (event.isDoubleClick()) {
					getFinProductForm(campaignDocumentId);
					assignValueToCampaignDocumentForm();
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
	 * @param campDocId
	 */
	public void assignValues(Long campDocId) {
		if (campDocId != null) {
			this.campaign = ENTITY_SRV.getById(Campaign.class, campDocId);
			List<CampaignDocument> campaignDocuments = this.getCampignDocumentByCamId(this.campaign); 
			pagedTable.setContainerDataSource(getIndexedContainer(campaignDocuments));
		} else {
			pagedTable.removeAllItems();
		}
	}
	/**
	 * 
	 * @param campaign
	 * @return
	 */
	private List<CampaignDocument> getCampignDocumentByCamId(Campaign campaign) {
		BaseRestrictions<CampaignDocument> restrictions = new BaseRestrictions<>(CampaignDocument.class);
		restrictions.addCriterion(Restrictions.eq("campaign", campaign));
		List<CampaignDocument> campaignDocuments = ENTITY_SRV.list(restrictions);
		return campaignDocuments;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<CampaignDocument> campaignDocuments) {
		IndexedContainer indexedContainer = new IndexedContainer();		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (CampaignDocument campaignDocument : campaignDocuments) {
			Item item = indexedContainer.addItem(campaignDocument.getId());
			item.getItemProperty(ID).setValue(campaignDocument.getId());
			
			if (campaignDocument.getDocumentId() != null) {
				Document document = ENTITY_SRV.getById(Document.class, campaignDocument.getDocumentId());
				item.getItemProperty("document").setValue(String.valueOf(document.getDescEn()));
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
		columnDefinitions.add(new ColumnDefinition("document", I18N.message("document"), String.class, Align.LEFT, 300));
		return columnDefinitions;
	}
	
	/**
	 * Display Document Campaign in Window Pop up (Save or Update)
	 * @param campaigndocumentId
	 */
	public void getFinProductForm(final Long campaigndocumentId) {
		this.campaignDocumentId = campaigndocumentId;
		final Window winAfterSaleFinProduct = new Window(I18N.message("document"));
		winAfterSaleFinProduct.setModal(true);
		winAfterSaleFinProduct.setResizable(false);
		winAfterSaleFinProduct.setWidth(550, Unit.PIXELS);
		winAfterSaleFinProduct.setHeight(163, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout();
		cbxDocument = new EntityRefComboBox<>(I18N.message("document"));
		cbxDocument.setRestrictions(new BaseRestrictions<>(Document.class));
		cbxDocument.renderer();
		cbxDocument.setImmediate(true);
		cbxDocument.setRequired(true);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
						
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        
        if (campaigndocumentId != null) {
			campaignDocument = ENTITY_SRV.getById(CampaignDocument.class, campaignDocumentId);
			//cbxDocument.setSelectedEntity(campaignDocument.getDocument());
		}
        
        formLayout.addComponent(cbxDocument);
               
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (campaigndocumentId == null) {
						campaignDocument = new CampaignDocument();	
					} else {
						campaignDocument = ENTITY_SRV.getById(CampaignDocument.class, campaigndocumentId);
					}
					campaignDocument.setDocumentId(cbxDocument.getSelectedEntity().getId());
					campaignDocument.setCampaign(campaign);	
					ENTITY_SRV.saveOrUpdate(campaignDocument);
					winAfterSaleFinProduct.close();
					assignValues(campaign.getId());	
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winAfterSaleFinProduct.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(formLayout);
        
        winAfterSaleFinProduct.setContent(contentLayout);
        UI.getCurrent().addWindow(winAfterSaleFinProduct);
	
	}	
	/**
	 * Validate Add campaign document Field Required 
	 * @return
	 */
	public boolean validate() {
		removeErrorsPanel();
		checkMandatorySelectField(cbxDocument, "document");
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
	 * Delete Document
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
						ENTITY_SRV.delete(CampaignDocument.class, id);
					    dialog.close();
						assignValues(campaign.getId());
						selectedItem = null;
					}
				}
			});
		}
	}
	
	/**
	 * 
	 */
	private void assignValueToCampaignDocumentForm() {
		campaignDocument = ENTITY_SRV.getById(CampaignDocument.class, campaignDocumentId);
		Document document = ENTITY_SRV.getById(Document.class, campaignDocument.getDocumentId());
		cbxDocument.setSelectedEntity(document);
	}
	
}
