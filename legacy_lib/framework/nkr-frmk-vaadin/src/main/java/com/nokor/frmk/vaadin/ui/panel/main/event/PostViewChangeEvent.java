/**
 * 
 */
package com.nokor.frmk.vaadin.ui.panel.main.event;

import com.nokor.frmk.vaadin.ui.panel.main.ViewType;

/**
 * @author prasnar
 *
 */
public class PostViewChangeEvent {
	private final ViewType view;

	/**
	 * 
	 * @param view
	 */
    public PostViewChangeEvent(final ViewType view) {
        this.view = view;
    }

    /**
     * 
     * @return
     */
    public ViewType getView() {
        return view;
    }
}
