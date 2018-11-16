package com.nokor.efinance.gui.ui.panel.cashflow;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.BooleanColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CashflowTablePanel extends AbstractTablePanel<Cashflow> implements CashflowEntityField {
	
	private static final long serialVersionUID = -381729263394373499L;

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("cashflows"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("cashflows"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Cashflow> createPagedDataProvider() {
		
		PagedDefinition<Cashflow> pagedDefinition = new PagedDefinition<Cashflow>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(CONTRACT + "." + REFERENCE, I18N.message("contract"), String.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(TREASURY_TYPE + "." + DESC, I18N.message("treasury"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CASHFLOW_TYPE + "." + DESC, I18N.message("type"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PAYMENT_METHOD + "." + DESC_EN, I18N.message("payment.method"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(NUM_INSTALLMENT, I18N.message("no"), Integer.class, Align.CENTER, 40);
		pagedDefinition.addColumnDefinition(INSTALLMENT_DATE, I18N.message("installment.date"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition(TI_INSTALLMENT_USD, I18N.message("amount"), Double.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition(CANCEL, I18N.message("cancel"), Double.class, Align.CENTER, 50, new BooleanColumnRenderer("x"));
		pagedDefinition.addColumnDefinition(PAID, I18N.message("paid"), Double.class, Align.CENTER, 50, new BooleanColumnRenderer("x"));
		pagedDefinition.addColumnDefinition(UNPAID, I18N.message("unpaid"), Double.class, Align.CENTER, 50, new BooleanColumnRenderer("x"));
		
		
		EntityPagedDataProvider<Cashflow> pagedDataProvider = new EntityPagedDataProvider<Cashflow>();//query data
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Cashflow getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Cashflow.class, id);
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Cashflow) entity, EStatusRecord.RECYC);
	}
	
	@Override
	protected CashflowSearchPanel createSearchPanel() {
		return new CashflowSearchPanel(this);		
	}
}
