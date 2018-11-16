package com.nokor.efinance.gui.ui.panel.collection.callcenter.staff;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.VerticalLayout;

/**
 * Call center staff panel 
 * @author uhout.cheng
 */
public class CallCenterStaffPanel extends AbstractControlPanel implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 418962242480929239L;

	private CollectionContractTablePanel collectionContractTablePanel;
	private CallCenterStaffFilterPanel filterPanel;
	
	/**
	 * 
	 */
	public CallCenterStaffPanel() {
		setMargin(true);
	
		collectionContractTablePanel = new CollectionContractTablePanel(null);
		filterPanel = new CallCenterStaffFilterPanel(collectionContractTablePanel);
		collectionContractTablePanel.refresh(filterPanel.getRestrictions());
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		if (ProfileUtil.isCallCenterStaff()) {
			mainLayout.addComponent(filterPanel);
			mainLayout.addComponent(collectionContractTablePanel);	
		}
	
		addComponent(mainLayout);
	}
	
	public void refresh() {
		collectionContractTablePanel.refresh(filterPanel.getRestrictions());
	}
}
