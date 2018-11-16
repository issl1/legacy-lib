package com.nokor.efinance.ra.ui.panel.dealer.contact;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class DealerContactTabPanel extends VerticalLayout {
	
	private static final long serialVersionUID = 6635987943587402568L;
	
	private DealerContactInfoPanel dealerContactInfoPanel;

	public DealerContactTabPanel() {
		setMargin(true);
		setSpacing(true);
		
        dealerContactInfoPanel = new DealerContactInfoPanel();
		
		addComponent(dealerContactInfoPanel);
	}
	
	/**
	 * AssignValue
	 */
	public void assignValue(Dealer dealer) {
		if (dealer != null) {
			dealerContactInfoPanel.assignValues(dealer);
		}
	}
	/**
	 * Reset
	 */
	public void reset() {
		dealerContactInfoPanel.reset();
	}

}
