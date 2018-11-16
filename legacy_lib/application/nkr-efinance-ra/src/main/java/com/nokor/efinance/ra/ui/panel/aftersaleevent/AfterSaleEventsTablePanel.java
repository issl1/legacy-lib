package com.nokor.efinance.ra.ui.panel.aftersaleevent;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.aftersale.AfterSaleEvent;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AfterSaleEventsTablePanel extends AbstractTablePanel<AfterSaleEvent> implements FMEntityField{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("after.sale.events"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("after.sale.events"));
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<AfterSaleEvent> createPagedDataProvider() {
		PagedDefinition<AfterSaleEvent> pagedDefinition = new PagedDefinition<AfterSaleEvent>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("afterSaleEventType.descEn", I18N.message("after.sale.event.type"), String.class, Align.LEFT, 200);
		EntityPagedDataProvider<AfterSaleEvent> pagedDataProvider = new EntityPagedDataProvider<AfterSaleEvent>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected AfterSaleEvent getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(AfterSaleEvent.class, id);
		}
		return null;
	}
	
	@Override
	protected AfterSaleEventsSearchPanel createSearchPanel() {
		return new AfterSaleEventsSearchPanel(this);		
	}

}
