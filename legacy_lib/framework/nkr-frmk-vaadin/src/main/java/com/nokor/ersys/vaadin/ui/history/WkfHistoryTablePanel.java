/*
 * Created on 30/06/2015.
 */
package com.nokor.ersys.vaadin.ui.history;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityA;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.WkfHistory;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.DateColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * History Table Panel.
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WkfHistoryTablePanel extends AbstractTablePanel<WkfHistory> {
	/** */
	private static final long serialVersionUID = 1015982564946591231L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("histories"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(getCaption());
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addRefreshClickListener(this);
	}
	
	public void setHistoryDataProvider(IWkfHistoryDataProvider historyDataProvider) {
		WkfHistoryPagedDataProvider pagedDataProvider = (WkfHistoryPagedDataProvider) getPagedTable().getDataProvider();
		pagedDataProvider.setHistoryDataProvider(historyDataProvider);
	}
	
	@Override
	protected PagedDataProvider<WkfHistory> createPagedDataProvider() {
		PagedDefinition<WkfHistory> pagedDefinition = new PagedDefinition<WkfHistory>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 45);
		pagedDefinition.addColumnDefinition("flow.desc", I18N.message("history.wkfflow"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("entity", I18N.message("history.entity"), String.class, Align.LEFT, 250, new EntityColumnRenderer() {
			/** */
			private static final long serialVersionUID = -8251467718387889971L;

			@Override
			public Object getValue() {
				WkfHistory wkfHistory = (WkfHistory) getEntity();
				Class<? extends EntityA> clazz;
				
				try {
					clazz = (Class<? extends EntityA>) Class.forName(wkfHistory.getEntity().getCode());
					EntityA entityA = (EntityA) ENTITY_SRV.getById(clazz, wkfHistory.getEntityId());
					return entityA.toString();
				} catch (ClassNotFoundException e) {
					logger.error(e.getMessage(), e);
				}
				return "";
			}
		});
		pagedDefinition.addColumnDefinition("createDate", I18N.message("create.date"), String.class, Align.LEFT, 150, new DateColumnRenderer("dd/MM/yyyy hh:mm:ss a"));
		pagedDefinition.addColumnDefinition("createUser", I18N.message("create.user"), String.class, Align.LEFT, 130);

		WkfHistoryPagedDataProvider pagedDataProvider = new WkfHistoryPagedDataProvider((WkfHistorySearchPanel) searchPanel);
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected WkfHistory getEntity() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected WkfHistorySearchPanel createSearchPanel() {
		return new WkfHistorySearchPanel(this);		
	}
	
	/**
	 * 
	 * @return
	 */
	public WkfHistorySearchPanel getSearchPanel() {
		return (WkfHistorySearchPanel) searchPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCustomEntity() {
		WkfHistoryPagedDataProvider pagedDataProvider = (WkfHistoryPagedDataProvider) getPagedTable().getDataProvider();
		return pagedDataProvider.getCustomEntity();
	}
}
