package com.nokor.ersys.vaadin.ui.security.secprofile.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Table.Align;

/**
 * ProfileTablePanel
 * @author phirun.kong
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SecProfileTablePanel extends AbstractTablePanel<SecProfile> implements
		DeleteClickListener, SearchClickListener, VaadinServicesHelper {
	
	/**	*/
	private static final long serialVersionUID = 4865995006582953763L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("profiles"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);				
		super.init(I18N.message("profiles"));		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<SecProfile> createPagedDataProvider() {
		PagedDefinition<SecProfile> pagedDefinition = new PagedDefinition<SecProfile>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 250);
		
		EntityPagedDataProvider<SecProfile> pagedDataProvider = new EntityPagedDataProvider<SecProfile>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected SecProfile getEntity() {
		if (selectedItem != null) {
			final Long id = getItemSelectedId();
			if (id != null) {
			    return ENTITY_SRV.getById(SecProfile.class, id);
			}
		}
		return null;
	}
	
	@Override
	protected SecProfileSearchPanel createSearchPanel() {
		return new SecProfileSearchPanel(this);		
	}
}
