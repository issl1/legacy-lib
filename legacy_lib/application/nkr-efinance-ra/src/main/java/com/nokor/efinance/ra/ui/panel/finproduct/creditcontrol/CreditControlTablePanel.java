package com.nokor.efinance.ra.ui.panel.finproduct.creditcontrol;


import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.CreditControl;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreditControlTablePanel extends AbstractTablePanel<CreditControl> implements CollectionEntityField{

	private static final long serialVersionUID = 1294290381270723346L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("credit.controls"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("credit.controls"));
		  addDefaultNavigation();
	}		

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<CreditControl> createPagedDataProvider() {
		PagedDefinition<CreditControl> pagedDefinition = new PagedDefinition<CreditControl>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<CreditControl> pagedDataProvider = new EntityPagedDataProvider<CreditControl>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected CreditControl getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(CreditControl.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CreditControlsSearchPanel createSearchPanel() {
		return new CreditControlsSearchPanel(this);		
	}
}
