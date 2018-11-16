package com.nokor.frmk.vaadin.ui.widget.table.listener;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.table.event.PagedTableChangeEvent;



/**
 * Page change listener
 * @author ly.youhort
 *
 */
public interface PageChangeListener <T extends Entity> {
    public void pageChanged(PagedTableChangeEvent<T> event);
}
