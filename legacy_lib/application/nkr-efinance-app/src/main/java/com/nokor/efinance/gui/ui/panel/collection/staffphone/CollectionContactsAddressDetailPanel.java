package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.List;
import java.util.Map;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;

/**
 * Contact address details in collection left panel
 * @author uhout.cheng
 */
public class CollectionContactsAddressDetailPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -6696806398570427090L;
	
	private ContactAddressTable borrowerAddressTable;
	private ContactAddressTable guarantorAddressTable;
	
	/**
	 * 
	 */
	public CollectionContactsAddressDetailPanel() {
		setMargin(true);
		setSpacing(true);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		borrowerAddressTable = new ContactAddressTable("borrower");
		guarantorAddressTable = new ContactAddressTable("guarantor");
		
		addComponent(borrowerAddressTable);
		addComponent(guarantorAddressTable);
	}
	
	/**
	 * 
	 * @param addresses
	 */
	public void assignValues(Map<EApplicantType, List<Address>> addresses) {
		borrowerAddressTable.assignValues(addresses, EApplicantType.C);
		guarantorAddressTable.assignValues(addresses, EApplicantType.G);
	}
	
}
