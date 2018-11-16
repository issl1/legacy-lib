package com.nokor.efinance.ra.ui.panel.document;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.shared.FMEntityField;
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
public class DocumentUwGroupTablePanel extends AbstractTablePanel<DocumentUwGroup> implements FMEntityField {

	private static final long serialVersionUID = -6675694004563946811L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("documents.uw.group"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("documents.uw.group"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<DocumentUwGroup> createPagedDataProvider() {
		PagedDefinition<DocumentUwGroup> pagedDefinition = new PagedDefinition<DocumentUwGroup>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("applicantType.desc", I18N.message("applicant.type"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<DocumentUwGroup> pagedDataProvider = new EntityPagedDataProvider<DocumentUwGroup>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected DocumentUwGroup getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(DocumentUwGroup.class, id);
		}
		return null;
	}
	
	@Override
	protected DocumentUwGroupSearchPanel createSearchPanel() {
		return new DocumentUwGroupSearchPanel(this);		
	}
}
