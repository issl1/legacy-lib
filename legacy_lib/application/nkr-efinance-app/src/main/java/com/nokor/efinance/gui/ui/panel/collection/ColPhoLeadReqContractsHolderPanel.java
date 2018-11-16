package com.nokor.efinance.gui.ui.panel.collection;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.filter.AbstractColPhoLeadReqConFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.filter.ColFlagContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.staffphone.ColContractDetailPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author uhout.cheng
 */
public class ColPhoLeadReqContractsHolderPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = 9187261039959045395L;
	
	private AbstractColPhoLeadReqConFilterPanel filterPanel;
	private ColPhoLeadReqContractTablePanel contractTabPanel;
	private ColContractDetailPanel detailPanel;
	
	/**
	 * 
	 * @param isFlagRequest
	 */
	public ColPhoLeadReqContractsHolderPanel(boolean isFlagRequest) {
		setMargin(true);
		detailPanel = new ColContractDetailPanel();
		contractTabPanel = new ColPhoLeadReqContractTablePanel(detailPanel, isFlagRequest);		
		filterPanel = new ColFlagContractFilterPanel(contractTabPanel);
		detailPanel.setVisibleLayout(false);		
	}
	
	/**
	 */
	protected void init(AbstractColPhoLeadReqConFilterPanel filterPanel, ColPhoLeadReqContractTablePanel contractTabPanel, ColContractDetailPanel detailPanel) {		
		
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
