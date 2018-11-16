package com.nokor.efinance.gui.ui.panel.accounting.paymentcode;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MEntityA;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.MJournalEvent;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentCodeTablePanel extends AbstractTablePanel<JournalEvent> implements MJournalEvent, MEntityA{

	/** */
	private static final long serialVersionUID = -6771040579643241241L;

	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("payment.codes"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("payment.codes"));
		
		addDefaultNavigation();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<JournalEvent> createPagedDataProvider() {
		PagedDefinition<JournalEvent> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50, false);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 170);
		pagedDefinition.addColumnDefinition(JournalEvent.EVENTGROUP + "." + "desc", I18N.message("group"), String.class, Align.LEFT, 170);	
		
		EntityPagedDataProvider<JournalEvent> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected JournalEvent getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(JournalEvent.class, id);
		}
		return null;
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}	

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected PaymentCodeSearchPanel createSearchPanel() {
		return new PaymentCodeSearchPanel(this);
	}
}
