package com.nokor.efinance.ra.ui.panel.collections.bypassrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.ColByPassRule;
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
public class ByPassRuleTablePanel extends AbstractTablePanel<ColByPassRule> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = -2389371609389052505L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("by.pass.rules"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);		
		super.init(I18N.message("by.pass.rules"));
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<ColByPassRule> createPagedDataProvider() {
		PagedDefinition<ColByPassRule> pagedDefinition = new PagedDefinition<ColByPassRule>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(COL_BY_PASS_RULE + "." + DESC_EN, I18N.message("by.pass.rule"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition(BY_PASS_RULE_FROM + "." + DESC_EN, I18N.message("by.pass.rule.from"), String.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition(BY_PASS_RULE_TO + "." + DESC_EN, I18N.message("by.pass.rule.to"), String.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition(VALUE, I18N.message("value"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<ColByPassRule> pagedDataProvider = new EntityPagedDataProvider<ColByPassRule>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected ColByPassRule getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ColByPassRule.class, id);
		}
		return null;
	}
	
	@Override
	protected ByPassRuleSearchPanel createSearchPanel() {
		return new ByPassRuleSearchPanel(this);		
	}

}
