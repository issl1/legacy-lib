package com.nokor.efinance.ra.ui.panel.referential.address.area;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Area code table panel in collection
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AreaAddressTablePanel extends AbstractTablePanel<Area> implements FMEntityField {

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
	protected PagedDataProvider<Area> createPagedDataProvider() {
		PagedDefinition<Area> pagedDefinition = new PagedDefinition<Area>(searchPanel.getRestrictions());
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
		EntityPagedDataProvider<Area> pagedDataProvider = new EntityPagedDataProvider<Area>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Area getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Area.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AreaAddressSearchPanel createSearchPanel() {
		return new AreaAddressSearchPanel(this);		
	}
}
