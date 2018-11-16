package com.nokor.efinance.gui.ui.panel.inbox.collection.fieldstaff;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
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
public class ColFieldStaffTaskTabPanel extends VerticalLayout implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -921312562759170581L;
	
	private Button btnMonthlyPressing;
	private Button btnAssistances;
	private VerticalLayout contentLayout;
	
	private ColFieldStaffMonthlyPressPanel colFieldStaffMonthlyPressPanel;
	private ColFieldStaffAssistancesPanel colFieldStaffAssistancesPanel;
	
	public ColFieldStaffTaskTabPanel() {
		
		btnMonthlyPressing = ComponentLayoutFactory.getDefaultButton("monthly.pressing", null, 140);
		btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
		btnMonthlyPressing.removeClickShortcut();
		btnMonthlyPressing.addClickListener(this);
		
		btnAssistances = ComponentLayoutFactory.getDefaultButton("assistances", null, 140);
		btnAssistances.removeClickShortcut();
		btnAssistances.addClickListener(this);
		
		colFieldStaffMonthlyPressPanel = new ColFieldStaffMonthlyPressPanel();
		contentLayout = new VerticalLayout(colFieldStaffMonthlyPressPanel);
		
		colFieldStaffAssistancesPanel = new ColFieldStaffAssistancesPanel();
		
		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(btnMonthlyPressing);
		buttonLayout.addComponent(btnAssistances);
		
		HorizontalSplitPanel mainLayout = new HorizontalSplitPanel(buttonLayout, contentLayout);
		mainLayout.setLocked(true);
		mainLayout.addStyleName(Reindeer.SPLITPANEL_SMALL);
		mainLayout.setSplitPosition(15, Unit.PERCENTAGE);
		mainLayout.setHeight(636, Unit.PIXELS);
		
		addComponent(mainLayout);
	}
	
	/**
	 * 
	 */
	private void setDefaultBackground(){
		btnMonthlyPressing.setStyleName("btn btn-success button-small");
		btnAssistances.setStyleName("btn btn-success button-small");
	}
	
	/**
	 * 
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		contentLayout.removeAllComponents();
		setDefaultBackground();
		if (event.getButton() == btnMonthlyPressing) {
			btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
			contentLayout.addComponent(colFieldStaffMonthlyPressPanel);
		} else if (event.getButton() == btnAssistances) {
			btnAssistances.setStyleName("btn btn-success button-small change-bg");
			contentLayout.addComponent(colFieldStaffAssistancesPanel);
		}
	}

}
