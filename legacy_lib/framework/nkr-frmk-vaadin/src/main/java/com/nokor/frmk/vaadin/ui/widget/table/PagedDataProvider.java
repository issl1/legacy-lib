package com.nokor.frmk.vaadin.ui.widget.table;

import java.io.Serializable;

import org.seuksa.frmk.model.entity.Entity;

import com.vaadin.data.util.IndexedContainer;

/**
 * @author ly.youhort
 */
public interface PagedDataProvider<T extends Entity> extends Serializable {

	/**
	 * @return
	 */
	PagedDefinition<T> getPagedDefinition();
	
	/**
	 * @return
	 */
	IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults);
	
	/**
	 * @return
	 */
	long getTotalRecords();
	
}
