/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.province.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProvinceTablePanel extends AbstractTablePanel<Province> {
	/**	 */
	private static final long serialVersionUID = 3745950527257605627L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("provinces"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);				
		super.init(I18N.message("provinces"));
		
		addDefaultNavigation();
	}		
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Province> createPagedDataProvider() {
		PagedDefinition<Province> pagedDefinition = new PagedDefinition<Province>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 250);
		
		EntityPagedDataProvider<Province> pagedDataProvider = new EntityPagedDataProvider<Province>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Province getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Province.class, id);
		}
		return null;
	}
	
	@Override
	protected ProvinceSearchPanel createSearchPanel() {
		return new ProvinceSearchPanel(this);		
	}

}
