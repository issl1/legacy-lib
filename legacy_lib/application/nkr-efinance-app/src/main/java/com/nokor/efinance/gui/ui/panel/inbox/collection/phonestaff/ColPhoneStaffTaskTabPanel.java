package com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.task.ColPhoneStaffTaskPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
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
public class ColPhoneStaffTaskTabPanel extends AbstractControlPanel implements ClickListener {

	private static final long serialVersionUID = 284726889749322871L;
	
	private Button btnMonthlyPressing;
	private Button btnDailyPressing;
	
	private VerticalLayout mainContent;
	private ColPhoneStaffTaskPanel colPhoneStaffTaskPanel;
	
	public ColPhoneStaffTaskTabPanel() {
		colPhoneStaffTaskPanel = new ColPhoneStaffTaskPanel();
																	
		btnMonthlyPressing = ComponentLayoutFactory.getDefaultButton("monthly.pressing", null, 130);
		btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
		btnMonthlyPressing.removeClickShortcut();
		btnMonthlyPressing.addClickListener(this);
		
		btnDailyPressing =  ComponentLayoutFactory.getDefaultButton("daily.pressing", null, 130);
		btnDailyPressing.removeClickShortcut();
		btnDailyPressing.addClickListener(this);
		
		mainContent = new VerticalLayout(colPhoneStaffTaskPanel);

		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.addComponent(btnMonthlyPressing);
		buttonLayout.addComponent(btnDailyPressing);
		buttonLayout.setWidth("140px");

		HorizontalSplitPanel mainLayout = new HorizontalSplitPanel(buttonLayout, mainContent);
		mainLayout.setLocked(true);
		mainLayout.addStyleName(Reindeer.SPLITPANEL_SMALL);
		mainLayout.setSplitPosition(14, Unit.PERCENTAGE);
		mainLayout.setHeight("636px");
		
		addComponent(mainLayout);
	}
	
	/**
	 * 
	 */
	private void setDefaultBackground(){
		btnMonthlyPressing.setStyleName("btn btn-success button-small");
		btnDailyPressing.setStyleName("btn btn-success button-small");
	}
	
	/**
	 * 
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		setDefaultBackground();
		mainContent.removeAllComponents();
		if (event.getButton() == btnMonthlyPressing) {
			btnMonthlyPressing.setStyleName("btn btn-success button-small change-bg");
			mainContent.addComponent(colPhoneStaffTaskPanel);
		} else if (event.getButton() == btnDailyPressing) {
			btnDailyPressing.setStyleName("btn btn-success button-small change-bg");
		}
	}
}
