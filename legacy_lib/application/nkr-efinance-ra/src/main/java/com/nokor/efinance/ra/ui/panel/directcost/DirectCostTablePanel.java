package com.nokor.efinance.ra.ui.panel.directcost;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.shared.service.ServiceEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DirectCostTablePanel extends AbstractTablePanel<FinService> implements ServiceEntityField {
	
	private static final long serialVersionUID = -633342773778713476L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("direct.costs"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("direct.costs"));
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<FinService> createPagedDataProvider() {
		PagedDefinition<FinService> pagedDefinition = new PagedDefinition<FinService>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(TI_PRICE, I18N.message("service.amount"), Double.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition(CALCUL_METHOD + "." + DESC, I18N.message("calculate.method"), String.class, Align.LEFT, 150);
		
		EntityPagedDataProvider<FinService> pagedDataProvider = new EntityPagedDataProvider<FinService>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected FinService getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(FinService.class, id);
		}
		return null;
	}
	
	@Override
	protected DirectCostSearchPanel createSearchPanel() {
		return new DirectCostSearchPanel(this);		
	}
}
