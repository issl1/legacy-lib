package com.nokor.efinance.ra.ui.panel.finproduct.minimum.interest.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.AssetCategory;
import com.nokor.efinance.core.financial.model.MinimumInterest;
import com.nokor.efinance.core.financial.model.Term;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Minimum Interest Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MinimumInterestTablePanel extends AbstractTablePanel<MinimumInterest> {
	/** */
	private static final long serialVersionUID = -7271693340032387820L;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		super.init(I18N.message("minimum.interests"));
		setCaption(I18N.message("minimum.interests"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addDefaultNavigation();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<MinimumInterest> createPagedDataProvider() {
		PagedDefinition<MinimumInterest> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(MinimumInterest.ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(MinimumInterest.ASSETCATEGORY + "." + AssetCategory.DESCLOCALE, I18N.message("asset.category"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(MinimumInterest.TERM + "." + Term.DESCLOCALE, I18N.message("term"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MinimumInterest.MINIMUMINTERESTAMOUNT, I18N.message("minimum.interest.amount"), Double.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<MinimumInterest> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected MinimumInterest getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(MinimumInterest.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected MinimumInterestSearchPanel createSearchPanel() {
		return new MinimumInterestSearchPanel(this);
	}
	
}
