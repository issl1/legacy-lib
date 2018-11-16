package com.nokor.efinance.gui.ui.panel.statisticconfig;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;

/**
 * StatisticConfigSearchPanel 
 * @author sok.vina
 *
 */
public class StatisticConfigSearchPanel extends AbstractSearchPanel<StatisticConfig> implements FMEntityField {

	private static final long serialVersionUID = 9136565522330994370L;
	
	private DealerComboBox cbxDealer;
	
	public StatisticConfigSearchPanel(StatisticConfigTablePanel statisticConfigTablePanel) {
		super(I18N.message("search"), statisticConfigTablePanel);
	}
	
	@Override
	protected void reset() {
		cbxDealer.setSelectedEntity(null);
	}


	@Override
	protected Component createForm() {		
		final GridLayout gridLayout = new GridLayout(4, 1);		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));  
        gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(cbxDealer), 1, 0);
        
		return gridLayout;
	}
	
	@Override
	public BaseRestrictions<StatisticConfig> getRestrictions() {		
		BaseRestrictions<StatisticConfig> restrictions = new BaseRestrictions<StatisticConfig>(StatisticConfig.class);		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", cbxDealer.getSelectedEntity().getId()));
		}
		restrictions.addOrder(Order.asc(ID));
		return restrictions;
	}

}
