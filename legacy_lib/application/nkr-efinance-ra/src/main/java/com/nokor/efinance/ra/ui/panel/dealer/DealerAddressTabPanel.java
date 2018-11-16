package com.nokor.efinance.ra.ui.panel.dealer;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;

/**
 * 
 * @author buntha.chea
 *
 */
public class DealerAddressTabPanel extends AbstractControlPanel implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7780196605397106950L;
	
	private DealerAddressesPanel dealerContactAddressPanel;
	
	public DealerAddressTabPanel() {
		setMargin(true);
        dealerContactAddressPanel = new DealerAddressesPanel();
        addComponent(dealerContactAddressPanel);
	}

	/**
	 * Assign Value
	 * @param dealer
	 */
	public void assignValue(Dealer dealer) {
		if (dealer != null) {
			dealerContactAddressPanel.setDealer(dealer);
			dealerContactAddressPanel.assignValue();
		}
	}

}
