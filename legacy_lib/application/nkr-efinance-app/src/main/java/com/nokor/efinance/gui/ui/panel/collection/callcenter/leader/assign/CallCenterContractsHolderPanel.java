package com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.staff.CallCenterStaffPanel;
import com.nokor.efinance.gui.ui.panel.collection.filter.callcenter.AbstractCallCenterContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.filter.callcenter.CallCenterContractFilterPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author uhout.cheng
 */
public class CallCenterContractsHolderPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = 2613695794557368330L;
	
	private AbstractCallCenterContractFilterPanel filterPanel;
	private CallCenterLeaderContractTablePanel contractTabPanel;
	private CallCenterStaffPanel detailPanel;
	
	/**
	 * 
	 */
	public CallCenterContractsHolderPanel() {
		setMargin(true);
		detailPanel = new CallCenterStaffPanel();
		contractTabPanel = new CallCenterLeaderContractTablePanel(detailPanel);		
		filterPanel = new CallCenterContractFilterPanel(contractTabPanel);
	}
	
	/**
	 * 
	 * @param filterPanel
	 * @param contractTabPanel
	 * @param detailPanel
	 */
	protected void init(AbstractCallCenterContractFilterPanel filterPanel, CallCenterLeaderContractTablePanel contractTabPanel, CallCenterStaffPanel detailPanel) {		
		
		this.detailPanel = detailPanel;
		this.contractTabPanel = contractTabPanel;		
		this.filterPanel = filterPanel;
		
		HorizontalLayout mainLayout = ComponentLayoutFactory.getHorizontalLayout(false, false);
		VerticalLayout contractTabLayout = new VerticalLayout();
		contractTabLayout.setWidth(600, Unit.PIXELS);
		contractTabLayout.setSpacing(true);
		contractTabLayout.addComponent(filterPanel);
		contractTabLayout.addComponent(contractTabPanel);
		mainLayout.addComponent(contractTabLayout);
		mainLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		mainLayout.addComponent(detailPanel);
		
		Panel panel = new Panel(new VerticalLayout(mainLayout));
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(panel);
	}
	
	/**
	 */
	public void refresh() {
		contractTabPanel.refresh(filterPanel.getRestrictions());
	}	
}
