package com.nokor.frmk.vaadin.ui.widget.tabsheet.event;


/**
 * @author ly.youhort
 *
 */
public interface TabsheetListener {

	/**
	 * @param event
     */
    void add(TabsheetAddEvent event);

    /**
     * @param event
     */
    void edit(TabsheetEditEvent event);
    
    /**
     * @param event
     */
    void refresh(TabsheetRefreshEvent event);
    
    /**
     * @param event
     */
    void delete(TabsheetDeleteEvent event);
    
    /**
     * @param event
     */
    void print(TabsheetPrintEvent event);
    
}