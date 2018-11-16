package com.nokor.efinance.ra.ui.panel.vat;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.Vat;
import com.nokor.efinance.core.shared.FMEntityField;
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
public class VatsTablePanel extends AbstractTablePanel<Vat> implements FMEntityField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("vats"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("vats"));
		addDefaultNavigation();
	}

	@Override
	protected PagedDataProvider<Vat> createPagedDataProvider() {
		PagedDefinition<Vat> pagedDefinition = new PagedDefinition<Vat>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("value", I18N.message("value"), Double.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("end.date"), Date.class, Align.LEFT, 150);
		
		EntityPagedDataProvider<Vat> pagedDataProvider = new EntityPagedDataProvider<Vat>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected Vat getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Vat.class, id);
		}
		return null;
	}
	
	@Override
	protected VatsSearchPanel createSearchPanel() {
		return new VatsSearchPanel(this);		
	}

}
