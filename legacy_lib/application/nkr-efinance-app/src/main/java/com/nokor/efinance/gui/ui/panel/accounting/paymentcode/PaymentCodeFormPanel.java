package com.nokor.efinance.gui.ui.panel.accounting.paymentcode;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentCodeFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 4943399929851841734L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private ERefDataComboBox<EJournalEventGroup> cbxGroup;
	
	private JournalEvent journalEvent;
	
	/**
     * 
     */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField(I18N.message("code"), true, 50, 150);
		txtDesc = ComponentFactory.getTextField(I18N.message("desc"), false, 50, 150);
		
		cbxGroup = new ERefDataComboBox<EJournalEventGroup>(EJournalEventGroup.values());
		cbxGroup.setWidth(150, Unit.PIXELS);
		cbxGroup.setCaption(I18N.message("group"));
		cbxGroup.setRequired(true);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.setSpacing(true);
		
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(cbxGroup);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		journalEvent.setJournal(ENTITY_SRV.getById(Journal.class, JournalEvent.PAYMENTS));
		journalEvent.setCode(txtCode.getValue());
		journalEvent.setDesc(txtDesc.getValue());
		journalEvent.setEventGroup(cbxGroup.getSelectedEntity());
		
		return journalEvent;
	}
	
	/**
	 * 
	 * @param journalId
	 */
	public void assignValues(Long journalId) {
		super.reset();
		if (journalId != null) {
			journalEvent = ENTITY_SRV.getById(JournalEvent.class, journalId);
			txtCode.setValue(journalEvent.getCode());
			txtDesc.setValue(journalEvent.getDesc());
			cbxGroup.setSelectedEntity(journalEvent.getEventGroup() != null ? journalEvent.getEventGroup() : null);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		journalEvent = new JournalEvent();
		txtCode.setValue(StringUtils.EMPTY);
		txtDesc.setValue(StringUtils.EMPTY);
		cbxGroup.setSelectedEntity(null);
	}
	
	/** @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		super.reset();
		checkMandatoryField(txtCode, "code");
		checkMandatorySelectField(cbxGroup, "group");
		return errors.isEmpty();
	}


}
