package com.nokor.frmk.vaadin.ui.widget.table.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.PropertyColumnRenderer;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 * 
 * @author ly.youhort
 */
public class EntityPagedDataProvider<T extends Entity> implements AppServicesHelper, PagedDataProvider<T> {

	private static final long serialVersionUID = -1478215476902295804L;

	protected static Logger logger = LoggerFactory.getLogger(EntityPagedDataProvider.class);

	private PagedDefinition<T> pagedDefinition;
	private List<T> entities;
	
	/**
	 * 
	 */
	public EntityPagedDataProvider() {
	}
			
	/**
	 * @return the pagedDefinition
	 */
	public PagedDefinition<T> getPagedDefinition() {
		return pagedDefinition;
	}
	
	/**
	 * @param pagedDefinition the pagedDefinition to set
	 */
	public void setPagedDefinition(PagedDefinition<T> pagedDefinition) {
		this.pagedDefinition = pagedDefinition;
	}
	
	/**
	 * @return the entities
	 */
	public List<T> getEntities() {
		return entities;
	}
	
	/**
	 * 
	 * @param firstResult
	 * @param maxResults
	 */
	private void fetchEntities(Integer firstResult, Integer maxResults) {
		BaseRestrictions<T> baseRestrictions = pagedDefinition.getRestrictions();
		baseRestrictions.setFirstResult(firstResult);
		baseRestrictions.setMaxResults(maxResults);
		entities = ENTITY_SRV.list(baseRestrictions);
	}
	
	/**
	 * 
	 * @param indexedContainer
	 */
	private void buildColumnsInIndexedContainer(IndexedContainer indexedContainer) {
		for (ColumnDefinition columnDefinition : pagedDefinition.getColumnDefinitions()) {
			indexedContainer.addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
		}
	}

	/**
	 * 
	 * @param indexedContainer
	 */
	private void buildRowsInIndexedContainer(IndexedContainer indexedContainer) {
		for (Entity entity : entities) {
			Item item = indexedContainer.addItem(entity.getId());
			
			// draw the grid rows with a Renderer
			if (pagedDefinition.getRowRenderer() != null) {
				pagedDefinition.getRowRenderer().renderer(item, entity);
			} 
			// draw the grid rows with the entity fields as columns
			else {
				for (ColumnDefinition columnDefinition : pagedDefinition.getColumnDefinitions()) {
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
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider#getIndexedContainer()
	 */
	@Override
	public IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
			fetchEntities(firstResult, maxResults);
		} catch (Exception e) {
			logger.error("Error at FecthEntities", e);
			return indexedContainer;
		}
		
		try {
			buildColumnsInIndexedContainer(indexedContainer);
			buildRowsInIndexedContainer(indexedContainer);
			
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
		return ENTITY_SRV.count(pagedDefinition.getRestrictions());
	}


	
}
