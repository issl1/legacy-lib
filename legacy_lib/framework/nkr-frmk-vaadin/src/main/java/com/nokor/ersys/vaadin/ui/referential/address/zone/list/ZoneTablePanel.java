/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.zone.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.address.Zone;
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
public class ZoneTablePanel extends AbstractTablePanel<Zone> {

	/**	 */
	private static final long serialVersionUID = -6915966122804103282L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("zones"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("zones"));

		addDefaultNavigation();
	}

	/**
	 * Get Paged definition
	 * 
	 * @return
	 */
	@Override
	protected PagedDataProvider<Zone> createPagedDataProvider() {
		PagedDefinition<Zone> pagedDefinition = new PagedDefinition<Zone>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("province.descLocale", I18N.message("province"), String.class, Align.LEFT, 200);

		EntityPagedDataProvider<Zone> pagedDataProvider = new EntityPagedDataProvider<Zone>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Zone getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
			return ENTITY_SRV.getById(Zone.class, id);
		}
		return null;
	}

	@Override
	protected ZoneSearchPanel createSearchPanel() {
		return new ZoneSearchPanel(this);
	}
}
