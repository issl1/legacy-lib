package com.nokor.efinance.ra.ui.panel.asset.registrationstatus;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetRegistrationStatus;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Asset registration status table panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssetRegistrationStatusTablePanel extends AbstractTablePanel<AssetRegistrationStatus> implements AssetEntityField {

	/** */
	private static final long serialVersionUID = 4030661730215704503L;

	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("asset.registrations.status"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("asset.registrations.status"));
		
		addDefaultNavigation();
	}	
	
	/**
	 * Get Paged definition
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<AssetRegistrationStatus> createPagedDataProvider() {
		PagedDefinition<AssetRegistrationStatus> pagedDefinition = new PagedDefinition<AssetRegistrationStatus>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<AssetRegistrationStatus> pagedDataProvider = new EntityPagedDataProvider<AssetRegistrationStatus>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected AssetRegistrationStatus getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(AssetRegistrationStatus.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AssetRegistrationStatusSearchPanel createSearchPanel() {
		return new AssetRegistrationStatusSearchPanel(this);		
	}
}
