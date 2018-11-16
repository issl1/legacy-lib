package com.nokor.efinance.ra.ui.panel.asset.make;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetMakeTablePanel extends AbstractTablePanel<AssetMake> implements AssetEntityField {
	private static final long serialVersionUID = -2983055467054135680L;
	
		
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("asset.makes"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("asset.makes"));
		
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
	protected PagedDataProvider<AssetMake> createPagedDataProvider() {
		PagedDefinition<AssetMake> pagedDefinition = new PagedDefinition<AssetMake>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("brand.name"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<AssetMake> pagedDataProvider = new EntityPagedDataProvider<AssetMake>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AssetMake getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(AssetMake.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AssetMakeSearchPanel createSearchPanel() {
		return new AssetMakeSearchPanel(this);		
	}
}
