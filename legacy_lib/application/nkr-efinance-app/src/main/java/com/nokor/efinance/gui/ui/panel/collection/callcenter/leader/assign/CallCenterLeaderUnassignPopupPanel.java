package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Call Center Leader UnAssign Pop up In Assign tab
 * @author uhout.cheng
 */
public class CallCenterLeaderUnassignPopupPanel extends Window implements ClickListener, MCollection, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 8750419499883857537L;

	private static final String CHECK = "check";
	
	private NativeButton btnSave;
	private NativeButton btnCancel;
	
	private SimpleTable<Contract> simpleTable;
	private UnassignListener unassignListener;
	private CallCenterLeaderFilterPanel filterPanel;
	
	/**
	 * 
	 * @param filterPanel
	 */
	public CallCenterLeaderUnassignPopupPanel(CallCenterLeaderFilterPanel filterPanel) {
		this.filterPanel = filterPanel;
		setModal(true);
		setCaption(I18N.message("unassign"));
		setWidth(60, Unit.PERCENTAGE);
		createForm();
	}
	
	/**
	 * Create form
	 */
	private void createForm() {
		simpleTable = new SimpleTable<Contract>(createColumnDefinitions());
		btnSave = new NativeButton(I18N.message("unassign"));
		btnSave.addClickListener(this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.addClickListener(this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout tableLayout = new VerticalLayout();
		tableLayout.setMargin(true);
		tableLayout.addComponent(simpleTable);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(tableLayout);
		setContent(content);
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CHECK, I18N.message("check"), CheckBox.class, Align.CENTER, 50));
		columnDefinitions.add(new ColumnDefinition(CONTRACTID, I18N.message("contract.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(DUEDAY, I18N.message("due.day"), Integer.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(ODM, I18N.message("odm"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), Amount.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantors"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(LATESTRESULT, I18N.message("latest.result"), String.class, Align.LEFT, 55));
		columnDefinitions.add(new ColumnDefinition(NEXTACTIONDATE, I18N.message("next.action.date"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(NEXTACTION, I18N.message("next.action"), String.class, Align.LEFT, 55));
		columnDefinitions.add(new ColumnDefinition(LATESTPAYMENTDATE, I18N.message("lpd"), Date.class, Align.LEFT, 65));
		columnDefinitions.add(new ColumnDefinition(AMOUNTCOLLECTED, I18N.message("amount.collected"), Amount.class, Align.LEFT, 100));
		
		return columnDefinitions;
	}
	
	/**
	 * Set Table ContainerDataSource
	 */
	@SuppressWarnings("unchecked")
	private void setTableContainerDataSource(List<ContractUserInbox> contractUserInboxs) {
		simpleTable.removeAllItems();
		if (contractUserInboxs != null) {
			for (ContractUserInbox contractUserInbox : contractUserInboxs) {
				Contract contract = contractUserInbox.getContract();
				String latestResult = "";
				
				Integer dueDay = null;
				Date lastPaymentDate = null;
				double amtCollected = 0d;
				
				Collection collection = contract.getCollection();
				if (collection != null) {
					int dpd = MyNumberUtils.getInteger(collection.getNbOverdueInDays());
					int odm = MyNumberUtils.getInteger(collection.getDebtLevel());
					double apd = MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue());
					dueDay = collection.getDueDay();
					lastPaymentDate = collection.getLastPaymentDate();
					
					CallCenterHistory callCenterHistory = collection.getLastCallCenterHistory();
					if (callCenterHistory != null) {
						ECallCenterResult result = callCenterHistory.getResult();
						latestResult = (result == null ? "" : result.getCode());
					}
					
					Item item = simpleTable.addItem(contract.getId());
					CheckBox cb = new CheckBox();
					item.getItemProperty(CHECK).setValue(cb);
					item.getItemProperty(CONTRACTID).setValue(contract.getReference());
					item.getItemProperty(DUEDAY).setValue(dueDay);
					item.getItemProperty(ODM).setValue(odm);
					item.getItemProperty(DPD).setValue(dpd);
					item.getItemProperty(APD).setValue(AmountUtils.convertToAmount(apd));
					item.getItemProperty(GUARANTOR).setValue(contract.getNumberGuarantors());
					item.getItemProperty(LATESTRESULT).setValue(latestResult);
					item.getItemProperty(LATESTPAYMENTDATE).setValue(lastPaymentDate);
					item.getItemProperty(AMOUNTCOLLECTED).setValue(AmountUtils.convertToAmount(amtCollected));
				}
			}
		}
	}
	
	/**
	 * Get Selected Id
	 * @return
	 */
	private List<Long> getSelectedId() {
		List<Long> ids = new ArrayList<>();
		for (Object i : simpleTable.getItemIds()) {
		    Long id = (Long) i;
		    Item item = simpleTable.getItem(id);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(CHECK).getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(id);
		    }
		}
		return ids;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.unassign.contract.user.inboxes"), new ConfirmDialog.Listener() {
				
				/** */
				private static final long serialVersionUID = 8723982092357828702L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						if (getUnassignListener() != null) {
							getUnassignListener().onUnassignContracts(getSelectedId());
						}
						close();
					}
				}
			});
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Refresh the table
	 */
	public void refresh() {
		List<ContractUserInbox> contractUserInboxs = CONT_SRV.list(filterPanel.getRestrictions());
		setTableContainerDataSource(contractUserInboxs);
	}
	
	/**
	 * Show the pop up
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
		refresh();
	}
	
	/**
	 * @author bunlong.taing
	 */
	public interface UnassignListener extends Serializable {
		
		/**
		 * @param selectedIds
		 */
		void onUnassignContracts(List<Long> selectedIds);
		
	}
	
	/**
	 * @return the unassignListener
	 */
	public UnassignListener getUnassignListener() {
		return unassignListener;
	}

	/**
	 * @param unassignListener the unassignListener to set
	 */
	public void setUnassignListener(final UnassignListener unassignListener) {
		this.unassignListener = unassignListener;
	}

}
