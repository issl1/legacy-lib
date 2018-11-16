package com.nokor.efinance.ra.ui.panel.referential.exchangerate;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.reference.model.ExchangeRate;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author nora.ky
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeRateTablePanel extends AbstractTablePanel<ExchangeRate> implements FMEntityField {
	private static final long serialVersionUID = -1790086953464051693L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("exchangerate"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("exchangerate"));
		
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
	protected PagedDataProvider<ExchangeRate> createPagedDataProvider() {
		PagedDefinition<ExchangeRate> pagedDefinition = new PagedDefinition<ExchangeRate>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(CURRENCY_FROM + "." + DESC, I18N.message("currency.from"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CURRENCY_TO + "." + DESC, I18N.message("currency.to"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(RATE, I18N.message("rate"), Double.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(STARTDATE, I18N.message("startdate"), Date.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(ENDDATE, I18N.message("enddate"), Date.class, Align.LEFT, 120);
		
		EntityPagedDataProvider<ExchangeRate> pagedDataProvider = new EntityPagedDataProvider<ExchangeRate>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ExchangeRate getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ExchangeRate.class, id);
		}
		return null;
	}
	
	@Override
	protected ExchangeRateSearchPanel createSearchPanel() {
		return new ExchangeRateSearchPanel(this);		
	}
}
