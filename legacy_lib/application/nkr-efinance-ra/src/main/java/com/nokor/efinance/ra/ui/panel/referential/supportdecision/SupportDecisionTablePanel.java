package com.nokor.efinance.ra.ui.panel.referential.supportdecision;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
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
public class SupportDecisionTablePanel extends AbstractTablePanel<SupportDecision> implements AssetEntityField {

	private static final long serialVersionUID = 5452019314675804505L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("support.decisions"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("support.decisions"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<SupportDecision> createPagedDataProvider() {
		PagedDefinition<SupportDecision> pagedDefinition = new PagedDefinition<SupportDecision>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<SupportDecision> pagedDataProvider = new EntityPagedDataProvider<SupportDecision>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected SupportDecision getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(SupportDecision.class, id);
		}
		return null;
	}
	
	@Override
	protected SupportDecisionSearchPanel createSearchPanel() {
		return new SupportDecisionSearchPanel(this);		
	}
}
