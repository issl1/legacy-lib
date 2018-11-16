package com.nokor.efinance.gui.ui.panel.contract.asset.registration.popup;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.asset.AssetPanel;
import com.vaadin.ui.Window;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AssetRegistrationDetailPopupPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5320074186656241591L;
	
	private AssetPanel assetPanel;
	
	/**
	 * Registration Detail Pop up Panel
	 */
	public AssetRegistrationDetailPopupPanel() {
		setCaption(I18N.message("asset.details"));
		setModal(true);
		assetPanel = new AssetPanel();
		setContent(assetPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			assetPanel.assignValues(contract);
		}
	}
}
