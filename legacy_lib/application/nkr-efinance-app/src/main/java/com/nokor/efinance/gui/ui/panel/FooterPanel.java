package com.nokor.efinance.gui.ui.panel;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Footer panel
 * @author ly.youhort
 */
public class FooterPanel extends VerticalLayout {

	private static final long serialVersionUID = 2001751364295281369L;
	
	public static final int PANEL_HEIGHT = 35;
	
	public FooterPanel() {
		setHeight(PANEL_HEIGHT, Unit.PIXELS);
		HorizontalLayout mainPanel = new HorizontalLayout();
		mainPanel.setSizeFull();		        
        addComponent(mainPanel);        
	}	
}
