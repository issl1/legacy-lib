package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.ColContractsHolderPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Result table in collection phone staff
 * @author uhout.cheng
 */
public class CollectionResultsTablePanel extends AbstractControlPanel implements FinServicesHelper, ItemClickListener, SelectedItem, ClickListener {

	/** */
	private static final long serialVersionUID = 8861049721225279313L;
	
	private CollectionHistory colHistory;
	private Contract contract;
	
	private EntityRefComboBox<EColResult> cbxResult;
	private TextArea txtRemark;	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private AutoDateField dfNextActionDate;
	private ERefDataComboBox<EColAction> cbxNextActionType;
	private NativeButton btnToday;
	private NativeButton btnP1;
	private NativeButton btnP2;
	private NativeButton btnP3;
	private NativeButton btnP5;
	private NativeButton btnP10;
	private Button btnSave;
	
	private Date nextActionDate;
	
	/**
	 * 
	 */
	public CollectionResultsTablePanel() {
		setWidth(525, Unit.PIXELS);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		cbxResult = new EntityRefComboBox<EColResult>();
		cbxResult.setRestrictions(new BaseRestrictions<>(EColResult.class));
		cbxResult.getRestrictions().addOrder(Order.asc("descEn"));
		cbxResult.setCaptionRenderer(new Function<EColResult, String>() {
			/**
			 * @see java.util.function.Function#apply(java.lang.Object)
			 */
			@Override
			public String apply(EColResult t) {
				return t.getCode() + " - " + (I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc());
			}
		});
		cbxResult.renderer();
		cbxResult.setWidth(150, Unit.PIXELS);
		txtRemark = ComponentFactory.getTextArea(false, 160, 40);
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 60, "btn btn-success button-small");
		btnSave.addClickListener(this);
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.addItemClickListener(this);
		simpleTable.setPageLength(3);
		simpleTable.setCaption(null);
		simpleTable.setSizeFull();
		
		dfNextActionDate = ComponentFactory.getAutoDateField();
		cbxNextActionType = new ERefDataComboBox<EColAction>(EColAction.values());
		cbxNextActionType.setWidth(150, Unit.PIXELS);
		
		btnToday = new NativeButton(I18N.message("today"));
		btnP1 = new NativeButton(I18N.message("+1"));
		btnP2 = new NativeButton(I18N.message("+2"));
		btnP3 = new NativeButton(I18N.message("+3"));
		btnP5 = new NativeButton(I18N.message("+5"));
		btnP10 = new NativeButton(I18N.message("+10"));
		
		cbxNextActionType.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -1055887378628137677L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (EColAction.NONE.equals(cbxNextActionType.getSelectedEntity())) {
					setEnableCollectionAction(false);
					dfNextActionDate.setValue(nextActionDate);
				} else {
					setEnableCollectionAction(true);
				}
			}
		});
		
		btnToday.addStyleName("btn btn-success button-small");
		btnP1.addStyleName("btn btn-success button-small");
		btnP2.addStyleName("btn btn-success button-small");
		btnP3.addStyleName("btn btn-success button-small");
		btnP5.addStyleName("btn btn-success button-small");
		btnP10.addStyleName("btn btn-success button-small");
		
		btnToday.addClickListener(this);
		btnP1.addClickListener(this);
		btnP2.addClickListener(this);
		btnP3.addClickListener(this);
		btnP5.addClickListener(this);
		btnP10.addClickListener(this);
		
		setEnableCollectionAction(false);
		
		addComponent(getResultsPanel());
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setEnableCollectionAction(boolean isEnable) {
		btnToday.setEnabled(isEnable);
		btnP1.setEnabled(isEnable);
		btnP2.setEnabled(isEnable);
		btnP3.setEnabled(isEnable);
		btnP5.setEnabled(isEnable);
		btnP10.setEnabled(isEnable);
		dfNextActionDate.setEnabled(isEnable);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getResultsPanel() {
		GridLayout gridLayout = new GridLayout(15, 3);
		int iCol = 0;
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("result"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxResult, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("remark"), iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtRemark, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(btnSave, iCol++, 0);
		
		GridLayout nextActionLayout = new GridLayout(10, 1);
		nextActionLayout.setSpacing(true);
		nextActionLayout.addComponent(ComponentLayoutFactory.getLabelCaption("next.action"));
		nextActionLayout.addComponent(cbxNextActionType);
		nextActionLayout.addComponent(ComponentLayoutFactory.getLabelCaption("date"));
		nextActionLayout.addComponent(btnToday);
		nextActionLayout.addComponent(btnP1);
		nextActionLayout.addComponent(btnP2);
		nextActionLayout.addComponent(btnP3);
		nextActionLayout.addComponent(btnP5);
		nextActionLayout.addComponent(btnP10);
		nextActionLayout.addComponent(dfNextActionDate);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(gridLayout);
		verLayout.addComponent(nextActionLayout);
		verLayout.addComponent(simpleTable);
		
		Panel panel = new Panel(verLayout);
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CREATEUSER, I18N.message("user.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.DEPARTMENT, I18N.message("department"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.RESULT, I18N.message("result"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.COMMENT, I18N.message("remark"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param colHistories
	 */
	@SuppressWarnings("unchecked")
	private void setColHistoryIndexedContainer(List<CollectionHistory> colHistories) {
		Container container = simpleTable.getContainerDataSource();
		if (colHistories != null && !colHistories.isEmpty()) {
			for (CollectionHistory colHistory : colHistories) {
				Item item = container.addItem(colHistory.getId());
				item.getItemProperty(CollectionHistory.ID).setValue(colHistory.getId());
				item.getItemProperty(CollectionHistory.CREATEDATE).setValue(colHistory.getCreateDate());
				item.getItemProperty(CollectionHistory.CREATEUSER).setValue(colHistory.getCreateUser());
				item.getItemProperty(CollectionHistory.DEPARTMENT).setValue("");
				item.getItemProperty(CollectionHistory.RESULT).setValue(colHistory.getResult() != null ? colHistory.getResult().getCode() : "");
				item.getItemProperty(CollectionHistory.COMMENT).setValue(colHistory.getComment());
			}
		}
	}
	
	/**
	 * 
	 * @param collection
	 */
	public void assignValues(Contract contract) {
		resetControls();
		this.contract = contract;
		if (contract != null) {
			setColHistoryIndexedContainer(COL_SRV.getCollectionHistoriesByContractId(contract.getId()));
		}
		assignNextAction(contract);
	}
	
	/**
	 * Assign next action
	 * @param contract
	 */
	private void assignNextAction(Contract contract) {
		if (contract != null) {
			CollectionAction colAction = contract.getCollection().getLastAction();
			if (colAction != null) {
				nextActionDate = colAction.getNextActionDate();
				cbxNextActionType.setSelectedEntity(colAction.getColAction());
				dfNextActionDate.setValue(nextActionDate);
			}
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			save();
		} else if (event.getButton() == btnToday) {
			dfNextActionDate.setValue(DateUtils.today());
		} else if (event.getButton() == btnP1) {
			dfNextActionDate.setValue(DateUtils.addDaysDate(DateUtils.today(), 1));
		} else if (event.getButton() == btnP2) {
			dfNextActionDate.setValue(DateUtils.addDaysDate(DateUtils.today(), 2));
		} else if (event.getButton() == btnP3) {
			dfNextActionDate.setValue(DateUtils.addDaysDate(DateUtils.today(), 3));
		} else if (event.getButton() == btnP5) {
			dfNextActionDate.setValue(DateUtils.addDaysDate(DateUtils.today(), 5));
		} else if (event.getButton() == btnP10) {
			dfNextActionDate.setValue(DateUtils.addDaysDate(DateUtils.today(), 10));
		}
	}
	
	/**
	 * Save
	 */
	private void save() {
		// Save result
		if (cbxResult.getSelectedEntity() != null || StringUtils.isNotEmpty(txtRemark.getValue())) {
			if (!checkMandatorySelectField(cbxResult, "")) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("result")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				if (colHistory == null) {
					colHistory = CollectionHistory.createInstance();
				} 
				colHistory.setContract(contract);
				colHistory.setResult(cbxResult.getSelectedEntity());
				colHistory.setComment(txtRemark.getValue());
				colHistory.setOrigin(EColType.PHONE);
				COL_SRV.saveOrUpdate(colHistory);
				
				Collection collection = contract.getCollection();
				collection.setLastCollectionHistory(colHistory);
				COL_SRV.saveOrUpdate(collection);
			}
		}
		
		// Save nextAction
		if (dfNextActionDate.getValue() != null || cbxNextActionType.getSelectedEntity() != null) {
			if (!checkMandatoryDateField(dfNextActionDate, "")) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("next.action.date")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else if (!checkMandatorySelectField(cbxNextActionType, "")) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("next.action")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				Collection collection = contract.getCollection();
				SecUser secUser = UserSessionManager.getCurrentUser();
				
				CollectionAction colAction = collection.getLastAction();
				if (colAction == null) {
					colAction = CollectionAction.createInstance();
					collection.setLastAction(colAction);
					colAction.setContract(contract);
				} 
				colAction.setColAction(cbxNextActionType.getSelectedEntity());
				colAction.setNextActionDate(dfNextActionDate.getValue());
				colAction.setUserLogin(secUser.getLogin());
				
				COL_SRV.saveOrUpdate(colAction);
				COL_SRV.saveOrUpdate(collection);
			}
		}
		refreshTable();
	}
	
	/**
	 * Refresh table after saved successful
	 */
	private void refreshTable() {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		notification.show(Page.getCurrent());
		ColContractsHolderPanel.refreshTable();
		assignValues(contract);
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		colHistory = null;
		this.contract = null;
		selectedItem = null;
		simpleTable.removeAllItems();
		txtRemark.setValue("");
		cbxResult.setSelectedEntity(null);
		
		dfNextActionDate.setValue(null);
		cbxNextActionType.setSelectedEntity(null);
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(CollectionHistory.ID).getValue());
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
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		colHistory = COL_SRV.getById(CollectionHistory.class, getItemSelectedId());
		if (colHistory != null) {
			cbxResult.setSelectedEntity(colHistory.getResult());
			txtRemark.setValue(colHistory.getComment());
		}
	}
	
}
