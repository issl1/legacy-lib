package com.nokor.efinance.ra.ui.panel.document;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.Document;
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
public class DocumentTablePanel extends AbstractTablePanel<Document> implements AssetEntityField {
	
	private static final long serialVersionUID = 7215718562616307995L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("documents"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("documents"));
		
		addDefaultNavigation();
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Document> createPagedDataProvider() {
		PagedDefinition<Document> pagedDefinition = new PagedDefinition<Document>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("applicantType.desc", I18N.message("applicant.type"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("documentGroup.desc", I18N.message("document.group"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(SORT_INDEX, I18N.message("sort.index"), Integer.class, Align.LEFT, 80);
		
		EntityPagedDataProvider<Document> pagedDataProvider = new EntityPagedDataProvider<Document>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Document getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Document.class, id);
		}
		return null;
	}
	
	@Override
	protected DocumentSearchPanel createSearchPanel() {
		return new DocumentSearchPanel(this);		
	}
}
