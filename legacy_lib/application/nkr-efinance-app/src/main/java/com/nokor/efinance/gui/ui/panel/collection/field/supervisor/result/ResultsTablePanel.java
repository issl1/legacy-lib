package com.nokor.efinance.gui.ui.panel.collection.field.supervisor.result;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Status table panel in collection 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ResultsTablePanel extends AbstractTablePanel<EColResult> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 6469771121773595576L;
	
	

	public ResultsTablePanel() {
		setCaption(I18N.message("results"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("results"));
		addDefaultNavigation();
	}
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<EColResult> createPagedDataProvider() {
		PagedDefinition<EColResult> pagedDefinition = new PagedDefinition<EColResult>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		EntityPagedDataProvider<EColResult> pagedDataProvider = new EntityPagedDataProvider<EColResult>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected EColResult getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EColResult.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected ResultsSearchPanel createSearchPanel() {
		return new ResultsSearchPanel(this);		
	}
}
