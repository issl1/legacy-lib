package com.nokor.ersys.vaadin.ui.security.secuser.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Table.Align;

/**
 * UserTablePanel
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SecUserTablePanel extends AbstractTablePanel<SecUser> implements VaadinServicesHelper, 
	DeleteClickListener, SearchClickListener {
	
	/**	 */
	private static final long serialVersionUID = 5306037085114421412L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("users"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);				
		super.init(I18N.message("users"));		
		addDefaultNavigation();
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<SecUser> createPagedDataProvider() {
		PagedDefinition<SecUser> pagedDefinition = new PagedDefinition<SecUser>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("login", I18N.message("login"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("desc", I18N.message("name.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("email", I18N.message("email"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("allProfilesDesc", I18N.message("profiles"), String.class, Align.LEFT, 350);
		
		EntityPagedDataProvider<SecUser> pagedDataProvider = new EntityPagedDataProvider<SecUser>();//query data
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected SecUser getEntity() {
		if (selectedItem != null) {
			final Long id = getItemSelectedId();
			if (id != null) {
			    return ENTITY_SRV.getById(SecUser.class, id);
			}
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.throwIntoRecycledBin(SecUser.class, ((SecUser) entity).getId());
	}
	
	@Override
	protected SecUserSearchPanel createSearchPanel() {
		return new SecUserSearchPanel(this);		
	}
}
