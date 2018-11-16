package com.nokor.efinance.ra.ui.panel.collections.emailtemplate;


import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.reference.model.EmailTemplate;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;


/**
 * Email Template Table Panel 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmailTemplateTablePanel extends AbstractTablePanel<EmailTemplate> implements CollectionEntityField { 

	/** */
	private static final long serialVersionUID = -536511140323377223L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("email.templates"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("email.templates"));
		addDefaultNavigation();
	}		

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<EmailTemplate> createPagedDataProvider() {
		PagedDefinition<EmailTemplate> pagedDefinition = new PagedDefinition<EmailTemplate>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<EmailTemplate> pagedDataProvider = new EntityPagedDataProvider<EmailTemplate>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected EmailTemplate getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EmailTemplate.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected EmailTemplateSearchPanel createSearchPanel() {
		return new EmailTemplateSearchPanel(this);		
	}
	
}
