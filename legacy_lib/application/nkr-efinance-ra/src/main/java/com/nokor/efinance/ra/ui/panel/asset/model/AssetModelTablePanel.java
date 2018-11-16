package com.nokor.efinance.ra.ui.panel.asset.model;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Table.Align;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetModelTablePanel extends AbstractTablePanel<AssetModel> implements AssetEntityField,
		DeleteClickListener, SearchClickListener {
		
	/** */
	private static final long serialVersionUID = 6284842533399649221L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("asset.models"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("asset.models"));
		
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
	protected PagedDataProvider<AssetModel> createPagedDataProvider() {
		PagedDefinition<AssetModel> pagedDefinition = new PagedDefinition<AssetModel>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(ASSET_RANGE + "." + DESC, I18N.message("asset.range"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("serie", I18N.message("serie"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("standardFinanceAmount", I18N.message("standard.finance.amount"), Double.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(TI_PRICE, I18N.message("asset.price"), Double.class, Align.LEFT, 120);
		
		EntityPagedDataProvider<AssetModel> pagedDataProvider = new EntityPagedDataProvider<AssetModel>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
			
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AssetModel getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(AssetModel.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AssetModelSearchPanel createSearchPanel() {
		return new AssetModelSearchPanel(this);		
	}
}
