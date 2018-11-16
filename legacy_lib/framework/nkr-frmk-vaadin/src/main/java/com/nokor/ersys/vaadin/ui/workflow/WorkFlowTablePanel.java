/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.workflow;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfFlow;
import com.nokor.common.app.workflow.model.MEWkfFlow;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WorkFlowTablePanel extends AbstractTablePanel<EWkfFlow> implements MEWkfFlow {
	/** */
	private static final long serialVersionUID = 3820906346018987497L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("workflows"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(getCaption());
		addDefaultNavigation();
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<EWkfFlow> createPagedDataProvider() {
		PagedDefinition<EWkfFlow> pagedDefinition = new PagedDefinition<EWkfFlow>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 250);
		
		EntityPagedDataProvider<EWkfFlow> pagedDataProvider = new EntityPagedDataProvider<EWkfFlow>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	@Override
	protected EWkfFlow getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EWkfFlow.class, id);
		}
		return null;
	}

	@Override
	protected WorkFlowSearchPanel createSearchPanel() {
		return new WorkFlowSearchPanel(this);		
	}
	
}
