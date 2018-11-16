package com.nokor.efinance.ra.ui.panel.referential.policestation;


import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
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
public class PoliceStationTablePanel extends AbstractTablePanel<PoliceStation> implements CollectionEntityField {

	private static final long serialVersionUID = 1294290381270723346L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("police.stations"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("police.stations"));
		addDefaultNavigation();
	}		

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<PoliceStation> createPagedDataProvider() {
		PagedDefinition<PoliceStation> pagedDefinition = new PagedDefinition<PoliceStation>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PROVINCE + "." + DESC, I18N.message("province"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<PoliceStation> pagedDataProvider = new EntityPagedDataProvider<PoliceStation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected PoliceStation getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(PoliceStation.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected PoliceStationSearchPanel createSearchPanel() {
		return new PoliceStationSearchPanel(this);		
	}
	
}
