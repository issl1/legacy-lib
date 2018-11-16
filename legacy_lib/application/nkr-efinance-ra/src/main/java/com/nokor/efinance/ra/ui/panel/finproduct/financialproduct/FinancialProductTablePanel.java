package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Table.Align;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FinancialProductTablePanel extends AbstractTablePanel<FinProduct> implements DealerEntityField,
		DeleteClickListener, SearchClickListener {

	private static final long serialVersionUID = -2983055467054135680L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("financial.products"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("financial.products"));
		
		addDefaultNavigation();
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
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<FinProduct> createPagedDataProvider() {
		PagedDefinition<FinProduct> pagedDefinition = new PagedDefinition<FinProduct>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("startdate"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("enddate"), Date.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<FinProduct> pagedDataProvider = new EntityPagedDataProvider<FinProduct>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected FinProduct getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(FinProduct.class, id);
		}
		return null;
	}
	
	@Override
	protected FinancialProductSearchPanel createSearchPanel() {
		return new FinancialProductSearchPanel(this);		
	}
}
