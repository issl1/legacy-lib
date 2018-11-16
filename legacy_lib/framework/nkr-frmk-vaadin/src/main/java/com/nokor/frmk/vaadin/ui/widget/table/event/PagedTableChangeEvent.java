package com.nokor.frmk.vaadin.ui.widget.table.event;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.table.PagedTable;

/**
 * @author ly.youhort
 *
 */
public class PagedTableChangeEvent<T extends Entity> {

    private final PagedTable<T> table;

    /**
     * 
     * @param table
     */
    public PagedTableChangeEvent(PagedTable<T> table) {
        this.table = table;
    }

    /**
     * Get paged table
     * @return
     */
    public PagedTable<T> getTable() {
        return table;
    }

    /**
     * Get current page index
     * @return
     */
    public int getCurrentPage() {
        return table.getCurrentPage();
    }

    /**
     * Get total amount of pages
     * @return
     */
    public int getTotalAmountOfPages() {
        return table.getTotalAmountOfPages();
    }
}
