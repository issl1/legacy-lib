package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.callcenterresult;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CallCenterResultsTablePanel extends AbstractTablePanel<ECallCenterResult> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -4022993745596162651L;

	/**
	 * 
	 */
	public CallCenterResultsTablePanel() {
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
	protected PagedDataProvider<ECallCenterResult> createPagedDataProvider() {
		PagedDefinition<ECallCenterResult> pagedDefinition = new PagedDefinition<ECallCenterResult>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<ECallCenterResult> pagedDataProvider = new EntityPagedDataProvider<ECallCenterResult>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ECallCenterResult getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ECallCenterResult.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CallCenterResultsSearchPanel createSearchPanel() {
		return new CallCenterResultsSearchPanel(this);		
	}
}
