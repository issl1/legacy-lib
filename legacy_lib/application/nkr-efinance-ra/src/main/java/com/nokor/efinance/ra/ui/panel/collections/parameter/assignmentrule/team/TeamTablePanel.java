package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.team;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Team table panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamTablePanel extends AbstractTablePanel<EColTeam> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2662605517274752915L;

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
	protected PagedDataProvider<EColTeam> createPagedDataProvider() {
		PagedDefinition<EColTeam> pagedDefinition = new PagedDefinition<EColTeam>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(COL_TYPE + "." + DESC_EN, I18N.message("collection.type"), String.class, Align.LEFT, 150);
		EntityPagedDataProvider<EColTeam> pagedDataProvider = new EntityPagedDataProvider<EColTeam>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected EColTeam getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EColTeam.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TeamSearchPanel createSearchPanel() {
		return new TeamSearchPanel(this);		
	}
}
