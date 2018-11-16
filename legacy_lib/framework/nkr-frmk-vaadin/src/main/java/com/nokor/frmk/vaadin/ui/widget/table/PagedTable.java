package com.nokor.frmk.vaadin.ui.widget.table;

import org.seuksa.frmk.model.entity.Entity;


/**
 * Paged table
 * @author ly.youhort
 */
public interface PagedTable<T extends Entity> {
	
    /**
     * Get current page index
     * @return
     */
    int getCurrentPage();

    /**
     * Get total amount of pages
     * @return
     */
    int getTotalAmountOfPages();
    
}