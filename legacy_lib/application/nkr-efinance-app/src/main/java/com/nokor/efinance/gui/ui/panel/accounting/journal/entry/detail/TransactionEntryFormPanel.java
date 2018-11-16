package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.detail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.third.finwiz.client.ap.ClientAP;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.TransactionEntry;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.finance.accounting.workflow.TransactionEntryWkfStatus;
import com.nokor.ersys.messaging.share.accounting.JournalEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryDTO;
import com.nokor.ersys.messaging.share.accounting.TransactionEntryStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * JournalEntryFormPanel
 * @author bunlong.taing
 */
public class TransactionEntryFormPanel extends AbstractFormPanel implements ErsysAccountingAppServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = 7430055957163455578L;
	
	private TextField txtDesc;
	
	private NavigationPanel navigationPanel;
	private NativeButton btnReject;
	private NativeButton btnValidate;
	
	private TransactionEntry transactionEntry;
	private VerticalLayout journalEntriesLayout;
	
	/**
	 */
	public TransactionEntryFormPanel() {
		super.init();
        setCaption(I18N.message("transaction.entry"));
        buildNavigationPanel();
	}
	
	/**
	 * Build Navigation Panel
	 */
	private void buildNavigationPanel() {
		
		btnReject = new NativeButton(I18N.message("reject"));
		btnReject.setIcon(FontAwesome.MINUS_CIRCLE);
		btnReject.addClickListener(this);
		
		btnValidate = new NativeButton(I18N.message("validate"));
		btnValidate.setIcon(FontAwesome.CHECK);
		btnValidate.addClickListener(this);
		
		navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnReject);
		navigationPanel.addButton(btnValidate);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtDesc = ComponentFactory.getTextField(false, 200, 200);		
		journalEntriesLayout = new VerticalLayout();
		journalEntriesLayout.setSpacing(true);
		
		HorizontalLayout horiLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horiLayout.setMargin(new MarginInfo(true, false, true, false));
		horiLayout.addComponent(ComponentLayoutFactory.getLabelCaption("description"));
		horiLayout.addComponent(txtDesc);
		return new VerticalLayout(horiLayout, journalEntriesLayout);
	}
	
	/**
	 * 
	 * @param transactionEntry
	 */
	private void buildJournalEntryFromControls(TransactionEntry transactionEntry) {		
		journalEntriesLayout.removeAllComponents();
		for (JournalEntry journalEntry : transactionEntry.getJournalEntries()) {
			JournalEntryFormPanel journalEntryFormPanel = new JournalEntryFormPanel(journalEntry);
			journalEntriesLayout.addComponent(journalEntryFormPanel);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		TransactionEntry transactionEntry = (TransactionEntry) getEntity();
		ACCOUNTING_SRV.updateTransactionEntry(transactionEntry);
		assignValues(transactionEntry.getId());
	}
	
	/**
	 * Assign values to controls
	 * @param entryId
	 */
	public void assignValues(Long entryId) {
		reset();
		if (entryId != null) {
			transactionEntry = ACCOUNTING_SRV.getById(TransactionEntry.class, entryId);
			txtDesc.setValue(transactionEntry.getDescEn());
			buildJournalEntryFromControls(transactionEntry);
			
			navigationPanel.getSaveClickButton().setVisible(TransactionEntryWkfStatus.DRAFT.equals(transactionEntry.getWkfStatus()));
			btnReject.setVisible(TransactionEntryWkfStatus.DRAFT.equals(transactionEntry.getWkfStatus()));
			btnValidate.setVisible(TransactionEntryWkfStatus.DRAFT.equals(transactionEntry.getWkfStatus()));
			if (TransactionEntryWkfStatus.REJECTED.equals(transactionEntry.getWkfStatus()) || TransactionEntryWkfStatus.APPROVED.equals(transactionEntry.getWkfStatus())) {
				removeComponent(navigationPanel);
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		txtDesc.setValue("");
	}

	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnReject) {
			transactionEntry.setWkfStatus(TransactionEntryWkfStatus.REJECTED);
			ACCOUNTING_SRV.saveOrUpdate(transactionEntry);
			String callBackUrl = transactionEntry.getCallBackUrl();
			if (StringUtils.isNotEmpty(callBackUrl)) {
				String[] callBackUrlParts = callBackUrl.split(";");
				for (int i = 0; i < callBackUrlParts.length; i++) {
					ClientAP.confirmTransaction(callBackUrlParts[i], toTransactionEntryDTO(transactionEntry));
				}
			}
			if (getParent() instanceof TabSheet) {
				((TabSheet) getParent()).setNeedRefresh(true);
			}
			assignValues(transactionEntry.getId());
		} else if (event.getButton() == btnValidate) {
			transactionEntry.setWkfStatus(TransactionEntryWkfStatus.APPROVED);
			ACCOUNTING_SRV.updateTransactionEntry(transactionEntry);
			String callBackUrl = transactionEntry.getCallBackUrl();
			if (StringUtils.isNotEmpty(callBackUrl)) {
				String[] callBackUrlParts = callBackUrl.split(";");
				for (int i = 0; i < callBackUrlParts.length; i++) {
					ClientAP.confirmTransaction(callBackUrlParts[i], toTransactionEntryDTO(transactionEntry));
				}
			}
			if (getParent() instanceof TabSheet) {
				((TabSheet) getParent()).setNeedRefresh(true);
			}
			assignValues(transactionEntry.getId());
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
	
	private class JournalEntryFormPanel extends VerticalLayout {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2296434384409159085L;
		private TextField txtDesc;
		private TextField txtAmount;
		private EntityRefComboBox<JournalEvent> cbxEvent;
		
		private JournalEntry journalEntry;
		
		/**
		 * @param journalEntry
		 */
		public JournalEntryFormPanel(JournalEntry journalEntry) {
			this.journalEntry = journalEntry;
			txtDesc = ComponentFactory.getTextField(false, 100, 200);
			txtAmount= ComponentFactory.getTextField(false, 60, 100);
					
			cbxEvent = new EntityRefComboBox<JournalEvent>();
			cbxEvent.setRestrictions(new JournalEventRestriction());
			cbxEvent.renderer();
			//cbxEvent.setRequired(true);
			cbxEvent.setWidth(300, Unit.PIXELS);

			txtDesc.setValue(journalEntry.getDescEn());
			cbxEvent.setSelectedEntity(journalEntry.getJournalEvent());
			txtAmount.setValue(AmountUtils.format(journalEntry.getAmount().doubleValue()));
			
			HorizontalLayout h = new HorizontalLayout();
			h.setSpacing(true);
			h.addComponent(ComponentLayoutFactory.getLabelCaption("desc"));
			h.addComponent(txtDesc);
			h.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("event"));
			h.addComponent(cbxEvent);
			h.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("amount"));
			h.addComponent(txtAmount);
			
			addComponent(h);
		}
		
		/**
		 * @return
		 */
		public JournalEntry getJournalEntry() {
			journalEntry.setDescEn(this.txtDesc.getValue());
			journalEntry.setJournalEvent(cbxEvent.getSelectedEntity());
			journalEntry.setAmount(BigDecimal.valueOf(getDouble(txtAmount)));
			return journalEntry;
		}
	}

	@Override
	protected Entity getEntity() {
		for (int i = 0; i < journalEntriesLayout.getComponentCount(); i++) {
			JournalEntryFormPanel journalEntryFormPanel = (JournalEntryFormPanel) journalEntriesLayout.getComponent(i);
			transactionEntry.getJournalEntries().set(i, journalEntryFormPanel.getJournalEntry());
		}
		return transactionEntry;
	}
}
