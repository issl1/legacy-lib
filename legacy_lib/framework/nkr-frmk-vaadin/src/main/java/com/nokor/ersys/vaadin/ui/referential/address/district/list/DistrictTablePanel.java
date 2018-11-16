/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.district.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.address.District;
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
public class DistrictTablePanel extends AbstractTablePanel<District> {
	/**	 */
	private static final long serialVersionUID = -8935044226288426223L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("districts"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("districts"));

		addDefaultNavigation();
	}

	/**
	 * Get Paged definition
	 * 
	 * @return
	 */
	@Override
	protected PagedDataProvider<District> createPagedDataProvider() {
		PagedDefinition<District> pagedDefinition = new PagedDefinition<District>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("province.descLocale", I18N.message("province"), String.class, Align.LEFT, 200);

		EntityPagedDataProvider<District> pagedDataProvider = new EntityPagedDataProvider<District>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected District getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
			return ENTITY_SRV.getById(District.class, id);
		}
		return null;
	}

	@Override
	protected DistrictSearchPanel createSearchPanel() {
		return new DistrictSearchPanel(this);
	}
}
