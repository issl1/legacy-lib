package com.nokor.efinance.gui.ui.panel.inbox.collection.delayedstaff;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColDelayedStaffTaskTabPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -921312562759170581L;
	
	private Button btnMonthlyPressing;
	private VerticalLayout contentLayout;
	
	private ColDelayedStaffMonthlyPressPanel colFieldStaffMonthlyPressPanel;
	
	public ColDelayedStaffTaskTabPanel() {
		
		btnMonthlyPressing = ComponentLayoutFactory.getDefaultButton("monthly.pressing", null, 140);
		btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
		btnMonthlyPressing.removeClickShortcut();
		
		colFieldStaffMonthlyPressPanel = new ColDelayedStaffMonthlyPressPanel();
		contentLayout = new VerticalLayout(colFieldStaffMonthlyPressPanel);

		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(btnMonthlyPressing);
		
		HorizontalSplitPanel mainLayout = new HorizontalSplitPanel(buttonLayout, contentLayout);
		mainLayout.setLocked(true);
		mainLayout.addStyleName(Reindeer.SPLITPANEL_SMALL);
		mainLayout.setSplitPosition(15, Unit.PERCENTAGE);
		mainLayout.setHeight(636, Unit.PIXELS);
		
		addComponent(mainLayout);
	}
}
