package com.nokor.efinance.gui.ui.panel.marketing.team;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.marketing.model.Team;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamMarketingTablePanel extends AbstractTablePanel<Team> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = 8146093387723342055L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("teams"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("teams"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Team> createPagedDataProvider() {
		PagedDefinition<Team> pagedDefinition = new PagedDefinition<Team>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(Team.ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Team.DESCRIPTION, I18N.message("desc"), String.class, Align.LEFT, 150);
		EntityPagedDataProvider<Team> pagedDataProvider = new EntityPagedDataProvider<Team>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Team getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Team.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TeamMarketingSearchPanel createSearchPanel() {
		return new TeamMarketingSearchPanel(this);		
	}
}
