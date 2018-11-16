package com.nokor.efinance.gui.ui.panel.collection.callcenter.staff;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Result panel in call center
 * @author uhout.cheng
 */
public class CallCenterResultPanel extends AbstractControlPanel implements FinServicesHelper, ClickListener, ItemClickListener, SelectedItem {

	/** */
	private static final long serialVersionUID = -4607268281410875418L;
	
	private EntityComboBox<ECallCenterResult> cbxResult;
	private TextArea txtRemark;
	private Button btnSave;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	
	private CallCenterHistory callCenterHistory;
	private Contract contract;
	
	private CallCenterContractTablePanel callCenterContractTablePanel;
	
	/**
	 * @param callCenterStaffPanel
	 */
	public CallCenterResultPanel(CallCenterContractTablePanel callCenterContractTablePanel) {
		this.callCenterContractTablePanel = callCenterContractTablePanel;
		
		cbxResult = new EntityComboBox<>(ECallCenterResult.class, ECallCenterResult.DESC);
		cbxResult.setWidth(150, Unit.PIXELS);
		cbxResult.renderer();
		txtRemark = ComponentFactory.getTextArea(false, 160, 50);
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.addItemClickListener(this);
		simpleTable.setPageLength(3);
		simpleTable.setCaption(I18N.message("call.center.histories"));
		simpleTable.setSizeFull();
		
		setWidth(550, Unit.PIXELS);
		setMargin(true);
		addComponent(getResultsPanel());
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		refresh();
	}
	
	/**
	 * 
	 */
	private void refresh() {
		resetControls();
		this.callCenterHistory = null;
		setCallCenterHistoryIndexedContainer(CALL_CTR_SRV.getCallCenterHistories(this.contract.getId()));
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CallCenterHistory.ID, I18N.message("id"), Long.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(CallCenterHistory.CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CallCenterHistory.RESULT, I18N.message("result"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CallCenterHistory.COMMENT, I18N.message("remark"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(CallCenterHistory.ACTION, I18N.message("action"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param callCenterHistories
	 */
	@SuppressWarnings("unchecked")
	private void setCallCenterHistoryIndexedContainer(List<CallCenterHistory> callCenterHistories) {
		this.selectedItem = null;
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (callCenterHistories != null && !callCenterHistories.isEmpty()) {
			for (CallCenterHistory calCtrHistory : callCenterHistories) {
				
				Button btnDelete = new Button();
				btnDelete.setStyleName(Reindeer.BUTTON_LINK);
				btnDelete.setIcon(FontAwesome.TRASH_O);
				
				Item item = container.addItem(calCtrHistory.getId());
				item.getItemProperty(CallCenterHistory.ID).setValue(calCtrHistory.getId());
				item.getItemProperty(CallCenterHistory.CREATEDATE).setValue(calCtrHistory.getCreateDate());
				item.getItemProperty(CallCenterHistory.RESULT).setValue(calCtrHistory.getResult() != null ? calCtrHistory.getResult().getCode() : "");
				item.getItemProperty(CallCenterHistory.COMMENT).setValue(calCtrHistory.getComment());
				item.getItemProperty(CallCenterHistory.ACTION).setValue(btnDelete);
				
				btnDelete.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = -3020590367392632852L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
								new String[] {calCtrHistory.getId().toString()}), new ConfirmDialog.Listener() {
									
								/** */
								private static final long serialVersionUID = 2083879919290111410L;

								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										try {
											CALL_CTR_SRV.delete(calCtrHistory);
											ComponentLayoutFactory.getNotificationDesc(calCtrHistory.getId().toString(), "item.deleted.successfully");
											refresh();
										} catch (Exception e) {
											logger.error(e.getMessage());
										}
						            }
								}
							});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
					}
				});
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getResultsPanel() {
		GridLayout gridLayout = new GridLayout(9, 1);
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
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(gridLayout);
		layout.addComponent(simpleTable);
		
		Panel panel = new Panel(layout);
		panel.setCaption(I18N.message("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("result") + "</h3>"));
		panel.setCaptionAsHtml(true);
		return panel;
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		cbxResult.setSelectedEntity(null);
		txtRemark.setValue("");
	}
	
	/**
	 * 
	 */
	private void saveCallCenterHistory() {
		if (cbxResult.getSelectedEntity() != null || StringUtils.isNotEmpty(txtRemark.getValue())) {
			if (!checkMandatorySelectField(cbxResult, "")) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("field.required.1", I18N.message("result")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				if (this.callCenterHistory == null) {
					this.callCenterHistory = CallCenterHistory.createInstance();
				} 
				this.callCenterHistory.setContract(contract);
				this.callCenterHistory.setResult(cbxResult.getSelectedEntity());
				this.callCenterHistory.setComment(txtRemark.getValue());
				try {
					CALL_CTR_SRV.saveOrUpdateCallCenterHistory(UserSessionManager.getCurrentUser(), this.callCenterHistory);
					if (callCenterContractTablePanel != null) {
						callCenterContractTablePanel.assignValues();
					} 
					displaySuccessfullyMsg();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void displaySuccessfullyMsg() {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		refresh();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			saveCallCenterHistory();
		}
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(CallCenterHistory.ID).getValue());
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
		this.callCenterHistory = CALL_CTR_SRV.getById(CallCenterHistory.class, getItemSelectedId());
		if (this.callCenterHistory != null) {
			cbxResult.setSelectedEntity(this.callCenterHistory.getResult());
			txtRemark.setValue(this.callCenterHistory.getComment());
		}
	}

}
