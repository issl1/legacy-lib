package com.nokor.efinance.core.document.template;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.DocumentTemplate;
import com.nokor.efinance.core.document.panel.DisplayDocumentPanel;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Runo;

/**
 * Document Template table panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentTemplateTablePanel extends AbstractTablePanel<DocumentTemplate> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -4379150786568391911L;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("document.templates"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("document.templates"));
		
		if (ProfileUtil.isAdmin()) {
			addDefaultNavigation();
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<DocumentTemplate> createPagedDataProvider() {
		PagedDefinition<DocumentTemplate> pagedDefinition = new PagedDefinition<DocumentTemplate>(searchPanel.getRestrictions());
		
		pagedDefinition.setRowRenderer(new DocumentRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40);
		pagedDefinition.addColumnDefinition("download", I18N.message("download"), Button.class, Align.CENTER, 70);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<DocumentTemplate> pagedDataProvider = new EntityPagedDataProvider<DocumentTemplate>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected DocumentTemplate getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(DocumentTemplate.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<DocumentTemplate> createSearchPanel() {
		return new DocumentTemplateSearchPanel(this);
	}
	
	/**
	 * Custom RowRenderer for DocumentTemplate Table panel
	 * @author bunlong.taing
	 */
	private class DocumentRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			DocumentTemplate document = (DocumentTemplate) entity;
			
			Button btnDocument = new Button();
			btnDocument.setIcon(new ThemeResource("../nkr-default/icons/16/pdf.png"));
			btnDocument.setData(document.getDocument());
			btnDocument.setStyleName(Runo.BUTTON_LINK);
			btnDocument.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = -1932985973791752733L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					new DisplayDocumentPanel((String) ((Button) event.getSource()).getData()).display();
				}
			});
			
			item.getItemProperty(ID).setValue(document.getId());
			item.getItemProperty("download").setValue(btnDocument);
			item.getItemProperty(CODE).setValue(document.getCode());
			item.getItemProperty(DESC).setValue(document.getDesc());
			item.getItemProperty(DESC_EN).setValue(document.getDescEn());
		}
		
	}

}
