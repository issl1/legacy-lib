package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.CampaignCreditBureauGrade;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class CampaignGradeFormPanel extends AbstractTabPanel implements SeuksaServicesHelper {

	/** */
	private static final long serialVersionUID = -5041791365216631955L;

	private final static String ID = "id";
	private final static String GRADE = "grade";
	
	private Campaign campaign;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<CampaignCreditBureauGrade> pagedTable;
	private Item selectedItem = null;
	private TextField txtGrade;
	private Window windowGrade;
	
	private VerticalLayout messagePanel;
	
	public CampaignGradeFormPanel() {
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
			private static final long serialVersionUID = 1609223163344170195L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				selectedItem = null;
				createCampaignGradeForm();
			}
		});
			
		NativeButton btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnDelete.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -1061284912102092794L;

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
		pagedTable = new SimplePagedTable<CampaignCreditBureauGrade>(this.columnDefinitions);

		pagedTable.addItemClickListener(new ItemClickListener() {
		
			/** */
			private static final long serialVersionUID = 3839008285779203141L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				if (event.isDoubleClick()) {
					createCampaignGradeForm();
					assingValueToCampaignGrade();
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
		if (validate()) {
			CampaignCreditBureauGrade campaignCreditBureauGrade;
			if (selectedItem == null) {
				campaignCreditBureauGrade = new CampaignCreditBureauGrade();
			} else {
				Long id = (Long) selectedItem.getItemProperty("id").getValue();
				campaignCreditBureauGrade = ENTITY_SRV.getById(CampaignCreditBureauGrade.class, id);
			}
			campaignCreditBureauGrade.setGrade(txtGrade.getValue());
			campaignCreditBureauGrade.setCampaign(campaign);
			ENTITY_SRV.saveOrUpdate(campaignCreditBureauGrade);
			windowGrade.close();
			assignValues(campaign.getId());
			reset();
			//displaySuccess();
		} 
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
				private static final long serialVersionUID = 428090367289711801L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						logger.debug("[>> deleteCampaignCreditBureauGrade]");
						ENTITY_SRV.delete(CampaignCreditBureauGrade.class, id);
						logger.debug("This item " + id + "deleted successfully !");
						logger.debug("[<< deleteCampaignCreditBureauGrade]");
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
			pagedTable.setContainerDataSource(getCampaignGradeIndexedContainer(campaign));
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
	private IndexedContainer getCampaignGradeIndexedContainer(Campaign campaign) {
		List<CampaignCreditBureauGrade> campaignCreditBureauGrades = getGradesByCampaignId(campaign);
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (!campaignCreditBureauGrades.isEmpty()) {
			for (CampaignCreditBureauGrade campaignCreditBureauGrade : campaignCreditBureauGrades) {
				Item item = indexedContainer.addItem(campaignCreditBureauGrade.getId());
				item.getItemProperty(ID).setValue(campaignCreditBureauGrade.getId());
				item.getItemProperty(GRADE).setValue(campaignCreditBureauGrade.getGrade());
			}	
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param campaign
	 * @return
	 */
	private List<CampaignCreditBureauGrade> getGradesByCampaignId(Campaign campaign) {
		BaseRestrictions<CampaignCreditBureauGrade> restrictions = new BaseRestrictions<>(CampaignCreditBureauGrade.class);
		restrictions.addCriterion(Restrictions.eq("campaign.id", campaign.getId()));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(GRADE, I18N.message("ncb.grades"), String.class, Align.LEFT, 70));
		return columnDefinitions;
	}
	
	/**
	 * Reset
	 */
	protected void reset() {
		removeErrorsPanel();
		txtGrade.setValue("");
	}
	
	/**
	 * Validate the term form
	 * @return
	 */
	private boolean validate() {
		removeErrorsPanel();
		checkMandatoryField(txtGrade, GRADE);
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
	
	private void createCampaignGradeForm() {
		windowGrade = new Window(I18N.message("ncb.grades"));
		windowGrade.setModal(true);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		
		txtGrade = ComponentFactory.getTextField("ncb.grades", true, 20, 180);
		reset();
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		formLayout.addStyleName("myform-align-left");
		formLayout.addComponent(txtGrade);
	        
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -5553812216007749161L;

			public void buttonClick(ClickEvent event) {
				save();
			}
		});
		btnSave.setIcon(FontAwesome.SAVE);
	     
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 6157695279201676832L;

			public void buttonClick(ClickEvent event) {
				windowGrade.close();
			}
		});
		btnCancel.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);			

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(formLayout);
	        
		windowGrade.setContent(contentLayout);
		UI.getCurrent().addWindow(windowGrade);
	}
	
	/**
	 * 
	 */
	private void assingValueToCampaignGrade() {
		Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		CampaignCreditBureauGrade campaignCreditBureauGrade = ENTITY_SRV.getById(CampaignCreditBureauGrade.class, id);
		txtGrade.setValue(campaignCreditBureauGrade.getGrade());
	}
}
