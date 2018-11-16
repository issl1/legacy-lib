package com.nokor.ersys.core.hr.holiday.list;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.PublicHoliday;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Holiday Table Panel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HolidayTablePanel extends AbstractTablePanel<PublicHoliday> {

	private static final long serialVersionUID = -5406068101032131503L;

	/**
	 * Holiday Table Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("holiday"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("holiday"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<PublicHoliday> createPagedDataProvider() {
		PagedDefinition<PublicHoliday> pagedDefinition = new PagedDefinition<PublicHoliday>(searchPanel.getRestrictions());

		pagedDefinition.addColumnDefinition(PublicHoliday.ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PublicHoliday.DAY, I18N.message("holiday.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PublicHoliday.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition(PublicHoliday.DESC, I18N.message("desc"), String.class, Align.LEFT, 250);

		EntityPagedDataProvider<PublicHoliday> pagedDataProvider = new EntityPagedDataProvider<PublicHoliday>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected PublicHoliday getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(PublicHoliday.ID).getValue();
			return ENTITY_SRV.getById(PublicHoliday.class, id);
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<PublicHoliday> createSearchPanel() {
		return new HolidaySearchPanel(this);
	}

}
