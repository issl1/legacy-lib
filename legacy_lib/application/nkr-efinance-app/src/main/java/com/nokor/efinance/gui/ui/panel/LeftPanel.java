package com.nokor.efinance.gui.ui.panel;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 *
 */
public class LeftPanel extends VerticalLayout {

	private static final long serialVersionUID = -9118393220129022654L;

	public LeftPanel() {
		 setWidth(200, Unit.PIXELS);
         setHeight("100%");
         
         Accordion quickNavigationPanel = new Accordion();
         quickNavigationPanel.setHeight(100.0f, Unit.PERCENTAGE);
         
         final VerticalLayout historyLayout = new VerticalLayout();
         final VerticalLayout historyLayout2 = new VerticalLayout();
         final VerticalLayout historyLayout3 = new VerticalLayout();
         final VerticalLayout historyLayout4 = new VerticalLayout();
         
         quickNavigationPanel.addTab(historyLayout, I18N.message("history"));
         quickNavigationPanel.addTab(historyLayout2, I18N.message("history"));
         quickNavigationPanel.addTab(historyLayout3, I18N.message("history"));
         quickNavigationPanel.addTab(historyLayout4, I18N.message("history"));
                  
         addComponent(quickNavigationPanel);
         setExpandRatio(quickNavigationPanel, 1);
	}
}
