package com.nokor.efinance.ra.ui.panel.document;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.DocumentGroup;
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
public class DocumentGroupTablePanel extends AbstractTablePanel<DocumentGroup> implements AssetEntityField {

	private static final long serialVersionUID = 8920822233632642486L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("documents.group"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("documents.group"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<DocumentGroup> createPagedDataProvider() {
		PagedDefinition<DocumentGroup> pagedDefinition = new PagedDefinition<DocumentGroup>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<DocumentGroup> pagedDataProvider = new EntityPagedDataProvider<DocumentGroup>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected DocumentGroup getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(DocumentGroup.class, id);
		}
		return null;
	}
	
	@Override
	protected DocumentGroupSearchPanel createSearchPanel() {
		return new DocumentGroupSearchPanel(this);		
	}
}
