package com.nokor.efinance.ra.ui.panel.collections.minimunreturnrate;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.reference.model.MinReturnRate;
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
public class MinReturnRateTablePanel extends AbstractTablePanel<MinReturnRate> implements FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395563112612774433L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("min.return.rates"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);		
		super.init(I18N.message("min.return.rates"));
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<MinReturnRate> createPagedDataProvider() {
		PagedDefinition<MinReturnRate> pagedDefinition = new PagedDefinition<MinReturnRate>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("end.date"), Date.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("rateValue", I18N.message("rate.value"), Double.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<MinReturnRate> pagedDataProvider = new EntityPagedDataProvider<MinReturnRate>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected MinReturnRate getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(MinReturnRate.class, id);
		}
		return null;
	}
	
	@Override
	protected MinReturnRateSearchPanel createSearchPanel() {
		return new MinReturnRateSearchPanel(this);		
	}

}
