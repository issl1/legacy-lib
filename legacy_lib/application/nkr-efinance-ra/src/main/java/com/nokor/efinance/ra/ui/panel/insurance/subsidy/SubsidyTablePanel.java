package com.nokor.efinance.ra.ui.panel.insurance.subsidy;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.ManufacturerSubsidy;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * 
 * @author seanglay.chhoeurn	
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubsidyTablePanel extends AbstractTablePanel<ManufacturerSubsidy> implements FMEntityField {

	
	/** */
	private static final long serialVersionUID = 7428452535024888319L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("subsidy"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
	
		super.init(I18N.message("subsidy"));
		
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
	protected PagedDataProvider<ManufacturerSubsidy> createPagedDataProvider() {
 		PagedDefinition<ManufacturerSubsidy> pagedDefinition = new PagedDefinition<ManufacturerSubsidy>(searchPanel.getRestrictions());
 		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(ASSET_MAKE + "." + DESC, I18N.message("asset.makes"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(ASSET_MODEL + "." + DESC, I18N.message("asset.models"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MONTH_FROM, I18N.message("from.month"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MONTH_TO, I18N.message("to.month"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(SUBSIDY_AMOUNT, I18N.message("subsidy.amount"), Double.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("end.date"), Date.class, Align.LEFT, 140);
		
		EntityPagedDataProvider<ManufacturerSubsidy> pagedDataProvider = new EntityPagedDataProvider<ManufacturerSubsidy>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ManufacturerSubsidy getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(ManufacturerSubsidy.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected SubsidySearchPanel createSearchPanel() {
		return new SubsidySearchPanel(this);		
	}

}
