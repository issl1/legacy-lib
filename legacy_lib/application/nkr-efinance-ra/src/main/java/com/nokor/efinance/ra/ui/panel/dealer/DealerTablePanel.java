package com.nokor.efinance.ra.ui.panel.dealer;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DealerTablePanel extends AbstractTablePanel<Dealer> implements DealerEntityField {
	private static final long serialVersionUID = -2983055467054135680L;
	
		
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("dealers"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("dealers"));
		
		addDefaultNavigation();
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Dealer> createPagedDataProvider() {
		PagedDefinition<Dealer> pagedDefinition = new PagedDefinition<Dealer>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(INTERNAL_CODE, I18N.message("dealershop.id"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(NAME_EN, I18N.message("name"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(LICENCE_NO, I18N.message("commercial.no"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<Dealer> pagedDataProvider = new EntityPagedDataProvider<Dealer>();//query data
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Dealer getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Dealer.class, id);
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Dealer) entity, EStatusRecord.INACT);
	}
	
	@Override
	protected DealerSearchPanel createSearchPanel() {
		return new DealerSearchPanel(this);		
	}
}
