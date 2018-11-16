package com.nokor.frmk.vaadin.ui.widget.table.impl;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.table.PagedTable;
import com.vaadin.ui.Table;

/**
 * 
 * @author prasnar
 *
 * @param <T>
 */
public abstract class AbstractPagedTable<T extends Entity> extends Table implements PagedTable<T> {
    /** */
	private static final long serialVersionUID = 1596654074145579117L;

	/**
	 * 
	 * @param caption
	 */
	public AbstractPagedTable(String caption) {
		super(caption);
	}
}