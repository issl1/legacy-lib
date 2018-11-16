package com.nokor.efinance.gui.ui.panel.accounting.receiptcode;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.finance.accounting.model.EJournalEventGroup;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.service.JournalEventRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ReceiptCodeSearchPanel extends AbstractSearchPanel<JournalEvent> {
	
	/** */
	private static final long serialVersionUID = 1367867796164215339L;
	
	private ERefDataComboBox<EJournalEventGroup> cbxGroup;
	private TextField txtCode;
	private TextField txtDesc;
	
	/**
	 * 
	 * @param paymentCodeTablePanel
	 */
	public ReceiptCodeSearchPanel(ReceiptCodeTablePanel paymentCodeTablePanel) {
		super(I18N.message("search"), paymentCodeTablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxGroup.setSelectedEntity(null);
		txtCode.setValue(StringUtils.EMPTY);
		txtDesc.setValue(StringUtils.EMPTY);
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		cbxGroup = new ERefDataComboBox<EJournalEventGroup>(EJournalEventGroup.values());
		cbxGroup.setWidth(150, Unit.PIXELS);
		
		txtCode = ComponentFactory.getTextField(false, 50, 150);
		txtDesc = ComponentFactory.getTextField(false, 100, 150);
		
		Label lblGroupTitle = ComponentFactory.getLabel(I18N.message("group"));
		Label lblCodeTitle = ComponentFactory.getLabel(I18N.message("code"));
		Label lblDescTitle = ComponentFactory.getLabel(I18N.message("desc"));
		
		GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
	
		gridLayout.addComponent(lblCodeTitle, 0, 0);
		gridLayout.addComponent(txtCode, 1, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceHeight(4, Unit.PIXELS), 2, 0);
		gridLayout.addComponent(lblDescTitle, 3, 0);
		gridLayout.addComponent(txtDesc, 4, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceHeight(4, Unit.PIXELS), 5, 0);
		gridLayout.addComponent(lblGroupTitle, 6, 0);
		gridLayout.addComponent(cbxGroup, 7, 0);
		
		return gridLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<JournalEvent> getRestrictions() {
		JournalEventRestriction journalEventRestriction = new JournalEventRestriction();
		journalEventRestriction.setJournalId(JournalEvent.RECEIPTS);
		journalEventRestriction.setCode(txtCode.getValue());
		journalEventRestriction.setDesc(txtDesc.getValue());
		journalEventRestriction.setEventGroup(cbxGroup.getSelectedEntity());
		
		return journalEventRestriction;
	}

}
