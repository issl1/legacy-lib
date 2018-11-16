package com.nokor.efinance.ra.ui.panel.insurance.compensation.reposession;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.contract.model.MCompensationReposession;
import com.nokor.efinance.core.financial.model.ManufacturerCompensation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author seanglay.chhoeurn	
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompensationReposessionTablePanel extends AbstractTablePanel<ManufacturerCompensation> implements MCompensationReposession, FMEntityField {

	/** */
	private static final long serialVersionUID = 4398898660877686241L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("compensation.reposessions"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("compensation.reposessions"));
		
		addDefaultNavigation();
	}	
	
	/** */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<ManufacturerCompensation> createPagedDataProvider() {
 		PagedDefinition<ManufacturerCompensation> pagedDefinition = new PagedDefinition<ManufacturerCompensation>(searchPanel.getRestrictions());
 		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(ASSET_MAKE + "." + DESC, I18N.message("Brand"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(ASSET_MODEL + "." + DESC, I18N.message("Serie"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(REFUND_PERCENTAGE, I18N.message("refund.percentage"), String.class, Align.LEFT, 100, new RefundPercentageColumnRenderer());
		pagedDefinition.addColumnDefinition(FROM_MONTH, I18N.message("from.month"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(TO_MONTH, I18N.message("to.month"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("end.date"), Date.class, Align.LEFT, 130);
		
		EntityPagedDataProvider<ManufacturerCompensation> pagedDataProvider = new EntityPagedDataProvider<ManufacturerCompensation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	private class RefundPercentageColumnRenderer extends EntityColumnRenderer {

		/** */
		private static final long serialVersionUID = 8031829432850823648L;
		
		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			ManufacturerCompensation compensation = ((ManufacturerCompensation) getEntity());
			return MyNumberUtils.getDouble(compensation.getRefundPercentage()) + "%";
		}
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ManufacturerCompensation getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(ManufacturerCompensation.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CompensationReposessionSearchPanel createSearchPanel() {
		return new CompensationReposessionSearchPanel(this);		
	}

}
