package com.nokor.efinance.gui.ui.panel.statisticconfig;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StatisticConfigTablePanel extends AbstractTablePanel<StatisticConfig> implements AssetEntityField {

	private static final long serialVersionUID = -5348044515902293311L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("statistic.configs"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("statistic.configs"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<StatisticConfig> createPagedDataProvider() {
		PagedDefinition<StatisticConfig> pagedDefinition = new PagedDefinition<StatisticConfig>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("startDate", I18N.message("start.date"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("targetHigh", I18N.message("target.high"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("targetLow", I18N.message("target.low"), Integer.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<StatisticConfig> pagedDataProvider = new EntityPagedDataProvider<StatisticConfig>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected StatisticConfig getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(StatisticConfig.class, id);
		}
		return null;
	}
	
	@Override
	protected StatisticConfigSearchPanel createSearchPanel() {
		return new StatisticConfigSearchPanel(this);		
	}
}
