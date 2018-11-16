package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.detail;

import java.math.BigDecimal;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.finance.accounting.workflow.JournalEntryWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * JournalEntryFormPanel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JournalEntryFormPanel extends AbstractFormPanel implements ErsysAccountingAppServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = 7430055957163455578L;
	
	private TextField txtDesc;
	private TextField txtDescEn;
	private TextField txtReference;
	private TextField txtInfo;
	private TextField txtOtherInfo;
	private TextField txtAmount;
	private ERefDataComboBox<ECurrency> cbxCurrency;
	private EntityRefComboBox<JournalEvent> cbxEvent;
	private EntityComboBox<Organization> cbxOrganization;
	private AutoDateField dfWhen;
	
	private NativeButton btnValidate;
	private NativeButton btnPost;
	private NativeButton btnCancel;
	
	private Long entityId;
	
	/**
	 */
	public JournalEntryFormPanel() {
		super.init();
        setCaption(I18N.message("journal.entry"));
        buildNavigationPanel();
	}
	
	/**
	 * Build Navigation Panel
	 */
	private void buildNavigationPanel() {
		btnValidate = new NativeButton(I18N.message("validate"));
		btnValidate.setIcon(FontAwesome.CHECK);
		btnValidate.addClickListener(this);
		btnValidate.setVisible(false);
		
		btnPost = new NativeButton(I18N.message("post"));
		btnPost.setIcon(FontAwesome.BOOK);
		btnPost.addClickListener(this);
		btnPost.setVisible(false);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		btnCancel.setVisible(false);
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
		navigationPanel.addButton(btnValidate);
		navigationPanel.addButton(btnPost);
		navigationPanel.addButton(btnCancel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtDesc = ComponentFactory.getTextField("desc", false, 100, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 100, 200);
		txtReference = ComponentFactory.getTextField("reference", false, 100, 200);
		txtInfo = ComponentFactory.getTextField("information", false, 100, 200);
		txtOtherInfo = ComponentFactory.getTextField("other.info", false, 100, 200);
		txtAmount= ComponentFactory.getTextField("amount", true, 100, 200);
		
		cbxCurrency = new ERefDataComboBox<ECurrency>(I18N.message("currency"), ECurrency.values());
		cbxCurrency.setWidth(200, Unit.PIXELS);
		
		cbxEvent = new EntityRefComboBox<JournalEvent>(I18N.message("event"));
		cbxEvent.setRestrictions(new JournalEventRestriction());
		cbxEvent.renderer();
		cbxEvent.setRequired(true);
		cbxEvent.setWidth(200, Unit.PIXELS);
		
		cbxOrganization = new EntityComboBox<Organization>(Organization.class, Organization.NAMELOCALE);
		cbxOrganization.setCaption(I18N.message("organization"));
		cbxOrganization.setWidth(200, Unit.PIXELS);
		cbxOrganization.setRequired(true);
		cbxOrganization.renderer();
		
		dfWhen = ComponentFactory.getAutoDateField("when", true);
		dfWhen.setValue(DateUtils.today());
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtAmount);
		formLayout.addComponent(cbxCurrency);
		formLayout.addComponent(cbxEvent);
		formLayout.addComponent(dfWhen);
		formLayout.addComponent(cbxOrganization);
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtInfo);
		formLayout.addComponent(txtOtherInfo);
		
		return formLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		JournalEntry entry = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			entry = ACCOUNTING_SRV.getById(JournalEntry.class, getEntityId());
		} else {
			entry = JournalEntry.createInstance();
		}
		buildJournalEntryFromControls(entry);
		return entry;
	}
	
	/**
	 * 
	 * @param entry
	 */
	private void buildJournalEntryFromControls(JournalEntry entry) {
		entry.setDesc(txtDesc.getValue());
		entry.setDescEn(txtDescEn.getValue());
		entry.setAmount(MyNumberUtils.getBigDecimal(txtAmount.getValue()));
		entry.setCurrency(cbxCurrency.getSelectedEntity());
		entry.setJournalEvent(cbxEvent.getSelectedEntity());
		entry.setWhen(dfWhen.getValue());
		entry.setReference(txtReference.getValue());
		entry.setInfo(txtInfo.getValue());
		entry.setOtherInfo(txtOtherInfo.getValue());
		entry.setOrganization(cbxOrganization.getSelectedEntity());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		JournalEntry entry= (JournalEntry) getEntity();
		boolean isUpdate = entry.getId() != null && entry.getId() > 0;
		if (isUpdate) {
			ACCOUNTING_SRV.updateProcess(entry);
        } else {
        	ACCOUNTING_SRV.createProcess(entry);
        	setEntityId(entry.getId());
        }
		assignValues(entry.getId());super.saveEntity();
	}
	
	/**
	 * Assign values to controls
	 * @param entryId
	 */
	public void assignValues(Long entryId) {
		reset();
		if (entryId != null) {
			setEntityId(entryId);
			JournalEntry entry = ACCOUNTING_SRV.getById(JournalEntry.class, entryId);
			BigDecimal amount = entry.getAmount();
			
			txtDesc.setValue(getDefaultString(entry.getDesc()));
			txtDescEn.setValue(getDefaultString(entry.getDescEn()));
			txtAmount.setValue(amount != null ? amount.toPlainString() : "");
			cbxCurrency.setSelectedEntity(entry.getCurrency());
			cbxEvent.setSelectedEntity(entry.getJournalEvent());
			dfWhen.setValue(entry.getWhen());
			cbxOrganization.setSelectedEntity(entry.getOrganization());
			txtReference.setValue(getDefaultString(entry.getReference()));
			txtInfo.setValue(getDefaultString(entry.getInfo()));
			txtOtherInfo.setValue(getDefaultString(entry.getOtherInfo()));
			
			btnValidate.setVisible(JournalEntryWkfStatus.NEW.equals(entry.getWkfStatus()));
			btnPost.setVisible(JournalEntryWkfStatus.VALIDATED.equals(entry.getWkfStatus()));
			btnCancel.setVisible(JournalEntryWkfStatus.NEW.equals(entry.getWkfStatus())
					|| JournalEntryWkfStatus.VALIDATED.equals(entry.getWkfStatus()));
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setEntityId(null);
		txtDesc.setValue("");
		txtDescEn.setValue("");
		txtReference.setValue("");
		txtInfo.setValue("");
		txtOtherInfo.setValue("");
		txtAmount.setValue("");
		cbxCurrency.setSelectedEntity(null);
		cbxEvent.setSelectedEntity(null);
		cbxOrganization.setSelectedEntity(null);
		dfWhen.setValue(DateUtils.today());
		
		btnValidate.setVisible(false);
		btnPost.setVisible(false);
		btnCancel.setVisible(false);
	}

	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtAmount, "amount");
		checkBigDecimalField(txtAmount, "amount");
		checkMandatorySelectField(cbxEvent, "event");
		checkMandatoryDateField(dfWhen, "when");
		checkMandatorySelectField(cbxOrganization, "organization");
		return errors.isEmpty();
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnValidate) {
			validateEntry();
		} else if (event.getButton() == btnPost) {
			postEntry();
		} else if (event.getButton() == btnCancel) {
			cancelEntry();
		}
	}
	
	/**
	 * Validate Entry
	 */
	private void validateEntry() {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.validate.journal.entry", String.valueOf(getEntityId())), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = -1099851175564998918L;
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					try {
						JournalEntry entry = (JournalEntry) getEntity();
						ACCOUNTING_SRV.reconcileJournalEntry(entry);
						assignValues(getEntityId());
						if (getParent() instanceof TabSheet) {
							((TabSheet) getParent()).setNeedRefresh(true);
						}
						displaySuccess();
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						errors.clear();
						errors.add(ex.getMessage());
						displayErrors();
					}
				}
			}
		});
	}
	
	/**
	 * Post Entry
	 */
	private void postEntry() {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.post.journal.entry", String.valueOf(getEntityId())), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = -1049811020225905652L;
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					try {
						JournalEntry entry = (JournalEntry) getEntity();
						ACCOUNTING_SRV.postJournalEntryIntoLedger(entry);
						assignValues(getEntityId());
						if (getParent() instanceof TabSheet) {
							((TabSheet) getParent()).setNeedRefresh(true);
						}
						displaySuccess();
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						errors.clear();
						errors.add(ex.getMessage());
						displayErrors();
					}
					
				}
			}
		});
	}
	
	/**
	 * Cancel Entry
	 */
	private void cancelEntry() {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.info.cancel.journal.entry", String.valueOf(getEntityId())), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = -7429155717240334658L;
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					try {
						JournalEntry entry = (JournalEntry) getEntity();
						ACCOUNTING_SRV.cancelJournalEntry(entry);
						assignValues(getEntityId());
						if (getParent() instanceof TabSheet) {
							((TabSheet) getParent()).setNeedRefresh(true);
						}
						displaySuccess();
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						errors.clear();
						errors.add(ex.getMessage());
						displayErrors();
					}
				}
			}
		});
	}
	
}
