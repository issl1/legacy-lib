/*
 * Created on 22/06/2015.
 */
package com.nokor.ersys.vaadin.ui.history.item;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.WkfBaseHistory;
import com.nokor.common.app.workflow.model.WkfHistory;
import com.nokor.common.app.workflow.model.WkfHistoryItem;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WkfHistoryItemTablePanel extends AbstractTabPanel implements SearchClickListener, SeuksaServicesHelper {
	/** */
	private static final long serialVersionUID = -6955833698693091222L;

	private WkfBaseHistory baseWkfHistory;
	private String customEntity;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<Entity> simpleTable;
	private IWkfHistoryItemDataProvider historyItemDataProvider;
	
	public WkfHistoryItemTablePanel() {
		super();
		setSizeFull();	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
	}
	
	public void setHistoryItemDataProvider(IWkfHistoryItemDataProvider historyItemDataProvider) {
		this.historyItemDataProvider = historyItemDataProvider;
	}
	
	@Override
	public void searchButtonClick(ClickEvent event) {
		refreshTable();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setMargin(true);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.setSortEnabled(false);
		contentLayout.addComponent(simpleTable);

        return contentLayout;
	}
	
	/**
	 * Get Column definitions
	 * @return {@link List<ColumnDefinition>}
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("no", "#", Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("propertyName", I18N.message("history.entity.propertyName"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("oldValue", I18N.message("history.entity.oldValue"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("newValue", I18N.message("history.entity.newValue"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition("modifiedBy", I18N.message("history.entity.modifiedBy"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date.updated"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Get indexed container
	 * @return {@link IndexedContainer}
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<? extends Entity> entities) {
		IndexedContainer indexedContainer = (IndexedContainer) simpleTable.getContainerDataSource();
		indexedContainer.removeAllItems();

		if (entities != null && !entities.isEmpty()) {
			int index = 0;
			
			for (Entity entity : entities) {
				WkfHistoryItem wkfHistoryItem = (WkfHistoryItem) entity;
				Item item = indexedContainer.addItem(wkfHistoryItem.getId());
				item.getItemProperty("no").setValue(++index);
				item.getItemProperty("propertyName").setValue(wkfHistoryItem.getPropertyName());
				item.getItemProperty("oldValue").setValue(wkfHistoryItem.getOldValue());
				item.getItemProperty("newValue").setValue(wkfHistoryItem.getNewValue());
				item.getItemProperty("modifiedBy").setValue(wkfHistoryItem.getModifiedBy().getDesc());
				item.getItemProperty("updateDate").setValue((DateUtils.date2StringDDMMYYYY_MINUS(wkfHistoryItem.getUpdateDate())));
			}
		}
		return indexedContainer;
	}

	/**
	 * Refresh table after save action
	 */
	public void refreshTable() {
		assignValues(baseWkfHistory.getId(), customEntity);
	}
	
	/**
	 * Assign values to controls
	 * @param entityId : {@link Long}
	 */
	public void assignValues(Long entityId, String customEntity) {
		super.reset();
		this.customEntity = customEntity;
		
		if (entityId != null) {
			List<? extends Entity> entities = new ArrayList<>();
			
			// Must to reload to refresh session
			if (customEntity == null) {
				baseWkfHistory = ENTITY_SRV.getById(WkfHistory.class, entityId);
				entities = fetchHistoryItems(baseWkfHistory);
			} else {
				if (historyItemDataProvider == null) {
					throw new IllegalArgumentException("[HistoryItemDataProvider] is null");
				}
				entities = historyItemDataProvider.fetchCustomEntities(baseWkfHistory, customEntity, entityId);
			}
			simpleTable.setContainerDataSource(getIndexedContainer(entities));
		}
	}
	
	/**
	 * 
	 * @param baseWkfHistory
	 * @return
	 */
	private List<? extends Entity> fetchHistoryItems(WkfBaseHistory baseWkfHistory) {
		BaseRestrictions<WkfHistoryItem> restrictions = new BaseRestrictions<WkfHistoryItem>(WkfHistoryItem.class);			
		restrictions.addCriterion("wkfHistory.id", baseWkfHistory.getId());
		restrictions.addOrder(Order.desc("changeDate"));
		return ENTITY_SRV.list(restrictions);
	}
}
