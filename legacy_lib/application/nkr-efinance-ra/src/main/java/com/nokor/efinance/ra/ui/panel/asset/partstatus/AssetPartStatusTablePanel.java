package com.nokor.efinance.ra.ui.panel.asset.partstatus;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetPartsStatus;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Asset part status table panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetPartStatusTablePanel extends AbstractTablePanel<AssetPartsStatus> implements AssetEntityField {

	/** */
	private static final long serialVersionUID = -3042156937570821887L;

	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("asset.parts.status"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("asset.parts.status"));
		
		addDefaultNavigation();
	}	
	
	/**
	 * Get Paged definition
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<AssetPartsStatus> createPagedDataProvider() {
		PagedDefinition<AssetPartsStatus> pagedDefinition = new PagedDefinition<AssetPartsStatus>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<AssetPartsStatus> pagedDataProvider = new EntityPagedDataProvider<AssetPartsStatus>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AssetPartsStatus getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(AssetPartsStatus.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AssetPartStatusSearchPanel createSearchPanel() {
		return new AssetPartStatusSearchPanel(this);		
	}
}
