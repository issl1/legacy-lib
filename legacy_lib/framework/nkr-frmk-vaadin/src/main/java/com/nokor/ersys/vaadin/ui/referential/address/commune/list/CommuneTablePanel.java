/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.commune.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.address.Commune;
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
public class CommuneTablePanel extends AbstractTablePanel<Commune> {
	/**	 */
	private static final long serialVersionUID = -5278960306297245783L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("communes"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("communes"));

		addDefaultNavigation();
	}

	/**
	 * Get Paged definition
	 * 
	 * @return
	 */
	@Override
	protected PagedDataProvider<Commune> createPagedDataProvider() {
		PagedDefinition<Commune> pagedDefinition = new PagedDefinition<Commune>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("district.descLocale", I18N.message("district"), String.class, Align.LEFT, 200);
		EntityPagedDataProvider<Commune> pagedDataProvider = new EntityPagedDataProvider<Commune>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Commune getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
			return ENTITY_SRV.getById(Commune.class, id);
		}
		return null;
	}

	@Override
	protected CommuneSearchPanel createSearchPanel() {
		return new CommuneSearchPanel(this);
	}
}
