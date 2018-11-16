package com.nokor.efinance.gui.ui.panel.collection.field.supervisor;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class UnmatchedContractTablePanel extends VerticalLayout implements MCollection, ItemClickListener, SelectedItem, ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9047202487419926794L;
	
	private SimplePagedTable<Entity> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem;

	private NativeButton btnAssign;
	private NativeButton btnReset;
	
	private List<ContractUserSimulInbox> contractsUserSimulInbox;
	
	public UnmatchedContractTablePanel() {
		init();
	}
	
	private void init() {
		
		pagedTable = new SimplePagedTable<>(getColumnDefinitions());
		pagedTable.addItemClickListener(this);
	
		VerticalLayout tableLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		tableLayout.addComponent(pagedTable);		
		tableLayout.addComponent(pagedTable.createControls());
				
		btnAssign = new NativeButton(I18N.message("assign"));
		btnAssign.addStyleName("btn btn-success");
		btnAssign.addClickListener(this);
		
		btnReset = new NativeButton(I18N.message("reset"));
		btnReset.addStyleName("btn btn-success");
		btnReset.addClickListener(this);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnAssign);
		buttonsLayout.addComponent(btnReset);
		
		setMargin(true);
		setSpacing(true);
		addComponent(tableLayout);
		addComponent(buttonsLayout);
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACTID, I18N.message("contract.id"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(LESSESS, I18N.message("lessee"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(PRIMARYPHONENO, I18N.message("primary.phone.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantors"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DUEDAY, I18N.message("due.day"), Integer.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(PI, I18N.message("pi"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(RI, I18N.message("ri"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(ODI, I18N.message("odi"), Integer.class, Align.LEFT, 50));		
		columnDefinitions.add(new ColumnDefinition(ODM, I18N.message("odm"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(DPD, I18N.message("dpd"), Integer.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition(APD, I18N.message("apd"), Amount.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(COLLECTED, I18N.message("collected"), Amount.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("area", I18N.message("area"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ACTION, I18N.message("action"), Button.class, Align.CENTER, 80));
		return columnDefinitions;
	}
	
	
	/**
	 * 
	 * @param contractsUserSimulInbox
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<ContractUserSimulInbox> contractsUserSimulInbox) {
		selectedItem = null;
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (contractsUserSimulInbox != null && !contractsUserSimulInbox.isEmpty()) {
			int index = 0;
			for (ContractUserSimulInbox contractUserSimulInbox : contractsUserSimulInbox) {
				Contract contract = contractUserSimulInbox.getContract();
				String contractID = contract.getReference();
				String lessess = "";
				String primaryPhone = "";
				int nbGuarantors = contract.getNumberGuarantors();
				int pi = 0;
				int ri = 0;
				int odi = 0;
				double collected = 0d;
				Collection collection = contract.getCollection();
				Applicant applicant = contract.getApplicant();
				Individual individual = applicant.getIndividual();
				lessess = applicant.getNameEn();
				if (individual != null) {
					primaryPhone = individual.getIndividualPrimaryContactInfo();
				}
				
				if (collection != null) {
					
					pi = MyNumberUtils.getInteger(collection.getNbInstallmentsPaid());
					ri = contract.getTerm() - pi;
					odi = MyNumberUtils.getInteger(collection.getNbInstallmentsInOverdue());
					
					Item item = indexedContainer.addItem(index);	
					item.getItemProperty(ID).setValue(contract.getId());
					item.getItemProperty(CONTRACTID).setValue(contractID);
					item.getItemProperty(LESSESS).setValue(lessess);
					item.getItemProperty(PRIMARYPHONENO).setValue(primaryPhone);
					item.getItemProperty(GUARANTOR).setValue(nbGuarantors);
					item.getItemProperty(DUEDAY).setValue(collection.getDueDay());
					item.getItemProperty(PI).setValue(pi);
					item.getItemProperty(RI).setValue(ri);
					item.getItemProperty(ODI).setValue(odi);
					item.getItemProperty(ODM).setValue(MyNumberUtils.getInteger(collection.getDebtLevel()));
					item.getItemProperty(DPD).setValue(MyNumberUtils.getInteger(collection.getNbOverdueInDays()));
					item.getItemProperty(APD).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(collection.getTiTotalAmountInOverdue())));
					item.getItemProperty(COLLECTED).setValue(AmountUtils.convertToAmount(collected));
					item.getItemProperty("area").setValue(contractUserSimulInbox.getArea() != null ? contractUserSimulInbox.getArea().getCode() : "");
					item.getItemProperty(ACTION).setValue(new AddAreaButton(contractUserSimulInbox));
					
					index++;
					
				}
			}
		}
		return indexedContainer;
	}
	
	/**
	 * AssignValue
	 * @param contracts
	 */
	public void assignValues(List<ContractUserSimulInbox> contractsUserSimulInbox) {
		this.contractsUserSimulInbox = contractsUserSimulInbox;
		pagedTable.setContainerDataSource(getIndexedContainer(contractsUserSimulInbox));
	}
	
	/**
	 * AssignValue
	 * @param contracts
	 */
	public void assignValues() {
		if (ProfileUtil.isColFieldSupervisor()) {
			contractsUserSimulInbox = COL_SRV.getUnmatchedFieldContracts();
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			contractsUserSimulInbox = COL_SRV.getUnmatchedInsideRepoContracts();
		} else if (ProfileUtil.isColOASupervisor()) {
			contractsUserSimulInbox = COL_SRV.getUnmatchedOAContracts();
		}
		assignValues(contractsUserSimulInbox);
	}
	

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + getItemSelectedId());
		}
	}
		
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
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
	 * 
	 * @author buntha.chea
	 *
	 */
	private class AddAreaButton extends NativeButton {		
		private static final long serialVersionUID = 1585961357565634055L;
		
		public AddAreaButton(final ContractUserSimulInbox contractUserSimulInbox) {
			super("");
			setCaption(I18N.message("add.area"));
			setIcon(FontAwesome.PLUS);
			setStyleName("btn btn-success button-small");
			addClickListener(new ClickListener() {				
				private static final long serialVersionUID = 494854605358694744L;
				@Override
				public void buttonClick(ClickEvent event) {
					new AreasSelectPanel().show(contractUserSimulInbox.getContract(), contractUserSimulInbox.getColType(), new AreasSelectPanel.Listener() {
						private static final long serialVersionUID = -6966543272648001137L;

						@Override
						public void onClose(AreasSelectPanel dialog) {
							contractUserSimulInbox.setArea(dialog.getSelectArea());
							assignValues(contractsUserSimulInbox);
						}
					});
				}
			});
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAssign) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.reassigned.contract.user.inbox.1"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = 4942531753829938995L;
				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						COL_SRV.assingUnmatchedFieldContracts(contractsUserSimulInbox);
						assignValues();
					}
				}
			});
			confirmDialog.setWidth(400, Unit.PIXELS);
			confirmDialog.setHeight(150, Unit.PIXELS);
			
		} else if (event.getButton() == btnReset) {
			assignValues();
		}
	}
}
