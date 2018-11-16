package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.teamgroup;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Team and Group panel in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeamGroupTablePanel extends AbstractTablePanel<EColTeamGroup> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = -2662605517274752915L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("team.groups"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("team.groups"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<EColTeamGroup> createPagedDataProvider() {
		PagedDefinition<EColTeamGroup> pagedDefinition = new PagedDefinition<EColTeamGroup>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(TEAM + "." + DESC_EN, I18N.message("team"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(GROUP + "." + DESC_EN, I18N.message("group"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(TEAM_LEADER + "." + DESC, I18N.message("team.leader"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DEBT_LEVEL, I18N.message("debt.level"), Integer.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(REMARK, I18N.message("remark"), String.class, Align.LEFT, 220);
		EntityPagedDataProvider<EColTeamGroup> pagedDataProvider = new EntityPagedDataProvider<EColTeamGroup>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected EColTeamGroup getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EColTeamGroup.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TeamGroupSearchPanel createSearchPanel() {
		return new TeamGroupSearchPanel(this);		
	}
}
