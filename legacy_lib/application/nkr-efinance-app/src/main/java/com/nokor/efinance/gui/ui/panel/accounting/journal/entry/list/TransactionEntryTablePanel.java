package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.third.finwiz.client.ap.ClientAP;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.MTransactionEntry;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfStatus;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.UI;

/**
 * JournalEntry Table Panel
 * @author bunlong.taing
 */
public class TransactionEntryTablePanel extends AbstractTablePanel<TransactionEntry> implements ErsysAccountingAppServicesHelper, ClickListener {
	
	/**
	 */
	private static final long serialVersionUID = 543240817634833963L;

	private static final String CHECKBOX = "checkBox";
	
	private NativeButton btnValidate;
	
	private Map<Long, CheckBox> mapCheckBoxes;
	private PagedDefinition<TransactionEntry> pagedDefinition;
	private boolean isCheckedAll = false;
	
	public TransactionEntryTablePanel() {
		mapCheckBoxes = new HashMap<Long, CheckBox>();
		createNavigationPanel();
		super.init(I18N.message("transaction.entries"));
		setCaption(I18N.message("transaction.entries"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		pagedTable.addHeaderClickListener(new HeaderClickListener() {
			/** */
			private static final long serialVersionUID = -1968298641891511983L;
			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (CHECKBOX.equals(event.getPropertyId())) {
					for (CheckBox checkBox : mapCheckBoxes.values()) {
						checkBox.setValue(!isCheckedAll);
					}
					isCheckedAll = !isCheckedAll;
				}
			}
		});
		pagedTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = -916616375624106865L;
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
			}
		});
		pagedTable.setColumnIcon(CHECKBOX, FontAwesome.CHECK);
	}
	
	/**
	 * Create Navigation Panel
	 */
	private void createNavigationPanel() {
		btnValidate = new NativeButton(I18N.message("validate"));
		btnValidate.setIcon(FontAwesome.CHECK);
		btnValidate.addClickListener(this);
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		navigationPanel.addButton(btnValidate);
		addComponent(navigationPanel, 0);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<TransactionEntry> createPagedDataProvider() {
		pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(CHECKBOX, "", CheckBox.class, Align.LEFT, 25, new CheckBoxColumnRenderer());
		pagedDefinition.addColumnDefinition(TransactionEntry.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(MTransactionEntry.DESC, I18N.message("desc"), String.class, Align.LEFT, 300);
		pagedDefinition.addColumnDefinition(MTransactionEntry.WKFSTATUS + "." + MTransactionEntry.DESCEN, I18N.message("status"), String.class, Align.LEFT, 200);			
		EntityPagedDataProvider<TransactionEntry> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * CheckBox Column Renderer
	 * @author bunlong.taing
	 */
	private class CheckBoxColumnRenderer extends EntityColumnRenderer {
		/** */
		private static final long serialVersionUID = 2203725336455932592L;
		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Long id = getEntity().getId();
			CheckBox checkBox = null;
			if (mapCheckBoxes.containsKey(id)) {
				checkBox = mapCheckBoxes.get(id);
			} else {
				checkBox = new CheckBox();
				mapCheckBoxes.put(id, checkBox);
			}
			return checkBox;
		}
		
	}
	

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected TransactionEntry getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ACCOUNTING_SRV.getById(TransactionEntry.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TransactionEntrySearchPanel createSearchPanel() {
		return new TransactionEntrySearchPanel(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#searchButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void searchButtonClick(ClickEvent event) {
		mapCheckBoxes.clear();
		super.searchButtonClick(event);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnValidate) {
			validateEntries();
		}
	}
	
	/**
	 * Validate Journal Entries
	 */
	private void validateEntries() {
		if (getSelectedIds().isEmpty()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.validate.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.validate.journal.entries"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -627507790677817902L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
							for (Long TransactionId : getSelectedIds()) {
								TransactionEntry transactionEntry = ACCOUNTING_SRV.getById(TransactionEntry.class, TransactionId);
								transactionEntry.setWkfStatus(TransactionEntryWkfStatus.APPROVED);
								ACCOUNTING_SRV.updateTransactionEntry(transactionEntry);
								String callBackUrl = transactionEntry.getCallBackUrl();
								if (StringUtils.isNotEmpty(callBackUrl)) {
									String[] callBackUrlParts = callBackUrl.split(";");
									for (int i = 0; i < callBackUrlParts.length; i++) {
										ClientAP.confirmTransaction(callBackUrlParts[i], toTransactionEntryDTO(transactionEntry));
									}
								}
								refresh();
							}
							Notification.show(I18N.message("journal.entries"), I18N.message("msg.info.change.successfully"), Type.HUMANIZED_MESSAGE);
						} catch (Exception ex) {
							Notification.show(I18N.message("journal.entries"), ex.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				}
			});
		}
	}
	
	/**
	 * @param transactionEntry
	 * @return
	 */
	private TransactionEntryDTO toTransactionEntryDTO(TransactionEntry transactionEntry) {
		TransactionEntryDTO transactionEntryDTO = new TransactionEntryDTO();
		transactionEntryDTO.setId(transactionEntry.getId());
		transactionEntryDTO.setDesc(transactionEntry.getDesc());
		transactionEntryDTO.setStatus(TransactionEntryStatus.valueOf(transactionEntry.getWkfStatus().getCode()));
		transactionEntryDTO.setJournalEntries(toJournalEntryDTOs(transactionEntry.getJournalEntries()));
		return transactionEntryDTO;
	}
	/**
	 * 
	 * @param entries
	 * @return
	 */
	private List<JournalEntryDTO> toJournalEntryDTOs(List<JournalEntry> entries) {
		List<JournalEntryDTO> dtoLst = new ArrayList<>();
		for (JournalEntry entry : entries) {
			dtoLst.add(toJournalEntryDTO(entry));
		}
		return dtoLst;
	}
	
	/**
	 * 
	 * @param entry
	 * @return
	 */
	private JournalEntryDTO toJournalEntryDTO(JournalEntry entry) {
		JournalEntryDTO entryDTO = new JournalEntryDTO();
	
		entryDTO.setId(entry.getId());
		if (entry.getOrganization() != null) {
			entryDTO.setOrganizationId(entry.getOrganization().getId());
		}
		entryDTO.setDesc(entry.getDesc());
		entryDTO.setDescEn(entry.getDescEn());
		entryDTO.setReference(entry.getReference());
		entryDTO.setJournalEventCode(entry.getJournalEvent().getCode());
		entryDTO.setInfo(entry.getInfo());
		entryDTO.setOtherInfo(entry.getOtherInfo());
		entryDTO.setWhen(entry.getWhen());
		entryDTO.setAmounts(entry.getAmounts());
		entryDTO.setWkfStatusCode(entry.getWkfStatus().getCode());
		
		return entryDTO;
	}
	
	
	/**
	 * Get selected Ids
	 * @return
	 */
	private List<Long> getSelectedIds() {
		List<Long> ids = new ArrayList<Long>();
		for (Long id : mapCheckBoxes.keySet()) {
			if (mapCheckBoxes.get(id).getValue()) {
				ids.add(id);
			}
		}
		return ids;
	}

}
