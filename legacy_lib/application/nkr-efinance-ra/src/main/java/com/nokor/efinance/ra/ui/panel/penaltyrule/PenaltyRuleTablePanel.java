package com.nokor.efinance.ra.ui.panel.penaltyrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.payment.model.PenaltyRule;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * Penalty Rule TablePanel
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PenaltyRuleTablePanel extends AbstractTablePanel<PenaltyRule> implements FMEntityField {

	private static final long serialVersionUID = 3405553943450660648L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("penalty.rules"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("penalty.rules"));
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<PenaltyRule> createPagedDataProvider() {
		PagedDefinition<PenaltyRule> pagedDefinition = new PagedDefinition<PenaltyRule>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PENALTY_CALCULMETHOD + "." + DESC, I18N.message("penalty.method"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(GRACE_PERIOD, I18N.message("grace.period"), Integer.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(TI_PENALTY_AMOUNT_PER_DAY, I18N.message("penalty.amount.per.day"), Double.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(PENALTY_RATE, I18N.message("penalty.rate"), Double.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<PenaltyRule> pagedDataProvider = new EntityPagedDataProvider<PenaltyRule>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected PenaltyRule getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(PenaltyRule.class, id);
		}
		return null;
	}
	
	@Override
	protected PenaltyRuleSearchPanel createSearchPanel() {
		return new PenaltyRuleSearchPanel(this);		
	}
}
