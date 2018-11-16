package com.nokor.efinance.gui.ui.panel.collection;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.filter.AbstractContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.filter.DefaultColContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.ColContractDetailPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author uhout.cheng
 */
public class ColContractsHolderPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private AbstractContractFilterPanel filterPanel;
	private ColContractTablePanel contractTabPanel;
	private ColContractDetailPanel detailPanel;
	
	private static AbstractContractFilterPanel publicFilterPanel;
	private static ColContractTablePanel publicContractTabPanel;
	
	/**
	 * @param dashboardPanel
	 */
	public ColContractsHolderPanel() {
		setMargin(true);
		detailPanel = new ColContractDetailPanel();
		contractTabPanel = new ColContractTablePanel(detailPanel);		
		filterPanel = new DefaultColContractFilterPanel(contractTabPanel);
		detailPanel.setVisibleLayout(false);	
	}
	
	/**
	 */
	@SuppressWarnings("static-access")
	protected void init(AbstractContractFilterPanel filterPanel, ColContractTablePanel contractTabPanel, ColContractDetailPanel detailPanel) {		
		
		this.detailPanel = detailPanel;
		this.contractTabPanel = contractTabPanel;		
		this.filterPanel = filterPanel;
		
		this.publicFilterPanel = this.filterPanel;
		this.publicContractTabPanel = this.contractTabPanel;
		
		HorizontalLayout mainLayout = ComponentLayoutFactory.getHorizontalLayout(false, false);
		VerticalLayout contractTabLayout = new VerticalLayout();
		contractTabLayout.setWidth(692, Unit.PIXELS);
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
	
	/**
	 * Refresh Table When click button save Result or CollectionAction in Tab Result
	 */
	public static void refreshTable() {
		publicContractTabPanel.refresh(publicFilterPanel.getRestrictions());
	}
}
