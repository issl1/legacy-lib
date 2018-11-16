package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Actions panel in collection phone staff
 * @author uhout.cheng
 */
public class CollectionActionsTablePanel extends AbstractControlPanel implements ClickListener, ItemClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -7857146399252787905L;
	
	private AutoDateField dfNextActionDate;
	private ERefDataComboBox<EColAction> cbxNextActionType;
	private Button btnUpdate;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private Contract contract;
	private CollectionAction colAction;
	
	/**
	 * 
	 */
	public CollectionActionsTablePanel() {
		setWidth(525, Unit.PIXELS);
		setSpacing(true);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		
		dfNextActionDate = ComponentFactory.getAutoDateField();
		cbxNextActionType = new ERefDataComboBox<EColAction>(EColAction.values());
		cbxNextActionType.setWidth(150, Unit.PIXELS);
		btnUpdate = getButton(null, FontAwesome.EDIT, 40);
		
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.addItemClickListener(this);
		simpleTable.setPageLength(3);
		simpleTable.setCaption(null);
		simpleTable.setSizeFull();
		
		addComponent(getActionsPanel());
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CollectionAction.ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(CollectionAction.CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(CollectionAction.NEXTACTIONDATE, I18N.message("next.action.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CollectionAction.COLACTION, I18N.message("next.action"), String.class, Align.LEFT, 250));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param colActions
	 */
	@SuppressWarnings("unchecked")
	private void setColActionIndexedContainer(List<CollectionAction> colActions) {
		Container container = simpleTable.getContainerDataSource();
		if (colActions != null && !colActions.isEmpty()) {
			for (CollectionAction colAction : colActions) {
				Item item = container.addItem(colAction.getId());
				item.getItemProperty(CollectionAction.ID).setValue(colAction.getId());
				item.getItemProperty(CollectionAction.CREATEDATE).setValue(colAction.getCreateDate());
				item.getItemProperty(CollectionAction.NEXTACTIONDATE).setValue(colAction.getNextActionDate());
				item.getItemProperty(CollectionAction.COLACTION).setValue(colAction.getColAction() != null ? (colAction.getColAction().getCode() + " - " + colAction.getColAction().getDescEn()) : "");
			}
		}
	}
	
	/** 
	 * @param caption
	 * @param icon
	 * @param width
	 * @return
	 */
	private Button getButton(String caption, Resource icon, float width) {
		Button button = ComponentLayoutFactory.getButtonStyle(caption, icon, width, "btn btn-success button-small");
		button.addClickListener(this);
		return button;
	} 
	
	/**
	 * 
	 * @return
	 */
	private Component getActionsPanel() {
		GridLayout gridLayout = new GridLayout(15, 3);
		int iCol = 0;
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("next.action.date"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfNextActionDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("next.action"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxNextActionType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(btnUpdate, iCol++, 0);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(gridLayout);
		verLayout.addComponent(simpleTable);
		
		Panel panel = new Panel(verLayout);
		panel.setCaption(I18N.message("actions"));
		return panel;
	}

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		resetControls();
		this.contract = contract;
		if (contract != null) {
			List<CollectionAction> collectionActions = new ArrayList<CollectionAction>();
			if (contract.getCollection().getLastAction() != null) {
				collectionActions.add(contract.getCollection().getLastAction());
			}
			setColActionIndexedContainer(collectionActions);
//			setColActionIndexedContainer(COL_SRV.getCollectionActionsByContractId(contract.getId()));
		}
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		colAction = null;
		this.contract = null;
		selectedItem = null;
		simpleTable.removeAllItems();
		dfNextActionDate.setValue(null);
		cbxNextActionType.setSelectedEntity(null);
	}
	
	/**
	 * Refresh table after saved successful
	 */
	private void refreshTable() {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnUpdate)) {
			if (dfNextActionDate.getValue() != null || cbxNextActionType.getSelectedEntity() != null) {
				SecUser secUser = UserSessionManager.getCurrentUser();
				if (colAction == null) {
					colAction = CollectionAction.createInstance();
				} 
				colAction.setContract(contract);
				colAction.setColAction(cbxNextActionType.getSelectedEntity());
				colAction.setNextActionDate(dfNextActionDate.getValue());
				colAction.setUserLogin(secUser.getLogin());				
				COL_SRV.saveOrUpdate(colAction);
				
				Collection collection = contract.getCollection();
				collection.setLastAction(colAction);
				COL_SRV.saveOrUpdate(collection);
				
				refreshTable();				
			}
		}
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(CollectionAction.ID).getValue());
		}
		return null;
	}
	
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		colAction = COL_SRV.getById(CollectionAction.class, getItemSelectedId());
		if (colAction != null) {
			cbxNextActionType.setSelectedEntity(colAction.getColAction());
			dfNextActionDate.setValue(colAction.getNextActionDate());
		}
	}
}
