package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.areacode;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColArea;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Area code table panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreaTablePanel extends AbstractTablePanel<EColArea> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 2777943503384227541L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("areas"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("areas"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<EColArea> createPagedDataProvider() {
		PagedDefinition<EColArea> pagedDefinition = new PagedDefinition<EColArea>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(POSTAL_CODE, I18N.message("postal.code"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(PROVINCE + "." + DESC, I18N.message("province"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DISTRICT + "." + DESC, I18N.message("district"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(COMMUNE + "." + DESC, I18N.message("subdistrict"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(STREET, I18N.message("street"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("line1", I18N.message("soi"), String.class, Align.LEFT, 150);
		EntityPagedDataProvider<EColArea> pagedDataProvider = new EntityPagedDataProvider<EColArea>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected EColArea getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EColArea.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AreaSearchPanel createSearchPanel() {
		return new AreaSearchPanel(this);		
	}
}
