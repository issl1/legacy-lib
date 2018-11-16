package com.nokor.efinance.ra.ui.panel.dealer.contact;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.ra.ui.panel.dealer.DealerAddressTabPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;


/**
 * 
 * @author uhout.cheng
 */
public class DealerAddressContactTab extends TabSheet {

	/** */
	private static final long serialVersionUID = 4606551376101502933L;
	
	private DealerAddressTabPanel dealerAddressTabPanel;
	private DealerContactTabPanel dealerContactTabPanel; 
	
	/**
	 * 
	 */
	public DealerAddressContactTab() {
		dealerAddressTabPanel = new DealerAddressTabPanel();
        dealerContactTabPanel = new DealerContactTabPanel();
		
		this.addTab(dealerAddressTabPanel, I18N.message("address"));
        this.addTab(dealerContactTabPanel, I18N.message("contact"));
        this.setVisible(false);
	}

	/**
	 * 
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		this.setSelectedTab(dealerAddressTabPanel);
		if (dealer != null) {
			this.setVisible(true);
			dealerAddressTabPanel.assignValue(dealer);
			dealerContactTabPanel.assignValue(dealer);
		} else {
			this.setVisible(false);
		}
	}
	
}
