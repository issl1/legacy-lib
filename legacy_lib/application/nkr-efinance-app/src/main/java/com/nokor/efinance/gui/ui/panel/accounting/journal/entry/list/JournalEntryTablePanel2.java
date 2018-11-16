package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.list;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.finance.accounting.workflow.JournalEntryWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.util.IndexedContainer;
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
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JournalEntryTablePanel2 extends AbstractTablePanel<JournalEntry> implements ErsysAccountingAppServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = -2340866604826407696L;
	private static final String CHECKBOX = "checkBox";
	
	private NativeButton btnValidate;
	private NativeButton btnPost;
	private NativeButton btnCancel;
	
	private Map<Long, CheckBox> mapCheckBoxes;
	private PagedDefinition<JournalEntry> pagedDefinition;
	private boolean isCheckedAll = false;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		mapCheckBoxes = new HashMap<Long, CheckBox>();
		createNavigationPanel();
		super.init(I18N.message("journal.entries"));
		setCaption(I18N.message("journal.entries"));
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
				if (!((JournalEntrySearchPanel) searchPanel).isNewOrValidated()) {
					JournalEntry entry = ACCOUNTING_SRV.getById(JournalEntry.class, getItemSelectedId());
					setVisibleButton(false);
					if (JournalEntryWkfStatus.NEW.equals(entry.getWkfStatus())) {
						btnValidate.setVisible(true);
						btnCancel.setVisible(true);
					} else if (JournalEntryWkfStatus.VALIDATED.equals(entry.getWkfStatus())) {
						btnPost.setVisible(true);
						btnCancel.setVisible(true);
					}
				}
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
		
		btnPost = new NativeButton(I18N.message("post"));
		btnPost.setIcon(FontAwesome.BOOK);
		btnPost.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		NavigationPanel navigationPanel = addDefaultNavigation();
		navigationPanel.addButton(ComponentFactory.getSpaceLayout(25, Unit.PIXELS));
		navigationPanel.addButton(btnValidate);
		navigationPanel.addButton(btnPost);
		navigationPanel.addButton(btnCancel);
		setVisibleButton(false);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<JournalEntry> createPagedDataProvider() {
		pagedDefinition = new PagedDefinition<JournalEntry>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(CHECKBOX, "", CheckBox.class, Align.LEFT, 25, new CheckBoxColumnRenderer());
		pagedDefinition.addColumnDefinition(JournalEntry.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(JournalEntry.DESC, I18N.message("desc"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(JournalEntry.AMOUNT, I18N.message("amount"), BigDecimal.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.WHEN, I18N.message("when"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.JOURNALEVENT + "." + JournalEvent.DESCLOCALE, I18N.message("event"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(JournalEntry.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.WKFSTATUS + "." + EWkfStatus.CODE, I18N.message("status"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.INFO, I18N.message("information"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(JournalEntry.CURRENCY + "." + ECurrency.CODE, I18N.message("currency"), String.class, Align.LEFT, 100);
		
		JournalEntryPagedDataProvider pagedDataProvider = new JournalEntryPagedDataProvider();
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
	 * JournalEntry Paged Data Provider
	 * @author bunlong.taing
	 */
	private class JournalEntryPagedDataProvider extends EntityPagedDataProvider<JournalEntry> {
		/** */
		private static final long serialVersionUID = 1819003460390593102L;
		
		@Override
		public IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults) {
			IndexedContainer container = super.getIndexedContainer(firstResult, maxResults);
			if (!((JournalEntrySearchPanel) searchPanel).isNewOrValidated()) {
				container.removeContainerProperty(CHECKBOX);
			}
			return container;
		}
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected JournalEntry getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ACCOUNTING_SRV.getById(JournalEntry.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected JournalEntrySearchPanel createSearchPanel() {
		// return new JournalEntrySearchPanel(this);
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		setVisibleButton(false);
		super.refresh();
		JournalEntrySearchPanel searchPanel = (JournalEntrySearchPanel) this.searchPanel;
		if (searchPanel.isNewOrValidated()) {
			if (searchPanel.isStatusNew()) {
				btnValidate.setVisible(true);
			}
			if (searchPanel.isStatusValidated()) {
				btnPost.setVisible(true);
			}
			btnCancel.setVisible(true);
		}
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
	 * Set Visible Button
	 * @param visible
	 */
	private void setVisibleButton(boolean visible) {
		btnValidate.setVisible(visible);
		btnPost.setVisible(visible);
		btnCancel.setVisible(visible);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnValidate) {
			validateEntries();
		} else if (event.getButton() == btnPost) {
			postEntries();
		} else if (event.getButton() == btnCancel) {
			cancelEntries();
		}
	}
	
	/**
	 * Validate Journal Entries
	 */
	private void validateEntries() {
		JournalEntrySearchPanel searchPanel = ((JournalEntrySearchPanel) this.searchPanel);
		if (getSelectedIds().isEmpty() && searchPanel.isNewOrValidated()) {
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
							// CheckBox select mode
							if (searchPanel.isNewOrValidated()) {
								List<JournalEntry> entries = ACCOUNTING_SRV.listByIds(JournalEntry.class, getSelectedIds());
								ACCOUNTING_SRV.reconcileJournalEntries(entries);
							} else {	// Table select mode
								JournalEntry entry = ACCOUNTING_SRV.getById(JournalEntry.class, getItemSelectedId());
								ACCOUNTING_SRV.reconcileJournalEntry(entry);
							}
							searchButtonClick(null);
							Notification.show(I18N.message("journal.entries"), I18N.message("msg.info.change.successfully"), Type.HUMANIZED_MESSAGE);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
							Notification.show(I18N.message("journal.entries"), ex.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				}
			});
		}
	}
	
	/**
	 * Post Journal Entries
	 */
	private void postEntries() {
		JournalEntrySearchPanel searchPanel = ((JournalEntrySearchPanel) this.searchPanel);
		if (getSelectedIds().isEmpty() && searchPanel.isNewOrValidated()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.post.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.post.journal.entries"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -627507790677817902L;
				public void onClose(ConfirmDialog dialog) {
					// CheckBox select mode
					if (dialog.isConfirmed()) {
						try {
							if (searchPanel.isNewOrValidated()) {
								List<JournalEntry> entries = ACCOUNTING_SRV.listByIds(JournalEntry.class, getSelectedIds());
								ACCOUNTING_SRV.postJournalEntriesIntoLedger(entries);
							} else {	// Table select mode
								JournalEntry entry = ACCOUNTING_SRV.getById(JournalEntry.class, getItemSelectedId());
								ACCOUNTING_SRV.postJournalEntryIntoLedger(entry);
							}
							searchButtonClick(null);
							Notification.show(I18N.message("journal.entries"), I18N.message("msg.info.change.successfully"), Type.HUMANIZED_MESSAGE);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
							Notification.show(I18N.message("journal.entries"), ex.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				}
			});
		}
	}
	
	/**
	 * Cancel Journal Entries
	 */
	private void cancelEntries() {
		JournalEntrySearchPanel searchPanel = ((JournalEntrySearchPanel) this.searchPanel);
		if (getSelectedIds().isEmpty() && searchPanel.isNewOrValidated()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.cancel.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.cancel.journal.entries"), new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -627507790677817902L;
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
							// CheckBox select mode
							if (searchPanel.isNewOrValidated()) {
								List<JournalEntry> entries = ACCOUNTING_SRV.listByIds(JournalEntry.class, getSelectedIds());
								ACCOUNTING_SRV.cancelJournalEntries(entries);
							} else {	// Table select mode
								JournalEntry entry = ACCOUNTING_SRV.getById(JournalEntry.class, getItemSelectedId());
								ACCOUNTING_SRV.cancelJournalEntry(entry);
							}
							searchButtonClick(null);
							Notification.show(I18N.message("journal.entries"), I18N.message("msg.info.change.successfully"), Type.HUMANIZED_MESSAGE);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
							Notification.show(I18N.message("journal.entries"), ex.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				}
			});
		}
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
