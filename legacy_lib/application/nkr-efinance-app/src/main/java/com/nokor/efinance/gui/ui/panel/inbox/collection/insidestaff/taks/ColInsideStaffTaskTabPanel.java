package com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks.assistances.ColInsideStaffAssistancesPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.insidestaff.taks.monthlypressing.ColInsideStaffMonthlyPressingPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColInsideStaffTaskTabPanel extends VerticalLayout implements ClickListener {

	
	private static final long serialVersionUID = -5087068793564727285L;
	
	private Button btnMonthlyPressing;
	private Button btnAssistances;
	
	private ColInsideStaffMonthlyPressingPanel colInsideStaffMonthlyPressingPanel;
	private ColInsideStaffAssistancesPanel colInsideStaffAssistancesPanel;
	private VerticalLayout mainContent;

	public ColInsideStaffTaskTabPanel() {
		
		colInsideStaffMonthlyPressingPanel = new ColInsideStaffMonthlyPressingPanel();
		colInsideStaffAssistancesPanel = new ColInsideStaffAssistancesPanel();
		
		mainContent = new VerticalLayout(colInsideStaffMonthlyPressingPanel);
		
		btnMonthlyPressing = ComponentLayoutFactory.getDefaultButton("monthly.pressing", null, 140);
		btnMonthlyPressing.removeClickShortcut();
		btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
		btnMonthlyPressing.addClickListener(this);
		
		btnAssistances = ComponentLayoutFactory.getDefaultButton("assistances", null, 140);
		btnAssistances.removeClickShortcut();
		btnAssistances.addClickListener(this);
		
		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(btnMonthlyPressing);
		buttonLayout.addComponent(btnAssistances);
		
		
		HorizontalSplitPanel mainLayout = new HorizontalSplitPanel(buttonLayout, mainContent);
		mainLayout.setLocked(true);
		mainLayout.addStyleName(Reindeer.SPLITPANEL_SMALL);
		mainLayout.setSplitPosition(15, Unit.PERCENTAGE);
		mainLayout.setHeight(636, Unit.PIXELS);
		
		addComponent(mainLayout);
	}
	
	private void setDefaultBackground(){
		btnMonthlyPressing.setStyleName("btn btn-success button-small");
		btnAssistances.setStyleName("btn btn-success button-small");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		setDefaultBackground();
		mainContent.removeAllComponents();
		if (event.getButton() == btnMonthlyPressing) {
			btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
			mainContent.addComponent(colInsideStaffMonthlyPressingPanel);
		} else if (event.getButton() == btnAssistances) {
			btnAssistances.setStyleName("btn btn-success button-small change-bg");
			mainContent.addComponent(colInsideStaffAssistancesPanel);
		}
		
	}
	
}
