package com.nokor.efinance.ra.ui.panel.referential.matrixPrice.assetmodel;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.AssetMatrixPrice;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Table.Align;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetModelMatrixPriceTablePanel extends AbstractTablePanel<AssetMatrixPrice> implements FMEntityField,
		DeleteClickListener, SearchClickListener {
		
	private static final long serialVersionUID = 6284842533399649221L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("matrix.prices"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("matrix.prices"));
		
		addDefaultNavigation();
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<AssetMatrixPrice> createPagedDataProvider() {
		PagedDefinition<AssetMatrixPrice> pagedDefinition = new PagedDefinition<AssetMatrixPrice>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80, false);
		pagedDefinition.addColumnDefinition(ASSET_MODEL + "." + DESC_EN, I18N.message("asset.model"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(YEAR, I18N.message("year"), Integer.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(COLOR + "." + DESC_EN, I18N.message("color"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(TI_PRICE, I18N.message("price"), Double.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 120);
		EntityPagedDataProvider<AssetMatrixPrice> pagedDataProvider = new EntityPagedDataProvider<AssetMatrixPrice>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
			
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AssetMatrixPrice getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(AssetMatrixPrice.class, id);
		}
		return null;
	}
	
	@Override
	protected AssetModelMatrixPriceSearchPanel createSearchPanel() {
		return new AssetModelMatrixPriceSearchPanel(this);		
	}
}
