/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.village.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VillageTablePanel extends AbstractTablePanel<Village>  {
	/**	 */
	private static final long serialVersionUID = 637253233065168912L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("villages"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("villages"));

		addDefaultNavigation();
	}

	/**
	 * Get Paged definition
	 * 
	 * @return
	 */
	@Override
	protected PagedDataProvider<Village> createPagedDataProvider() {
		PagedDefinition<Village> pagedDefinition = new PagedDefinition<Village>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("commune.descLocale", I18N.message("commune"), String.class, Align.LEFT, 200);
		EntityPagedDataProvider<Village> pagedDataProvider = new EntityPagedDataProvider<Village>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Village getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
			return ENTITY_SRV.getById(Village.class, id);
		}
		return null;
	}

	@Override
	protected VillageSearchPanel createSearchPanel() {
		return new VillageSearchPanel(this);
	}

}
