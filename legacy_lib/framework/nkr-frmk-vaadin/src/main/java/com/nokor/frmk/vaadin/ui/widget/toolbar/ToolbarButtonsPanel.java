package com.nokor.frmk.vaadin.ui.widget.toolbar;

import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;

/**
 * Navigation panel
 * @author ly.youhort
 */
public class ToolbarButtonsPanel extends HorizontalLayout {

	private static final long serialVersionUID = 504496987179901681L;

	private HorizontalLayout navigationLayout = null;
		
	/**
	 * Navigation panel
	 */
	public ToolbarButtonsPanel() {
		 addStyleName("sidebar");
         setWidth("100%");
         navigationLayout = new HorizontalLayout();
         navigationLayout.addStyleName("navigation");
         navigationLayout.addStyleName("no-vertical-drag-hints");
         navigationLayout.addStyleName("no-horizontal-drag-hints");
         addComponent(navigationLayout);
         setExpandRatio(navigationLayout, 1);
	}
	
	/**
	 * Add a new navigation button
	 * @param button
	 * @return
	 */
	public void addButton(Component button) {
		navigationLayout.addComponent(button);
		
	}
			
	/**
	 * Clear style css
	 */
	protected void clearSelection() {
        for (Iterator<Component> it = navigationLayout.iterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                ((DragAndDropWrapper) next).iterator().next().removeStyleName("selected");
            }
        }
    }

	/**
	 * @return the navigationLayout
	 */
	public HorizontalLayout getNavigationLayout() {
		return navigationLayout;
	}
}
