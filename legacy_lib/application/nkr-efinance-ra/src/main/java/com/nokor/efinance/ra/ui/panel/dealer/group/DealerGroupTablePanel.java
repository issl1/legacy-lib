package com.nokor.efinance.ra.ui.panel.dealer.group;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.shared.FMEntityField;
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
public class DealerGroupTablePanel extends AbstractTablePanel<DealerGroup> implements FMEntityField {
	
	/**
	 */
	private static final long serialVersionUID = -4371220788070058393L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("groups"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("groups"));
		
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
	protected PagedDataProvider<DealerGroup> createPagedDataProvider() {
		PagedDefinition<DealerGroup> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<DealerGroup> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected DealerGroup getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(DealerGroup.class, id);
		}
		return null;
	}
	
	@Override
	protected DealerGroupSearchPanel createSearchPanel() {
		return new DealerGroupSearchPanel(this);		
	}
}
