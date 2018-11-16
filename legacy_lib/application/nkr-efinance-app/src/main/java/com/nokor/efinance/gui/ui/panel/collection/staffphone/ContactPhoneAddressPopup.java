package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.List;
import java.util.Map;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Display contact phone & address
 * @author uhout.cheng
 */
public class ContactPhoneAddressPopup extends Window {

	/** */
	private static final long serialVersionUID = -899593663427668591L;
	
	private ContactPhoneTable phoneTable;
	private ContactAddressTable borrowerAddressTable;
	private ContactAddressTable guarantorAddressTable;
	
	/**
	 * 
	 */
	public ContactPhoneAddressPopup() {
		setCaption(I18N.message("contacts"));
		setModal(true);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		phoneTable = new ContactPhoneTable();
		borrowerAddressTable = new ContactAddressTable("borrower");
		guarantorAddressTable = new ContactAddressTable("guarantor");
		
		VerticalLayout addressLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		addressLayout.addComponent(borrowerAddressTable);
		addressLayout.addComponent(guarantorAddressTable);
		
		Panel addressPanel = new Panel(addressLayout);
		addressPanel.setCaption(I18N.message("addresses"));
		
		VerticalLayout mainVerLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainVerLayout.addComponent(phoneTable);
		mainVerLayout.addComponent(addressPanel);
	
		setContent(mainVerLayout);
	}
	
	/**
	 * 
	 * @param phones
	 * @param addresses
	 */
	public void assignValues(Map<EApplicantType, List<String>> phones, 
			Map<EApplicantType, List<Address>> addresses) {
		phoneTable.assignValues(phones);
		borrowerAddressTable.assignValues(addresses, EApplicantType.C);
		guarantorAddressTable.assignValues(addresses, EApplicantType.G);
	}

}
