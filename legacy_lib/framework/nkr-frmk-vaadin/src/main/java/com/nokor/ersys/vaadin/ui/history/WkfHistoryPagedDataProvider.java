package com.nokor.ersys.vaadin.ui.history;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.workflow.model.WkfHistory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PropertyColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 * 
 * @author bunlong
 *
 */
public class WkfHistoryPagedDataProvider extends EntityPagedDataProvider<WkfHistory> {
	/** */
	private static final long serialVersionUID = 2848211309524400542L;

	private List<? extends Entity> entities;
	private String customEntity;
	private WkfHistorySearchPanel searchPanel;
	private IWkfHistoryDataProvider historyDataProvider;
	
	/**
	 * 
	 * @param searchPanel
	 */
	public WkfHistoryPagedDataProvider(WkfHistorySearchPanel searchPanel) {
		this.searchPanel = searchPanel;
	}
	
	/**
	 * 
	 * @param historyDataProvider
	 */
	public void setHistoryDataProvider(IWkfHistoryDataProvider historyDataProvider) {
		this.historyDataProvider = historyDataProvider;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCustomEntity() {
		return customEntity;
	}
	
	/**
	 * 
	 * @param indexedContainer
	 */
	private void buildColumnsInIndexedContainer(IndexedContainer indexedContainer) {
		for (ColumnDefinition columnDefinition : getPagedDefinition().getColumnDefinitions()) {
			indexedContainer.addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
		}
	}

	/**
	 * 
	 * @param indexedContainer
	 */
	private void buildCustomRowsInIndexedContainer(IndexedContainer indexedContainer) {
		for (Entity entity : entities) {
			Item item = indexedContainer.addItem(entity.getId());
			
			for (ColumnDefinition columnDefinition : getPagedDefinition().getColumnDefinitions()) {
				try {
					String propertyId = columnDefinition.getPropertyId();
					if (columnDefinition.getColumnRenderer() == null) {
						PropertyUtilsBean bean = new PropertyUtilsBean();
						item.getItemProperty(propertyId).setValue(bean.getNestedProperty(entity, propertyId));
					} else {
						ColumnRenderer columnRenderer = columnDefinition.getColumnRenderer();
						if (columnRenderer instanceof EntityColumnRenderer) {
							((EntityColumnRenderer) columnRenderer).setEntity(entity);
						} else if (columnRenderer instanceof PropertyColumnRenderer) {
							PropertyUtilsBean bean = new PropertyUtilsBean();
							((PropertyColumnRenderer) columnRenderer).setPropertyValue(bean.getNestedProperty(entity, propertyId));
						}
						item.getItemProperty(propertyId).setValue(columnRenderer.getValue());
					}
					
				} catch (com.vaadin.data.Property.ReadOnlyException e) {
					logger.error("ReadOnlyException", e);
				} catch (IllegalAccessException e) {
					logger.error("IllegalAccessException", e);
				} catch (InvocationTargetException e) {
					logger.error("InvocationTargetException", e);
				} catch (NoSuchMethodException e) {
					logger.error("NoSuchMethodException", e);
				} catch (IllegalArgumentException e) {
				} 
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider#getIndexedContainer()
	 */
	@Override
	public IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults) {
		if (searchPanel.getType().equals(WkfHistorySearchPanel.TYPE_DEFAULT)) {
			return super.getIndexedContainer(firstResult, maxResults);
		}
		
		IndexedContainer indexedContainer = new IndexedContainer();
		
		if (historyDataProvider != null) {
			try {
				BaseRestrictions<WkfHistory> histoRestrictions = getPagedDefinition().getRestrictions();
				entities = historyDataProvider.fetchCustomEntities(histoRestrictions, customEntity, firstResult, maxResults);
			} catch (Exception e) {
				logger.error("Error at FecthEntities", e);
				return indexedContainer;
			}
		} else {
			entities = new ArrayList<Entity>();
		}
		
		try {
			buildColumnsInIndexedContainer(indexedContainer);
			buildCustomRowsInIndexedContainer(indexedContainer);
			
		} catch (Exception e) {
			logger.error("Error at buildColumnsInIndexedContainer/buildRowsInIndexedContainer", e);
		}
		return indexedContainer;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider#getTotalRecords()
	 */
	@Override
	public long getTotalRecords() {
		BaseRestrictions<WkfHistory> histoRestrictions = getPagedDefinition().getRestrictions();
		
		if (searchPanel.getType().equals(WkfHistorySearchPanel.TYPE_DEFAULT)) {
			customEntity = null;
			
			return ENTITY_SRV.count(histoRestrictions);
		} else if (searchPanel.getCustomValue() != null) {
			if (historyDataProvider == null) {
				throw new IllegalArgumentException("[HistoryDataProvider] is null");
			}
			customEntity = searchPanel.getCustomValue();
			
			return historyDataProvider.getTotalRecords(histoRestrictions, customEntity);
		}
		return 0;
	}
	
}
